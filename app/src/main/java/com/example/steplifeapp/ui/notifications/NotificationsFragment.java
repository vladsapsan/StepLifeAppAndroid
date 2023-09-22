package com.example.steplifeapp.ui.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.steplifeapp.AllArticleActivity;
import com.example.steplifeapp.ChooseArticle;
import com.example.steplifeapp.R;
import com.example.steplifeapp.ViewPagerArticleAdapter;
import com.example.steplifeapp.databinding.FragmentNotificationsBinding;
import com.example.steplifeapp.ui.Article;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private FragmentNotificationsBinding binding;
    ViewPagerArticleAdapter viewPagerArticleAdapter;
    FrameLayout FrameArticles;

    private int NOTIFICATION_ID = 112;
    Animation animationIN;
    private String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    private String NewHeadTextArticle = "Новый протез за 10 тысяч?";
    ViewPager viewpager;
    ImageView SearchButton,NotificationButton;

    TextView NameTopPost,SecNameTopPost;
    TextView EditDataTextTopPost1,EditDataTextTopPost2,EditDataTextTopPost3,EditDataTextTopPost4,EditDataTextTopPost5;
    TextView EditTextTopPost1,EditTextTopPost2,EditTextTopPost3,EditTextTopPost4,EditTextTopPost5;

    private String TopPost_Key ="TopPostArticle";
    private String Library_Key ="Lib";

    private DatabaseReference mDataBase,bDataBase;

    CardView TopPostCard1,TopPostCard2,TopPostCard3,TopPostCard4,TopPostCard5,SeeAllText,SeeAllText2;
    ProgressBar progressBarTopPost;
    Article DowArticle,DowArticle1,DowArticle2,DowArticle3,DowArticle4;
    ImageView imagetoppost1,imagetoppost2,imagetoppost3,imagetoppost4,imagetoppost5;

    FrameLayout TopPostFrame;
    List <Article> TopPostArticle;

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
                            EditDataTextTopPost1.setText(Html.fromHtml(DowArticle.Date).toString().trim());
                            progressBarTopPost.setVisibility(View.GONE);

                        }
                    }
                    catch (Exception e){
                    }
                }
            }
        });

        //Загрузка данных о 2 карточке
        mDataBase.child("2").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    //Ошибка получения данных
                }
                else {
                    try {
                        //Данные получены
                        DowArticle1 =  task.getResult().getValue(Article.class);
                        if(DowArticle1!= null){
                            EditTextTopPost2.setText(Html.fromHtml(DowArticle1.HeadText).toString().trim());
                            Glide.with(getActivity()).load(DowArticle1.PreviewPhotoUri).into(imagetoppost2);
                            EditDataTextTopPost2.setText(Html.fromHtml(DowArticle1.Date).toString().trim());
                            progressBarTopPost.setVisibility(View.GONE);

                        }
                    }
                    catch (Exception e){
                    }
                }
            }
        });

        //Загрузка данных о 3 карточке
        mDataBase.child("3").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    //Ошибка получения данных
                }
                else {
                    try {
                        //Данные получены
                        DowArticle2 =  task.getResult().getValue(Article.class);
                        if(DowArticle2!= null){
                            EditTextTopPost3.setText(Html.fromHtml(DowArticle2.HeadText).toString().trim());
                            Glide.with(getActivity()).load(DowArticle2.PreviewPhotoUri).into(imagetoppost3);
                            EditDataTextTopPost3.setText(Html.fromHtml(DowArticle2.Date).toString().trim());
                            progressBarTopPost.setVisibility(View.GONE);

                        }
                    }
                    catch (Exception e){
                    }
                }
            }
        });

        //Загрузка данных о 4 карточке
        mDataBase.child("4").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    //Ошибка получения данных
                }
                else {
                    try {
                        //Данные получены
                        DowArticle3 =  task.getResult().getValue(Article.class);
                        if(DowArticle3!= null){
                            EditTextTopPost4.setText(Html.fromHtml(DowArticle3.HeadText).toString().trim());
                            Glide.with(getActivity()).load(DowArticle3.PreviewPhotoUri).into(imagetoppost4);
                            EditDataTextTopPost4.setText(Html.fromHtml(DowArticle3.Date).toString().trim());
                            progressBarTopPost.setVisibility(View.GONE);

                        }
                    }
                    catch (Exception e){
                    }
                }
            }
        });

        //Загрузка данных о 5 карточке
        mDataBase.child("5").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    //Ошибка получения данных
                }
                else {
                    try {
                        //Данные получены
                        DowArticle4 =  task.getResult().getValue(Article.class);
                        if(DowArticle4!= null){
                            EditTextTopPost5.setText(Html.fromHtml(DowArticle4.HeadText).toString().trim());
                            Glide.with(getActivity()).load(DowArticle4.PreviewPhotoUri).into(imagetoppost5);
                            EditDataTextTopPost5.setText(Html.fromHtml(DowArticle4.Date).toString().trim());
                            progressBarTopPost.setVisibility(View.GONE);
                            if(FrameArticles.getVisibility()==View.GONE) {
                                FrameArticles.setVisibility(View.VISIBLE);
                                FrameArticles.setAnimation(animationIN);
                            }

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


        animationIN = AnimationUtils.loadAnimation(getContext(),R.anim.expectedanim);

        //Элементы главной обложки учебника
        imagetoppost1 = view.findViewById(R.id.imagetoppost1);
        imagetoppost2 = view.findViewById(R.id.imagetoppost2);
        imagetoppost3 = view.findViewById(R.id.imagetoppost3);
        imagetoppost4 = view.findViewById(R.id.imagetoppost4);
        imagetoppost5 = view.findViewById(R.id.imagetoppost5);
        EditTextTopPost1 = view.findViewById(R.id.EditTextTopPost1);
        EditTextTopPost2 = view.findViewById(R.id.EditTextTopPost2);
        EditTextTopPost3 = view.findViewById(R.id.EditTextTopPost3);
        EditTextTopPost4 = view.findViewById(R.id.EditTextTopPost4);
        EditTextTopPost5 = view.findViewById(R.id.EditTextTopPost5);



        FrameArticles = view.findViewById(R.id.FrameArticles);
        FrameArticles.setVisibility(View.GONE);
        TopPostFrame = view.findViewById(R.id.TopPostFrame);
        progressBarTopPost = view.findViewById(R.id.progressBarTopPost);

        Intent intentAllArticle = new Intent(getActivity(), AllArticleActivity.class);


        //Уведомление
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), Notification.EXTRA_CHANNEL_ID)
                .setSmallIcon(R.drawable.bell)
                .setContentTitle("Новая статья уже вышла!")
                .setContentText(NewHeadTextArticle)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);


        //кнопка уведомлений
        NotificationButton = view.findViewById(R.id.NotificationButton);
        NotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getActivity());
                notificationManager.notify(3, builder.build());
            }
        });


        EditDataTextTopPost1 = view.findViewById(R.id.EditDataTextTopPost1);
        EditDataTextTopPost2 = view.findViewById(R.id.EditDataTextTopPost2);
        EditDataTextTopPost3 = view.findViewById(R.id.EditDataTextTopPost3);
        EditDataTextTopPost4 = view.findViewById(R.id.EditDataTextTopPost4);
        EditDataTextTopPost5 = view.findViewById(R.id.EditDataTextTopPost5);



        //Переход к статье по карточке
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

        //Переход к статье по карточке
        TopPostCard2 = view.findViewById(R.id.TopPostCard2);
        TopPostCard2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(DowArticle1!=null) {
                    Intent intent = new Intent(getActivity(), ChooseArticle.class);
                    // передача объекта с ключом "MainText" и значением
                    intent.putExtra("MainText", DowArticle1.MainText);
                    intent.putExtra("Date", DowArticle1.Date);
                    intent.putExtra("HeaderText", Html.fromHtml(DowArticle1.HeadText).toString().trim());
                    // запуск ChooseArticle
                    startActivity(intent);
                }
            }
        });

        //Переход к статье по карточке
        TopPostCard3 = view.findViewById(R.id.TopPostCard3);
        TopPostCard3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(DowArticle2!=null) {
                    Intent intent = new Intent(getActivity(), ChooseArticle.class);
                    // передача объекта с ключом "MainText" и значением
                    intent.putExtra("MainText", DowArticle2.MainText);
                    intent.putExtra("Date", DowArticle2.Date);
                    intent.putExtra("HeaderText", Html.fromHtml(DowArticle2.HeadText).toString().trim());
                    // запуск ChooseArticle
                    startActivity(intent);
                }
            }
        });

        //Переход к статье по карточке
        TopPostCard4 = view.findViewById(R.id.TopPostCard4);
        TopPostCard4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(DowArticle3!=null) {
                    Intent intent = new Intent(getActivity(), ChooseArticle.class);
                    // передача объекта с ключом "MainText" и значением
                    intent.putExtra("MainText", DowArticle3.MainText);
                    intent.putExtra("Date", DowArticle3.Date);
                    intent.putExtra("HeaderText", Html.fromHtml(DowArticle3.HeadText).toString().trim());
                    // запуск ChooseArticle
                    startActivity(intent);
                }
            }
        });


        //Переход к статье по карточке
        TopPostCard5 = view.findViewById(R.id.TopPostCard5);
        TopPostCard5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(DowArticle4!=null) {
                    Intent intent = new Intent(getActivity(), ChooseArticle.class);
                    // передача объекта с ключом "MainText" и значением
                    intent.putExtra("MainText", DowArticle4.MainText);
                    intent.putExtra("Date", DowArticle4.Date);
                    intent.putExtra("HeaderText", Html.fromHtml(DowArticle4.HeadText).toString().trim());
                    // запуск ChooseArticle
                    startActivity(intent);
                }
            }
        });
        SeeAllButton = view.findViewById(R.id.seeallArticleButton);
        SeeAllText = view.findViewById(R.id.seeallArticleText);
        SeeAllText2 = view.findViewById(R.id.seeallArticleText2);
        SearchButton = view.findViewById(R.id.SearchButton);
        SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentAllArticle);
            }
        });

        //Переход ко всем статьям
        SeeAllText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentAllArticle);
            }
        });
        SeeAllText2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentAllArticle);
            }
        });
        //Переход ко всем статьям
        SeeAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentAllArticle);
            }
        });
    }

    //Создание канала уведомлений
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(Notification.EXTRA_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getContext());

// notificationId is a unique int for each notification that you must define

        }
    }
}