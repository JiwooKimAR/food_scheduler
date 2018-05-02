package practice.example.com.food_scheduler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {
    Button btn_refrigerator, btn_calendar, btn_help;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btn_refrigerator = findViewById(R.id.btn_refrigerator);
        btn_calendar = findViewById(R.id.btn_calendar);
        btn_help = findViewById(R.id.btn_help);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btn_refrigerator:
                        //냉장고로 넘어가는 인덴트
                        Toast.makeText(getApplicationContext(),"냉장고로",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.btn_calendar:
                        //캘린더로 넘어가는 인덴트
                        Toast.makeText(getApplicationContext(),"캘린더로",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.btn_help:
                        //도움말 관련 인덴트
                        Toast.makeText(getApplicationContext(),"도움말로",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        btn_refrigerator.setOnClickListener(listener);
        btn_calendar.setOnClickListener(listener);
        btn_help.setOnClickListener(listener);
    }
}
