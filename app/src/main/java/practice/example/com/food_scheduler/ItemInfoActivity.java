package practice.example.com.food_scheduler;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/*
 */
public class ItemInfoActivity extends AppCompatActivity {
    int cancel_mode_set = 0, add_mode_set = 1, edit_mode_set = 2;
    ImageView img_item;
    EditText edit_itemName, edit_itemAmount, edit_itemDate;
    Button btn_ok, btn_cancel;

    //인덴트 불로오기
    Intent intent;
    Intent intent_ItemInfoToRefri;
    boolean edit_mode;

    TextView AutoDate;
    byte[] byteArr;
    static final int REQUEST_TAKE_PHOTO = 3;
    static final int REQUEST_GALLERY = 4;
    static final int REQUEST_AUTODATE = 5;
    private static final int MAX_DIMENSION = 600;
    String mCurrentPhotoPath;
    Uri photoURI;

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
        AutoDate = findViewById(R.id.Auto_Date_Click);

        //인덴트 설정
        intent_ItemInfoToRefri = new Intent();
        intent = getIntent();
        edit_mode = intent.getBooleanExtra("EDITMODE",false);

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
            } else { // 나중에도 이미지가 없을 수 있을까?
                //picasso이용하여 해당 url의 그림 불러오기
                Picasso.get().load("http://i.imgur.com/DvpvklR.png"/**/).placeholder(R.drawable.ic_type_image).into(img_item);
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
                        if (bit_D == null) {
                            Toast.makeText(ItemInfoActivity.this, "사진을 넣어주세요", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        Bitmap bit = bit_D.getBitmap();
                        byteArr = bitmapToByteArray(bit);
                        if (byteArr.length > 400000) {
                            Toast.makeText(ItemInfoActivity.this, "사진의 크기가 너무 큽니다.\n다른 사진을 골라주세요.", Toast.LENGTH_SHORT).show();
                            break;
                        }
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
                        Intent intent_auto = new Intent(ItemInfoActivity.this, AutoDateActivity.class);
                        startActivityForResult(intent_auto, REQUEST_AUTODATE);
                        break;
                }
            }
        };
        btn_ok.setOnClickListener(listener);
        btn_cancel.setOnClickListener(listener);
        img_item.setOnClickListener(listener);
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
            img_item.setImageBitmap(bitmap);
        }
        if (requestCode == REQUEST_GALLERY && resultCode == RESULT_OK) {
            Toast.makeText(this, "사진 선택!", Toast.LENGTH_LONG).show();
            photoURI = data.getData();
            img_item.setImageURI(photoURI);
        }
        if (requestCode == REQUEST_AUTODATE && resultCode == RESULT_OK) {
            edit_itemDate.setText(data.getStringExtra("AUTO_DATE"));
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
