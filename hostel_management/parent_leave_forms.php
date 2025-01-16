<?php
// Database connection
include 'db_connection.php';

header('Content-Type: application/json');
// Get the login credentials from POST request
$parent_username = $_POST['username'];
$parent_password = $_POST['password'];

// Authenticate the parent and get student reference key
$sql = "SELECT hs_id FROM parent WHERE username=? AND password=?";
$stmt = $conn->prepare($sql);
$stmt->bind_param("ss", $parent_username, $parent_password);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows > 0) {
    $row = $result->fetch_assoc();
    $student_ref_key = $row['hs_id'];

    // Get PRN from hosteller table using student ID (assuming student_ref_key is the student ID)
    $sql2 = "SELECT roll_no FROM hosteller WHERE hs_id=?";
    $stmt2 = $conn->prepare($sql2);
    $stmt2->bind_param("i", $student_ref_key);
    $stmt2->execute();
    $result2 = $stmt2->get_result();

    if ($result2->num_rows > 0) {
        $row2 = $result2->fetch_assoc();
        $student_prn = $row2['roll_no'];

        // Fetch the leave forms using PRN from leave_forms table
        $sql3 = "SELECT * FROM leave_forms WHERE PRN=?";
        $stmt3 = $conn->prepare($sql3);
        $stmt3->bind_param("s", $student_prn);
        $stmt3->execute();
        $result3 = $stmt3->get_result();

        $leave_forms = array();

        while ($row3 = $result3->fetch_assoc()) {
            $leave_forms[] = $row3;
        }

        // Send leave forms data as JSON
        echo json_encode(array('status' => 'success', 'data' => $leave_forms));
    } else {
        echo json_encode(array('status' => 'error', 'message' => 'PRN not found'));
    }
} else {
    echo json_encode(array('status' => 'error', 'message' => 'Invalid credentials'));
}

$conn->close();
?>
