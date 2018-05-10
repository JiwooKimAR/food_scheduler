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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by user on 2018-04-12.
 */

public class RefrigeratorAdapter extends ArrayAdapter<RefrigeratorItem>{
    Context context;
    int resId;
    private ArrayList<RefrigeratorItem> datas;

    public RefrigeratorAdapter(Context context, int resId, ArrayList<RefrigeratorItem> datas){
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
        TextView itemValueView = holder.itemValueView;

        RefrigeratorItem item = datas.get(position);

        itemNameView.setText(item.getName());
        itemAmountView.setText(item.getAmount());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //유통기간에 따른 글씨색 정하기
        String strDate = sdf.format(item.getDate().getTime());
        itemDateView.setText(strDate);
        itemValueView.setText(String.valueOf(item.getValue()));

        if(item.getImg() == null) {
            //사용자가 지정한 이미지 설정
            itemImageView.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_type_image, null));
        }else{
            itemImageView.setImageDrawable(item.getImg());
        }

        return convertView;
    }

    @Override
    public RefrigeratorItem getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void initForTest(){//처음에 파일 읽어오기
        //1개의 데이터 추가
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, 4,05);
        datas.add(new RefrigeratorItem("계란", 3800 ,"30개" , calendar, null));
        calendar.set(2018, 8,23);
        datas.add(new RefrigeratorItem("빼빼로", 2500, "1통", calendar, null));
    }

    public void dataChanged(){
        this.notifyDataSetChanged();
    }

    public void addItem(RefrigeratorItem item){
        datas.add(item);
    }

    public void removeItem(int position){
        datas.remove(position);
    }
}
