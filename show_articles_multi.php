<?php
$user = "timotheefxtim";
$pass = "Adminparis96";
$host = "timotheefxtim.mysql.db";
$dbname = "timotheefxtim";/*
 * Following code will create a new product row
 * All product details are read from HTTP Post Request
 */
 
// array for JSON response
$response = array();
if(isset($_GET['ID'])){
    $id=$_GET['ID'];
}else{
    $id=0;
}
try {
    $conn = new PDO("mysql:host=$host;dbname=$dbname", $user, $pass);
    $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
// check for required fields
if ($_GET['ville']!="") {
    $ville = $_GET['ville'];
     $sql="SELECT * FROM shuffle_articles WHERE ID>'".$id."'&& ville='". $ville ."' LIMIT 15";

}
elseif(isset($_GET['recherche'])){
    $recherche = $_GET['recherche'];
    
     $sql="SELECT * FROM shuffles_articles WHERE ID>'". $id ."'";

}else{
    
     $sql="SELECT * FROM shuffle_articles WHERE ID>'".$id."'";
    
    //mysql_free_result ($result);  
}
 
    $req = $conn->prepare($sql);
    $req->execute();
          
    $result = $req->fetchAll(PDO::FETCH_ASSOC);
    $i = 0;
    foreach($result as $val){
        $output['Articles'][] = $val;  
        $currentId=$val['ID']; 
        $title=$val['title'];
        $describ=$val['describ'];
        $ville=$val['ville'];
        $i++;
    }

print(json_encode($output));
}catch(PDOException $e)
    {
    echo $sql . "<br>" . $e->getMessage();
    }

$conn = null;
//}
?>