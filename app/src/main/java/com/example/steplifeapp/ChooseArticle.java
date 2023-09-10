package com.example.steplifeapp;

import static com.example.steplifeapp.AllArticle.getDeviceWidth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public class ChooseArticle extends AppCompatActivity {
    ImageView CloseArticleButton;
    ScrollView DownloadArticleScrollView;

    Uri DownloadphotoUri;
    StorageReference storageRef ;
    ProgressBar progressBar;
    Bitmap bitmap1 = null;

    Drawable drawable;
    SwipeRefreshLayout SwipeRefreshArticle;

    private Target mTarget;

    TextView DownloadHeadText,TextDateDownloadArticle,MainTextDownloadArticle;


    private class ImageGetter implements Html.ImageGetter {
        int countimage = 0;
        int loadedcount = 0;
        public Drawable getDrawable(String source) {
            int id;
            DownloadphotoUri = Uri.parse(source);
            bitmap1 = null;

            drawable = null;
            id = R.drawable.buttonimage;

            //Download file in Memory
         //   StorageReference islandRef = storageRef.child(source);
        //    final long ONE_MEGABYTE = 1024 * 1024;
        //    islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
         //       @Override
         //       public void onSuccess(byte[] bytes) {
          //          bitmap1 = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);;
          //          progressBar.setVisibility(View.GONE);
                    // Data for "images/island.jpg" is returns, use this as needed

         //       }
          //  }).addOnFailureListener(new OnFailureListener() {
          //      @Override
         //       public void onFailure(@NonNull Exception exception) {
          //          // Handle any errors
          //      }
        //    });


            mTarget = new Target() {

               @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    bitmap1 = bitmap;
                    drawable = new BitmapDrawable(getResources(),bitmap);
                    loadedcount++;
                   MainTextDownloadArticle.setVisibility(View.VISIBLE);
                    Recreatetool();
                   progressBar.setVisibility(View.GONE);
                }

                @Override
               public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(final Drawable placeHolderDrawable) {
                    progressBar.setVisibility(View.VISIBLE);
                }
            };
            Picasso.get().load(DownloadphotoUri).into(mTarget);

            if(bitmap1 != null)
            {
                drawable = new BitmapDrawable(getResources(), bitmap1);
                double OptimizationHeight = ((double) drawable.getIntrinsicWidth()/(double) drawable.getIntrinsicHeight());
                double DownloadPhotoHeight = ((getDeviceWidth(ChooseArticle.this.getApplicationContext())) / OptimizationHeight);
                drawable.setBounds(0,0,getDeviceWidth(ChooseArticle.this.getApplicationContext()), (int) DownloadPhotoHeight);

            }
            else
            {
                countimage = countimage + 1;
                drawable = getResources().getDrawable(id);
                drawable.setBounds(0,0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            }
            return drawable;
        }

        public void Recreatetool(){
            Log.d("Количество картинок", String.valueOf(countimage));
            Log.d("Количество загруженных", String.valueOf(loadedcount));

            if(loadedcount == countimage) {
                recreate();
                progressBar.setVisibility(View.GONE);
            }
            if(loadedcount+1 == countimage) {

                recreate();
                progressBar.setVisibility(View.GONE);
            }
            if(loadedcount+2 == countimage) {

                recreate();
                progressBar.setVisibility(View.GONE);
            }
            if(loadedcount+3 == countimage) {

                recreate();
                progressBar.setVisibility(View.GONE);
            }

        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_article);


        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();



        // Установка стиля безрамочного
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        getSupportActionBar().hide(); //Скрытие actionBar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        //Основные элементы статьи
        DownloadHeadText=findViewById(R.id.DownloadHeadText);
        TextDateDownloadArticle=findViewById(R.id.TextDateDownloadArticle);
        MainTextDownloadArticle=findViewById(R.id.MainTextDownloadArticle);
        progressBar=findViewById(R.id.progressBarArticle);




        //Установка OverScroll
        DownloadArticleScrollView = findViewById(R.id.DownloadArticleScrollView);



        //Закрытие окна
        CloseArticleButton = findViewById(R.id.CloseArticleButton);
        CloseArticleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });





    }





    Thread myThread = new Thread( // создаём новый поток
            new Runnable() { // описываем объект Runnable в конструкторе
                public void run() {
                    //Получение значений через ключ
                    Bundle arguments = getIntent().getExtras();
                    DownloadHeadText.setText(Html.fromHtml((String) arguments.get("HeaderText"),new GlideImageGetter(DownloadHeadText),null));
                    TextDateDownloadArticle.setText((CharSequence) arguments.get("Date"));
                    MainTextDownloadArticle.setText(Html.fromHtml((String) arguments.get("MainText"),new GlideImageGetter(MainTextDownloadArticle),null));
                }
            }
    );



    @Override
    public void onStart() {
        super.onStart();

        //Получение значений через ключ
        Bundle arguments = getIntent().getExtras();
        DownloadHeadText.setText(Html.fromHtml((String) arguments.get("HeaderText"),new GlideImageGetter(DownloadHeadText),null));
        TextDateDownloadArticle.setText((CharSequence) arguments.get("Date"));
        MainTextDownloadArticle.setText(Html.fromHtml((String) arguments.get("MainText"),new GlideImageGetter(MainTextDownloadArticle),null));



    }
}