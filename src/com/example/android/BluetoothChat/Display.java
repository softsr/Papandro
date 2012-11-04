package com.example.android.Papandro;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class Display {
	public int left;
	public int top;
	public int right;
	public int bottom;
	public int text_height;
	public String text;
	public RectF[] display_buf;
	public Paint paint;
	public Color color;
	
	public void Display() {
		paint = new Paint();
		color = new Color();
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
}
