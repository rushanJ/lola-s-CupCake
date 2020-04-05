<?php
include 'config.php';
header("Content-Type: application/json; charset=UTF-8");


$order= $_REQUEST['order'];

$sql ="UPDATE `itemorder` SET `status` = 'DELIVERED' WHERE `itemorder`.`id` = $order
;
";
//echo $sql;
if(mysqli_query($conn, $sql)){

    $item = $conn->insert_id;
    $myObj=new \stdClass();
    $myObj->success = true;
    $myJSON = json_encode($myObj);
    echo $myJSON;
} else{
    $myObj=new \stdClass();
    $myObj->success = false;
    $myJSON = json_encode($myObj);
    echo $myJSON;
}
include "viewOrders.php";
mysqli_close($conn);



?>
