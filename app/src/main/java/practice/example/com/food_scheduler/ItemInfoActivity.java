package practice.example.com.food_scheduler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

/*
 */
public class ItemInfoActivity extends AppCompatActivity {
    ImageView img_item;
    EditText edit_itemName, edit_itemAmount, edit_itemDate;
    Button btn_ok, btn_cancel;

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

        //picasso이용하여 해당 url의 그림 불러오기
        Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(img_item);

        //버튼 클릭 리스너 만들기
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btn_itemInfo_ok:
                        //불러온 이미지 저장하기도 필요
                        Toast.makeText(getApplicationContext(), "아이템 refrigeratorActivity로", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.btn_itemInfo_cancel:
                        //refrigeratorActivity로
                        Toast.makeText(getApplicationContext(),"아이템 refrigeratorActivity로",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.img_item:
                        //팝업창 띄우기(카메라 연결하여 사진찍기 또는 앨범연결)
                        Toast.makeText(getApplicationContext(),"아이템 사진변경",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        btn_ok.setOnClickListener(listener);
        btn_cancel.setOnClickListener(listener);
        img_item.setOnClickListener(listener);
    }
}
