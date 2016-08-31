//package com.qihoo.bumppic.frgment;
//
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.SharedPreferences;
//import android.net.wifi.p2p.WifiP2pManager;
//import android.nfc.NdefMessage;
//import android.nfc.NdefRecord;
//import android.nfc.NfcAdapter;
//import android.nfc.NfcEvent;
//import android.os.Bundle;
//import android.os.Parcelable;
//import android.os.ResultReceiver;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.qihoo.bumppic.R;
//import com.qihoo.bumppic.wifidirect.ServerService;
//import com.qihoo.bumppic.wifidirect.WiFiServerBroadcastReceiver;
//
//import java.io.File;
//
//import static android.nfc.NdefRecord.createMime;
//
///**
// * Created by hacker on 16/8/31.
// */
//public class FilterFragment1 extends Fragment{
//
//    private View view;
//    private FragmentActivity context;
//    private EditText mInputField;
//    private TextView mOutputText;
//    private NfcAdapter mNfcAdapter;
//    private WifiP2pManager wifiManager;
//    private WifiP2pManager.Channel wifichannel;
//    private WiFiServerBroadcastReceiver wifiServerReceiver;
//    private IntentFilter wifiServerReceiverIntentFilter;
//    private String path;
//    private File downloadTarget;
//    private Intent serverServiceIntent;
//    private boolean serverThreadActive;
//    private int port;
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        view = inflater.inflate(R.layout.filter_layout,container,false);
//        return view;
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        context = getActivity();
//        initializeComponents(view);
//        serverInit();
//    }
//
//
//
//    private void serverInit() {
//        if (serverThreadActive){
//            return;
//        }
//        wifiManager = (WifiP2pManager) context.getSystemService(Context.WIFI_P2P_SERVICE);
//        wifichannel = wifiManager.initialize(context, context.getMainLooper(), null);
//        wifiServerReceiver = new WiFiServerBroadcastReceiver(wifiManager, wifichannel, this);
//        wifiServerReceiverIntentFilter = new IntentFilter();
//        wifiServerReceiverIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
//        wifiServerReceiverIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
//        wifiServerReceiverIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
//        wifiServerReceiverIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
//
//        path = "/sdcard/nfc";
//        downloadTarget = new File(path);
//        if (!downloadTarget.exists()){
//            downloadTarget.mkdirs();
//        }
//        serverServiceIntent = null;
//        serverThreadActive = false;
//
//        context.registerReceiver(wifiServerReceiver, wifiServerReceiverIntentFilter);
//    }
//
//    private void initializeComponents(View view) {
//        mInputField = (EditText) view.findViewById(R.id.edt_input_beam);
//        mOutputText = (TextView) view.findViewById(R.id.received_txt_beam);
//        SharedPreferences sp = context.getSharedPreferences("nfc_data", Context.MODE_PRIVATE);
//        String payload = sp.getString("payload","空");
//        mOutputText.setText(payload);
//        ImageView mTapImageBeam = (ImageView) view.findViewById(R.id.img_beam);
//        mTapImageBeam.setOnClickListener(this);
//
//        mNfcAdapter = NfcAdapter.getDefaultAdapter(getActivity());
//        mNfcAdapter.setNdefPushMessageCallback(this, context);
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        startService();//开启服务
//        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(context.getIntent().getAction())) {
//            processIntent(context.getIntent());
//        }
//    }
//    @Override
//    public void onPause() {
//        super.onPause();
//        mNfcAdapter.disableForegroundDispatch(context);
//        stopServer();//停止服务
//    }
//
//    void processIntent(Intent intent) {
//        Parcelable[] receivedMessages = intent.getParcelableArrayExtra(
//                NfcAdapter.EXTRA_NDEF_MESSAGES);
//
//        NdefMessage message = (NdefMessage) receivedMessages[0];
//        String payload = new String(message.getRecords()[0].getPayload());
//        SharedPreferences sp = context.getSharedPreferences("nfc_data", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sp.edit();
//        editor.putString("payload",payload);
//        mOutputText.setText(payload);
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.img_beam:
//                // startService();
////                mNfcAdapter.setNdefPushMessageCallback(this, context);
//                break;
//        }
//    }
//
//    @Override
//    public NdefMessage createNdefMessage(NfcEvent event) {
//        startService();
//        String text = (mInputField.getText().toString());
//        return new NdefMessage(
//                new NdefRecord[]{createMime(
//                        "application/vnd.com.example.android.beam", text.getBytes())
//                });
//    }
//
//    private void startService(){
//        //If server is already listening on port or transfering data, do not attempt to start server service
//        if(!serverThreadActive)
//        {
//            //Create new thread, open socket, wait for connection, and transfer file
//
//            serverServiceIntent = new Intent(context, ServerService.class);
//            serverServiceIntent.putExtra("saveLocation", downloadTarget);
//            serverServiceIntent.putExtra("port", new Integer(port));
//            serverServiceIntent.putExtra("serverResult", new ResultReceiver(null) {
//                @Override
//                protected void onReceiveResult(int resultCode, final Bundle resultData) {
//
//                    if(resultCode == port )
//                    {
//                        if (resultData == null) {
//                            //Server service has shut down. Download may or may not have completed properly.
//                            serverThreadActive = false;
//                        }
//                        else
//                        {
//                        }
//                    }
//
//                }
//            });
//
//            serverThreadActive = true;
//            context.startService(serverServiceIntent);
//        }
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        try {
//            stopServer();
//            context.unregisterReceiver(wifiServerReceiver);
//        } catch (IllegalArgumentException e) {
//            // This will happen if the server was never running and the stop
//            // button was pressed.
//            // Do nothing in this case.
//        }
//    }
//
//    private void stopServer() {
//        //stop download thread
//        if(serverServiceIntent != null)
//        {
//            context.stopService(serverServiceIntent);
//        }
//    }
//}
