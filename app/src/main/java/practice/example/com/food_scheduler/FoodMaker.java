package practice.example.com.food_scheduler;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FoodMaker
{
    public static String path = "/files/foodlists.txt";

    public FoodMaker(Context context)
    {

    }


    public static String getFoodList(Context context)
    {
        FileReader fr = null;
        BufferedReader br = null;
        String data = null;
        try
        {
            File file = new File(path);
            // read file.
            StringBuilder sb = new StringBuilder();

            if(file.exists())
            {
                // open file.
                fr = new FileReader(file);
                br = new BufferedReader(fr);

                while ((data = br.readLine()) != null)
                {
                    //1줄씩 읽어옴 (data 변수)
                    sb.append(data).append("\n");
                }
                return sb.toString();
            }
            else
            {
                InputStream inputStream =  context.getResources().openRawResource(R.raw.foodlists);
                InputStreamReader reader = new InputStreamReader(inputStream);
                br = new BufferedReader(reader);

                while ((data = br.readLine()) != null)
                {
                    //1줄씩 읽어옴 (data 변수)
                    sb.append(data).append("\n");
                    //String splitData[] = data.split("::");
                }

                reader.close();
                inputStream.close();

                String data1 = sb.toString();
                setFoodList(data1);
                return data1;
            }
        }
        catch (Exception ex) {Log.e("FOODLIST", "음식 목록을 가져오는중 오류가 발생했습니다.", ex);}
        finally
        {
            try{if(fr != null)fr.close();}catch (Exception ex){}
            try{if(br != null)br.close();}catch (Exception ex){}
        }
        return null;
    }

    public static void setFoodList(String text)
    {
        File file = new File(path);
        FileWriter fw = null;
        BufferedWriter bw = null;

        try
        {
            fw = new FileWriter(file);
            bw = new BufferedWriter(fw);
            bw.write(text);
            bw.flush();
        }
        catch (IOException e) {e.printStackTrace();}
        finally
        {
            try{bw.close();}catch (Exception ex){}
            try{fw.close();}catch (Exception ex){}
        }
    }
}
