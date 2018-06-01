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
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequest;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.api.services.vision.v1.model.TextAnnotation;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AutoDateActivity extends AppCompatActivity{
    static TextView textView2;
    ImageView mImageView;
    static String Auto_Date;

    private static final String CLOUD_VISION_API_KEY = "AIzaSyAdbT97Iyqn-JBdxtCKUr13ohvdEoKnehw";
    private static final String ANDROID_CERT_HEADER = "X-Android-Cert";
    private static final String ANDROID_PACKAGE_HEADER = "X-Android-Package";
    private static final int MAX_LABEL_RESULTS = 10;

    static final int REQUEST_TAKE_PHOTO = 3;
    static final int REQUEST_GALLERY = 4;
    static final int REQUEST_AUTODATE = 5;
    private static final int MAX_DIMENSION = 800;
    String mCurrentPhotoPath;
    Uri photoURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_date);

        mImageView = findViewById(R.id.Auto_Date_View);
        textView2 = findViewById(R.id.Auto_Date_Text);
    }

    public void onButtonClick(View view) {
        switch(view.getId()) {
            case R.id.Auto_Date_OK:
                Intent intent_date = new Intent();
                intent_date.putExtra("AUTO_DATE", Auto_Date);
                setResult(RESULT_OK, intent_date);
                finish();
                break;
            case R.id.Auto_Date_NO:
                finish();
                break;
            case R.id.Auto_Date_View:
                PopupMenu popup = new PopupMenu(getApplicationContext(), view);

                getMenuInflater().inflate(R.menu.auto_set_img_option_menu, popup.getMenu());
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
                            case R.id.optionMenu_auto:
                                BitmapDrawable bit_D = (BitmapDrawable) mImageView.getDrawable();
                                Bitmap bitmap = bit_D.getBitmap();
                                if (bitmap != null) {
                                    callCloudVision(bitmap);
                                }
                                else {
                                    Toast.makeText(AutoDateActivity.this, "사진을 선택해주세요", Toast.LENGTH_SHORT).show();
                                }
                        }
                        return false;
                    }
                });
                popup.show();
                break;
        }
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
            mImageView.setImageBitmap(bitmap);
        }
        if (requestCode == REQUEST_GALLERY && resultCode == RESULT_OK) {
            Toast.makeText(this, "사진 선택!", Toast.LENGTH_LONG).show();
            photoURI = data.getData();
            mImageView.setImageURI(photoURI);
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

    // 클라우드 비전 API 관련
    private Vision.Images.Annotate prepareAnnotationRequest(final Bitmap bitmap) throws IOException {
        HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

        VisionRequestInitializer requestInitializer =
                new VisionRequestInitializer(CLOUD_VISION_API_KEY) {
                    /**
                     * We override this so we can inject important identifying fields into the HTTP
                     * headers. This enables use of a restricted cloud platform API key.
                     */
                    @Override
                    protected void initializeVisionRequest(VisionRequest<?> visionRequest)
                            throws IOException {
                        super.initializeVisionRequest(visionRequest);

                        String packageName = getPackageName();
                        visionRequest.getRequestHeaders().set(ANDROID_PACKAGE_HEADER, packageName);

                        String sig = PackageManagerUtils.getSignature(getPackageManager(), packageName);

                        visionRequest.getRequestHeaders().set(ANDROID_CERT_HEADER, sig);
                    }
                };

        Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, null);
        builder.setVisionRequestInitializer(requestInitializer);

        Vision vision = builder.build();

        BatchAnnotateImagesRequest batchAnnotateImagesRequest =
                new BatchAnnotateImagesRequest();
        batchAnnotateImagesRequest.setRequests(new ArrayList<AnnotateImageRequest>() {{
            AnnotateImageRequest annotateImageRequest = new AnnotateImageRequest();

            // Add the image
            Image base64EncodedImage = new Image();
            // Convert the bitmap to a JPEG
            // Just in case it's a format that Android understands but Cloud Vision
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();

            // Base64 encode the JPEG
            base64EncodedImage.encodeContent(imageBytes);
            annotateImageRequest.setImage(base64EncodedImage);

            // add the features we want
            annotateImageRequest.setFeatures(new ArrayList<Feature>() {{
                Feature textDetection = new Feature();
                textDetection.setType("TEXT_DETECTION");
                textDetection.setMaxResults(MAX_LABEL_RESULTS);
                add(textDetection);
            }});

            // Add the list of one thing to the request
            add(annotateImageRequest);
        }});

        Vision.Images.Annotate annotateRequest =
                vision.images().annotate(batchAnnotateImagesRequest);
        // Due to a bug: requests to Vision API containing large images fail when GZipped.
        annotateRequest.setDisableGZipContent(true);

        return annotateRequest;
    }

    private static class LableDetectionTask extends AsyncTask<Object, Void, String> {
        private final WeakReference<AutoDateActivity> mActivityWeakReference;
        private Vision.Images.Annotate mRequest;

        LableDetectionTask(AutoDateActivity activity, Vision.Images.Annotate annotate) {
            mActivityWeakReference = new WeakReference<>(activity);
            mRequest = annotate;
        }

        @Override
        protected String doInBackground(Object... params) {
            try {
                BatchAnnotateImagesResponse response = mRequest.execute();
                return convertResponseToString(response);

            } catch (GoogleJsonResponseException e) {
            } catch (IOException e) { }
            return "Cloud Vision API request failed. Check logs for details.";
        }

        protected void onPostExecute(String result) {
            AutoDateActivity activity = mActivityWeakReference.get();
            if (activity != null && !activity.isFinishing()) {
                Auto_Date = searchDate(result);
                System.out.println(Auto_Date);
            }
        }
    }

    private void callCloudVision(final Bitmap bitmap) {
        TextView loadingText = findViewById(R.id.Auto_Date_Text);
        loadingText.setText("텍스트 인식 중입니다...");

        // Do the real work in an async task, because we need to use the network anyway
        try {
            AsyncTask<Object, Void, String> labelDetectionTask = new LableDetectionTask(this, prepareAnnotationRequest(bitmap));
            labelDetectionTask.execute();
        } catch (IOException e) {
            Toast.makeText(AutoDateActivity.this, "인터넷 연결이 필요합니다", Toast.LENGTH_SHORT).show();
        }
    }

    private static String convertResponseToString(BatchAnnotateImagesResponse response) {
        StringBuilder message = new StringBuilder("유통기한:\n\n");

        final TextAnnotation text = response.getResponses().get(0).getFullTextAnnotation();
        if (text != null) {
            message.append(text.getText());
        }
        else {
            message.append("찾을 수 없습니다.");
        }
        return message.toString();
    }
    // 클라우드 비전 API 관련 끝

    // 유통기한 찾기 관련
    private static String searchDate(String result) {
        String total = result;

        String line = "";
        String date = "";
        int num = 0;
        for (int i = 0; i < total.length(); i++) {
            line += total.charAt(i);
            if (total.charAt(i) == '\n') {
                for (int j = 0; j < line.length(); j++) {
                    if (IsNum(line.charAt(j))) {
                        num++;
                    }
                }
                if (num > 5) {
                    date = line;
                }
                line = "";
                num = 0;
            }
        }

        int flag = 0, k =0;
        String year = "", month = "", day = "";
        while (!IsNum(date.charAt(k++)));
        k--;
        date = date.substring(k);

        for (int i = 0; i < date.length(); i++) {
            if (IsNum(date.charAt(i))) {
                switch(flag) {
                    case 0:
                        year += date.charAt(i);
                        break;
                    case 1:
                        month += date.charAt(i);
                        break;
                    case 2:
                        day += date.charAt(i);
                        break;
                }
            }
            else {
                flag++;
            }
        }
        if (year.length() != 4) {
            year = "20" + year;
        }
        if (month.length() != 2) {
            month = "0" + month;
        }
        if (day.length() != 2) {
            day = "0" + day;
        }

        String realDate = year + "-" + month + "-" + day;

        textView2.setText(realDate);
        return realDate;
    }

    static boolean IsNum(char ch) {
        if (ch == '1' || ch == '2' || ch == '3' || ch == '4' || ch == '5' || ch == '6' || ch == '7' || ch == '8' || ch == '9' || ch == '0')
            return true;
        else
            return false;
    }
    // 유통기한 찾기 관련 끝
}
