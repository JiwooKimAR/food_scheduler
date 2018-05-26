package practice.example.com.food_scheduler;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class AbleFoodListActivity extends AppCompatActivity {
    ListView listView_ableFood;
    private AbleFoodListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_able_food_list);

        listView_ableFood = findViewById(R.id.able_food_listview);

        Intent intentFromRefrigerator = getIntent();

        adapter  = new AbleFoodListAdapter(this, R.layout.able_food_item,intentFromRefrigerator);
        adapter.getNumAbleFood();
        listView_ableFood.setAdapter(adapter);
/* 무슨 소리이진;;;;
        listView_ableFood.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent goCroll = new Intent(AbleFoodListActivity.this, WebCroller.class);
                goCroll.putExtra("FOODNAME_KEY", adapter.getItem(position).getFoodNum());
                startActivityForResult(goCroll,0);
            }
        });
*/
    }

}
