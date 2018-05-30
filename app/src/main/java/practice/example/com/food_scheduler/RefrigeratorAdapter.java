package practice.example.com.food_scheduler;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
    private SparseBooleanArray checkedBooleanArray;

    public RefrigeratorAdapter(Context context, int resId, ArrayList<RefrigeratorItem> datas){
        super(context, resId);
        this.context = context;
        this.resId = resId;
        this.datas = datas;
        checkedBooleanArray = new SparseBooleanArray();
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
        CheckBox itemCheckBox = holder.itemCheckBox;
        itemCheckBox.setTag(position);
        itemCheckBox.setOnCheckedChangeListener(itemCheckBoxListener);

        RefrigeratorItem item = datas.get(position);

        itemNameView.setText(item.getName());
        itemAmountView.setText(item.getAmount());

        //유통기간
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = sdf.format(item.getDate().getTime());
        itemDateView.setText(strDate);

        if(item.getByteArray() == null) {
            itemImageView.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_type_image, null));
        }else{
            //사용자가 시정한 이미지 불러오기
            byte[] byteArr = item.getByteArray();
            Bitmap img = byteArrayToBitmap(byteArr);
            itemImageView.setImageBitmap(img);
        }

        itemCheckBox.setChecked(false);
        if(item.getShowCheckBox()) itemCheckBox.setVisibility(View.VISIBLE);
        else itemCheckBox.setVisibility(View.GONE);

        return convertView;
    }

    CompoundButton.OnCheckedChangeListener itemCheckBoxListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            checkedBooleanArray.put((Integer) buttonView.getTag(), isChecked);
        }
    };

    @Override
    public RefrigeratorItem getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public ArrayList<RefrigeratorItem> getCheckedItems() {//send arraylist of selected items by CheckBox
        ArrayList<RefrigeratorItem> selectedDatas = new ArrayList<>();

        for(int i = 0; i < datas.size(); i++)
            if(checkedBooleanArray.get(i)) selectedDatas.add(datas.get(i));
        return selectedDatas;
    }

    public void initForTest(){//처음에 파일 읽어오기
        //1개의 데이터 추가
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, 4,05);
        datas.add(new RefrigeratorItem("김치", "1통" , calendar, null));
        calendar.set(2018, 8,23);
        datas.add(new RefrigeratorItem("된장", "1팩", calendar, null));
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

    public void removeItem(RefrigeratorItem item){
        datas.remove(item);
    }

    public void setCheckBoxVisibility(boolean state) {
        if(state == true)//set CheckBox Visible
            for(int position = 0; position < datas.size(); position++)
                datas.get(position).setShowCheckBox(true);
        else
            for(int position = 0; position < datas.size(); position++)
                datas.get(position).setShowCheckBox(false);
        dataChanged();
    }

    public Bitmap byteArrayToBitmap(byte[] byteArray) {
        Bitmap bitmap = null;
        bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        byteArray = null;
        return bitmap;
    }
}
