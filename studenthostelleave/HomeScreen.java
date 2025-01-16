package com.example.studenthostelleave;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HomeScreen extends AppCompatActivity {

    Button b1, b2, b3, b4;
    TextView Student_name;
    private String prn, name,hroom,hname;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        b1 = findViewById(R.id.profile);
        b2 = findViewById(R.id.holeave);
        b3 = findViewById(R.id.hoapprove);
        b4 = findViewById(R.id.hologout);
        Student_name=findViewById(R.id.t1);

        String userid = getIntent().getStringExtra("userid");
        fetchStudentData(userid);

        Student_name.setText("Welcome "+name);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreen.this, StudentProfile.class);
                intent.putExtra("userid", userid);
                startActivity(intent);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreen.this, StudentLeaveApplication.class);
                intent.putExtra("prn", prn);
                intent.putExtra("studentName", name);
                intent.putExtra("Room", hroom);
                intent.putExtra("Hostel",hname);
                startActivity(intent);
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreen.this, LeaveActivity.class);
                startActivity(intent);
            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreen.this, StudentLogin.class);
                startActivity(intent);
            }
        });
    }

    private void fetchStudentData(String userid) {
        String url = "http://192.168.43.20/hostel_management/fetchDataForLeave.php?userid=" + userid;

        RequestQueue queue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<>();
        params.put("userid", userid);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            prn = response.getString("PRN");
                            name = response.getString("Name");
                            hroom = response.getString("Room");
                            hname=response.getString("Hostel");

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(HomeScreen.this, "Data parsing error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HomeScreen.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                Log.e("VolleyError", error.toString());
            }
        });

        queue.add(jsonObjectRequest);
    }
}