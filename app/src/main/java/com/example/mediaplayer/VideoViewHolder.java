package com.example.mediaplayer;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class VideoViewHolder extends RecyclerView.ViewHolder {
    private final TextView txtName,txtDuration;
    private final ConstraintLayout constraintLayout;

    public VideoViewHolder(@NonNull View itemView) {

        super(itemView);
        txtName=itemView.findViewById(R.id.txtName);
        txtDuration=itemView.findViewById(R.id.txtDuration);
        constraintLayout=itemView.findViewById(R.id.constraintLayout);
    }

    public TextView getTxtName() {
        return txtName;
    }

    public TextView getTxtDuration() {
        return txtDuration;
    }

    public ConstraintLayout getConstraintLayout() {
        return constraintLayout;
    }
}
