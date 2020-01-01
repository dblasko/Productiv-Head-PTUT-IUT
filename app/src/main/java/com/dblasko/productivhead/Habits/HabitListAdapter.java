package com.dblasko.productivhead.Habits;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.astritveliu.boom.Boom;
import com.dblasko.productivhead.R;

import java.util.List;

public class HabitListAdapter extends RecyclerView.Adapter<HabitListViewHolder> {

    public List<String> habitsData;
    public Context context;
    public String year;
    public String month;

    public HabitListAdapter(List<String> data, Context context, String year, String month){
        this.habitsData = data;
        this.context = context;
        this.year = year;
        this.month = month;
    }

    public void setHabitsData(List<String> habitsData) {
        this.habitsData = habitsData;
    }


    @NonNull
    @Override
    public HabitListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Called when HabitListViewHolder has to be initialized
        // We build our view and inflate it
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_card_view, parent, false);
        HabitListViewHolder viewHolder = new HabitListViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HabitListViewHolder holder, final int position) {
        // Specifies the content of each single element of the recyclerview
        final int pos = position;
        holder.habitText.setText(habitsData.get(position));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start detail activity with needed data as extra
                Intent intent = new Intent(context, HabitDetailActivity.class);
                intent.putExtra("habit", habitsData.get(position));
                intent.putExtra("month", month);
                intent.putExtra("year", year);
                context.startActivity(intent);
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Delete habit from DB and update view
                HabitListActivity.deleteHabit(habitsData.get(pos));
                habitsData.remove(pos);
                notifyItemRemoved(pos);
                notifyItemRangeChanged(pos, habitsData.size());
            }
        });

        // Add animations
        new Boom((View)holder.cardView);
        new Boom((View)holder.deleteButton);
    }

    @Override
    public int getItemCount() {
        // Number of items in the recyclerview
        return habitsData.size();
    }
}
