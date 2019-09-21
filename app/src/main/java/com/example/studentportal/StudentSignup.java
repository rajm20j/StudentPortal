package com.example.studentportal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StudentSignup extends AppCompatActivity {
    private EditText Password,RePassword,Name,Email,Contact,Hostel,Roll,Branch;
    private Button Register;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth,firebaseAuth;
    private FirebaseAuth mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_signup);

        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Password = (EditText)findViewById(R.id.signup_password);
        RePassword = (EditText)findViewById(R.id.signup_repassword);
        Name = (EditText)findViewById(R.id.signup_name);
        Contact = (EditText)findViewById(R.id.signup_contact);
        Email = (EditText)findViewById(R.id.signup_email);
        Hostel = (EditText)findViewById(R.id.signup_hostel);
        Roll = (EditText)findViewById(R.id.signup_roll);
        Branch = (EditText) findViewById(R.id.signup_branch);

        Register = (Button) findViewById(R.id.signup_register);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Userinfo");
        mAuth = FirebaseAuth.getInstance();

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUser();
            }
        });
    }
    private void addUser()
    {
        String password = Password.getText().toString().trim();
        String repassword = RePassword.getText().toString().trim();
        final String name = Name.getText().toString().trim();
        final String branch = Branch.getText().toString().trim();
        final String email = Email.getText().toString().trim();
        final String roll = Roll.getText().toString().trim();
        final String contact = Contact.getText().toString().trim();
        final String hostel= Hostel.getText().toString().trim();

        if(name.isEmpty()){
            Name.setError("Name is Required");
            Name.requestFocus();
        }

        if(email.isEmpty())
        {
            Email.setError("Email is Required");
            Email.requestFocus();
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            Email.setError("Invalid Email");
            Email.requestFocus();
        }

        if(branch.isEmpty())
        {
            Branch.setError("Branch is Required");
            Branch.requestFocus();
        }

        if(contact.isEmpty())
        {
            Contact.setError("Contact is Required");
            Contact.requestFocus();
        }

        if(contact.length() != 10)
        {
            Contact.setError("Invalid Contact");
            Contact.requestFocus();
        }

        if(hostel.isEmpty())
        {
            Hostel.setError("Hostel is Required");
            Hostel.requestFocus();
        }

        if(roll.isEmpty())
        {
            Roll.setError("Roll Number is Required");
            Roll.requestFocus();
        }

        if(password.isEmpty())
        {
            Password.setError("Password is Required");
            Password.requestFocus();
        }

        if(password.length() < 8)
        {
            Password.setError("Minimum Length required");
            Password.requestFocus();
        }

        if(repassword.isEmpty())
        {
            RePassword.setError("Password is Required");
            RePassword.requestFocus();
        }

        if(repassword.length() < 8)
        {
            RePassword.setError("Minimum Length required");
            RePassword.requestFocus();
        }

        if(!password.equals(repassword))
        {
            RePassword.setError("Passwords do not match");
            RePassword.requestFocus();
        }

        final UserInfo userInfo = new UserInfo(name,email,branch,hostel,roll,contact);

        Toast.makeText(getApplicationContext(),"Hey There",Toast.LENGTH_LONG).show();
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Log.d("TASK SUCCESS","####"+mAuth.getCurrentUser().getUid());
                    myRef.child(mAuth.getCurrentUser().getUid()).child("Info").setValue(userInfo);
                    Toast.makeText(getApplicationContext(), "Registered Successfully..", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    intent.putExtra("Name",name);
//                    intent.putExtra("Branch",branch);
//                    intent.putExtra("Roll",roll);
//                    intent.putExtra("Contact",contact);
//                    intent.putExtra("Hostel",hostel);
                    startActivity(intent);
                    finish();
                }
                else
                {

                    Log.d("TASK FAILED","#####");
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "Email is already registered", Toast.LENGTH_LONG).show();
                    } else if (task.getException() instanceof FirebaseAuthWeakPasswordException) {
                        Toast.makeText(getApplicationContext(), "Password is too weak", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}
