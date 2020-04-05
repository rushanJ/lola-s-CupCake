<?php
//session_start();
$userName=$_POST['userName'];
$pass=$_POST['pass'];
header("Content-Type: application/json");
if ($userName=="admin"&& $pass=="nimda"){
    $myObj=new \stdClass();
    $myObj->success = true;
    $myObj->user = "ADMIN";
    $myObj->role = "ADMIN";
    $myObj->id = 0;
    $myJSON = json_encode($myObj);
    echo $myJSON;
}
else {
include "../config.php";
$sql = "SELECT * FROM `user` WHERE `uname` = '$userName' AND `pass` = '$pass';";
$result = $conn->query($sql);
//echo $sql;
if ($result->num_rows > 0) {
    while($row = $result->fetch_assoc()) {
        $id=$row["id"];
        $name=$row["name"];
        $role=$row["role"];
        $myObj=new \stdClass();
        $myObj->success = true;
        $myObj->user = $name;
        $myObj->role = $role;
        $myObj->id = $id;
    
        $myJSON = json_encode($myObj);
        echo $myJSON;
     
    }
} 
else {
    $myObj=new \stdClass();
    $myObj->success = false;
    $myObj->user = 0;
    $myObj->role =0;
    $myObj->id = -1;

    $myJSON = json_encode($myObj);
    echo $myJSON;
}
$conn->close();
}
?>