package com.happyheng.sport_android.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.happyheng.sport_android.R;
import com.happyheng.sport_android.utils.DensityUtils;

/**
 * Created by liuheng on 16/7/16.
 */
public class TabSportBackView extends View {

    private Paint paint;
    private int bgHeight;

    public TabSportBackView(Context context, AttributeSet attrs) {
        super(context, attrs);

        int backColor = context.getResources().getColor(R.color.sport_bg);
        paint = new Paint();
        paint.setColor(backColor);
        paint.setStyle(Paint.Style.FILL);

        bgHeight = DensityUtils.dp2px(context, 305);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(0, 0, getMeasuredWidth(),bgHeight, paint);
    }
}
