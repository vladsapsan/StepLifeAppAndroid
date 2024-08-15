package com.StepLife.steplifeapp.Adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.StepLife.steplifeapp.R;
import com.StepLife.steplifeapp.Model.Section;

import java.util.ArrayList;

public class SectionViewAdapter extends RecyclerView.Adapter<SectionViewAdapter.ViewHolderSection>{

    private ArrayList<Section> mListArticle;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public SectionViewAdapter(Context context, ArrayList<Section> mListArticle){
        this.mInflater = LayoutInflater.from(context);
        this.mListArticle = mListArticle;
    }
    @NonNull
    @Override
    public ViewHolderSection onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_view_item_selection, parent, false);
        return new ViewHolderSection(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolderSection holder, int position) {
        holder.NameSection.setText(Html.fromHtml(mListArticle.get(position).SectionName).toString());
        holder.AboutSection.setText(Html.fromHtml(mListArticle.get(position).AboutSection).toString());
    }
    @Override
    public int getItemCount() {
        return mListArticle.size();
    }
    public class ViewHolderSection extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView NameSection,AboutSection;
        ViewHolderSection(View itemView) {
            super(itemView);
            NameSection = itemView.findViewById(R.id.TextSection);
            AboutSection = itemView.findViewById(R.id.TextAboutSection);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());

        }
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
