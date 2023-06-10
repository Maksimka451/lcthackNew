package com.example.lcthack;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {

    private List<Date> dates;
    private Set<Integer> coloredPositions;
    private String[] daysOfWeek ; // Массив с названиями дней недели
    private ItemClickListener itemClickListener;

    public CalendarAdapter(List<Date> dates, Set<Integer> coloredPositions, String[] daysOfWeek) {
        this.dates = dates;
        this.coloredPositions = coloredPositions;
        this.daysOfWeek = daysOfWeek;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void updateColoredPositions(Set<Integer> coloredPositions) {
        this.coloredPositions = coloredPositions;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(com.example.lcthack.R.layout.item_calendar_date, parent, false);
        return new CalendarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        daysOfWeek = new String[]{"ВС", "ПН", "ВТ", "СР", "ЧТ", "ПТ", "СБ"};
        if (position < 7) { // Проверяем, если позиция в пределах первого ряда (столбца с названиями дней недели)
            holder.dateTextView.setText(daysOfWeek[position]); // Устанавливаем название дня недели
            holder.dateTextView.setBackgroundColor(Color.WHITE);// Устанавливаем цвет фона
            holder.dateTextView.setTextColor(Color.BLACK);
        } else {
            Date date = dates.get(position - 7); // Вычитаем 7, чтобы учесть первую строку с днями недели
            boolean isColored = coloredPositions.contains(position - 7);

            // Установка данных и стилей для элемента календаря
            holder.dateTextView.setText(getFormattedDate(date));
            if (isColored) {
                holder.dateTextView.setBackground(ContextCompat.getDrawable(holder.dateTextView.getContext(), com.example.lcthack.R.drawable.calendare_back ));
            } else {
                holder.dateTextView.setBackgroundColor(Color.WHITE);
            }
        }

        holder.itemView.setOnClickListener(view -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dates.size() + 7; // Добавляем 7 позиций для первой строки с днями недели
    }

    private String getFormattedDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd", Locale.getDefault());
        return sdf.format(date);
    }

    public class CalendarViewHolder extends RecyclerView.ViewHolder {
        public TextView dateTextView;

        public CalendarViewHolder(View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(com.example.lcthack.R.id.dateTextView);
        }
    }
}
