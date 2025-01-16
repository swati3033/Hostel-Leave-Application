<?php
include 'db_connection.php';
// Enable error reporting
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

// Set content type to JSON
header('Content-Type: application/json');



// Get the raw POST data
$data = file_get_contents("php://input");

// Decode the JSON data
$json = json_decode($data, true);

// Log the received JSON for debugging
error_log("Received JSON: " . print_r($json, true));

// Check if the required fields are set
if (isset($json['prn']) && isset($json['acknowledgment']) && isset($json['isNoted'])) {
    $prn = $json['prn'];
    $acknowledgment = $json['acknowledgment'];
    
    // Check if isNoted is set and convert it to "Noted" or ""
    $isNoted = ($json['isNoted'] === "Noted") ? "Noted" : "";

    // Prepare the SQL query
    $sql = "UPDATE leave_forms 
            SET acknowledgement = ?, ack_status = ? 
            WHERE PRN = ? AND status = 'pending'";

    // Prepare statement
    $stmt = $conn->prepare($sql);

    if ($stmt) {
        // Bind parameters
        $stmt->bind_param("sss", $acknowledgment, $isNoted, $prn);

        // Execute statement
        if ($stmt->execute()) {
            $response = array("status" => "success", "message" => "Leave acknowledgment updated successfully.");
        } else {
            $response = array("status" => "error", "message" => "Failed to update leave acknowledgment.");
        }

        // Close statement
        $stmt->close();
    } else {
        $response = array("status" => "error", "message" => "Failed to prepare the SQL statement.");
    }
} else {
    $response = array("status" => "error", "message" => "Invalid input. Please provide 'prn', 'acknowledgment', and 'isNoted'.");
}

// Close the connection
$conn->close();

// Send the response
echo json_encode($response);
?>
