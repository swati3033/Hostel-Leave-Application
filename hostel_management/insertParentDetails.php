
<?php
include 'db_connection.php';

header('Content-Type: application/json');

$response = array();

$input = file_get_contents('php://input');
$data = json_decode($input, true);

if ($_SERVER['REQUEST_METHOD'] == 'POST' && $data) {
    $ptype = isset($data['ptype']) ? $data['ptype'] : '';
    $pnameh = isset($data['parentName']) ? $data['parentName'] : '';
    $pch = isset($data['parentContact']) ? (int)$data['parentContact'] : 0;
    $emailidh = isset($data['Email']) ? $data['email'] : '';


    if (empty($ptype) || empty($pnameh) || empty($pch) || empty($emailidh)) {
        $response['error'] = true;
        $response['message'] = "All fields are required!";
    } else {
        $insert_query = "INSERT INTO parent (ptype, name, contact,email) VALUES ('$ptype','$pnameh','$pch','$emailidh')";
        $result = mysqli_query($conn, $insert_query);

        if ($result) {
            $response['error'] = false;
            $response['message'] = "Parent Details Submitted Sucessfully";
        } else {
            $response['error'] = true;
            $response['message'] = "Failed to submit Parent Details! Error: " . mysqli_error($conn);
        }
    }
} else {
    $response['error'] = true;
    $response['message'] = "Invalid Request!";
}

echo json_encode($response);
?>   