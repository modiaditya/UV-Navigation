<?php session_start(); ?>
<html>


<script type="text/javascript">

function sayMessage(message)
{
	alert(message);
	sayHello();
}

function sayHello()
{
	var v = "<?php if(isset($_SESSION['views']) echo $_SESSION['views']; else echo 'lite' ?> ";
	alert(v);
}


</script>
<?php

//header('Content-Type: application/json');
// exec("java -jar uvNavigation.jar 34.06649 -118.452984 34.059468 -118.447705", $output);


//exec("/usr/bin/java -jar testJar.jar");

$_SESSION['views'] = "Hi";
//print_r($output);

$myValue = "Hi ";

echo '<script type="text/javascript">'
   , 'sayHello();'
   , '</script>';
?>





</html>