package com.example.android.BluetoothChat;

import java.util.HashMap;

public class PprzMessages {
	
	public byte parse_voltage(byte[] payload) {
		if(payload[1] == 12)
			return payload[4];
		return 0;
	}
	
	private HashMap parse_class_by_id(String pprz_class) {
		HashMap by_id = new HashMap<Object, Object>();
		return null;
	}
	
	private HashMap parse_class_by_msg(String pprz_class) {
		return null;
	}
}
