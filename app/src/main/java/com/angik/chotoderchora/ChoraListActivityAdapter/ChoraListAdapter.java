package com.angik.chotoderchora.ChoraListActivityAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.angik.chotoderchora.Model.Poem;
import com.angik.chotoderchora.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class ChoraListAdapter extends RecyclerView.Adapter<ChoraListAdapter.ChoraViewHolder> {

    private OnItemClickListener mListener;
    private final List<Poem> poemList;
    private Context context;

    public ChoraListAdapter(Context context, List<Poem> poemList) {
        this.context = context;
        this.poemList = poemList;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ChoraListAdapter.ChoraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.chora_layout, parent, false);
        return new ChoraViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ChoraListAdapter.ChoraViewHolder holder, int position) {

        Glide
                .with(context)
                .load(poemList.get(position).getPoemImage())
                .centerCrop()
                .into(holder.poemImageView);


        holder.choraTitleTextView.setText(poemList.get(position).getPoemTitle());
    }

    @Override
    public int getItemCount() {
        return poemList.size();
    }

    public class ChoraViewHolder extends RecyclerView.ViewHolder {

        private TextView choraTitleTextView;
        private ImageView poemImageView;
        private CardView cardView;

        public ChoraViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            cardView = itemView.findViewById(R.id.imageCardView);
            poemImageView = itemView.findViewById(R.id.choraImageView);
            choraTitleTextView = itemView.findViewById(R.id.choraTitle);

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

