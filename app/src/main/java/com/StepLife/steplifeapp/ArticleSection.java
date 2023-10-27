package com.StepLife.steplifeapp;

import static com.StepLife.steplifeapp.ui.home.HomeFragment.Bundle_Section_Tag;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.StepLife.steplifeapp.other.Section;
import com.StepLife.steplifeapp.ui.Article;
import com.StepLife.steplifeapp.ui.ArticleListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.transition.MaterialSharedAxis;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ArticleSection extends Fragment {



    FloatingActionButton BacktoButton;
    String SectionID;
    TextView TextSectionAbout,NameTextSection;
    ListView SectionArticleListView;
    ArrayList <Article> listTemp = new ArrayList<>();
    ArticleListAdapter articleListAdapter;
    ProgressBar progressBar;
    DatabaseReference firebaseDatabase;

    Section Csection;
    public static final String AllSectionDB = "AllArticleSection";


    public ArticleSection() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setEnterTransition(new MaterialSharedAxis(MaterialSharedAxis.Z, true));
        setReturnTransition(new MaterialSharedAxis(MaterialSharedAxis.Z, true));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_article_section, container, false);


    }

    private void DownloadSection(){
        progressBar.setVisibility(View.VISIBLE);
        firebaseDatabase = FirebaseDatabase.getInstance().getReference(AllSectionDB).child(SectionID);
        firebaseDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    Csection = task.getResult().getValue(Section.class);
                    LoadSection();
                }else {

                }
            }
        });
    }

    private void LoadSection(){
        if(Csection!=null){
            NameTextSection.setText(Csection.SectionName);
            TextSectionAbout.setText(Csection.AboutSection);
            listTemp = (ArrayList<Article>) Csection.articleList;
            articleListAdapter = new ArticleListAdapter(getContext(),R.layout.listviewarticleitem, listTemp);
            SectionArticleListView.setAdapter(articleListAdapter);
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Получения ID для загрузки раздела
        Bundle InfoBundle;
        InfoBundle = getArguments();
        SectionID = InfoBundle.getString(Bundle_Section_Tag);

        NameTextSection = view.findViewById(R.id.NameTextSection);
        TextSectionAbout = view.findViewById(R.id.TextSectionAbout);
        SectionArticleListView = view.findViewById(R.id.SectionArticleListView);
        SectionArticleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //    Bundle Bundle = new Bundle();
                Article DowArticle = listTemp.get(position);
                // создание объекта Intent для запуска ChooseArticle
                Intent intent = new Intent(getContext(), ChooseArticle.class);
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
        progressBar = view.findViewById(R.id.progressBar);
        DownloadSection();


        //Кнопка возвращения
        BacktoButton = view.findViewById(R.id.BacktoButton);
        BacktoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }
}