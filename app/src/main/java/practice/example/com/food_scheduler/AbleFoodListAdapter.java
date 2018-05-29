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

        //FoodMaker maker = new FoodMaker(getApplicationContext());

        context = con;
        resId = ri;
        //this.datas = maker.Find(intent);
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
            convertView = inflater.inflate(resId, null);
            AbleFoodInfor holder = new AbleFoodInfor(convertView);
            convertView.setTag(holder);
        }

        AbleFoodInfor holder = (AbleFoodInfor)convertView.getTag();

        //ImageView ableFoodImgView = holder.ableFoodImgView;
        TextView ableFoodNameView = holder.ableFoodNameView;
        TextView ableFoodIngredientListView = holder.ableFoodIngredientListView;


        AbleFoodItem ableFoodItem = datas.get(position);
        ableFoodNameView.setText(ableFoodItem.getFoodName());

        //ableFoodImgView.setImageBitmap(ableFoodItem.getFoodImgView());
        ableFoodIngredientListView.setText(ableFoodItem.getFoodIngredientList());

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
