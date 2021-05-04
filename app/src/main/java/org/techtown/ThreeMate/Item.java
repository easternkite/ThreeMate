package org.techtown.ThreeMate;

public class Item {
    public int icon;
    public String place_name;
    public String road_address_name;
    public String category_name;
    public String phone;

    public Item(int icon, String name, String category_name, String address, String phone){
        this.icon = icon;
        this.place_name = name;
        this.category_name=category_name;
        this.road_address_name = address;
        this.phone = phone;
    }
    public int getIcon(){
        return icon;
    }
    public String getPlace_name(){
        return place_name;
    }
    public String getCategory_name(){
        return category_name;
    }
    public String getRoad_address_name(){
        return road_address_name;
    }
    public String getPhone(){
        return phone;
    }
    public void  setIcon(int icon){
        this.icon = icon;
    }
    public void  setPlace_name(String place_name){
        this.place_name = place_name;
    }

    public void  setCategory_name(String category_name){
        this.category_name = category_name;
    }
    public void  setRoad_address_name(String road_address_name){
        this.road_address_name = road_address_name;
    }
    public void  setPhone(String phone){
        this.phone = phone;
    }
}