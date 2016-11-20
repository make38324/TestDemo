package com.example.mac.testdemo1.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by czm on 16/9/18.
 */
public class CircleSeekBar extends View {
    private Paint circlePaint;
    private float circleWidth=3;
    private float cx;
    private float cy;
    private float radius;
    private Paint smallCirclePaint;
    private float smallCirclewidth=1;
    private float smallradius=15;
    private float mCurrentRadian=0;
    private float preradian;
    private int circleColor=Color.BLUE;
    private int smallcircleColor=Color.RED;
    private Paint textPaint;
    private int TextColor=Color.BLACK;
    private float textSize=38;
    private boolean mInCirclebutton;
    private LRChangListener lrChangListener;

    public void setLrChangListener(LRChangListener lrChangListener){
        this.lrChangListener=lrChangListener;
    }

    public CircleSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        circlePaint=new Paint();
        circlePaint.setColor(circleColor);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(circleWidth);
        smallCirclePaint=new Paint();
        smallCirclePaint.setColor(smallcircleColor);
        smallCirclePaint.setStrokeWidth(smallCirclewidth);
        textPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(textSize);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(TextColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        this.cx = width / 2;
        this.cy = height / 2;
        this.radius=width/2-circleWidth/2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(cx,cy,radius,circlePaint);
//        canvas.drawText("L",getFontWidth("L",textPaint)/2+smallradius*2,cy+getFontHeight("L",textPaint)/2,textPaint);
//        canvas.drawText("R",getMeasuredWidth()-getFontWidth("R",textPaint)/2-smallradius*2,cy+getFontHeight("R",textPaint)/2,textPaint);
        canvas.rotate((float) Math.toDegrees(mCurrentRadian),cx,cy);
        canvas.drawCircle(cx,getMeasuredHeight()/2-radius+circleWidth/2+smallCirclewidth/2+20,smallradius,smallCirclePaint);
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                float x = event.getX();
                float y = event.getY();
//                if(mInCircleButton(x,y)){
//                    mInCirclebutton =true;
                    preradian = getRadian(x, y);
//                }
                break;
            case MotionEvent.ACTION_MOVE:
//                if(mInCirclebutton) {
                float temp = getRadian(event.getX(), event.getY());
                mCurrentRadian += (temp - preradian);
                preradian = temp;
                if (mCurrentRadian > 2 * Math.PI)
                {
                    mCurrentRadian = (float) (2 * Math.PI);
                }
                else if (mCurrentRadian < 0)
                {
                    mCurrentRadian = 0;
                }
                if(lrChangListener!=null){
                    lrChangListener.onLRChange(Math.toDegrees(mCurrentRadian));
                }
                invalidate();
//                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    private float getRadian(float x, float y)
    {
        float alpha = (float) Math.atan((x - cx) / (cy - y));
        if (x > cx && y > cy)
        {
            alpha += Math.PI;
        }
        else if (x < cx && y > cy)
        {
            alpha += Math.PI;
        }
        else if (x < cx && y < cy)
        {
            alpha = (float) (2 * Math.PI + alpha);
        }
        return alpha;
    }
    private float getFontHeight(String text,Paint paint)
    {
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, 1, rect);
        return rect.height();
    }
    private float getFontWidth(String text,Paint paint)
    {
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, 1, rect);
        return rect.width();
    }
    private boolean mInCircleButton(float x, float y)
    {
        float r = radius-circleWidth-smallCirclewidth/2-10;
        float x2 = (float) (cx + r * Math.sin(mCurrentRadian));
        float y2 = (float) (cy - r * Math.cos(mCurrentRadian));
        if (Math.sqrt((x - x2) * (x - x2) + (y - y2) * (y - y2)) < 2*smallradius)
        {
            return true;
        }
        return false;
    }
    public static interface LRChangListener{
        void onLRChange(double currentDeg);
    }
}
