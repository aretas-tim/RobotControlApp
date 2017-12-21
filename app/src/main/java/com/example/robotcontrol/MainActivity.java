package com.example.robotcontrol;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.design.widget.Snackbar;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.android.gms.appindexing.Action;
//import com.google.android.gms.appindexing.AppIndex;
//import com.google.android.gms.common.api.GoogleApiClient;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    BluetoothManager btManager;
    BluetoothAdapter btAdapter;
    BluetoothLeScanner btScanner;


    //private final static int REQUEST_ENABLE_BT = 1;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;


    Boolean btScanning = false;
    int deviceIndex = 0;
    //------Tim added in Testing
    public static final String TAG = "Robot Control";
    private static final int REQUEST_SELECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private static final int UART_PROFILE_CONNECTED = 20;
    private static final int UART_PROFILE_DISCONNECTED = 21;
    BluetoothDevice mDevice;
    private final static String DEVICE_NAME = "Tim";
    private final static String DEVICE_ADDRESS = "E2:62:C4:C7:04:EE";
    private com.example.robotcontrol.UartService mService = null;
    private Button btnSend;
    private EditText edtMessage;
    private int mState = UART_PROFILE_DISCONNECTED;
    //------Tim added in Testing

    //------Tim added For Real
    //The Direction buttons used to control the robot
    //-----------Tim added for Reals
    //Get a reference to all the Widgets used in the UI
    private Button UP_button;
    private Button DOWN_button;
    private Button RIGHT_button;
    private Button LEFT_button;
    private Button NW_button;
    private Button SW_button;
    private Button NE_button;
    private Button SE_button;

    private SeekBar Speed_Bar;


    //the handler object that mill be used to schedule repeated
    //BTLE trasmission every x milliseconds.
    Handler sendHandler = new Handler();
    //The number of milliseconds that BTLE update messages will be sent to
    //the arduino
    private static final int sendInterval = 5000;


    //The part of the data packet that will notify the arduino as to
    //wheather to the speed of the robot. either "O"(capital o, not zero),"S","M","F"
    //for off, slow, medium, fast
    private String speed_string = "O";
    //The direction string will be a string representation of a number 0-255
    //there are 8 possible directions, so a binary number can be used to represent which button is pressed
    //the number will be transmitted in decimal string to save on processing on the arduino side
    private int direction_int = 0b0;

    //bit position variables
    //used in the setting an dclearing of the direction byte bits
    public byte bit_0 = 0;
    public byte bit_1 = 1;
    public byte bit_2 = 2;
    public byte bit_3 = 3;
    public byte bit_4 = 4;
    public byte bit_5 = 5;
    public byte bit_6 = 6;
    public byte bit_7 = 7;





    private String receivedString;
    private String recievedTemp1;
    private String recievedTemp2;


    //------Tim added For Real

    BluetoothGatt bluetoothGatt;

    public final static String ACTION_GATT_CONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE =
            "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA =
            "com.example.bluetooth.le.EXTRA_DATA";

    public Map<String, String> uuids = new HashMap<String, String>();

    // Stops scanning after 5 seconds.
    private Handler mHandler = new Handler();
    private static final long SCAN_PERIOD = 5000;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    //private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        btManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        btAdapter = btManager.getAdapter();
        btScanner = btAdapter.getBluetoothLeScanner();

        UP_button = (Button) findViewById(R.id.up_button);
        DOWN_button = (Button) findViewById(R.id.down_button);
        RIGHT_button = (Button) findViewById(R.id.right_button);
        LEFT_button =(Button) findViewById(R.id.left_button);
        NW_button = (Button) findViewById(R.id.NW_button);
        SW_button = (Button) findViewById(R.id.SW_button);
        NE_button = (Button) findViewById(R.id.NE_button);
        SE_button = (Button) findViewById(R.id.SE_button);

        Speed_Bar = (SeekBar) findViewById(R.id.speedBar);
        //This starts the repeat sending of BTLE transmission
        //of direction and speed to arduino
        startRepeatingSend();






        //-----------Tim added for Reals

        //Does some initialization on Uart Service
        service_init();


        if (btAdapter != null && !btAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }

        // Make sure we have access coarse location enabled, if not, prompt the user to enable it
        if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("This app needs location access");
            builder.setMessage("Please grant location access so this app can detect peripherals.");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                }
            });
            builder.show();
        }

        //client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        startScanning();






        // a press or release of the UP button
        UP_button.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN ) {
                    //set the 0th bit to 1
                    direction_int |= (1<<bit_0);

                }else{
                    //clear the 0th bit to 0
                    direction_int &= ~(1 << bit_0);

                }

                return true;
            }
        });

        // a press or release of the NW button
        NW_button.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN ) {
                    //set the 0th bit to 1
                    direction_int |= (1<<bit_1);

                }else{
                    //clear the 0th bit to 0
                    direction_int &= ~(1 << bit_1);

                }

                return true;
            }
        });

        // a press or release of the RIGHT button
        RIGHT_button.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN ) {
                    //set the 0th bit to 1
                    direction_int |= (1<<bit_2);

                }else{
                    //clear the 0th bit to 0
                    direction_int &= ~(1 << bit_2);

                }

                return true;
            }
        });

        // a press or release of the RIGHT button
        SW_button.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN ) {
                    //set the 0th bit to 1
                    direction_int |= (1<<bit_3);

                }else{
                    //clear the 0th bit to 0
                    direction_int &= ~(1 << bit_3);

                }

                return true;
            }
        });

        // a press or release of the RIGHT button
        DOWN_button.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN ) {
                    //set the 0th bit to 1
                    direction_int |= (1<<bit_4);

                }else{
                    //clear the 0th bit to 0
                    direction_int &= ~(1 << bit_4);

                }

                return true;
            }
        });

        SE_button.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN ) {
                    //set the 0th bit to 1
                    direction_int |= (1<<bit_5);

                }else{
                    //clear the 0th bit to 0
                    direction_int &= ~(1 << bit_5);

                }

                return true;
            }
        });

        LEFT_button.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN ) {
                    //set the 0th bit to 1
                    direction_int |= (1<<bit_6);

                }else{
                    //clear the 0th bit to 0
                    direction_int &= ~(1 << bit_6);

                }

                return true;
            }
        });

        NE_button.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN ) {
                    //set the 0th bit to 1
                    direction_int |= (1<<bit_7);

                }else{
                    //clear the 0th bit to 0
                    direction_int &= ~(1 << bit_7);

                }

                return true;
            }
        });

        Speed_Bar.setOnSeekBarChangeListener(
                new OnSeekBarChangeListener() {
                    int progress = 0;
                    @Override
                    public void onProgressChanged(SeekBar seekBar,
                                                  int progresValue, boolean fromUser) {
                        progress = progresValue;
                        switch (progress) {

                            case 0:
                                speed_string = "O";
                                break;
                            case 1:
                                speed_string = "S";
                                break;
                            case 2:
                                speed_string = "M";
                                break;
                            case 3:
                                speed_string = "F";
                                break;

                            default:
                                speed_string = "O";
                                break;
                        }
                    }

                   @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        // Do something here,
                        //if you want to do anything at the start of
                        // touching the seekbar
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        // Display the value in textview

                    }
                });





    }

    //The code block that repeatedly sends BTLE transmissions of teh
    // speedbar state and direction pad
    private Runnable repeatBTLEsend = new Runnable() {
        @Override
        public void run() {

            // the string that will hold the direction of the button pressed
            String direction_string;
            //the mesage string containg the direction and speed
            String sendMessage="";
            //the byte array that will be sent to BLE module
            byte[] sendBytes;

            Log.d("Tx send Handlers", "Called on main thread");
            switch(direction_int)
            {
                //only the up button is pushed
                case 0b00000001:
                    direction_string = "w";
                    Log.d("Tx send Handlers", "up button is pushed");

               //the up and NW buttons are pushed     //
                case 0b00000011:
                    direction_string ="e";
                    Log.d("Tx send Handlers", "up and NW buttons are pushed");

                    //the NW button is pushed     //
                case 0b00000010:
                    direction_string ="e";
                    Log.d("Tx send Handlers", "the NW button is pushed ");

                 //the NW and right buttons are pushed
                case 0b00000110:
                    direction_string ="e";
                    Log.d("Tx send Handlers", "the NW and right buttons are pushed");

                //the right button is pushed
                case 0b00000100:
                    direction_string ="d";
                    Log.d("Tx send Handlers", "the right button is pushed");

                //the right and SW buttons are pushed
                case 0b00001100:
                    direction_string ="c";
                    Log.d("Tx send Handlers", "the right and SW buttons are pushed");

                //the SW button is pushed
                case 0b00001000:
                    direction_string ="c";
                    Log.d("Tx send Handlers", "the SW button is pushed");

                //the Sw and Down buttons are pushed
                case 0b00011000:
                    direction_string ="c";
                    Log.d("Tx send Handlers", "the Sw and Down buttons are pushed");

                //the Down button is pushed
                case 0b00010000:
                    direction_string ="x";
                    Log.d("Tx send Handlers", "the Down button is pushed");

                //the SE and Down buttons are pushed
                case 0b00110000:
                    direction_string ="z";
                    Log.d("Tx send Handlers", "the SE and Down buttons are pushed");

                //the SE button is pushed
                case 0b00100000:
                    direction_string ="z";
                    Log.d("Tx send Handlers", "the SE button is pushed");

                //the SE and left buttons are pushed
                case 0b01100000:
                    direction_string ="z";
                    Log.d("Tx send Handlers", "the SE and left buttons are pushed");

                //the left buttons is pushed
                case 0b01000000:
                    direction_string ="a";
                    Log.d("Tx send Handlers", "the left buttons is pushed");

                //the NE and left buttons are pushed
                case 0b11000000:
                    direction_string ="q";
                    Log.d("Tx send Handlers", "the NE and left buttons are pushed");

                //the NE buttons is pushed
                case 0b10000000:
                    direction_string ="q";
                    Log.d("Tx send Handlers", "the NE buttons is pushed");

                //the NE and UP buttons are pushed
                case 0b10000001:
                    direction_string ="q";
                    Log.d("Tx send Handlers", "the NE and UP buttons are pushed");

                default:
                    direction_string ="0";
            }

            //concatenate the direction and speed strings together to send to arduino
            sendMessage = direction_string+","+speed_string+";";


            //convert message to bytes and try to send it
            try {
                sendBytes = sendMessage.getBytes("UTF-8");
                mService.writeRXCharacteristic(sendBytes);
                //Update the log with time stamp
                String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
                Log.d("Tx send Handlers", "sending: "+sendMessage);
            }catch(UnsupportedEncodingException e){

                e.printStackTrace();
            }catch(NullPointerException e){

                Log.d(TAG,"writeRXcharacteristic threw null pointer exception. Reconnecting to bluetooth gatt");
                mService.connect(DEVICE_ADDRESS);
            }
            // Repeat this the same runnable code block again another 2 seconds
            // 'this' is referencing the Runnable object
            sendHandler.postDelayed(this, sendInterval);
        }
    };


    //The function that starts the repeated sending of BTLE
    //transmissions of direction and speed data to the arduiono
    void startRepeatingSend() {
        repeatBTLEsend.run();
    }

    //The function that stops the repeated sending of BTLE
    //transmissions of direction and speed data to the arduiono
    void stopRepeatingTask() {
        mHandler.removeCallbacks(repeatBTLEsend);
    }


    //UART service connected/disconnected
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder rawBinder) {
            mService = ((com.example.robotcontrol.UartService.LocalBinder) rawBinder).getService();
            Log.d(TAG, "onServiceConnected mService= " + mService);
            //----Tim Added
            //edtMessage.setEnabled(true);
            //btnSend.setEnabled(true);
            mService.initialize();
            mService.connect(DEVICE_ADDRESS);
            //----Tim Added
            if (!mService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }

        }

        public void onServiceDisconnected(ComponentName classname) {
            ////     mService.disconnect(mDevice);
            mService = null;
        }
    };


    //Its in this block that the program receives data transmitted from the nrf8001
    private final BroadcastReceiver UARTStatusChangeReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            final Intent mIntent = intent;
            //*********************//
            if (action.equals(com.example.robotcontrol.UartService.ACTION_GATT_CONNECTED)) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
                        Log.d(TAG, "UART_CONNECT_MSG");
                        //edtMessage.setEnabled(true);
                        //btnSend.setEnabled(true);
                        mState = UART_PROFILE_CONNECTED;
                    }
                });
            }

            //*********************//
            if (action.equals(com.example.robotcontrol.UartService.ACTION_GATT_DISCONNECTED)) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
                        Log.d(TAG, "UART_DISCONNECT_MSG");
                        //edtMessage.setEnabled(false);
                        //btnSend.setEnabled(false);
                        //((TextView) findViewById(R.id.deviceName)).setText("Not Connected");
                        mState = UART_PROFILE_DISCONNECTED;
                        mService.close();
                        //setUiState();

                    }
                });
            }


            //*********************//
            if (action.equals(com.example.robotcontrol.UartService.ACTION_GATT_SERVICES_DISCOVERED)) {
                mService.enableTXNotification();
                //mService.enableRXNotification();
            }
            //*********************//
            // this block is where the Uart data is received
            if (action.equals(com.example.robotcontrol.UartService.ACTION_DATA_AVAILABLE)) {

                final byte[] txValue = intent.getByteArrayExtra(com.example.robotcontrol.UartService.EXTRA_DATA);
                runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            receivedString = new String(txValue, "UTF-8");

                            String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
                            //Log.d(TAG, "receiving "+currentDateTimeString +" "+receivedString);
                            Log.d(TAG, "receiving "+currentDateTimeString +" "+receivedString);
                            String[] parts = receivedString.split(",|;|\r\n");
                            for(String part:parts){
                                Log.d(TAG, part+" was parsed from received string");


                            }
                            //receivedSetpoint1 =parts[0];
                            //receivedSetpoint2 =parts[1];
                            recievedTemp1 = parts[0];
                            recievedTemp2 = parts[1];
                            Log.d(TAG, recievedTemp1+" "+recievedTemp2+" ");



                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }
                    }
                });
            }
            //*********************//
            if (action.equals(com.example.robotcontrol.UartService.DEVICE_DOES_NOT_SUPPORT_UART)){
                showMessage("Device doesn't support UART. Disconnecting");
                mService.disconnect();
            }


        }
    };

    private void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }

    private void service_init() {
        Intent bindIntent = new Intent(this, com.example.robotcontrol.UartService.class);
        bindService(bindIntent, mServiceConnection, Context.BIND_AUTO_CREATE);

        LocalBroadcastManager.getInstance(this).registerReceiver(UARTStatusChangeReceiver, makeGattUpdateIntentFilter());
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(com.example.robotcontrol.UartService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(com.example.robotcontrol.UartService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(com.example.robotcontrol.UartService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(com.example.robotcontrol.UartService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(com.example.robotcontrol.UartService.DEVICE_DOES_NOT_SUPPORT_UART);
        return intentFilter;
    }
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
        //stop te repeated sending of directiona and speed
        //data to the arduino
        stopRepeatingTask();
        try {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(UARTStatusChangeReceiver);
        } catch (Exception ignore) {
            Log.e(TAG, ignore.toString());
        }
        unbindService(mServiceConnection);
        mService.stopSelf();
        mService= null;


    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        if (!btAdapter.isEnabled()) {
            Log.i(TAG, "onResume - BT not enabled yet");
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }



    //Tim Altered
    // Device scan callback.
    private ScanCallback leScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            //peripheralTextView.append("Index: " + deviceIndex + ", Device Name: " + result.getDevice().getName() + " rssi: " + result.getRssi() + "address"+result.getDevice().getAddress()+"\n");


            if(result.getDevice().getAddress().equals(DEVICE_ADDRESS)){
                System.out.println("conecting to "+result.getDevice().getName());
                connectToDeviceSelected(result.getDevice());

            }





        }
    };

    // Device connect call back
    private final BluetoothGattCallback btleGattCallback = new BluetoothGattCallback() {

        // this will get called anytime you perform a read or write characteristic operation
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {

            //if(characteristic.getUuid().equals(mService.RX_CHAR_UUID)) {
            //System.out.println("Rx characteristic has changed Main Activity");
            //mService.readRxCharacteristic();
            //}
            MainActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    //peripheralTextView.append("device read or wrote to\n");

                }
            });
        }

        @Override
        public void onConnectionStateChange(final BluetoothGatt gatt, final int status, final int newState) {
            // this will get called when a device connects or disconnects

            System.out.println(newState);
            switch (newState) {
                case 0:

                    MainActivity.this.runOnUiThread(new Runnable() {
                        public void run() {

                            //Tim added
                            gatt.connect();
                        }
                    });
                    break;
                case 2:

                    MainActivity.this.runOnUiThread(new Runnable() {
                        public void run() {

                        }
                    });


                    bluetoothGatt.discoverServices();

                    break;
                default:
                    MainActivity.this.runOnUiThread(new Runnable() {
                        public void run() {

                        }
                    });
                    break;
            }
        }

        @Override
        public void onServicesDiscovered(final BluetoothGatt gatt, final int status) {

            // this will get called after the client initiates a 			BluetoothGatt.discoverServices() call
            MainActivity.this.runOnUiThread(new Runnable() {
                public void run() {

                }
            });
            displayGattServices(bluetoothGatt.getServices());
        }

        @Override
        // Result of a characteristic read operation
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);

                final String dataInput = characteristic.getValue().toString();
                System.out.println("Value read from "+characteristic.getUuid().toString()+" is "+dataInput);
            }
        }
    };

    private void broadcastUpdate(final String action,
                                 final BluetoothGattCharacteristic characteristic) {

        System.out.println(characteristic.getUuid());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    System.out.println("coarse location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }
                return;
            }
        }
    }

    public void startScanning() {
        System.out.println("start scanning");
        btScanning = true;

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try{
                btScanner.startScan(leScanCallback);
                } catch (Exception NullPointerException)
                {

                    System.out.println("Caught " +NullPointerException+" trying to connect to ble device");

                }
            }
        });

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopScanning();
            }
        }, SCAN_PERIOD);

    }

    public void stopScanning() {
        System.out.println("stopping scanning");

        btScanning = false;

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                btScanner.stopScan(leScanCallback);
            }
        });


    }



    //Tim altered
    public void connectToDeviceSelected(BluetoothDevice device) {

        System.out.println("!!!!!!!!!!!!!!!!!!!!");
        System.out.println("Before connecting to " +device.getName());
        System.out.println("!!!!!!!!!!!!!!!!!!!!");


        try {
            bluetoothGatt = device.connectGatt(this, false, btleGattCallback);
        }catch(Exception e ){

            System.out.println("Caught " +e+" trying to connect to ble device");

        }

        System.out.println("!!!!!!!!!!!!!!!!!!!!");
        System.out.println("After After After " +device.getName());
        System.out.println("!!!!!!!!!!!!!!!!!!!!");
    }

    public void disconnectDeviceSelected() {

        bluetoothGatt.disconnect();
    }

    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;

        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {

            final String uuid = gattService.getUuid().toString();
            System.out.println("Service discovered: " + uuid);
            MainActivity.this.runOnUiThread(new Runnable() {
                public void run() {

                }
            });
            new ArrayList<HashMap<String, String>>();
            List<BluetoothGattCharacteristic> gattCharacteristics =
                    gattService.getCharacteristics();

            // Loops through available Characteristics.
            for (BluetoothGattCharacteristic gattCharacteristic :
                    gattCharacteristics) {

                final String charUuid = gattCharacteristic.getUuid().toString();
                System.out.println("Characteristic discovered for service: " + charUuid);
                MainActivity.this.runOnUiThread(new Runnable() {
                    public void run() {

                    }
                });

            }
        }
    }

   /* @Override
    public void onStart() {
        super.onStart();

        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.joelwasserman.androidbleconnectexample/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }*/

    /*@Override
    public void onStop() {
        super.onStop();

        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.joelwasserman.androidbleconnectexample/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }*/

    /**---------------Added UART service code------------------------**/
    /**---------------Added UART service code------------------------**/
    /**---------------Added UART service code------------------------**/


}

