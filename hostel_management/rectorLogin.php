<?php
include 'db_connection.php';

header('Content-Type: application/json');

$response = array();

$input = file_get_contents('php://input');
$data = json_decode($input, true);

if ($_SERVER['REQUEST_METHOD'] == 'POST' && $data) {
    $unameh = isset($data['uname']) ? $data['uname'] : '';
    $pwdh = isset($data['passwd']) ? $data['passwd'] : '';

    if (empty($unameh) || empty($pwdh)) {
        $response['error'] = true;
        $response['message'] = "Username and password are required!";
    } else {
        $stmt = $conn->prepare("SELECT * FROM rector WHERE username = ?");
        $stmt->bind_param("s", $unameh);
        $stmt->execute();
        $result = $stmt->get_result();

        if ($result->num_rows > 0) {
            $row = $result->fetch_assoc();
            if ($pwdh === $row['password']) {
                $response['error'] = false;
                $response['message'] = "Login successful!";
                // Additional data can be included here if necessary
            } else {
                $response['error'] = true;
                $response['message'] = "Invalid password!";
            }
        } else {
            $response['error'] = true;
            $response['message'] = "Invalid User!";
        }
        $stmt->close();
    }
} else {
    $response['error'] = true;
    $response['message'] = "Invalid Request!";
}

$conn->close();
echo json_encode($response);
?>
