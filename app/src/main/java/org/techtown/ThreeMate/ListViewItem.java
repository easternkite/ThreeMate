package org.techtown.ThreeMate;

import android.graphics.drawable.Drawable;

public class ListViewItem {
    private String  iconDrawable ;
    private String titleStr ;
    private String descStr ;

    public ListViewItem(String s, String s1, String s2) {
        this.iconDrawable = s;
        this.titleStr = s1;
        this.descStr = s2;
    }

    public void setIcon(String  icon) {
        iconDrawable = icon ;
    }
    public void setTitle(String title) {
        titleStr = title ;
    }
    public void setDesc(String desc) {
        descStr = desc ;
    }

    public String getIcon() {
        return this.iconDrawable ;
    }
    public String getTitle() {
        return this.titleStr ;
    }
    public String getDesc() {
        return this.descStr ;
    }
}