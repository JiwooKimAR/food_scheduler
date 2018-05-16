package practice.example.com.food_scheduler;

import android.content.Context;
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
    private ArrayList<AbleFoodItem> datas;

    AbleFoodListAdapter(Context con, int ri, ArrayList<AbleFoodItem> datas) {
        super(con, ri);
        context = con;
        resId = ri;
        this.datas = datas;
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

        ImageView itemImageView = holder.itemImageView;
        TextView itemNameView = holder.itemNameView;

        AbleFoodItem item = datas.get(position);

        itemNameView.setText(item.getName());
        itemImageView.setImageBitmap(item.getImg());

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

    public void initForTest(){//처음에 파일 읽어오기
        //1개의 데이터 추가
        datas.add(new AbleFoodItem("1","스파게티"));
    }
}
