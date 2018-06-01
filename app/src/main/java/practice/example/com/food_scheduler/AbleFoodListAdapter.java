package practice.example.com.food_scheduler;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 류성희 on 2018-05-13.
 */

public class AbleFoodListAdapter extends ArrayAdapter<AbleFoodItem> {
    Context context;
    int resId;
    private ArrayList<AbleFoodItem> data;

    AbleFoodListAdapter(Context con, int ri, Intent intent) {
        super(con, ri);

        FoodMaker maker = new FoodMaker(con);

        context = con;
        resId = ri;
        this.data = maker.Find(intent);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resId, null);
            AbleFoodInfo holder = new AbleFoodInfo(convertView);
            convertView.setTag(holder);
        }

        AbleFoodInfo holder = (AbleFoodInfo)convertView.getTag();

        ImageView ableFoodImgView = holder.ableFoodImgView;
        TextView ableFoodNameView = holder.ableFoodNameView;
        TextView ableFoodIngredientListView = holder.ableFoodIngredientListView;

        AbleFoodItem ableFoodItem = data.get(position);

        ableFoodNameView.setText(ableFoodItem.getFoodName());
        ableFoodIngredientListView.setText(ableFoodItem.getFoodIngredientList());

        ableFoodImgView.setImageResource(ableFoodItem.getFoodImageResource(ableFoodImgView));

        return convertView;
    }
    @Override
    public AbleFoodItem getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public void addItem(AbleFoodItem item){
        data.add(item);
    }
}
