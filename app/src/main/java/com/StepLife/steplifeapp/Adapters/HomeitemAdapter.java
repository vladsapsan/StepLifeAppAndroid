package com.StepLife.steplifeapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.StepLife.steplifeapp.MainActivity.MainActivity;
import com.StepLife.steplifeapp.Model.Section;
import com.StepLife.steplifeapp.R;

import java.util.ArrayList;

public class HomeitemAdapter  extends RecyclerView.Adapter<HomeitemAdapter.ViewHolder>{
    private ArrayList<Section> mItems;
    private LayoutInflater mInflater;
    private Context context;
    private FragmentManager fragmentManager;

    HomeitemAdapter(Context context, ArrayList<Section> mItems, FragmentManager fragmentManager){
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mItems = mItems;
        this.fragmentManager = fragmentManager;
    }
    @Override
    @NonNull
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.home_item, parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.NameTextView.setText(mItems.get(position).SectionName);

        LinearLayoutManager layoutManager= new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false);
        holder.SectionRecycleView.setLayoutManager(layoutManager);
        SectionArticleViewAdapter sectionArticleViewAdapter = new SectionArticleViewAdapter(holder.myView.getContext(), mItems.get(position).articleList);
        SectionArticleViewAdapter.ItemClickListener itemClickListener = new SectionArticleViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position){if (mItems.get(position).articleList.get(position) != null) {
                MainActivity.LoadArticleFragmentFromID(mItems.get(position).articleList.get(position).id, fragmentManager, R.id.HomeFragment);
            }}};
        sectionArticleViewAdapter.setClickListener(itemClickListener);
        holder.SectionRecycleView.setAdapter(sectionArticleViewAdapter);
    }
    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        View myView;
        TextView NameTextView;
        RecyclerView SectionRecycleView;
        CardView CardNextButton;

        ViewHolder(View itemView) {
            super(itemView);
            NameTextView = itemView.findViewById(R.id.TextviewSectionName);
            CardNextButton = itemView.findViewById(R.id.CardNextButton);
            SectionRecycleView = itemView.findViewById(R.id.RecycleviewSectionArticle);
        }
    }

    public Section getItem(int id) {
        return mItems.get(id);
    }
}
