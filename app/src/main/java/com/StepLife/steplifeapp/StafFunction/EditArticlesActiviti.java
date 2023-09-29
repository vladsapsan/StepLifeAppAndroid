package com.StepLife.steplifeapp.StafFunction;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.StepLife.steplifeapp.AllArticleViewModel;
import com.StepLife.steplifeapp.R;
import com.StepLife.steplifeapp.other.NetworkChangeListner;
import com.StepLife.steplifeapp.ui.Article;
import com.StepLife.steplifeapp.ui.ArticleListAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

public class EditArticlesActiviti extends AppCompatActivity {

    ImageView imagebackEditArticles;
    private AllArticleViewModel mViewModel;
    private ImageView backbutton;
    private CardView articlecard;
    private ArticleListAdapter ArticleListAdapter;
    private ListView allArticlelist;
    private ArrayAdapter<String> adapter;
    int CurrnetPositionList ;


    private List<String> listData;
    NetworkChangeListner networkChangeListner;
    private ArrayList <Article> listTemp = new ArrayList<Article>();
    ProgressBar progressBar;
    Article DowArticle;
    Uri DownloadphotoUri;
    BottomSheetDialog bottomSheetDialog;

    private Target mTarget;
    private String Article_Key ="AllArticle";
    private DatabaseReference mDataBase;

    //Иницилизация компонентов
    private void initilization()
    {
        allArticlelist = findViewById(R.id.AllArticleEditListview);
        listData = new ArrayList<>();
        mDataBase = FirebaseDatabase.getInstance().getReference(Article_Key);
        ArticleListAdapter = new ArticleListAdapter(this,R.layout.listviewarticleitem, listTemp);
        allArticlelist.setAdapter(ArticleListAdapter);
    }


    //Загрузка уроков из базы
    private void DownloadArticleFirebaseData()
    {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(listData.size()>0) listData.clear();
                if(listTemp.size()>0) listTemp.clear();
                for (DataSnapshot ds : snapshot.getChildren())
                {
                    Article article = ds.getValue(Article.class);
                    //Проверка
                    assert article != null;
                    listTemp.add(article);
                }
                ArticleListAdapter.notifyDataSetChanged();
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
        setContentView(R.layout.activity_edit_articles_activiti);


        //Дефолт стиль
        getWindow().setStatusBarColor(getResources().getColor(R.color.MainGray));
        getSupportActionBar().hide(); //Скрытие actionBar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

       // initilization();



        //Плашка удаления статьи
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetDialog);
        View bottomSheetView = LayoutInflater.from(this.getApplicationContext())
                .inflate(
                        R.layout.sheetdowndeletearticle,
                        (FrameLayout) findViewById(R.id.SheetDialogDeleteArticleContainer)
                );
        //Удаление статьи
        bottomSheetView.findViewById(R.id.DeleteArticleButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DowArticle = listTemp.get(CurrnetPositionList);
                //Поиск статьи для удаления по заголовку статьи
                Query Query = mDataBase.orderByChild("HeadText").equalTo(DowArticle.HeadText);
                Query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot Snapshot: dataSnapshot.getChildren()) {
                            //Удаление превью фото из базы
                            StorageReference photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(DowArticle.PreviewPhotoUri);
                            photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // File deleted successfully
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    Toast.makeText(getApplicationContext(),"Ошибка удаления обложки!",Toast.LENGTH_SHORT);
                                }
                            });
                            Snapshot.getRef().removeValue();
                            bottomSheetDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Статья удалена",Toast.LENGTH_SHORT);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(),"Ошибка удаления",Toast.LENGTH_SHORT);
                    }
                });
            }
        });
        //Отмена удаления
        bottomSheetView.findViewById(R.id.ExitDeleteArticleButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();

            }
        });
        bottomSheetDialog.setContentView(bottomSheetView);

        //Инициализация и загрузка компонентов
        initilization();
        DownloadArticleFirebaseData();



        //При выборе статьи
        allArticlelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CurrnetPositionList = position;
                bottomSheetDialog.show();
            }
        });

        //закрытие окна
        imagebackEditArticles = findViewById(R.id.imagebackEditArticles);
        imagebackEditArticles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }
}