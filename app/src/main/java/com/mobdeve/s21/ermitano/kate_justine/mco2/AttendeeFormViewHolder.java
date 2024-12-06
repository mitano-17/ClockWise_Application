package com.mobdeve.s21.ermitano.kate_justine.mco2;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AttendeeFormViewHolder extends RecyclerView.ViewHolder {
    TextView labelTv;
    EditText inputEt;

    public AttendeeFormViewHolder(@NonNull View itemView){
        super(itemView);
        labelTv = itemView.findViewById(R.id.labelTv);
        inputEt = itemView.findViewById(R.id.inputEt);
    }
}
