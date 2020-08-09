package com.example.loginapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

//import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    EditText mTextUsername;
    EditText mTextPassword;
    EditText mTextEmail;
    Button mButtonRegister;
    TextView mTextViewLogin;
    //ImageView mImageProfile;
    private FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firebaseAuth = FirebaseAuth.getInstance();

        //mImageProfile = (ImageView) findViewById(R.id.imageview_profile);
        mTextUsername = (EditText) findViewById(R.id.edittext_username);
        mTextPassword = (EditText) findViewById(R.id.edittext_password);
        mTextEmail = (EditText) findViewById(R.id.edittext_email);
        mButtonRegister = (Button) findViewById(R.id.button_register);
        mTextViewLogin = (TextView) findViewById(R.id.textview_login);

        progressDialog = new ProgressDialog(this);

        mTextViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(loginIntent);
            }
        });

        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()) {
                    String user_email = mTextEmail.getText().toString().trim();
                    String user_pwd = mTextPassword.getText().toString().trim();

                    firebaseAuth.createUserWithEmailAndPassword(user_email,user_pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                               sendEmailVerification();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            }

        });

    }


    private Boolean validate() {
        Boolean result = false;
        String user = mTextUsername.getText().toString().trim();
        String pwd = mTextPassword.getText().toString().trim();
        String email = mTextEmail.getText().toString().trim();

        if (user.isEmpty() || pwd.isEmpty() || email.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Please enter all the details ", Toast.LENGTH_SHORT).show();
        } else {
            result = true;
        }

        return result;

    }

    private void sendEmailVerification() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        progressDialog.setMessage("Registering user");
        progressDialog.show();

        if(firebaseUser!=null) {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this,"Successfully Registered , Verification mail sent!",Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this,"Verification mail not sent",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}