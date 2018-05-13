package practice.example.com.food_scheduler;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

        adapter  = new AbleFoodListAdapter(this, R.layout.able_food_item, new ArrayList<AbleFoodItem>());
        //adapter.initForTest();
        listView_ableFood = findViewById(R.id.able_food_listview);

        listView_ableFood.setAdapter(adapter);

        listView_ableFood.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),"Adsf",Toast.LENGTH_SHORT).show();
                Intent goCroll = new Intent(AbleFoodListActivity.this, WebCroller.class);
                goCroll.putExtra("FOODNAME_KEY", adapter.getItem(position).getNum());
                startActivityForResult(goCroll,0);
            }
        });
    }

}
