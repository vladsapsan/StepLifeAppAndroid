package com.example.steplifeapp.ui.home;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.steplifeapp.AllArticle;
import com.example.steplifeapp.Bt_module;
import com.example.steplifeapp.ChooseArticle;
import com.example.steplifeapp.DownloadProcessActiviti;
import com.example.steplifeapp.MainActivity;
import com.example.steplifeapp.R;
import com.example.steplifeapp.TelephoneSignUp;
import com.example.steplifeapp.UserAgreement;
import com.example.steplifeapp.User_ProfileActiviti;
import com.example.steplifeapp.databinding.FragmentHomeBinding;
import com.example.steplifeapp.ui.Article;
import com.example.steplifeapp.ui.userProfileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;


//+79151408060

public class HomeFragment extends Fragment {
    FrameLayout FrameVideo;
    TextView TextBtnHide,AllAcricleButton;
    CardView ImageProfile,HowToGetProtCard;
    final private static String DBase_Code = "AllArticle";
    final private static String DB_Article_HowToGet = "-NJgrzWOZOFxEejjLr5J";
    private DatabaseReference mDatabase;

    Button buttonConnect;
    CardView  ArticleTeach;
    ScrollView HomescrollView;

    private ImageView ArticleState1,ArticleState2,ArticleState3;
    private FirebaseAuth mAuth;
    HorizontalScrollView horizontalScrollViewArticle,horizontalScrollView2;
    private Animation HideAnimation;
    private HomeViewModel homeViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    void DownloadArticlePictures()
    {
        Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/steplife1.appspot.com/o/AllArticleBase%2F15870364150766PreviewImage?alt=media&token=7386348c-18be-4373-8d9f-66a284f53bb0").into(ArticleState1);
        Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/steplife1.appspot.com/o/AllArticleBase%2F15955776419587PreviewImage?alt=media&token=f6aef4eb-312a-46d8-a45e-7e55505acd86").into(ArticleState2);
        Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/steplife1.appspot.com/o/AllArticleBase%2F212919968329690PreviewImage?alt=media&token=05c460be-54e6-450a-ab55-e847ad64334f").into(ArticleState3);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //Аунтефикация
        mAuth = FirebaseAuth.getInstance();


        HomescrollView = view.findViewById(R.id.HomeScrollView);
        OverScrollDecoratorHelper.setUpOverScroll(HomescrollView);

        horizontalScrollViewArticle = view.findViewById(R.id.horizontalScrollViewArticle);
        OverScrollDecoratorHelper.setUpOverScroll(horizontalScrollViewArticle);

        horizontalScrollView2 = view.findViewById(R.id.horizontalScrollView2);
        OverScrollDecoratorHelper.setUpOverScroll(horizontalScrollView2);

        //Переход ко всем статьям
        AllAcricleButton = (TextView) view.findViewById(R.id.AllAcricleButton);
        AllAcricleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new AllArticle();
                FragmentTransaction ft = ((FragmentActivity)getContext()).getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.slide_left, R.anim.slide_right,R.anim.slide_left, R.anim.slide_right);
                ft.addToBackStack("AllArticle");
                ft.add(R.id.HomeFragment,fragment,"AllArticle").commit();
            }
        });


        //Кнопка перехода к подключению модуля
        buttonConnect = view.findViewById(R.id.buttonConnect);
        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Bt_module.class);
                startActivity(intent);
            }
        });

        //кнопка закрытия видео на главном экране
        FrameVideo = (FrameLayout) view.findViewById(R.id.FrameVideoInstruction);
        TextBtnHide = (TextView) view.findViewById(R.id.textHideVideoButton);
        TextBtnHide.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                FrameVideo.setVisibility(View.GONE);
            }

        });

        //Картинки статей на главном экране
        ArticleState1 =  view.findViewById(R.id.ArticleState1);
        ArticleState2 =  view.findViewById(R.id.ArticleState2);
        ArticleState3 =  view.findViewById(R.id.ArticleState3);

        DownloadArticlePictures();


        //Открытие статьи
        HowToGetProtCard = view.findViewById(R.id.HowToGetProtCard);
        HowToGetProtCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDatabase = FirebaseDatabase.getInstance().getReference();
                //Вызов данных из базы по ключу
                mDatabase.child(DBase_Code).child(DB_Article_HowToGet).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {

                        }
                        else {
                            //Загрузка окна со статьей
                            Article NewArticle =  task.getResult().getValue(Article.class);
                            // создание объекта Intent для запуска ChooseArticle
                            Intent intent = new Intent(getActivity(), ChooseArticle.class);
                            // передача объекта с ключом "MainText" и значением
                            intent.putExtra("MainText",NewArticle.MainText);
                            intent.putExtra("Date",NewArticle.Date);
                            intent.putExtra("HeaderText", Html.fromHtml(NewArticle.HeadText).toString().trim());
                            // запуск ChooseArticle
                            startActivity(intent);
                        }
                    }
                });
            }
        });

        //Кнопка перехода в профиль
        ImageProfile =  view.findViewById(R.id.ProfileButton);
        ImageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser cUser = mAuth.getCurrentUser();
                Intent intent;
                if(cUser!=null)
                {
                    intent = new Intent(getActivity(), User_ProfileActiviti.class);
                }
                else
                {
                    intent = new Intent(getActivity(), TelephoneSignUp.class);
                }

                startActivity(intent);



            }
        });



    }
    //Скрытие Видео по нажатию на текст

    @Override
    public void onStart() {
        super.onStart();
       // FirebaseUser cUser = mAuth.getCurrentUser();
       // if(cUser!=null)
       // {

       //    String phoneNumber = cUser.getPhoneNumber();
       //     Uri UriPhoto = cUser.getPhotoUrl();

      //  }
    }

}