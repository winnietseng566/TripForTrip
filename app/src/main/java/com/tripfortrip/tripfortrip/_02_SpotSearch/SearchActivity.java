package com.tripfortrip.tripfortrip._02_SpotSearch;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tripfortrip.tripfortrip.Manifest;
import com.tripfortrip.tripfortrip.R;

public class SearchActivity extends AppCompatActivity implements LocationListener {

    LocationManager locmgr = (LocationManager)
            getSystemService(Context.LOCATION_SERVICE);



    static final int MIN_TIME = 5000;
    static final float MIN_DIST = 0;
    LocationManager mgr;
    TextView txvLoc;
    TextView txvSetting;

    boolean isGPSEnabled;
    boolean isNetworkEnabled;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        txvLoc = (TextView) findViewById(R.id.txvLoc);
        txvSetting = (TextView) findViewById(R.id.txvSetting);


        mgr = (LocationManager) getSystemService(LOCATION_SERVICE);

        checkPermission(); // 檢查若尚未授權，則向使用者要求定位權限
    }

    @Override
    protected void onResume() {
        super.onResume();

        txvLoc.setText("尚未取得定位資訊");
        enableLocationUpdates(true);

        String str = "GPS定位:" + (isGPSEnabled ? "開啟" : "關閉");
        str += "\n網路定位:" + (isNetworkEnabled ? "開啟" : "關閉");
        txvSetting.setText(str);
    }

    @Override
    protected void onPause() {
        super.onPause();
        enableLocationUpdates(false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        if (grantResults.length >= 1 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "程式需要定位才能運作", Toast.LENGTH_SHORT).show();
        }
    }


    private void enableLocationUpdates(boolean isTurnOn) {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)
        {
            if (isTurnOn){
                // 檢查GPS與網路定位是否可用
                isGPSEnabled =
                        mgr.isProviderEnabled(LocationManager.GPS_PROVIDER);
                isNetworkEnabled =
                        mgr.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                if (!isGPSEnabled && !isNetworkEnabled){
                    // 無提供者，顯示提示訊息
                    Toast.makeText(this,"請確認已開啟定位功能！", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(this,"取得定位資訊中...", Toast.LENGTH_LONG).show();
                    if (isGPSEnabled){
                        mgr.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                MIN_TIME, MIN_DIST, this);
                        if (isNetworkEnabled){
                            mgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                                    MIN_TIME, MIN_DIST, this);
                        }
                    } else {
                        mgr.removeUpdates(this);
                    }
                }
            }
        }

    }

    // 檢查若尚未授權，則向使用者要求定位權限
    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},200);
        }
    }

    public void setup(View v) {
        Intent it =  // 使用Intent物件啟動"定位"設定程式
                new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(it);
    }


    @Override
    public void onLocationChanged(Location location) {     // 位置變更事件

        String str = "定位提供者：" + location.getProvider();
        str += String.format("\n緯度:%.5f\n經度:%.5f\n高度:%.2f公尺",
                location.getLatitude(),  // 緯度
                location.getLongitude(), // 經度
                location.getAltitude()); // 高度
        txvLoc.setText(str);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}
}