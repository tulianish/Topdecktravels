package com.dal.touristapp;

import android.graphics.Bitmap;

public class Card {
    private String placeImg;
    private String placeText;

    public Card(String img, String text){
        placeImg = img;
        placeText = text;
    }

    public String getImg(){
        return placeImg;
    }

    public String getText(){
        return placeText;
    }
}
