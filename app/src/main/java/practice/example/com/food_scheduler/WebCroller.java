package practice.example.com.food_scheduler;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by 류성희 on 2018-05-09.
 *
 */

// NOTICE : 전혀 이상한 음시과 매칭;;
public class WebCroller extends AppCompatActivity {

    int cnt = 0;

    private WebView wv1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_web_activity);

        wv1 = (WebView)findViewById(R.id.recipe_webView);
        wv1.setWebChromeClient(new WebChromeClient());

        Intent intent = getIntent();
        String FoodNum = intent.getStringExtra("FOODNAME_KEY");
        System.out.println(FoodNum + "-2");

        StringBuffer strB = new StringBuffer();
        WebCroll getW = new WebCroll(wv1, FoodNum);
        getW.execute();
    }
}

class WebCroll extends AsyncTask<Void, Void, Void> {

    private String htmlPageUrl = "http://terms.naver.com/entry.nhn?docId="; //파싱할 홈페이지의 URL주소
    // private TextView textviewHtmlDocument;
    private String htmlContentInStringFormat = "";
    private String urlNum = "";
    private WebView wv1;
    WebCroll(WebView wv, String foodNum) {
        urlNum = foodNum;
        wv1 = wv;
        System.out.println(foodNum + "-3");
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {

            Document doc = Jsoup.connect(htmlPageUrl + urlNum + "&cid=48164&categoryId=48208").get();

            Elements titles = doc.select("div.size_ct_v2");
            System.out.println("-------------------------------------------------------------");
            for (Element e : titles) {
                htmlContentInStringFormat += e.html().trim() + "\n";
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        if(htmlContentInStringFormat.trim().contains("enlarge_button")) htmlContentInStringFormat = htmlContentInStringFormat.split("이미지 크게보기</a>")[1];
        wv1.loadData(htmlContentInStringFormat.trim(), "text/html; charset=utf-8", null);
        //System.out.println("onPostExecute--" + htmlContentInStringFormat.trim());
    }
}