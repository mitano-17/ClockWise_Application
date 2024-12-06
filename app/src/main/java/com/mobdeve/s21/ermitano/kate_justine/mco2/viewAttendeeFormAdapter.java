package com.mobdeve.s21.ermitano.kate_justine.mco2;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class viewAttendeeFormAdapter extends RecyclerView.Adapter<viewAttendeeFormAdapter.viewAttendeeFormViewHolder> {

    private final List<String> items;

    public viewAttendeeFormAdapter(List<String> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public viewAttendeeFormViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.questions, parent, false);
        return new viewAttendeeFormViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewAttendeeFormViewHolder holder, int position) {
        String field = items.get(position);
        holder.labelTv.setText(field);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class viewAttendeeFormViewHolder extends RecyclerView.ViewHolder {
        TextView labelTv;
        EditText inputEt;

        public viewAttendeeFormViewHolder(@NonNull View itemView) {
            super(itemView);
            labelTv = itemView.findViewById(R.id.labelTv);
            inputEt = itemView.findViewById(R.id.inputEt);
        }
    }
}
