package com.example.rectorapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class LeaveAdapter extends RecyclerView.Adapter<LeaveAdapter.LeaveViewHolder> {

    private List<Leave> leaveList;
    private Context context;

    public LeaveAdapter(List<Leave> leaveList, Context context) {
        this.leaveList = leaveList;
        this.context = context;
    }

    @Override
    public LeaveViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leave_item, parent, false);
        return new LeaveViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LeaveViewHolder holder, int position) {
        Leave leave = leaveList.get(position);
        holder.name.setText(leave.getName());
        holder.hostelName.setText(leave.getHostel_name());
        holder.prnNumber.setText(leave.getPRN());
        holder.startDate.setText(leave.getStart_date());
        holder.endDate.setText(leave.getEnd_date());
        holder.status.setText(leave.getStatus());

        if (leave.isVisited()) {
            holder.itemView.setBackgroundColor(Color.LTGRAY); // visited color
        } else {
            holder.itemView.setBackgroundColor(Color.DKGRAY); // not visited color
        }

        if (leave.isVisited()) {
            holder.itemView.setBackgroundColor(Color.LTGRAY); // visited color
        } else {
            holder.itemView.setBackgroundColor(Color.DKGRAY); // not visited color
        }
        // Set color based on status
        String status = leave.getStatus();
        if ("Accepted".equalsIgnoreCase(status)) {
            holder.status.setTextColor(Color.GREEN);
        } else if ("Rejected".equalsIgnoreCase(status)) {
            holder.status.setTextColor(Color.RED);
        } else if ("Pending".equalsIgnoreCase(status)) {
            holder.status.setTextColor(Color.GRAY);
        } else {
            holder.status.setTextColor(Color.BLACK); // Default color if status is not recognized
        }

        // Set onClickListener for the view holder to show details
        holder.itemView.setOnClickListener(v -> {
            leave.setVisited(true);
            notifyItemChanged(position);

            showDetailsDialog(leave);
        });

        // Set onClickListener for the View Details button
        holder.btnViewDetails.setOnClickListener(v -> {
            showDetails(leave.getPRN());
        });
    }

    @Override
    public int getItemCount() {
        return leaveList.size();
    }

    public static class LeaveViewHolder extends RecyclerView.ViewHolder {
        public TextView name, hostelName, prnNumber, startDate, endDate, status;
        public Button btnViewDetails;

        public LeaveViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            hostelName = itemView.findViewById(R.id.hostel_name);
            prnNumber = itemView.findViewById(R.id.prn_number);
            startDate = itemView.findViewById(R.id.start_date);
            endDate = itemView.findViewById(R.id.end_date);
            status = itemView.findViewById(R.id.status);
            btnViewDetails = itemView.findViewById(R.id.btn_view);
        }
    }

    private void showDetailsDialog(Leave leave) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Leave Details");

        // Inflate custom dialog view
        View dialogView = LayoutInflater.from(context).inflate(R.layout.leave_details_dialog, null);
        builder.setView(dialogView);

        // Initialize UI components
        TextView prn = dialogView.findViewById(R.id.details_prn);
        TextView name = dialogView.findViewById(R.id.details_name);
        TextView hostelName = dialogView.findViewById(R.id.details_hostel_name);
        TextView startDate = dialogView.findViewById(R.id.details_start_date);
        TextView endDate = dialogView.findViewById(R.id.details_end_date);
        TextView noOfDays = dialogView.findViewById(R.id.details_no_of_days);
        TextView reason = dialogView.findViewById(R.id.details_reason);
        TextView status = dialogView.findViewById(R.id.details_status);

        EditText dialogReply = dialogView.findViewById(R.id.reply);

        // Set data
        prn.setText("PRN: "+leave.getPRN());
        name.setText("Name: "+leave.getName());
        hostelName.setText("Hostel: "+leave.getHostel_name());
        startDate.setText("Start Date: "+leave.getStart_date());
        endDate.setText("End Date: "+leave.getEnd_date());
        noOfDays.setText("Days: "+String.valueOf(leave.getNo_of_days()));
        reason.setText("Reason: "+leave.getReason());
        status.setText("Status: "+leave.getStatus());

        String status1 = leave.getStatus();
        String acknowledgmentStatus = leave.getACK(); // Assuming this is the acknowledgment status

        // Enable/Disable buttons and EditText based on acknowledgment status
        if ("Noted".equalsIgnoreCase(acknowledgmentStatus)) {
            dialogReply.setEnabled(false);
            builder.setNegativeButton(null, null);
            builder.setNeutralButton(null, null);
        } else {
            if ("Accepted".equalsIgnoreCase(status1)) {
                status.setTextColor(Color.GREEN);
                builder.setNegativeButton(null, null);
            } else if ("Rejected".equalsIgnoreCase(status1)) {
                status.setTextColor(Color.RED);
                builder.setNegativeButton(null, null);
                builder.setNeutralButton(null, null);
            } else if ("Pending".equalsIgnoreCase(status1)) {
                status.setTextColor(Color.GRAY);
                builder.setNegativeButton("Reject", (dialog, which) -> handleLeaveAction(leave, "Rejected", dialogReply.getText().toString()));
                builder.setNeutralButton("Accept", (dialog, which) -> handleLeaveAction(leave, "Accepted", dialogReply.getText().toString()));
            } else {
                status.setTextColor(Color.BLACK); // Default color if status is not recognized
                builder.setNegativeButton("Reject", (dialog, which) -> handleLeaveAction(leave, "Rejected", dialogReply.getText().toString()));
                builder.setNeutralButton("Accept", (dialog, which) -> handleLeaveAction(leave, "Accepted", dialogReply.getText().toString()));
            }
        }
        builder.setPositiveButton("Close", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void showDetails(String prn) {
        String url = "http://192.168.43.20/hostel_management/ShowStudentByLeave.php";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("PRN", prn);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url, jsonObject,
                response -> {
                    try {
                        // Check if the response contains the status key
                        if (response.has("status") && response.getString("status").equals("No records found")) {
                            Toast.makeText(context, "No details found for this PRN", Toast.LENGTH_LONG).show();
                            return;
                        }

                        // Extract details
                        JSONObject data = response.getJSONObject("data");
                        String name = data.getString("name");
                        String rollNo = data.getString("roll_no");
                        String branch = data.getString("branch");
                        String studentClass = data.getString("class");
                        String email = data.getString("email");
                        String address = data.getString("address");
                        String studPhone = data.getString("stud_phone");
                        String parentPhone = data.getString("parent_phone");
                        String hostelName = data.getString("hostel_name");
                        String roomNo = data.getString("room_no");

                        // Show the dialog with the details
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Hosteller Details");

                        View dialogView1 = LayoutInflater.from(context).inflate(R.layout.dialog_hosteller_details, null);
                        builder.setView(dialogView1);

                        // Set data to the views
                        TextView nameTextView = dialogView1.findViewById(R.id.details_name);
                        TextView rollNoTextView = dialogView1.findViewById(R.id.details_roll_no);
                        TextView branchTextView = dialogView1.findViewById(R.id.details_branch);
                        TextView classTextView = dialogView1.findViewById(R.id.details_class);
                        TextView emailTextView = dialogView1.findViewById(R.id.details_email);
                        TextView addressTextView = dialogView1.findViewById(R.id.details_address);
                        TextView studPhoneTextView = dialogView1.findViewById(R.id.details_stud_phone);
                        TextView parentPhoneTextView = dialogView1.findViewById(R.id.details_parent_phone);
                        TextView hostelNameTextView = dialogView1.findViewById(R.id.details_hostel_name);
                        TextView roomNoTextView = dialogView1.findViewById(R.id.details_room_no);

                        nameTextView.setText("Name: "+name);
                        rollNoTextView.setText("PRN: "+rollNo);
                        branchTextView.setText("Branch: "+branch);
                        classTextView.setText("Class: "+studentClass);
                        emailTextView.setText("Email: "+email);
                        addressTextView.setText("Address: "+address);
                        studPhoneTextView.setText("Student Mob: "+studPhone);
                        parentPhoneTextView.setText("Parent Mob: "+parentPhone);
                        hostelNameTextView.setText("Hostel: "+hostelName);
                        roomNoTextView.setText("Room: "+roomNo);

                        builder.setPositiveButton("Close", (dialog, which) -> dialog.dismiss());
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Error parsing data", Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    Toast.makeText(context, "Failed to fetch details", Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                }
        );

        // Add the request to the Volley request queue
        Volley.newRequestQueue(context).add(jsonObjectRequest);
    }

    private void handleLeaveAction(Leave leave, String action, String statusReason) {
        String requestUrl = "http://192.168.43.20/hostel_management/updateLeaveStatus.php"; // Update with your server URL

        // Create JSON object for request
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", leave.getId());
            jsonObject.put("status", action);
            jsonObject.put("status_reason", statusReason); // Add status_reason to request
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }

        // Create request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                requestUrl,
                jsonObject,
                response -> {
                    try {
                        boolean success = response.getBoolean("success");
                        if (success) {
                            Toast.makeText(context, "Leave " + action, Toast.LENGTH_SHORT).show();
                            // Refresh data or update UI accordingly
                            ((Activity) context).finish(); // Close activity if applicable
                        } else {
                            String message = response.getString("message");
                            Toast.makeText(context, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Parsing error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    // Handle error
                    String errorMessage = "Error: " + error.getMessage();
                    Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
                    if (error.networkResponse != null) {
                        String responseBody = new String(error.networkResponse.data);
                        Toast.makeText(context, "Response Body: " + responseBody, Toast.LENGTH_LONG).show();
                    }
                }
        );

        // Add request to queue
        Volley.newRequestQueue(context).add(jsonObjectRequest);
    }

}