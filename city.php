<?php

include('config.php');
/*
 * Following code will create a new product row
 * All product details are read from HTTP Post Request
 */

// array for JSON response
$response = array();

// check for required fields
if (isset($_GET['title']) && isset($_GET['describ']) && isset($_GET['ville'])) {

    $title = $_GET['title'];
    $describ = $_GET['describ'];
    $ville = $_GET['ville'];

    /*$title="Un autre chateau";
    $describ="Il est cool aussi";
    $ville = "Paris";*/
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