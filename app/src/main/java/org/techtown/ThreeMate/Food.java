package org.techtown.ThreeMate;

public class Food {
    String name;
    String url;
    String info;

    public Food(String name, String url, String info){
        this.name = name;
        this.url = url;
        this.info = info;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getInfo() {
        return info;
    }
}
