package com.StepLife.steplifeapp.EnterenceActivity;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.StepLife.steplifeapp.R;
import com.StepLife.steplifeapp.garbage.TagSearchArticle;
import com.StepLife.steplifeapp.Adapters.MyRecyclerViewTagsAdapter;
import com.StepLife.steplifeapp.Model.Article;
import com.StepLife.steplifeapp.ui.notifications.NotificationsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import aglibs.loading.skeleton.layout.SkeletonLinearLayout;


public class ArticleFragment extends Fragment implements MyRecyclerViewTagsAdapter.ItemClickListener {

    TextView DownloadHeadText,MainTextDownloadArticle;
    RecyclerView RecycleviewTagsArticle;
    ArrayList mNewArticleTags = new ArrayList<>();
    MyRecyclerViewTagsAdapter myRecyclerViewTagsAdapter;
    SkeletonLinearLayout SkeletonLoader;
    LinearLayout CloseArticleButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_article, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //Отображение тегов
        RecycleviewTagsArticle = view.findViewById(R.id.RecycleviewTagsArticle);
        LinearLayoutManager layoutManager= new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false);
        RecycleviewTagsArticle.setLayoutManager(layoutManager);
        myRecyclerViewTagsAdapter = new MyRecyclerViewTagsAdapter(getContext(),mNewArticleTags);
        myRecyclerViewTagsAdapter.setClickListener(this::onItemClick);
        RecycleviewTagsArticle.setAdapter(myRecyclerViewTagsAdapter);


        SkeletonLoader = view.findViewById(R.id.SkeletonLoader);
        SkeletonLoader.startLoading();

        DownloadHeadText = view.findViewById(R.id.DownloadHeadText);
        MainTextDownloadArticle = view.findViewById(R.id.MainTextDownloadArticle);

        //Кнопка закрытия
        CloseArticleButton = view.findViewById(R.id.CloseArticleButton);
        CloseArticleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        SetBundleInfo();
        //  DownloadArticle(null,"-NgyG_wHf1lAofiHQ3Zx","1",true);

    }

    //Загружаем статью с помощью бандла из другого фрагмента
    private void SetBundleInfo(){
        Bundle InfoBundle;
        InfoBundle = getArguments();
        if(InfoBundle.getString("HeaderText")!=null) {
            setInfo(InfoBundle.getString("HeaderText"), InfoBundle.getString("MainText"), InfoBundle.getStringArrayList("TagList"));
        }else {
            DownloadArticle(InfoBundle.getString("articleID"),null,null,false);
        }
    }
    private void setInfo(String HeadText, String MainText, ArrayList<String> Tags){
        DownloadHeadText.setText(String.valueOf(Html.fromHtml((String) HeadText)).trim());
        MainTextDownloadArticle.setText(Html.fromHtml((String) MainText,new GlideImageGetter(MainTextDownloadArticle),null));
        if(Tags!=null){
            if(mNewArticleTags.size()==0) {
                mNewArticleTags.addAll( Tags);
                myRecyclerViewTagsAdapter.notifyDataSetChanged();
            }
        }
        SkeletonLoader.stopLoading();
        SkeletonLoader.setVisibility(View.GONE);
    }

    @Override
    public void onItemClick(View view, int position) {
        NotificationsFragment.StartChipActivity(myRecyclerViewTagsAdapter.getItem(position).toString(),getActivity().getSupportFragmentManager(),R.id.ArticleFragment);
    }

    //Загрузка статьи
    private void DownloadArticle(String ArticleID,String SectionID,String ArticleNumber,Boolean isFromSection){
        DatabaseReference mdatabase;
        if(isFromSection==false){
            //Загрузка из общего пула всех статей
            FirebaseDatabase.getInstance().getReference().child(TagSearchArticle.Article_Key).child(ArticleID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.isSuccessful()){
                        Article article = task.getResult().getValue(Article.class);
                        if(article!=null) {
                            setInfo(article.HeadText, article.MainText, article.TagList);
                        }
                    }
                }
            });
        }else {
            //Загрузка из пула статей раздела
            FirebaseDatabase.getInstance().getReference().child("AllArticleSection").child(SectionID).child("articleList").child(ArticleNumber)
                    .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.isSuccessful()){
                        Article article = task.getResult().getValue(Article.class);
                        setInfo(article.HeadText,article.MainText,article.TagList);
                    }

                }
            });
        }

    }
}