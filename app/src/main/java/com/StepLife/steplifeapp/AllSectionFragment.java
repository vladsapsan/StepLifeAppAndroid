package com.StepLife.steplifeapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.StepLife.steplifeapp.other.Section;
import com.StepLife.steplifeapp.other.SectionViewAdapter;
import com.google.android.material.transition.MaterialSharedAxis;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class AllSectionFragment extends Fragment {



    SectionViewAdapter sectionViewAdapter;
    ValueEventListener valueEventListener;
    private ArrayList<Section> listTemp = new ArrayList<Section>();

    private DatabaseReference mDataBase;
    private static final String Article_Key ="AllArticle";
    private static final String Section_Article_Key ="AllArticleSection";
    RecyclerView RecycleviewSection;

    //Загрузка уроков из базы
    private void DownloadArticleFirebaseData()
    {
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(listTemp.size()>0) listTemp.clear();
                for (DataSnapshot ds : snapshot.getChildren())
                {
                    Section section = ds.getValue(Section.class);
                    assert section != null;
                    listTemp.add(section);
                }
                sectionViewAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        mDataBase.addValueEventListener(valueEventListener);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setEnterTransition(new MaterialSharedAxis(MaterialSharedAxis.Z, true));
        setReturnTransition(new MaterialSharedAxis(MaterialSharedAxis.Z, true));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_section, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SectionViewAdapter.ItemClickListener clickListener1 = new SectionViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
               MainActivity.LoadSectionFragment(listTemp.get(position).SectionID,getActivity().getSupportFragmentManager(),R.id.AllSectionFragmentContainer);
            }
        };

        mDataBase = FirebaseDatabase.getInstance().getReference(Section_Article_Key);
        //Лист всех статей
        RecycleviewSection = view.findViewById(R.id.RecycleviewSection);
        RecycleviewSection.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false));
        sectionViewAdapter = new SectionViewAdapter(getContext(),listTemp);
        sectionViewAdapter.setClickListener(clickListener1);
        RecycleviewSection.setAdapter(sectionViewAdapter);
        DownloadArticleFirebaseData();
    }
}