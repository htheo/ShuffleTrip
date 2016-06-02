<?php

header('Content-type : bitmap; charset=utf-8');

if(isset($_POST["encoded_string"])){
	
	$encoded_string = $_POST["encoded_string"];
	$image_name = $_POST["image_name"];

	$decoded_string = base64_decode($encoded_string);

	$path = 'images/'.$image_name;

	$file = fopen($path, 'wb');

	$is_written = fwrite($file, $decoded_string);
	fclose($is_written);

	if($is_written > 0) {

			$db = mysqli_connect('timotheefxtim.mysql.db','timotheefxtim','Adminparis96','timotheefxtim');
			$query = "INSERT INTO suffle_articles(name,path) values('$image_name','$path');";

			$result = mysqli_query($db, $query);

			if($result){
				echo 'success';
			}else{
				echo 'failed';
			}
			mysql_close($db);
	}
	
}