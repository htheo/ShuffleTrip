<?php

header('Content-type : bitmap; charset=utf-8');

if(isset($_POST["encoded_string"])){
	
	$encoded_string = $_POST["encoded_string"];
	$image_name = $_POST["image_name"];
	$title = $_POST["title"];
	$describ = $_POST["describ"];
	$ville = $_POST["ville"];

	$decoded_string = base64_decode($encoded_string);

	$path = 'images/'.$image_name;

	$file = fopen($path, 'wb');

	$is_written = fwrite($file, $decoded_string);
	fclose($is_written);

	if($is_written > 0) {

			$connection = mysqli_connect('timotheefxtim.mysql.db','timotheefxtim','Adminparis96','timotheefxtim');
			$query = "INSERT INTO shuffle_articles(image,path,describ,ville,title) values('$image_name','$path','$describ','$ville','$title');";

			$result = mysqli_query($connection, $query);

			if($result){
				echo 'success';
			}else{
				echo 'failed';
			}
			mysql_close($connection);
	}
	
}