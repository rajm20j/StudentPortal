package com.example.studentportal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private EditText email,pass;
    private Button forgot_password,login,noaccount;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Login");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        radioGroup = (RadioGroup) findViewById(R.id.login_radiogroup);
        email = (EditText) findViewById(R.id.login_email);
        pass = (EditText) findViewById(R.id.login_pass);
        login = (Button) findViewById(R.id.login_btn);
        forgot_password = (Button) findViewById(R.id.login_forgotpassword);
        noaccount = (Button) findViewById(R.id.login_noaccount);
        auth = FirebaseAuth.getInstance();

        noaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),StudentSignup.class));
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });
    }

    public void loginUser()
    {
        final String username = email.getText().toString().trim();
        final String password = pass.getText().toString().trim();

        if(username.isEmpty()){

            email.setError("Email is Required");
            email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(username).matches()){

            email.setError("Enter a valid Email Address");
            email.requestFocus();
            return;
        }


        if(password.isEmpty()){

            pass.setError("Password is Required");
            pass.requestFocus();
            return;
        }

        if(password.length()<8){
            pass.setError("Minimum Password Length is 8.");
            pass.requestFocus();
            return;
        }

        auth.signInWithEmailAndPassword(username,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful())
                        {
                            radioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
                            Log.d("Client dekho","####"+radioButton.getText().toString());
                            SharedPreferences sharedPref=getSharedPreferences("MyData", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor=sharedPref.edit();
                            editor.putString("Email",username);
                            editor.putString("Password",password);
                            editor.putString("Client",radioButton.getText().toString());
                            editor.commit();

                            if(radioButton.getText().toString().equals("Student"))
                            {
                                Intent intent =new Intent(getApplicationContext(),StudentProfile.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
                                Intent intent = new Intent(getApplicationContext(),StaffProfile.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Login Failed",Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    public void Client(View v)
    {
        //Toast.makeText(getApplicationContext(),"Hey Sejal",Toast.LENGTH_SHORT).show();
        radioButton=(RadioButton)findViewById(radioGroup.getCheckedRadioButtonId());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
