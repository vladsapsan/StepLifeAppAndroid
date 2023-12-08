package com.StepLife.steplifeapp.StafFunction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.StepLife.steplifeapp.R;
import com.StepLife.steplifeapp.other.LightArticle;
import com.StepLife.steplifeapp.other.Section;
import com.StepLife.steplifeapp.ui.Article;
import com.StepLife.steplifeapp.ui.ArticleListAdapter;
import com.StepLife.steplifeapp.ui.LightArticleListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddNewSection extends AppCompatActivity {


    EditText NameSectionEditText,AboutSectionEditText;
    Button AddArticleSectionButton,CreateSectionButton;
    ListView allArticlelist,allArticlelistSection;
    private List<String> listData;
    private List<String> ListSelectChips;
    private LightArticleListAdapter ArticleListAdapter,ChooseArticleListAdapter;
    ValueEventListener valueEventListener;
    private ArrayList <LightArticle> listTemp = new ArrayList<>();
    private ArrayList <LightArticle> listChoose = new ArrayList<>();
    private DatabaseReference mDataBase;
    private static final String Article_Key ="AllArticle";
    private static final String Section_Article_Key ="AllArticleSection";

    //Инициализация компонентов базы данных
    void initialization(){
        mDataBase = FirebaseDatabase.getInstance().getReference(Article_Key);
        //Лист всех статей
        ArticleListAdapter = new LightArticleListAdapter(this,R.layout.listviewarticleitem, listTemp);
        allArticlelist.setAdapter(ArticleListAdapter);
        //Листа выбранных статей
        allArticlelistSection = findViewById(R.id.allArticlelistSection);
        ChooseArticleListAdapter = new LightArticleListAdapter(this,R.layout.listviewarticleitem, listChoose);
        allArticlelistSection.setAdapter(ChooseArticleListAdapter);
    }

    //Загрузка уроков из базы
    private void DownloadArticleFirebaseData()
    {
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(listTemp.size()>0) listTemp.clear();
                for (DataSnapshot ds : snapshot.getChildren())
                {
                    LightArticle article = ds.getValue(LightArticle.class);
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
        setContentView(R.layout.activity_add_new_section);




        //Поля ввода
        NameSectionEditText = findViewById(R.id.NameSectionEditText);
        AboutSectionEditText = findViewById(R.id.AboutSectionEditText);




        //Плашка выбора статьи для загрузки
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetDialog);
        View bottomSheetView = LayoutInflater.from(this.getApplicationContext())
                .inflate(
                        R.layout.sheetchoosearticles,
                        (FrameLayout) findViewById(R.id.SheetDialogChooseArticleContainer)
                );
        //Лист всех статей
        allArticlelist = bottomSheetView.findViewById(R.id.AllArticleListview);
        allArticlelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                listChoose.add(listTemp.get(position));
                listTemp.remove(position);
                ChooseArticleListAdapter.notifyDataSetChanged();
                ArticleListAdapter.notifyDataSetChanged();
                Toast.makeText(AddNewSection.this,"Статья добавлена",Toast.LENGTH_SHORT).show();
            }
        });
        //Инициализация компонентов
        bottomSheetDialog.setContentView(bottomSheetView);
        initialization();
        DownloadArticleFirebaseData();

        //Кнопка добавления статей в курс
        AddArticleSectionButton = findViewById(R.id.AddArticleSectionButton);
        AddArticleSectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.show();
            }
        });
        //Лист с выбранными статьями
        allArticlelistSection.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                listTemp.add(listChoose.get(position));
                listChoose.remove(position);
                ChooseArticleListAdapter.notifyDataSetChanged();
                ArticleListAdapter.notifyDataSetChanged();
                Toast.makeText(AddNewSection.this,"Статья убрана", Toast.LENGTH_SHORT).show();
            }
        });

        //Кнопка создания курса
        CreateSectionButton = findViewById(R.id.CreateSectionButton);
        CreateSectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(NameSectionEditText.getText().length()!=0){
                    if(AboutSectionEditText.getText().length()!=0&&AboutSectionEditText.getText().length()<=80){
                        if(listChoose.size()!=0){
                            //Все првоерки пройдены загружаем раздел в бд
                            mDataBase = FirebaseDatabase.getInstance().getReference(Section_Article_Key);
                            String IDSection = mDataBase.push().getKey();
                            Section newSection = new Section(NameSectionEditText.getText().toString(),AboutSectionEditText.getText().toString(),IDSection,listChoose);
                            mDataBase.child(IDSection).setValue(newSection).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(AddNewSection.this,"Раздел успешно добавлен",Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                        }else {
                            Toast.makeText(AddNewSection.this,"Лист статей внутри раздела не может быть пустым!",Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(AddNewSection.this,"Описание не можем быть пустым!",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(AddNewSection.this,"Название не может быть пустым!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}