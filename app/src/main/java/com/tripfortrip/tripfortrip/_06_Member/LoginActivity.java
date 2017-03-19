package com.tripfortrip.tripfortrip._06_Member;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.ProviderQueryResult;
import com.tripfortrip.tripfortrip.R;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity
        implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener {

    LoginButton loginButton;  // facebook
    CallbackManager callbackManager;  // facebook

    private static final int RC_SIGN_IN = 420;


    private static final String TAG = "LoginActivity";
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
            //登入成功時會呼叫onAuthStateChanged
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                uid = user.getUid();
                Log.d("UID", uid);
//             將登入成功的使用者帳號密碼記起來準備下次自動登入使用
                settingPref.edit()
                        .putString(getString(R.string.pref_uid), uid)
                        .putString(getString(R.string.pref_email), email)
                        .putString(getString(R.string.pref_password), password)
                        .apply();
                Toast.makeText(LoginActivity.this, "登入成功，歡迎" + auth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
                Intent intent = getIntent();
                intent.putExtra("uid", email);
                intent.putExtra("password", password);
                setResult(RESULT_OK, intent);
                finish();

            }
        }
    };
    private String uid,email,password;
    private SharedPreferences settingPref;
    private EditText edEmail,edPassword;
    private List edList;

    //google
    GoogleSignInOptions gso;
    GoogleApiClient googleApiClient;
    private static final int REQUEST_GOOGLE_SING_IN = 100;
    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;
    private SignInButton btnSignIn;
    private Button btnSignOut;
    private LinearLayout llProfileLayout;
    private ImageView imgProfilePic;
    private TextView txtName, txtEmail;

//    private CallbackManager callbackManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());  // facebook
        setContentView(R.layout.activity_login);
        initializeControls();  // google
        initializeGPlusSettings();  // google

        loginButton = (LoginButton) findViewById(R.id.login_button);  // facebook
        callbackManager = CallbackManager.Factory.create();  //  facebook

        loginButton.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Toast.makeText(LoginActivity.this, "歡迎使用",
                                Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(LoginActivity.this,
                                "Login Canceled!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException error) {

                    }
                });

        //使用Firebase會員認證，取得實體
        auth = FirebaseAuth.getInstance();
        //設定記住帳號密碼，自動登入//如果帳號密碼不等於空值就直接登入上次登入的使用者
//        edEmail.setText(settingPref.getString(getString(R.string.pref_email),""));
        autoLogin();
        edEmail = (EditText) findViewById(R.id.edEmail);
        edPassword = (EditText) findViewById(R.id.edPasswd);
        edList = new ArrayList();

        //google
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.web_application_id))
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();
    }




    private void initializeGPlusSettings() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        btnSignIn.setSize(SignInButton.SIZE_STANDARD);
        btnSignIn.setScopes(gso.getScopeArray());
    }

    private void initializeControls() {
        btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);
        btnSignOut = (Button) findViewById(R.id.btn_sign_out);
        llProfileLayout = (LinearLayout) findViewById(R.id.llProfile);
        imgProfilePic = (ImageView) findViewById(R.id.imgProfilePic);
        txtName = (TextView) findViewById(R.id.txtName);
        txtEmail = (TextView) findViewById(R.id.txtEmail);

        btnSignIn.setOnClickListener(this);
        btnSignOut.setOnClickListener(this);
    }


    private void autoLogin() {
        //設定記住帳號密碼，自動登入//如果密碼不等於空值就直接登入上次登入的使用者
        settingPref = getSharedPreferences(getString(R.string.pref_name), MODE_PRIVATE);
        email = settingPref.getString(getString(R.string.pref_email), "");
        password = settingPref.getString(getString(R.string.pref_password), "");
        //如果帳號密碼不等於空值就直接登入上次登入的使用者
        if (!password.equals("")) {
            Log.d("SharedPreferences:", email + "/" + password);
            loginWithEmailAndPassword();
        }
    }

    public void onLoginClick(View view) {
        email = edEmail.getText().toString();
        password = edPassword.getText().toString();
        if(email.trim().length() != 0  && !email.equals("") ) {
            if(password.trim().length() != 0  && !password.equals("") ) {
                loginWithEmailAndPassword();
                edList.add(edEmail);
                edList.add(edPassword);
            }else {
                Toast.makeText(this,"請輸入密碼",Toast.LENGTH_SHORT).show();
            }
        }else if(password.trim().length() != 0  && !password.equals("") ){
            Toast.makeText(this,"請輸入帳號",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this,"請輸入帳號密碼",Toast.LENGTH_SHORT).show();
        }
//        Log.d("User:", email + "/" + password);

    }


    private void loginWithEmailAndPassword() {
        //使用申請本ＡＰＰ的email與密碼登入
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            public static final String TAG = "signInWithEmail";

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                // If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.
                //登入成功時會呼叫onAuthStateChanged所以只要寫登入失敗就好
                if (!task.isSuccessful()) {
                    auth.fetchProvidersForEmail(email).addOnSuccessListener(new OnSuccessListener<ProviderQueryResult>() {
                        @Override
                        public void onSuccess(ProviderQueryResult providerQueryResult) {
                            Log.d("has email", providerQueryResult.getProviders() + "");
                            //如果有帳號，providerQueryResult.getProviders()會得[password]
                            //如果找不到會得空值，所以寫判斷List是否等於1
                            List<String> emailCheck = providerQueryResult.getProviders();
                            if (emailCheck.size() == 1) {
                                wrongPasswdDialog();
                            } else {
                                //如果沒有帳號，跳出視窗詢問是否新增帳號
                                Toast.makeText(LoginActivity.this, "登入錯誤",
                                        Toast.LENGTH_SHORT).show();
                                noEmailDialog();
                            }
                        }

                        private void wrongPasswdDialog() {
                            new AlertDialogFragment(getString(R.string.wrongPasswd), getString(R.string.wrongPasswd),
                                    getString(R.string.again), null, getString(R.string.lostPasswd), edList, LoginActivity.this)
                                    .show(getSupportFragmentManager(), "alert");
                        }

                        private void noEmailDialog() {
                            new AlertDialogFragment("查無此帳號", "是否新增此帳號？", "註冊此帳號", "返回", null, edList, LoginActivity.this).show(getSupportFragmentManager(), "Alert");
                        }
                    });
                }
            }
        });
    }

    //啟動時加入登入監聽
    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleGPlusSignInResult(result);
        } else {
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleGPlusSignInResult(googleSignInResult);
                }
            });
        }
    }

    //關閉 LoginActivity 時解除登入監聽
    @Override
    protected void onStop() {
        super.onStop();
        auth.removeAuthStateListener(authStateListener);
    }
    //Google登入
    public void onGoogleClick(View view) {
        Intent signItent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signItent,REQUEST_GOOGLE_SING_IN);
    }
    //Facebook登入
    public void onFacebookClick(View view) {
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleGPlusSignInResult(result);
        }

        if(requestCode == REQUEST_GOOGLE_SING_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            System.out.print(TAG+"/"+ result.toString());

            if(result.isSuccess()){
                GoogleSignInAccount googleAccount = result.getSignInAccount();

                System.out.print(TAG+"/"+ googleAccount.getEmail().toString());
                AuthCredential credential  = GoogleAuthProvider.getCredential(googleAccount.getIdToken(),null);
                auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        finish();
                    }
                });
            }
        }
    }


    //implements GoogleApiClient.OnConnectionFailedListener
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG,"onConnectionFailed");
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_sign_in:
                signIn();
                break;
            case R.id.btn_sign_out:
                signOut();
                break;
        }
    }

    private void signOut() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signIn() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI(false);
                    }
                });
    }

    private void handleGPlusSignInResult(GoogleSignInResult result) {
        Log.d(TAG,"handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()){
            GoogleSignInAccount acct = result.getSignInAccount();

            String personName = acct.getDisplayName();
            String personPhotoUrl = acct.getPhotoUrl().toString();
            String email = acct.getEmail();
            String familyName = acct.getFamilyName();

            Log.e(TAG, "Name: " + personName +
                    ", email: " + email +
                    ", Image: " + personPhotoUrl +
                    ", Family Name: " + familyName);

            txtName.setText(personName);
            txtEmail.setText(email);


            Glide.with(getApplicationContext()).load(personPhotoUrl)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgProfilePic);

            updateUI(true);
        } else {
            updateUI(false);
        }
    }

    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            btnSignIn.setVisibility(View.GONE);
            btnSignOut.setVisibility(View.VISIBLE);
            llProfileLayout.setVisibility(View.VISIBLE);
        } else {
            btnSignIn.setVisibility(View.VISIBLE);
            btnSignOut.setVisibility(View.GONE);
            llProfileLayout.setVisibility(View.GONE);
        }
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    //AlertDialogFragment用來產生AlertDialog
    public static class AlertDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {
        private String title, message, positiveButton, negativeButton, neutralButton;
        private int icon = R.drawable.default_alert;
        private List edlist;
        private Context context;

        public void setIcon(int icon) {
            this.icon = icon;
        }

        public AlertDialogFragment(String title, String message, String positiveButton, String negativeButton, String neutralButton, List edList, Context context) {
            this.title = title;
            this.message = message;
            this.positiveButton = positiveButton;
            this.negativeButton = negativeButton;
            this.neutralButton = neutralButton;
            this.edlist = edList;
            this.context = context;

        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new android.app.AlertDialog.Builder(getActivity())
                    .setTitle(title)
                    .setIcon(icon)
                    .setMessage(message)
                    .setPositiveButton(positiveButton, this)
                    .setNegativeButton(negativeButton, this)
                    .setNeutralButton(neutralButton, this)
                    .create();
        }

        //實作DialogInterface.OnClickListener的onClick方法
        @Override
        public void onClick(DialogInterface dialog, int which) {
            final LoginActivity loginActivity = (LoginActivity) context;
            EditText edEmail = (EditText) edlist.get(0);
            EditText edPassword = (EditText) edlist.get(1);
            String email = edEmail.getText().toString();
            String password = edPassword.getText().toString();
            FirebaseAuth auth = FirebaseAuth.getInstance();

            switch (which) {
                //press BUTTON_POSITIVE
                case DialogInterface.BUTTON_POSITIVE:
                    //密碼錯誤重新輸入
                    if (title.equals(getString(R.string.wrongPasswd))) {
                        edPassword.setFocusable(true);
                        edPassword.setText("");
                        break;
                    } else if (title.equals("確定重設密碼？")) {
                        //重新設定密碼
                        auth.sendPasswordResetEmail(email);
                        edPassword.setFocusable(true);
                        edPassword.setText("");
                        Toast.makeText(getActivity(), "密碼更改信已寄出，請至信箱確認", Toast.LENGTH_LONG).show();
                        break;
                    } else if (title.equals("查無此帳號")) {
                        //註冊此帳號
                        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(loginActivity, new OnCompleteListener<AuthResult>() {
                            public static final String TAG = "createUserWithEmail";

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(loginActivity, "新增失敗",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                                    loginActivity.loginWithEmailAndPassword();
                                }
                            }
                        });
                        break;
                    }
                    break;

                //press BUTTON_NEGATIVE
                case DialogInterface.BUTTON_NEGATIVE:

                    break;
                //press BUTTON_NEUTRAL
                case DialogInterface.BUTTON_NEUTRAL:
                    if (title.equals(getString(R.string.wrongPasswd))) {
                        new AlertDialogFragment("確定重設密碼？", "按下確定後系統會自動發出重設密碼認證到您的信箱", "確定", null, null, edlist, context).show(loginActivity.getSupportFragmentManager(), "alert");
                    }
                    break;

                default:
                    break;
            }
        }
    }
}