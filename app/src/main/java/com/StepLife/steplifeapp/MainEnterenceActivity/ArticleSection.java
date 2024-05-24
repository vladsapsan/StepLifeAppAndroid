package com.StepLife.steplifeapp.MainEnterenceActivity;

import static com.StepLife.steplifeapp.ui.home.HomeFragment.Bundle_Section_Tag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.StepLife.steplifeapp.Model.LightArticle;
import com.StepLife.steplifeapp.Model.Section;
import com.StepLife.steplifeapp.R;
import com.StepLife.steplifeapp.ui.LightArticleListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import aglibs.loading.skeleton.layout.SkeletonLinearLayout;

public class ArticleSection extends Fragment {
    LinearLayout BacktoButton;
    String SectionID;
    TextView TextSectionAbout,NameTextSection;
    ListView SectionArticleListView;
    SkeletonLinearLayout SkeletonLinearTextSection;
    ArrayList <LightArticle> listTemp = new ArrayList<>();
    LightArticleListAdapter articleListAdapter;
    DatabaseReference firebaseDatabase;
    Section Csection;
    public static final String AllSectionDB = "AllArticleSection";


    public ArticleSection() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_article_section, container, false);


    }

    private void DownloadSection(){

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
            listTemp = (ArrayList<LightArticle>) Csection.articleList;
            articleListAdapter = new LightArticleListAdapter(getContext(),R.layout.listviewarticleitem, listTemp);
            SectionArticleListView.setAdapter(articleListAdapter);
            SkeletonLinearTextSection.stopLoading();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Получения ID для загрузки раздела
        Bundle InfoBundle;
        InfoBundle = getArguments();
        SectionID = InfoBundle.getString(Bundle_Section_Tag);
        SkeletonLinearTextSection = view.findViewById(R.id.SkeletonLinearTextSection);
        SkeletonLinearTextSection.startLoading();
        NameTextSection = view.findViewById(R.id.NameTextSection);
        TextSectionAbout = view.findViewById(R.id.TextSectionAbout);
        SectionArticleListView = view.findViewById(R.id.SectionArticleListView);
        SectionArticleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                MainActivity.LoadArticleFragmentFromID(listTemp.get(position).id,getActivity().getSupportFragmentManager(),R.id.ArticleSectionFrame);
            }
        });

        DownloadSection();


        //Кнопка возвращения
        BacktoButton = view.findViewById(R.id.BacktoButton);
        BacktoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }
}