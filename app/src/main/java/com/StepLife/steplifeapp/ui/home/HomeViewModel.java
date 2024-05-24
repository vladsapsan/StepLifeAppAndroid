package com.StepLife.steplifeapp.ui.home;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.constraintlayout.utils.widget.MockView;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.StepLife.steplifeapp.MainEnterenceActivity.MainActivity;
import com.StepLife.steplifeapp.R;
import com.StepLife.steplifeapp.Model.LightArticle;
import com.StepLife.steplifeapp.UserProfile.MainSettingsActivity;
import com.StepLife.steplifeapp.other.SectionArticleViewAdapter;
import com.StepLife.steplifeapp.ui.FirebaseData.Firebasedatamanager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeViewModel extends ViewModel {
    MutableLiveData<ArrayList<LightArticle>> ArticleSectionList;
    public HomeViewModel() {
    }
    public LiveData<ArrayList<LightArticle>> getData(String SectionNumber) {
        if (ArticleSectionList == null) {
            ArticleSectionList = new MutableLiveData<>();
            loadSectionData(SectionNumber);
        }
        Log.d("FireBaseDate", "a");
        return ArticleSectionList;
    }
    protected static void InitRecycleView (RecyclerView recyclerView, Context context, ArrayList<LightArticle> articlelistTemp, FragmentManager fragmentManager){
        LinearLayoutManager layoutManager= new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        SectionArticleViewAdapter sectionArticleViewAdapter = new SectionArticleViewAdapter(context, articlelistTemp);
        SectionArticleViewAdapter.ItemClickListener itemClickListener = new SectionArticleViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(articlelistTemp.get(position)!=null) {
                    MainActivity.LoadArticleFragmentFromID(articlelistTemp.get(position).id ,fragmentManager,R.id.HomeFragment);
                }
            }
        };
        sectionArticleViewAdapter.setClickListener(itemClickListener);
        recyclerView.setAdapter(sectionArticleViewAdapter);
    }

    public final Uri GetHomeVideoPath(String PackageName){
        return Uri.parse( "android.resource://" + PackageName + "/" + R.raw.steplifevideo);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    private void loadSectionData(String SectionNumber) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<LightArticle> lightArticles = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    lightArticles.add(ds.getValue(LightArticle.class));
                }
                ArticleSectionList.postValue(lightArticles);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        FirebaseDatabase.getInstance().getReference().child("Lib").child("HomeArticle").child("Section"+SectionNumber).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    Log.d("FireBaseDate", "/AllArticleSection/"+task.getResult().getValue().toString()+"/articleList");
                    Firebasedatamanager.getDataFromFirebase("/AllArticleSection/"+task.getResult().getValue().toString()+"/articleList",valueEventListener);
                }
            }
        });
    }
}