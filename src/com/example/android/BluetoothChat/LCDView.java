/**************************************************************************
 *                                          
 * View to draw the MK LCD
 *                                          
 * Author:  Marcus -LiGi- Bueschleb   
 *
 * Project URL:
 *  http://mikrokopter.de/ucwiki/en/DUBwise
 * 
 * License:
 *  http://creativecommons.org/licenses/by-nc-sa/2.0/de/ 
 *  (Creative Commons / Non Commercial / Share Alike)
 *  Additionally to the Creative Commons terms it is not allowed
 *  to use this project in _any_ violent manner! 
 *  This explicitly includes that lethal Weapon owning "People" and 
 *  Organisations (e.g. Army & Police) 
 *  are not allowed to use this Project!
 *
 **************************************************************************/

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
	private short mode;
	private Float max_voltage;
	private static final Float LIPO_4S = 16.8f;
	private static final Float LIPO_3S = 12.6f;
	private static final Float LIPO_2S = 8.4f;
	private static final Float LIPO_1S = 4.2f;
	
	private RectF[] voltage_buf = new RectF[20];
	private Float volt_width = (this.getWidth()/40.0f)-2.0f;

	private Float volt_left = 1.0f;
	private Float volt_up = 1.0f;
	private Float volt_right = (this.getWidth()/2.0f)-2.0f;
	private Float volt_bottom = this.getHeight()-2.0f;
	
	@Override
	protected void onDraw(Canvas canvas) {
		Paint textPaint=new Paint();
		
		textPaint.setColor(0xFF000000);		
		textPaint.setAntiAlias(true);
		
		textPaint.setTextSize(char_height*1.65f);
		textPaint.setTextAlign(Align.CENTER);
		textPaint.setShadowLayer(1, 1, 1, 0xFF000000);
		textPaint.setFakeBoldText(true);
		//for (int line=0;line<4;line++)	{
			//for (int c=0;c<20;c++)	{
		volt_width = 8.0f;

		volt_left = 1.0f;
		volt_up = 1.0f;
		volt_right = (this.getWidth()/2.0f)-2.0f;
		volt_bottom = this.getHeight()-2.0f;		
			
				//canvas.drawRect(new RectF((this.getWidth()*c/20.0f)+1.0f , char_height*line+1, (this.getWidth()*(c+1)/20.0f)-2.0f  , char_height*(line+1)-2), mPaint);
				//canvas.drawRect(new Rect(c*(this.getWidth()/20) , char_height*line, (c+1)*(this.getWidth()/20)  , char_height*(line+1)), mPaint);
				//canvas.drawText(""+MKProvider.getMK().LCD.get_act_page()[line], 10, 10+line*mPaint.getTextSize(), mPaint);
				//canvas.drawRect(new Rect(chr*(this.getWidth()/20) , char_height*line, (chr+1)*(this.getWidth()/20)  , char_height*(line+1)), mPaint);
				//canvas.drawText("1", (float)c*(this.getWidth()/20.0f)+this.getWidth()/40.0f , char_height*(line)+3.0f*char_height/4.0f, textPaint);
			//}}
		mPaint.setAntiAlias(true);
		mPaint.setColor(0xFFFFFFCF);
		canvas.drawRect(0,0,this.getWidth(),this.getHeight(), mPaint);
		mPaint.setColor(0xFFFFFF7F);
		mPaint.setShadowLayer(2, 2, 2, 0xFFFFFF7F);
		canvas.drawRoundRect(new RectF(1.0f , 2.0f, volt_width*20 + 2.0f, this.getHeight()-2), 6, 6, mPaint);
		mPaint.setColor(0xFF40FF40);
		mPaint.setShadowLayer(2, 2, 2, 0xFF40FF40);
		
		for(RectF v_rect : voltage_buf)
			if(v_rect != null)
				canvas.drawRect(v_rect, mPaint);
		canvas.drawText(voltage, this.getWidth()/4.0f, this.getHeight()/2.0f, textPaint);
		//canvas.drawRect(new RectF(1.0f , this.getHeight()*(1 - voltage/12.6f), (this.getWidth()/2.0f)-2.0f  , this.getHeight()-2), mPaint);
		invalidate();
	}
	
	public void setVoltage(float volt)
	{
		if(voltage == "N/A")
			if(volt != 0) {
				if (volt > LIPO_2S) {
					if(volt > LIPO_3S) {
						max_voltage = LIPO_4S;
					}
					else
						max_voltage = LIPO_3S;
				}
				else
					max_voltage = LIPO_2S;
			}
		if(max_voltage != 0) {
			voltage_rect.set(2.0f , this.getHeight()*(1 - volt/max_voltage), 
					2.0f + volt_width, this.getHeight()-2);
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
			
			
			voltage = Float.toString(volt);
			//voltage_rect.set(1.0f , this.getHeight()*(1 - volt/max_voltage), (this.getWidth()/2.0f)-2.0f  , this.getHeight()-2);
		}
	}
	
	public void setVoltageNA()
	{
		voltage = "N/A";
		voltage_rect.set(1.0f , this.getHeight()*(1/12.6f), (this.getWidth()/2.0f)-2.0f  , this.getHeight()-2);
	}
	
	public void setMode(short md){
		mode = md;
	}
	
}
