package ca.bcit.androidProject;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/*
A bar displaying how much the global mean sea level has risen since 1900 in millimeters.
Adapted from https://ssaurel.medium.com/learn-to-create-a-thermometer-application-for-android-295d6611b4f9
 */

public class SeaLevelBar extends View {

    private float outerCircleRadius, outerRectRadius;
    private Paint outerPaint;
    private float middleCircleRadius, middleRectRadius;
    private Paint middlePaint;
    private float innerCircleRadius, innerRectRadius;
    private Paint innerPaint;
    private Paint mmPaint, incrementPaint;
        
    // The text size of the increments in sp
    private static final int INCREMENT_TEXT_SIZE = 16;
    private static float MM_WIDTH = 30;
    private static final int NUMBER_OF_INCREMENTS = 10;
    // The highest and lowest increment values
    public static final float MAX_HEIGHT = 200, MIN_HEIGHT = 0;
    // The highest value in our data set
    public static final float MAX_VALUE = 206.2f;
    // The lowest value in our data set
    public static final float MIN_VALUE = -3.6f;
    private static final float HEIGHT_RANGE = MAX_HEIGHT - MIN_HEIGHT;
    private float currentHeight = MIN_HEIGHT;
    private final Rect rect = new Rect();
    // The gray outline of the bar
    private final RectF outerRect = new RectF();
    // The white background of the bar
    private final RectF middleRect = new RectF();
    // The blue bar
    private final RectF bar = new RectF();

    public SeaLevelBar(Context context) {
        super(context);
        initializeSeaLevelBar(context, null);
    }

    public SeaLevelBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeSeaLevelBar(context,attrs);
    }

    public SeaLevelBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super (context, attrs, defStyleAttr);
        initializeSeaLevelBar(context, attrs);
    }

    /**
     * Set the current height of the bar with a float representing the number of millimeters
     * the global mean sea level has risen since 1900. Calls invalidate to redraw the view
     * when the current height is changed
     * @param currentHeight a float
     */
    public void setCurrentHeight(float currentHeight) {
        this.currentHeight = currentHeight;
        invalidate();
    }

    /**
     * Initialize the colours and sizes of the sea level bar
     * @param context the Context
     * @param attrs the AttributeSet
     */
    public void initializeSeaLevelBar(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SeaLevelBar);
        outerCircleRadius = typedArray.getDimension(R.styleable.SeaLevelBar_radius, 20f);
        int outerColor = typedArray.getColor(R.styleable.SeaLevelBar_outerColor, Color.GRAY);
        int middleColor = typedArray.getColor(R.styleable.SeaLevelBar_middleColor, Color.WHITE);
        int innerColor = typedArray.getColor(R.styleable.SeaLevelBar_innerColor, Color.BLUE);

        typedArray.recycle();

        outerRectRadius = outerCircleRadius / 2;
        outerPaint = new Paint();

        outerPaint.setColor(outerColor);
        outerPaint.setStyle(Paint.Style.FILL);

        middleCircleRadius = outerCircleRadius - 5;
        middleRectRadius = outerRectRadius - 5;
        middlePaint = new Paint();
        middlePaint.setColor(middleColor);
        middlePaint.setStyle(Paint.Style.FILL);

        innerCircleRadius = middleCircleRadius - middleCircleRadius / 6;
        innerRectRadius = middleRectRadius - middleRectRadius / 6;
        innerPaint = new Paint();
        innerPaint.setColor(innerColor);
        innerPaint.setStyle(Paint.Style.FILL);

        MM_WIDTH = middleCircleRadius / 8;

        mmPaint = new Paint();
        mmPaint.setStrokeWidth(middleCircleRadius / 16);
        mmPaint.setColor(outerColor);
        mmPaint.setStyle(Paint.Style.FILL);

        incrementPaint = new Paint();
        incrementPaint.setColor(outerColor);
        incrementPaint.setStyle(Paint.Style.FILL);
        incrementPaint.setAntiAlias(true);
        incrementPaint.setTextSize(INCREMENT_TEXT_SIZE * context.getResources().getDisplayMetrics().density);

    }

    /**
     * Draw the sea level bar filled in to the value of currentHeight
     * @param canvas the Canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int height = getHeight();
        int width = getWidth();

        int circleCenterX = width / 2;
        float circleCenterY = height - outerCircleRadius;
        float outerStartY = 0;
        float middleStartY = outerStartY + 5;


        float innerEffectStartY = middleStartY + middleRectRadius + 10;
        float innerEffectEndY = circleCenterY - outerCircleRadius - 10;
        float innerRectHeight = innerEffectEndY - innerEffectStartY;
        float innerStartY = innerEffectStartY + (MAX_HEIGHT - currentHeight) / HEIGHT_RANGE * innerRectHeight;

        outerRect.left = circleCenterX - outerRectRadius;
        outerRect.top = outerStartY;
        outerRect.right = circleCenterX + outerRectRadius;
        outerRect.bottom = circleCenterY;

        // Draw the gray outline of the bar
        canvas.drawRoundRect(outerRect, outerRectRadius, outerRectRadius, outerPaint);
        // Draw the gray outline of the circle at the bottom of the bar
        //canvas.drawCircle(circleCenterX, circleCenterY, outerCircleRadius, outerPaint);

        middleRect.left = circleCenterX - middleRectRadius;
        middleRect.top = middleStartY;
        middleRect.right = circleCenterX + middleRectRadius;
        middleRect.bottom = circleCenterY;

        // Draw the inner white part of the bar
        canvas.drawRoundRect(middleRect, middleRectRadius, middleRectRadius, middlePaint);
        // Draw the inner white circle at the bottom of the bar
        //canvas.drawCircle(circleCenterX, circleCenterY, middleCircleRadius, middlePaint);

        bar.left = circleCenterX - innerRectRadius;
        bar.top = innerStartY;
        bar.right = circleCenterX + innerRectRadius;
        bar.bottom = circleCenterY - 5f;

        // Draw the blue bar
        //canvas.drawRect(circleCenterX - innerRectRadius, innerStartY, circleCenterX + innerRectRadius, circleCenterY, innerPaint);
        canvas.drawRoundRect(bar, innerRectRadius, innerRectRadius, innerPaint);
        // Draw the blue circle at the bottom of the bar
        //canvas.drawCircle(circleCenterX, circleCenterY, innerCircleRadius, innerPaint);

        float areaIncremented = innerEffectStartY;
        float incrementValue = MAX_HEIGHT;
        float increment = HEIGHT_RANGE / NUMBER_OF_INCREMENTS;

        // Draw the increments on the side of the bar
        while (areaIncremented <= innerEffectEndY) {
            canvas.drawLine(circleCenterX - outerRectRadius - MM_WIDTH, areaIncremented, circleCenterX - outerRectRadius, areaIncremented, mmPaint);
            String txt = ((int) incrementValue) + getResources().getString(R.string.sea_level_units);
            incrementPaint.getTextBounds(txt, 0, txt.length(), rect);
            float textWidth = rect.width();
            float textHeight = rect.height();

            canvas.drawText(((int) incrementValue) + getResources().getString(R.string.sea_level_units),
                    circleCenterX - outerRectRadius - MM_WIDTH - textWidth - MM_WIDTH * 1.5f,
                    areaIncremented + textHeight / 2, incrementPaint);
            areaIncremented += (innerEffectEndY - innerEffectStartY) / NUMBER_OF_INCREMENTS;
            incrementValue -= increment;
        }
    }
}
