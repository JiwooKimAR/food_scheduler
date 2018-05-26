package practice.example.com.food_scheduler;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by 류성희 on 2018-05-13.
 */

public class AbleFoodListAdapter extends ArrayAdapter<AbleFoodItem> {
    Context context;
    int resId;
    private ArrayList<AbleFoodItem> datas;

    AbleFoodListAdapter(Context con, int ri, Intent intent) {
        super(con, ri);
        context = con;
        resId = ri;
        //this.datas = datas;
        this.datas = FoodMaker.testGetAbleFood(context, intent);
        /*NOTICE 경연아
        this.data = FoodMaker.getFoodList(intent) 넘기면 return 값이 arraylist여야하는데 FoodMaker보니깐 그렇게 안한거 같아! 수정요함
        !!인자값에 일단 인덴트가 없어
        !!리턴값이 void야
        함수 안에서

        ArrayList<RefrigeratorItem> ingredients = (ArrayList<RefrigeratorItem>)intent.getSerializableExtra("INGREDIENTS");
        하면 재료가 담긴 arraylist 넘겼어

        테스트를 위해 FoodMaker에 testGetAboleFood 만든거~
        */
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            Log.d("test","pt2 5");
            convertView = inflater.inflate(resId, null);
            Log.d("test","pt2 6");
            AbleFoodInfor holder = new AbleFoodInfor(convertView);
            Log.d("test","pt2 7");
            convertView.setTag(holder);
            Log.d("test","pt2 8");
        }

        Log.d("test","pt2");
        AbleFoodInfor holder = (AbleFoodInfor)convertView.getTag();

        Log.d("test","pt3");
        //ImageView ableFoodImgView = holder.ableFoodImgView;
        TextView ableFoodNameView = holder.ableFoodNameView;
        TextView ableFoodIngredientListView = holder.ableFoodIngredientListView;

        Log.d("test","pt4");
        AbleFoodItem ableFoodItem = datas.get(position);

        Log.d("test","pt5");
        ableFoodNameView.setText(ableFoodItem.getFoodName());
        //ableFoodImgView.setImageBitmap(ableFoodItem.getFoodImgView());
        ableFoodIngredientListView.setText(ableFoodItem.getFoodIngredientList());

        Log.d("test","t6");
        return convertView;
    }
    @Override
    public AbleFoodItem getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public void addItem(AbleFoodItem item){
        datas.add(item);
    }

    public void getNumAbleFood(){
        Toast.makeText(context, datas.get(0).getFoodName() + " " + datas.get(1).getFoodIngredientList(),Toast.LENGTH_SHORT).show();
    }
}
