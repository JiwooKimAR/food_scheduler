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

    /* NOTIICE : 한번 테스트해볼라고 만든거야 이 주석 아래 있는거 지워도 됩니다!*/
    static public ArrayList<AbleFoodItem> testGetAbleFood(Context context, Intent intent){
        //재료 리스트가 담긴 인덴트 받아오고 꺼내기
        ArrayList<RefrigeratorItem> ingredients = (ArrayList<RefrigeratorItem>)intent.getSerializableExtra("INGREDIENTS");

        //리턴할 새로운 어레이 리스트 만들기
        ArrayList<AbleFoodItem> ableFoodItems = new ArrayList<>();

        //임시 데이터 ableFoodItems에 추가하기
        //김치찜, 숫자, 재료리스트 아무거나
        ableFoodItems.add(new AbleFoodItem("김치찜","48208",ingredients.get(0).getName()));
        ableFoodItems.add(new AbleFoodItem("취나물무침","48205",ingredients.get(1).getName()));

        return ableFoodItems;
    }
}
