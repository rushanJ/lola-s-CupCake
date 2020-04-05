<?php
include 'config.php';
header("Content-Type: application/json; charset=UTF-8");


$name= $_POST['name'];
$price= $_POST['price'];
$description= $_POST['description'];
    

$sql = 
"INSERT INTO `item` (`id`, `name`, `price`, `description`) VALUES ('','$name','$price','$description')
ON DUPLICATE KEY UPDATE
`name`='$name',
`price`='$price',
`description`='$description'
;
";
//echo $sql;
if(mysqli_query($conn, $sql)){

    $item = $conn->insert_id;


            
        $target_dir = "images/";
        $target_file = $target_dir . $item.".jpg";
        $uploadOk = 1;
        $imageFileType = strtolower(pathinfo($target_file,PATHINFO_EXTENSION));
        // Check if image file is a actual image or fake image
        if(isset($_POST["submit"])) {
            $check = getimagesize($_FILES["img"]["tmp_name"]);
            if($check !== false) {
                echo "File is an image - " . $check["mime"] . ".";
                $uploadOk = 1;
            } else {
                echo "File is not an image.";
                $uploadOk = 0;
            }
        }
        // Check if file already exists
        if (file_exists($target_file)) {
            echo "Sorry, file already exists.";
            $uploadOk = 0;
        }
        // Check file size
        if ($_FILES["img"]["size"] > 50000000) {
            echo "Sorry, your file is too large.";
            $uploadOk = 0;
        }
        // Allow certain file formats
        // if($imageFileType != "jpg" && $imageFileType != "png" && $imageFileType != "jpeg"
        // && $imageFileType != "gif" ) {
        //     echo "Sorry, only JPG, JPEG, PNG & GIF files are allowed.";
        //     $uploadOk = 0;
        // }
        // Check if $uploadOk is set to 0 by an error
        if ($uploadOk == 0) {
            echo "Sorry, your file was not uploaded.";
        // if everything is ok, try to upload file
        } else {
            if (move_uploaded_file($_FILES["img"]["tmp_name"], $target_file)) {
                // echo "The file ". basename( $_FILES["img"]["name"]). " has been uploaded.";
            } else {
                // echo "Sorry, there was an error uploading your file.";
            }
        }
            

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
include "viewItem.php";
mysqli_close($conn);


?>
