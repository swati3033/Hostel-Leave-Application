package com.example.rectorapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RectorLogin extends AppCompatActivity {

    EditText uname,passwd;
    private static final String URL = "http://192.168.43.20/hostel_management/rectorLogin.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rector_login);

        Button b1=findViewById(R.id.login);
        uname=findViewById(R.id.username);
        passwd=findViewById(R.id.password);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }
    private void loginUser() {
        final String username = uname.getText().toString().trim();
        final String password = passwd.getText().toString().trim();

        JSONObject loginData = new JSONObject();
        try {
            loginData.put("uname", username);
            loginData.put("passwd", password);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(RectorLogin.this, "Error creating JSON data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, loginData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean error = response.getBoolean("error");
                            String message = response.getString("message");
                            Toast.makeText(RectorLogin.this, message, Toast.LENGTH_SHORT).show();

                            if (!error) {
                                Intent intent=new Intent(RectorLogin.this, HomeScreen.class);

                                intent.putExtra("userid", username);

                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(RectorLogin.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RectorLogin.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
}