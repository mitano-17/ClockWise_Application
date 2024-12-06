package com.mobdeve.s21.ermitano.kate_justine.mco2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AttendeeFormAdapter extends RecyclerView.Adapter<AttendeeFormAdapter.AttendeeFormViewHolder> {

    private final List<String> items;

    public AttendeeFormAdapter(List<String> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public AttendeeFormViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.questions, parent, false);
        return new AttendeeFormViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendeeFormViewHolder holder, int position) {
        String field = items.get(position);
        holder.labelTv.setText(field);
    }

    @Override
    public int getItemCount() {

        return items.size();
    }

    public static class AttendeeFormViewHolder extends RecyclerView.ViewHolder {
        TextView labelTv;
        EditText inputEt;

        public AttendeeFormViewHolder(@NonNull View itemView) {
            super(itemView);
            labelTv = itemView.findViewById(R.id.labelTv);
            inputEt = itemView.findViewById(R.id.inputEt);
        }
    }
}
