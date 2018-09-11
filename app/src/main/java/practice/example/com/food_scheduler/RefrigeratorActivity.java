package practice.example.com.food_scheduler;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * created by SeLee Chae
 * Activity to show list of refrigerator items(ingredients)
 */
public class RefrigeratorActivity extends AppCompatActivity {
    boolean btn_cookSecondClick = false;
    private RefrigeratorAdapter adapter;
    Button btn_cook, btn_add, btn_choose, btn_delete;
    ListView listView_refrigerator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refrigerator_new);

        adapter  = new RefrigeratorAdapter(this, R.layout.refrigerator_item, new ArrayList<RefrigeratorItem>());
        adapter.initForTest();//put some initialized items for test

        listView_refrigerator = findViewById(R.id.refrigerator_listview);
        btn_cook = findViewById(R.id.btn_cook);
        btn_add = findViewById(R.id.btn_add);
        //btn_choose = findViewById(R.id.btn_choose);
        btn_delete = findViewById(R.id.btn_delete);
        adapter.setCheckBoxVisibility(true);

        //ClickListener set
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btn_add:
                        //show popup menu
                        PopupMenu popup = new PopupMenu(getApplicationContext(), v);
                        getMenuInflater().inflate(R.menu.add_option_menu, popup.getMenu());

                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch(item.getItemId()){
                                    case R.id.optionMenu_addByBarcode://add by barcode
                                        Intent intent_BarcodeAdd = new Intent(RefrigeratorActivity.this, BarcodeScannerActivity.class);
                                        startActivityForResult(intent_BarcodeAdd, 0);
                                        //바코드로 아이템 추가 코드
                                        break;
                                    case R.id.optionMeue_addBySelf://add by myself
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
                        //pass intent with array of refrigerator items selected by user
                        ArrayList<RefrigeratorItem> ingredients = adapter.getCheckedItems();

                        if(ingredients.size() > 0) {
                            Intent intent_RefriToAbleFood = new Intent(RefrigeratorActivity.this, AbleFoodListActivity.class);
                            intent_RefriToAbleFood.putExtra("INGREDIENTS", ingredients);
                            startActivity(intent_RefriToAbleFood);
                        }else {//there is no selected item when passing
                            Toast.makeText(getApplicationContext(), "아무것도 선택하지 않으셨습니다.", Toast.LENGTH_SHORT).show();
                        }

                        //initialize checkBox
                        btn_cookSecondClick = false;
                        //adapter.setCheckBoxVisibility(false);
                        //btnChangedInitialMode();//change button alignment(cook & choose & delete -> choose & add)
                        break;
                   /* case R.id.btn_choose:
                        if(btn_cookSecondClick == false) {
                            //change check box visible
                            adapter.setCheckBoxVisibility(true);
                            btn_cookSecondClick = true;
                            btnChangedChooseMode(); //change button alignment(choose & add -> cook & choose & delete)
                        }else{
                            adapter.setCheckBoxVisibility(false);
                            btnChangedInitialMode();
                            btn_cookSecondClick = false;
                        }
                        break;*/
                    case R.id.btn_delete:
                        //pass intent with array of refrigerator items selected by user
                        ArrayList<RefrigeratorItem> deleteItems = adapter.getCheckedItems();

                        if(deleteItems.size() > 0) {
                            for(int i = 0; i < deleteItems.size(); i++){
                                adapter.removeItem(deleteItems.get(i));
                            }
                            adapter.dataChanged();
                        }else {//there is no selected item when passing
                            Toast.makeText(getApplicationContext(), "아무것도 선택하지 않으셨습니다.", Toast.LENGTH_SHORT).show();
                        }

                        //initialize checkBox
                        btn_cookSecondClick = false;
                        //adapter.setCheckBoxVisibility(false);
                        //btnChangedInitialMode();//change button alignment(cook & choose & delete -> choose & add)
                        break;
                }
            }
        };
        listView_refrigerator.setAdapter(adapter);
        btn_cook.setOnClickListener(listener);
        btn_add.setOnClickListener(listener);
        btn_choose.setOnClickListener(listener);
        btn_delete.setOnClickListener(listener);

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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        System.out.println("onActivityResult 나온다");
        //check this is result of add_mode or edit_mode
        int add_mode_set = 1, edit_mode_set = 2;
        if (resultCode == add_mode_set) {//make new refrigerator item
            adapter.addItem((RefrigeratorItem) intent.getSerializableExtra("ADDINFO"));
            adapter.dataChanged();
        } else if (resultCode == edit_mode_set) {//revise selected item
            RefrigeratorItem editItem = (RefrigeratorItem) intent.getSerializableExtra("EDITINFO");
            int pos = intent.getIntExtra("POSITION", -1);
            if (pos != -1) {
                adapter.getItem(pos).setName(editItem.getName());
                adapter.getItem(pos).setAmount(editItem.getAmount());
                adapter.getItem(pos).setDate(editItem.getDate());
                adapter.getItem(pos).setByteArray(editItem.getByteArray());
            } else Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            adapter.dataChanged();
        }
    }
    public void btnChangedChooseMode(){//set upper button like (Cook & Choose & Delete)
        btn_cook.setVisibility(View.VISIBLE);
        btn_add.setVisibility(View.GONE);
        btn_delete.setVisibility(View.VISIBLE);
    }
    public void btnChangedInitialMode(){//set supper button like (Choose & Add)
        btn_cook.setVisibility(View.GONE);
        btn_add.setVisibility(View.VISIBLE);
        btn_delete.setVisibility(View.GONE);
    }
}
