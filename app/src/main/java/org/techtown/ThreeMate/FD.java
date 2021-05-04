package org.techtown.ThreeMate;

public class FD {
    public String icon;
    public String name;
    public String kcal;
    public String carbs;
    public String protein;
    public String fat;

    public FD(String icon, String name, String kcal, String carbs, String protein, String fat){
        this.icon = icon;
        this.name = name;
        this.kcal = kcal;
        this.carbs=carbs;
        this.protein = protein;
        this.fat    = fat;
    }
    public String getIcon() {
        return icon;
    }
    public String getName(){
        return name;
    }
    public String getKcal(){
        return kcal;
    }
    public String getCarbs(){
        return carbs;
    }
    public String getProtein(){
        return protein;
    }
    public String getFat(){
        return fat;
    }


    public void setIcon(String icon){
        this.icon = icon;
    }
    public void setName(String name){
        this.name=name;
    }
    public void setKcal(String kcal){
        this.kcal=kcal;
    }
    public void setCarbs(String carbs){
        this.carbs=carbs;
    }
    public void setProtein(String protein){
        this.protein=protein;
    }
    public void setFat(String fat){
        this.fat=fat;
    }


}
