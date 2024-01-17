package com.StepLife.steplifeapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;

import com.StepLife.steplifeapp.TelephoneSign.TelephoneSignUp;
import com.StepLife.steplifeapp.UserProfile.User_ProfileActiviti;
import com.StepLife.steplifeapp.databinding.ActivityMainBinding;
import com.StepLife.steplifeapp.other.NetworkChangeListner;
import com.StepLife.steplifeapp.ui.Article;
import com.StepLife.steplifeapp.ui.dashboard.DashboardFragment;
import com.StepLife.steplifeapp.ui.home.HomeFragment;
import com.StepLife.steplifeapp.ui.notifications.NotificationsFragment;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding binding;
    HomeFragment homeFragment;
    DashboardFragment dashboardFragment;
    CardView HelperButton;
    public FirebaseAuth mAuth;
    CardView ProfileUserButton;
    ImageView imageviewprofile;
    NotificationsFragment notificationsFragment;
    NetworkChangeListner networkChangeListner = new NetworkChangeListner();
    Fragment active = null;

    BottomNavigationView navView;
    final FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navView = findViewById(R.id.nav_view);

        //Окно пользователя
        imageviewprofile = findViewById(R.id.imageviewprofile);
        //Кнопка профиля
        ProfileUserButton = findViewById(R.id.ProfileUserButton);
        ProfileUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser cUser = mAuth.getCurrentUser();
               if(cUser!=null)
                {
                    startActivity(new Intent(MainActivity.this, User_ProfileActiviti.class));
                }
                else
                {
                    startActivity(new Intent(MainActivity.this, TelephoneSignUp.class));
                }
            }
        });

        navView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.navigation_home:
                    if(homeFragment==null){
                        homeFragment = new HomeFragment();
                    }
                    setFragment(homeFragment, "1", 1);
                    break;
                case R.id.navigation_dashboard:
                    if(dashboardFragment==null){
                        dashboardFragment = new DashboardFragment();
                    }
                    setFragment(dashboardFragment, "2", 0);
                    break;
                case R.id.navigation_notifications:
                    if(notificationsFragment==null){
                        notificationsFragment = new NotificationsFragment();
                    }
                    setFragment(notificationsFragment, "3", 2);
                    break;
            }
            return true;
        });

        //Кнопка получения помощи
        HelperButton = findViewById(R.id.HelperButton);
        HelperButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Helper.class));
            }
        });

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }


    public void setNavigateFragment(int FragmentID){
        switch (FragmentID){
            case 0:
                Navigation.findNavController(this,R.id.nav_host_fragment_activity_main).navigate(R.id.homeFragment);
                break;
            default:
                break;
        }
    }
    public void setFragment(Fragment fragment, String tag, int position) {
        if (fm.findFragmentByTag(tag)!=null) {
            if (fragment == active) {
                fm.popBackStackImmediate(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }else if(active ==dashboardFragment){
                fm.beginTransaction().detach(active).show(fragment).commit();
            } else if (fragment == dashboardFragment) {
                fm.beginTransaction().attach(fragment).show(fragment).commit();
            } else {
                fm.beginTransaction().hide(active).show(fragment).commit();
            }
        } else if (active != null) {
            if(active ==dashboardFragment){
                fm.beginTransaction().detach(active).add(R.id.nav_host_fragment_activity_main, fragment,tag).commit();
            } else {
                fm.beginTransaction().hide(active).add(R.id.nav_host_fragment_activity_main, fragment,tag).commit();
            }
        } else {
            fm.beginTransaction().add(R.id.nav_host_fragment_activity_main, fragment,tag).commit();
        }
        navView.getMenu().getItem(position).setChecked(true);
        active = fragment;
    }


    public Fragment getFragment(int Fragment_id){
        if(Fragment_id==3){
            return notificationsFragment;
        }else if(Fragment_id==2){
            return dashboardFragment;
        }
        else {
            return homeFragment;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        MainActivity.LoadImageProfile(mAuth.getCurrentUser(),imageviewprofile,this);
        if(homeFragment==null){
            homeFragment = new HomeFragment();
            setFragment(homeFragment, "1", 1);
        }
        if(notificationsFragment==null){
            notificationsFragment = new NotificationsFragment();
        }
        if(dashboardFragment==null){
            dashboardFragment = new DashboardFragment();
        }
      //  IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
      //  registerReceiver(networkChangeListner,intentFilter);
    }

    public static void LoadSectionFragment(String SectionID,FragmentManager fragmentManager,int ReplaceFragment){
        Bundle InfoBundle = new Bundle();
        InfoBundle.putString(HomeFragment.Bundle_Section_Tag, SectionID);
        ArticleSection articleSection = new ArticleSection();
        articleSection.setArguments(InfoBundle);
        //и замена текущего главного фрагмента на фрагмент раздела
        fragmentManager.beginTransaction().replace(ReplaceFragment, articleSection, "section").addToBackStack(null).commit();
    }


    public static void LoadArticleFragment(Article article, FragmentManager fragmentManager, int ReplaceFragment){
        Bundle InfoBundle = new Bundle();
        InfoBundle.putString("MainText", article.MainText);
        InfoBundle.putString("Date", article.Date);
        InfoBundle.putString("HeaderText", Html.fromHtml(article.HeadText).toString().trim());
        if(article.TagList!=null){
            InfoBundle.putStringArrayList("TagList", article.TagList);
        }
        ArticleFragment articleFragment = new ArticleFragment();
        articleFragment.setArguments(InfoBundle);

        //и замена текущего главного фрагмента на фрагмент раздела
        fragmentManager.beginTransaction().replace(ReplaceFragment, articleFragment, "section").addToBackStack(null).commit();
    }
    public static void LoadArticleFragmentFromID(String articleID, FragmentManager fragmentManager, int ReplaceFragment){
        Bundle InfoBundle = new Bundle();
        InfoBundle.putString("articleID", articleID);
        ArticleFragment articleFragment = new ArticleFragment();
        articleFragment.setArguments(InfoBundle);
        //и замена текущего главного фрагмента на фрагмент раздела
        fragmentManager.beginTransaction().replace(ReplaceFragment, articleFragment, "section").addToBackStack(null).commit();
    }




    public static void LoadImageProfile(FirebaseUser cUser, ImageView imageviewprofile, Activity activity){
        //Данные аутентификации
        if(cUser!=null)
        {
            if(cUser.getPhotoUrl()!=null) {
                //   Загрузка фото
                //Picasso.get().load(cUser.getPhotoUrl()).into(ImageProfile);
                Glide
                        .with(activity)
                        .load(cUser.getPhotoUrl())
                        .into(imageviewprofile);
            }
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
       // unregisterReceiver(networkChangeListner);
    }
}