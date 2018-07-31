package com.yoshihiko.steadyviewer;

import android.content.Context;
import android.content.res.Resources;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;


class ContentManager {
    private Content ContentView;
    private InterfaceManager activity;
    private InclinationManager mInclinationManager;
    private boolean ImageChanging=true;

    ContentManager(final InterfaceManager activity){
        this.activity=activity;
        ContentView=(Content)activity.mContentView;

        new Thread(){
            @Override
            public void run(){
                mInclinationManager=new InclinationManager((SensorManager) activity.getSystemService(Context.SENSOR_SERVICE),ContentView);
                ContentView.setOnTouchListener(new TouchManager(ContentView,activity));
            }
        }.start();
    }

    public boolean setImage(Bitmap image){
        if(image==null){
            return false;
        }else {
            ContentView.setImage(image);
            return true;
        }
    }

    public void pause(){
        if(mInclinationManager!=null)mInclinationManager.pause();
    }

    public void resume(){
        if(ImageChanging)return;
        if(mInclinationManager!=null)mInclinationManager.resume();
    }

    public void StartImageChange(){
        ImageChanging=true;
        this.pause();
    }

    public void EndImageChange(){
        ImageChanging=false;
        this.resume();
    }
}
