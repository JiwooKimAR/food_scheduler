package practice.example.com.food_scheduler;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AutoItemInfoActivity extends AppCompatActivity {
    int cancel_mode_set = 0, add_mode_set = 1, edit_mode_set = 2;

    TextView AutoDate;
    EditText editDateAuto, editWeightAuto, editNameAuto;
    ImageView productImage;
    static final int REQUEST_TAKE_PHOTO = 3;
    static final int REQUEST_GALLERY = 4;
    static final int REQUEST_AUTODATE = 5;
    private static final int MAX_DIMENSION = 600;
    String mCurrentPhotoPath;
    Uri photoURI;
    byte[] byteArr;

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

        editNameAuto = findViewById(R.id.edit_itemName);
        editDateAuto = findViewById(R.id.edit_itemDate);
        editWeightAuto = findViewById(R.id.edit_itemAmount);
        productImage = findViewById(R.id.img_item);
        Button OK = findViewById(R.id.btn_itemInfo_ok);
        Button No = findViewById(R.id.btn_itemInfo_cancel);
        AutoDate = findViewById(R.id.Auto_Date_Click);

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
                        BitmapDrawable EIA_BD = (BitmapDrawable)productImage.getDrawable();
                        if (EIA_BD == null) {
                            Toast.makeText(AutoItemInfoActivity.this, "사진을 넣어주세요", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        Bitmap EIA_B = EIA_BD.getBitmap();
                        byteArr = bitmapToByteArray(EIA_B);
                        byteArr = bitmapToByteArray(EIA_B);
                        if (byteArr.length > 400000) {
                            Toast.makeText(AutoItemInfoActivity.this, "사진의 크기가 너무 큽니다.\n다른 사진을 골라주세요.", Toast.LENGTH_SHORT).show();
                            break;
                        }

                        Intent intent = new Intent();
                        intent.putExtra("ADDINFO", new RefrigeratorItem(ENA, EWA, date, byteArr));
                        setResult(add_mode_set, intent);
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
                                        dispatchTakePictureIntent();
                                        //카메라 연동
                                        break;
                                    case R.id.optionMeue_setImgWithGalary:
                                        openGallery();
                                        //갤러리 연동
                                        break;
                                    case R.id.optionMenu_rotate:
                                        ImageView img = findViewById(R.id.img_item);
                                        BitmapDrawable bit_d = (BitmapDrawable) img.getDrawable();
                                        Bitmap bit = bit_d.getBitmap();
                                        rotate(bit, 90);
                                        break;
                                }
                                return false;
                            }
                        });
                        popup.show();
                        break;
                    case R.id.Auto_Date_Click:
                        Intent intent_auto = new Intent(AutoItemInfoActivity.this, AutoDateActivity.class);
                        startActivityForResult(intent_auto, REQUEST_AUTODATE);
                        break;
                }
            }
        };

        OK.setOnClickListener(listener);
        No.setOnClickListener(listener);
        productImage.setOnClickListener(listener);
        AutoDate.setOnClickListener(listener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {

            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
            try {
                bitmap = scaleBitmapDown(MediaStore.Images.Media.getBitmap(getContentResolver(), photoURI), MAX_DIMENSION);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ExifInterface exif = null;
            try {
                exif = new ExifInterface(mCurrentPhotoPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int exifOrientation;
            int exifDegree;
            if (exif != null) {
                exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                exifDegree = exifOrientationToDegrees(exifOrientation);
            } else {
                exifDegree = 0;
            }
            bitmap = rotate(bitmap, exifDegree);
            productImage.setImageBitmap(bitmap);
        }
        if (requestCode == REQUEST_GALLERY && resultCode == RESULT_OK) {
            Toast.makeText(this, "사진 선택!", Toast.LENGTH_LONG).show();
            photoURI = data.getData();
            productImage.setImageURI(photoURI);
        }
        if (requestCode == REQUEST_AUTODATE && resultCode == RESULT_OK) {
            editDateAuto.setText(data.getStringExtra("AUTO_DATE"));
        }
    }

    // 카메라+사진 관련
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this, "com.food_scheduler.provider", photoFile);
                //photoURI = FileProvider.getUriForFile(this, getPackageName(), photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    private Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {

        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }
    // 카메라+사진 관련 끝

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
                productImage.setImageBitmap(img);
            }
            else {
                // 사진 없음
            }
            if (Name != null) {
                editNameAuto.setText(Name); // 이름이 edittext보다 길 때 처리하기
            }
            else {
                // 이름 없음
            }
            if (Weight != null) {
                editWeightAuto.setText(Weight);
            }
            else {
                // 중량 없음
            }
        }
    }

    public static boolean isNumber(char A) {
        if (A == '0' || A == '1' || A == '2' || A == '3' || A == '4' || A == '5'
                || A == '6' || A == '7' || A == '8' || A == '9' || A == '\0')
            return false;
        return true;
    }

    public byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
}