<?php
$user = "timotheefxtim";
$pass = "Adminparis96";
$host = "timotheefxtim.mysql.db";
$dbname = "timotheefxtim";

/*
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
    $sql="SELECT * FROM shuffle_articles WHERE ville='". $ville ."' LIMIT 1";
  
}elseif(isset($_GET['ID'])&&$_GET['ID']>0){
    $ID = $_GET['ID'];
    $sql="SELECT * FROM shuffle_articles WHERE ID='". $ID ."'";

}else{
    $ID = 1;
    $sql="SELECT * FROM shuffle_articles order by RAND() LIMIT 1";
    
    
    //mysql_free_result ($result);  
}
    $req = $conn->prepare($sql);
    $req->execute();
          
    $result = $req->fetchAll(PDO::FETCH_ASSOC);

    foreach($result as $val){
        $output[]=$val;    
        $title=$val['title'];
        $describ=$val['describ'];
        $ville=$val['ville'];
        $image=$val['image'];
    }
print(json_encode($output));

}catch(PDOException $e)
    {
    echo $sql . "<br>" . $e->getMessage();
    }

$conn = null;
//}
?>