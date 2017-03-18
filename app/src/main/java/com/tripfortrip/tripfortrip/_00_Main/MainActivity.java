package com.tripfortrip.tripfortrip._00_Main;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tripfortrip.tripfortrip.R;
import com.tripfortrip.tripfortrip._06_Member.AlertDialogFragment;
import com.tripfortrip.tripfortrip._06_Member.LoginActivity;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
/**
 //關於firebase登入
    private static final int REQUEST_LOGIN_OK = 100;
    private static final String TAG = "MainActivity";
    private boolean logon = false ;
    FirebaseAuth auth;
    private FirebaseUser user;
**/
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;//與DrawerLayout搭配使用的ActionBar控制物件
    private ImageView imageView;
    //private static final int REQUEST_TAKE_PICTURE_SMALL = 0;
    //private static final int REQUEST_TAKE_PICTURE_LARGE = 1;
    private static final int REQUEST_PICK_PICTURE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /**
         //關於firebase登入
        auth = FirebaseAuth.getInstance();
       login();
        **/
        imageView = (ImageView) findViewById(R.id.ivUser);//處理顯示照片
        setUpActionBar();
        initDrawer();
        initBody();

    }
    public void onPickPictureClick(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_PICK_PICTURE);
    }

    private final static int REQ_PERMISSIONS = 0;
    @Override
    protected void onStart() {
        super.onStart();
        /**
        //關於firebase登入
        user = auth.getCurrentUser();
        if (user !=null){
            Log.d(TAG, "UID:"+ user.getUid());
            Log.d(TAG, "email:"+ user.getEmail());
        }
         */

        askPermissions();
    }
    private void askPermissions() {
        String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE
        };

        Set<String> permissionsRequest = new HashSet<>();
        for (String permission : permissions) {
            int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                permissionsRequest.add(permission);
            }
        }

        if (!permissionsRequest.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    permissionsRequest.toArray(new String[permissionsRequest.size()]),
                    REQ_PERMISSIONS);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQ_PERMISSIONS:
                for (int result : grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        String text = getString(R.string.text_ShouldGrant);
                        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
                        finish();
                        return;
                    }
                }
                break;
        }
    }
    //選單
    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // home icon will keep still without calling syncState()
        actionBarDrawerToggle.syncState();
    }
    //取得ActionBar物件
    private void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//設定圖示
        }
    }

    private void initDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //建立與註冊ActionBarDrawerToggle監聽物件
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.text_Open, R.string.text_Close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        NavigationView view = (NavigationView) findViewById(R.id.navigation_view);
        //註冊Drawer選單項目點擊事件
        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();//關閉Drawer
                Fragment fragment;
                switch (menuItem.getItemId()) {
                    case R.id.item_myJourney:
                        initBody();
                        break;
                    case R.id.item_Scene:
                        fragment = new com.tripfortrip.tripfortrip._00_Main.SceneFragment();
                        switchFragment(fragment);
                        setTitle(R.string.text_Scene);
                        break;
//                    case R.id.item_Bag:  //我的背包
//                        fragment = new com.tripfortrip.tripfortrip._00_Main.BagFragment();
//                        switchFragment(fragment);
//                        setTitle(R.string.text_Bag);
//                        break;
                    case R.id.item_EmergencyButton:
                        fragment =new com.tripfortrip.tripfortrip._00_Main.EmergencyButtonFragment();
                        switchFragment(fragment);
                        setTitle(R.string.text_EmergencyButton);
                        break;
//                    case R.id.item_Share:     //ＡＰＰ分享
//                        fragment = new com.tripfortrip.tripfortrip._00_Main.ShareFragment();
//                        switchFragment(fragment);
//                        setTitle(R.string.text_Share);
//                        break;
                    case R.id.item_About:
                        fragment = new com.tripfortrip.tripfortrip._00_Main.AboutFragment();
                        switchFragment(fragment);
                        setTitle(R.string.text_About);
                        break;
                    case R.id.drawer_settings:  //會員專區
                        fragment = new com.tripfortrip.tripfortrip._00_Main.SettingsFragment();
                        switchFragment(fragment);
                        setTitle(R.string.text_Settings);
                        break;
                    default:
                        initBody();
                        break;
                }
                return true;
            }
        });
    }

    private void initBody() {
        Fragment fragment = new com.tripfortrip.tripfortrip._00_Main.HomeFragment();
        switchFragment(fragment);
        setTitle(R.string.text_myJourney);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void switchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.body, fragment);
        fragmentTransaction.commit();
    }
    /**
     //關於firebase登入
    private void login() {
        if(!logon) {
            if(user==null){
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, REQUEST_LOGIN_OK);
            }
        }
    }
    **/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         //關於firebase登入
        if(requestCode == REQUEST_LOGIN_OK){
            if(resultCode == RESULT_OK){
                String email = data.getStringExtra("uid");
                String password = data.getStringExtra("password");
                Log.d(TAG, email+"/"+password);
            }else {
                finish();
            }
        }
         *
         */
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_PICK_PICTURE:
                    Uri uri = data.getData();
                    String[] columns = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(uri, columns,
                            null, null, null);
                    if (cursor.moveToFirst()) {
                        String imagePath = cursor.getString(0);
                        cursor.close();
                        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                        imageView.setImageBitmap(bitmap);
                    }
                    break;
            }
        }

    }
    /**
      //關於firebase登入
    public void onLogoutClick(View view) {
        auth.signOut();
        SharedPreferences setting = getSharedPreferences(getString(R.string.pref_name),MODE_PRIVATE);
        setting.edit().putString(getString(R.string.pref_password),"").apply();
        user = auth.getCurrentUser();
        login();
    }
     **/
}
