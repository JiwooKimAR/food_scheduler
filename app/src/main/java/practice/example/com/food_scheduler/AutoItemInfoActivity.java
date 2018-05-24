package practice.example.com.food_scheduler;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AutoItemInfoActivity extends AppCompatActivity {
    int cancel_mode_set = 0, add_mode_set = 1, edit_mode_set = 2;

    String barcode;

    public AutoSetting SetContents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);

        Intent intent = getIntent();
        barcode = intent.getStringExtra("BarCode");

        SetContents = new AutoSetting();
        SetContents.execute();

        final EditText editNameAuto = findViewById(R.id.edit_itemName);
        final EditText editDateAuto = findViewById(R.id.edit_itemDate); // 구글 오픈 클라우드 비전 API를 통해 유통기한을 찾아야 함
        final EditText editWeightAuto = findViewById(R.id.edit_itemAmount);
        final ImageView productImage = findViewById(R.id.img_item);
        Button OK = findViewById(R.id.btn_itemInfo_ok);
        Button No = findViewById(R.id.btn_itemInfo_cancel);

        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_itemInfo_ok:
                        Toast.makeText(AutoItemInfoActivity.this, "재료 추가", Toast.LENGTH_LONG).show();

                        String ENA = editNameAuto.getText().toString();
                        String EWA = editWeightAuto.getText().toString();
                        String EDA = editDateAuto.getText().toString();
                        Calendar date = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            date.setTime(sdf.parse(EDA));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        BitmapDrawable EIA_B = (BitmapDrawable)productImage.getDrawable();
                        Drawable EIA = EIA_B; // 이미지 오류남

                        Intent intent = new Intent();
                        intent.putExtra("ADDINFO", new RefrigeratorItem(ENA, EWA, date, null)); // 이미지를 null로 하니 오류 해결, 왜지?
                        System.out.println("ADDINFO 성공");
                        setResult(add_mode_set, intent);
                        System.out.println("RefrigeratorActivity로 가는거 성공");
                        finish();
                        // 재료 추가하고 재료 리스트뷰로 향하기
                        break;
                    case R.id.btn_itemInfo_cancel:
                        Toast.makeText(AutoItemInfoActivity.this, "취소", Toast.LENGTH_LONG).show();
                        Intent intent1 = new Intent(AutoItemInfoActivity.this, RefrigeratorActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent1);
                        finish(); // 필요한가?
                        // 재료 리스트뷰로 위에 쌓인 인텐트 모두 지우고 향하기
                        // '취소' 버튼이 아니라 뒤로가기 버튼을 누르면 빈 레이아웃이 뜬다. 나중에 추가해야 할듯.
                        break;
                    case R.id.img_item: // ItemInfoActivity.java 에 있는 코드랑 똑같음. 앞의 액티비티가 바뀐다면 이것도 바뀌어야 함.
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

        OK.setOnClickListener(listener);
        No.setOnClickListener(listener);
        productImage.setOnClickListener(listener);
    }

    public class AutoSetting extends AsyncTask<Void, Void, Void> { // 다 끝나면 println 문 다 지우기
        Bitmap img = null;
        String Name = null;
        String Weight = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void ... params) {
            String text = null;
            String url = "http://www.koreannet.or.kr/home/hpisSrchGtin.gs1?gtin=" + barcode;
            try {
                Document document = Jsoup.connect(url).get();
                String checkForSearch = document.toString();
                if (checkForSearch.contains("Sorry")) {
                    // 아무것도 안 함
                }
                else {
                    // 사진 시작
                    String checkForImage = document.select("#detailImage").attr("src");
                    String imgUrl;
                    if (!(checkForImage.contains("/images/common/no_img.gif"))) {
                        imgUrl = document.select("#thumImg1").attr("src");

                        URL imgurl = new URL(imgUrl);
                        URLConnection conn = imgurl.openConnection();
                        BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
                        img = BitmapFactory.decodeStream(bis);
                        if (img != null) {
                            // 성공
                        }
                        bis.close();
                    } else {
                        imgUrl = "None";
                        // 이미지만 없는 거
                    }
                    // 사진 끝

                    // 이름+중량 시작
                    Elements NameWeight = document.select(".productTit");
                    text = NameWeight.text();

                    int i = 0;
                    while (text.charAt(i) != ' ') i++;
                    i++;
                    text = text.substring(i, text.length());

                    i = 0;
                    while (isNumber(text.charAt(i++))) ;

                    if (i != text.length()) {
                        i = i - 2;
                        Name = text.substring(0, i);
                        Weight = text.substring(i + 1, text.length());
                    } else {
                        Name = text;
                    }
                    // 이름+중량 끝
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //String img_save_path = "C:\\Users\\JW\\Nox_share\\Image";
            //String img_name = "1"; // 몇 번째 항목인지 받아서 넣어야 될 듯

            if (img != null) {
                ImageView productImage = findViewById(R.id.img_item);
                productImage.setImageBitmap(img);
                // saveBitmaptoJpeg(img, img_save_path, img_name); // 이미지 저장은 가능하나, 그 디렉토리를 어디로 둬야 할지 모르겠음.
            }
            else {
                // 사진 없음
            }
            if (Name != null) {
                EditText editMaterialAuto = findViewById(R.id.edit_itemName);
                editMaterialAuto.setText(Name); // 이름이 edittext보다 길 때 처리하기
            }
            else {
                // 이름 없음
            }
            if (Weight != null) {
                EditText editWeightAuto = findViewById(R.id.edit_itemAmount);
                editWeightAuto.setText(Weight);
            }
            else {
                // 중량 없음
            }
        }
    }

    // 비트맵을 다른 확장자 사진 파일로 저장하는 합수, 파일 저장 경로를 알아야 함.
    public static void saveBitmaptoJpeg(Bitmap bitmap, String folder, String name) {
        //String ex_storage =Environment.getExternalStorageDirectory().getAbsolutePath();
        // Get Absolute Path in External Sdcard
        //String foler_name = "/"+folder+"/";
        String file_name = name + ".jpg";
        //String string_path = ex_storage+foler_name;

        File file_path;
        try {
            file_path = new File(folder + "/");
            if (!file_path.isDirectory()) {
                file_path.mkdirs();
            }
            FileOutputStream out = new FileOutputStream(folder + "/" + file_name);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();

        } catch (FileNotFoundException exception) {
            Log.e("FileNotFoundException", exception.getMessage());
        } catch (IOException exception) {
            Log.e("IOException", exception.getMessage());
        }
    }

    public static boolean isNumber(char A) {
        if (A == '0' || A == '1' || A == '2' || A == '3' || A == '4' || A == '5'
                || A == '6' || A == '7' || A == '8' || A == '9' || A == '\0')
            return false;
        return true;
    }
}