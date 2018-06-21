    package practice.example.com.food_scheduler;

    import android.view.View;
    import android.widget.ImageView;
    import android.widget.TextView;

    /**
     * Created by 류성희 on 2018-05-13.
     */

    public class AbleFoodInfo {
        public ImageView ableFoodImgView;
        public TextView ableFoodNameView;
        public TextView ableFoodIngredientListView;

        public AbleFoodInfo(View root) {
            ableFoodImgView = root.findViewById(R.id.img_able_food);
            ableFoodNameView = root.findViewById(R.id.name_able_food);
            ableFoodIngredientListView = root.findViewById(R.id.ingredients_list_able_food);
        }
    }
