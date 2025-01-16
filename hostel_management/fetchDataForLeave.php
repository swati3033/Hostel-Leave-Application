<?php
include 'db_connection.php';

header('Content-Type: application/json');

$response = array();

$input = file_get_contents('php://input');
$data = json_decode($input, true);

if ($_SERVER['REQUEST_METHOD'] == 'POST' && isset($data['userid'])) {
    $userid = $data['userid'];

    $stmt = $conn->prepare("SELECT roll_no, name,room_no,hostel_name FROM hostellers WHERE username = ?");
    if ($stmt === false) {
        $response['error'] = true;
        $response['message'] = 'Prepare failed: ' . $conn->error;
        echo json_encode($response);
        exit;
    }

    $stmt->bind_param("s", $userid);

    if ($stmt->execute()) {
        $result = $stmt->get_result();
        if ($result->num_rows > 0) {
            $row = $result->fetch_assoc();
            $response = array(
                'error' => false,
                'PRN' => $row['roll_no'],
                'Name' => $row['name'],
                'Room' => $row['room_no'],
                'Hostel' => $row['hostel_name']
            );
        } else {
            $response['error'] = true;
            $response['message'] = 'User not found';
        }
    } else {
        $response['error'] = true;
        $response['message'] = 'SQL execution error';
    }

    $stmt->close();
} else {
    $response['error'] = true;
    $response['message'] = 'Invalid Request or missing parameters';
}

$conn->close();
echo json_encode($response);
?>