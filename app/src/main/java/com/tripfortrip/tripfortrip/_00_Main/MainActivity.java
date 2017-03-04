package com.tripfortrip.tripfortrip._00_Main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tripfortrip.tripfortrip.R;
import com.tripfortrip.tripfortrip._06_Member.AlertDialogFragment;
import com.tripfortrip.tripfortrip._06_Member.LoginActivity;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_LOGIN_OK = 100;
    private static final String TAG = "MainActivity";
    private boolean logon = false ;
    FirebaseAuth auth;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        login();

//        FragmentManager fragmentManager = getSupportFragmentManager();
//        new AlertDialogFragment("hi","hihi","ok","no").show(fragmentManager, "alert");
    }
    @Override
    protected void onStart() {
        super.onStart();
        user = auth.getCurrentUser();
        if (user !=null){
            Log.d(TAG, "UID:"+ user.getUid());
            Log.d(TAG, "email:"+ user.getEmail());
//           atabaseReference users =  FirebaseDatabase db = FirebaseDatabase.getInstance();
//            Ddb.getReference("users");
//            addFriend(user, users);
//            setNickname(user, users);

        }
    }

    private void login() {
        if(!logon) {
            if(user==null){
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, REQUEST_LOGIN_OK);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_LOGIN_OK){
            if(resultCode == RESULT_OK){
                String email = data.getStringExtra("uid");
                String password = data.getStringExtra("password");
                Log.d(TAG, email+"/"+password);
            }else {
                finish();
            }
        }

    }

    public void onLogoutClick(View view) {
        auth.signOut();
        SharedPreferences setting = getSharedPreferences(getString(R.string.pref_name),MODE_PRIVATE);
        setting.edit().putString(getString(R.string.pref_password),"").apply();
        user = auth.getCurrentUser();
        login();
    }
}
