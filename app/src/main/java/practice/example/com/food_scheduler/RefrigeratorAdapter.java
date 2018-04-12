package practice.example.com.food_scheduler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by user on 2018-04-12.
 */

public class RefrigeratorAdapter extends ArrayAdapter<DBInfoType>{
    Context context;
    int resId;
    ArrayList<DBInfoType> datas;

    public RefrigeratorAdapter(Context context, int resId, ArrayList<DBInfoType> datas){
        super(context, resId);
        this.context = context;
        this.resId = resId;
        this.datas = datas;
    }

    @Override
    public int getCount(){
        return datas.size();
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resId, null);
            RefriItemInfor holder = new RefriItemInfor(convertView);
            convertView.setTag(holder);
        }
        RefriItemInfor holder = (RefriItemInfor)convertView.getTag();

        ImageView itemImageView = holder.itemImageView;
        TextView itemNameView = holder.itemNameView;
        TextView itemAmountView = holder.itemAmountView;
        TextView itemDateView = holder.itemDateView;

        final DBInfoType infoType = datas.get(position);

        itemNameView.setText(infoType.name);
        itemAmountView.setText(infoType.amount);

        //유통기간에 따른 글씨색 정하기
        itemDateView.setText(infoType.date);

        if(infoType.img.equals("Y")) {
            //사용자가 지정한 이미지 설정
            itemImageView.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_type_image, null));
        }else{
            //대표적인 이미지 불러오기
        }

        return convertView;
    }
}
