package com.bignerdranch.android.draganddraw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class BoxDrawingView extends View {
    public static final String TAG = "BoxDrawingView";

    private static final String PARENT_STATE_KEY = "parent_state";
    private static final String BOXEN_KEY = "boxen_key";


    private Box mCurrentBox;
    private List<Box> mBoxen = new ArrayList<>();
    private Paint mBoxPaint;
    private Paint mBackgroundPaint;

    public BoxDrawingView(Context context) {
        this(context, null);
    }

    public BoxDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mBoxPaint = new Paint();
        mBoxPaint.setColor(0x22ff0000);

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(0xfff8efe0);
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable parentState = super.onSaveInstanceState();
        Bundle bundle = new Bundle();
        bundle.putParcelable(PARENT_STATE_KEY, parentState);
        bundle.putParcelableArrayList(BOXEN_KEY, (ArrayList<? extends Parcelable>) mBoxen);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state != null && state instanceof Bundle) {
            super.onRestoreInstanceState(((Bundle) state).getBundle(PARENT_STATE_KEY));
            mBoxen = ((Bundle) state).getParcelableArrayList(BOXEN_KEY);
        } else {
            super.onRestoreInstanceState(state);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        PointF touchPoint = null;
        PointF touchPoint2 = null;
        for (int i = 0; i < event.getPointerCount(); i++) {
            if (event.getPointerId(i) == 0)
                touchPoint = new PointF(event.getX(i), event.getY(i));
            if (event.getPointerId(i) == 1)
                touchPoint2 = new PointF(event.getX(i), event.getY(i));
        }


        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mCurrentBox = new Box(touchPoint);
                mBoxen.add(mCurrentBox);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                mCurrentBox.setPointer(touchPoint2);
                break;
            case MotionEvent.ACTION_MOVE:
                if (touchPoint != null)
                    mCurrentBox.setCurrent(touchPoint);
                if (touchPoint2 != null) {
                    PointF boxOrigin = mCurrentBox.getOrigin();
                    PointF pointerOrigin = mCurrentBox.getPointer();
                    float angle2 = (float) Math.atan2(touchPoint2.y - boxOrigin.y, touchPoint2.x - boxOrigin.x);
                    float angle1 = (float) Math.atan2(pointerOrigin.y - boxOrigin.y, pointerOrigin.x - boxOrigin.x);
                    float calculatedAngle = (float) Math.toDegrees(angle2 - angle1);
                    if (calculatedAngle < 0) {
                        calculatedAngle += 360;
                    }
                    mCurrentBox.setAngle(calculatedAngle);
                    Log.d(TAG, "Set Box Angle " + calculatedAngle);
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "Finger UP Box Set");
                mCurrentBox = null;
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.d(TAG, "Action Cancel Box Set");
                mCurrentBox = null;
                break;
        }

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPaint(mBackgroundPaint);

        for (Box box : mBoxen) {
            float left = Math.min(box.getOrigin().x, box.getCurrent().x);
            float right = Math.max(box.getOrigin().x, box.getCurrent().x);
            float top = Math.min(box.getOrigin().y, box.getCurrent().y);
            float bottom = Math.max(box.getOrigin().y, box.getCurrent().y);

            float angle = box.getAngle();
            float px = (box.getOrigin().x + box.getCurrent().x) / 2;
            float py = (box.getOrigin().y + box.getCurrent().y) / 2;
            canvas.save();
            canvas.rotate(angle, px, py);
            canvas.drawRect(left, top, right, bottom, mBoxPaint);
            canvas.restore();
        }
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        PointF current = new PointF(event.getX(), event.getY());
//        String action = "";
//
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                action = "ACTION_DOWN";
//                mCurrentBox = new Box(current);
//                mBoxen.add(mCurrentBox);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                action = "ACTION_MOVE";
//                if(mCurrentBox != null) {
//                    mCurrentBox.setCurrent(current);
//                    invalidate();
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                action = "ACTION_UP";
//                mCurrentBox = null;
//                break;
//            case MotionEvent.ACTION_CANCEL:
//                action = "ACTION_CANCEL";
//                mCurrentBox = null;
//                break;
//        }
//
//        Log.i(TAG, action + " at x=" + current.x + ", y=" + current.y);
//        return true;
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        //Filling the background
//        canvas.drawPaint(mBackgroundPaint);
//
//        for(Box box : mBoxen) {
//            float left = Math.min(box.getOrigin().x, box.getCurrent().x);
//            float right = Math.max(box.getOrigin().x, box.getCurrent().x);
//            float top = Math.min(box.getOrigin().y, box.getCurrent().y);
//            float bottom = Math.max(box.getOrigin().y, box.getCurrent().y);
//            canvas.drawRect(left, top, right, bottom, mBoxPaint);
//        }
//    }
}
