package practice.example.com.food_scheduler;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by SeLee Chae
 */

public class RefriItemInfor {
    public ImageView itemImageView;
    public TextView itemNameView;
    public TextView itemAmountView;
    public TextView itemDateView;
    public CheckBox itemCheckBox;

    public RefriItemInfor(View root){
        itemImageView = root.findViewById(R.id.refrigerator_item_type_image);
        itemNameView = root.findViewById(R.id.refrigerator_item_name);
        itemAmountView = root.findViewById(R.id.refrigerator_item_amount);
        itemDateView = root.findViewById(R.id.refrigerator_item_date);
        itemCheckBox = root.findViewById(R.id.refrigerator_item_checkBox);
    }
}
