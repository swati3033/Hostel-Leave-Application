<?php
include 'db_connection.php';

header('Content-Type: application/json');

$response = array();
$response['leaves'] = array(); // Create an array to hold leave data

// SQL query to fetch accepted leaves in chronological order based on the 'logs' column
$sql = "SELECT l_id, PRN, name, room, hostel_name, start_date, end_date, no_of_days, reason, status, logs 
        FROM leave_forms 
        WHERE status = 'Accepted' 
        ORDER BY logs DESC"; // Sort by logs in descending order

$result = $conn->query($sql);

if ($result === false) {
    $response['error'] = $conn->error; // Add error to response
} elseif ($result->num_rows > 0) {
    while($row = $result->fetch_assoc()) {
        $response['leaves'][] = $row; // Add each row to the 'leaves' array
    }
} else {
    $response['message'] = "No records found"; // Add message if no records found
}

echo json_encode($response, JSON_PRETTY_PRINT); // Output JSON

$conn->close();
?>