<?php
 header('Content-type : bitmap; charset=utf-8');

$db = mysqli_connect('timotheefxtim.mysql.db','timotheefxtim','Adminparis96','timotheefxtim');

 
 if(isset($_POST["encoded_string"])){
 	
	$encoded_string = $_POST["encoded_string"];
	$image_name = $_POST["image_name"];
	
	$decoded_string = base64_decode($encoded_string);
	
	$path = 'images/'.$image_name;
	
	$file = fopen($path, 'wb');
	
	$is_written = fwrite($file, $decoded_string);
	fclose($file);
	
	if($is_written > 0) {
				

//		$connection = mysqli_connect('timotheefxtim.mysql.db','timotheefxtim','Adminparis96','timotheefxtim');

		// $connection = mysqli_connect('localhost', 'root', '','tutorial3');
		$query = "INSERT INTO shuffle_articles(photo,path) values('$image_name','$path');";
		
		$result = mysqli_query($db, $query) ;
		
		if($result){
			echo "success";
		}else{
			echo "failed";
		}
		
		mysqli_close($db);
	}
 }


// array for JSON response
$response = array();

// check for required fields
if (isset($_GET['title']) && isset($_GET['describ']) && isset($_GET['ville'])) {

	$title = $_GET['title'];
	$describ = $_GET['describ'];
	$ville = $_GET['ville'];

	// include db connect class
	//require_once __DIR__ . '/connect.php';

	$sql = "INSERT INTO 
            shuffle_articles 
        SET 
            title = :title,
            describ = :describ,
            ville = :ville
        ";
	$req = $db->prepare($sql);
	$req->execute(array(
		':title'   =>  $title,
		':describ' =>  $describ,
		':ville' => $ville

	));

}
?>