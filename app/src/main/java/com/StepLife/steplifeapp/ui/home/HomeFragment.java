package com.StepLife.steplifeapp.ui.home;

import static com.StepLife.steplifeapp.StafFunction.HomeArticleRedactActivity.Section1_Article_Key;
import static com.StepLife.steplifeapp.StafFunction.HomeArticleRedactActivity.Section2_Article_Key;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.StepLife.steplifeapp.MainActivity;
import com.StepLife.steplifeapp.R;
import com.StepLife.steplifeapp.StafFunction.HomeArticleRedactActivity;
import com.StepLife.steplifeapp.other.LightArticle;
import com.StepLife.steplifeapp.other.SectionArticleViewAdapter;
import com.StepLife.steplifeapp.other.VideoPlayer;
import com.StepLife.steplifeapp.ui.Article;
import com.StepLife.steplifeapp.ui.ArticleListAdapter;
import com.StepLife.steplifeapp.ui.dashboard.DashboardFragment;
import com.StepLife.steplifeapp.ui.notifications.NotificationsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.transition.MaterialFadeThrough;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

import aglibs.loading.skeleton.layout.SkeletonLinearLayout;


public class HomeFragment extends Fragment implements SectionArticleViewAdapter.ItemClickListener, MediaPlayer.OnPreparedListener {
    FrameLayout FrameVideo;
    TextView TextBtnHide,AllAcricleButton,TextviewSectionName1,TextviewSectionName2;
    private String HomeArticle_Key ="HomeArticle";
    private String Library_Key ="Lib";
    ImageView VideoHolder;
    Article DowArticle1,DowArticle2,DowArticle3,DowArticle4,DowArticle5;
    final private static String DBase_Code = "AllArticle";
    final private static String DBase_HomeTopArticleCode = "HomeTopArticle";
    private ArrayList<Article> listTemp = new ArrayList<Article>();
    CardView buttonConnect;
    SkeletonLinearLayout CardArticleSection1,CardArticleSection2,SkeletonCards1,SkeletonCards2;
    LinearLayout ShoolStepButton;
    ToggleButton VideoVolumeButton;
    HorizontalScrollView HomeArticleScroll;
    //Фрагмент школа Ходьбы
    NotificationsFragment notificationsFragment;
    private ImageView VideoStartButton;
    private FirebaseAuth mAuth;
    DashboardFragment dashboardFragment;

    final static String HomeVideoUri = "/SupportFiles/Steplife P5.mp4";
    private DatabaseReference mDataBase;
    CardView ProfileUserButton,Card1Next,Card2Next;
    ArticleListAdapter ArticleListAdapter;
    ImageView imageviewprofile;
    ToggleButton VideoSoundButton;
    RecyclerView RecycleviewSectionArticle1,RecycleviewSectionArticle2;
    MediaController MediaController ;
    ArrayList <LightArticle> ArticlelistTemp = new ArrayList<>();
    ArrayList <LightArticle> ArticlelistTemp1 = new ArrayList<>();
    VideoPlayer HomeVideoView;
    SectionArticleViewAdapter sectionArticleViewAdapter,sectionArticleViewAdapter1;
    private List<String> listData;
    String Section1ID,Section2ID;
    Animation animationIN;
    private String Article_Key ="AllArticle";
    public final static String Bundle_Section_Tag ="SectionInfo";
    private MainActivity mainActivity;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    //Иницилизация компонентов
    private void initilization()
    {
        //Аунтефикация
        mAuth = FirebaseAuth.getInstance();
        mainActivity = (MainActivity) getActivity();
    }
    private void FireBaseVideoStart(){
        //Получение ссылки и запуск видео
        FirebaseStorage.getInstance().getReference().child(HomeVideoUri).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
        {
            @Override
            public void onSuccess(Uri downloadUrl)
            {
                HomeVideoView.setVideoURI((downloadUrl));
                HomeVideoView.start();
            }
        });
    }
    private void VideoStart(){
          final Uri videopath =Uri.parse( "android.resource://" + getActivity().getPackageName() + "/" + R.raw.steplifevideo);
        Log.d("VideoM", videopath.toString());
        //Получение ссылки и запуск видео
        HomeVideoView.setVideoURI((videopath));

      //  HomeVideoView.setOnPreparedListener(PreparedListener);
    }


    MediaPlayer.OnPreparedListener PreparedListener = new MediaPlayer.OnPreparedListener(){

        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            try {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = new MediaPlayer();
                }
                mediaPlayer.seekTo(1);
                mediaPlayer.setVolume(1f, 1f);
                mediaPlayer.setLooping(false);
                mediaPlayer.start();
                VideoHolder.setVisibility(View.GONE);
            } catch (Exception e) {
                Log.d("VideoCheck", e.toString());
            }
        }
    };

    MediaPlayer.OnCompletionListener completionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            try {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.setVolume(0f,0f);
                }
            } catch (Exception e) {
                Log.d("VideoCheck", e.toString());
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //анимация
        setExitTransition(new MaterialFadeThrough());
        setEnterTransition(new MaterialFadeThrough());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);

        initilization();
         


        SkeletonCards1 =  view.findViewById(R.id.SkeletonCards1);
        SkeletonCards2 =  view.findViewById(R.id.SkeletonCards2);

        RecycleviewSectionArticle1 = view.findViewById(R.id.RecycleviewSectionArticle1);
        LinearLayoutManager layoutManager1= new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false);
        RecycleviewSectionArticle1.setLayoutManager(layoutManager1);
        sectionArticleViewAdapter = new SectionArticleViewAdapter(getContext(),ArticlelistTemp);
        sectionArticleViewAdapter.setClickListener(this::onItemClick);
        RecycleviewSectionArticle1.setAdapter(sectionArticleViewAdapter);

        RecycleviewSectionArticle2 = view.findViewById(R.id.RecycleviewSectionArticle2);
        LinearLayoutManager layoutManager= new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false);
        RecycleviewSectionArticle2.setLayoutManager(layoutManager);
        sectionArticleViewAdapter1 = new SectionArticleViewAdapter(getContext(),ArticlelistTemp1);
        sectionArticleViewAdapter1.setClickListener(this::onItemClick);
        RecycleviewSectionArticle2.setAdapter(sectionArticleViewAdapter1);





        //Инициализация анимации



        //Видео плеер окна
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
        //Кнопка регулирования звука видео



       // HomeVideoView.setMediaController(MediaController);





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
        Card1Next = view.findViewById(R.id.Card1Next);
        Card1Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Section1ID!=null) {
                    MainActivity.LoadSectionFragment(Section1ID,getActivity().getSupportFragmentManager(),R.id.HomeFragment);
                }
            }
        });
        //Карточка раздела 1
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
        Card2Next = view.findViewById(R.id.Card2Next);
        Card2Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Section2ID!=null) {
                    MainActivity.LoadSectionFragment(Section2ID,getActivity().getSupportFragmentManager(),R.id.HomeFragment);
                }
            }
        });








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

        //Второй раздел название
        TextviewSectionName2 = view.findViewById(R.id.TextviewSectionName2);

        DownloadHomeFragmentData();


        VideoStart();
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    private void DownloadHomeFragmentData(){
        mDataBase = FirebaseDatabase.getInstance().getReference().child(Library_Key).child(HomeArticle_Key);
        mDataBase.child(Section1_Article_Key).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().getValue().toString()!=null) {
                        Section1ID = task.getResult().getValue().toString();
                        HomeArticleRedactActivity.DownloadHomeSection(HomeArticleRedactActivity.Section1_Article_Key,TextviewSectionName1,ArticlelistTemp,sectionArticleViewAdapter,CardArticleSection1,SkeletonCards1,Card1Next);
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
                        HomeArticleRedactActivity.DownloadHomeSection(Section2_Article_Key,TextviewSectionName2,ArticlelistTemp1,sectionArticleViewAdapter1,CardArticleSection2,SkeletonCards2,Card2Next);
                    }
                }
            }
        });
    }

    //Нажатие на карточку статьи
    @Override
    public void onItemClick(View view, int position) {
        if(ArticlelistTemp.get(position)!=null) {
            MainActivity.LoadArticleFragmentFromID(ArticlelistTemp.get(position).id ,getActivity().getSupportFragmentManager(),R.id.HomeFragment);
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                if (what == MediaPlayer.MEDIA_INFO_METADATA_UPDATE)  {
                    // video started; hide the placeholder.
                    VideoHolder.setVisibility(View.GONE);
                    return true;
                }
                if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START)  {
                    // video started; hide the placeholder.
                    VideoHolder.setVisibility(View.GONE);
                    return true;
                }
                return false;
            }
        });
    }
}