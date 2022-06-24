package com.andreaziqing.signlanguagedetectionapp.HelperClasses.Adapters.HomeAdapter;

public class LessonsHelperClass {

    int image;
    String title, desc;

    public LessonsHelperClass(int image, String title, String desc) {
        this.image = image;
        this.title = title;
        this.desc = desc;
    }

    public int getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }
}
