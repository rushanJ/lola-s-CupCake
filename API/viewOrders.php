<?php
// $id=$_REQUEST["id"];
$tody=date("Y-m-d");
header("Content-Type: application/json; charset=UTF-8");
//include_once "config.php";
$sql = "SELECT `itemorder`.`id`,`itemorder`.`status`,`user`.`name` AS `user`,`item`.`name` AS `item` FROM ((`itemorder` INNER JOIN `user` ON `itemorder`.`user` = `user`.`id`) INNER JOIN `item` ON `itemorder`.`item` = `item`.`id`) WHERE `itemorder`.`status`='NOT DELIVERED'";

$stmt = $conn->prepare($sql);
// $stmt->bind_param("ss", $obj->table, $obj->limit);
$stmt->execute();
$result = $stmt->get_result();
$outp = $result->fetch_all(MYSQLI_ASSOC);


$myObj=new \stdClass();
    $myObj->contacts = $outp;
    $myJSON = json_encode($myObj);
  //  echo $myJSON;

$myfile = fopen("orderList.json", "w") or die("Unable to open file!");
$txt = $myJSON;
fwrite($myfile, $txt);
fclose($myfile);
?>