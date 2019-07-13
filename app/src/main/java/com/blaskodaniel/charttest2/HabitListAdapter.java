package com.blaskodaniel.charttest2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HabitListAdapter extends RecyclerView.Adapter<HabitListViewHolder> {

    public List<String> habitsData;

    public HabitListAdapter(List<String> data){
        this.habitsData = data;
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
    public void onBindViewHolder(@NonNull HabitListViewHolder holder, int position) {
        // Specifies the content of each single element of the recyclerview
        final int pos = position;
        holder.habitText.setText(habitsData.get(position));
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HabitListActivity.deleteHabit(habitsData.get(pos));
                habitsData.remove(pos);
                notifyItemRemoved(pos);
                notifyItemRangeChanged(pos, habitsData.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        // Number of items in the recyclerview
        return habitsData.size();
    }
}
