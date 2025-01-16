package com.example.studenthostelleave;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StudentRegistration extends AppCompatActivity {

    EditText e1,e2,e3;
    ProgressBar loadingPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_registration);
        Button b1=findViewById(R.id.register);
        Button b2=findViewById(R.id.haveaccount);

        e1=findViewById(R.id.editText1);
        e2=findViewById(R.id.editText2);
        e3=findViewById(R.id.editText3);
        loadingPB = findViewById(R.id.idLoadingPB);


        Intent intent=new Intent(StudentRegistration.this, AddStudentProfile.class);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = e1.getText().toString();
                String pass = e2.getText().toString();
                String repass = e3.getText().toString();

                if (user.equals("") || pass.equals("") || repass.equals("")) {
                    Toast.makeText(StudentRegistration.this, "Fill All the Fields", Toast.LENGTH_SHORT).show();
                } else {

                    String regex = "^(.+)@(.+)$";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(user);
                    if (pass.equals(repass) && (pass.length()==8) && matcher.matches()) {
                        intent.putExtra("username",user);
                        intent.putExtra("passwd",pass);
                        intent.putExtra("c_passwd",repass);
                        startActivity(intent);

                        getSupportActionBar().setTitle("Hosteller");
                        finish();

                    } else if(!pass.equals(repass) && (pass.length()==8) && matcher.matches()){
                        Toast.makeText(StudentRegistration.this, "password and confirm password mismatched! ", Toast.LENGTH_SHORT).show();
                    }else if(pass.equals(repass) && !(pass.length()==8) && matcher.matches()){
                        Toast.makeText(StudentRegistration.this, "Password length must have 8 characters! ", Toast.LENGTH_SHORT).show();
                    }else if(pass.equals(repass) && (pass.length()==8) && !matcher.matches()){
                        Toast.makeText(StudentRegistration.this, "Invalid Username! ", Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(StudentRegistration.this, "Invalid Credentials! ", Toast.LENGTH_SHORT).show();

                    }
                }
            }

        });


        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(StudentRegistration.this, StudentLogin.class);
                startActivity(intent);
            }
        });
    }

}