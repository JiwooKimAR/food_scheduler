package practice.example.com.food_scheduler;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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

        /*
        NOTICE
        FoodMaker에서 파일을 읽고 이 클래스를 단위로 하는 arraylist만들거잖아
        생성자에 파일로 읽은 음식이름, 음식 숫자, 음식 재료 리스트를 넘겨서 추가해놓아주라~~
        이해안되거나 어려운거 있음 말해줘!
         */
        this.foodName = foodName;
        this.foodNum = foodNum;
        this.foodIngredientList = foodIngredientList;

        File imgpath = new File("/data/data/practice.example.com.food_scheduler/res" + foodNum + ".jpg");
        if (imgpath.exists()) {
            foodImg = BitmapFactory.decodeFile(imgpath.getAbsolutePath());

        } else {
            foodImg = null;
        }
        //NOTICE : 원래 음식 재료 리스트 받아오는 장소 어디니?? 여기 맞아??
    }

    public String getFoodName() { return foodName; }
    public Bitmap getFoodImgView() { return foodImg; }
    public String getFoodNum() { return foodNum; }
    public String getFoodIngredientList() { return  foodIngredientList; }
}

