package com.example.steplifeapp.ui.notifications;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.steplifeapp.AllArticle;
import com.example.steplifeapp.R;
import com.example.steplifeapp.ViewPagerArticleAdapter;
import com.example.steplifeapp.databinding.FragmentNotificationsBinding;
import com.example.steplifeapp.ui.MostArticle1Fragment;
import com.example.steplifeapp.ui.MostArticle2Fragment;
import com.example.steplifeapp.ui.MostArticle3Fragment;
import com.example.steplifeapp.ui.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import me.relex.circleindicator.CircleIndicator;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private FragmentNotificationsBinding binding;
    ViewPagerArticleAdapter viewPagerArticleAdapter;
    ViewPager viewpager;
    TextView SeeAllText;
    Button SeeAllButton;
    ScrollView TeachBookScroll;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {




        return inflater.inflate(R.layout.fragment_notifications, container,false);
    }

    @Override
    public void onStart() {

        super.onStart();

    }


    public void attachFragment(String tag) {

        FragmentManager manager = ( (FragmentActivity) getContext() ).getSupportFragmentManager ();
        FragmentTransaction ft = manager.beginTransaction ();
        if (manager.findFragmentByTag ( tag ) == null) { // No fragment in backStack with same tag..

        }
        else {
            ft.show ( manager.findFragmentByTag ( tag ) ).commit ();
        }
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        attachFragment("AllArticle");

        viewPagerArticleAdapter = new ViewPagerArticleAdapter(((FragmentActivity)getContext()).getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPagerArticleAdapter.addFragment(new MostArticle1Fragment());
        viewPagerArticleAdapter.addFragment(new MostArticle2Fragment());
        viewPagerArticleAdapter.addFragment(new MostArticle2Fragment());
        viewPagerArticleAdapter.addFragment(new MostArticle3Fragment());
        viewPagerArticleAdapter.addFragment(new MostArticle1Fragment());


        //Adapter adapter = new Adapter(getSupportFragmentManager());

        viewpager = (ViewPager) view.findViewById(R.id.viewpagerArticle);
        viewpager.setAdapter(viewPagerArticleAdapter);


        TeachBookScroll = view.findViewById(R.id.TeachBookScroll);
        OverScrollDecoratorHelper.setUpOverScroll(TeachBookScroll);

        SeeAllButton = view.findViewById(R.id.seeallArticleButton);
        SeeAllText = view.findViewById(R.id.seeallArticleText);

        CircleIndicator indicator = (CircleIndicator) view.findViewById(R.id.indicator);
        indicator.setViewPager(viewpager);

        //Переход ко всем статьям
        SeeAllText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AllArticle();
                FragmentTransaction ft = ((FragmentActivity)getContext()).getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.slide_left, R.anim.slide_right,R.anim.slide_left, R.anim.slide_right);
                ft.addToBackStack("AllArticle");
                ft.add(R.id.TeachArticleFrame,fragment,"AllArticle").commit();

            }
        });
        //Переход ко всем статьям
        SeeAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AllArticle();
                FragmentTransaction ft = ((FragmentActivity)getContext()).getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.slide_left, R.anim.slide_right,R.anim.slide_left, R.anim.slide_right);
                ft.addToBackStack("AllArticle");
                ft.add(R.id.TeachArticleFrame,fragment,"AllArticle").commit();

            }
        });
    }
}