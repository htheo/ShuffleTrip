<?php


if($_SERVER["REQUEST_METHOD"]=="POST"){
	require 'config.php';
	addlike();
}


function addlike()
{
	global $connect;
	
	$pseudo = $_POST["pseudo"];	
	$post_id = $_POST["post_id"];
	$like = $_POST["like"];

	$query = "INSERT INTO shuffle_likes(pseudo,post_id,likes) VALUES ('$pseudo','$post_id','$like');";

	mysqli_query($connect, $query) or die (mysqli_error($connect));
	mysqli_close($connect);
	
}
