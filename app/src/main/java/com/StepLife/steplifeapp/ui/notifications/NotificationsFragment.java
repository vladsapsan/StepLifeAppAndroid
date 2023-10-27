package com.StepLife.steplifeapp.ui.notifications;

import static com.StepLife.steplifeapp.StafFunction.HomeArticleRedactActivity.Section1_Article_Key;
import static com.StepLife.steplifeapp.StafFunction.HomeArticleRedactActivity.Section2_Article_Key;
import static com.StepLife.steplifeapp.StafFunction.HomeArticleRedactActivity.Section3_Article_Key;
import static com.StepLife.steplifeapp.ui.home.HomeFragment.Bundle_Section_Tag;

import android.app.Activity;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.StepLife.steplifeapp.AllArticleActivity;
import com.StepLife.steplifeapp.AllSectionFragment;
import com.StepLife.steplifeapp.ArticleSection;
import com.StepLife.steplifeapp.ChooseArticle;
import com.StepLife.steplifeapp.R;
import com.StepLife.steplifeapp.StafFunction.TopPostRedactActivity;
import com.StepLife.steplifeapp.TagSearchArticle;
import com.StepLife.steplifeapp.ViewPagerArticleAdapter;
import com.StepLife.steplifeapp.databinding.FragmentNotificationsBinding;
import com.StepLife.steplifeapp.other.SectionArticleViewAdapter;
import com.StepLife.steplifeapp.ui.Article;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.transition.MaterialFadeThrough;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment implements SectionArticleViewAdapter.ItemClickListener {

    private NotificationsViewModel notificationsViewModel;
    private FragmentNotificationsBinding binding;
    ViewPagerArticleAdapter viewPagerArticleAdapter;
    FrameLayout FrameArticles;

    private int NOTIFICATION_ID = 112;
    Animation animationIN,animationUP;
    private String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    private String NewHeadTextArticle = "Новый протез за 10 тысяч?";
    ViewPager viewpager;
    ImageView SearchButton,NotificationButton;

    TextView NameTopPost,SecNameTopPost,LibPostTextRow1,LibPostTextRow2;


    private final static String TopPost_Key ="TopPostArticle";
    private final static String Library_Key ="Lib";

    private static final String Library_Row1_Key ="Row1";
    private static final String Library_Row2_Key ="Row2";

    private DatabaseReference mDataBase,bDataBase;

    Chip TagChip1,TagChip2,TagChip3,TagChip4,TagChip5,TagChip6;


    ProgressBar progressBarTopPost;


    FrameLayout TopPostFrame,NotificationAppBar;
    List <Article> TopPostArticle;

    private String Article_Key ="AllArticle";

    List<String> TopPostList;
    Button SeeAllButton,seeallSectionButton;
    private ArrayList<Article> ArticlelistTemp1 = new ArrayList<>();
    private ArrayList<Article> ArticlelistTemp2 = new ArrayList<>();
    private ArrayList<Article> ArticlelistTemp3 = new ArrayList<>();
    String SectionID1,SectionID2,SectionID3;
    SectionArticleViewAdapter sectionArticleViewAdapter1,sectionArticleViewAdapter2,sectionArticleViewAdapter3;
    RecyclerView RecycleviewSectionArticle1,RecycleviewSectionArticle2,RecycleviewSectionArticle3;
    TextView TextviewSectionName1,TextviewSectionName2,TextviewSectionName3,ChooseTextView;
    LinearLayout CardArticleSection1,CardArticleSection2,CardArticleSection3;
    ScrollView TeachBookScroll;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notifications, container,false);
    }

    //Иницилизация компонентов
    private void initilization(View view)
    {
        mDataBase = FirebaseDatabase.getInstance().getReference(Library_Key).child(TopPost_Key);

        //Да это работает так)
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(),LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(view.getContext(),LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(view.getContext(),LinearLayoutManager.HORIZONTAL, false);

        //Инициализация кликов для статей
        SectionArticleViewAdapter.ItemClickListener clickListener1 = new SectionArticleViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                StartArticle(ArticlelistTemp1.get(position),getActivity());
            }
        };
        //Инициализация кликов для статей
        SectionArticleViewAdapter.ItemClickListener clickListener2 = new SectionArticleViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                StartArticle(ArticlelistTemp2.get(position),getActivity());
            }
        };
        //Инициализация кликов для статей
        SectionArticleViewAdapter.ItemClickListener clickListener3 = new SectionArticleViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                StartArticle(ArticlelistTemp3.get(position),getActivity());
            }
        };

        //первый раздел столбец данных
        RecycleviewSectionArticle1 = view.findViewById(R.id.RecycleviewSectionArticle1);
        RecycleviewSectionArticle1.setLayoutManager(layoutManager);
        sectionArticleViewAdapter1 = new SectionArticleViewAdapter(view.getContext(),ArticlelistTemp1);
        RecycleviewSectionArticle1.setAdapter(sectionArticleViewAdapter1);
        sectionArticleViewAdapter1.setClickListener(clickListener1);

        //второй раздел столбец данных
        RecycleviewSectionArticle2 = view.findViewById(R.id.RecycleviewSectionArticle2);
        RecycleviewSectionArticle2.setLayoutManager(layoutManager2);
        sectionArticleViewAdapter2 = new SectionArticleViewAdapter(view.getContext(),ArticlelistTemp2);
        RecycleviewSectionArticle2.setAdapter(sectionArticleViewAdapter2);
        sectionArticleViewAdapter2.setClickListener(clickListener2);

        //второй раздел столбец данных
        RecycleviewSectionArticle3 = view.findViewById(R.id.RecycleviewSectionArticle3);
        RecycleviewSectionArticle3.setLayoutManager(layoutManager3);
        sectionArticleViewAdapter3 = new SectionArticleViewAdapter(view.getContext(),ArticlelistTemp3);
        RecycleviewSectionArticle3.setAdapter(sectionArticleViewAdapter3);
        sectionArticleViewAdapter3.setClickListener(clickListener3);

        mDataBase.child(Section1_Article_Key).child("SectionID").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    SectionID1 = task.getResult().getValue().toString();
                }
            }
        });
        mDataBase.child(Section2_Article_Key).child("SectionID").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    SectionID2 = task.getResult().getValue().toString();
                }
            }
        });
        mDataBase.child(Section3_Article_Key).child("SectionID").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    SectionID3 = task.getResult().getValue().toString();
                }
            }
        });
        //Запуск анимации
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
                        FrameArticles.setVisibility(View.VISIBLE);
                        FrameArticles.setAnimation(animationIN);
                    }
                    catch (Exception e){
                        Log.e("Profile",e.toString());
                    }

                }
            }
        });
        TopPostRedactActivity.DownloadSection(ArticlelistTemp1,Section1_Article_Key,TextviewSectionName1,sectionArticleViewAdapter1,progressBarTopPost,null,mDataBase);
        TopPostRedactActivity.DownloadSection(ArticlelistTemp2,Section2_Article_Key,TextviewSectionName2,sectionArticleViewAdapter2,progressBarTopPost,null,mDataBase);
        TopPostRedactActivity.DownloadSection(ArticlelistTemp3,Section3_Article_Key,TextviewSectionName3,sectionArticleViewAdapter3,progressBarTopPost,null,mDataBase);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //анимация
        setExitTransition(new MaterialFadeThrough());
        setEnterTransition(new MaterialFadeThrough());
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



        //Инициализация анимации
        animationIN = AnimationUtils.loadAnimation(getContext(),R.anim.expectedanim);
        animationUP = AnimationUtils.loadAnimation(getContext(),R.anim.expected_app_bar);





        FrameArticles = view.findViewById(R.id.FrameArticles);
        FrameArticles.setVisibility(View.GONE);
        TopPostFrame = view.findViewById(R.id.TopPostFrame);
        progressBarTopPost = view.findViewById(R.id.progressBarTopPost);
        TextviewSectionName1 = view.findViewById(R.id.TextviewSectionName1);
        TextviewSectionName2 = view.findViewById(R.id.TextviewSectionName2);
        TextviewSectionName3 = view.findViewById(R.id.TextviewSectionName3);


        Intent intentAllArticle = new Intent(getActivity(), AllArticleActivity.class);


        //Уведомление
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), Notification.EXTRA_CHANNEL_ID)
                .setSmallIcon(R.drawable.bell)
                .setContentTitle("Новая статья уже вышла!")
                .setContentText(NewHeadTextArticle)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);


        CardArticleSection1 = view.findViewById(R.id.CardArticleSection1);
        CardArticleSection1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SectionID1!=null) {
                    //Переход по навигации в фграмент школа
                    Bundle InfoBundle = new Bundle();
                    InfoBundle.putString(Bundle_Section_Tag, SectionID1);

                    ArticleSection articleSection = new ArticleSection();
                    articleSection.setArguments(InfoBundle);
                    FragmentManager fragmentManager = getFragmentManager();
                    //и замена текущего главного фрагмента на фрагмент раздела
                        fragmentManager.beginTransaction().replace(R.id.TeachArticleFrame, articleSection, "section").addToBackStack(null).commit();
                }
            }
        });
        CardArticleSection2 = view.findViewById(R.id.CardArticleSection2);
        CardArticleSection2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SectionID2!=null) {
                    //Переход по навигации в фграмент школа
                    Bundle InfoBundle = new Bundle();
                    InfoBundle.putString(Bundle_Section_Tag, SectionID2);

                    ArticleSection articleSection = new ArticleSection();
                    articleSection.setArguments(InfoBundle);
                    FragmentManager fragmentManager = getFragmentManager();
                    //и замена текущего главного фрагмента на фрагмент раздела
                        fragmentManager.beginTransaction().replace(R.id.TeachArticleFrame, articleSection, "section").addToBackStack(null).commit();
                }
            }
        });

        CardArticleSection3 = view.findViewById(R.id.CardArticleSection3);
        CardArticleSection3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SectionID3!=null) {
                    //Переход по навигации в фграмент школа
                    Bundle InfoBundle = new Bundle();
                    InfoBundle.putString(Bundle_Section_Tag, SectionID3);

                    ArticleSection articleSection = new ArticleSection();
                    articleSection.setArguments(InfoBundle);
                    FragmentManager fragmentManager = getFragmentManager();
                    //и замена текущего главного фрагмента на фрагмент раздела
                        fragmentManager.beginTransaction().replace(R.id.TeachArticleFrame, articleSection, "section").addToBackStack(null).commit();
                }
            }
        });


        //Теги с переходами на новое окно
        TagChip1 = view.findViewById(R.id.TagChip1);
        TagChip1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), TagSearchArticle.class);
                intent.putExtra("TagFilter",TagChip1.getText());
                startActivity(intent);
            }
        });
        TagChip2 = view.findViewById(R.id.TagChip2);
        TagChip2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), TagSearchArticle.class);
                intent.putExtra("TagFilter",TagChip2.getText());
                startActivity(intent);
            }
        });
        TagChip3 = view.findViewById(R.id.TagChip3);
        TagChip3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), TagSearchArticle.class);
                intent.putExtra("TagFilter",TagChip3.getText());
                startActivity(intent);
            }
        });
        TagChip4 = view.findViewById(R.id.TagChip4);
        TagChip4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), TagSearchArticle.class);
                intent.putExtra("TagFilter",TagChip4.getText());
                startActivity(intent);
            }
        });
        TagChip5 = view.findViewById(R.id.TagChip5);
        TagChip5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), TagSearchArticle.class);
                intent.putExtra("TagFilter",TagChip5.getText());
                startActivity(intent);
            }
        });
        TagChip6 = view.findViewById(R.id.TagChip6);
        TagChip6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), TagSearchArticle.class);
                intent.putExtra("TagFilter",TagChip6.getText());
                startActivity(intent);
            }
        });



        initilization(view);


        seeallSectionButton  = view.findViewById(R.id.seeallSectionButton);
        seeallSectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartAllSectionFragment(R.id.TeachArticleFrame,getActivity().getSupportFragmentManager());
            }
        });

        SeeAllButton = view.findViewById(R.id.seeallArticleButton);
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

    public static void StartArticle(Article article, Activity thisActivity){
        if(article!=null) {
            Intent intentChooseArticle = new Intent(thisActivity, ChooseArticle.class);
            // передача объекта с ключом "MainText" и значением
            intentChooseArticle.putExtra("MainText", article.MainText);
            intentChooseArticle.putExtra("Date", article.Date);
            intentChooseArticle.putExtra("HeaderText", Html.fromHtml(article.HeadText).toString().trim());
            if(article.TagList!=null){
                intentChooseArticle.putStringArrayListExtra("TagList", article.TagList);
            }
            // запуск ChooseArticle
            thisActivity.startActivity(intentChooseArticle);
        }
    }

    public static void StartAllSectionFragment(int ReplaceFrameID,FragmentManager fragmentManager){
        AllSectionFragment allSectionFragment;
        //и замена текущего главного фрагмента на фрагмент раздела
        if(fragmentManager.findFragmentByTag("allSectionFragment")!=null){
            allSectionFragment = (AllSectionFragment) fragmentManager.findFragmentByTag("allSectionFragment");
        }else {
            allSectionFragment = new AllSectionFragment();
        }
        fragmentManager.beginTransaction().replace(ReplaceFrameID, allSectionFragment, "allSectionFragment").addToBackStack(null).commit();
    }


    @Override
    public void onItemClick(View view, int position) {

    }
}