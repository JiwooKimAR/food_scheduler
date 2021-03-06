package practice.example.com.food_scheduler;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * created by SeLee Chae
 * add new item or edit selected item
 */
public class ItemInfoActivity extends AppCompatActivity {
    int cancel_mode_set = 0, add_mode_set = 1, edit_mode_set = 2;
    ImageView img_item;
    EditText edit_itemName, edit_itemAmount, edit_itemDate;
    Button btn_ok, btn_cancel;
    Drawable imgDrawable;

    //인덴트 불로오기
    Intent intent;
    Intent intent_ItemInfoToRefri;
    boolean edit_mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);

        //findview처리
        img_item = (ImageView)findViewById(R.id.img_item);
        edit_itemName = (EditText)findViewById(R.id.edit_itemName);
        edit_itemAmount = (EditText)findViewById(R.id.edit_itemAmount);
        edit_itemDate = (EditText)findViewById(R.id.edit_itemDate);
        btn_ok = (Button)findViewById(R.id.btn_itemInfo_ok);
        btn_cancel = (Button)findViewById(R.id.btn_itemInfo_cancel);

        //인덴트 설정
        intent_ItemInfoToRefri = new Intent();
        intent = getIntent();
        edit_mode = intent.getBooleanExtra("EDITMODE",false);

        Picasso.get().load("https://static9.depositphotos.com/1077687/1237/v/950/depositphotos_12370584-stock-illustration-chefs-hat.jpg"/**/).placeholder(R.drawable.ic_type_image).into(img_item);
        //edit 모드라면 값 로딩
        if(edit_mode) {
            RefrigeratorItem item = (RefrigeratorItem) intent.getSerializableExtra("ITEM");
            edit_itemName.setText(item.getName());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String strDate = sdf.format(item.getDate().getTime());
            edit_itemDate.setText(strDate);
            edit_itemAmount.setText(item.getAmount());
            if (item.getByteArray() != null) {
                byte[] byteArr = item.getByteArray();
                Bitmap bit = byteArrayToBitmap(byteArr);
                img_item.setImageBitmap(bit);
            }
        }

        //버튼 클릭 리스너 만들기
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btn_itemInfo_ok:
                        //값 받아오기
                        String name = edit_itemName.getText().toString();
                        String amount = edit_itemAmount.getText().toString();
                        Calendar date = Calendar.getInstance();
                        BitmapDrawable bit_D = (BitmapDrawable) img_item.getDrawable();
                        Bitmap bit = bit_D.getBitmap();
                        byte[] byteArr = bitmapToByteArray(bit);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                        try {
                            date.setTime(sdf.parse(edit_itemDate.getText().toString()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        //값 전달
                        if(!edit_mode) {//새로운 항목 만들어 인덴트에 추가하여 전달
                            intent_ItemInfoToRefri.putExtra("ADDINFO", new RefrigeratorItem(name,amount,date,byteArr));
                            setResult(add_mode_set, intent_ItemInfoToRefri);
                        }
                        else {//기존 인덴트 속 아이템 꺼내와 변경하기
                            // 수정하면 2017-12-09로 date 값 변경됨
                            intent_ItemInfoToRefri.putExtra("POSITION",intent.getIntExtra("POSITION",-1));
                            intent_ItemInfoToRefri.putExtra("EDITINFO", new RefrigeratorItem(name,amount,date,byteArr));
                            setResult(edit_mode_set, intent_ItemInfoToRefri);
                        }
                        finish();
                        break;
                    case R.id.btn_itemInfo_cancel:
                        //refrigeratorActivity로
                        finish();
                        break;
                    case R.id.img_item:
                        //팝업창 띄우기(카메라 연결하여 사진찍기 또는 앨범연결)
                        PopupMenu popup = new PopupMenu(getApplicationContext(), v);

                        getMenuInflater().inflate(R.menu.set_img_option_menu, popup.getMenu());
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch(item.getItemId()){
                                    case R.id.optionMenu_setImgByCamera:
                                        //카메라 연동
                                        break;
                                    case R.id.optionMeue_setImgWithGalary:
                                        //갤러리 연동
                                        break;
                                }
                                return false;
                            }
                        });
                        popup.show();
                        break;
                }
            }
        };
        btn_ok.setOnClickListener(listener);
        btn_cancel.setOnClickListener(listener);
        img_item.setOnClickListener(listener);
    }

    public Bitmap byteArrayToBitmap(byte[] byteArray) {
        Bitmap bitmap = null;
        bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        byteArray = null;
        return bitmap;
    }

    public byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
}
