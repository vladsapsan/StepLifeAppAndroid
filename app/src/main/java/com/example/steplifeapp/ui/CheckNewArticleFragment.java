package com.example.steplifeapp.ui;

import static androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE;

import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.steplifeapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class CheckNewArticleFragment extends Fragment {
    private CheckNewArticleViewModel mViewModel;
    private TextView HeaderView,MainView,DateText;
    private EditText Header,Main;
    private ImageView imagebackAddArticle,imageAddArticlePublic;
    private DatabaseReference mDataBase;
    private final String Article_Key = "AllArticle";

    public static CheckNewArticleFragment newInstance() {
        return new CheckNewArticleFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {



        return inflater.inflate(R.layout.fragment_check_new_article, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CheckNewArticleViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDataBase = FirebaseDatabase.getInstance().getReference(Article_Key);

        HeaderView = view.findViewById(R.id.TextViewHeaderFinal);
        MainView = view.findViewById(R.id.TextViewMainTextEditorFinal);
        imagebackAddArticle = view.findViewById(R.id.imagebackAddArticle);
        DateText = view.findViewById(R.id.TextViewDate);


        //Запись урока в базу данных,публикация
        imageAddArticlePublic = view.findViewById(R.id.imageAddArticlePublic);
        imageAddArticlePublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idArticle = mDataBase.getKey();


                //Текст с сохраненным форматированием
                Spanned HeadSpanned =   Html.fromHtml( HeaderView.getText().toString());
                Editable HeadViewText = (Editable) HeadSpanned;
                String HeadString = Html.toHtml(HeadViewText);


                Spanned MainSpanned = Html.fromHtml( MainView.getText().toString());
                Editable MainViewText = (Editable) MainSpanned;
                String MainString = Html.toHtml(MainViewText);


              // Article newArticle = new Article(idArticle,(String) DateText.getText(),HeadString,MainString);

                //mDataBase.push().setValue(newArticle);

                getActivity().getSupportFragmentManager().popBackStackImmediate("AddArticle",POP_BACK_STACK_INCLUSIVE);
                BottomNavigationView bottomNavigationView= getActivity().findViewById(R.id.nav_view);
                bottomNavigationView.setVisibility(View.VISIBLE);

                Toast.makeText(getActivity(), "Публикация...",Toast.LENGTH_SHORT).show();
            }
        });




         //Возвращение обратно
        imagebackAddArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStackImmediate("CheckNewArticle",POP_BACK_STACK_INCLUSIVE);
            }
        });




        DateFormat df = new SimpleDateFormat("d MMM yyyy");
        String Simpledate = df.format(Calendar.getInstance().getTime());



        DateText.setText(Simpledate.toString());

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String Head = bundle.getString("Head", null);
            HeaderView.setText(Head);
            SpannableString Main = (SpannableString) Html.fromHtml(bundle.getString("Main", null));
            MainView.setText(Main);
        }


    }
}