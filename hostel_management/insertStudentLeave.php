<?php
include 'db_connection.php';

header('Content-Type: application/json');

$response = array();

$input = file_get_contents('php://input');
$data = json_decode($input, true);

if ($_SERVER['REQUEST_METHOD'] == 'POST' && $data) {
    $prn = isset($data['prn']) ? (int)$data['prn'] : 0;
    $student_name = isset($data['student_name']) ? $data['student_name'] : '';
    $start_date = isset($data['start_date']) ? $data['start_date'] : '';
    $end_date = isset($data['end_date']) ? $data['end_date'] : '';
    $no_of_days = isset($data['no_of_days']) ? (int)$data['no_of_days'] : 0;
    $room = isset($data['room']) ? $data['room'] : '';
    $hostel_name = isset($data['hname']) ? $data['hname'] : '';

    $reason = isset($data['reason']) ? $data['reason'] : '';

    if (empty($prn) || empty($student_name) || empty($room) || empty($start_date) || empty($end_date) || empty($no_of_days) || empty($reason)) {
        $response['error'] = true;
        $response['message'] = "All fields are required!";
    } else {
        $insert_query = "INSERT INTO leave_forms (PRN, name, room,hostel_name, start_date, end_date, no_of_days, reason,status) VALUES ('$prn', '$student_name', '$room','$hostel_name', '$start_date', '$end_date', '$no_of_days', '$reason','pending')";
        $result = mysqli_query($conn, $insert_query);

        if ($result) {
            $response['error'] = false;
            $response['message'] = "Leave application submitted successfully!";
        } else {
            $response['error'] = true;
            $response['message'] = "Failed to submit leave application! Error: " . mysqli_error($conn);
        }
    }
} else {
    $response['error'] = true;
    $response['message'] = "Invalid Request!";
}

echo json_encode($response);
?>