package com.example.studenthostelleave;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddStudentProfile extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "AddStudentProfile";
    private static final String SERVER_URL = "http://192.168.43.20/hostel_management/fetchRegistrationRequest.php";
    private static final String ParentURL = "http://192.168.43.20/hostel_management/insertParentDetails.php";

    private Button saveButton;
    private EditText sname, prn, address, mail, parentPhnNo, sphnNo, pname, pemail;
    private Spinner branch, sclass, parent_type;
    private String selectedBranch, selectedClass, selectedptype;
    private static final String[] branchNames = {"CSIT", "CSE", "ETC", "EE", "ME", "CIVIL", "AIML"};
    private static final String[] classes = {"Final Year", "Third Year", "Second Year", "First Year"};
    private static final String[] parentType = {"Mother", "Father", "Brother", "Sister"};

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student_profile);

        selectedBranch = branchNames[0];
        selectedClass = classes[0];
        selectedptype = parentType[0];

        saveButton = findViewById(R.id.saveP);
        sname = findViewById(R.id.sname);
        prn = findViewById(R.id.prn);
        branch = findViewById(R.id.branch);
        sclass = findViewById(R.id.sclass);
        address = findViewById(R.id.address);
        mail = findViewById(R.id.mail);
        parentPhnNo = findViewById(R.id.parent_phn_no);
        sphnNo = findViewById(R.id.sphn_no);
        parent_type = findViewById(R.id.ptype);
        pname = findViewById(R.id.pname);
        pemail = findViewById(R.id.pemail);

        ArrayAdapter<String> branchAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, branchNames);
        branchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        branch.setAdapter(branchAdapter);
        branch.setOnItemSelectedListener(this);

        ArrayAdapter<String> classAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, classes);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sclass.setAdapter(classAdapter);
        sclass.setOnItemSelectedListener(this);

        ArrayAdapter<String> ptypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, parentType);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        parent_type.setAdapter(ptypeAdapter);
        parent_type.setOnItemSelectedListener(this);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String password = intent.getStringExtra("c_passwd");

        saveButton.setOnClickListener(v -> {
            String email = mail.getText().toString();
            String regex = "^(.+)@(.+)$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(email);

            int sphnNoLength = sphnNo.getText().toString().length();
            int paPhnNoLength = parentPhnNo.getText().toString().length();

            if (sname.getText().toString().isEmpty() || prn.getText().toString().isEmpty() || mail.getText().toString().isEmpty()
                    || address.getText().toString().isEmpty() || sphnNo.getText().toString().isEmpty() || parentPhnNo.getText().toString().isEmpty() || pname.getText().toString().isEmpty() || pemail.getText().toString().isEmpty()) {
                showToast("Please enter all the values");
                return;
            }

            if (!matcher.matches()) {
                showToast("Please enter a valid email");
            } else if (sphnNoLength != 10 || paPhnNoLength != 10) {
                showToast("Please enter a valid mobile number");
            } else {
                postStudentDataUsingVolley(username, password, sname.getText().toString(), prn.getText().toString(),
                        selectedBranch, selectedClass, mail.getText().toString(), address.getText().toString(),
                        sphnNo.getText().toString(), parentPhnNo.getText().toString());

                postParentDataUsingVolley(selectedptype, pname.getText().toString(), parentPhnNo.getText().toString(), pemail.getText().toString());
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int parentId = parent.getId();
        if (parentId == R.id.branch) {
            selectedBranch = branchNames[position];
            Log.d(TAG, "Selected Branch: " + selectedBranch);
        } else if (parentId == R.id.sclass) {
            selectedClass = classes[position];
            Log.d(TAG, "Selected Class: " + selectedClass);
        } else if (parentId == R.id.ptype) {
            Log.d(TAG, "Parent Type: " + selectedptype);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing
    }

    private void postParentDataUsingVolley(String ptype, String pname, String parentPhn, String email) {
        RequestQueue queue = Volley.newRequestQueue(AddStudentProfile.this);

        JSONObject postData = new JSONObject();
        try {

            postData.put("parentType", ptype);
            postData.put("ParentName", pname);
            postData.put("ParentPhone", parentPhn);
            postData.put("Email", email);

        } catch (JSONException e) {
            e.printStackTrace();
            showToast("Error creating JSON data: " + e.getMessage());
            return;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, ParentURL, postData, response -> {
                    try {
                        boolean error = response.getBoolean("error");
                        String message = response.getString("message");

                        if (!error) {
                            clearInputFields();
                            showToast("Data added to API: " + message);
                            Intent intent = new Intent(AddStudentProfile.this, StudentLogin.class);
                            startActivity(intent);
                        } else {
                            showToast("Failed to add data: " + message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showToast("JSON Parsing error: " + e.getMessage());
                    }
                }, this::handleVolleyError);

        queue.add(jsonObjectRequest);
    }

    private void clearInputFields1() {
        pname.setText("");
        pemail.setText("");
        parentPhnNo.setText("");
    }

    private void handleVolleyError1(VolleyError error) {
        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            showToast1("Network error. Please try again later.");
        } else if (error instanceof AuthFailureError) {
            showToast1("Authentication error. Please check your credentials.");
        } else if (error instanceof ServerError) {
            showToast1("Server error. Please try again later.");
        } else if (error instanceof NetworkError) {
            showToast1("Network error. Please check your connection.");
        } else if (error instanceof ParseError) {
            showToast1("Parsing error. Please try again later.");
        } else {
            showToast1("An unknown error occurred.");
        }
    }

    private void showToast1(String message) {
        Toast.makeText(AddStudentProfile.this, message, Toast.LENGTH_LONG).show();
    }

    private void postStudentDataUsingVolley(String username, String password, String stname, String sprn, String branch, String sclass, String email, String saddress, String scontactNo, String paPhnNo) {
        RequestQueue queue = Volley.newRequestQueue(AddStudentProfile.this);

        JSONObject postData = new JSONObject();
        try {
            postData.put("uname", username);
            postData.put("passwd", password);
            postData.put("studentName", stname);
            postData.put("prn", sprn);
            postData.put("branch", branch);
            postData.put("sclass", sclass);
            postData.put("email", email);
            postData.put("address", saddress);
            postData.put("studentContact", scontactNo);
            postData.put("parentContact", paPhnNo);
        } catch (JSONException e) {
            e.printStackTrace();
            showToast("Error creating JSON data: " + e.getMessage());
            return;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, SERVER_URL, postData, response -> {
                    try {
                        boolean error = response.getBoolean("error");
                        String message = response.getString("message");

                        if (!error) {
                            clearInputFields();
                            showToast("Data added to API: " + message);
//                            Intent intent = new Intent(AddStudentProfile.this, StudentLogin.class);
//                            startActivity(intent);
                        } else {
                            showToast("Failed to add data: " + message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showToast("JSON Parsing error: " + e.getMessage());
                    }
                }, this::handleVolleyError);

        queue.add(jsonObjectRequest);
    }

    private void clearInputFields() {
        sname.setText("");
        prn.setText("");
        address.setText("");
        mail.setText("");
        parentPhnNo.setText("");
        sphnNo.setText("");
    }

    private void handleVolleyError(VolleyError error) {
        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            showToast("Network error. Please try again later.");
        } else if (error instanceof AuthFailureError) {
            showToast("Authentication error. Please check your credentials.");
        } else if (error instanceof ServerError) {
            showToast("Server error. Please try again later.");
        } else if (error instanceof NetworkError) {
            showToast("Network error. Please check your connection.");
        } else if (error instanceof ParseError) {
            showToast("Parsing error. Please try again later.");
        } else {
            showToast("An unknown error occurred.");
        }
    }

    private void showToast(String message) {
        Toast.makeText(AddStudentProfile.this, message, Toast.LENGTH_LONG).show();
    }



}