package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;



import java.util.ArrayList;
import java.util.List;


import com.example.myapplication.adapter.ChatListAdapter;
import com.example.myapplication.adapter.CourseGridAdapter;
import com.example.myapplication.adapter.ShowListAdapter;
import com.example.myapplication.adapter.ShowUpListAdapter;
import com.example.myapplication.data.DBtestActivity;
import com.example.myapplication.data.UserData;
import com.example.myapplication.util.ToastUtil;
import com.example.myapplication.viewpages.UpdateMineView;

import org.litepal.LitePal;

public class HomeActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private RadioGroup mRadioGroup;
    private RadioButton tab1,tab2,tab3,tab4;  //四个单选按钮
    private List<View> mViews;   //存放四个视图
    private String username;

    UpdateMineView updateMineView;

    private ListView lv_chat;
    private ListView lv_show;
    private GridView gv_course;
    private PopupWindow mPop,mPop0;
    private UserData user;
    private List<UserData> users= LitePal.findAll(UserData.class);
    private List<UserData> friends=new ArrayList<>();
    Context mContext=HomeActivity.this;


    //课程
  //  private VideoView vvCourseVideo;

    private final int UPSHOW=3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        username=getIntent().getStringExtra("username");



        //去除默认标题栏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.hide();
        }

        initView();//初始化数据

        //对单选按钮进行监听，选中、未选中
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_course:
                        mViewPager.setCurrentItem(0);
                        break;
                    case R.id.rb_circle:
                        mViewPager.setCurrentItem(1);
                        break;
                    case R.id.rb_chat:
                        mViewPager.setCurrentItem(2);
                        break;
                    case R.id.rb_mine:
                        mViewPager.setCurrentItem(3);
                        break;
                }
            }
        });

    }





    private void initView() {
        //初始化控件
        mViewPager=findViewById(R.id.viewpager);
        mRadioGroup=findViewById(R.id.rg_tab);
        tab1=findViewById(R.id.rb_course);
        tab2=findViewById(R.id.rb_circle);
        tab3=findViewById(R.id.rb_chat);
        tab4=findViewById(R.id.rb_mine);

        mViews=new ArrayList<View>();//加载，添加视图
        mViews.add(LayoutInflater.from(this).inflate(R.layout.fragment_course,null));
        mViews.add(LayoutInflater.from(this).inflate(R.layout.fragment_circle,null));
        mViews.add(LayoutInflater.from(this).inflate(R.layout.fragment_chat,null));
        mViews.add(LayoutInflater.from(this).inflate(R.layout.fragment_mine,null));

        //后期更改
//                        for(UserData user :users) {
//                            if (user.getUsername().equals(username)) {
//
//                                friends.add(user);
//                                friends = user.getFriends();
//                            }
//
//                        }



        //手动添加
        for(UserData user :users) {
            friends.add(user);

        }

        for (UserData user : users) {
            if (user.getUsername().equals(username)) {
                this.user = user;
            }
        }

        //获取个人页面和用户名，用于页面刷新
        updateMineView=new UpdateMineView(mViews.get(3),username);
        //默认进入页面更新

        mViewPager.setAdapter(new MyViewPagerAdapter());//设置一个适配器
        //对viewpager监听，让分页和底部图标保持一起滑动
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override   //让viewpager滑动的时候，下面的图标跟着变动
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        tab1.setChecked(true);
                        tab2.setChecked(false);
                        tab3.setChecked(false);
                        tab4.setChecked(false);
                        updateCourse();

                        //点击浏览课程

                        //点击课程分类
                        ImageView ivCourseCategory=mViews.get(0).findViewById(R.id.iv_course_category);
                        ivCourseCategory.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                View view1 = getLayoutInflater().inflate(R.layout.fragment_course_category,null);
 //                               ListView lvCategory;
 //                               lvCategory=view1.findViewById(R.id.lv_course_category);
//                                lvCategory.setAdapter(new CategoryListAdapter( mContext));
//                                lvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                    @Override
//                                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//
//                                        Toast.makeText(HomeActivity.this, "点击pos:"+position, Toast.LENGTH_SHORT).show();
//                                        //position是组件在列表中的位置编号，0，1,2
//                                        //跳转事件
//                                    }
//                                });

                                mPop0 = new PopupWindow(view1,ivCourseCategory.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
                                mPop0.setFocusable(true);    //控制点击按钮可以消失
                                mPop0.setOutsideTouchable(true);//控制在外侧触摸可以消失
                                mPop0.setBackgroundDrawable(new BitmapDrawable()); //加上这一行，否则我这里实现不了上述功能
                                //mPop.setAnimationStyle();//设置动画
                                mPop0.showAsDropDown(ivCourseCategory);
                            }
                        });




                        //点击添加课程信息
                        ImageView ivAddCourse=mViews.get(0).findViewById(R.id.iv_add_course);
                        ivAddCourse.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent=new Intent(HomeActivity.this,AddCourseActivity.class);
                               intent.putExtra("username",username);//传输用户名
                                startActivity(intent);
                            }
                        });

                        break;
                    case 1:
                        tab1.setChecked(false);
                        tab2.setChecked(true);
                        tab3.setChecked(false);
                        tab4.setChecked(false);
                        //！！！！！！
                        //there!!!
                        updateShow();

                        ImageView ivAddShow=mViews.get(1).findViewById(R.id.iv_add_show);
                        ivAddShow.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent=new Intent(HomeActivity.this,AddShowActivity.class);
                                intent.putExtra("username",username);//传输用户名
                                startActivityForResult(intent,UPSHOW);
                            }
                        });


                        break;
                    case 2:
                        tab1.setChecked(false);
                        tab2.setChecked(false);
                        tab3.setChecked(true);
                        tab4.setChecked(false);
                        ImageView ivAddFriend=mViews.get(2).findViewById(R.id.iv_addfriends);
                        ImageView ivFriendList=mViews.get(2).findViewById(R.id.iv_friends);
                        ivAddFriend.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                View view2 = getLayoutInflater().inflate(R.layout.layout_add,null);
                                //设置“搜索好友”的点击事件
                                TextView searchView = view2.findViewById(R.id.tv_search);
                                searchView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mPop.dismiss();
                                        //do something...

                                        ToastUtil.showMsg(HomeActivity.this,"搜索");
                                    }
                                });


                                //设置“搜索好友”的点击事件
                                TextView volunteer = view2.findViewById(R.id.tv_volunteer);
                                volunteer.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mPop.dismiss();
                                        //do something...
                                        ToastUtil.showMsg(HomeActivity.this,"志愿者列表");
                                    }
                                });

                                //设置“搜索好友”的点击事件
                                TextView student = view2.findViewById(R.id.tv_student);
                                student.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mPop.dismiss();
                                        //do something...
                                        ToastUtil.showMsg(HomeActivity.this,"学生列表");
                                    }
                                });

                                mPop = new PopupWindow(view2,ivAddFriend.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
                                mPop.setFocusable(true);    //控制点击按钮可以消失
                                mPop.setOutsideTouchable(true);//控制在外侧触摸可以消失
                                mPop.setBackgroundDrawable(new BitmapDrawable()); //加上这一行，否则我这里实现不了上述功能
                                //mPop.setAnimationStyle();//设置动画
                                mPop.showAsDropDown(ivAddFriend);
                            }
                        });
                        ivFriendList.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent =new Intent(HomeActivity.this,FriendListActivity.class);
                                intent.putExtra("name",username);
                                startActivity(intent);
                            }
                        });

                        lv_chat=findViewById(R.id.lv_chat);
                        ChatListAdapter chatListAdapter;
                        chatListAdapter = new ChatListAdapter( (List<UserData>) friends, mContext);
                        lv_chat.setAdapter(chatListAdapter);

                        //lv_chat.setAdapter(new ChatListAdapter(HomeActivity.this));//传参构造
                        lv_chat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                                Intent intent =new Intent(HomeActivity.this,ChatRoomActivity.class);
                                intent.putExtra("name",username);
                                startActivity(intent);
                                Toast.makeText(HomeActivity.this, "点击pos:"+position, Toast.LENGTH_SHORT).show();
                                //position是组件在列表中的位置编号，0，1,2
                                //跳转事件
                            }
                        });

                        lv_chat.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                            @Override
                            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Toast.makeText(HomeActivity.this, "长按pos:"+i, Toast.LENGTH_SHORT).show();
                                return true;
                            }
                        });

                        break;
                    case 3:
                        tab1.setChecked(false);
                        tab2.setChecked(false);
                        tab3.setChecked(false);
                        tab4.setChecked(true);
                        //更新MineView,传输当前view和用户名
                        updateMineView.updateMine();
                        //编辑信息,跳转activity
                        Button btn_exit=mViews.get(3).findViewById(R.id.btn_quit);
                        btn_exit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                finish();
                            }
                        });
                        TextView mtv_EditInfo=mViews.get(3).findViewById(R.id.tv_edit_info);

                        mtv_EditInfo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent =new Intent(HomeActivity.this, EditInfoActivity.class);
                                intent.putExtra("username",username);//传输用户名
                                startActivityForResult(intent,2);
                            }
                        });
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void updateCourse()
    {
        gv_course=mViews.get(0).findViewById(R.id.gv_course);
        gv_course.setAdapter(new CourseGridAdapter(HomeActivity.this));
        gv_course.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent =new Intent(HomeActivity.this,PlayCourseActivity.class);
                Bundle bundle=new Bundle();
                bundle.putInt("position",position);
                intent.putExtras(bundle);
                startActivity(intent);
               // Toast.makeText(HomeActivity.this, "点击pos:"+position, Toast.LENGTH_SHORT).show();
                //position是组件在列表中的位置编号，0，1,2
                //跳转事件
            }
        });

    }
    private void updateShow()
    {
        lv_show=mViews.get(1).findViewById(R.id.lv_show);
        ShowListAdapter showListAdapter;
//      List<UserShow> shows=new ArrayList<>();
        showListAdapter=new ShowListAdapter(this.user,HomeActivity.this);
        lv_show.setAdapter(showListAdapter);
    }
    private void updateShow2()
    {
        lv_show=mViews.get(1).findViewById(R.id.lv_show);
        ShowUpListAdapter showUpListAdapter;
//      List<UserShow> shows=new ArrayList<>();
        showUpListAdapter=new ShowUpListAdapter(this.user,HomeActivity.this);
        lv_show.setAdapter(showUpListAdapter);
    }

    //ViewPager适配器
    private class MyViewPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mViews.size();
        }


        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view==object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(mViews.get(position));
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            container.addView(mViews.get(position));
            return mViews.get(position);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            //跳转编辑信息页面返回
            case 2://请求码=2
                if (resultCode == 2) {//刷新页面，返回码=2
                    updateMineView.updateMine();
                }
                break;
            //跳转发布动态页面返回
            case 3://请求码=3
                if (resultCode == 3) {//刷新页面，返回码=2
                    updateShow2();
                }
                break;


            default:
        }
    }

}
