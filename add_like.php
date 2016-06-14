<?php


if($_SERVER["REQUEST_METHOD"]=="POST"){
	require 'config.php';
	addlik();

}

function addlik(){
	$user = "timotheefxtim";
	$pass = "Adminparis96";
	$host = "timotheefxtim.mysql.db";
	$dbname = "timotheefxtim";

	$pseudo = $_POST["pseudo"];	
	$post_id = $_POST["post_id"];
	$like = $_POST["like"];
	try {
	    $conn = new PDO("mysql:host=$host;dbname=$dbname", $user, $pass);
	    $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	// check for required fields
	    
	    $sql ="SELECT * FROM shuffle_likes WHERE post_id='".$post_id."'&& pseudo='".$pseudo."'";
	   
	   
	    $req = $conn->prepare($sql);
	    $req->execute();

	    $result = $req->fetchAll(PDO::FETCH_ASSOC);
	    $i=0;
        foreach($result as $val){
            $i++;
        }
        if($i>0){
        	echo "fack";
        }else{
        	addlike($pseudo, $post_id, $like);
        }
	    
    }catch(PDOException $e){
    	echo $sql . "<br>" . $e->getMessage();
    }
}

function addlike($pseudo, $post_id, $like)
{
	global $connect;
	echo "hello !";
	
	$pseudo = $_GET["pseudo"];	
	$post_id = $_GET["post_id"];
	$like = $_GET["like"];


	$query = "INSERT INTO shuffle_likes(pseudo,post_id,likes) VALUES ('$pseudo','$post_id','$like');";

	mysqli_query($connect, $query) or die (mysqli_error($connect));
	mysqli_close($connect);



	addlike2($post_id, $like);
	
}
function addlike2($id,$like){
	$user = "timotheefxtim";
	$pass = "Adminparis96";
	$host = "timotheefxtim.mysql.db";
	$dbname = "timotheefxtim";

	try {
	    $conn = new PDO("mysql:host=$host;dbname=$dbname", $user, $pass);
	    $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	// check for required fields
	    if($like==1){
	    	 $sql ="UPDATE shuffle_articles
	    SET like_nb=like_nb+1 WHERE ID=".$id;
		}else{
			 $sql ="UPDATE shuffle_articles
		    SET like_nb=like_nb-1 WHERE ID=".$id;
		}
	   
	    $req = $conn->prepare($sql);
	    $req->execute();

    }catch(PDOException $e){
    	echo $sql . "<br>" . $e->getMessage();
    }
}

