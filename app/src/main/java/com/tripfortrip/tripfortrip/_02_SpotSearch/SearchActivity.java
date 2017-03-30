package com.tripfortrip.tripfortrip._02_SpotSearch;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.GeoDataApi;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.GoogleMap;
import com.tripfortrip.tripfortrip.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity implements LocationListener {


    static final int MIN_TIME = 5000;
    static final float MIN_DIST = 0;
    LocationManager mgr;
    TextView txvLoc;
    TextView txvSetting;

    boolean isGPSEnabled;
    boolean isNetworkEnabled;

    Location myLocation;        // 儲存最近的定位資料
    Geocoder geocoder;          // 用來查詢地址的 Geocoder 物件
    EditText edtLat, edtLon;    // 經緯度輸入欄位




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        txvLoc = (TextView) findViewById(R.id.txvLoc);
        txvSetting = (TextView) findViewById(R.id.txvSetting);


        mgr = (LocationManager) getSystemService(LOCATION_SERVICE);

        checkPermission(); // 檢查若尚未授權，則向使用者要求定位權限


        edtLat = (EditText) findViewById(R.id.edtLan);
        edtLon = (EditText) findViewById(R.id.edtLon);
        geocoder = new Geocoder(this, Locale.getDefault());  // 建立 Geocoder 物件
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
                android.Manifest.permission.ACCESS_FINE_LOCATION)
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
                                MIN_TIME, MIN_DIST,this);
                        if (isNetworkEnabled){
                            mgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                                    MIN_TIME, MIN_DIST, this);
                        }
                    } else {
                        mgr.removeUpdates( this);
                    }
                }
            }
        }

    }

    // 檢查若尚未授權，則向使用者要求定位權限
    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},200);
        }
    }

    public void setup(View v) {
        Intent it =  // 使用Intent物件啟動"定位"設定程式
                new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(it);
    }

    public void getLocation(View v){
        if (myLocation != null){
            edtLat.setText(Double.toString(myLocation.getLatitude()));  // 將經度轉成字串
            edtLon.setText(Double.toString(myLocation.getLongitude())); // 將緯度轉成字串
        } else {
            txvLoc.setText("無法取得定位資料！");
        }
    }

    public void onQuery(View view){  // 用經緯度查地址"按鈕 onClick 事件"
        String strLat = edtLat.getText().toString(); // 取輸入的緯度字串
        String strLon = edtLon.getText().toString(); // 取輸入的經度字串
        if (strLat.length() == 0 || strLon.length() == 0){  //當字串為空白時 結束處理
            return;
        }
        txvLoc.setText("讀取中...");
        double latitude = Double.parseDouble(strLat); // 取得緯度值
        double longitute = Double.parseDouble(strLon);// 取得經度值

        String strAdrr = "";  // 用來建立所要顯示的訊息字串（地址字串）
        try {
            List<Address> listAddr = geocoder.getFromLocation(latitude, longitute, 1);
            // 用經緯度查地址
            // 只需要傳回1筆地址資料

            if (listAddr == null || listAddr.size() == 0){  // 檢查是否有取得地址
                strAdrr += "無法取得地址資料！";
            } else {                                        // 取 List 中的第一筆
                Address addr = listAddr.get(0);             // （也是唯一的一筆）
                for (int i = 0 ; i <= addr.getMaxAddressLineIndex() ; i++){
                    strAdrr += addr.getAddressLine(i) + "\n";
                    if (addr.getCountryCode() != null){
                        strAdrr += addr.getCountryCode();  // 查國碼
                    }
                }
            }
        } catch (Exception ex){
            strAdrr += "取得地址發生錯誤：" + ex.toString();
        }
        txvLoc.setText(strAdrr);
    }


    @Override
    public void onLocationChanged(Location location) {     // 位置變更事件

        myLocation = location;  // 儲存定位資料

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
