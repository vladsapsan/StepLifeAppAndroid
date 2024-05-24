package com.StepLife.steplifeapp.StafFunction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.StepLife.steplifeapp.R;
import com.StepLife.steplifeapp.StafFunction.Add.AddNewSection;
import com.StepLife.steplifeapp.StafFunction.Add.AddTagsArticle;
import com.StepLife.steplifeapp.StafFunction.Add.PublicationNewArticles;
import com.StepLife.steplifeapp.StafFunction.Edit.EditSection;
import com.StepLife.steplifeapp.StafFunction.Edit.HomeArticleRedactActivity;
import com.StepLife.steplifeapp.StafFunction.Edit.TopPostRedactActivity;
import com.StepLife.steplifeapp.other.NetworkChangeListner;

public class ArticleOnTopSettingsActivity extends AppCompatActivity {


    ImageView imagebackEditArticles;
    NetworkChangeListner networkChangeListner;

    Button TopPostRedactButton,HomeArticleRedactButton,AddTagsButton,PublicationArticleButton,AddSectionButton,DeleteSectionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_on_top_settings);




        Intent TopPostRedactintent = new Intent(getApplicationContext(), TopPostRedactActivity.class);
        Intent HomeArticleRedactintent = new Intent(getApplicationContext(), HomeArticleRedactActivity.class);
        Intent AddTagsArticletintent = new Intent(getApplicationContext(), AddTagsArticle.class);
        Intent PublicationNewArticlesintent = new Intent(getApplicationContext(), PublicationNewArticles.class);
        Intent AddNewSectionintent = new Intent(getApplicationContext(), AddNewSection.class);


        //переход к окну редактирования верхней предложки учебника
        PublicationArticleButton = findViewById(R.id.PublicationArticleButton);
        PublicationArticleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(PublicationNewArticlesintent);
            }
        });


        //переход к окну редактирования верхней предложки учебника
        TopPostRedactButton = findViewById(R.id.TopPostRedactButton);
        TopPostRedactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(TopPostRedactintent);
            }
        });

        HomeArticleRedactButton = findViewById(R.id.HomeArticleRedactButton);
        HomeArticleRedactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(HomeArticleRedactintent);
            }
        });
        //Переход к окну добавление тегов
        AddTagsButton = findViewById(R.id.AddTagsButton);
        AddTagsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(AddTagsArticletintent);
            }
        });

        //Переход к  добавлению курсов
        AddSectionButton = findViewById(R.id.AddSectionButton);
        AddSectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(AddNewSectionintent);
            }
        });
        //Окно редиктирования и удоления курсов
        DeleteSectionButton  = findViewById(R.id.DeleteSectionButton);
        DeleteSectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ArticleOnTopSettingsActivity.this, EditSection.class));
            }
        });
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