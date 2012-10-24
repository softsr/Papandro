package com.example.android.BluetoothChat;

public class PprzTransport {
	private static final int TRANSPORT_PAYLOAD_LEN = 256;
	
	public static final int STX = 0x99;
	public static final int UNINIT      = 0;
    public static final int GOT_STX     = 1;
    public static final int GOT_LENGTH  = 2;
    public static final int GOT_PAYLOAD = 3;
    public static final int GOT_CRC1    = 4;
	
	// payload buffer
	public byte[] payload;
	// payload length
	public volatile int payload_len;
	// message received flag
	public volatile Boolean msg_received;
	// overrun and error flags
	public int ovrn, error;
	// specific pprz transport variables
	public int status;
	public int payload_idx;
	public byte ck_a, ck_b;
	
	private int i;
	private short c;
	
	public PprzTransport() {
		payload = new byte[TRANSPORT_PAYLOAD_LEN];
		payload_len = ovrn = error = payload_idx = ck_a = ck_b = 0;
		msg_received = false;
		status = UNINIT;
	}
	
	private short Int8ToUInt8(byte in) {
		if (in < 0)
			return (short) (256 + in);
		else
			return in;
	}
	
	private byte UInt8ToInt8(short in) {
		if (in > 127)
			return (byte) (127 - in);
		else
			return (byte) in;
	}
	
	private void parse_char(short c) {
		switch (status) {
    	  case UNINIT:
    	    if (c == STX)
    	      status++;
    	    break;
    	  case GOT_STX:
    	    if (msg_received) {
    	    	ovrn++;
    	    	error++;
    	    	status = UNINIT;
    	    	return;
    	    }
    	    payload_len = c-4; /* Counting STX, LENGTH and CRC1 and CRC2 */
    	    ck_a = ck_b = (byte) c;
    	    status++;
    	    payload_idx = 0;
    	    break;
    	  case GOT_LENGTH:
    	    payload[payload_idx] = UInt8ToInt8(c);
    	    ck_a += c; ck_b += ck_a;
    	    payload_idx++;
    	    if (payload_idx == payload_len)
    	    	status++;
    	    break;
    	  case GOT_PAYLOAD:
    	    if (c != ck_a) {
    	    	error++;
       	  		status = UNINIT;
       	  		return;
    	    }
    	    status++;
    	    break;
    	  case GOT_CRC1:
    	    if (c != ck_b){
    	    	error++;
       	  		status = UNINIT;
       	  		return;
    	    }
    	    msg_received = true;
       	  	status = UNINIT;
       	  	return;
    	  default:
    		  error++;
    		  status = UNINIT;
    		break;
    	  }
	}
	
	public boolean parse(int bytes, byte[] buf) {
		i = 0;
        while(bytes > i) {
        	c = Int8ToUInt8(buf[i]);	
      	  	parse_char(c);
      	  	i++;
        }
        if(msg_received) {
        	//msg_received = false;
        	return true;
        }
        return false;
	}
}
