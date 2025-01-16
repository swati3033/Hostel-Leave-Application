<?php
include 'db_connection.php'; // Include your database connection file

header('Content-Type: application/json'); // Set the content type to JSON

$response = array();

// Check if the 'hostel_name' POST parameter is set
if (isset($_POST['hostel_name'])) {
    $hostel_name = $_POST['hostel_name'];

    // Prepare the SQL statement to fetch leave data based on hostel name
    $sql = "SELECT l_id, PRN, name, room, hostel_name, start_date, end_date, no_of_days, reason, status,status_reason,ack_status, logs 
            FROM leave_forms WHERE hostel_name = ?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("s", $hostel_name);
    $stmt->execute();

    $result = $stmt->get_result();

    if ($result->num_rows > 0) {
        $response['status'] = 'success';
        $response['leaves'] = array();

        // Fetch each leave record and add it to the 'leaves' array in the response
        while ($row = $result->fetch_assoc()) {
            $leave = array(
                "l_id" => $row['l_id'],
                "PRN" => $row['PRN'],
                "name" => $row['name'],
                "room" => $row['room'],
                "hostel_name" => $row['hostel_name'],
                "start_date" => $row['start_date'],
                "end_date" => $row['end_date'],
                "no_of_days" => $row['no_of_days'],
                "reason" => $row['reason'],
                "status" => $row['status'],
                "logs" => $row['logs']
            );
            array_push($response['leaves'], $leave);
        }
    } else {
        // If no records are found, return an error message
        $response['status'] = 'error';
        $response['message'] = 'No leaves found for the specified hostel.';
    }

    $stmt->close();
} else {
    // If 'hostel_name' is not set, return an error message
    $response['status'] = 'error';
    $response['message'] = 'Hostel name not provided.';
}

$conn->close(); // Close the database connection

echo json_encode($response); // Encode the response as JSON and output it
?>
