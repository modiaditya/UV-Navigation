<?php

/* set out document type to text/javascript instead of text/html */
header('Content-Type: application/json');

exec("java -jar uvNavigation.jar ".$_GET['option']." ".$_GET['lat1']." ".$_GET['lng1']." ".$_GET['lat2']." ".$_GET['lng2']."", $output);

echo $output[0];
?>