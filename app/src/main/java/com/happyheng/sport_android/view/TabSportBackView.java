package com.happyheng.sport_android.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.happyheng.sport_android.R;

/**
 * 运动界面的背景View
 * Created by liuheng on 16/7/16.
 */
public class TabSportBackView extends View {

    private Paint mPaint;
    private int mBackHeight,mBackBottomHeight;
    private Path mBottomPath;

    public TabSportBackView(Context context, AttributeSet attrs) {
        super(context, attrs);

        int backColor = context.getResources().getColor(R.color.sport_bg);
        mPaint = new Paint();
        mPaint.setColor(backColor);
        mPaint.setStyle(Paint.Style.FILL);

        mBottomPath =new Path();

        mBackHeight = getResources().getDimensionPixelSize(R.dimen.tab_sport_top_height);
        mBackBottomHeight = getResources().getDimensionPixelSize(R.dimen.tab_sport_oval_half_height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(0, 0, getMeasuredWidth(), mBackHeight, mPaint);

        mBottomPath.moveTo(0, mBackHeight);
        mBottomPath.lineTo(getMeasuredWidth()/2, mBackHeight + mBackBottomHeight);
        mBottomPath.lineTo(getMeasuredWidth(), mBackHeight);
        mBottomPath.close();
        canvas.drawPath(mBottomPath,mPaint);
    }
}
