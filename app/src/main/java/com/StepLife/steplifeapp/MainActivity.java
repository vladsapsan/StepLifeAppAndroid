package com.StepLife.steplifeapp;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.StepLife.steplifeapp.TelephoneSign.TelephoneSignUp;
import com.StepLife.steplifeapp.databinding.ActivityMainBinding;
import com.StepLife.steplifeapp.other.NetworkChangeListner;
import com.StepLife.steplifeapp.ui.dashboard.DashboardFragment;
import com.StepLife.steplifeapp.ui.home.HomeFragment;
import com.StepLife.steplifeapp.ui.notifications.NotificationsFragment;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity  {


    private ActivityMainBinding binding;
    boolean CheckApp;
    HomeFragment homeFragment;
    DashboardFragment dashboardFragment;
    FloatingActionButton HelperButton;
    private FirebaseAuth mAuth;
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

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
         navView = findViewById(R.id.nav_view);
        homeFragment = new HomeFragment();
        dashboardFragment = new DashboardFragment();
        notificationsFragment = new NotificationsFragment();
        setFragment(homeFragment, "1", 1);

        //Окно пользователя
        mAuth = FirebaseAuth.getInstance();
        Intent User_ProfileActiviti = new Intent(this, com.StepLife.steplifeapp.UserProfile.User_ProfileActiviti.class);

        navView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.navigation_home:
                    setFragment(homeFragment, "1", 1);
                    break;
                case R.id.navigation_dashboard:
                    setFragment(dashboardFragment, "2", 0);
                    break;
                case R.id.navigation_notifications:
                    setFragment(notificationsFragment, "3", 2);
                    break;
            }
            return true;
        });


        //Кнопка профиля
        imageviewprofile = findViewById(R.id.imageviewprofile);
        ProfileUserButton = findViewById(R.id.ProfileUserButton);
        ProfileUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser cUser = mAuth.getCurrentUser();
                if(cUser!=null)
                {
                    startActivity(User_ProfileActiviti);
                }
                else
                {
                    startActivity(new Intent(MainActivity.this, TelephoneSignUp.class));
                }
            }
        });

        //Кнопка получения помощи
        HelperButton = findViewById(R.id.HelperButton);
        HelperButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Helper.class));
            }
        });

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
     //   AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
      //          R.id.navigation_home, R.id.navigation_notifications, R.id.navigation_profile)
      //          .build();
     //   NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
     //   NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
     //  NavigationUI.setupWithNavController(binding.navView, navController);


        // clear FLAG_TRANSLUCENT_STATUS flag:
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(getResources().getColor(R.color.MainGray));
        getSupportActionBar().hide(); //Скрытие actionBar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


    }

    void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_activity_main,fragment);
        fragmentTransaction.commit();
    }

    public void setFragment(Fragment fragment, String tag, int position) {
        if (fm.findFragmentByTag(tag)!=null) {
            if (fragment == active) {
            }else if(active ==dashboardFragment){
                fm.beginTransaction().detach(active).show(fragment).commit();
            } else if (fragment == dashboardFragment) {
                fm.beginTransaction().attach(fragment).show(fragment).commit();
            } else {
                fm.beginTransaction().hide(active).show(fragment).commit();
            }
        } else if (active != null) {
            if(active ==dashboardFragment){
                fm.beginTransaction().detach(active).add(R.id.nav_host_fragment_activity_main, fragment, tag).commit();
            } else {
                fm.beginTransaction().hide(active).add(R.id.nav_host_fragment_activity_main, fragment, tag).commit();
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
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListner,intentFilter);

        FirebaseUser cUser = mAuth.getCurrentUser();
        //Данные аутентификации
        if(cUser!=null)
        {
            if(cUser.getPhotoUrl()!=null) {
                //   Загрузка фото
                //Picasso.get().load(cUser.getPhotoUrl()).into(ImageProfile);
                Glide
                        .with(this)
                        .load(cUser.getPhotoUrl())
                        .into(imageviewprofile);
            }
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkChangeListner);
    }
}