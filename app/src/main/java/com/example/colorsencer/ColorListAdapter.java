package com.example.colorsencer;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ColorListAdapter extends ArrayAdapter<ColorData> {
    private final List<ColorData> colorList;
    private ColorDBHelper colorDBHelper;

    public ColorListAdapter(Context context, List<ColorData> colors) {
        super(context, 0, colors);
        this.colorDBHelper = new ColorDBHelper(context);
        this.colorList = colors;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View colorView = convertView;
        if (colorView == null) {
            colorView = LayoutInflater.from(getContext()).inflate(R.layout.color_item, parent, false);
        }

        ColorData colorData = getItem(position);
        if (colorData != null) {
            int color = colorData.getColor();
            View colorSquare = colorView.findViewById(R.id.colorSquare);
            colorSquare.setBackgroundColor(color);

            TextView rgbTextView = colorView.findViewById(R.id.colorText);
            String rgbValue = colorData.getRgbValue();
            Log.d("ColorListAdapter", "RGB Value from ColorData: " + rgbValue); 
            rgbTextView.setText(rgbValue);

            // 找到删除按钮
            ImageButton deleteButton = colorView.findViewById(R.id.deleteButton);
            Log.d("ColorListAdapter", "Delete button found: " + (deleteButton != null));
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("ColorListAdapter", "Delete button clicked at position: " + position);
                    // 删除颜色项
                    removeColorItem(position);
                }
            });
        }

        return colorView;
    }

    private void removeColorItem(int position) {
        ColorData colorToDelete = getItem(position);

        //從數據庫中移除顏色數據
        if (colorDBHelper != null && colorToDelete != null) {
            int idToDelete = colorToDelete.getId(); 
            colorDBHelper.deleteColor(idToDelete); 
        }

        //移除列表顏色項
        remove(colorToDelete);
        notifyDataSetChanged();
    }

    public void setColorDBHelper(ColorDBHelper dbHelper) {

        this.colorDBHelper = dbHelper;
    }

}
