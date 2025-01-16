

package com.example.studenthostelleave;

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

public class LeaveActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LeaveAdapter leaveAdapter;
    private List<Leave> leaveList = new ArrayList<>();
    private static final String url = "http://192.168.43.20/hostel_management/fetchLeaves.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave);

        recyclerView = findViewById(R.id.recyclerView);
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
                    leaveList.clear();
                    try {
                        JSONArray leavesArray = response.getJSONArray("leaves");
                        for (int i = 0; i < leavesArray.length(); i++) {
                            JSONObject leaveObject = leavesArray.getJSONObject(i);

                            Leave leave = new Leave();
                            leave.setId(leaveObject.getInt("l_id"));
                            leave.setPrnNumber(leaveObject.getString("PRN"));
                            leave.setName(leaveObject.getString("name"));
                            leave.setHostelName(leaveObject.getString("hostel_name"));
                            leave.setStartDate(leaveObject.getString("start_date"));
                            leave.setEndDate(leaveObject.getString("end_date"));
                            leave.setStatus(leaveObject.getString("status"));
                            leave.setReason(leaveObject.getString("reason"));
                            leave.setReason(leaveObject.getString("status_reason"));


                            leaveList.add(leave);
                        }
                        leaveAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Leave is not exist", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
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

