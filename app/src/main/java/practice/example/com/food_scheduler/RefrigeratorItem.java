package practice.example.com.food_scheduler;

import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by user on 2018-05-06.
 */

public class RefrigeratorItem implements Serializable{
    private String name;
    private int value;
    private String amount;
    private Calendar date;
    private Drawable img;
    private boolean checked;
    private boolean showCheckBox;

    public RefrigeratorItem(){}

    public RefrigeratorItem(String name, int value, String amount, Calendar date, Drawable img){
        this.name =  name;
        this.value = value;
        this.amount = amount;
        this.date = date;
        this.img = img;
        checked = false;
        showCheckBox = false;
    }

    public String getName(){ return name;}
    public int getValue(){ return value;}
    public String getAmount(){ return amount;}
    public Calendar getDate(){ return date;}
    public Drawable getImg(){ return img; }
    public boolean getChecked(){ return checked; }
    public boolean getShowCheckBox() { return  showCheckBox; }

    public void setName(String name){ this.name = name; }
    public void setValue(int value){ this.value = value; }
    public void setAmount(String amount){ this.amount = amount; }
    public void setDate(Calendar date){ this.date = date; }
    public void setImg(Drawable img){ this.img = img; }
    public void setChecked(Boolean checked){ this.checked = checked; }
    public void setShowCheckBox(Boolean showCheckBox){ this.showCheckBox = showCheckBox; }
}
