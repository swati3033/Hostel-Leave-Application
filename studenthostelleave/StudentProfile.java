package com.example.studenthostelleave;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

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

public class StudentProfile extends AppCompatActivity {

    TextView stuName, stuPrn, stuBranch, stuClass, stuAddress, stuEmail, parentContact, stuContact, stuHostel, stuRoom;
    public final String url = "http://192.168.43.20/hostel_management/fetchStudentProfile.php";

    String roll_no,name;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        stuName = findViewById(R.id.name);
        stuPrn = findViewById(R.id.prn);
        stuBranch = findViewById(R.id.branchh);
        stuClass = findViewById(R.id.stuclass);
        stuAddress = findViewById(R.id.addr);
        stuEmail = findViewById(R.id.email);
        parentContact = findViewById(R.id.Parent_Contact);
        stuContact = findViewById(R.id.Student_Contact);
        stuHostel = findViewById(R.id.hostel);
        stuRoom = findViewById(R.id.room);

        String username = getIntent().getStringExtra("userid");
        // Fetch student details
        if (!TextUtils.isEmpty(username)) {
            getStudentDetails(username);
        } else {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
        }


//        finish();

    }



    @Override
    public boolean onSupportNavigateUp() {
        // Create an Intent to hold the data to be sent back
        Intent resultIntent = new Intent();
        resultIntent.putExtra("key_name", "value_data"); // Replace "key_name" and "value_data" with your data

        // Set the result to be returned to the previous activity
        setResult(RESULT_OK, resultIntent);

        // Finish the current activity
        finish();

        return true;
    }
    private void getStudentDetails(String userid) {
        // Request queue for network operations
        RequestQueue queue = Volley.newRequestQueue(StudentProfile.this);

        // Parameters for the POST request
        Map<String, String> params = new HashMap<>();
        params.put("userid", userid);

        // Create a JsonObjectRequest
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("StudentProfile", "Response: " + response.toString()); // Log the response for debugging

                        try {
                            if (response.getBoolean("error")) {
                                Toast.makeText(StudentProfile.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                                return;
                            }

                            // Set student data in text views
                            stuName.setText("Name: " + response.getString("name"));
                            stuPrn.setText("PRN: " + response.getString("roll_no"));
                            stuBranch.setText("Branch: " + response.getString("branch"));
                            stuClass.setText("Class: " + response.getString("class"));
                            stuAddress.setText("Address: " + response.getString("address"));
                            stuEmail.setText("Email: " + response.getString("email"));
                            parentContact.setText("Parent's Contact: " + response.getString("parent_phone"));
                            stuContact.setText("Student's Contact: " + response.getString("stud_phone"));
                            stuHostel.setText("Hostel: " + response.getString("hostel_name"));
                            stuRoom.setText("Room No: " + response.getString("room_no"));

                            roll_no=response.getString("roll_no");
                            name=response.getString("name");

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(StudentProfile.this, "Error parsing response: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle errors
                Toast.makeText(StudentProfile.this, "Failed to get student details: " + error, Toast.LENGTH_LONG).show();
            }
        });

        // Add request to queue
        queue.add(request);
    }
}