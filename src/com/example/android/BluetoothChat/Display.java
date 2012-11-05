package com.example.android.Papandro;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Align;

public class Display {
	public static final int orange = 0xFFEEDD82;
	public static final int green = 0xFF40FF40;
	public static final int red = 0xFFFF0000;
	public static final int blue = 0xFF00FFFF;
	public static final int black = 0xFF000000;
	
	public int left;
	public int top;
	public int right;
	public int bottom;
	public int text_height;
	public String text;
	public RectF[] display_buf;
	public Paint paint;
	public Paint tpaint;
	public Color color;
	
	public Display() {
		paint = new Paint();
		paint.setColor(orange);
		tpaint = new Paint();
		color = new Color();
	}
	
	public Display(int col, int tcol, int t_heigh, String txt) {
		paint = new Paint();
		tpaint = new Paint();
		color = new Color();
		paint.setColor(col);
		tpaint.setColor(tcol);
		tpaint.setTextSize(t_heigh);
		tpaint.setTextAlign(Align.CENTER);
		tpaint.setAntiAlias(true);
		tpaint.setShadowLayer(1, 1, 1, tcol);
		text_height = t_heigh;
		text = txt;
	}
	
	public void setRect(int l, int t, int r, int b) {
		left = l; top = t; right = r; bottom = b;
	}
	
	public RectF getRect() {
		return new RectF(left , top, right, bottom);
	}
	
	public int getWidth() {
		return (right - left);
	}
	
	public int getHeight() {
		return (bottom - top);
	}
	
	public void setColor(int col) {
		try{
		paint.setColor(col);
		}catch(Exception e){}
	}
	
	public void setTextHeight(int heigh) {
		text_height = heigh;
		tpaint.setTextSize(heigh);
	}
}
