package com.example.produtivhead.Habits;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.produtivhead.R;

public class HabitListViewHolder extends RecyclerView.ViewHolder {
    // Référence les components view de chaque item de la liste
    // Adapter makes us not fetch the view ID's everytime

    CardView cardView;
    TextView habitText;
    ImageView deleteButton;

    public HabitListViewHolder(View view){
        super(view);
        cardView = (CardView)view.findViewById(R.id.listCardView);
        habitText = (TextView)view.findViewById(R.id.listCardViewHabitText);
        deleteButton = (ImageView)view.findViewById(R.id.listCardViewDelete);
    }
}
