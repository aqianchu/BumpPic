package com.qihoo.bumppic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.qihoo.bumppic.utils.ToastUtils;
import com.qihoo.bumppic.wifidirect.ClientService;
import com.qihoo.bumppic.wifidirect.WiFiClientBroadcastReceiver;

import java.io.File;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class SendPictureActivity extends ActivityBase {
    private DisplayImageOptions options;
    private Button send_bt;
    private ImageView img;
    private String path;
    private ImageLoader imgLoader;

    public final int fileRequestID = 98;
    public final int port = 7950;

    private WifiP2pManager wifiManager;
    private WifiP2pManager.Channel wifichannel;
    private BroadcastReceiver wifiClientReceiver;

    private IntentFilter wifiClientReceiverIntentFilter;

    private boolean connectedAndReadyToSendFile;

    private boolean filePathProvided;
    private File fileToSend;
    private boolean transferActive;

    private Intent clientServiceIntent;
    private WifiP2pDevice targetDevice;
    private WifiP2pInfo wifiInfo;
    private SendPictureActivity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_activity_send_picture);
        setBackListener();
        imgLoader = ImageLoader.getInstance();
        path = getIntent().getStringExtra("path");
        setView();
        setListener();
    }

    private void setView() {
        options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.background_picture1)
                .showImageForEmptyUri(R.drawable.background_picture1).showImageOnFail(R.drawable.background_picture1)
                .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
                .imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565).build();
        send_bt = (Button)findViewById(R.id.title_right_bt);
        send_bt.setVisibility(View.VISIBLE);
        send_bt.setOnClickListener(this);
        img = (ImageView)findViewById(R.id.send_picture_iv);
        imgLoader.displayImage("file://" + path,img, options);
    }

    private void setListener() {
        findViewById(R.id.title_right_bt).setOnClickListener(this);
    }


    @Override
    public void onResume() {
        super.onResume();
        startClient();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopClientReceiver();
    }
    private void startClient(){
        if (transferActive){
            return;
        }
        wifiManager = (WifiP2pManager) context.getSystemService(Context.WIFI_P2P_SERVICE);

        wifichannel = wifiManager.initialize(context, context.getMainLooper(), null);
        wifiClientReceiver = new WiFiClientBroadcastReceiver(wifiManager, wifichannel, this);

        wifiClientReceiverIntentFilter = new IntentFilter();
        wifiClientReceiverIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        wifiClientReceiverIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        wifiClientReceiverIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        wifiClientReceiverIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        connectedAndReadyToSendFile = false;
        filePathProvided = false;
        fileToSend = null;
        transferActive = false;
        clientServiceIntent = null;
        targetDevice = null;
        wifiInfo = null;

        context.registerReceiver(wifiClientReceiver, wifiClientReceiverIntentFilter);
//        setClientFileTransferStatus("Client is currently idle");
    }

    public void connectToPeer(final WifiP2pDeviceList peers)
    {
        WifiP2pDevice wifiPeer = null;
        for (WifiP2pDevice wd : peers.getDeviceList()){
            if (wifiPeer==null){
                wifiPeer = wd;
                break;
            }
        }
        if (wifiPeer==null){
            return;
        }

        this.targetDevice = wifiPeer;
        final WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = wifiPeer.deviceAddress;
        new Thread(){
            @Override
            public void run() {
                try {
                    Thread.currentThread().sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                wifiManager.connect(wifichannel, config, new WifiP2pManager.ActionListener()  {
                    public void onSuccess() {
                        ToastUtils.showShort(context,"连接服务器成功");
                        //setClientStatus("Connection to " + targetDevice.deviceName + " sucessful");
                    }
                    public void onFailure(int reason) {
                        ToastUtils.showShort(context,"连接服务器成功");
                        //setClientStatus("Connection to " + targetDevice.deviceName + " failed");
                    }
                });
            }
        }.start();
    }

    public void sendFile() {
        //Only try to send file if there isn't already a transfer active
        if(!transferActive)
        {
            filePathProvided = true;
            connectedAndReadyToSendFile = true;
            if(!filePathProvided)
            {
                ToastUtils.showShort(context,"Select a file to send before pressing send");
                //setClientFileTransferStatus("Select a file to send before pressing send");
            }
            else if(!connectedAndReadyToSendFile)
            {
                ToastUtils.showShort(context,"You must be connected to a server before attempting to send a file");
                //setClientFileTransferStatus("You must be connected to a server before attempting to send a file");
            }
	        /*
	        else if(targetDevice == null)
	        {
	        	setClientFileTransferStatus("Target Device network information unknown");
	        }
	        */
            else if(wifiInfo == null)
            {
                ToastUtils.showShort(context,"Missing Wifi P2P information");
                //setClientFileTransferStatus("Missing Wifi P2P information");
            }
            else
            {
                //Launch client service
                fileToSend = new File("/sdcard/IMG_20141213_194714_1.jpg");
                clientServiceIntent = new Intent(context, ClientService.class);
                clientServiceIntent.putExtra("fileToSend", fileToSend);
                clientServiceIntent.putExtra("port", new Integer(port));
                //clientServiceIntent.putExtra("targetDevice", targetDevice);
                clientServiceIntent.putExtra("wifiInfo", wifiInfo);
                clientServiceIntent.putExtra("clientResult", new ResultReceiver(null) {
                    @Override
                    protected void onReceiveResult(int resultCode, final Bundle resultData) {
                        if(resultCode == port )
                        {
                            if (resultData == null) {
                                //Client service has shut down, the transfer may or may not have been successful. Refer to message
                                transferActive = false;
                            }
                            else
                            {
                                ToastUtils.showShort(context,""+resultData.getString("message"));
//                                final TextView client_status_text = (TextView) findViewById(R.id.file_transfer_status);
//
//                                client_status_text.post(new Runnable() {
//                                    public void run() {
//                                        client_status_text.setText((String)resultData.get("message"));
//                                    }
//                                });
                            }
                        }
                    }
                });

                context.startService(clientServiceIntent);
                transferActive = true;
                //end
            }
        }
    }

    private void stopClientReceiver()
    {
        if (transferActive){
            try
            {
                context.unregisterReceiver(wifiClientReceiver);
            }
            catch(IllegalArgumentException e)
            {
                //This will happen if the server was never running and the stop button was pressed.
                //Do nothing in this case.
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopClientReceiver();
    }

    public void searchForPeers(View view) {
        //Discover peers, no call back method given
        wifiManager.discoverPeers(wifichannel, null);
    }

    public void setTransferStatus(boolean status)
    {
        connectedAndReadyToSendFile = status;
    }

    public void setNetworkToReadyState(boolean status, WifiP2pInfo info, WifiP2pDevice device)
    {
        wifiInfo = info;
        targetDevice = device;
        connectedAndReadyToSendFile = status;
    }
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.title_right_bt:
                ToastUtils.showShort(this,"发送");
                Glide.with(this).load(path)
                         .bitmapTransform(new BlurTransformation(context))
//                        .bitmapTransform( new CropCircleTransformation(context))
                        .into(img);
                break;
        }
    }
}
