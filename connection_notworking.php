<?php
  header('Content-type : bitmap; charset=utf-8');


$db = mysqli_connect('timotheefxtim.mysql.db','timotheefxtim','Adminparis96','timotheefxtim');

print 'Hello world';
 

  if(isset($_POST["encoded_string"]){
 	
	$encoded_string = $_POST["encoded_string"];
	$image_name = $_POST["image_name"];
	$describ = $_POST["describ"];
	$ville = $_POST["ville"];
	
	$decoded_string = base64_decode($encoded_string);
	
	$path = 'images/'.$image_name;
	
	$file = fopen($path, 'wb');
	
	$is_written = fwrite($file, $decoded_string);
	fclose($file);

		$sql = "INSERT INTO 
            shuffle_articles 
        SET 
        	image = :image,
            title = :title,
            describ = :describ,
            ville = :ville,
            path = :path
                    
        ";

    $req = $db->prepare($sql);
    $req->execute(array(
    	':image'   =>  $image_name,
        ':title'   =>  $title,
        ':describ' =>  $describ,
        ':ville'   =>  $ville,
        ':path'    =>  $path
        		));
		
 }else{
 	print "Erreur, pas d'information(s) envoyée(s)";
 }

 

?>