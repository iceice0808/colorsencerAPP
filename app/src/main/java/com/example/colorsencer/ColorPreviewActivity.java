package com.example.colorsencer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ColorPreviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_color_preview_dialog);

        Intent intent = getIntent();
        ArrayList<Parcelable> parcelableArrayList = intent.getParcelableArrayListExtra("selectedColors");
        List<ColorData> selectedColors = new ArrayList<>();

        for (Parcelable parcelable : parcelableArrayList) {
            if (parcelable instanceof ColorData) {
                selectedColors.add((ColorData) parcelable);
            }
        }

        View mixedColorPreview = findViewById(R.id.mixedColorPreview);
        Button saveButton = findViewById(R.id.saveButton);  
        Button cancelButton = findViewById(R.id.cancelButton);

        // 混合所選的顏色
        int totalRed = 0, totalGreen = 0, totalBlue = 0;
        for (ColorData color : selectedColors) {
            totalRed += Color.red(color.getColorValue());
            totalGreen += Color.green(color.getColorValue());
            totalBlue += Color.blue(color.getColorValue());
        }

        int mixedRed = totalRed / selectedColors.size();
        int mixedGreen = totalGreen / selectedColors.size();
        int mixedBlue = totalBlue / selectedColors.size();
        int mixedColor = Color.rgb(mixedRed, mixedGreen, mixedBlue);

        // 顯示混合後的顏色
        mixedColorPreview.setBackgroundColor(mixedColor);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 執行儲存操作，將預覽的顏色資料儲存到資料庫中
                ColorDBHelper colorDBHelper = new ColorDBHelper(getApplicationContext());
                colorDBHelper.addColor(mixedColor, "RGB: " + mixedRed + ", " + mixedGreen + ", " + mixedBlue);

                Intent resultIntent = new Intent();
                resultIntent.putExtra("mixedColor", mixedColor);
                resultIntent.putExtra("mixedRed", mixedRed);
                resultIntent.putExtra("mixedGreen", mixedGreen);
                resultIntent.putExtra("mixedBlue", mixedBlue);

                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 返回上一個畫面
                finish();
            }
        });

    }
}