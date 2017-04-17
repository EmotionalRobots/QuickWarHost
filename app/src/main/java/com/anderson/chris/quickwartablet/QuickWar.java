package com.anderson.chris.quickwartablet;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by christopheranderson on 3/31/17.
 */

public class QuickWar extends Application {

    @Override
    public void onCreate(){
        super.onCreate();

        Firebase.setAndroidContext(this);



    }

}
