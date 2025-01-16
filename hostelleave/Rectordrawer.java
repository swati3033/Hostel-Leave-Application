package com.example.hostelleave;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Rectordrawer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rectordrawer);

        ImageButton add,ap,del,log;

        add=findViewById(R.id.add);
        ap=findViewById(R.id.Approve);
        del=findViewById(R.id.remove);
        log=findViewById(R.id.logout);

        Intent i=new Intent(Rectordrawer.this,Rector_Login.class);
        Intent ai=new Intent(Rectordrawer.this,AddStudent.class);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ai);
            }
        });

        ap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i);
            }
        });
    }
}