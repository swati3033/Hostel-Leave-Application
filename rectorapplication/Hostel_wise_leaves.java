package com.example.rectorapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Hostel_wise_leaves extends AppCompatActivity {

    LinearLayout layout;
    private static final String URL = "http://192.168.43.20/hostel_management/fetchHostelData.php";
    private Map<String, Runnable> hostelActions;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hostel_wise_leaves);

        layout = findViewById(R.id.layoutId);

        // Instantiate the RequestQueue
        RequestQueue queue = Volley.newRequestQueue(this);

        // Initialize the map for hostel actions
        hostelActions = new HashMap<>();

        // Request a JSON array response from the provided URL
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.POST,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject hostel = response.getJSONObject(i);
                                String hostelName = hostel.getString("hostel_name");

                                // Create a button for each hostel
                                Button hostelButton = new Button(Hostel_wise_leaves.this);
                                hostelButton.setText(hostelName);

                                // Store the hostel name as a tag for the button
                                hostelButton.setTag(hostelName);

                                // Set Layout Parameters for better visual presentation
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT
                                );
                                layoutParams.setMargins(16, 16, 16, 16); // Add margins for better UI
                                hostelButton.setLayoutParams(layoutParams);

                                // Set padding, text size, and background color
                                hostelButton.setPadding(20, 20, 20, 20);
                                hostelButton.setTextSize(18); // Increase text size
                                hostelButton.setTextColor(Color.WHITE);

                                // Create and set a custom background with rounded corners
                                GradientDrawable drawable = new GradientDrawable();
                                drawable.setColor(Color.parseColor("#6495ED")); // Background color
                                drawable.setCornerRadius(16); // Rounded corners
                                hostelButton.setBackground(drawable);

                                // Set OnClickListener for button
                                hostelButton.setOnClickListener(v -> {
                                    String name = (String) v.getTag();
                                    handleButtonClick(name);
                                });

                                // Add the button to the layout
                                layout.addView(hostelButton);

                                // Define actions for each hostel dynamically
                                hostelActions.put(hostelName, createActionForHostel(hostelName));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Hostel_wise_leaves.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                error -> {
                    // Handle error
                    Toast.makeText(Hostel_wise_leaves.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                });

        // Add the request to the RequestQueue
        queue.add(jsonArrayRequest);
    }

    // Method to create a dynamic action for each hostel
    private Runnable createActionForHostel(String hostelName) {
        return () -> {
            // Navigate to HostelLeavesActivity and pass the hostel name
            Intent intent = new Intent(Hostel_wise_leaves.this, HostelLeavesActivity.class);
            intent.putExtra("HOSTEL_NAME", hostelName);
            startActivity(intent);
        };
    }

    // Method to handle button clicks dynamically based on hostel name
    private void handleButtonClick(String hostelName) {
        Runnable action = hostelActions.get(hostelName);
        if (action != null) {
            action.run();
        } else {
            Toast.makeText(this, "No action defined for " + hostelName, Toast.LENGTH_SHORT).show();
        }
    }
}