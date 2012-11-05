package com.example.android.Papandro;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

//import com.google.android.maps.MapActivity;
//import com.google.android.maps.MapView;

import com.example.android.Papandro.LCDView;

//import org.mapsforge.android.maps.MapActivity;
//import org.mapsforge.android.maps.MapView;
//import org.mapsforge.android.maps.Projection;
//import org.mapsforge.android.maps.overlay.ArrayItemizedOverlay;
//import org.mapsforge.android.maps.overlay.OverlayItem;
//import org.mapsforge.core.GeoPoint;

import android.R.drawable;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This is the main Activity that displays the current chat session.
 */
public class Papandro extends Activity {
    // Debugging
    private static final String TAG = "Papandro";
    private static final boolean D = true;

    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String DEVICE_ADDR = "device_addr";
    public static final String TOAST = "toast";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    
    private static final String LAST_DEV = "pprz_bt";

    // Layout Views
    //private TextView mTitle;
    private ListView mConversationView;
    //private EditText mOutEditText;
    //private Button mSendButton;

    // Name of the connected device
    private String mConnectedDeviceName = null;
    private String mConnectedDeviceAddr = null;
    // Array adapter for the conversation thread
    //private ArrayAdapter<String> mConversationArrayAdapter;
    // String buffer for outgoing messages
    //private StringBuffer mOutStringBuffer;
    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the chat services
    private BluetoothService mChatService = null;
    
//    private Projection projection;
    public LCDView lcd_view;


    @Override
    public void onCreate(Bundle savedInstanceState) {
    	if(D) Log.e(TAG, "+++ ON CREATE +++");
        super.onCreate(savedInstanceState);
        

        // Set up the window layout
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        lcd_view = new LCDView(this);
        lcd_view.setVoltageNA();
		setContentView(lcd_view);
        /*
        MapView mapView = new MapView(this);
        mapView.setClickable(true);
        mapView.setBuiltInZoomControls(true);
        mapView.setMapFile(new File("/sdcard/Papandro/berlin.map"));
        setContentView(mapView);
        
        Drawable defaultMarker = getResources().getDrawable(drawable.ic_menu_mylocation);
        // create an ItemizedOverlay with the default marker
        ArrayItemizedOverlay itemizedOverlay = new ArrayItemizedOverlay(defaultMarker);
        // create a GeoPoint with the latitude and longitude coordinates
        GeoPoint geoPoint = new GeoPoint(52.516272, 13.377722);
        // create an OverlayItem with title and description
        OverlayItem item = new OverlayItem(geoPoint, "Brandenburg Gate",
                "One of the main symbols of Berlin and Germany.");
        MyOverlay line = new MyOverlay();
        // add the OverlayItem to the ArrayItemizedOverlay
        itemizedOverlay.addItem(item);
        itemizedOverlay.addItem(line);
        // add the ArrayItemizedOverlay to the MapView
        mapView.getOverlays().add(itemizedOverlay);
        */
        /*
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.main);
        MapView mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        mapView.setSatellite(true);
        */
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.screenBrightness = 255.0f;
		getWindow().setAttributes(lp);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
        // Set up the custom title
        //mTitle = (TextView) findViewById(R.id.title_left_text);
        //mTitle.setText(R.string.app_name);
        //mTitle = (TextView) findViewById(R.id.title_right_text);
        
        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
    }
/*
    class MyOverlay extends OverlayItem{

        public MyOverlay(){

        }   

        public void draw(Canvas canvas, MapView mapv, boolean shadow){
            this.draw(canvas, mapv, shadow);

            Paint   mPaint = new Paint();
            mPaint.setDither(true);
            mPaint.setColor(Color.RED);
            mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            mPaint.setStrokeJoin(Paint.Join.ROUND);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            mPaint.setStrokeWidth(2);

            GeoPoint gP1 = new GeoPoint(52.516272, 13.377722);
            GeoPoint gP2 = new GeoPoint(52.617272, 13.478722);

            Point p1 = new Point();
            Point p2 = new Point();
            Path path = new Path();
 
            projection.toPixels(gP1, p1);
            projection.toPixels(gP2, p2);

            path.moveTo(p2.x, p2.y);
            path.lineTo(p1.x,p1.y);

            canvas.drawPath(path, mPaint);
        }
    }
*/    
    @Override
    public void onStart() {
        super.onStart();
        if(D) Log.e(TAG, "++ ON START ++");

        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        // Otherwise, setup the chat session
        } else {
            if (mChatService == null) 
            	setupChat();
        }
        String bt_addr = getBtDevice();
        if(bt_addr != null) {
        	// Attempt to connect to the device
        	BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(bt_addr);
        	mChatService.connect(device);
        }
    }

    @Override
    public synchronized void onResume() {
        super.onResume();
        if(D) Log.e(TAG, "+ ON RESUME +");

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mChatService.getState() == BluetoothService.STATE_NONE) {
              // Start the Bluetooth chat services
              mChatService.start();
            }
        }
    }

    private void saveBtDevice(String device) {
    	FileOutputStream fos;
		try {
			fos = openFileOutput(LAST_DEV, MODE_PRIVATE);
			try {
				fos.write(device.getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private String getBtDevice() {
    	FileInputStream fos;
        byte[] buf = new byte[17];
		try {
			fos = openFileInput(LAST_DEV);
			try {
				fos.read(buf);
				//mConnectedDeviceAddr = new String(buf);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Get the BLuetoothDevice object
			return new String(buf);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }
    
    private void setupChat() {
        Log.d(TAG, "setupChat()");

        // Initialize the array adapter for the conversation thread
        //mConversationArrayAdapter = new ArrayAdapter<String>(this, R.layout.message);
        //mConversationView = (ListView) findViewById(R.id.in);
        //mConversationView.setAdapter(mConversationArrayAdapter);

        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = new BluetoothService(this, mHandler);
    }

    @Override
    public synchronized void onPause() {
        super.onPause();
        if(D) Log.e(TAG, "- ON PAUSE -");
    }

    @Override
    public void onStop() {
        super.onStop();
        if(D) Log.e(TAG, "-- ON STOP --");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop the Bluetooth chat services
        if (mChatService != null) mChatService.stop();
        if(D) Log.e(TAG, "--- ON DESTROY ---");
    }

    private void ensureDiscoverable() {
        if(D) Log.d(TAG, "ensure discoverable");
        if (mBluetoothAdapter.getScanMode() !=
            BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    /**
     * Sends a message.
     * @param message  A string of text to send.
     */
    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothService.STATE_CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mChatService.write(send);
        }
    }
    
    private short Int8ToUInt8(byte in) {
		if (in < 0)
			return (short) (256 + in);
		else
			return in;
	}
    
    // The Handler that gets information back from the BluetoothChatService
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MESSAGE_STATE_CHANGE:
                if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                switch (msg.arg1) {
                case BluetoothService.STATE_CONNECTED:
                    //mTitle.setText(R.string.title_connected_to);
                    //mTitle.append(mConnectedDeviceName);
                	lcd_view.connect.text = getString(R.string.title_connected_to);
                	lcd_view.connect.setColor(lcd_view.connect.green);
                    //mConversationArrayAdapter.clear();
                    break;
                case BluetoothService.STATE_CONNECTING:
                    //mTitle.setText(R.string.title_connecting);
                	lcd_view.connect.text = getString(R.string.title_connecting);
                	lcd_view.connect.setColor(lcd_view.connect.orange);
                    break;
                case BluetoothService.STATE_LISTEN:
                case BluetoothService.STATE_NONE:
                    //mTitle.setText(R.string.title_not_connected);
                	lcd_view.connect.text = getString(R.string.title_not_connected);
                	lcd_view.connect.setColor(lcd_view.connect.red);
                	lcd_view.link.text = getString(R.string.link_nolink);
                	lcd_view.link.setColor(lcd_view.connect.red);
                    break;
                }
                break;
            case MESSAGE_WRITE:
                byte[] writeBuf = (byte[]) msg.obj;
                // construct a string from the buffer
                String writeMessage = new String(writeBuf);
                //mConversationArrayAdapter.add("Me:  " + writeMessage);
                break;
            case MESSAGE_READ:
            	byte[] readBuf = null;
            	ArrayList<byte[]> messages = (ArrayList<byte[]>) msg.obj;
                //String readMessage, Id;
                if(D) Log.i(TAG, "Message received " + (messages.size() - 1));
                //Id = "ID "+ Integer.toString(readBuf[0]) + " ";					//UAV Id
                for(int i = 0; i < messages.size(); i++)
                {
                	try {
                		readBuf = messages.get(i);
                	
                switch (Int8ToUInt8(readBuf[1])) {
                case 2: // ALIVE Message
                	//readMessage = Integer.toString(readBuf[3]<<8|readBuf[2]);					//Version
                	//readMessage = Integer.toString(readBuf[4]<<24|readBuf[5]<<16|readBuf[6]<<8|readBuf[7]);			//XBee_H
                	//readMessage = Integer.toString(readBuf[8]<<24|readBuf[9]<<16|readBuf[10]<<8|readBuf[11]);		//XBee_L
                	//readMessage = Integer.toString(Int8ToUInt8(readBuf[12]));					//MD5 sum
                	break;
                case 8: // GPS Message
                	//if(D) Log.i(TAG, "GPS " + readBuf[2]);
                	switch(readBuf[2]) {
                	case 0:
                		lcd_view.gps.text = getString(R.string.gps_no);
                    	lcd_view.gps.setColor(lcd_view.connect.red);
                		break;
                	case 1:
                		lcd_view.gps.text = getString(R.string.gps_2d);
                    	lcd_view.gps.setColor(lcd_view.connect.orange);
                		break;
                	case 3:
                		lcd_view.gps.text = getString(R.string.gps_3d);
                    	lcd_view.gps.setColor(lcd_view.connect.green);
                		break;
                	}
                	float speed = (float)((readBuf[18]<<8|readBuf[17])/100.);
                	
                	if(speed < 9) {
                		lcd_view.speed.setColor(lcd_view.connect.red);
                		if(speed < 1.)
                			lcd_view.speed.text = "0.0m/s"; // Speed
                		else
                			lcd_view.speed.text = Float.toString(speed) + "m/s"; // Speed
                	}
                	else {
                		lcd_view.speed.setColor(lcd_view.connect.orange);
                		lcd_view.speed.text = Float.toString(speed) + "m/s"; // Speed
                	}
                	break;
                case 11: // PPRZ_MODE Message
                	//readMessage = Integer.toString(readBuf[2]<<8|readBuf[3]);					//photo_nr
                	if(D) Log.i(TAG, "PPRZ_MODE: ");
                	switch(readBuf[2]) {
                	case 0:
                		lcd_view.mode.text = getString(R.string.mode_manu);
                    	lcd_view.mode.setColor(lcd_view.connect.orange);
                		break;
                	case 1:
                		lcd_view.mode.text = getString(R.string.mode_auto1);
                    	lcd_view.mode.setColor(lcd_view.connect.blue);
                		break;
                	case 2:
                		lcd_view.mode.text = getString(R.string.mode_auto2);
                    	lcd_view.mode.setColor(lcd_view.connect.green);
                		break;
                	case 3:
                		lcd_view.mode.text = getString(R.string.mode_home);
                    	lcd_view.mode.setColor(lcd_view.connect.red);
                		break;
                	case 4:
                		lcd_view.mode.text = getString(R.string.mode_nogps);
                    	lcd_view.mode.setColor(lcd_view.connect.red);
                		break;
                	case 5:
                		lcd_view.mode.text = getString(R.string.mode_failsafe);
                    	lcd_view.mode.setColor(lcd_view.connect.red);
                		break;
                	}
                	switch(readBuf[7]) {
                	case 0:
                		lcd_view.rc.text = getString(R.string.rc_lost);
                    	lcd_view.rc.setColor(lcd_view.connect.orange);
                		break;
                	case 1:
                		lcd_view.rc.text = getString(R.string.ok);
                    	lcd_view.rc.setColor(lcd_view.connect.green);
                		break;
                	case 2:
                		lcd_view.rc.text = getString(R.string.rc_no);
                    	lcd_view.rc.setColor(lcd_view.connect.red);
                		break;
                	}
                	break;
                case 12: // BAT Message
                	//readMessage = Integer.toString(readBuf[2]<<8|readBuf[3]);					//Throttle
                	lcd_view.motor.text = Integer.toString(readBuf[2]<<8|readBuf[3]/9600) + "%";
                	
                	//readMessage = Float.toString((float) (Int8ToUInt8(readBuf[4])/10.));		//Voltage
                	if(readBuf[4] != 0) {
                		lcd_view.setVoltage((float) (Int8ToUInt8(readBuf[4])/10.));
                		lcd_view.link.text = getString(R.string.ok);
                    	lcd_view.link.setColor(lcd_view.connect.green);
                		if(D) Log.i(TAG, "Voltage: " + readBuf[4]);
                	}
                	//readMessage = Integer.toString(readBuf[5]<<8|readBuf[6]);					//Flight time
                	break;
                case 31: // DL_VALUE Message
                	//readMessage = Integer.toString(readBuf[2]<<8|readBuf[3]);					//photo_nr
                	
                	switch(readBuf[2]) {
                	case 11: // kill throttle
                		if(D) Log.i(TAG, "DL Value: N" + readBuf[6]);
                		if(readBuf[6] == 0)
                			lcd_view.motor.setColor(lcd_view.connect.orange);
                		else
                			lcd_view.motor.setColor(lcd_view.connect.red);
                	break;
                	}
                	
                	break;
                case 110: // DC_SHOT Message
                	//readMessage = Integer.toString(readBuf[2]<<8|readBuf[3]);					//photo_nr
                	
                	break;
                }
                readBuf[i] = 0;
                	}catch(Exception e){}
                }
                break;
            case MESSAGE_DEVICE_NAME:
                // save the connected device's name
                mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                mConnectedDeviceAddr = msg.getData().getString(DEVICE_ADDR);
                
                saveBtDevice(mConnectedDeviceAddr);
                
                Toast.makeText(getApplicationContext(), "Connected to "
                               + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                break;
            case MESSAGE_TOAST:
                Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                               Toast.LENGTH_SHORT).show();
                break;
            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(D) Log.d(TAG, "onActivityResult " + resultCode);
        switch (requestCode) {
        case REQUEST_CONNECT_DEVICE:
            // When DeviceListActivity returns with a device to connect
            if (resultCode == Activity.RESULT_OK) {
                // Get the device MAC address
                String address = data.getExtras()
                                     .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                // Get the BLuetoothDevice object
                BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                // Attempt to connect to the device
                mChatService.connect(device);
            }
            break;
        case REQUEST_ENABLE_BT:
            // When the request to enable Bluetooth returns
            if (resultCode == Activity.RESULT_OK) {
                // Bluetooth is now enabled, so set up a chat session
                setupChat();
            } else {
                // User did not enable Bluetooth or an error occured
                Log.d(TAG, "BT not enabled");
                Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.scan:
            // Launch the DeviceListActivity to see devices and do scan
            Intent serverIntent = new Intent(this, DeviceListActivity.class);
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
            return true;
        case R.id.discoverable:
            // Ensure this device is discoverable by others
            ensureDiscoverable();
            return true;
        }
        return false;
    }
/*
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
*/
}