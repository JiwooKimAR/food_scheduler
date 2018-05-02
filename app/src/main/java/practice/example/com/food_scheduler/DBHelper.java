package practice.example.com.food_scheduler;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION=7;

    public DBHelper(Context context){
        super(context, "datadb", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //SimpleAdapter, CusrsorAdapter Test.........
        String driverTable="create table tb_drive ("+
                "_id integer primary key autoincrement," +
                "name," +
                "date," +
                "amount," +
                "img)";
        db.execSQL(driverTable);
        db.execSQL("insert into tb_drive (name, date, amount, img) values ('계란','18.04.25', '30개','N')");
        db.execSQL("insert into tb_drive (name, date, amount, img) values ('빼빼로','18.08.04', '1통','Y')");
        db.execSQL("insert into tb_drive (name, date, amount, img) values ('우유','18.03.01', '1통','N')");
        db.execSQL("insert into tb_drive (name, date, amount, img) values ('생수','19.12.30', '2L','N')");
        db.execSQL("insert into tb_drive (name, date, amount, img) values ('라면','18.08.26', '3개','Y')");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion == DATABASE_VERSION) {
            Log.v("DB", "성공");
            db.execSQL("drop table tb_drive");
            onCreate(db);

        }
    }
}
