package com.StepLife.steplifeapp.ui.notifications;

import static com.StepLife.steplifeapp.StafFunction.Edit.HomeArticleRedactActivity.Section1_Article_Key;
import static com.StepLife.steplifeapp.StafFunction.Edit.HomeArticleRedactActivity.Section2_Article_Key;
import static com.StepLife.steplifeapp.StafFunction.Edit.HomeArticleRedactActivity.Section3_Article_Key;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.StepLife.steplifeapp.Model.Article;
import com.StepLife.steplifeapp.garbage.AllArticleActivity;
import com.StepLife.steplifeapp.MainEnterenceActivity.AllSectionFragment;
import com.StepLife.steplifeapp.MainEnterenceActivity.MainActivity;
import com.StepLife.steplifeapp.R;
import com.StepLife.steplifeapp.StafFunction.Edit.TopPostRedactActivity;
import com.StepLife.steplifeapp.MainEnterenceActivity.TagSearchArticleFragment;
import com.StepLife.steplifeapp.Model.LightArticle;
import com.StepLife.steplifeapp.other.SectionArticleViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.transition.MaterialFadeThrough;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import aglibs.loading.skeleton.layout.SkeletonLinearLayout;

public class NotificationsFragment extends Fragment implements SectionArticleViewAdapter.ItemClickListener {

    FrameLayout FrameArticles;
    TextView NameTopPost,SecNameTopPost;
    private final static String TopPost_Key ="TopPostArticle";
    private static final String AllSection_Key ="AllArticleSection";
    public static final String TagString_Key ="TagString";
    private final static String Library_Key ="Lib";
    private DatabaseReference mDataBase;
    Chip TagChip1,TagChip2,TagChip3,TagChip4,TagChip5,TagChip6;
    CardView Card1Next,Card2Next,Card3Next;
    FrameLayout TopPostFrame;
    private String Article_Key ="AllArticle";
    Button SeeAllButton,seeallSectionButton;
    private ArrayList<LightArticle> ArticlelistTemp1 = new ArrayList<>();
    private ArrayList<LightArticle> ArticlelistTemp2 = new ArrayList<>();
    private ArrayList<LightArticle> ArticlelistTemp3 = new ArrayList<>();
    String SectionID1,SectionID2,SectionID3;
    SectionArticleViewAdapter sectionArticleViewAdapter1,sectionArticleViewAdapter2,sectionArticleViewAdapter3;
    RecyclerView RecycleviewSectionArticle1,RecycleviewSectionArticle2,RecycleviewSectionArticle3;
    TextView TextviewSectionName1,TextviewSectionName2,TextviewSectionName3;
    SkeletonLinearLayout CardArticleSection1,SkeletonCards1,SkeletonCards2,SkeletonCards3,CardArticleSection2,CardArticleSection3;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notifications, container,false);
    }
    //Иницилизация компонентов
    private void initilization(View view)
    {
        mDataBase = FirebaseDatabase.getInstance().getReference();
        //Да это работает так)
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(),LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(view.getContext(),LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(view.getContext(),LinearLayoutManager.HORIZONTAL, false);
        //Инициализация кликов для статей
        SectionArticleViewAdapter.ItemClickListener clickListener1 = new SectionArticleViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MainActivity.LoadArticleFragmentFromID(ArticlelistTemp1.get(position).id ,getActivity().getSupportFragmentManager(),R.id.TeachArticleFrame);
            }
        };
        //Инициализация кликов для статей
        SectionArticleViewAdapter.ItemClickListener clickListener2 = new SectionArticleViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MainActivity.LoadArticleFragmentFromID(ArticlelistTemp2.get(position).id ,getActivity().getSupportFragmentManager(),R.id.TeachArticleFrame);
            }
        };
        //Инициализация кликов для статей
        SectionArticleViewAdapter.ItemClickListener clickListener3 = new SectionArticleViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MainActivity.LoadArticleFragmentFromID(ArticlelistTemp3.get(position).id ,getActivity().getSupportFragmentManager(),R.id.TeachArticleFrame);
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



        DownloadSection();

    }

    @Override
    public void onStart() {
        super.onStart();

    }


    private void DownloadSection(){
        mDataBase.child(Library_Key).child(TopPost_Key).child(Section1_Article_Key).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().getValue().toString()!=null) {
                        SectionID1 = (String) task.getResult().getValue();
                        TopPostRedactActivity.DownloadSection(ArticlelistTemp1, (String) task.getResult().getValue(), TextviewSectionName1, sectionArticleViewAdapter1, null, null,
                                mDataBase,CardArticleSection1,SkeletonCards1,Card1Next);
                    }
                }

            }
        });
        mDataBase.child(Library_Key).child(TopPost_Key).child(Section2_Article_Key).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().getValue().toString()!=null) {
                        SectionID2 = (String) task.getResult().getValue();
                        TopPostRedactActivity.DownloadSection(ArticlelistTemp2, (String) task.getResult().getValue(), TextviewSectionName2, sectionArticleViewAdapter2,
                                null, null, mDataBase,CardArticleSection2,SkeletonCards2,Card2Next);
                    }
                }

            }
        });
        mDataBase.child(Library_Key).child(TopPost_Key).child(Section3_Article_Key).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().getValue().toString()!=null) {
                        SectionID3 = (String) task.getResult().getValue();
                        TopPostRedactActivity.DownloadSection(ArticlelistTemp3, (String) task.getResult().getValue(), TextviewSectionName3, sectionArticleViewAdapter3, null,
                                null, mDataBase,CardArticleSection3,SkeletonCards3,Card3Next);
                    }
                }

            }
        });
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


        TopPostFrame = view.findViewById(R.id.TopPostFrame);
        TextviewSectionName1 = view.findViewById(R.id.TextviewSectionName1);
        TextviewSectionName2 = view.findViewById(R.id.TextviewSectionName2);
        TextviewSectionName3 = view.findViewById(R.id.TextviewSectionName3);

        Intent intentAllArticle = new Intent(getActivity(), AllArticleActivity.class);

        CardArticleSection1 = view.findViewById(R.id.CardArticleSection1);
        SkeletonCards1 = view.findViewById(R.id.SkeletonCards1);
        SkeletonCards1.startLoading();
        CardArticleSection1.startLoading();
        CardArticleSection1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SectionID1!=null) {
                    MainActivity.LoadSectionFragment(SectionID1,getActivity().getSupportFragmentManager(),R.id.TeachArticleFrame);
                }
            }
        });
        Card1Next = view.findViewById(R.id.Card1Next);
        Card1Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SectionID1!=null) {
                    MainActivity.LoadSectionFragment(SectionID1,getActivity().getSupportFragmentManager(),R.id.TeachArticleFrame);
                }
            }
        });
        CardArticleSection2 = view.findViewById(R.id.CardArticleSection2);
        CardArticleSection2.startLoading();
        SkeletonCards2 = view.findViewById(R.id.SkeletonCards2);
        SkeletonCards2.startLoading();
        CardArticleSection2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SectionID2!=null) {
                    MainActivity.LoadSectionFragment(SectionID2,getActivity().getSupportFragmentManager(),R.id.TeachArticleFrame);
                }
            }
        });
        Card2Next = view.findViewById(R.id.Card2Next);
        Card2Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SectionID2!=null) {
                    MainActivity.LoadSectionFragment(SectionID2,getActivity().getSupportFragmentManager(),R.id.TeachArticleFrame);
                }
            }
        });
        CardArticleSection3 = view.findViewById(R.id.CardArticleSection3);
        CardArticleSection3.startLoading();
        SkeletonCards3 = view.findViewById(R.id.SkeletonCards3);
        SkeletonCards3.startLoading();
        CardArticleSection3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SectionID3!=null) {
                    MainActivity.LoadSectionFragment(SectionID3,getActivity().getSupportFragmentManager(),R.id.TeachArticleFrame);
                }
            }
        });
        Card3Next = view.findViewById(R.id.Card3Next);
        Card3Next.setOnClickListener(new View.OnClickListener() {
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
                StartChipActivity(TagChip1.getText().toString(),getActivity().getSupportFragmentManager(),R.id.TeachArticleFrame);
            }
        });
        TagChip2 = view.findViewById(R.id.TagChip2);
        TagChip2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartChipActivity(TagChip2.getText().toString(),getActivity().getSupportFragmentManager(),R.id.TeachArticleFrame);
            }
        });
        TagChip3 = view.findViewById(R.id.TagChip3);
        TagChip3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartChipActivity(TagChip3.getText().toString(),getActivity().getSupportFragmentManager(),R.id.TeachArticleFrame);
            }
        });
        TagChip4 = view.findViewById(R.id.TagChip4);
        TagChip4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartChipActivity(TagChip4.getText().toString(),getActivity().getSupportFragmentManager(),R.id.TeachArticleFrame);
            }
        });



        initilization(view);
    }



    public static void StartChipActivity(String TagText,FragmentManager fragmentManager,int ReplaceFrameID){
        TagSearchArticleFragment tagSearchArticleFragment;

        Bundle InfoBundle = new Bundle();
        InfoBundle.putString(TagString_Key, TagText);

        tagSearchArticleFragment = new TagSearchArticleFragment();
        tagSearchArticleFragment.setArguments(InfoBundle);
        fragmentManager.beginTransaction().replace(ReplaceFrameID, tagSearchArticleFragment, "tagSearchArticleFragment").addToBackStack(null).commit();
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