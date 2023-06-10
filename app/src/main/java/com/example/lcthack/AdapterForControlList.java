package com.example.lcthack;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterForControlList extends RecyclerView.Adapter<AdapterForControlList.ViewHolder> {

    private ArrayList<ItemControlBanner> mData;

    public AdapterForControlList(ArrayList<ItemControlBanner> data) {
        mData = data;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView dateView, knoView, typeView, timeView, themeView;

        public ViewHolder(View itemView) {
            super(itemView);

            dateView = (TextView) itemView.findViewById(R.id.textView31);
            knoView = (TextView) itemView.findViewById(R.id.textView32);
            typeView = (TextView) itemView.findViewById(R.id.textView33);
            timeView = (TextView) itemView.findViewById(R.id.textView34);
            themeView = (TextView) itemView.findViewById(R.id.textView35);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ItemControlBanner item = mData.get(position);

        holder.dateView.setText(item.date);
        holder.knoView.setText(item.kno);
        holder.typeView.setText(item.themeofconsulting);
        holder.timeView.setText(item.time);
        holder.themeView.setText(item.typeofcontrol);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}