<?php
include 'db_connection.php';

header('Content-Type: application/json');

$input = file_get_contents('php://input');
$data = json_decode($input, true);

if (isset($data['id']) && isset($data['status'])) {
    $id = $data['id'];
    $status = $data['status'];
    $status_reason = isset($data['status_reason']) ? $data['status_reason'] : null;

    // Prepare the SQL statement
    if ($status_reason !== null) {
        $sql = "UPDATE leave_forms SET status='$status', status_reason='$status_reason' WHERE l_id=$id";
    } else {
        $sql = "UPDATE leave_forms SET status='$status' WHERE l_id=$id";
    }

    if ($conn->query($sql) === TRUE) {
        echo json_encode(["success" => true]);
    } else {
        echo json_encode(["success" => false, "message" => $conn->error]);
    }
} else {
    echo json_encode(["success" => false, "message" => "Invalid input"]);
}

$conn->close();
?>
