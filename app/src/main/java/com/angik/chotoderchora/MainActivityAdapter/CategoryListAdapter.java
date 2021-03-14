package com.angik.chotoderchora.MainActivityAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.angik.chotoderchora.R;
import com.angik.chotoderchora.Utility.DrawableUtility;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.CategoryViewHolder> {

    private OnItemClickListener mListener;

    private final String[] categoryTitles;
    private final Context context;

    public CategoryListAdapter(String[] categoryTitles, Context context) {
        this.categoryTitles = categoryTitles;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @NonNull
    @Override
    public CategoryListAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.category_layout, parent, false);
        return new CategoryViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryListAdapter.CategoryViewHolder holder, int position) {
        holder.categoryImageView.setImageDrawable(DrawableUtility.getCategoryDrawable(context, position));
        holder.categoryTitleTextView.setText(categoryTitles[position]);
    }

    @Override
    public int getItemCount() {
        return categoryTitles.length;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        private final TextView categoryTitleTextView;

        private final ImageView categoryImageView;

        public CategoryViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            categoryTitleTextView = itemView.findViewById(R.id.categoryTitle);

            categoryImageView = itemView.findViewById(R.id.categoryImageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = getAdapterPosition();

                    if (listener != null) {
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position, view);
                        }
                    }
                }
            });
        }
    }
}
