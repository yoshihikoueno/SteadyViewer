package com.yoshihiko.steadyviewer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

public class Content extends SurfaceView implements SurfaceHolder.Callback {
    SurfaceHolder holder;
    public Bitmap image;
    private Bitmap DrawImage;
    private int DefaultDrawImageWidth,DefaultDrawImageHeight;
    private Point Center;
    private boolean busy=true;

    public Content(Context context) {
        super(context);
        SetupHolder();
    }

    public Content(Context context, AttributeSet attrs) {
        super(context, attrs);
        SetupHolder();
    }

    public Content(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        SetupHolder();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        DrawContent();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        DrawContent();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        DrawContent();

    }

    private void SetupHolder(){
        holder=getHolder();
        holder.addCallback(this);
    }

    public void Initialize(){
        if(Center==null)Center=new Point();
        Center.x=DrawImage.getWidth()/2;
        Center.y= DrawImage.getHeight()/2;

        DefaultDrawImageWidth=DrawImage.getWidth();
        DefaultDrawImageHeight= DrawImage.getHeight();
    }

    public boolean DrawContent(){
        busy=true;

        if(image!=null && DrawImage==null) {
            DrawImage = Bitmap.createScaledBitmap(
                    image, getScreenWidth(), image.getHeight() * getScreenWidth() / image.getWidth(), false);
            Initialize();
        }

        Canvas c=holder.lockCanvas();
        if(c==null) return false;
        if(image==null)c.drawColor(Color.BLUE);
        else c.drawBitmap(DrawImage,0,0,null);
        holder.unlockCanvasAndPost(c);

        busy=false;
        return true;
    }

    int getScreenWidth(){
        Point p = new Point();
        ((WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRealSize(p);
        return p.x;
    }

    int getScreenHeight(){
        Point p = new Point();
        ((WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRealSize(p);
        return p.y;
    }

    public boolean RotateContent(float rad){
        Canvas c = holder.lockCanvas();
        if (c == null || DrawImage == null) return false;

        c.drawColor(Color.BLACK);
        c.rotate((float) (rad * 180 / Math.PI), Center.x, Center.y);
        c.drawBitmap(DrawImage, Center.x - DrawImage.getWidth() / 2, Center.y - DrawImage.getHeight() / 2, null);
        holder.unlockCanvasAndPost(c);
        return true;
    }

    public boolean isReady(){
        if(holder != null && !holder.isCreating() && DrawImage!=null && !busy)return true;
        else return false;
    }

    public void SetCenter(Point input){
        Center=input;
    }

    public void setZoom(float rate){
        //Rejecting too small rate that causes IllegalArgumentException
        if(Math.min(DefaultDrawImageWidth*rate,DefaultDrawImageHeight*rate)<10)return;

        //Rejecting too large rate that causes Memory Overflow
        if(rate>2.5)return;

        DrawImage=Bitmap.createScaledBitmap(
                image,(int)(DefaultDrawImageWidth*rate),(int)(DefaultDrawImageHeight*rate),false);
        return;
    }

    public float getZoom(){
        return (float) DrawImage.getWidth()/DefaultDrawImageWidth;
    }

    public Point getCenter(){
        return Center;
    }

    public void setImage(Bitmap image){
        this.image=image;
        DrawImage=null;
        DrawContent();
    }
}
