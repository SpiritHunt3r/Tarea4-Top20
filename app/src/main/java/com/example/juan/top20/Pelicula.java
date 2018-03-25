package com.example.juan.top20;

import android.graphics.Bitmap;

/**
 * Created by juan_ on 3/24/2018.
 */

public class Pelicula {
    private Bitmap img;
    private String inf;


    public Pelicula(){
    }

    public Pelicula(String inf, Bitmap img){

        this.inf = inf;
        this.img = img;

    }


    public void setInf(String inf) {
        this.inf = inf;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    public Bitmap getImg() {
        return img;
    }

    public String getInf() {
        return inf;
    }




}

