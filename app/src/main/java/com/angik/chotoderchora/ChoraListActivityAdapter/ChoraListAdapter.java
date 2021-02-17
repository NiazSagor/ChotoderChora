package com.angik.chotoderchora.ChoraListActivityAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.angik.chotoderchora.R;

public class ChoraListAdapter extends RecyclerView.Adapter<ChoraListAdapter.ChoraViewHolder> {

    private OnItemClickListener mListener;
    private String[] choraTitles;

    public ChoraListAdapter(String[] choraTitles) {
        this.choraTitles = choraTitles;
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
        holder.choraTitleTextView.setText(choraTitles[position]);
    }

    @Override
    public int getItemCount() {
        return choraTitles.length;
    }

    public class ChoraViewHolder extends RecyclerView.ViewHolder {

        private TextView choraTitleTextView;
        private CardView cardView;

        public ChoraViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            cardView = itemView.findViewById(R.id.imageCardView);
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

