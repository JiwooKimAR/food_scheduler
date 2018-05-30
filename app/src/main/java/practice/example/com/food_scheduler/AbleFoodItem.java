package practice.example.com.food_scheduler;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by 류성희 on 2018-05-13.
 */

public class AbleFoodItem {
    private String foodNum;
    private String foodName;
    private Bitmap foodImg;
    private String foodIngredientList;//음식에 쓰이는 재료인데 파일에서 재료가 스트링으로 통째로 읽는게 더 편할거 같아서 그냥 arraylist 안하고 String으로 해놨어

    AbleFoodItem(String foodName, String foodNum, String foodIngredientList) {

        this.foodName = foodName;
        this.foodNum = foodNum;
        this.foodIngredientList = foodIngredientList;
        System.out.println(foodNum + "-1");
        // NOTICE  : 성희 파일 주소 수정 요함
        File imgpath = new File("/drawable/f" + foodNum + ".jpg");
        if (imgpath.exists()) {
            Log.d("test","yes");
            foodImg = BitmapFactory.decodeFile(imgpath.getAbsolutePath());

        } else {
            Log.d("test","no");
            foodImg = null;
        }
    }

    public String getFoodName() { return foodName; }
    public Bitmap getFoodImgView() { return foodImg; }
    public String getFoodNum() { return foodNum; }
    public String getFoodIngredientList() { return  foodIngredientList; }
}

