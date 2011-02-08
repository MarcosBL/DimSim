<?php

$target_path = "C:/www/archive/static_files/";
//$target_path = "/usr/local/www/archive/static_files/";
$meetingKey = $_REQUEST["meetingKey"];
if (isset($meetingKey)) {

$file = $_FILES['Filedata']['name'];
$name = substr($file,0,strrpos($file,'.'));
$extension = substr($file,strrpos($file,'.'));

@mkdir($target_path.$meetingKey);
$target_path = $target_path.$meetingKey."/"."dimdim_logo".$extension;
}
else {
$target_path = $target_path.basename( $_FILES['Filedata']['name']);
}

if(move_uploaded_file($_FILES['Filedata']['tmp_name'], $target_path))
{ echo "The file ". basename( $_FILES['Filedata']['name']). " has been uploaded"; }
else
{ echo "There was an error uploading the file, please try again!"; }
?>
