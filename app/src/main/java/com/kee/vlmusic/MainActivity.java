package com.kee.vlmusic;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class   MainActivity extends AppCompatActivity {
    private EditText et_email,et_pass;
    private Button btn_login;
    private  TextView tv_signup;
    private String email="",password="";
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    FirebaseUser user;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_email=(EditText) findViewById(R.id.et_email1);
        et_pass=(EditText)findViewById(R.id.et_password);
        btn_login =(Button)findViewById(R.id.btn_login);
        tv_signup=(TextView)findViewById(R.id.tv_signup);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        //check if user already logged in
        if(firebaseAuth.getCurrentUser() != null)
        {

            startActivity(new Intent(MainActivity.this,MusicActivity.class));

        }


        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SignUp.class));
            }
        });


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = et_email.getText().toString().trim();
                password = et_pass.getText().toString().trim();
                if(email.isEmpty() || password.isEmpty())
                {
                    Toast.makeText(MainActivity.this,"both fields are compulsury",Toast.LENGTH_SHORT).show();
                }else {
                    progressDialog.setMessage("Logging In..");
                    progressDialog.show();
                    userAuthentication(email, password);
                }
            }
        });

    }

    public void userAuthentication(final String email, String password){

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()) {

                    progressDialog.dismiss();
                    checkuserexist();
                    String temp = firebaseAuth.getCurrentUser().getUid().toString();
                    startActivity(new Intent(getBaseContext(),MusicActivity.class));

                }else {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this,"Incorrect email or password..",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void checkuserexist() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("user info");
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        final String userid = firebaseAuth.getUid().toString();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(userid) && user.isEmailVerified()){

                    Intent intent = new Intent(MainActivity.this, MusicActivity.class);
                    intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                    finish();
                    startActivity(intent);


                }else {
                    Toast.makeText(MainActivity.this,"you need to sign up",Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void onBackPressed() {

        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

}