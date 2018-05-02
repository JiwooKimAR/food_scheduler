package practice.example.com.food_scheduler;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_able_food_list);

    /*
        //데이터 가져오기
        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from tb_drive", null);

        ArrayList<DBInfoType> datas = new ArrayList<>();
        while(cursor.moveToNext()){
            //데이터 읽어오기
        }
        db.close();

        //어댑터 설정
        RefrigeratorAdapter adapter = new RefrigeratorAdapter(this, R.layout.refrigerator_item, datas);
        listView_ableFood.setAdapter(adapter);

        //리스트뷰 클릭시 자세한 레시피 화면으로
        listView_ableFood.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //해당 레시피 자세한 화면을 표시
                Toast.makeText(getApplicationContext(),"자세한 레시피로", Toast.LENGTH_SHORT).show();
            }
        });
        */
    }
}
