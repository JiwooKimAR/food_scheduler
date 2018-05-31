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
import java.util.HashMap;

public class FoodMaker {
    public static String path = "/files/foodlists.txt";

    ArrayList<Food> foods = null;

    public FoodMaker(Context context)
    {
        foods = getFoodList(context);
    }

    public ArrayList<AbleFoodItem> Find(Intent intent)
    {
        ArrayList<RefrigeratorItem> ingredients = (ArrayList<RefrigeratorItem>) intent.getSerializableExtra("INGREDIENTS");

        String[] Item = new String[ingredients.size()];

        for(int i = 0; i < ingredients.size(); i++)
            Item[i] = ingredients.get(i).getName();

        HashMap<String, Food> hash = new HashMap<>();
        ArrayList<AbleFoodItem> items = new ArrayList<>();
        for(int i = 0; i < foods.size(); i++)
        {
            boolean Flag = true;
            Food a = foods.get(i);

            boolean mainIngre = false;
            String []temp = a.getItems().toArray(new String[a.getItems().size()]);
            for (int j = 0; j < Item.length; j++)
                if (temp[0].equals(Item[j]))
                    mainIngre = true;
            if (!mainIngre)
                continue;
            for(int j = 0; j < Item.length; j++)
                Flag &= a.Item.indexOf(Item[j]) > -1;
            if(Flag &&!hash.containsKey(a.Id))
            {
                hash.put(a.getId(), a);
                items.add(a.getAbleFoodItem());
            }
        }
        return items;
    }


    public static ArrayList<Food> getFoodList(Context context)
    {
        FileReader fr = null;
        BufferedReader br = null;
        String data = null;
        ArrayList<Food> food = new ArrayList<>();
        try
        {
            File file = new File(path);
            // read file.
            //StringBuilder sb = new StringBuilder();

            if(file.exists())
            {
                // open file.
                fr = new FileReader(file);
                br = new BufferedReader(fr);

                while ((data = br.readLine()) != null)
                {
                    //1줄씩 읽어옴 (data 변수)
                    //sb.append(data).append("\n");
                    food.add(new Food(data));
                }
                //return sb.toString();
            }
            else
            {
                InputStream inputStream =  context.getResources().openRawResource(R.raw.foodlists);
                InputStreamReader reader = new InputStreamReader(inputStream);
                br = new BufferedReader(reader);

                while ((data = br.readLine()) != null)
                {
                    //1줄씩 읽어옴 (data 변수)
                    //sb.append(data).append("\n");
                    food.add(new Food(data));
                }

                reader.close();
                inputStream.close();

                //String data1 = sb.toString();
                //setFoodList(data1);
                return food;
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
