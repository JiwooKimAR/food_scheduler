package practice.example.com.food_scheduler;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * http://recipes4dev.tistory.com/48 참고해서 listview 아이테 수정 및 추가 설정
 */
public class RefrigeratorActivity extends AppCompatActivity { // dfdf
    Button btn_cook, btn_add;
    ListView listView_refrigerator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refrigerator);
        listView_refrigerator = findViewById(R.id.refrigerator_listview);
        btn_cook = findViewById(R.id.btn_cook);
        btn_add = findViewById(R.id.btn_add);
        
        //클릭리스너 객체 생성
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btn_add:
                        //add화면으로 넘어가기
                        Toast.makeText(getApplicationContext(), "아이템 add화면으로", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.btn_cook:
                        //선택된 재료 존재시 cook화면으로 넘어가기
                        Toast.makeText(getApplicationContext(),"아이템 cook화면으로",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };


        //데이터 가져오기
        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from tb_drive", null);

        ArrayList<DBInfoType> datas = new ArrayList<>();
        while(cursor.moveToNext()){
            DBInfoType infoType = new DBInfoType();
            infoType.name = cursor.getString(1);
            infoType.date = cursor.getString(2);
            infoType.amount = cursor.getString(3);
            infoType.img = cursor.getString(4);
            datas.add(infoType);
        }
        db.close();

        //어댑터 설정
        RefrigeratorAdapter adapter = new RefrigeratorAdapter(this, R.layout.refrigerator_item, datas);
        listView_refrigerator.setAdapter(adapter);

        //아이템을 클릭시 수정화면으로 넘어가기
        listView_refrigerator.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //수정화면으로 넘어가기
                Toast.makeText(RefrigeratorActivity.this,id+" 수정화면으로 넘어갑니다",Toast.LENGTH_SHORT).show();
            }
        });

        //아이템 길게 누를시 라디오 버튼
        listView_refrigerator.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //radio button으로 바뀌기
                Toast.makeText(RefrigeratorActivity.this,"라디오버튼으로 바뀝니다",Toast.LENGTH_SHORT);
                return true;
            }
        });

        //아이템 삭제
        listView_refrigerator.setOnTouchListener(new OnSwipeTouchListener(RefrigeratorActivity.this){
            @Override
            public void onSwipeLeft(){
                //삭제코드
                Toast.makeText(RefrigeratorActivity.this,"리스트뷰를 삭제합니다",Toast.LENGTH_SHORT);
            }
        });

        btn_cook.setOnClickListener(listener);
        btn_add.setOnClickListener(listener);
    }
}
