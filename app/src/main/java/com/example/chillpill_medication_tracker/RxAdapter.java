package com.example.chillpill_medication_tracker;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RxAdapter extends RecyclerView.Adapter<RxAdapter.MedicationViewHolder> {
    private ArrayList<Medication> mRxList;
    private Context c;
    private OnItemClickListener mListener;
    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class MedicationViewHolder extends RecyclerView.ViewHolder {
        // public ImageView iview;
        public TextView textView1;
        public TextView textView2;
        public ImageView delete;

        public MedicationViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            //iview = itemView.findViewById(R.id.imageView);
            textView1 = itemView.findViewById(R.id.rx_title);
            textView2 = itemView.findViewById(R.id.rx_subtitle);
            delete = itemView.findViewById(R.id.image_delete);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
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
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }

    public RxAdapter(Context c, ArrayList<Medication> rxList) {
        this.c = c;
        mRxList = rxList;
    }

    @NonNull
    @Override
    public MedicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(c).inflate(R.layout.rx_item, parent, false);
        MedicationViewHolder mvh = new MedicationViewHolder(v, mListener);
        return mvh;
    }

    @Override
    public void onBindViewHolder(MedicationViewHolder holder, int position) {
        Medication currentItem = mRxList.get(position);

        holder.textView1.setText(currentItem.getName());
        holder.textView2.setText(currentItem.getInstructions());

    }

    @Override
    public int getItemCount() {
        return mRxList.size();
    }

}
