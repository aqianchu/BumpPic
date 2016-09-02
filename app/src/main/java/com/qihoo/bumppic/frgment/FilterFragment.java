package com.qihoo.bumppic.frgment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.p2p.WifiP2pManager;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.FileObserver;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qihoo.bumppic.R;
import com.qihoo.bumppic.SendActivity;
import com.qihoo.bumppic.utils.FilterUtils;
import com.qihoo.bumppic.utils.NfcUtils;
import com.qihoo.bumppic.utils.ToastUtils;
import com.qihoo.bumppic.wifidirect.ServerService;
import com.qihoo.bumppic.wifidirect.WiFiServerBroadcastReceiver;

import java.io.File;
import java.util.Random;

import jp.wasabeef.glide.transformations.BlurTransformation;

import static android.nfc.NdefRecord.createMime;

/**
 * Created by hacker on 16/8/29.
 */
public class FilterFragment extends Fragment implements View.OnClickListener{

    private View view;
    private FragmentActivity context;
//    private EditText mInputField;
    private TextView mOutputText;
    private NfcAdapter mNfcAdapter;
    private WifiP2pManager wifiManager;
    private WifiP2pManager.Channel wifichannel;
    private WiFiServerBroadcastReceiver wifiServerReceiver;
    private IntentFilter wifiServerReceiverIntentFilter;
    private String path;
    private File downloadTarget;
    private Intent serverServiceIntent;
    private boolean serverThreadActive;
    private int port;
    private ImageView img_beam;
    private ImageLoader imgLoader;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.filter_layout,container,false);
        setView();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        //这里判断是否支持nfc
        imgLoader = ImageLoader.getInstance();
        setView();
        if (!NfcUtils.isSupportNfc(context)){
            ToastUtils.showShort(context,NfcUtils.NFC_HINT);
            return;
        }
//        initializeComponents();
//        serverInit();
    }

    private void setView() {
        img_beam = (ImageView) view.findViewById(R.id.img_beam);
    }

    private int getRandom(int min,int max){
        Random random = new Random();
        int s = random.nextInt(max)%(max-min+1) + min;
        return s;
    }

    public void setImgBeamSrc(String path){
        if (img_beam!=null){
            Log.i("test","FilterSetImg");
          //  imgLoader.displayImage(path,img_beam);
            FilterUtils utils = new FilterUtils(context);
            DrawableRequestBuilder drb = utils.getFilter(path)[getRandom(1,13)];
            drb.into(img_beam);
        }
    }

    private void serverInit() {
        if (serverThreadActive){
            return;
        }
        wifiManager = (WifiP2pManager) context.getSystemService(Context.WIFI_P2P_SERVICE);
        wifichannel = wifiManager.initialize(context, context.getMainLooper(), null);
        wifiServerReceiver = new WiFiServerBroadcastReceiver(wifiManager, wifichannel, this);
        wifiServerReceiverIntentFilter = new IntentFilter();
        wifiServerReceiverIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        wifiServerReceiverIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        wifiServerReceiverIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        wifiServerReceiverIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        path = "/sdcard/nfc";
        downloadTarget = new File(path);
        if (!downloadTarget.exists()){
            downloadTarget.mkdirs();
        }
        serverServiceIntent = null;
        serverThreadActive = false;

        context.registerReceiver(wifiServerReceiver, wifiServerReceiverIntentFilter);
    }

    private void initializeComponents() {
        //mInputField = (EditText) view.findViewById(R.id.edt_input_beam);
        mOutputText = (TextView) view.findViewById(R.id.received_txt_beam);
        SharedPreferences sp = context.getSharedPreferences("nfc_data", Context.MODE_PRIVATE);
        String payload = sp.getString("payload","空");
        mOutputText.setText(payload);
        mNfcAdapter = NfcAdapter.getDefaultAdapter(getActivity());
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        if (!NfcUtils.isSupportNfc(context))
//            return;
//        mOutputText.setText("Action:"+context.getIntent().getAction());
//        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(context.getIntent().getAction())
//                ||NfcAdapter.ACTION_TAG_DISCOVERED.equals(context.getIntent().getAction())
//        ||NfcAdapter.ACTION_TECH_DISCOVERED.equals(context.getIntent().getAction())) {
//            ToastUtils.showShort(context,"收到");
//            ((SendActivity)context).setCurrentPager(1);
//            processIntent(context.getIntent());
//            startService();//开启服务
//        }
//    }
//    @Override
//    public void onPause() {
//        super.onPause();
//        if (!NfcUtils.isSupportNfc(context))
//            return;
//        mNfcAdapter.disableForegroundDispatch(context);
//        stopServer();//停止服务
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        try {
//            context.unregisterReceiver(wifiServerReceiver);
//        } catch (IllegalArgumentException e) {
//            // This will happen if the server was never running and the stop
//            // button was pressed.
//            // Do nothing in this case.
//        }
//        if (!NfcUtils.isSupportNfc(context))
//            return;
//        stopServer();
//    }

    private void processIntent(Intent intent) {
        Parcelable[] receivedMessages = intent.getParcelableArrayExtra(
                NfcAdapter.EXTRA_NDEF_MESSAGES);

        if(receivedMessages==null){
            return;
        }
        NdefMessage message = (NdefMessage) receivedMessages[0];
        String payload = new String(message.getRecords()[0].getPayload());
        SharedPreferences sp = context.getSharedPreferences("nfc_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("payload",payload);
        ToastUtils.showShort(context,payload);
        mOutputText.setText(payload);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_beam:
               // startService();
//                mNfcAdapter.setNdefPushMessageCallback(this, context);
                break;
        }
    }

    private void startService(){
        //If server is already listening on port or transfering data, do not attempt to start server service
        if(!serverThreadActive)
        {
            //Create new thread, open socket, wait for connection, and transfer file

            serverServiceIntent = new Intent(context, ServerService.class);
            serverServiceIntent.putExtra("saveLocation", downloadTarget);
            serverServiceIntent.putExtra("port", new Integer(port));
            serverServiceIntent.putExtra("serverResult", new ResultReceiver(null) {
                @Override
                protected void onReceiveResult(int resultCode, final Bundle resultData) {

                    if(resultCode == port )
                    {
                        if (resultData == null) {
                            //Server service has shut down. Download may or may not have completed properly.
                            serverThreadActive = false;
                        }
                        else
                        {
                        }
                    }
                }
            });

            serverThreadActive = true;
            context.startService(serverServiceIntent);
        }
    }

    private void stopServer() {
        //stop download thread
        if(serverServiceIntent != null)
        {
            context.stopService(serverServiceIntent);
        }
    }
}
