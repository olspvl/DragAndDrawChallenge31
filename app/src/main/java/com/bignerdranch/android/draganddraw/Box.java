package com.bignerdranch.android.draganddraw;

import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;


public class Box implements Parcelable {
    private PointF mOrigin;
    private PointF mCurrent;
    private PointF mPointer;
    private float mAngle;

    public Box(PointF origin) {
        mOrigin = origin;
        mCurrent = origin;
    }

    protected Box(Parcel in) {
        mOrigin = in.readParcelable(PointF.class.getClassLoader());
        mCurrent = in.readParcelable(PointF.class.getClassLoader());
    }

    public static final Creator<Box> CREATOR = new Creator<Box>() {
        @Override
        public Box createFromParcel(Parcel in) {
            return new Box(in);
        }

        @Override
        public Box[] newArray(int size) {
            return new Box[size];
        }
    };

    public PointF getCurrent() {
        return mCurrent;
    }

    public void setCurrent(PointF current) {
        mCurrent = current;
    }

    public PointF getOrigin() {
        return mOrigin;
    }

    public PointF getPointer() {
        return mPointer;
    }

    public void setPointer(PointF pointer) {
        mPointer = pointer;
    }

    public float getAngle() {
        return mAngle;
    }

    public void setAngle(float angle) {
        mAngle = angle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        mOrigin.writeToParcel(dest, flags);
        mCurrent.writeToParcel(dest, flags);
    }
}
