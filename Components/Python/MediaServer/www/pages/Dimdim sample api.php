<html>
<h1>Start and Join Meeting API</h1>
<body>
<?php
	$ip = "www1.dimdim.com";
	$port = 80;
	$presenteremail = "presenter@dimdim.com";
	$attendeeemail = "attendee@dimdim.com";
	$presenterdisplayname = "Presenter";
	$attendeedisplayname = "Attendee";

	$confname = "Test Meeting";

	// Generating a random key
	srand((double)microtime()*1000000);
	$vowels = array("a", "e", "i", "o", "u");
	$cons = array("b", "c", "d", "g", "h", "j", "k", "l", "m", "n", "p", "r", "s", "t", "u", "v", "w", "tr",
		"cr", "br", "fr", "th", "dr", "ch", "ph", "wr", "st", "sp", "sw", "pr", "sl", "cl");
	$num_vowels = count($vowels);
	$num_cons = count($cons);

	for($i = 0; $i < 5; $i++){
		$password .= $cons[rand(0, $num_cons - 1)] . $vowels[rand(0, $num_vowels - 1)];
	}
	$confkey = $password;
	$lobby = 'true';
	$networkprof = 1;
	$meethrs = 2;
	$meetmin = 0;
	$maxpart = 20;
	$potentialpresenter = "null";
	$meetaudio = "av";
	$meetattendees = " ";
	$meetmike = 2;
	$returnurl = "www.dimdim.com";
	$formload = 'true';
	$starturl = "http://".$ip.":".$port."/dimdim/html/signin/signin.action?action=host&"."email=".$presenteremail."&confKey=".$confkey."&displayName=".$presenterdisplayname."&confName=".$confname."&lobby=".$lobby."&networkProfile=".$networkprof."&meetingHours=".$meethrs."&meetingMinutes=".$meetmin."&maxParticipants=".$maxpart."&presenterAV=".$meetaudio."&attendees=".$meetattendees."&maxAttendeeMikes=".$meetmike."&returnUrl=".$returnurl."&submitFormOnLoad=".$formload."";
    $joinurl = "http://".$ip.":".$port."/dimdim/html/signin/signin.action?action=join&"."email=".$attendeeemail."&confKey=".$confkey."&displayName=".$attendeedisplayname."&submitFormOnLoad=".$formload."";
    echo "<b>Start URL</b>";
    echo "<br>";
    echo $starturl;
    echo "<br>";
    echo "<b>Join URL</b>";
    echo "<br>";
    echo $joinurl;
?>
</body>
</html>