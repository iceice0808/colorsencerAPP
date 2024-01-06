package com.example.colorsencer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private ListView colorListView;
    private ColorListAdapter adapter;
    private List<ColorData> colorList;
    private ColorDBHelper colorDBHelper;
    private int uniqueId = 0;
    private static final int COLOR_PREVIEW_REQUEST_CODE = 1001; // 定義請求碼

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        colorListView = findViewById(R.id.colorList);

        // 初始化顏色列表和適配器
        colorList = new ArrayList<>();
        adapter = new ColorListAdapter(this, colorList);
        colorListView.setAdapter(adapter);
        colorDBHelper = new ColorDBHelper(this);
        loadColorsFromDatabase();




        // 按鈕點擊事件
        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentColor = ((ColorDrawable) textView.getBackground()).getColor();
                int redValue = Color.red(currentColor);
                int greenValue = Color.green(currentColor);
                int blueValue = Color.blue(currentColor);
                String rgbValue = "RGB: " + redValue + ", " + greenValue + ", " + blueValue;

                // 使用计数器获取唯一ID
                uniqueId++;

                // 创建 ColorData 对象并添加到列表中
                ColorData colorData = new ColorData(uniqueId, currentColor, rgbValue);
                colorList.add(colorData);

                try {
                    colorDBHelper.addColor(currentColor, rgbValue);
                    loadColorsFromDatabase();
                    adapter.notifyDataSetChanged(); // 通知列表数据发生了改变
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Database", "Error writing to database: " + e.getMessage());
                }
            }
        });
        // 使用Handler实现每3秒更新一次
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 再次执行获取颜色数据的操作
                // 你可以在这里重新发起网络请求获取最新的颜色数据
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL("http://192.168.1.110/readColors");
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("GET");

                            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                                StringBuilder response = new StringBuilder();
                                String line;
                                while ((line = reader.readLine()) != null) {
                                    response.append(line);
                                }
                                reader.close();

                                // 在UI线程中更新UI元素
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            JSONArray jsonArray = new JSONArray(response.toString());
                                            int redValue = Integer.parseInt(jsonArray.getString(0));
                                            int greenValue = Integer.parseInt(jsonArray.getString(1));
                                            int blueValue = Integer.parseInt(jsonArray.getString(2));
                                            int color = Color.rgb(redValue, greenValue, blueValue);

                                            // 设置TextView的背景色
                                            textView.setBackgroundColor(color);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                            conn.disconnect();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                handler.postDelayed(this, 3000); // 3秒
            }
        }, 3000); // 3秒

    }

    private void loadColorsFromDatabase() {
        colorList.clear(); // 清空列表

        // Fetch colors from database
        try {
            List<ColorData> colorsFromDB = colorDBHelper.getAllColors();
            for (ColorData color : colorsFromDB) {
                Log.d("Database", "Color ID: " + color.getId() + ", RGBValue: " + color.getRgbValue());
            }

            // Add colors from database to the colorList
            if (colorsFromDB != null && !colorsFromDB.isEmpty()) {
                colorList.addAll(colorsFromDB);
                adapter.notifyDataSetChanged(); // 通知列表数据发生了改变
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Database", "Error reading from database: " + e.getMessage());
        }
    }
    public void switchToColorPicker(View view) {
        Intent intent = new Intent(this, ColorPickerActivity.class);
        startActivityForResult(intent, COLOR_PREVIEW_REQUEST_CODE);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == COLOR_PREVIEW_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            int mixedColor = data.getIntExtra("mixedColor", 0);
            int mixedRed = data.getIntExtra("mixedRed", 0);
            int mixedGreen = data.getIntExtra("mixedGreen", 0);
            int mixedBlue = data.getIntExtra("mixedBlue", 0);

            // 使用計數器获取唯一ID
            uniqueId++;

            // 创建 ColorData 对象并添加到列表中
            ColorData colorData = new ColorData(mixedColor, mixedRed, mixedGreen, mixedBlue);
            colorList.add(colorData);
            adapter.notifyDataSetChanged(); // 通知列表数据发生了改变


        }
    }
}