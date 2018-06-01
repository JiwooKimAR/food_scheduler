package practice.example.com.food_scheduler;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by 류성희 on 2018-05-13.
 */

public class AbleFoodItem {
    private String foodNum;
    private String foodName;
    private ImageView foodImg;
    private String foodIngredientList;//음식에 쓰이는 재료인데 파일에서 재료가 스트링으로 통째로 읽는게 더 편할거 같아서 그냥 arraylist 안하고 String으로 해놨어

    AbleFoodItem(String foodName, String foodNum, String foodIngredientList) {

        this.foodName = foodName;
        this.foodNum = foodNum;
        this.foodIngredientList = foodIngredientList;
    }

    public String getFoodName() { return foodName; }
    public ImageView getFoodImgView() { return foodImg; }
    public String getFoodNum() { return foodNum; }
    public String getFoodIngredientList() { return  foodIngredientList; }
    public int getFoodImageResource (View v) {
        Log.i("FoodItem", Boolean.toString(v.getResources() == null) );
        return v.getResources().getIdentifier("@drawable/f" + foodNum, "drawable", "practice.example.com.food_scheduler");
    }
}

