package com.StepLife.steplifeapp.StafFunction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.StepLife.steplifeapp.R;

public class EditArticleSection extends AppCompatActivity {


    ImageView imagebackAddArticle;                      
    CardView GoToFinalViewButton;
    Button AddTagsArticle;
    EditText TextEditHeader,TextEditMainText;
    String MainText,HeadText,ArticleID;

    protected void EnterArticleInformation(){
        
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_article_section);

        TextEditHeader = findViewById(R.id.TextEditHeader);
        TextEditMainText = findViewById(R.id.TextEditMainText);

        //Кнопка продолжения
        GoToFinalViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        imagebackAddArticle = findViewById(R.id.imagebackAddArticle);
        imagebackAddArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditArticleSection.this.onBackPressed();
            }
        });
    }
}