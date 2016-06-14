<?php

$user = "timotheefxtim";
$pass = "Adminparis96";
$host = "timotheefxtim.mysql.db";
$dbname = "timotheefxtim";

	$con = mysqli_connect('timotheefxtim.mysql.db','timotheefxtim','Adminparis96','timotheefxtim'); or die("connection failed");
	mysql_select_db($dbname,$con) or die("db selection failed");
	 
	$pseudo=$_REQUEST['pseudo'];
	$post_id=$_REQUEST['post_id'];
	$like=$_REQUEST['like'];

	$flag['code']=0;

	if($r=mysql_query("insert into shuffle_likes values('$pseudo','$post_id','$like') ",$con))
	{
		$flag['code']=1;
		echo"hi";
	}

	print(json_encode($flag));
	mysql_close($con);