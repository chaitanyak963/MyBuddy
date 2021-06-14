package com.example.finalproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AuthenticationActivity extends AppCompatActivity {
    LinearLayout loginlay, regsiterlay;
    EditText umail, upass, rumail, rupass, rurepass, runame;
    FirebaseAuth auth;
    DatabaseReference reference;
    ProgressDialog progress;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        loginlay = findViewById(R.id.login_layout);
        umail = findViewById(R.id.umail);
        upass = findViewById(R.id.upass);
        rumail = findViewById(R.id.rumail);
        rupass = findViewById(R.id.rupass);
        rurepass = findViewById(R.id.rurepass);
        runame = findViewById(R.id.runame);
        preferences = getSharedPreferences("id",MODE_PRIVATE);
        progress = new ProgressDialog(this);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(AuthenticationActivity.this, SecondActivity.class));
            finish();
        }
        regsiterlay = findViewById(R.id.regsiter_layout);

    }

    public void signup(View view) {
        regsiterlay.setVisibility(LinearLayout.VISIBLE);
        loginlay.setVisibility(LinearLayout.GONE);
    }

    public void login(View view) {
        regsiterlay.setVisibility(LinearLayout.GONE);
        loginlay.setVisibility(LinearLayout.VISIBLE);
    }

    public void logins(View view) {
        String mail = umail.getText().toString().trim();
        String pass = upass.getText().toString().trim();
        if (isConnected()) {
            if (mail.isEmpty() && pass.isEmpty()) {
                Toast.makeText(this, "Please fill all the details", Toast.LENGTH_SHORT).show();
            }
           else {
                progress.setMessage(getString(R.string.wait));
                progress.show();
                auth.signInWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progress.dismiss();
                        if (task.isSuccessful()) {
                            startActivity(new Intent(AuthenticationActivity.this, SecondActivity.class));
                            finish();
                        } else {
                            Toast.makeText(AuthenticationActivity.this, "Please Verify Your Credentials", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
        else{
            Toast.makeText(this, "Please turn on your internet", Toast.LENGTH_SHORT).show();
        }
    }

    public void signin(View view) {
        final String rmail, rpass, repass, name;
        rmail = rumail.getText().toString().trim();
        rpass = rupass.getText().toString().trim();
        repass = rurepass.getText().toString().trim();
        name = runame.getText().toString().trim();
        if (isConnected()) {
            if (rmail.isEmpty() || rpass.isEmpty() || repass.isEmpty() || name.isEmpty()) {
                Toast.makeText(this, "Please fill all the details", Toast.LENGTH_SHORT).show();
            } else if (rpass.length() < 6) {
                Toast.makeText(this, "Password length must be 6 digits", Toast.LENGTH_SHORT).show();
            } else if (!rpass.equals(repass)) {
                Toast.makeText(this, "Password and Re-Password must be same", Toast.LENGTH_SHORT).show();
            } else {
                progress.setMessage(getString(R.string.register));
                progress.show();
                auth.createUserWithEmailAndPassword(rmail, rpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                           /* reference = FirebaseDatabase.getInstance().getReference("Users");
                            FirebaseUser user = auth.getCurrentUser();
                            assert user != null;
                            String uploadId = reference.push().getKey();
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("uname",name);
                            editor.putString("uid",uploadId);
                            editor.commit();
                            Upload upload = new Upload(uploadId, name, "offline");
                            reference.child(uploadId).setValue(upload);
                            Intent i = new Intent(AuthenticationActivity.this, SecondActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            finish();*/
                                FirebaseUser firebaseUser = auth.getCurrentUser();
                                assert firebaseUser != null;
                                String userid = firebaseUser.getUid();

                                reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("id", userid);
                                hashMap.put("username",name);
                                hashMap.put("imageURL", "default");
                                hashMap.put("status", "offline");
                                hashMap.put("search", name.toLowerCase());

                                reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Intent intent = new Intent(AuthenticationActivity.this, SecondActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                });
                        } else {
                            Toast.makeText(AuthenticationActivity.this, "Please verify your credentials", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
        else{
            Toast.makeText(this, "Please turn on your internet", Toast.LENGTH_SHORT).show();
        }
    }

    public void reset(View view) {
        if (isConnected()) {
            final EditText email;
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            View v = inflater.inflate(R.layout.activity_reset, null);
            builder.setCancelable(false);
            builder.setView(v);
            email = v.findViewById(R.id.rmail);
            builder.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, int which) {
                    if (isConnected()) {
                        String rmail = email.getText().toString().trim();
                        if (TextUtils.isEmpty(rmail)) {
                            Toast.makeText(AuthenticationActivity.this, getString(R.string.email), Toast.LENGTH_SHORT).show();
                        } else {
                            auth.sendPasswordResetEmail(rmail).addOnCompleteListener(AuthenticationActivity.this, new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(AuthenticationActivity.this, getString(R.string.passwordreset), Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    } else {
                                        Toast.makeText(AuthenticationActivity.this, getString(R.string.failedreset), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    } else {
                        Toast.makeText(AuthenticationActivity.this, getString(R.string.internet), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }
        else{
            Toast.makeText(this, "Please turn on your internet", Toast.LENGTH_SHORT).show();
        }
    }


    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = manager.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {


        }
        return connected;
    }
}
