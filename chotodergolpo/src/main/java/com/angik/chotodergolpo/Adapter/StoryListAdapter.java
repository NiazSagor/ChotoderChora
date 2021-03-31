package com.angik.chotodergolpo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.angik.chotodergolpo.Model.Story;
import com.angik.chotodergolpo.R;

import java.util.List;

public class StoryListAdapter extends RecyclerView.Adapter<StoryListAdapter.ChoraViewHolder> {

    private OnItemClickListener mListener;

    private final List<Story> storyList;

    private final Context context;

    public StoryListAdapter(Context context, List<Story> storyList) {
        this.context = context;
        this.storyList = storyList;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @NonNull
    @Override
    public StoryListAdapter.ChoraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.story_layout, parent, false);
        return new ChoraViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryListAdapter.ChoraViewHolder holder, int position) {
        holder.itemView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fall_down));
        holder.choraTitleTextView.setText(storyList.get(position).getStoryTitle());
    }

    @Override
    public int getItemCount() {
        return storyList.size();
    }

    public class ChoraViewHolder extends RecyclerView.ViewHolder {

        private TextView choraTitleTextView;

        public ChoraViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            choraTitleTextView = itemView.findViewById(R.id.storyTitleTextView);

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

