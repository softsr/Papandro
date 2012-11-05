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
	
	public Display volt;
	public Display speed;
	public Display mode;
	public Display rc;
	public Display gps;
	public Display motor;
	public Display alt;
	public Display link;
	public Display block;
	public Display wp;
	public Display connect;
	
	public LCDView(Context context, AttributeSet attrs) {
		super(context, attrs);
		volt = new Display();
		speed = new Display();
		mode = new Display();
		rc = new Display();
		gps = new Display();
		motor = new Display();
		alt = new Display();
		link = new Display();
		block = new Display();
		wp = new Display();
		connect = new Display();
	}

	public LCDView(Activity context) {
		super(context);
		// needed to get Key Events
		setFocusable(true);
		volt = new Display();
		speed = new Display(Display.red, Display.black, this.getHeight()/10, "0.0m/s");
		mode = new Display(Display.orange, Display.black, this.getHeight()/10, context.getString(R.string.mode_manu));
		rc = new Display(Display.red, Display.black, this.getHeight()/10, context.getString(R.string.rc_no));
		gps = new Display(Display.red, Display.black, this.getHeight()/10, context.getString(R.string.gps_no));
		motor = new Display(Display.red, Display.black, this.getHeight()/10, "0%");
		alt = new Display();
		link = new Display(Display.red, Display.black, this.getHeight()/10, "");
		block = new Display();
		wp = new Display();
		connect = new Display(Display.red, Display.black, this.getHeight()/30, context.getString(R.string.title_not_connected));
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
		mPaint.setColor(0xFFFFFFFF);
		canvas.drawRect(0,0,this.getWidth(),this.getHeight(), mPaint);
		mPaint.setColor(0xFFeedd82);
		mPaint.setShadowLayer(2, 2, 2, 0xFFeedd82);
		
		canvas.drawRoundRect(volt.getRect(), 7, 7, mPaint); //voltage
		canvas.drawRoundRect(speed.getRect(), 7, 7, speed.paint); // speed
		canvas.drawRoundRect(mode.getRect(), 7, 7, mode.paint); // mode
		canvas.drawRoundRect(rc.getRect(), 7, 7, rc.paint); // rc
		canvas.drawRoundRect(gps.getRect(), 7, 7, gps.paint); // gps
		canvas.drawRoundRect(motor.getRect(), 7, 7, motor.paint); // throttle
		canvas.drawRoundRect(alt.getRect(), 7, 7, mPaint); // altitude
		canvas.drawRoundRect(link.getRect(), 7, 7, link.paint); // link
		canvas.drawRoundRect(wp.getRect(), 7, 7, mPaint); // waypoints
		canvas.drawRoundRect(block.getRect(), 7, 7, mPaint); // block
		canvas.drawRoundRect(connect.getRect(), 7, 7, connect.paint); // modem connection
		
		mPaint.setColor(0xFF40FF40);
		mPaint.setShadowLayer(2, 2, 2, 0xFF40FF40);
		
		for(RectF v_rect : voltage_buf)
			if(v_rect != null)
				canvas.drawRect(v_rect, mPaint);
		canvas.drawText(voltage, (volt.left + volt.right)/2, (volt.top + volt.bottom)/2 + char_height/3, textPaint);
		connect.setTextHeight(this.getHeight()/20);
		canvas.drawText(connect.text, (connect.left + connect.right)/2, (connect.top + connect.bottom)/2 + connect.text_height/3, connect.tpaint);
		gps.setTextHeight(this.getHeight()/17);
		canvas.drawText(gps.text, (gps.left + gps.right)/2, (gps.top + gps.bottom)/2 + gps.text_height/3, gps.tpaint);
		rc.setTextHeight(this.getHeight()/17);
		canvas.drawText(rc.text, (rc.left + rc.right)/2, (rc.top + rc.bottom)/2 + rc.text_height/3, rc.tpaint);
		mode.setTextHeight(this.getHeight()/17);
		canvas.drawText(mode.text, (mode.left + mode.right)/2, (mode.top + mode.bottom)/2 + mode.text_height/3, mode.tpaint);
		motor.setTextHeight(this.getHeight()/20);
		canvas.drawText(motor.text, (motor.left + motor.right)/2, (motor.top + motor.bottom)/2 + motor.text_height/3, motor.tpaint);
		speed.setTextHeight(this.getHeight()/20);
		canvas.drawText(speed.text, (speed.left + speed.right)/2, (speed.top + speed.bottom)/2 + speed.text_height/3, speed.tpaint);
		link.setTextHeight(this.getHeight()/20);
		canvas.drawText(link.text, (link.left + link.right)/2, (link.top + link.bottom)/2 + link.text_height/3, link.tpaint);
		
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
