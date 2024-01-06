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
            Log.d("ColorListAdapter", "RGB Value from ColorData: " + rgbValue); // 添加这行日志来检查从 ColorData 中获取的 RGB 值是否正确
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

        // 从数据库中删除颜色数据
        if (colorDBHelper != null && colorToDelete != null) {
            int idToDelete = colorToDelete.getId(); // 获取用于删除的标识符
            colorDBHelper.deleteColor(idToDelete); // 传入标识符进行删除
        }

        // 从列表中移除颜色项
        remove(colorToDelete);
        notifyDataSetChanged();
    }

    public void setColorDBHelper(ColorDBHelper dbHelper) {

        this.colorDBHelper = dbHelper;
    }

}