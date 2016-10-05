package com.uf.dancemarathon;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;



/**
 * This class is a work in progress. The goal is to display a fundraising thermometer.
 * @author Chris Whitten
 *
 */
public class ThermometerView extends View
{

	private float maxValue;
	
	//All the bulb fields
	private Paint bulbPaint;
	private RectF bulbRect;
	private int bulbRectWidth;
	private int bulbRectHeight;
	private int bulbStartAngle = -45;
	private int bulbSweepAngle = 270;
	private Path bulbClipPath;
	private Paint bulbOutlinePaint;
	
	//The line fields
	private int leftLine_sx;
	private int leftLine_sy;
	private int leftLine_ex;
	private int leftLine_ey;
	private int lineHeight;
	
	private int fillColor;
	private int outlineColor;
	private int outlineWidth;
	private float posX = 0.0f;
	private float posY = 0.0f;
	private float screenWidth;
	private float screenHeight;
	
	public ThermometerView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		
		//Get an array of the attributes
		TypedArray a = context.getTheme().obtainStyledAttributes(
		        attrs,
		        R.styleable.ThermometerView,
		        0, 0);
		
		try
		{
			//Try to get the max value attribute. Put 0 if not defined
			maxValue = a.getFloat(R.styleable.ThermometerView_maxValue, 0);
			fillColor = a.getColor(R.styleable.ThermometerView_fillColor, Color.YELLOW);
			outlineColor = a.getColor(R.styleable.ThermometerView_outlineColor, Color.GRAY);
			outlineWidth = a.getInteger(R.styleable.ThermometerView_outlineWidth, 7);
		}
		finally
		{
			a.recycle();
		}
		
		screenWidth = context.getResources().getDisplayMetrics().widthPixels;
		screenHeight = context.getResources().getDisplayMetrics().heightPixels;
		
		setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		initPaintObjects();

	}
	
	private void calculateLeftLinePoints(int thermHeight)
	{
		int centerX = (int) bulbRect.centerX();
		int centerY = (int) bulbRect.centerY();
		int angle = bulbStartAngle + bulbSweepAngle;
		
		int xDisp = centerX - (int) Math.abs(Math.cos(angle));
		int yDisp = centerY - (int) Math.abs(Math.sin(angle));
		leftLine_sx = (int) (bulbRect.left + xDisp);
		leftLine_sy = (int) (bulbRect.top + yDisp);
		
		leftLine_ex = leftLine_sx;
		leftLine_ey = leftLine_sy - thermHeight; //Minus goes up
	}
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		
		bulbRectHeight = h/5;
		bulbRectWidth = bulbRectHeight;
		bulbRect = new RectF(5, h-bulbRectHeight+5 ,bulbRectWidth-5, h-5);
		
		lineHeight = h - bulbRectHeight - 5;
		calculateLeftLinePoints(lineHeight);
	}


	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		int width = 0;
		int height = 0;
		int desiredWidth = 100;
		int desiredHeight = 500;
		int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
		
		//Set width
		if(widthMode == View.MeasureSpec.EXACTLY)
			width = View.MeasureSpec.getSize(widthMeasureSpec);
		else if(widthMode == View.MeasureSpec.AT_MOST)
		{
			int widthBound = View.MeasureSpec.getSize(widthMeasureSpec);
			width = Math.min(desiredWidth, widthBound-1);
		}
		else
			width = desiredWidth;
		
		//Set height
		if(heightMode == View.MeasureSpec.EXACTLY)
			height = View.MeasureSpec.getSize(heightMeasureSpec);
		else if(heightMode == View.MeasureSpec.AT_MOST)
		{
			int heightBound = View.MeasureSpec.getSize(heightMeasureSpec);
			height = Math.min(desiredHeight,heightBound-1);
		}
		else
			height = desiredHeight;
			
		setMeasuredDimension(width, height);
	}
	
	public void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		canvas.drawArc(bulbRect, bulbStartAngle, bulbSweepAngle, false, bulbOutlinePaint);
		canvas.drawLine(leftLine_sx, leftLine_sy, leftLine_ex, leftLine_ey, bulbOutlinePaint);
	}
	
	
	
	private void initPaintObjects()
	{
		
		//Initialize bulb
		bulbPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		bulbPaint.setColor(fillColor);
		bulbPaint.setStyle(Paint.Style.FILL);
		
		//Initialize bulb outline
		bulbOutlinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		bulbOutlinePaint.setColor(outlineColor);
		bulbOutlinePaint.setStyle(Paint.Style.STROKE);
		bulbOutlinePaint.setStrokeWidth(outlineWidth);
		bulbOutlinePaint.setStrokeJoin(Paint.Join.MITER);
		
		//Initalize bulb clipping path
		bulbClipPath = new Path();
		
	}
	/**
	 * @return the maxValue
	 */
	public float getMaxValue()
	{
		return maxValue;
	}
	/**
	 * @param maxValue the maxValue to set
	 */
	public void setMaxValue(float maxValue)
	{
		this.maxValue = maxValue;
		//Must update the view
		invalidate();
		requestLayout();
	}

	/**
	 * @return the fillColor
	 */
	public int getFillColor()
	{
		return fillColor;
	}

	/**
	 * @param fillColor the fillColor to set
	 */
	public void setFillColor(int fillColor)
	{
		this.fillColor = fillColor;
		//Must update the view
		invalidate();
		requestLayout();
}

}
