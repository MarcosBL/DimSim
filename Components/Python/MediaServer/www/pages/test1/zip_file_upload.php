<?php

function unzip($dir, $file, $verbose = 0) {

	$name = substr($file,0,strrpos($file,'.'));
	$extension = substr($file,strrpos($file,'.'));

   $dir_path = "$dir$name";
   $zip_path = "$dir$file";

   $ERROR_MSGS[0] = "OK";
   $ERROR_MSGS[1] = "Zip path $zip_path doesn't exists.";
   $ERROR_MSGS[2] = "Directory $dir_path for unzip the pack already exists, impossible continue.";
   $ERROR_MSGS[3] = "Error while opening the $zip_path file.";

   $ERROR = 0;

	echo "inside unzip method"."\n dir = ".$dir_path." zippath = ".$zip_path;
   if (file_exists($zip_path)) {
		echo "status: file exists " .$zip_path. "\n";
         if (!file_exists($dir_path)) {
			echo "making directory " .$dir_path. "\n";
            mkdir($dir_path);
		 }
         if (($link = zip_open($zip_path))) {
			echo "inside if open";
            while (($zip_entry = zip_read($link)) && (!$ERROR)) {

               if (zip_entry_open($link, $zip_entry, "r")) {

                  $data = zip_entry_read($zip_entry, zip_entry_filesize($zip_entry));
                  $dir_name = dirname(zip_entry_name($zip_entry));
                  $name = zip_entry_name($zip_entry);

                  if ($name[strlen($name)-1] == '/') {

                        $base = "$dir_path/";

                     foreach ( explode("/", $name) as $k) {

                        $base .= "$k/";

                        if (!file_exists($base))
                           mkdir($base);

                     }

                  }
                  else {

                      $name = "$dir_path/$name";

                      #if ($verbose)
                       echo "\n extracting: $name<br>";

                    $stream = fopen($name, "w");
                    fwrite($stream, $data);

                  }

                  zip_entry_close($zip_entry);

               }
               else
                  $ERROR = 4;

              }

              zip_close($link);

           }
           else
              $ERROR = "3";
       }
       else
          $ERROR = 2;
    #}
    #else
    #   $ERROR = 1;

   return $ERROR_MSGS[$ERROR];

}


$target_path = "C:/www/archive/static_files/";
$meetingKey = $_REQUEST["meetingKey"];
if (isset($meetingKey)) {
	@mkdir($target_path.$meetingKey);
	$target_path = $target_path.$meetingKey."/".$_FILES['Filedata']['name'];
}
else {
	$target_path = $target_path.basename( $_FILES['Filedata']['name']);
}

if(move_uploaded_file($_FILES['Filedata']['tmp_name'], $target_path))
{ echo "The file ". basename( $_FILES['Filedata']['name']). " has been uploaded";
  echo "The file uploaded to ".$target_path;

  $za = new ZipArchive();
	echo "created zip archive..";
  $za->open($target_path);

  print_r($za);
  var_dump($za);
  echo "numFiles: " . $za->numFiles . "\n";
  echo "status: " . $za->status  . "\n";
  echo "statusSys: " . $za->statusSys . "\n";
  echo "filename: " . $za->filename . "\n";
  echo "comment: " . $za->comment . "\n";

  #for ($i=0; $i<$za->numFiles;$i++) {
  #    echo "index: $i\n";
  #    print_r($za->statIndex($i));
  #}
  echo "numFile:" . $za->numFiles . "\n";

  unzip("C:/www/archive/static_files/".$meetingKey."/", $_FILES['Filedata']['name'], 1);
}
else
{ echo "There was an error uploading the file, please try again!"; }
?>
