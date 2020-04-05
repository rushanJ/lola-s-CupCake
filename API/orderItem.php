<?php
include 'config.php';
header("Content-Type: application/json; charset=UTF-8");


$user= $_REQUEST['user'];
$item= $_REQUEST['item'];
    

$sql = 
"INSERT INTO `itemorder` (`id`, `user`, `item`, `status`) VALUES ('','$user','$item','NOT DELIVERED')
;
";
//echo $sql;
if(mysqli_query($conn, $sql)){

    
    $item = $conn->insert_id;        
    $myObj=new \stdClass();
    $myObj->status =true;
    $myJSON = json_encode($myObj);
    echo $myJSON;
} else{
    $myObj=new \stdClass();
    $myObj->status = 0;
    $myJSON = json_encode($myObj);
    echo $myJSON;
}
include "viewOrders.php";
mysqli_close($conn);




?>
