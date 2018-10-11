package com.matt2393.chivato;

import android.app.Application;

import com.matt2393.chivato.Tools.FirebaseInit;

public class IND_App extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseInit.init();
    }
}
