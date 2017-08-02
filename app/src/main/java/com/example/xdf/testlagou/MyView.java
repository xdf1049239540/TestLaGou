package com.example.xdf.testlagou;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;


/**
 * Created by xdf on 2017/8/2.
 */

public class MyView extends View {
    private Path path;//第一条曲线
    private Path path2;//第二条曲线
    private Paint paint;//第一条曲线 画笔
    private Paint paint2;//第二条曲线 画笔
    private int poHeight=20;//波高
    private int move_height=500;//移动的高度（距离顶点）
    private int interval=120;//两条曲线的间隔(视差效果)
    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        path=new Path();
        path2=new Path();

        paint=new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#00B38A"));
        paint.setStyle(Paint.Style.FILL);

        paint2 =new Paint();
        paint2.setAntiAlias(true);
        paint2.setColor(Color.parseColor("#9BF3CE"));
        paint2.setStyle(Paint.Style.FILL);

    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
   int width;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width=getWidth();
        startAnimator();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int half= getWidth()/2;

        int saveLayer2 = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        path2.reset();
        path2.moveTo(-2*width+dx+interval,move_height);
        for (int i = 0; i <3 ; i++) {
            path2.rQuadTo(half/2,-poHeight,half,0);
            path2.rQuadTo(half/2,poHeight,half,0);
            canvas.drawPath(path2, paint2);
        }
        paint2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));
        canvas.drawRect(0,0,getWidth(),move_height, paint2);
        paint2.setXfermode(null);
        canvas.restoreToCount(saveLayer2);


        int saveLayer = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        path.reset();
        path.moveTo(-width+dx,move_height);
        for (int i = 0; i <3 ; i++) {
            path.rQuadTo(half/2,-poHeight,half,0);
            path.rQuadTo(half/2,poHeight,half,0);
            canvas.drawPath(path,paint);
        }
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));
        canvas.drawRect(0,0,getWidth(),move_height,paint);
        paint.setXfermode(null);
        canvas.restoreToCount(saveLayer);

    }
    //控制点往右移动
    int dx;
    private void startAnimator(){
        ValueAnimator va=ValueAnimator.ofInt(0,width);
        va.setRepeatCount(ValueAnimator.INFINITE);
        va.setInterpolator(new LinearInterpolator());

        va.setDuration(5000);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                dx= (int) animation.getAnimatedValue();
                Log.i("dx",""+dx);
                invalidate();
            }
        });
        va.start();
    }
}
