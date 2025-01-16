<?php
include 'db_connection.php';

header('Content-Type: application/json');

$response = array();

$input = file_get_contents('php://input');
$data = json_decode($input, true);

// Check connection


$sql = "SELECT h_id, hostel_name FROM hostel";
$result = $conn->query($sql);

$hostels = array();

if ($result->num_rows > 0) {
    while($row = $result->fetch_assoc()) {
        $hostels[] = $row;
    }
}

echo json_encode($hostels);

$conn->close();
?>
