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

if(isset($_GET['addlike'])&&$id!=0){
    $sql ="UPDATE shuffle_articles
    SET like_nb=like_nb+1 WHERE ID=".$id;
     $req = $conn->prepare($sql);
        $req->execute();
}else{


    if ($_GET['ville']!="") {

        $ville = $_GET['ville'];
        $sql="SELECT * FROM shuffle_articles WHERE ville='". $ville ."' LIMIT 1";
      
    }elseif($id!=0){
        $sql="SELECT * FROM shuffle_articles WHERE ID=". $id;

    }

    else{
        $sql="SELECT * FROM shuffle_articles ORDER BY RAND() LIMIT 1";
        
        
        //mysql_free_result ($result);  
    }
        $req = $conn->prepare($sql);
        $req->execute();
              
        $result = $req->fetchAll(PDO::FETCH_ASSOC);

        foreach($result as $val){
            $output[]=$val;    
            $id=$val['ID'];
          
            $title=$val['title'];
            $describ=$val['describ'];
            $ville=$val['ville'];
            $image=$val['image'];
        }


    print(json_encode($output));
}

}catch(PDOException $e)
    {
    echo $sql . "<br>" . $e->getMessage();
    }

$conn = null;
//}
?>