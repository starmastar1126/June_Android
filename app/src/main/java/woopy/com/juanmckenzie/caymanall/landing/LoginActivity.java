package woopy.com.juanmckenzie.caymanall.landing;

/*-------------------------------

    - woopy -

    Created by cubycode @2017
    All Rights reserved

-------------------------------*/

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;

import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import woopy.com.juanmckenzie.caymanall.common.activities.BaseActivity;
import woopy.com.juanmckenzie.caymanall.utils.Configs;
import woopy.com.juanmckenzie.caymanall.R;
import woopy.com.juanmckenzie.caymanall.home.activities.HomeActivity;

public class LoginActivity extends BaseActivity {

    /* Views */
    EditText usernameTxt;
    EditText passwordTxt;

    FirebaseAuth auth;
    Configs myapplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        // Hide Status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Init views
        usernameTxt = findViewById(R.id.usernameTxt);
        passwordTxt = findViewById(R.id.passwordTxt);
        usernameTxt.setTypeface(Configs.titSemibold);
        passwordTxt.setTypeface(Configs.titSemibold);

        myapplication = (Configs) getApplicationContext();
        TextView lTitleTxt = findViewById(R.id.loginTitleTxt);
        lTitleTxt.setTypeface(Configs.qsBold);

        // MARK: - LOGIN BUTTON ------------------------------------------------------------------
        Button loginButt = findViewById(R.id.loginButt);
        loginButt.setTypeface(Configs.titBlack);
        loginButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (usernameTxt.getText().toString().equals("")) {
                    Configs.simpleAlert(getString(R.string.please_enter_your_email), LoginActivity.this);
                    return;
                }
                if (passwordTxt.getText().toString().equals("")) {
                    Configs.simpleAlert(getString(R.string.please_enter_your_password), LoginActivity.this);
                    return;
                }

                showLoading();

                auth.signInWithEmailAndPassword(usernameTxt.getText().toString().trim(), passwordTxt.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            hideLoading();
                            myapplication.setSessionUserID(auth.getCurrentUser().getUid());
                            Intent homeIntent = new Intent(LoginActivity.this, HomeActivity.class);
                            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(homeIntent);
                        } else {
                            hideLoading();
                            Configs.simpleAlert(task.getException().getMessage(), LoginActivity.this);
                        }

                    }
                });
            }
        });

        // MARK: - SIGN UP BUTTON ------------------------------------------------------------------
        Button signupButt = findViewById(R.id.signUpButt);
        signupButt.setTypeface(Configs.titSemibold);
        signupButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUp.class));
            }
        });

        // MARK: - FORGOT PASSWORD BUTTON ------------------------------------------------------------------
        Button fpButt = findViewById(R.id.forgotPassButt);
        fpButt.setTypeface(Configs.titSemibold);
        fpButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                alert.setTitle(R.string.app_name);
                alert.setMessage(getString(R.string.invalid_email_address_messsage));
                // Add an EditTxt
                final EditText editTxt = new EditText(LoginActivity.this);
                editTxt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                alert.setView(editTxt)
                        .setNegativeButton(getString(R.string.cancel), null)
                        .setPositiveButton(getString(R.string.ok5), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                auth.sendPasswordResetEmail(editTxt.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Configs.simpleAlert(getString(R.string.we_sent_email_for_reset_password), LoginActivity.this);
                                        } else {
                                            Configs.simpleAlert(task.getException().getMessage(), LoginActivity.this);
                                        }
                                    }
                                });

                            }
                        });
                alert.show();

            }
        });

        // MARK: - DISMISS BUTTON ------------------------------------
        Button dismButt = findViewById(R.id.loginDismissButt);
        dismButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}

