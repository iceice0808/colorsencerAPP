package com.example.colorsencer;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class ColorData implements Parcelable{
    private int colorValue;
    private String rgbValue;
    private int color;
    private  int id;
    private boolean isSelected;
    private int mixedRed;
    private int mixedGreen;
    private int mixedBlue;

    public ColorData(int id, int colorValue, String rgbValue) {
        this.id = id;
        this.colorValue = colorValue;
        this.color = colorValue; // 设置 color 变量的值为传入的 colorValue
        this.isSelected = false; // 默認未選擇

        // 提取 RGB 值
        int redValue = Color.red(colorValue);
        int greenValue = Color.green(colorValue);
        int blueValue = Color.blue(colorValue);
        Log.d("ColorData", "Red: " + redValue + ", Green: " + greenValue + ", Blue: " + blueValue);
        this.rgbValue = "RGB: " + redValue + ", " + greenValue + ", " + blueValue;
        Log.d("ColorData", "RGB Value: " + this.rgbValue); // 添加这行日志来检查 RGB 值是否正确设置

    }
    public ColorData(int colorValue, int mixedRed, int mixedGreen, int mixedBlue) {
        this.colorValue = colorValue;
        this.color = colorValue;
        this.isSelected = false;
        this.mixedRed = mixedRed;
        this.mixedGreen = mixedGreen;
        this.mixedBlue = mixedBlue;
        this.rgbValue = "RGB: " + mixedRed + ", " + mixedGreen + ", " + mixedBlue;
        Log.d("ColorData", "MixRGB Value: " + this.rgbValue);
    }

    public int getMixedRed() {
        return mixedRed;
    }

    public int getMixedGreen() {
        return mixedGreen;
    }

    public int getMixedBlue() {
        return mixedBlue;
    }
    public int getColorValue() {

        return colorValue;
    }

    public String getRgbValue() {
        Log.d("ColorData", "RGB Value: " + rgbValue);

        return rgbValue;
    }

    public int getColor() {

        return color;
    }
    public  int getId() {
        // 返回用于删除的唯一标识符，可能是行的 ID 或者其他独特的标识符
        return id;
    }
    public boolean isSelected() {

        return isSelected;
    }

    public void setSelected(boolean selected) {

        isSelected = selected;
    }
    public int describeContents() {
        return 0;
    }


    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(colorValue);
        dest.writeString(rgbValue);
        dest.writeInt(color);
        dest.writeInt(id);
        dest.writeByte((byte) (isSelected ? 1 : 0));
    }

    public  static final Parcelable.Creator<ColorData> CREATOR = new Parcelable.Creator<ColorData>() {
        @Override
        public ColorData createFromParcel(Parcel in) {
            return new ColorData(in);
        }

        @Override
        public ColorData[] newArray(int size) {
            return new ColorData[size];
        }
    };

    protected ColorData(Parcel in) {
        colorValue = in.readInt();
        rgbValue = in.readString();
        color = in.readInt();
        id = in.readInt();
        isSelected = in.readByte() != 0;
    }
}