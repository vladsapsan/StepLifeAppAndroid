package com.StepLife.steplifeapp.EnterenceActivity;

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

import com.StepLife.steplifeapp.MainActivity.MainActivity;
import com.StepLife.steplifeapp.Model.LightArticle;
import com.StepLife.steplifeapp.R;
import com.StepLife.steplifeapp.ui.LightArticleListAdapter;
import com.StepLife.steplifeapp.ui.notifications.NotificationsFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class TagSearchArticleFragment extends Fragment {
    TextView TextviewTagFilter;
    String TagFilter;
    LinearLayout BacktoAllActivity;
    ListView AllArticleListviewTags;
    private LightArticleListAdapter ArticleListAdapter;
    private List<String> listData;
    ValueEventListener valueEventListener;
    private static final String Tags_Key ="AllTags";
    private ArrayList<LightArticle> listTemp = new ArrayList<>();
    private static final String Article_Key ="AllArticle";
    private DatabaseReference mDataBase,mDataTags;

    //Иницилизация компонентов
    private void initilization()
    {
        AllArticleListviewTags.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                MainActivity.LoadArticleFragmentFromID(listTemp.get(position).id,getActivity().getSupportFragmentManager(), R.id.AllArticle);
               // MainActivity.LoadArticleFragment(listTemp.get(position) ,getActivity().getSupportFragmentManager(),R.id.AllArticle);
            }
        });
        listData = new ArrayList<>();
        mDataBase = FirebaseDatabase.getInstance().getReference(Article_Key);
        ArticleListAdapter = new LightArticleListAdapter(getContext(),R.layout.listviewarticleitem, listTemp);
        AllArticleListviewTags.setAdapter(ArticleListAdapter);
    }

    //Загрузка уроков из базы
    private void DownloadArticleFirebaseData()
    {
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(listData.size()>0) listData.clear();
                if(listTemp.size()>0) listTemp.clear();
                for (DataSnapshot ds : snapshot.getChildren())
                {
                    LightArticle article = ds.getValue(LightArticle.class);
                    assert article != null;
                    if(article.TagList!=null) {
                        for (String ArticleTag : article.TagList) {
                            if (ArticleTag.equals(TagFilter)) {
                                listTemp.add(article);
                            }
                        }
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


    private void DownloadTag(){
        //Получение значений через ключ
        Bundle arguments = getArguments();
        TagFilter = (String) arguments.getString(NotificationsFragment.TagString_Key);
        if(TagFilter!=null) {
            TextviewTagFilter.setText(TagFilter);
            initilization();
            DownloadArticleFirebaseData();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            // Inflate the layout
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tag_search_article, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AllArticleListviewTags = view.findViewById(R.id.AllArticleListviewTags);
        TextviewTagFilter = view.findViewById(R.id.TextviewTagFilter);
        //Кнопка выхода
        BacktoAllActivity = view.findViewById(R.id.CloseArticleButton);
        BacktoAllActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        DownloadTag();
    }
}