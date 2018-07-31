package com.yoshihiko.steadyviewer;

import android.graphics.Point;
import android.provider.Settings;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by yoshihiko on 9/3/17.
 */

public class TouchManager implements View.OnTouchListener {
    private Point begin,original;
    private Content mContent;
    private GestureManager mGestureManager;
    private InterfaceManager mInterfaceManager;
    private int id;

    public TouchManager(Content content,InterfaceManager interfaceManager) {
        mContent =content;
        mGestureManager=new GestureManager(mContent);
        mInterfaceManager=interfaceManager;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getPointerCount()==1)return SinglePoint(v,event);
        if(event.getPointerCount()==2){
            id=-1;
            return mGestureManager.onTouch(v,event);
        }

        return false;
    }

    private boolean SinglePoint(View v,MotionEvent event){
        int action= MotionEventCompat.getActionMasked(event);
        switch (action){
            case MotionEvent.ACTION_DOWN:
                original= mContent.getCenter();
                begin=new Point((int)event.getX(),(int)event.getY());
                id=event.getPointerId(0);
                return true;

            case MotionEvent.ACTION_MOVE:
                if(event.getPointerId(0)!=id){
                    id=-1;
                    return true;
                }
                mContent.SetCenter(new Point(original.x+(int)event.getX()-begin.x,original.y+(int)event.getY()-begin.y));
                return true;

            case MotionEvent.ACTION_UP:
                if(distance(begin,new Point((int)event.getX(),(int)event.getY()))<30){
                    mInterfaceManager.toggle();
                    mContent.SetCenter(original);
                    return true;
                }
        }

        return false;
    }

    private float distance(Point p1,Point p2){
        return (float) Math.sqrt(((double)(p1.x-p2.x))*((double)(p1.x-p2.x))+((double)(p1.y-p2.y))*((double)(p1.y-p2.y)));
    }
}
