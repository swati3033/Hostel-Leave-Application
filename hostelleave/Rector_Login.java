package com.example.hostelleave;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Rector_Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rector_login);

        Button b1;
        TextView t,passwd;

        b1=findViewById(R.id.login);
        t=findViewById(R.id.username);
        passwd=findViewById(R.id.password);

        Intent a1=new Intent(Rector_Login.this,Rectordrawer.class);


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(a1);
            }
        });


    }
}