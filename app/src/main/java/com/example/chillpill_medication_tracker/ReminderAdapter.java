package com.example.chillpill_medication_tracker;

import android.content.Context;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder> {

    private ArrayMap collection;
    private Context c;

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void OnDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class ReminderViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView firstItem;
        public ImageView delete;

        public ReminderViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            title = itemView.findViewById(R.id.collectionTitle);
            firstItem = itemView.findViewById(R.id.firstItem);
            delete = itemView.findViewById(R.id.reminder_delete);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            listener.OnDeleteClick(position);
                        }
                    }
                }
            });
        }
    }


    public ReminderAdapter(Context c, ArrayMap rList) {
        this.c = c;
        collection = rList;
    }
    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.reminder_item, parent, false);
        ReminderViewHolder rvh = new ReminderViewHolder(v, mListener);
        return rvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, int position) {
        String key = collection.keyAt(position).toString();
        ReminderItem currentItem = (ReminderItem) collection.get(key);
        holder.title.setText(key);
        holder.firstItem.setText(currentItem.getTime());
    }

    @Override
    public int getItemCount() {
        return collection.size();
    }
}
