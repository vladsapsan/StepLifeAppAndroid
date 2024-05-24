package com.StepLife.steplifeapp.ui.home;

import static com.StepLife.steplifeapp.StafFunction.Edit.HomeArticleRedactActivity.Section1_Article_Key;
import static com.StepLife.steplifeapp.StafFunction.Edit.HomeArticleRedactActivity.Section2_Article_Key;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.StepLife.steplifeapp.MainEnterenceActivity.MainActivity;
import com.StepLife.steplifeapp.R;
import com.StepLife.steplifeapp.StafFunction.Edit.HomeArticleRedactActivity;
import com.StepLife.steplifeapp.Model.LightArticle;
import com.StepLife.steplifeapp.other.SectionArticleViewAdapter;
import com.StepLife.steplifeapp.Model.VideoPlayer;
import com.StepLife.steplifeapp.ui.Animation.FragmentAnimation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import aglibs.loading.skeleton.layout.SkeletonLinearLayout;


public class HomeFragment extends Fragment implements  FragmentAnimation {

    TextView TextviewSectionName1,TextviewSectionName2;
    ImageView VideoHolder;
    CardView buttonConnect;
    SkeletonLinearLayout CardArticleSection1,CardArticleSection2,SkeletonCards1,SkeletonCards2;
    LinearLayout ShoolStepButton;
    private DatabaseReference mDataBase;
    CardView Card1Next,Card2Next;
    ToggleButton VideoSoundButton;
    LiveData<ArrayList<LightArticle>> ArticlelistTemp;
    ArrayList <LightArticle> ArticlelistTemp1 = new ArrayList<>();
    VideoPlayer HomeVideoView;
    HomeViewModel viewModel;
    String Section1ID,Section2ID;
    private String Article_Key ="AllArticle";
    public final static String Bundle_Section_Tag ="SectionInfo";
    private MainActivity mainActivity;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    private void VideoStart(){
        HomeVideoView.setVideoURI((viewModel.GetHomeVideoPath(getActivity().getPackageName())));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentAnimation.SetAnimation(this);
    }
    //Видео плеер
    private void InitVideoPlayer(@NonNull View view){
        HomeVideoView = view.findViewById(R.id.HomeVideoView);
        VideoHolder = view.findViewById(R.id.VideoHolder);
        VideoSoundButton = view.findViewById(R.id.VideoSoundButton);
        VideoSoundButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b==true){
                    HomeVideoView.unmute();
                }else {
                        HomeVideoView.mute();
                }
            }
        });
        VideoStart();
    }
    private void InitSection(@NonNull View view){
        //Название раздела
        TextviewSectionName1 = view.findViewById(R.id.TextviewSectionName1);
        //Карточка раздела 1
        CardArticleSection1 = view.findViewById(R.id.CardArticleSection1);
        CardArticleSection1.startLoading();
        CardArticleSection1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Section1ID!=null) {
                    MainActivity.LoadSectionFragment(Section1ID,getActivity().getSupportFragmentManager(),R.id.HomeFragment);
                }
            }
        });
        //Второй раздел название
        TextviewSectionName2 = view.findViewById(R.id.TextviewSectionName2);
        CardArticleSection2 = view.findViewById(R.id.CardArticleSection2);
        CardArticleSection2.startLoading();
        CardArticleSection2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Section2ID!=null) {
                    MainActivity.LoadSectionFragment(Section2ID,getActivity().getSupportFragmentManager(),R.id.HomeFragment);
                }
            }
        });
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        LiveData<ArrayList<LightArticle>> ArticleList1data = viewModel.getData("1");
        ArticleList1data.observe(getViewLifecycleOwner(), new Observer<ArrayList<LightArticle>>() {
            @Override
            public void onChanged(ArrayList<LightArticle> lightArticles) {
                //Изменение данных?
                HomeViewModel.InitRecycleView(view.findViewById(R.id.RecycleviewSectionArticle1),getContext(),lightArticles,getActivity().getSupportFragmentManager());
            }
        });
        LiveData<ArrayList<LightArticle>> ArticleList2data = viewModel.getData("2");
        ArticleList2data.observe(getViewLifecycleOwner(), new Observer<ArrayList<LightArticle>>() {
            @Override
            public void onChanged(ArrayList<LightArticle> lightArticles) {
                //Изменение данных?
                HomeViewModel.InitRecycleView(view.findViewById(R.id.RecycleviewSectionArticle2),getContext(),lightArticles,getActivity().getSupportFragmentManager());
            }
        });

        InitVideoPlayer(view);
        InitSection(view);


        //Кнопка перехода к подключению модуля
        buttonConnect = view.findViewById(R.id.buttonConnect);
        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.setFragment(mainActivity.getFragment(2), "2", 0);
            }
        });
        //Кнопка перехода в школу ходьбы
        ShoolStepButton = view.findViewById(R.id.ShoolStepButton);
        ShoolStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.setFragment(mainActivity.getFragment(3), "3",2);
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
    }




    private void DownloadHomeFragmentData(){

        mDataBase.child(Section1_Article_Key).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().getValue().toString()!=null) {
                        Section1ID = task.getResult().getValue().toString();
                        //   HomeArticleRedactActivity.DownloadHomeSection(HomeArticleRedactActivity.Section1_Article_Key,TextviewSectionName1,ArticlelistTemp,sectionArticleViewAdapter,CardArticleSection1,SkeletonCards1,Card1Next);
                    }
                }
            }
        });
        mDataBase.child(Section2_Article_Key).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().getValue().toString()!=null) {
                        Section2ID = task.getResult().getValue().toString();
                      //  HomeArticleRedactActivity.DownloadHomeSection(Section2_Article_Key,TextviewSectionName2,ArticlelistTemp1,sectionArticleViewAdapter1,CardArticleSection2,SkeletonCards2,Card2Next);
                    }
                }
            }
        });
    }

}