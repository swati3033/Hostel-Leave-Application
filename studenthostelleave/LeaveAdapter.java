package com.example.studenthostelleave;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
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

    @NonNull
    @Override
    public LeaveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leave_item, parent, false);
        return new LeaveViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaveViewHolder holder, int position) {
        Leave leave = leaveList.get(position);
        holder.name.setText(leave.getName());
        holder.hostelName.setText(leave.getHostelName());
        holder.prnNumber.setText(leave.getPrnNumber());
        holder.startDate.setText(leave.getStartDate());
        holder.endDate.setText(leave.getEndDate());
        holder.status.setText(leave.getStatus());

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

        holder.itemView.setOnClickListener(v -> showLeaveDetailsDialog(leave));
    }

    @Override
    public int getItemCount() {
        return leaveList.size();
    }

    private void showLeaveDetailsDialog(Leave leave) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_leave_details, null);
        builder.setView(dialogView);

        TextView dialogName = dialogView.findViewById(R.id.dialog_name);
        TextView dialogHostelName = dialogView.findViewById(R.id.dialog_hostel_name);
        TextView dialogPrnNumber = dialogView.findViewById(R.id.dialog_prn_number);
        TextView dialogStartDate = dialogView.findViewById(R.id.dialog_start_date);
        TextView dialogEndDate = dialogView.findViewById(R.id.dialog_end_date);
        TextView dialogStatus = dialogView.findViewById(R.id.dialog_status);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView dialogReason = dialogView.findViewById(R.id.dialog_reason);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView dialogReply = dialogView.findViewById(R.id.dialog_reply);


        EditText dialogAcknowledgment = dialogView.findViewById(R.id.dialog_acknowledgment);
        CheckBox dialogCheckboxNoted = dialogView.findViewById(R.id.dialog_checkbox_noted);
        Button dialogButtonSubmit = dialogView.findViewById(R.id.dialog_button_submit);

        dialogName.setText("Name: " + leave.getName());
        dialogHostelName.setText("Hostel: " + leave.getHostelName());
        dialogPrnNumber.setText("PRN: " + leave.getPrnNumber());
        dialogStartDate.setText("Start Date: " + leave.getStartDate());
        dialogEndDate.setText("End Date: " + leave.getEndDate());
        dialogStatus.setText("Status: " + leave.getStatus());
        dialogReason.setText("Reason: " + leave.getReason());
        dialogReason.setText("Rector Note: " + leave.getReply());


        String status1 = leave.getStatus();
        if ("Accepted".equalsIgnoreCase(status1)) {
            dialogStatus.setTextColor(Color.GREEN);
        } else if ("Rejected".equalsIgnoreCase(status1)) {
            dialogStatus.setTextColor(Color.RED);
        } else if ("Pending".equalsIgnoreCase(status1)) {
            dialogStatus.setTextColor(Color.GRAY);
        }

        AlertDialog dialog = builder.create();
        dialog.show();

        dialogButtonSubmit.setOnClickListener(v -> {
            String acknowledgment = dialogAcknowledgment.getText().toString().trim();
            boolean isNoted = dialogCheckboxNoted.isChecked();

            if (!acknowledgment.isEmpty() && isNoted) {
                updateLeaveAcknowledgment(leave.getPrnNumber(), acknowledgment, isNoted);
                dialog.dismiss();
            } else {
                Toast.makeText(context, "Please enter acknowledgment or Check Noted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateLeaveAcknowledgment(String prn, String acknowledgment, boolean isNoted) {
        String url = "http://192.168.43.20/hostel_management/updateLeaveAcknowledgment.php";

        // Create a JSON object for the parameters
        JSONObject params = new JSONObject();
        try {
            params.put("prn", prn);
            params.put("acknowledgment", acknowledgment);
            // Send 'Noted' as a string when isNoted is true; otherwise, send an empty string
            params.put("isNoted", isNoted ? "Noted" : "");
        } catch (JSONException e) {
            e.printStackTrace();
            return; // Exit if JSON construction fails
        }

        // Create the request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, params,
                response -> Toast.makeText(context, "Acknowledgment updated", Toast.LENGTH_SHORT).show(),
                error -> {
                    String errorMessage = "Failed to update acknowledgment: " + error.getMessage();
                    if (error.networkResponse != null) {
                        String responseBody = new String(error.networkResponse.data);
                        int statusCode = error.networkResponse.statusCode;
                        errorMessage += "\nResponse Body: " + responseBody;
                        errorMessage += "\nStatus Code: " + statusCode;
                    }
                    Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
                    Log.e("VolleyError", errorMessage);
                });

        // Add the request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);
    }

    static class LeaveViewHolder extends RecyclerView.ViewHolder {
        TextView name, hostelName, prnNumber, startDate, endDate, status;

        LeaveViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            hostelName = itemView.findViewById(R.id.hostel_name);
            prnNumber = itemView.findViewById(R.id.prn_number);
            startDate = itemView.findViewById(R.id.start_date);
            endDate = itemView.findViewById(R.id.end_date);
            status = itemView.findViewById(R.id.status);
        }
    }
}
