package com.example.steplifeapp;

import static androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.steplifeapp.ui.Article;
import com.example.steplifeapp.ui.ArticleListAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public class AllArticle extends Fragment {

    private AllArticleViewModel mViewModel;
    private ImageView backbutton;
    private CardView articlecard;
    private ArticleListAdapter ArticleListAdapter;
    private ListView allArticlelist;
    private ArrayAdapter<String> adapter;


    private List<String> listData;
    private ArrayList <Article> listTemp = new ArrayList<Article>();
    ProgressBar progressBar;
    Article DowArticle;
    Uri DownloadphotoUri;
    BottomSheetDialog bottomSheetDialog;

    private Target mTarget;
    private String Article_Key ="AllArticle";
    private DatabaseReference mDataBase;

    public static AllArticle newInstance() {
        return new AllArticle();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_article, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AllArticleViewModel.class);
        // TODO: Use the ViewModel
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

    //Иницилизация компонентов
    private void initilization()
    {
        allArticlelist = getActivity().findViewById(R.id.AllArticleListview);
        listData = new ArrayList<>();
        mDataBase = FirebaseDatabase.getInstance().getReference(Article_Key);
        ArticleListAdapter = new ArticleListAdapter(getActivity(),R.layout.listviewarticleitem, listTemp);
        allArticlelist.setAdapter(ArticleListAdapter);
    }




    private class ImageGetter implements Html.ImageGetter {

        public Drawable getDrawable(String source) {
            int id;
            DownloadphotoUri = Uri.parse(source);
            final Bitmap[] bitmap1 = {null};
            final Drawable[] d = {null};
                id = R.drawable.buttonimage;


            mTarget = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    bitmap1[0] = bitmap;
                    d[0] = new BitmapDrawable(getResources(),bitmap);

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

           if(bitmap1[0] != null)
           {
               d[0] = new BitmapDrawable(getResources(), bitmap1[0]);
               double OptimizationHeight = ((double) d[0].getIntrinsicWidth()/(double) d[0].getIntrinsicHeight());
               double DownloadPhotoHeight = ((getDeviceWidth(getContext())) / OptimizationHeight);
               d[0].setBounds(0,0,getDeviceWidth(getContext()), (int) DownloadPhotoHeight);


           }
           else
           {
                d[0] = getResources().getDrawable(id);
               d[0].setBounds(0,0, d[0].getIntrinsicWidth(), d[0].getIntrinsicHeight());
           }
            return d[0];
        }
    };






    //Загрузка уроков из базы
    private void DownloadArticleFirebaseData()
    {

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int a = 0;
                progressBar.setMax((int) snapshot.getChildrenCount());
                if(listData.size()>0) listData.clear();
                if(listTemp.size()>0) listTemp.clear();
                for (DataSnapshot ds : snapshot.getChildren())
                {
                    Article article = ds.getValue(Article.class);
                    //Проверка
                    assert article != null;
                    //Spanned Head = Html.fromHtml(article.HeadText);
                   // String key = ds.getKey();
                    //listData.add(String.valueOf(Head));
                    listTemp.add(article);
                    a++;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        progressBar.setProgress(a, true);
                    }
                    if(progressBar.getProgress()==(int) snapshot.getChildrenCount())
                    {

                    }
                }

                ArticleListAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        };
        mDataBase.addValueEventListener(valueEventListener);


    }


    public static int getDeviceWidth(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        Display display = wm.getDefaultDisplay();
        display.getRealMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        return width;
    }

    public static int getDeviveHeight(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        Display display = wm.getDefaultDisplay();
        display.getRealMetrics(displayMetrics);
        int heightPixels = displayMetrics.heightPixels;
        return heightPixels;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        progressBar = view.findViewById(R.id.progressBarAllArticle);


        initilization();
        DownloadArticleFirebaseData();
        OverScrollDecoratorHelper.setUpOverScroll(allArticlelist);


        FirebaseStorage storage = FirebaseStorage.getInstance();




        EditText SearchText = view.findViewById(R.id.editTextSearch);
        SearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                  ArticleListAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //При выборе урока переход на новый экран
        allArticlelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            //    Bundle Bundle = new Bundle();
                DowArticle = listTemp.get(position);

                // создание объекта Intent для запуска ChooseArticle
                Intent intent = new Intent(getActivity(), ChooseArticle.class);
                    // передача объекта с ключом "MainText" и значением
                intent.putExtra("MainText",DowArticle.MainText);
                intent.putExtra("Date",DowArticle.Date);
                intent.putExtra("HeaderText",Html.fromHtml(DowArticle.HeadText).toString().trim());
                    // запуск ChooseArticle
                startActivity(intent);

             //   progressBarsheet.setVisibility(View.VISIBLE);
             //   MainText.setText(Html.fromHtml(DowArticle.MainText,new ImageGetter(),null));
             //   DateText.setText(DowArticle.Date);
             //   HeaderText.setText(Html.fromHtml(DowArticle.HeadText).toString().trim());

              //  progressBarsheet.setVisibility(View.INVISIBLE);
               // StorageReference httpsReference = storage.getReferenceFromUrl(DowArticle.MainText);
              //  final long ONE_MEGABYTE = 1024 * 1024;
             //   httpsReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
              //      @Override
               //     public void onSuccess(byte[] bytes) {
                        // Data for "images/island.jpg" is returns, use this as needed
                 //       CharSequence MainTextChar = null;
                 //       try {
                  //          MainTextChar = new String(bytes,"UTF-8");
//
                  //      } catch (UnsupportedEncodingException e) {
                  //          e.printStackTrace();
                  //      }




                //    }
             //   }).addOnFailureListener(new OnFailureListener() {
              //     @Override
               //     public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
              //      }
            //    });






              //  Bundle.putString("IDArticle", DowArticle.id);
              //  Bundle.putString("HeadText", DowArticle.HeadText);
              //  Bundle.putString("MainTextUri", DowArticle.MainText);
             //   Bundle.putString("DateText", DowArticle.Date);

             //   Fragment fragment = new DownloadArticle();
             //  fragment.setArguments(Bundle);

             //   FragmentTransaction ft = ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction();
             //   ft.setCustomAnimations(R.anim.slide_down, R.anim.slide_up, R.anim.slide_down, R.anim.slide_up);
             //   ft.addToBackStack("DownloadArticle");
             //   ft.add(R.id.AllArticle, fragment, "DownloadArticle").commit();

             //   BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.nav_view);
             //   bottomNavigationView.setVisibility(View.INVISIBLE);


            }
        });

        backbutton = view.findViewById(R.id.BacktoNotif);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStackImmediate("AllArticle",POP_BACK_STACK_INCLUSIVE);

            }
        });

    }


}