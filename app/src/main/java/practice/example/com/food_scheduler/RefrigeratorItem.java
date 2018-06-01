package practice.example.com.food_scheduler;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by SeLee Chae
 * information about each refrigerator item handled by refrigerator adapter
 */

public class RefrigeratorItem implements Serializable{
    private String name;
    private String amount;
    private Calendar date;
    private byte[] byteArray;
    private boolean checked;
    private boolean showCheckBox;

    public RefrigeratorItem(){}

    public RefrigeratorItem(String name, String amount, Calendar date, byte[] byteArr){
        this.name =  name;
        this.amount = amount;
        this.date = date;
        this.byteArray = byteArr;
        checked = false;
        showCheckBox = false;
    }

    public String getName(){ return name;}
    public String getAmount(){ return amount;}
    public Calendar getDate(){ return date;}
    public byte[] getByteArray(){ return byteArray; }
    public boolean getChecked(){ return checked; }
    public boolean getShowCheckBox() { return  showCheckBox; }

    public void setName(String name){ this.name = name; }
    public void setAmount(String amount){ this.amount = amount; }
    public void setDate(Calendar date){ this.date = date; }
    public void setByteArray(byte[] byteArr){ this.byteArray = byteArr; }
    public void setChecked(Boolean checked){ this.checked = checked; }
    public void setShowCheckBox(Boolean showCheckBox){ this.showCheckBox = showCheckBox; }
}