package com.qihoo.bumppic.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hacker on 16/8/31.
 */
public class ImageEntity {
    String dir;
    String firstImagePath;
    String name;
    int count;
    boolean isSelected;
    public List<String> images = new ArrayList<String>();
    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getFirstImagePath() {
        return firstImagePath;
    }

    public void setFirstImagePath(String firstImagePath) {
        this.firstImagePath = firstImagePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
