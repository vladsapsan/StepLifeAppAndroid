package com.StepLife.steplifeapp.StafFunction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
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
import com.StepLife.steplifeapp.ChooseArticle;
import com.StepLife.steplifeapp.R;
import com.StepLife.steplifeapp.other.NetworkChangeListner;
import com.StepLife.steplifeapp.ui.Article;
import com.StepLife.steplifeapp.ui.ArticleListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

public class PublicationNewArticles extends AppCompatActivity {

    ImageView imagebackEditArticles;
    private AllArticleViewModel mViewModel;
    private ImageView backbutton;
    private CardView articlecard;
    private com.StepLife.steplifeapp.ui.ArticleListAdapter ArticleListAdapter;
    private ListView AllNonPublicateArticleEditListview;
    private ArrayAdapter<String> adapter;
    int CurrnetPositionList ;
    private List<String> listData;
    NetworkChangeListner networkChangeListner;
    private ArrayList<Article> listTemp = new ArrayList<Article>();
    ProgressBar progressBar;
    Article DowArticle;
    Uri DownloadphotoUri;
    BottomSheetDialog bottomSheetDialog;

    private Target mTarget;
    private final static String Article_Key ="AllArticle";
    private final static String Non_Public_Article_Key ="AllNonPublicArticle";
    private DatabaseReference mDataBase;

    //Иницилизация компонентов
    private void initilization()
    {
        AllNonPublicateArticleEditListview = findViewById(R.id.AllNonPublicateArticleEditListview);
        //Нажатие на статью
        AllNonPublicateArticleEditListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                bottomSheetDialog.show();
                CurrnetPositionList = position;
            }
        });
        listData = new ArrayList<>();
        mDataBase = FirebaseDatabase.getInstance().getReference(Non_Public_Article_Key);
        ArticleListAdapter = new ArticleListAdapter(this,R.layout.listviewarticleitem, listTemp);
        AllNonPublicateArticleEditListview.setAdapter(ArticleListAdapter);
    }


    //Загрузка неопубликованных уроков из базы
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
        setContentView(R.layout.activity_publication_new_articles);




        //Диалог действий с материалом
        bottomSheetDialog = new BottomSheetDialog(PublicationNewArticles.this, R.style.BottomSheetDialog);
        bottomSheetDialog.setDismissWithAnimation(true);
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        View bottomSheetView = LayoutInflater.from(this.getApplicationContext())
                .inflate(
                        R.layout.bottom_sheet_publication_new_article,
                        (FrameLayout) findViewById(R.id.SheetDialogPublicationNewArticleContainer)
                );
        //Кнопка просмотра материала
        bottomSheetView.findViewById(R.id.buttonShowNonArticle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DowArticle = listTemp.get(CurrnetPositionList);
                // создание объекта Intent для запуска ChooseArticle
                Intent intent = new Intent(getApplicationContext(), ChooseArticle.class);
                // передача объекта с ключом "MainText" и значением
                intent.putExtra("MainText",DowArticle.MainText);
                intent.putExtra("Date",DowArticle.Date);
                intent.putExtra("HeaderText", Html.fromHtml(DowArticle.HeadText).toString().trim());
                if(DowArticle.TagList!=null){
                    intent.putStringArrayListExtra("TagList", DowArticle.TagList);
                }
                // запуск ChooseArticle
                startActivity(intent);
            }
        });
        //Кнопка удаления
        bottomSheetView.findViewById(R.id.buttonDeleteArticle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
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
                                    Toast.makeText(PublicationNewArticles.this,"Ошибка удаления обложки!",Toast.LENGTH_SHORT);
                                }
                            });
                            Snapshot.getRef().removeValue();
                            Toast.makeText(PublicationNewArticles.this,"Статья удалена",Toast.LENGTH_SHORT);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(PublicationNewArticles.this,"Ошибка удаления",Toast.LENGTH_SHORT);
                    }
                });
            }
        });
        //Кнопка публикации статей
        bottomSheetView.findViewById(R.id.buttonPublicArticle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                DowArticle = listTemp.get(CurrnetPositionList);
                //Сначал удаляем из неопубликованных после уже загружаем в опубликованные
                Query Query = mDataBase.orderByChild("HeadText").equalTo(DowArticle.HeadText);
                Query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot Snapshot: dataSnapshot.getChildren()) {
                            //Удаление из неопубликованных завершено
                            Snapshot.getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    //Загружаем публикацию в основной пул
                                    mDataBase = FirebaseDatabase.getInstance().getReference(Article_Key);
                                    mDataBase.child(DowArticle.id).setValue(DowArticle).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(PublicationNewArticles.this,"Статья успешно опубликована",Toast.LENGTH_SHORT);
                                        }
                                    });
                                }
                            });
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(PublicationNewArticles.this,"Ошибка удаления",Toast.LENGTH_SHORT);
                    }
                });
            }
        });

        bottomSheetDialog.setContentView(bottomSheetView);




        //Инициализация и загрузка компонентов
        initilization();
        DownloadArticleFirebaseData();

        //закрытие окна
        imagebackEditArticles = findViewById(R.id.imagebackPublicationArticles);
        imagebackEditArticles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}