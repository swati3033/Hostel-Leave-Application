<?php
include 'db_connection.php';

header('Content-Type: application/json');

$response = array();

// Read and decode the input JSON data
$input = file_get_contents('php://input');
$data = json_decode($input, true);

// Check if the request method is POST and PRN is present
if ($_SERVER['REQUEST_METHOD'] == 'POST' && isset($data['PRN'])) {
    $prn = $data['PRN'];

    // Prepare SQL query to fetch student details by PRN
    $stmt = $conn->prepare("SELECT name, roll_no, branch, class, email, address, stud_phone, parent_phone, hostel_name, room_no FROM hostellers WHERE roll_no = ?");
    if ($stmt === false) {
        // Handle query preparation error
        $response['status'] = 'error';
        $response['message'] = 'Prepare failed: ' . $conn->error;
        echo json_encode($response);
        exit;
    }

    // Bind the PRN parameter to the SQL query
    $stmt->bind_param("s", $prn);

    // Execute the query and fetch the result
    if ($stmt->execute()) {
        $result = $stmt->get_result();
        if ($result->num_rows > 0) {
            // Fetch data and add it to the response
            $row = $result->fetch_assoc();
            $response = array(
                'status' => 'success',
                'data' => array(
                    'name' => $row['name'],
                    'roll_no' => $row['roll_no'],
                    'branch' => $row['branch'],
                    'class' => $row['class'],
                    'email' => $row['email'],
                    'address' => $row['address'],
                    'stud_phone' => $row['stud_phone'],
                    'parent_phone' => $row['parent_phone'],
                    'hostel_name' => $row['hostel_name'],
                    'room_no' => $row['room_no']
                )
            );
        } else {
            // No records found for the given PRN
            $response['status'] = 'No records found';
        }
    } else {
        // SQL execution error
        $response['status'] = 'error';
        $response['message'] = 'SQL execution error: ' . $stmt->error;
    }

    $stmt->close();
} else {
    // Invalid request method or missing PRN parameter
    $response['status'] = 'Invalid Request or missing PRN';
}

$conn->close();
echo json_encode($response);

// Log the response for debugging
error_log(json_encode($response));
?>