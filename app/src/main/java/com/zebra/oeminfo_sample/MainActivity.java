package com.zebra.oeminfo_sample;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
/*
BACKWARD PORTABILITY NOTES

- ON BSP 01.23.18, NO PATCHES, A8 => Error: content://oem_info/oem.zebra.secure/build_serial returns the package signature
*/

public class MainActivity extends AppCompatActivity{

    String TAG = "DeviceID";
    String URI_SERIAL = "content://oem_info/oem.zebra.secure/build_serial";
    String URI_IMEI = "content://oem_info/wan/imei";
    String URI_BT_MAC = "content://oem_info/oem.zebra.secure/bt_mac";
    String URI_WIFI_MAC = "content://oem_info/oem.zebra.secure/wifi_mac";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String resultString = "";

        if(Build.VERSION.SDK_INT<29){
            ((TextView) findViewById(R.id.txtSerialNumber)).setText( Build.getSerial() );  //REMINDER: MANUALLY GRANT THE PHONE STATE PERMISSION!
        }
        else {
            RetrieveOEMInfo(Uri.parse(URI_SERIAL), (TextView) findViewById(R.id.txtSerialNumber), false);       //  Build.getSerial()
            RetrieveOEMInfo(Uri.parse(URI_IMEI), (TextView) findViewById(R.id.txtImei), true);              //  TelephonyManager getImei()
            RetrieveOEMInfo(Uri.parse(URI_WIFI_MAC), (TextView) findViewById(R.id.txtWifiMac), false);
        }

        //  Note: Build ID and Fingerprint do not need special permission granted by Access Manager (these are standard Android IDs)
        TextView textViewID =  findViewById(R.id.txtBuildId);
        textViewID.setText(Build.ID);
        TextView textViewFingerPrint =   findViewById(R.id.txtFingerprint);
        textViewFingerPrint.setText( "App version "+BuildConfig.VERSION_NAME +"\n" + Build.FINGERPRINT+"\n"+getTargetSDK());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void RetrieveOEMInfo(Uri uri, TextView status, boolean isIMEI) {
        //  For clarity, this code calls ContentResolver.query() on the UI thread but production code should perform queries asynchronously.
        //  See https://developer.android.com/guide/topics/providers/content-provider-basics.html for more information
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);

        if (cursor == null || cursor.getCount() < 1)
        {
            String errorMsg = "Error: This app does not have access to call OEM service. " +
              "Please assign access to " + uri + " through MX.  See ReadMe for more information";
            Log.d(TAG, errorMsg);
            status.setText(errorMsg);
            return;
        }
        Log.i(TAG, "Records available =" + cursor.getCount());
        while (cursor.moveToNext()) {
            if (cursor.getColumnCount() == 0)
            {
                //  No data in the cursor.  I have seen this happen on non-WAN devices
                String errorMsg = "Error: " + uri + " does not exist on this device";
                Log.d(TAG, errorMsg);
                if (isIMEI)
                    errorMsg = "Error: Could not find IMEI.  Is device WAN capable?";
                status.setText(errorMsg);
            }
            else{
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    Log.v(TAG, "column " + i + "=" + cursor.getColumnName(i));
                    try {
                        String data = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(i)));
                        Log.i(TAG, "Column Data " + i + "=" + data);
                        status.setText(data);
                    }
                    catch (Exception e)
                    {
                        Log.i(TAG, "Exception reading data for column " + cursor.getColumnName(i));
                    }
                }
            }
        }
        cursor.close();
    }

    String getTargetSDK(){
        int version = 0;
        String app_username="";
        PackageManager pm = getPackageManager();
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = pm.getApplicationInfo(getPackageName() , 0);
        } catch (PackageManager.NameNotFoundException e) {}
        if (applicationInfo != null) {
            version = applicationInfo.targetSdkVersion;
            app_username = AndroidFileSysInfo.getNameForId( applicationInfo.uid );
        }
        return  "APP_TARGET_API:"+version+" APP_USER:"+app_username;
    }

}