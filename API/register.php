<?php
include 'config.php';
header("Content-Type: application/json; charset=UTF-8");


$name= $_POST['name'];
$email= $_POST['email'];
$pass= $_POST['pass'];
    

$sql = 
"INSERT INTO `user` (`id`, `name`, `email`, `pass`) VALUES ('','$name','$email','$pass')
ON DUPLICATE KEY UPDATE
`name`='$name',
`email`='$email',
`pass`='$pass'
;
";
//echo $sql;
if(mysqli_query($conn, $sql)){
    $myObj=new \stdClass();
    $myObj->status = 1;
    $myJSON = json_encode($myObj);
    echo $myJSON;
} else{
    $myObj=new \stdClass();
    $myObj->status = 0;
    $myJSON = json_encode($myObj);
    echo $myJSON;
}
mysqli_close($conn);

?>
