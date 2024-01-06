package com.example.colorsencer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ColorRecyclerViewAdapter extends RecyclerView.Adapter<ColorRecyclerViewAdapter.ColorViewHolder> {

    private List<ColorData> colorList;
    private Context context;
    private List<ColorData> selectedColors = new ArrayList<>();
    private AdapterView.OnItemClickListener onItemClickListener;
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = (AdapterView.OnItemClickListener) listener;
    }
    public ColorRecyclerViewAdapter(Context context, List<ColorData> colorList) {
        this.context = context;
        this.colorList = colorList;
    }

    @NonNull
    @Override
    public ColorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_color_item, parent, false);
        return new ColorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorViewHolder holder, int position) {
        ColorData colorData = colorList.get(position);
        holder.bindData(colorData);
    }

    @Override
    public int getItemCount() {
        return colorList.size();
    }

    public class ColorViewHolder extends RecyclerView.ViewHolder {

        private TextView colorValue;
        private View colorSquare;
        private CheckBox checkboxColor;

        public ColorViewHolder(@NonNull View itemView) {
            super(itemView);
            colorSquare = itemView.findViewById(R.id.colorSquare);
            colorValue = itemView.findViewById(R.id.colorValue);
            checkboxColor = itemView.findViewById(R.id.checkbox_color);

            checkboxColor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleSelection(getAdapterPosition());
                }
            });
        }

        public void bindData(ColorData colorData) {
            // Bind data to views here
            colorValue.setText(colorData.getRgbValue());
            colorSquare.setBackgroundColor(colorData.getColor());

            // Set checkbox state based on selection
            checkboxColor.setChecked(selectedColors.contains(colorData));
        }
    }
    public List<ColorData> getSelectedColors() {
        return selectedColors;
    }

    public void toggleSelection(int position) {
        ColorData color = colorList.get(position);
        if (selectedColors.contains(color)) {
            selectedColors.remove(color);
        } else {
            selectedColors.add(color);
        }
        notifyItemChanged(position);
    }
}