package com.example.studenthostelleave;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StudentLeaveApplication extends AppCompatActivity {

    EditText no_Of_Days, reason;
    TextView startDate, endDate;
    Button applyToLeave;
    String roll_no, name;
    private static final String URL = "http://192.168.43.20/hostel_management/insertStudentLeave.php";
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_leave_application);
        startDate = findViewById(R.id.startdate);
        endDate = findViewById(R.id.endDate);
        no_Of_Days = findViewById(R.id.noOfDays);
        reason = findViewById(R.id.reason);
        applyToLeave = findViewById(R.id.apply);

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker(startDate);
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker(endDate);
            }
        });

        applyToLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendLeaveApplicationData();
            }
        });
    }

    private void openDatePicker(final TextView dateTextView) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                StudentLeaveApplication.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        String selectedDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        dateTextView.setText(selectedDate);

                        validateAndCalculateDays();
                    }
                },
                year, month, day);

        if (dateTextView == startDate) {
            // Set min date as current date for start date picker
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        } else if (dateTextView == endDate) {
            // Set min date as start date for end date picker
            try {
                String startDateString = startDate.getText().toString();
                if (!startDateString.isEmpty()) {
                    Date startDateParsed = dateFormat.parse(startDateString);
                    datePickerDialog.getDatePicker().setMinDate(startDateParsed.getTime());
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        datePickerDialog.show();
    }

    private void validateAndCalculateDays() {
        String sStartDate = startDate.getText().toString();
        String sEndDate = endDate.getText().toString();

        if (!sStartDate.isEmpty() && !sEndDate.isEmpty()) {
            try {
                Date startDateParsed = dateFormat.parse(sStartDate);
                Date endDateParsed = dateFormat.parse(sEndDate);

                if (startDateParsed != null && endDateParsed != null) {
                    if (endDateParsed.before(startDateParsed)) {
                        Toast.makeText(this, "End date should be equal to or ahead of start date.", Toast.LENGTH_LONG).show();
                        endDate.setText(""); // Clear invalid end date
                    } else {
                        long diffInMillis = endDateParsed.getTime() - startDateParsed.getTime();
                        long diffInDays = (diffInMillis / (1000 * 60 * 60 * 24)) + 1; // Inclusive of both dates
                        no_Of_Days.setText(String.valueOf(diffInDays));
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendLeaveApplicationData() {
        String prn = getIntent().getStringExtra("prn");
        String studentName = getIntent().getStringExtra("studentName");
        String room = getIntent().getStringExtra("Room");
        String hname = getIntent().getStringExtra("Hostel");
        String sStartDate = startDate.getText().toString();
        String sEndDate = endDate.getText().toString();
        String sNoOfDays = no_Of_Days.getText().toString();
        String sReason = reason.getText().toString();

        if (sStartDate.isEmpty() || sEndDate.isEmpty() || sNoOfDays.isEmpty() || sReason.isEmpty()) {
            Toast.makeText(StudentLeaveApplication.this, "Please fill in all the fields", Toast.LENGTH_LONG).show();
            return;
        }

        JSONObject postData = new JSONObject();
        try {
            postData.put("prn", prn);
            postData.put("student_name", studentName);
            postData.put("start_date", sStartDate);
            postData.put("end_date", sEndDate);
            postData.put("no_of_days", sNoOfDays);
            postData.put("room", room);
            postData.put("hname", hname);
            postData.put("reason", sReason);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean error = response.getBoolean("error");
                            String message = response.getString("message");
                            if (!error) {
                                Toast.makeText(StudentLeaveApplication.this, message, Toast.LENGTH_SHORT).show();
                                clearFields();
                            } else {
                                Toast.makeText(StudentLeaveApplication.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(StudentLeaveApplication.this, "JSON Parsing error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(StudentLeaveApplication.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }

    private void clearFields() {
        startDate.setText("");
        endDate.setText("");
        no_Of_Days.setText("");
        reason.setText("");
    }
}