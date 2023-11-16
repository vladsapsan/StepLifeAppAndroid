package com.StepLife.steplifeapp.ui.notifications;

import static com.StepLife.steplifeapp.StafFunction.HomeArticleRedactActivity.Section1_Article_Key;
import static com.StepLife.steplifeapp.StafFunction.HomeArticleRedactActivity.Section2_Article_Key;
import static com.StepLife.steplifeapp.StafFunction.HomeArticleRedactActivity.Section3_Article_Key;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.StepLife.steplifeapp.AllArticleActivity;
import com.StepLife.steplifeapp.AllSectionFragment;
import com.StepLife.steplifeapp.MainActivity;
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

import aglibs.loading.skeleton.layout.SkeletonLinearLayout;

public class NotificationsFragment extends Fragment implements SectionArticleViewAdapter.ItemClickListener {

    private NotificationsViewModel notificationsViewModel;
    private FragmentNotificationsBinding binding;
    ViewPagerArticleAdapter viewPagerArticleAdapter;
    FrameLayout FrameArticles;

    private int NOTIFICATION_ID = 112;

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
    SkeletonLinearLayout CardArticleSection1,CardArticleSection2,CardArticleSection3;
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
                MainActivity.LoadArticleFragment(ArticlelistTemp1.get(position) ,getActivity().getSupportFragmentManager(),R.id.TeachArticleFrame);
            }
        };
        //Инициализация кликов для статей
        SectionArticleViewAdapter.ItemClickListener clickListener2 = new SectionArticleViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                MainActivity.LoadArticleFragment(ArticlelistTemp2.get(position) ,getActivity().getSupportFragmentManager(),R.id.TeachArticleFrame);
            }
        };
        //Инициализация кликов для статей
        SectionArticleViewAdapter.ItemClickListener clickListener3 = new SectionArticleViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MainActivity.LoadArticleFragment(ArticlelistTemp3.get(position) ,getActivity().getSupportFragmentManager(),R.id.TeachArticleFrame);
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
                    CardArticleSection1.stopLoading();

                }
            }
        });
        mDataBase.child(Section2_Article_Key).child("SectionID").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    SectionID2 = task.getResult().getValue().toString();
                    CardArticleSection2.stopLoading();
                }
            }
        });
        mDataBase.child(Section3_Article_Key).child("SectionID").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    SectionID3 = task.getResult().getValue().toString();
                    CardArticleSection3.stopLoading();
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

        NameTopPost = view.findViewById(R.id.NameTopPost);
        SecNameTopPost = view.findViewById(R.id.SecNameTopPost);

        FrameArticles = view.findViewById(R.id.FrameArticles);
        TopPostFrame = view.findViewById(R.id.TopPostFrame);
        progressBarTopPost = view.findViewById(R.id.progressBarTopPost);
        TextviewSectionName1 = view.findViewById(R.id.TextviewSectionName1);
        TextviewSectionName2 = view.findViewById(R.id.TextviewSectionName2);
        TextviewSectionName3 = view.findViewById(R.id.TextviewSectionName3);

        Intent intentAllArticle = new Intent(getActivity(), AllArticleActivity.class);

        CardArticleSection1 = view.findViewById(R.id.CardArticleSection1);
        CardArticleSection1.startLoading();
        CardArticleSection1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SectionID1!=null) {
                    MainActivity.LoadSectionFragment(SectionID1,getActivity().getSupportFragmentManager(),R.id.TeachArticleFrame);
                }
            }
        });
        CardArticleSection2 = view.findViewById(R.id.CardArticleSection2);
        CardArticleSection2.startLoading();
        CardArticleSection2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SectionID2!=null) {
                    MainActivity.LoadSectionFragment(SectionID2,getActivity().getSupportFragmentManager(),R.id.TeachArticleFrame);
                }
            }
        });

        CardArticleSection3 = view.findViewById(R.id.CardArticleSection3);
        CardArticleSection3.startLoading();
        CardArticleSection3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SectionID3!=null) {
                    MainActivity.LoadSectionFragment(SectionID3,getActivity().getSupportFragmentManager(),R.id.TeachArticleFrame);
                }
            }
        });

        //Теги с переходами на новое окно
        TagChip1 = view.findViewById(R.id.TagChip1);
        TagChip1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartChipActivity(TagChip1.getText().toString(),getActivity());
            }
        });
        TagChip2 = view.findViewById(R.id.TagChip2);
        TagChip2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartChipActivity(TagChip2.getText().toString(),getActivity());
            }
        });
        TagChip3 = view.findViewById(R.id.TagChip3);
        TagChip3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartChipActivity(TagChip3.getText().toString(),getActivity());
            }
        });
        TagChip4 = view.findViewById(R.id.TagChip4);
        TagChip4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartChipActivity(TagChip4.getText().toString(),getActivity());
            }
        });
        TagChip5 = view.findViewById(R.id.TagChip5);
        TagChip5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartChipActivity(TagChip5.getText().toString(),getActivity());
            }
        });
        TagChip6 = view.findViewById(R.id.TagChip6);
        TagChip6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartChipActivity(TagChip6.getText().toString(),getActivity());
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



    private static void StartChipActivity(String TagText, Activity MainActivity){
        Intent intent = new Intent(MainActivity, TagSearchArticle.class);
        intent.putExtra("TagFilter",TagText);
        MainActivity.startActivity(intent);
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