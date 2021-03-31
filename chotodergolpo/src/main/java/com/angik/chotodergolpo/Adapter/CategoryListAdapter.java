package com.angik.chotodergolpo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.angik.chotodergolpo.DrawableUtility.DrawableUtility;
import com.angik.chotodergolpo.R;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.CategoryViewHolder> {

    private OnItemClickListener mListener;

    private final String[] categoryTitles = {"Category 1", "Category 2", "Category 3", "Category 4"};

    private final Context context;

    public CategoryListAdapter(Context context) {
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
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.category_layout, parent, false);
        return new CategoryViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
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
