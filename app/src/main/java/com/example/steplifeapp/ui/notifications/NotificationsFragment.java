package com.example.steplifeapp.ui.notifications;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.steplifeapp.AllArticle;
import com.example.steplifeapp.ChooseArticle;
import com.example.steplifeapp.R;
import com.example.steplifeapp.ViewPagerArticleAdapter;
import com.example.steplifeapp.databinding.FragmentNotificationsBinding;
import com.example.steplifeapp.ui.Article;
import com.example.steplifeapp.ui.MostArticle1Fragment;
import com.example.steplifeapp.ui.MostArticle2Fragment;
import com.example.steplifeapp.ui.MostArticle3Fragment;
import com.example.steplifeapp.ui.SettingsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import me.relex.circleindicator.CircleIndicator;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private FragmentNotificationsBinding binding;
    ViewPagerArticleAdapter viewPagerArticleAdapter;
    ViewPager viewpager;
    ImageButton SearchButton;
    TextView SeeAllText,NameTopPost,SecNameTopPost;

    private String TopPost_Key ="TopPostArticle";
    private String Library_Key ="Lib";

    private DatabaseReference mDataBase,bDataBase;

    CardView TopPostCard1;
    ProgressBar progressBarTopPost;
    Article DowArticle;
    ImageView imagetoppost1;
    TextView EditTextTopPost1;
    FrameLayout TopPostFrame;

    private String Article_Key ="AllArticle";

    List<String> TopPostList;
    Button SeeAllButton;
    ScrollView TeachBookScroll;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notifications, container,false);
    }

    @Override
    public void onStart() {
        super.onStart();
        mDataBase = FirebaseDatabase.getInstance().getReference(Library_Key).child(TopPost_Key);
        //Получение данных из базы
        mDataBase.child("Name").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    //Ошибка получения данных
                }
                else {
                    try {
                        //Данные получены
                        NameTopPost.setText((String)task.getResult().getValue());
                    }
                    catch (Exception e){
                        Log.e("Profile",e.toString());
                    }

                }
            }
        });

        //Получение данных из базы
        mDataBase.child("SecName").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    //Ошибка получения данных
                }
                else {
                    try {
                        //Данные получены
                        SecNameTopPost.setText((String)task.getResult().getValue());
                    }
                    catch (Exception e){
                        Log.e("Profile",e.toString());
                    }

                }
            }
        });
        //Загрузка данных о 1 карточке
        mDataBase.child("1").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    //Ошибка получения данных
                }
                else {
                    try {
                        //Данные получены
                        DowArticle =  task.getResult().getValue(Article.class);
                        if(DowArticle!= null){
                            EditTextTopPost1.setText(Html.fromHtml(DowArticle.HeadText).toString().trim());
                            Glide.with(getActivity()).load(DowArticle.PreviewPhotoUri).into(imagetoppost1);
                            progressBarTopPost.setVisibility(View.GONE);
                            TopPostFrame.setVisibility(View.VISIBLE);
                        }
                    }
                    catch (Exception e){
                    }
                }
            }
        });
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


        //Adapter adapter = new Adapter(getSupportFragmentManager());

        NameTopPost = view.findViewById(R.id.NameTopPost);
        SecNameTopPost = view.findViewById(R.id.SecNameTopPost);

        imagetoppost1 = view.findViewById(R.id.imagetoppost1);
                EditTextTopPost1 = view.findViewById(R.id.EditTextTopPost1);


        TopPostFrame = view.findViewById(R.id.TopPostFrame);
        TopPostFrame.setVisibility(View.GONE);
        progressBarTopPost = view.findViewById(R.id.progressBarTopPost);



        TopPostCard1 = view.findViewById(R.id.TopPostCard1);
        TopPostCard1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(DowArticle!=null) {
                    Intent intent = new Intent(getActivity(), ChooseArticle.class);
                    // передача объекта с ключом "MainText" и значением
                    intent.putExtra("MainText", DowArticle.MainText);
                    intent.putExtra("Date", DowArticle.Date);
                    intent.putExtra("HeaderText", Html.fromHtml(DowArticle.HeadText).toString().trim());
                    // запуск ChooseArticle
                    startActivity(intent);
                }
            }
        });

        SeeAllButton = view.findViewById(R.id.seeallArticleButton);
        SeeAllText = view.findViewById(R.id.seeallArticleText);
        SearchButton = view.findViewById(R.id.SearchButton);
        SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new AllArticle();
                FragmentTransaction ft = ((FragmentActivity)getContext()).getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.slide_left, R.anim.slide_right,R.anim.slide_left, R.anim.slide_right);
                ft.addToBackStack("AllArticle");
                ft.add(R.id.TeachArticleFrame,fragment,"AllArticle").commit();
            }
        });

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