package com.dut.moneytracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dut.moneytracker.R;
import com.dut.moneytracker.models.AppPreferences;
import com.dut.moneytracker.utils.DialogUtils;
import com.dut.moneytracker.utils.NetworkUtils;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 21/02/2017.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener, FirebaseAuth.AuthStateListener {
    private static final String[] PERMISSION_FB = {"email", "public_profile", "user_posts", "user_location"};
    private static final int RC_SIGN_IN = 1;
    private static final String TAG = LoginActivity.class.getSimpleName();
    private Button btnLoginWithGoogle;
    private Button btnLoginWithFacebook;
    private Button btnLoginWithEmail;
    private CallbackManager mCallbackManager;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mFireBaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        configFireBase();
        configGoogleApi();
    }

    private void configFireBase() {
        mFireBaseAuth = FirebaseAuth.getInstance();
    }

    private void initView() {
        btnLoginWithEmail = (Button) findViewById(R.id.btnLoginWithEmail);
        btnLoginWithEmail.setOnClickListener(this);
        btnLoginWithGoogle = (Button) findViewById(R.id.btnLoginWithGoogle);
        btnLoginWithGoogle.setOnClickListener(this);
        btnLoginWithFacebook = (Button) findViewById(R.id.btnLoginWithFacebook);
        btnLoginWithFacebook.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        onLoginFacebook();
        mFireBaseAuth.addAuthStateListener(this);
    }

    @Override
    public void onClick(View v) {
        if (!NetworkUtils.getInstance().isConnectNetwork(this)) {
            Toast.makeText(this, R.string.toast_text_connection_internet, Toast.LENGTH_SHORT).show();
            return;
        }
        switch (v.getId()) {
            case R.id.btnLoginWithEmail:
                startActivity(new Intent(this, LoginMailActivity.class));
                break;
            case R.id.btnLoginWithFacebook:
                requestLoginFacebook();
                break;
            case R.id.btnLoginWithGoogle:
                requestLoginWithGoogle();
                break;
        }
    }

    private void onLoginFacebook() {
        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                fireBaseAuthWthFacebook(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }

    private void requestLoginFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList(PERMISSION_FB));
    }

    private void configGoogleApi() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void requestLoginWithGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                fireBaseAuthWithGoogle(account);
            }
        }
    }

    private void fireBaseAuthWithGoogle(GoogleSignInAccount account) {
        DialogUtils.getInstance().showProgressDialog(this, getString(R.string.dialog_messenger_connect));
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mFireBaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        DialogUtils.getInstance().dismissProgressDialog();
                    }
                });
    }

    private void fireBaseAuthWthFacebook(AccessToken accessToken) {
        DialogUtils.getInstance().showProgressDialog(this, getString(R.string.dialog_messenger_connect));
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mFireBaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        DialogUtils.getInstance().dismissProgressDialog();
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mFireBaseAuth != null) {
            mFireBaseAuth.removeAuthStateListener(this);
        }
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            logOutGoogle();
            syncDataFromServer(user);
        } else {
            Log.d(TAG, "onAuthStateChanged:signed_out");
        }
    }

    private void syncDataFromServer(FirebaseUser firebaseUser) {
        String currentUserId = AppPreferences.getInstance().getCurrentUserId(this);
        Log.d(TAG, "syncDataFromServer: " + currentUserId + "   " + firebaseUser.getUid());
        if (!TextUtils.equals(currentUserId, firebaseUser.getUid())) {
            startActivity(new Intent(this, ActivityLoadData.class));
        } else {
            startActivity(new Intent(this, MainActivity.class));
        }
        finish();
    }

    private void logOutGoogle() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        }
    }
}
