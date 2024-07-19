package com.example.colorsencer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ColorPickerActivity extends AppCompatActivity {
    private static final int COLOR_PREVIEW_REQUEST_CODE = 1001; // 定義請求碼
    private RecyclerView colorPickerRecyclerView;
    private ColorRecyclerViewAdapter adapter;
    private ColorDBHelper colorDBHelper;
    private List<ColorData> colorList;
    private int uniqueId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.color_picker_layout);

        colorPickerRecyclerView = findViewById(R.id.colorPickerRecyclerView);
        colorList = new ArrayList<>();
        adapter = new ColorRecyclerViewAdapter(this, colorList);
        colorPickerRecyclerView.setAdapter(adapter);
        colorDBHelper = new ColorDBHelper(this);
        loadColorsFromDatabase();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        colorPickerRecyclerView.setLayoutManager(layoutManager);
    }

    private void loadColorsFromDatabase() {
        colorList.clear();
        try {
            List<ColorData> colorsFromDB = colorDBHelper.getAllColors();
            if (colorsFromDB != null && !colorsFromDB.isEmpty()) {
                colorList.addAll(colorsFromDB);
                adapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Database", "Error reading from database: " + e.getMessage());
        }

    }
    public void showColorPreview(View view) {
        List<ColorData> selectedColors = adapter.getSelectedColors();
        Log.d("SelectedColors", "Number of selected colors: " + selectedColors.size());

        if (selectedColors.size() > 1) {
            ArrayList<ColorData> parcelableList = new ArrayList<>(selectedColors);

            Intent intent = new Intent(this, ColorPreviewActivity.class);
            intent.putParcelableArrayListExtra("selectedColors", parcelableList);
            startActivityForResult(intent, COLOR_PREVIEW_REQUEST_CODE);
        } else {
            Toast.makeText(this, "Please select at least two colors for preview.", Toast.LENGTH_SHORT).show();
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == COLOR_PREVIEW_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            int mixedColor = data.getIntExtra("mixedColor", 0);
            int mixedRed = data.getIntExtra("mixedRed", 0);
            int mixedGreen = data.getIntExtra("mixedGreen", 0);
            int mixedBlue = data.getIntExtra("mixedBlue", 0);

            // 使用計數器獲取唯一ID
            uniqueId++;

            // 創建 ColorData 並添加到列表中
            ColorData colorData = new ColorData(uniqueId, mixedColor, "RGB: " + mixedRed + ", " + mixedGreen + ", " + mixedBlue);
            colorList.add(colorData);
            adapter.notifyDataSetChanged(); // 通知列表數據發生變化

            Intent updateIntent = new Intent();
            updateIntent.putParcelableArrayListExtra("updatedColors", new ArrayList<>(colorList));
            setResult(RESULT_OK, updateIntent);
        }
    }
    public void cancel(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivityForResult(intent, COLOR_PREVIEW_REQUEST_CODE);
    }
}
