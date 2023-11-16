package com.StepLife.steplifeapp;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.transition.MaterialFadeThrough;


public class ArticleFragment extends Fragment {

    TextView DownloadHeadText,MainTextDownloadArticle;
    RecyclerView RecycleviewTagsArticle;
    ImageView CloseArticleButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //анимация
        setExitTransition(new MaterialFadeThrough());
        setEnterTransition(new MaterialFadeThrough());
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



        DownloadHeadText = view.findViewById(R.id.DownloadHeadText);
        MainTextDownloadArticle = view.findViewById(R.id.MainTextDownloadArticle);

        CloseArticleButton = view.findViewById(R.id.CloseArticleButton);
        CloseArticleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        SetInfo();


    }

    private void SetInfo(){
        Bundle InfoBundle;
        InfoBundle = getArguments();
        DownloadHeadText.setText(Html.fromHtml((String) InfoBundle.getString("HeaderText"),new GlideImageGetter(DownloadHeadText),null));
        MainTextDownloadArticle.setText(Html.fromHtml((String) InfoBundle.getString("MainText"),new GlideImageGetter(MainTextDownloadArticle),null));
    }
}