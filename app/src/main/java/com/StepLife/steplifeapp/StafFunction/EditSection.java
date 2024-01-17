package com.StepLife.steplifeapp.StafFunction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.StepLife.steplifeapp.R;
import com.StepLife.steplifeapp.other.Section;
import com.StepLife.steplifeapp.other.SectionViewAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EditSection extends AppCompatActivity {

    ImageView imagebackEditArticles;

    SectionViewAdapter sectionViewAdapter;
    ValueEventListener valueEventListener;
    private ArrayList<Section> listTemp = new ArrayList<Section>();

    private DatabaseReference mDataBase;
    private static final String Article_Key ="AllArticle";
    private static final String Section_Article_Key ="AllArticleSection";
    RecyclerView AllArticleEditListview;

    private void DownloadArticleFirebaseData()
    {
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(listTemp.size()>0) listTemp.clear();
                for (DataSnapshot ds : snapshot.getChildren())
                {
                    Section section = ds.getValue(Section.class);


                    assert section != null;
                    listTemp.add(section);
                }
                sectionViewAdapter.notifyDataSetChanged();
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
        setContentView(R.layout.activity_edit_section);

        //Диалог выбора редактирования
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetDialog);
        View bottomSheetView = LayoutInflater.from(this)
                .inflate(
                        R.layout.bottom_sheet_section_redation,
                        (FrameLayout) findViewById(R.id.SheetDialogRedactionSectionContainer)
                );
        //Кнопка редактирования материала
        bottomSheetView.findViewById(R.id.buttonEditArticle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
            }
        });
        //Кнопка удаления материала
        bottomSheetView.findViewById(R.id.buttonDeleteArticle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        bottomSheetDialog.setContentView(bottomSheetView);


        //Нажатие на выбранный раздел
        SectionViewAdapter.ItemClickListener clickListener1 = new SectionViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                bottomSheetDialog.show();
            }
        };



        //Лист всех статей
        AllArticleEditListview = findViewById(R.id.AllArticleEditListview);
        AllArticleEditListview.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));
        sectionViewAdapter = new SectionViewAdapter(this,listTemp);
        sectionViewAdapter.setClickListener(clickListener1);
        AllArticleEditListview.setAdapter(sectionViewAdapter);

        //Загружаем информацию
        mDataBase = FirebaseDatabase.getInstance().getReference(Section_Article_Key);
        DownloadArticleFirebaseData();

        imagebackEditArticles = findViewById(R.id.imagebackEditArticles);
        imagebackEditArticles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}