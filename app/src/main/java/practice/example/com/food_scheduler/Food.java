package practice.example.com.food_scheduler;

import java.util.ArrayList;

import practice.example.com.food_scheduler.AbleFoodItem;

public class Food {
    public Food(String FoodString) {
        try {
            String splitData[] = FoodString.split("::");
            Id = splitData[0];
            Name = splitData[1];
            Item = splitData[2];

            String a = Item;
            ArrayList<String> list = new ArrayList<>();
            for (int i = 0, j = 0; i < a.length(); i++, j++) {
                if (i + 2 <= a.length() && a.substring(i, i + 2).equals("||")) {
                    if (i - j == 0) list.add(a.substring(i - j, i));
                    else list.add(a.substring((i - j) + 2, i));
                    j = 0;
                }
            }
            if (a.lastIndexOf("||") < 0) list.add(a);
            else list.add(a.substring(a.lastIndexOf("||") + 2));

            Items = list;

            String[] temp = list.toArray(new String[list.size()]);

            IngredientItem = temp[0];
            for (int i = 1; i < list.size(); i++)
                IngredientItem += ", " + temp[i];

            //split가 말을 안들음
            //food.Items = splitData[2].split("||");
        } catch (Exception ex) {
        }
    }

    public Food(String Id, String Name) {
        this.Id = Id;
        this.Name = Name;
    }

    public Food(String Id, String Name, String... Items) {
        this.Id = Id;
        this.Name = Name;
        this.Items = new ArrayList<>(Items.length);
        for (int i = 0; i < Items.length; i++)
            this.Items.set(i, Items[i]);
    }

    public String Id;
    public String Name;
    public ArrayList<String> Items;
    public String Item;
    public String IngredientItem = null;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public ArrayList<String> getItems() {
        return Items;
    }

    public void setItems(ArrayList<String> items) {
        Items = items;
    }

    public AbleFoodItem getAbleFoodItem() {
        return new AbleFoodItem(Name, Id, IngredientItem);//변경
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(Id);
        sb.append("::").append(Name).append("::");
        if (Items == null ? false : Items.size() > 0) sb.append(Items.get(0));
        for (int i = 1; i < Items.size(); i++)
            sb.append("||").append(Items.get(i));
        return sb.toString();
    }
}
