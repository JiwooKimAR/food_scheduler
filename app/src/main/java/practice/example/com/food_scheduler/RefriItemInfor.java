package practice.example.com.food_scheduler;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by user on 2018-04-12.
 */

public class RefriItemInfor {
    public ImageView itemImageView;
    public TextView itemNameView;
    public TextView itemAmountView;
    public TextView itemDateView;

    public RefriItemInfor(View root){
        itemImageView = root.findViewById(R.id.refrigerator_item_type_image);
        itemNameView = root.findViewById(R.id.refrigerator_item_name);
        itemAmountView = root.findViewById(R.id.refrigerator_item_amount);
        itemDateView = root.findViewById(R.id.refrigerator_item_date);
    }
}
