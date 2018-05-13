package practice.example.com.food_scheduler;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by 류성희 on 2018-05-13.
 */

public class AbleFoodInfor {
    public ImageView itemImageView;
    public TextView itemNameView;

    public AbleFoodInfor(View root) {
        itemImageView = root.findViewById(R.id.refrigerator_item_type_image);
        itemNameView = root.findViewById(R.id.refrigerator_item_name);
    }
}
