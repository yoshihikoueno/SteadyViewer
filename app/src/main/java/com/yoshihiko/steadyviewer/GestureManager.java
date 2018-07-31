package com.yoshihiko.steadyviewer;

import android.graphics.Point;
import android.support.v4.view.MotionEventCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;


public class GestureManager{
    private Content mContent;
    float begin,current,rate;

    public GestureManager(Content content) {
        mContent=content;
    }

    public boolean onTouch(View v, MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);

        switch (action){
            case MotionEvent.ACTION_POINTER_DOWN:
                rate=mContent.getZoom();
                begin=distance(new Point((int)event.getX(0),(int)event.getY(0)),new Point((int)event.getX(1),(int)event.getY(1)));
                return true;

            case MotionEvent.ACTION_MOVE:
                current=distance(new Point((int)event.getX(0),(int)event.getY(0)),new Point((int)event.getX(1),(int)event.getY(1)));
                mContent.setZoom(rate*current/begin);
                return true;
        }

        return false;
    }

    private float distance(Point p1,Point p2){
        return (float) Math.sqrt(((double)(p1.x-p2.x))*((double)(p1.x-p2.x))+((double)(p1.y-p2.y))*((double)(p1.y-p2.y)));
    }
}
