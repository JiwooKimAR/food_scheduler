package practice.example.com.food_scheduler;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;

import java.io.File;

import android.app.Activity;

/**
 * Created by 류성희 on 2018-05-13.
 */

public class AbleFoodItem {
    private String FoodNum;
    private String FoodName;
    private Bitmap FoodImg;

    AbleFoodItem(String foodNum, String foodName) {
        File imgpath = new File("/data/data/practice.example.com.food_scheduler/res" + foodNum + ".jpg");
        if(imgpath.exists()) {
               FoodImg = BitmapFactory.decodeFile(imgpath.getAbsolutePath());
        }
        FoodName = foodName;
        FoodNum = foodNum;
    }

    public String getName() { return FoodName; }
    public Bitmap getImg() { return FoodImg; }
    public String getNum() { return FoodNum; }
}
