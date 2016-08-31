//package com.qihoo.bumppic.frgment;
//
//import android.app.Activity;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.net.wifi.p2p.WifiP2pConfig;
//import android.net.wifi.p2p.WifiP2pDevice;
//import android.net.wifi.p2p.WifiP2pDeviceList;
//import android.net.wifi.p2p.WifiP2pInfo;
//import android.net.wifi.p2p.WifiP2pManager;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.Handler;
//import android.os.Message;
//import android.os.ResultReceiver;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.GridView;
//import android.widget.Toast;
//
//import com.qihoo.bumppic.R;
//import com.qihoo.bumppic.adapter.SendGridViewAdapter;
//import com.qihoo.bumppic.utils.ThreadUtils;
//import com.qihoo.bumppic.utils.ToastUtils;
//import com.qihoo.bumppic.wifidirect.ClientService;
//import com.qihoo.bumppic.wifidirect.WiFiClientBroadcastReceiver;
//
//import java.io.BufferedOutputStream;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.util.ArrayList;
//import java.util.List;
//
//
///**
// * Created by hacker on 16/8/29.
// */
//public class SendFragment extends Fragment implements AdapterView.OnItemClickListener {
//
//    public final int fileRequestID = 98;
//    public final int port = 7950;
//
//    private WifiP2pManager wifiManager;
//    private WifiP2pManager.Channel wifichannel;
//    private BroadcastReceiver wifiClientReceiver;
//
//    private IntentFilter wifiClientReceiverIntentFilter;
//
//    private boolean connectedAndReadyToSendFile;
//
//    private boolean filePathProvided;
//    private File fileToSend;
//    private boolean transferActive;
//
//    private Intent clientServiceIntent;
//    private WifiP2pDevice targetDevice;
//    private WifiP2pInfo wifiInfo;
//
//    View view;
//    Activity context;
//    List<Integer>datas;
//    Handler handler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what){
//                case ThreadUtils.THREAD_FIVE_MESSAGE_WHAT:
//                    //  view.findViewById(R.id.send_picture_hint_tv).setVisibility(View.GONE);
//                    break;
//            }
//        }
//    };
//    private GridView gridview;
//    private static final int REQUEST_CODE_PICK_IMAGE=1001;
//    private  static final int REQUEST_CODE_CAPTURE_CAMEIA=1002;
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.send_layout,container,false);
//        this.view = view;
//        return view;
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        context = getActivity();
//        setView(view);
//
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        startClient();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        stopClientReceiver();
//    }
//
//    private void startClient(){
//        if (transferActive){
//            return;
//        }
//        wifiManager = (WifiP2pManager) context.getSystemService(Context.WIFI_P2P_SERVICE);
//
//        wifichannel = wifiManager.initialize(context, context.getMainLooper(), null);
//        wifiClientReceiver = new WiFiClientBroadcastReceiver(wifiManager, wifichannel, this);
//
//        wifiClientReceiverIntentFilter = new IntentFilter();
//        wifiClientReceiverIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
//        wifiClientReceiverIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
//        wifiClientReceiverIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
//        wifiClientReceiverIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
//
//        connectedAndReadyToSendFile = false;
//        filePathProvided = false;
//        fileToSend = null;
//        transferActive = false;
//        clientServiceIntent = null;
//        targetDevice = null;
//        wifiInfo = null;
//
//        context.registerReceiver(wifiClientReceiver, wifiClientReceiverIntentFilter);
////        setClientFileTransferStatus("Client is currently idle");
//    }
//
//    int res_Id[] = new int[]{R.drawable.example_picture_2,R.drawable.example_picture_3,
//            R.drawable.example_picture_2,R.drawable.example_picture_3};
//
//    private void setView(View view) {
//        gridview = (GridView)view.findViewById(R.id.send_gridView);
//        initData();
//        gridview.setAdapter(new SendGridViewAdapter(context,datas));
//        ThreadUtils.threadFiveMinute(handler);
//        gridview.setOnItemClickListener(this);
//        view.findViewById(R.id.send_picture_hint_tv).setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                //wifiManager.discoverPeers(wifichannel,null);
////                getImageFromAlbum();
//            }
//        });
//    }
//
//
//
//    private void initData() {
//        datas = new ArrayList<Integer>();
//        for (int i=0;i<res_Id.length;i++){
//            datas.add(res_Id[i]);
//        }
//    }
//
//    public void connectToPeer(final WifiP2pDeviceList peers)
//    {
//        WifiP2pDevice wifiPeer = null;
//        for (WifiP2pDevice wd : peers.getDeviceList()){
//            if (wifiPeer==null){
//                wifiPeer = wd;
//                break;
//            }
//        }
//        if (wifiPeer==null){
//            return;
//        }
//
//        this.targetDevice = wifiPeer;
//        final WifiP2pConfig config = new WifiP2pConfig();
//        config.deviceAddress = wifiPeer.deviceAddress;
//        new Thread(){
//            @Override
//            public void run() {
//                try {
//                    Thread.currentThread().sleep(5000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                wifiManager.connect(wifichannel, config, new WifiP2pManager.ActionListener()  {
//                    public void onSuccess() {
//                        ToastUtils.showShort(context,"连接服务器成功");
//                        //setClientStatus("Connection to " + targetDevice.deviceName + " sucessful");
//                    }
//                    public void onFailure(int reason) {
//                        ToastUtils.showShort(context,"连接服务器成功");
//                        //setClientStatus("Connection to " + targetDevice.deviceName + " failed");
//                    }
//                });
//            }
//        }.start();
//    }
//
//    public void sendFile() {
//        //Only try to send file if there isn't already a transfer active
//        if(!transferActive)
//        {
//            filePathProvided = true;
//            connectedAndReadyToSendFile = true;
//            if(!filePathProvided)
//            {
//                ToastUtils.showShort(context,"Select a file to send before pressing send");
//                //setClientFileTransferStatus("Select a file to send before pressing send");
//            }
//            else if(!connectedAndReadyToSendFile)
//            {
//                ToastUtils.showShort(context,"You must be connected to a server before attempting to send a file");
//                //setClientFileTransferStatus("You must be connected to a server before attempting to send a file");
//            }
//	        /*
//	        else if(targetDevice == null)
//	        {
//	        	setClientFileTransferStatus("Target Device network information unknown");
//	        }
//	        */
//            else if(wifiInfo == null)
//            {
//                ToastUtils.showShort(context,"Missing Wifi P2P information");
//                //setClientFileTransferStatus("Missing Wifi P2P information");
//            }
//            else
//            {
//                //Launch client service
//                fileToSend = new File("/sdcard/IMG_20141213_194714_1.jpg");
//                clientServiceIntent = new Intent(context, ClientService.class);
//                clientServiceIntent.putExtra("fileToSend", fileToSend);
//                clientServiceIntent.putExtra("port", new Integer(port));
//                //clientServiceIntent.putExtra("targetDevice", targetDevice);
//                clientServiceIntent.putExtra("wifiInfo", wifiInfo);
//                clientServiceIntent.putExtra("clientResult", new ResultReceiver(null) {
//                    @Override
//                    protected void onReceiveResult(int resultCode, final Bundle resultData) {
//                        if(resultCode == port )
//                        {
//                            if (resultData == null) {
//                                //Client service has shut down, the transfer may or may not have been successful. Refer to message
//                                transferActive = false;
//                            }
//                            else
//                            {
//                                ToastUtils.showShort(context,""+resultData.getString("message"));
////                                final TextView client_status_text = (TextView) findViewById(R.id.file_transfer_status);
////
////                                client_status_text.post(new Runnable() {
////                                    public void run() {
////                                        client_status_text.setText((String)resultData.get("message"));
////                                    }
////                                });
//                            }
//                        }
//                    }
//                });
//
//                context.startService(clientServiceIntent);
//                transferActive = true;
//                //end
//            }
//        }
//    }
//
//    private void stopClientReceiver()
//    {
//        if (transferActive){
//            try
//            {
//                context.unregisterReceiver(wifiClientReceiver);
//            }
//            catch(IllegalArgumentException e)
//            {
//                //This will happen if the server was never running and the stop button was pressed.
//                //Do nothing in this case.
//            }
//        }
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        stopClientReceiver();
//    }
//
//    public void searchForPeers(View view) {
//        //Discover peers, no call back method given
//        wifiManager.discoverPeers(wifichannel, null);
//    }
//
//    public void setTransferStatus(boolean status)
//    {
//        connectedAndReadyToSendFile = status;
//    }
//
//    public void setNetworkToReadyState(boolean status, WifiP2pInfo info, WifiP2pDevice device)
//    {
//        wifiInfo = info;
//        targetDevice = device;
//        connectedAndReadyToSendFile = status;
//    }
//
//    @Override
//    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//        //sendFile();
//    }
//}
