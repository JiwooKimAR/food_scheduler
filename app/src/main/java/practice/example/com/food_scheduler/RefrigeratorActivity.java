package practice.example.com.food_scheduler;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * http://recipes4dev.tistory.com/48 참고해서 listview 아이테 수정 및 추가 설정
 */
public class RefrigeratorActivity extends AppCompatActivity {
    int add_mode_set = 1, edit_mode_set = 2;
    private RefrigeratorAdapter adapter;
    Button btn_cook, btn_add;
    ListView listView_refrigerator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refrigerator);

        adapter  = new RefrigeratorAdapter(this, R.layout.refrigerator_item, new ArrayList<RefrigeratorItem>());
        adapter.initForTest();//초기화용 테스트용

        listView_refrigerator = findViewById(R.id.refrigerator_listview);
        btn_cook = findViewById(R.id.btn_cook);
        btn_add = findViewById(R.id.btn_add);
        
        //클릭리스너 객체 생성
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btn_add:
                        PopupMenu popup = new PopupMenu(getApplicationContext(), v);

                        getMenuInflater().inflate(R.menu.add_option_menu, popup.getMenu());
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch(item.getItemId()){
                                    case R.id.optionMenu_addByBarcode:
                                        Toast.makeText(getApplicationContext(), "바코드로 아이템 추가", Toast.LENGTH_SHORT).show();
                                        //바코드로 아이템 추가 코드
                                        break;
                                    case R.id.optionMeue_addBySelf:
                                        Intent intent_RefriToAdd = new Intent(RefrigeratorActivity.this,ItemInfoActivity.class);
                                        startActivityForResult(intent_RefriToAdd,0);
                                        break;
                                }
                                return false;
                            }
                        });
                        popup.show();
                        break;
                    case R.id.btn_cook:
                        //선택된 아이템 테스트를 어레이 리스트로 넘기기
                        ArrayList<RefrigeratorItem> ingredients = new ArrayList<>();
                        //for test
                        ingredients.add(adapter.getItem(0));
                        ingredients.add(adapter.getItem(1));
                        Intent intent_RefriToAbleFood = new Intent(RefrigeratorActivity.this,AbleFoodListActivity.class);
                        intent_RefriToAbleFood.putExtra("INGREDIENTS", ingredients);
                        startActivity(intent_RefriToAbleFood);
                        break;
                }
            }
        };




        //어댑터 설정
        listView_refrigerator.setAdapter(adapter);

        //아이템을 클릭시 수정화면으로 넘어가기
        listView_refrigerator.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent_HomeToEdit = new Intent(RefrigeratorActivity.this, ItemInfoActivity.class);
                intent_HomeToEdit.putExtra("ITEM", adapter.getItem(position));
                intent_HomeToEdit.putExtra("POSITION",position);
                intent_HomeToEdit.putExtra("EDITMODE",true);
                startActivityForResult(intent_HomeToEdit,0);
            }
        });

        //아이템 길게 누를시 라디오 버튼
        listView_refrigerator.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("myTag", "fired");
                //radio button으로 바뀌기
                Toast.makeText(RefrigeratorActivity.this,"라디오버튼으로 바뀝니다",Toast.LENGTH_SHORT);
                return true;
            }
        });

        //아이템 삭제
        listView_refrigerator.setOnTouchListener(new OnSwipeTouchListener(RefrigeratorActivity.this){
            public void onSwipeLeft(){
                //삭제코드
                Toast.makeText(RefrigeratorActivity.this,"리스트뷰를 삭제합니다",Toast.LENGTH_SHORT);
            }
        });

        btn_cook.setOnClickListener(listener);
        btn_add.setOnClickListener(listener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        if(requestCode == 0) {
            if (resultCode == add_mode_set) {
                Toast.makeText(this, "Comebakc", Toast.LENGTH_SHORT).show();
                adapter.addItem((RefrigeratorItem) intent.getSerializableExtra("ADDINFO"));
                adapter.dataChanged();
            } else if (resultCode == edit_mode_set) {
                RefrigeratorItem editItem = (RefrigeratorItem) intent.getSerializableExtra("EDITINFO");
                int pos = intent.getIntExtra("POSITION", -1);
                if (pos != -1) {
                    adapter.getItem(pos).setName(editItem.getName());
                    adapter.getItem(pos).setValue(editItem.getValue());
                    adapter.getItem(pos).setAmount(editItem.getAmount());
                    adapter.getItem(pos).setDate(editItem.getDate());
                    adapter.getItem(pos).setImg(editItem.getImg());
                } else Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                adapter.dataChanged();
            }
        }
    }
}
