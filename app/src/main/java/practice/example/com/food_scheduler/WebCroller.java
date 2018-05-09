package practice.example.com.food_scheduler;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by 류성희 on 2018-05-09.
 */
/*
public class WebCroller extends AppCompatActivity {
    int cnt = 0;

    private WebView wv1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wv1 = (WebView)findViewById(R.id.webView);
        wv1.setWebChromeClient(new WebChromeClient());

        Button htmlTitleButton = (Button)findViewById(R.id.CmdV);
        Log.d("test","p0");
        htmlTitleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuffer strB = new StringBuffer();
                WebCroll getW = new WebCroll();
                Log.d("test","p1");
                getW.execute();
                Log.d("test","p4");
            }
        });

    }
*/
    public class WebCroll extends AsyncTask<Void, Void, Void> {

        private String htmlPageUrl = "http://terms.naver.com/entry.nhn?docId=1988371&cid=48164&categoryId="; //파싱할 홈페이지의 URL주소
        // private TextView textviewHtmlDocument;
        private String htmlContentInStringFormat="";
        private String urlNum = "";
        private WebView wv1;

        public void SeturlNum(String num){
            urlNum = num;
        }

        public void SetWebView(WebView wv){
            wv1 = wv;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();Log.d("test","p3");
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                Document doc = Jsoup.connect(htmlPageUrl + urlNum).get();

                Elements titles= doc.select("div.size_ct_v2");
                System.out.println("-------------------------------------------------------------");
                for(Element e: titles){
                    System.out.println("title: " + e.text());

                    htmlContentInStringFormat += e.html().trim() + "\n";
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //WebView wv1 = (WebView)findViewbyId(R.id.webView);

            Log.d("test","pp3");
            wv1.loadData(htmlContentInStringFormat.trim(), "text/html; charset=utf-8",null);
            System.out.println(htmlContentInStringFormat);
        }
    }
}
