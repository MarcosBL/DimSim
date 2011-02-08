<?
	if ($_FILES['Filedata']['name'] && ($log = fopen('./upload.log', 'a') ) )
	{
		$file = $_FILES['Filedata']['tmp_name'];
		$error = false;

		/**
		 * THESE ERROR CHECKS ARE JUST EXAMPLES HOW TO USE THE REPONSE HEADERS
		 * TO SEND THE STATUS OF THE UPLOAD, change them!
		 *
		 */

		if (!is_uploaded_file($file) || ($_FILES['Filedata']['size'] > 2 * 1024 * 1024) )
		{
			$error = '400 Bad Request';
		}
		if (!$error && !($size = @getimagesize($file)))
		{
			$error = '409 Conflict';
		}
		if (!$error && !in_array($size[2], array(1, 2, 3, 7, 8) ) )
		{
			$error = '415 Unsupported Media Type';
		}
		if (!$error && ($size[0] < 25) || ($size[1] < 25))
		{
			$error = '417 Expectation Failed';
		}

		/**
		 * This simply writes a log entry
		 */
		fputs($log, ($error ? 'FAILED' : 'SUCCESS') . ' - ' . gethostbyaddr($_SERVER['REMOTE_ADDR']) . ": {$_FILES[Filedata][name]} - {$_FILES[Filedata][size]} byte \n" );
		fclose($log);

		if ($error)
		{
			/**
			 * ERROR DURING UPLOAD, one of the validators failed
			 * 
			 * see FancyUpload.js - onError for header handling
			 */
			header('HTTP/1.0 ' . $error);
			die('Error ' . $error);
		}
		else
		{
			/**
			 * UPLOAD SUCCESSFULL AND VALID
			 *
			 * Use move_uploaded_file here to save the uploaded file in your directory
			 */

		}

		die('Upload Successfull');

	}
?>