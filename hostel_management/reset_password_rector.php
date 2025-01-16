<?php
include 'db_connection.php'; // Ensure this file contains your database connection setup

header('Content-Type: application/json');

$response = array();

// Get the input data from the request body
$input = file_get_contents('php://input');
$data = json_decode($input, true);

// Get email and password from the decoded JSON data
$email = isset($data['email']) ? $data['email'] : null;
$password = isset($data['password']) ? $data['password'] : null;

// Validate input
if (empty($email) || empty($password)) {
    $response['status'] = 'error';
    $response['message'] = 'Email and password fields cannot be empty.';
    echo json_encode($response);
    exit();
}
// Prepare and execute the update statement
$stmt = $conn->prepare("UPDATE rector SET password = ? WHERE username = ?");
$stmt->bind_param("ss", $password, $email);

if ($stmt->execute()) {
    if ($stmt->affected_rows > 0) {
        $response['status'] = 'success';
        $response['message'] = 'Password has been reset successfully.';
    } else {
        $response['status'] = 'error';
        $response['message'] = 'No user found with that email address.';
    }
} else {
    $response['status'] = 'error';
    $response['message'] = 'Error: ' . $stmt->error;
}

$stmt->close();
$conn->close();

// Send JSON response back to the Android app
echo json_encode($response);
?>
