package com.StepLife.steplifeapp.StafFunction.Edit;

import static com.StepLife.steplifeapp.StafFunction.Edit.HomeArticleRedactActivity.Section1_Article_Key;
import static com.StepLife.steplifeapp.StafFunction.Edit.HomeArticleRedactActivity.Section2_Article_Key;
import static com.StepLife.steplifeapp.StafFunction.Edit.HomeArticleRedactActivity.Section3_Article_Key;
import static com.StepLife.steplifeapp.StafFunction.Edit.HomeArticleRedactActivity.Section_Article_Key;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.StepLife.steplifeapp.EnterenceActivity.AllArticleViewModel;
import com.StepLife.steplifeapp.R;
import com.StepLife.steplifeapp.Model.LightArticle;
import com.StepLife.steplifeapp.other.NetworkChangeListner;
import com.StepLife.steplifeapp.Model.Section;
import com.StepLife.steplifeapp.Adapters.SectionArticleViewAdapter;
import com.StepLife.steplifeapp.Model.Article;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

import aglibs.loading.skeleton.layout.SkeletonLinearLayout;

public class TopPostRedactActivity extends AppCompatActivity {

    Button SaveTopPostRedactButton,SelectSectionHomeButton1,SelectSectionHomeButton2,SelectSectionHomeButton3;
    TextView TextviewSectionName1,TextviewSectionName2,TextviewSectionName3,ChooseTextView;
    NetworkChangeListner networkChangeListner;
    ImageView imagebackEditTopPost;

    private static final String TopPost_Key ="TopPostArticle";
    protected static final String AllSection_Key ="AllArticleSection";
    private static final String Library_Key ="Lib";
    private static final String Library_Row1_Key ="Row1";
    private static final String Library_Row2_Key ="Row2";
    private DatabaseReference mDataBase;
    CardView TopPostCard1,TopPostCard2,TopPostCard3,TopPostCard4,TopPostCard5;
    ProgressBar progressBar;
    TextView LastEditText,PostTextRow1,PostTextRow2;

    private AllArticleViewModel mViewModel;
    private ImageView backbutton;
    private CardView articlecard;
    private com.StepLife.steplifeapp.Adapters.ArticleListAdapter ArticleListAdapter;
    private ListView allArticlelist;
    private ArrayAdapter<String> adapter;

    private List<String> listData;
    int ChooseCard;
    private ArrayList<String> listTemp = new ArrayList<>();
    private ArrayList<Section> SectionlistTemp = new ArrayList<>();
    private ArrayList<LightArticle> ArticlelistTemp1 = new ArrayList<>();
    private ArrayList<LightArticle> ArticlelistTemp2 = new ArrayList<>();
    private ArrayList<LightArticle> ArticlelistTemp3 = new ArrayList<>();
    Article DowArticle;
    ProgressBar progressBarTopPostEdit;
    Uri DownloadphotoUri;
    BottomSheetDialog bottomSheetDialog;

    private Target mTarget;
    int ChooseSection;
    SectionArticleViewAdapter sectionArticleViewAdapter1,sectionArticleViewAdapter2,sectionArticleViewAdapter3;
    RecyclerView RecycleviewSectionArticle1,RecycleviewSectionArticle2,RecycleviewSectionArticle3;
    ArrayAdapter<String> SectionAdapter;

    private String Article_Key ="AllArticle";

    EditText NameTopPost,SecNameTopPost;

    //Иницилизация компонентов
    private void initilization()
    {
        mDataBase = FirebaseDatabase.getInstance().getReference();
        SectionAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, listTemp);
        allArticlelist.setAdapter(SectionAdapter);

        //Да это работает так)
        LinearLayoutManager layoutManager = new LinearLayoutManager(TopPostRedactActivity.this,LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(TopPostRedactActivity.this,LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(TopPostRedactActivity.this,LinearLayoutManager.HORIZONTAL, false);

        //первый раздел столбец данных
        RecycleviewSectionArticle1 = findViewById(R.id.RecycleviewSectionArticle);
        RecycleviewSectionArticle1.setLayoutManager(layoutManager);
        sectionArticleViewAdapter1 = new SectionArticleViewAdapter(TopPostRedactActivity.this,ArticlelistTemp1);
        RecycleviewSectionArticle1.setAdapter(sectionArticleViewAdapter1);

        //второй раздел столбец данных
        RecycleviewSectionArticle2 = findViewById(R.id.RecycleviewSectionArticle2);
        RecycleviewSectionArticle2.setLayoutManager(layoutManager2);
        sectionArticleViewAdapter2 = new SectionArticleViewAdapter(TopPostRedactActivity.this,ArticlelistTemp2);
        RecycleviewSectionArticle2.setAdapter(sectionArticleViewAdapter2);

        //второй раздел столбец данных
        RecycleviewSectionArticle3 = findViewById(R.id.RecycleviewSectionArticle3);
        RecycleviewSectionArticle3.setLayoutManager(layoutManager3);
        sectionArticleViewAdapter3 = new SectionArticleViewAdapter(TopPostRedactActivity.this,ArticlelistTemp3);
        RecycleviewSectionArticle3.setAdapter(sectionArticleViewAdapter3);
    }

    //Загрузка разделов из базы
    private void DownloadSectionFirebaseData()
    {
        DatabaseReference mDataBase = FirebaseDatabase.getInstance().getReference(Section_Article_Key);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(listTemp.size()>0) listTemp.clear();
                for (DataSnapshot ds : snapshot.getChildren())
                {
                    Section section = ds.getValue(Section.class);
                    //Проверка
                    assert section != null;
                    listTemp.add(section.SectionName);
                    SectionlistTemp.add(section);
                }
                SectionAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        mDataBase.addValueEventListener(valueEventListener);
    }

    public static void DownloadArticleFirebaseData(DatabaseReference mDataBase,ArrayList<LightArticle> ListArticle,SectionArticleViewAdapter sectionArticleViewAdapter,CardView OpenSectionCard)
    {
        ListArticle.clear();
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count = 0;
                if(ListArticle.size()>0) ListArticle.clear();
                for (DataSnapshot ds : snapshot.getChildren())
                {
                    if(count>=3){
                        if(OpenSectionCard!=null){
                            OpenSectionCard.setVisibility(View.VISIBLE);
                        }
                        break;
                    }
                    LightArticle article = ds.getValue(LightArticle.class);
                    //Проверка
                    assert article != null;
                    ListArticle.add(article);
                    count++;
                }
                sectionArticleViewAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        mDataBase.addValueEventListener(valueEventListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_post_redact);



        //Аунтефикация
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser cUser = mAuth.getCurrentUser();


        //Текст перед карточками

        LastEditText = findViewById(R.id.LastEditText);


        progressBar = findViewById(R.id.progressBar);
        TextviewSectionName1 = findViewById(R.id.TextviewSectionName1);
        TextviewSectionName2 = findViewById(R.id.TextviewSectionName2);
        TextviewSectionName3 = findViewById(R.id.TextviewSectionName3);


        //Плашка выбора раздела для загрузки
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetDialog);
        View bottomSheetView = LayoutInflater.from(this.getApplicationContext())
                .inflate(
                        R.layout.sheetchoosearticles,
                        (FrameLayout) findViewById(R.id.SheetDialogChooseArticleContainer)
                );
        ChooseTextView = bottomSheetView.findViewById(R.id.ChooseTextView);
        ChooseTextView.setText("Выберите раздел для загрузки");
        allArticlelist = bottomSheetView.findViewById(R.id.AllArticleListview);
        allArticlelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                bottomSheetDialog.dismiss();
                if(ChooseSection==1){
                    //Загружаем выбранный раздел в бд
                    mDataBase.child(Library_Key).child(TopPost_Key).child(Section1_Article_Key).setValue(SectionlistTemp.get(position).SectionID).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                DownloadSection(ArticlelistTemp1,SectionlistTemp.get(position).SectionID,TextviewSectionName1,sectionArticleViewAdapter1,progressBar,imagebackEditTopPost,mDataBase,
                                        null,null,null);
                                Toast.makeText(getApplicationContext(),"Раздел успешно загружен",Toast.LENGTH_SHORT).show();

                            }else {
                                Toast.makeText(getApplicationContext(),"Ошибка загрузки раздела",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else if (ChooseSection==2) {
                    //Загружаем выбранный раздел в бд
                    mDataBase.child(Library_Key).child(TopPost_Key).child(Section2_Article_Key).setValue(SectionlistTemp.get(position).SectionID).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                DownloadSection(ArticlelistTemp2,SectionlistTemp.get(position).SectionID,TextviewSectionName2,sectionArticleViewAdapter2,progressBar,imagebackEditTopPost,mDataBase,
                                        null,null,null);
                                Toast.makeText(getApplicationContext(),"Раздел успешно загружен",Toast.LENGTH_SHORT).show();

                            }else {
                                Toast.makeText(getApplicationContext(),"Ошибка загрузки раздела",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else if (ChooseSection==3) {
                    //Загружаем выбранный раздел в бд
                    mDataBase.child(Library_Key).child(TopPost_Key).child(Section3_Article_Key).setValue(SectionlistTemp.get(position).SectionID).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                DownloadSection(ArticlelistTemp3,SectionlistTemp.get(position).SectionID,TextviewSectionName3,sectionArticleViewAdapter3,progressBar,imagebackEditTopPost,mDataBase,
                                        null,null,null);
                                Toast.makeText(getApplicationContext(),"Раздел успешно загружен",Toast.LENGTH_SHORT).show();

                            }else {
                                Toast.makeText(getApplicationContext(),"Ошибка загрузки раздела",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        bottomSheetDialog.setContentView(bottomSheetView);

        //Выбор первого раздела
        SelectSectionHomeButton1 = findViewById(R.id.SelectSectionHomeButton1);
        SelectSectionHomeButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChooseSection=1;
                bottomSheetDialog.show();

            }
        });

        //Выбор первого раздела
        SelectSectionHomeButton2 = findViewById(R.id.SelectSectionHomeButton2);
        SelectSectionHomeButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChooseSection=2;
                bottomSheetDialog.show();

            }
        });

        //Выбор первого раздела
        SelectSectionHomeButton3 = findViewById(R.id.SelectSectionHomeButton3);
        SelectSectionHomeButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChooseSection=3;
                bottomSheetDialog.show();

            }
        });

        initilization();
        DownloadSectionFirebaseData();



        //закрытие окна
        imagebackEditTopPost = findViewById(R.id.imagebackEditTopPost);


        imagebackEditTopPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }


    public static void DownloadSection(ArrayList<LightArticle> List, String SectionID, TextView textView, SectionArticleViewAdapter sectionArticleViewAdapter, ProgressBar DownloadPRogress, ImageView imagebackButton,
                                       DatabaseReference mDataBase, SkeletonLinearLayout skeletonName,SkeletonLinearLayout skeletonCard,CardView OpenSectionCard){
        if(imagebackButton!=null) {
            imagebackButton.setVisibility(View.GONE);
        }
        DownloadArticleFirebaseData(mDataBase.child(AllSection_Key).child(SectionID).child("articleList"),List,sectionArticleViewAdapter,OpenSectionCard);
        mDataBase.child(AllSection_Key).child(SectionID).child("SectionName").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    textView.setText(task.getResult().getValue().toString());
                    if(imagebackButton!=null) {
                        imagebackButton.setVisibility(View.VISIBLE);
                    }
                    if(skeletonName!=null){
                        skeletonName.stopLoading();
                    }
                    if(skeletonCard!=null){
                        skeletonCard.stopLoading();
                        skeletonCard.setVisibility(View.GONE);
                    }
                }
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        mDataBase.child(Library_Key).child(TopPost_Key).child(Section1_Article_Key).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    DownloadSection(ArticlelistTemp1,task.getResult().getValue().toString(),TextviewSectionName1,sectionArticleViewAdapter1,progressBar,imagebackEditTopPost,mDataBase,null,null,null);
                }
            }
        });
        mDataBase.child(Library_Key).child(TopPost_Key).child(Section2_Article_Key).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    DownloadSection(ArticlelistTemp2,task.getResult().getValue().toString(),TextviewSectionName2,sectionArticleViewAdapter2,progressBar,imagebackEditTopPost,mDataBase,null,null,null);
                }
            }
        });
        mDataBase.child(Library_Key).child(TopPost_Key).child(Section3_Article_Key).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    DownloadSection(ArticlelistTemp3,task.getResult().getValue().toString(),TextviewSectionName3,sectionArticleViewAdapter3,progressBar,imagebackEditTopPost,mDataBase,null,null,null);
                }
            }
        });
    }



    @Override
    protected void onStop() {
        super.onStop();

    }
}