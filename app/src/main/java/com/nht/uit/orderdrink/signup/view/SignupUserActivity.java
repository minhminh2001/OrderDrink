package com.nht.uit.orderdrink.signup.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.nht.uit.orderdrink.R;
import com.nht.uit.orderdrink.oop.BaseActivity;
import com.nht.uit.orderdrink.signup.presenter.SignUpActivityPresenter;
import com.nht.uit.orderdrink.utility.Constain;

import java.util.HashMap;

public class SignupUserActivity extends BaseActivity {

    private EditText edtEmail, edtPassword, edtConfirmPassword, edtPhoneNumber, edtUserName;
    private Button btnSignup;
    private SignUpActivityPresenter presenter;
    private double lo = 0.0;
    private double la = 0.0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set up notitle
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //set up full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup);
        addControls ();
        addEvents ();
    }

    private void addEvents() {
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp ();

            }
        });
    }

    private void signUp() {
        boolean isVail = true;
        String email = (edtEmail.getText().toString()).trim();
        String password = (edtPassword.getText().toString()).trim();
        String confirmPassword = (edtConfirmPassword.getText().toString()).trim();
        String phoneNumber = (edtPhoneNumber.getText().toString()).trim();
        String userName = (edtUserName.getText().toString()).trim();
        if (TextUtils.isEmpty(userName)){
            isVail = false;
            edtUserName.setError("Bắt Buộc");
        }
        else {
            edtUserName.setError(null);
        }
        if (TextUtils.isEmpty(email)){
            isVail = false;
            edtEmail.setError("Bắt Buộc");
        }
        else {
            edtEmail.setError(null);
        }
        if (!isEmailVail(email)){
            isVail = false;
            edtEmail.setError("Email không hợp lệ!");
        }
        else {
            edtEmail.setError(null);
        }
        if (TextUtils.isEmpty(phoneNumber)){
            isVail = false;
            edtPhoneNumber.setError("Bắt Buộc");
        }
        else {
            edtPhoneNumber.setError(null);
        }
        if (password.length() > 0 && password.length() <6){
            isVail = false;
            edtPassword.setText("");
            edtPassword.requestFocus();
            showToast("Mật khẩu phải ít nhất 6 ký tự");
        }
        if (TextUtils.isEmpty(password)){
            isVail = false;
            edtPassword.setError("Bắt Buộc");
        }
        else {
            edtPassword.setError(null);
        }
        if (!confirmPassword.equals(password)){
            isVail = false;
            edtConfirmPassword.setError("Mật khẩu không khớp");
        }
        else {
            edtConfirmPassword.setError(null);
        }
        if (isVail) {
            showProgressDialog();
            HashMap<String, Object> favorite_drink = new HashMap<>();
            HashMap<String , Object> location = new HashMap<>();
            location.put(Constain.ADDRESS, "");
            location.put(Constain.LO, lo);
            location.put(Constain.LA, la);
            presenter.signUpWithEmail(password, userName, email, phoneNumber, "", "", false, location, favorite_drink);
        }
    }

    private void addControls() {
        //EditText
        edtEmail = (EditText) findViewById(R.id.edtemail_signup);
        edtPhoneNumber = (EditText) findViewById(R.id.edtphonenumber_signup);
        edtPassword = (EditText) findViewById(R.id.edtpassword_signup);
        edtConfirmPassword = (EditText) findViewById(R.id.edtconfirmpassword_signup);
        edtUserName = (EditText) findViewById(R.id.edtusername_signup);
        //Button
        btnSignup = (Button) findViewById(R.id.btnsignup);
        //presenter
        presenter = new SignUpActivityPresenter(this);
    }
}
