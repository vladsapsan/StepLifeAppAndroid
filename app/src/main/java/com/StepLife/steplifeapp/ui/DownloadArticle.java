package com.StepLife.steplifeapp.ui;

import static androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.StepLife.steplifeapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.nio.charset.Charset;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public class DownloadArticle extends Fragment {

    private DownloadArticleViewModel mViewModel;
    private ImageView backbutton;
    private TextView HeaderText,MainText,DateText;
    private String IdArticle = "";
    Charset charset = Charset.forName("UTF-16");
    private ScrollView DownloadArticleScrollView;
    private ProgressBar progressBar;
    private String Maintexts,Headtext,DataText;
    private Article downloadArticle;
    private String Article_Key ="AllArticle";
    private DatabaseReference mDataBase;
    Uri DownloadphotoUri;
    Drawable d;

    private Target mTarget;

    public static DownloadArticle newInstance() {
        return new DownloadArticle();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_download_article, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DownloadArticleViewModel.class);

    }


    public static CharSequence trim(CharSequence s, int start, int end) {
        while (start < end && Character.isWhitespace(s.charAt(start))) {
            start++;
        }

        while (end > start && Character.isWhitespace(s.charAt(end - 1))) {
            end--;
        }
        return s.subSequence(start, end);
    }


    static class BitmapDrawablePlaceHolder extends BitmapDrawable {

        protected Drawable drawable;

        @Override
        public void draw(final Canvas canvas) {
            if (drawable != null) {
                drawable.draw(canvas);
            }
        }

        public void setDrawable(Drawable drawable) {
            this.drawable = drawable;
        }

    }


    // Загрузка фото из интернета
    private class ImageGetter implements Html.ImageGetter {
        @Override
        public  Drawable getDrawable(String source) {
            int id;
            progressBar.setVisibility(View.VISIBLE);
            final BitmapDrawablePlaceHolder result = new BitmapDrawablePlaceHolder();
            DownloadphotoUri = Uri.parse(source);
            final Bitmap[] bitmap1 = {null};
            id = R.drawable.buttonimage;
            ImageView image = new ImageView(getContext());
            image.getDrawable();

            mTarget = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    //d = new BitmapDrawable(getResources(), bitmap);
                   // double OptimizationHeight = ((double) d.getIntrinsicWidth()/(double)d.getIntrinsicHeight());
                  //  double DownloadPhotoHeight = ((getDeviceWidth(getContext())) / OptimizationHeight);
                   // d.setBounds(0,0,getDeviceWidth(getContext()), (int) DownloadPhotoHeight);
                   // progressBar.setVisibility(View.GONE);

                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                }

                @Override
                public void onPrepareLoad(final Drawable placeHolderDrawable) {

                }
            };

            new AsyncTask<Void, Void, Bitmap>() {

                @Override
                protected Bitmap doInBackground(final Void... meh) {
                    try {
                        return Picasso.get().load(DownloadphotoUri).get();
                    } catch (Exception e) {
                        return null;
                    }
                }

                @Override
                protected void onPostExecute(final Bitmap bitmap) {
                    try {
                        final BitmapDrawable drawable = new BitmapDrawable(getResources(), bitmap);

                        double OptimizationHeight = ((double) drawable.getIntrinsicWidth()/(double)drawable.getIntrinsicHeight());
                        double DownloadPhotoHeight = ((getDeviceWidth(getContext())) / OptimizationHeight);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        result.setDrawable(drawable);
                        result.setBounds(0, 0, getDeviceWidth(getContext()), (int) DownloadPhotoHeight);


                    } catch (Exception e) {
                        /* nom nom nom*/
                    }
                }

            }.execute((Void) null);


           // if(bitmap1[0] != null)
            //{

            //}
            //else
            //{
              //  d = getResources().getDrawable(id);
                //d.setBounds(0,0,d.getIntrinsicWidth(),d.getIntrinsicHeight());
            //}
            return result;








           // Picasso.get().load(DownloadphotoUri).into(image, new Callback() {
               // @Override
               // public void onSuccess() {
                   // d = image.getDrawable();
                   // double OptimizationHeight = ((double) d.getIntrinsicWidth()/(double)d.getIntrinsicHeight());
                   // double DownloadPhotoHeight = ((getDeviceWidth(getContext())) / OptimizationHeight);
                    //d.setBounds(0,0,getDeviceWidth(getContext()), (int) DownloadPhotoHeight);
                  //  progressBar.setVisibility(View.GONE);
                //}
                //@Override
               // public void onError(Exception e) {

             //   }
           // });

        }
    };


    public static int getDeviceWidth(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        Display display = wm.getDefaultDisplay();
        display.getRealMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        return width;
    }

    //Получение данных  статьи
    private void DownloadArticleFirebaseData()
    {

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference("AllArticleBase");

        progressBar = view.findViewById(R.id.progressBar2);
        HeaderText = view.findViewById(R.id.DownloadHeadText);
        MainText = view.findViewById(R.id.MainTextDownloadArticle);
        DateText = view.findViewById(R.id.TextDateDownloadArticle);


        DownloadArticleScrollView = view.findViewById(R.id.DownloadArticleScrollView);
        OverScrollDecoratorHelper.setUpOverScroll(DownloadArticleScrollView);




        Bundle bundle = this.getArguments();
        if (bundle != null) {
            IdArticle = (bundle.getString("IDArticle", null));
            Maintexts = (bundle.getString("MainTextUri", null));
            DataText = (bundle.getString("DateText", null));
            Headtext = (bundle.getString("HeadText", null));

            progressBar.setVisibility(View.VISIBLE);
            MainText.setText(Html.fromHtml(Maintexts,new ImageGetter(),null));
                    DateText.setText(DataText);
                    HeaderText.setText(Html.fromHtml(Headtext));
                    progressBar.setVisibility(View.INVISIBLE);

        }
        else
        {
            getActivity().getSupportFragmentManager().popBackStackImmediate("DownloadArticle",POP_BACK_STACK_INCLUSIVE);
            BottomNavigationView bottomNavigationView= getActivity().findViewById(R.id.nav_view);
            bottomNavigationView.setVisibility(View.VISIBLE);
        }


        backbutton = view.findViewById(R.id.BacktoAllArticle);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStackImmediate("DownloadArticle",POP_BACK_STACK_INCLUSIVE);
                BottomNavigationView bottomNavigationView= getActivity().findViewById(R.id.nav_view);
                bottomNavigationView.setVisibility(View.VISIBLE);
            }
        });
    }
}