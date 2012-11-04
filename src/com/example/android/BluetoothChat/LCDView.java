package com.example.android.Papandro;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.view.View;
import android.graphics.*;
import android.graphics.Paint.Align;

public class LCDView extends View

{
	private Paint mPaint = new Paint();
	public LCDView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LCDView(Activity context) {
		super(context);
		// needed to get Key Events
		setFocusable(true);
		//speed.paint = new Paint();
		//speed.color = new Color();
	}

    float[] line_points;
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		char_height=w/10;
		char_width=w/20;
	}

	int char_height=20;
	int char_width=10;
	private String voltage;
	private RectF voltage_rect = new RectF();
	private short mmode;
	private Float max_voltage;
	private static final Float LIPO_4S = 16.8f;
	private static final Float LIPO_3S = 12.6f;
	private static final Float LIPO_2S = 8.4f;
	private static final Float LIPO_1S = 4.2f;
	
	private int width, height;
	private int gap;
	
	private RectF[] voltage_buf = new RectF[20];
	private int volt_width;
	
	public Display volt = new Display();
	public Display speed = new Display();
	public Display mode = new Display();
	public Display rc = new Display();
	public Display gps = new Display();
	public Display motor = new Display();
	public Display alt = new Display();
	public Display link = new Display();
	public Display block = new Display();
	public Display wp = new Display();
	public Display connect = new Display();
	
	@Override
	protected void onDraw(Canvas canvas) {
		Paint textPaint=new Paint();
		
		textPaint.setColor(0xFF000000);		
		textPaint.setAntiAlias(true);
		
		textPaint.setTextSize(char_height);
		textPaint.setTextAlign(Align.CENTER);
		textPaint.setShadowLayer(1, 1, 1, 0xFF000000);
		textPaint.setFakeBoldText(true);
		
		width = this.getWidth();
		height = this.getHeight();
		gap = (int) (width*0.015625f);
		
		volt_width = volt.getWidth()/20;

		volt.setRect(gap, height/10 + gap, width/3, (int) (height*0.58f));
		link.setRect(gap, volt.bottom + gap, volt.right, volt.bottom + gap + height/10);
		
		speed.setRect(volt.right + gap, gap, (int) (width*0.67f), height/10);
		mode.setRect(volt.right + gap, speed.bottom + gap, speed.right, speed.bottom + gap + (volt.getHeight() + link.getHeight() - gap)/3);
		rc.setRect(volt.right + gap, mode.bottom + gap, speed.right, mode.bottom + gap + (volt.getHeight() + link.getHeight() - gap)/3);
		gps.setRect(volt.right + gap, rc.bottom + gap, speed.right, rc.bottom + gap + (volt.getHeight() + link.getHeight() - gap)/3);
		
		motor.setRect(gps.right + gap, gap, width - gap, height/10);
		alt.setRect(gps.right + gap, rc.top, width - gap, link.bottom + gap + link.getHeight());
		wp.setRect(gap, link.bottom + gap, gps.right, alt.bottom);
		block.setRect(gap, wp.bottom + gap, width - gap, alt.bottom + height/10);
		connect.setRect(gap, block.bottom + gap, width - gap, height - gap);
		
		
		mPaint.setAntiAlias(true);
		mPaint.setColor(0xFFFFFFCF);
		canvas.drawRect(0,0,this.getWidth(),this.getHeight(), mPaint);
		mPaint.setColor(0xFFFFFF3F);
		mPaint.setShadowLayer(2, 2, 2, 0xFFFFFF3F);
		
		canvas.drawRoundRect(volt.getRect(), 7, 7, mPaint); //voltage
		canvas.drawRoundRect(speed.getRect(), 7, 7, mPaint); // speed
		canvas.drawRoundRect(mode.getRect(), 7, 7, mPaint); // mode
		canvas.drawRoundRect(rc.getRect(), 7, 7, mPaint); // rc
		canvas.drawRoundRect(gps.getRect(), 7, 7, mPaint); // gps
		canvas.drawRoundRect(motor.getRect(), 7, 7, mPaint); // throttle
		canvas.drawRoundRect(alt.getRect(), 7, 7, mPaint); // altitude
		canvas.drawRoundRect(link.getRect(), 7, 7, mPaint); // link
		canvas.drawRoundRect(wp.getRect(), 7, 7, mPaint); // waypoints
		canvas.drawRoundRect(block.getRect(), 7, 7, mPaint); // block
		canvas.drawRoundRect(connect.getRect(), 7, 7, mPaint); // modem connection
		
		mPaint.setColor(0xFF40FF40);
		mPaint.setShadowLayer(2, 2, 2, 0xFF40FF40);
		
		for(RectF v_rect : voltage_buf)
			if(v_rect != null)
				canvas.drawRect(v_rect, mPaint);
		canvas.drawText(voltage, (volt.left + volt.right)/2, (volt.top + volt.bottom + char_height)/2, textPaint);
		//canvas.drawText(connect.text, (connect.left + connect.right)/2, (connect.top + connect.bottom + char_height)/2, textPaint);
		//canvas.drawRect(new RectF(1.0f , this.getHeight()*(1 - voltage/12.6f), (this.getWidth()/2.0f)-2.0f  , this.getHeight()-2), mPaint);
		invalidate();
	}
	
	public void setVoltage(float in_volt)
	{
		if(voltage == "N/A")
			if(in_volt != 0) {
				if (in_volt > LIPO_2S) {
					if(in_volt > LIPO_3S) {
						max_voltage = LIPO_4S;
					}
					else
						max_voltage = LIPO_3S;
				}
				else
					max_voltage = LIPO_2S;
			}
		if(max_voltage != 0) {
			voltage_rect.set(volt.left , volt.top + (volt.bottom - volt.top)*(1 - in_volt/max_voltage), 
					volt.left + volt_width, volt.bottom);
			voltage_buf[0] = voltage_rect;
			for(int i = 18; i >= 0; i--) {
				if(voltage_buf[i] != null) {
					if(voltage_buf[i+1] != null)
						voltage_buf[i+1].set(voltage_buf[i].left + volt_width, voltage_buf[i].top, 
								voltage_buf[i].right + volt_width, voltage_buf[i].bottom);
					else
						voltage_buf[i+1] = new RectF(voltage_buf[i].left + volt_width, voltage_buf[i].top, 
								voltage_buf[i].right + volt_width, voltage_buf[i].bottom);
				}
			}
			
			
			voltage = Float.toString(in_volt);
			//voltage_rect.set(1.0f , this.getHeight()*(1 - volt/max_voltage), (this.getWidth()/2.0f)-2.0f  , this.getHeight()-2);
		}
	}
	
	public void setVoltageNA()
	{
		voltage = "N/A";
		voltage_rect.set(1.0f , this.getHeight()*(1/12.6f), (this.getWidth()/2.0f)-2.0f  , this.getHeight()-2);
	}
	
	public void setMode(short md){
		mmode = md;
	}
	
}
