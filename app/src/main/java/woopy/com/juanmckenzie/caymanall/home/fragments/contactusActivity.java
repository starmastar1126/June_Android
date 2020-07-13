package woopy.com.juanmckenzie.caymanall.home.fragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import woopy.com.juanmckenzie.caymanall.Objects.User;
import woopy.com.juanmckenzie.caymanall.R;
import woopy.com.juanmckenzie.caymanall.common.activities.BaseActivity;
import woopy.com.juanmckenzie.caymanall.utils.Configs;

public class contactusActivity extends BaseActivity {


    EditText message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactus);
        message = findViewById(R.id.message);


        findViewById(R.id.ase_back_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.sendcontactusmesage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((message.getText().toString().equals(""))) {
                    Configs.simpleAlert(getString(R.string.please_enter_your_message), contactusActivity.this);
                    return;
                }

                showLoading();
                Configs configs = (Configs) getApplicationContext();
                ContactUS contactUS = new ContactUS();
                contactUS.setMessage(message.getText().toString());
                contactUS.setId(FirebaseDatabase.getInstance().getReference().child("contactus").getKey());
                contactUS.setUser(configs.getCurrentUser());
                FirebaseDatabase.getInstance().getReference().child("contactus").child(contactUS.getId()).setValue(contactUS).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        hideLoading();
                        AlertDialog.Builder alert = new AlertDialog.Builder(contactusActivity.this);
                        alert.setMessage(getString(R.string.thanks_for_contacting_us_we_wil))
                                .setTitle(R.string.app_name)
                                .setCancelable(false)
                                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                })
                                .setIcon(R.drawable.logo);
                        alert.create().show();
                    }
                });

            }
        });
    }

    public class ContactUS {
        private String message, id;
        private User user;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }
    }

}
