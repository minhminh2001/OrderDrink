package com.nht.uit.orderdrink.Login.view;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nht.uit.orderdrink.Login.presenter.LoginFacebookPresenter;
import com.nht.uit.orderdrink.Login.presenter.LoginPresenter;
import com.nht.uit.orderdrink.R;
import com.nht.uit.orderdrink.main.view.MainActivity;
import com.nht.uit.orderdrink.oop.BaseActivity;
import com.nht.uit.orderdrink.reset_password.view.ResetPassword;
import com.nht.uit.orderdrink.signup.view.ChooseSignnup;
import com.facebook.CallbackManager;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText edtEmail, edtPassword;
    private TextView txtSignUp, txtForgotPassword;
    private Button btnLogin;
    private ImageView imgLoginFb, imgLoginGoogle;
    private LoginPresenter presenter;
    private LoginFacebookPresenter fbPresenter;
    private CallbackManager callbackManager;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        addControls();
        addEvents();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AlertDialog.Builder aler = new AlertDialog.Builder(this);
        aler.setCancelable(false);
        aler.setMessage("Bạn thực sự muốn thoát?");
        aler.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });
        aler.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        aler.create().show();
    }

    private void addEvents() {
        txtSignUp.setOnClickListener(this);
        txtForgotPassword.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        imgLoginFb.setOnClickListener(this);
        imgLoginGoogle.setOnClickListener(this);

    }

    private void addControls() {
        //Edittext
        edtEmail = (EditText) findViewById(R.id.edtemail_login);
        edtPassword = (EditText) findViewById(R.id.edtpassword_login);
        //TextView
        txtSignUp = (TextView) findViewById(R.id.txtsignup_login);
        txtForgotPassword = (TextView) findViewById(R.id.txtforgotpassword);
        //Button
        btnLogin = (Button) findViewById(R.id.btnlogin);
        //ImageView
        imgLoginFb = (ImageView) findViewById(R.id.imgloginfb);
        imgLoginGoogle = (ImageView) findViewById(R.id.imglogingoogle);
        //presenter
        presenter = new LoginPresenter(this);
        mAuth = FirebaseAuth.getInstance();
        fbPresenter = new LoginFacebookPresenter(this);
        callbackManager = CallbackManager.Factory.create();
    }

    @Override
    public void onClick(View v) {
        int view = v.getId();
        if (view == R.id.txtsignup_login) {
            sign_Up();
        }
        if (view == R.id.txtforgotpassword) {
            progressForgotPassword();
        }
        if (view == R.id.btnlogin) {
            logIn();
        }
        if (view == R.id.imgloginfb) {
            loginFb();

        }
        if (view == R.id.imglogingoogle) {
            logInGoogle();

        }

    }

    private void logInGoogle() {
    }

    private void loginFb() {
        fbPresenter.loginFacebook(this, callbackManager);
    }

    private void logIn() {
        boolean isVail = true;
        String email = (edtEmail.getText().toString()).trim();
        String password = (edtPassword.getText().toString()).trim();
        if (TextUtils.isEmpty(email)) {
            isVail = false;
            edtEmail.setError("Bắt Buộc");
        } else {
            edtEmail.setError(null);
        }
        if (TextUtils.isEmpty(password)) {
            isVail = false;
            edtPassword.setError("Bắt Buộc");
        } else {
            edtPassword.setError(null);
        }
        if (isVail) {
            showProgressDialog();
            presenter.login(email, password);
        }
    }

    private void progressForgotPassword() {
        startActivity(new Intent(this, ResetPassword.class));
    }

    private void sign_Up() {
        startActivity(new Intent(LoginActivity.this, ChooseSignnup.class));
    }

    public void moveToMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
