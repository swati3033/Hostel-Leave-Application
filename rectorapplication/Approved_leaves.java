package com.example.rectorapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Approved_leaves extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LeaveAdapter leaveAdapter;
    private List<Leave> leaveList = new ArrayList<>();
    private static final String url = "http://192.168.43.20/hostel_management/AcceptedLeaves.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approved_leaves);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        leaveAdapter = new LeaveAdapter(leaveList, this);
        recyclerView.setAdapter(leaveAdapter);

        fetchLeaves();
    }

    private void fetchLeaves() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                null,
                response -> {
                    leaveList.clear(); // Clear the list to avoid duplicates
                    try {
                        // Assume the response contains an array of leaves under a key "leaves"
                        JSONArray leavesArray = response.getJSONArray("leaves");
                        for (int i = 0; i < leavesArray.length(); i++) {
                            JSONObject leaveObject = leavesArray.getJSONObject(i);

                            Leave leave = new Leave();
                            leave.setId(leaveObject.getInt("l_id"));
                            leave.setPRN(leaveObject.getString("PRN"));
                            leave.setName(leaveObject.getString("name"));
                            leave.setRoom(leaveObject.getString("room"));
                            leave.setHostel_name(leaveObject.getString("hostel_name"));
                            leave.setStart_date(leaveObject.getString("start_date"));
                            leave.setEnd_date(leaveObject.getString("end_date"));
                            leave.setNo_of_days(leaveObject.getInt("no_of_days"));
                            leave.setReason(leaveObject.getString("reason"));
                            leave.setStatus(leaveObject.getString("status"));
                            leave.setLogs(leaveObject.getString("logs"));
                            leave.setVisited(false); // Initially set as not visited

                            leaveList.add(leave);
                        }
                        leaveAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Parsing error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    // Handle error
                    String errorMessage = "Error: " + error.getMessage();
                    Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
                    error.printStackTrace();

                    if (error.networkResponse != null) {
                        String responseBody = new String(error.networkResponse.data);
                        Toast.makeText(this, "Response Body: " + responseBody, Toast.LENGTH_LONG).show();
                        Log.e("VolleyError", responseBody);
                    }
                }
        );

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
}
