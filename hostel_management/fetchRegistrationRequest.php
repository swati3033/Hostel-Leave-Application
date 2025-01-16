<?php
include 'db_connection.php';

header('Content-Type: application/json');

$response = array();

$input = file_get_contents('php://input');
$data = json_decode($input, true);

if ($_SERVER['REQUEST_METHOD'] == 'POST' && $data) {
    $unameh = isset($data['uname']) ? $data['uname'] : '';
    $pwdh = isset($data['passwd']) ? $data['passwd'] : '';
    $snameh = isset($data['studentName']) ? $data['studentName'] : '';
    $rollh = isset($data['prn']) ? (int)$data['prn'] : 0;
    $tradeh = isset($data['branch']) ? $data['branch'] : '';
    $classsh = isset($data['sclass']) ? $data['sclass'] : '';
    $emailidh = isset($data['email']) ? $data['email'] : '';
    $addrh = isset($data['address']) ? $data['address'] : '';
    $sch = isset($data['studentContact']) ? (int)$data['studentContact'] : 0;
    $pch = isset($data['parentContact']) ? (int)$data['parentContact'] : 0;

    if (empty($unameh) || empty($pwdh) || empty($snameh) || empty($rollh) || empty($tradeh) || empty($classsh) || empty($emailidh) || empty($addrh) || empty($sch) || empty($pch)) {
        $response['error'] = true;
        $response['message'] = "All fields are required!";
    } else {
        $select_query = "SELECT * FROM students WHERE username='$unameh'";
        $display = mysqli_query($conn, $select_query);
        
        if (!$display) {
            $response['error'] = true;
            $response['message'] = "Error in SQL query: " . mysqli_error($conn);
            echo json_encode($response);
            exit();
        }
        
        $count = mysqli_num_rows($display);

        if ($count > 0) {
            $response['error'] = true;
            $response['message'] = "Student is already registered!";
        } else {
            $insert_query = "INSERT INTO students(username, password, stud_name, PRN, branch, class, email, address, stud_contact, parent_contact) VALUES('$unameh', '$pwdh', '$snameh', $rollh, '$tradeh', '$classsh', '$emailidh', '$addrh', $sch, $pch)";
            $result = mysqli_query($conn, $insert_query);

            if ($result) {
                $response['error'] = false;
                $response['message'] = "Student added successfully!";
            } else {
                $response['error'] = true;
                $response['message'] = "Failed to add student! Error: " . mysqli_error($conn);
            }
        }
    }
} else {
    $response['error'] = true;
    $response['message'] = "Invalid Request!";
}

echo json_encode($response);
?>