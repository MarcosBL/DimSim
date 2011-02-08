// JavaScript Document
function screen()
{
	document.getElementById('screentd').className = 'show';
}

function trail()
{
	document.getElementById('trailtd').className = 'show';
}

function downlo()
{
	document.getElementById('downloadtd').className = 'show';
}

function downlo1()
{
	document.getElementById('downloadtd').className = 'show';
}

function invite()
{
	document.getElementById('form').className = 'Hide';
}

function invite_paypal()
{
	//document.getElementById('lnk1').className = 'Hide';
	//document.getElementById('invit').className = 'Show';
	$('premiumusers_tbg').className = 'Show';
}

function invite1(edtion)
{
	edition_type=edtion;
//	alert(edition_type);
	//document.getElementById('lnk1').className = 'Hide';
	//document.getElementById('invit').className = 'Show';
		$('sales_form').className = 'Show';
	if(edition_type=='freehosted')
	{
		$('form_free_enterprise').className = 'Show';
		$('form_hosted_enterprise').className = 'Hide';
//		alert($('hiddenField').value);
	}
	else{
		$('form_free_enterprise').className = 'Hide';
		$('form_hosted_enterprise').className = 'Show';
//				alert($('hiddenField').value);	
	}
	
}

function sf_form_close()
{
	//document.getElementById('lnk1').className = 'Hide';
	//document.getElementById('invit').className = 'Show';
	$('sales_form').className = 'Hide';
	$('premiumusers_tbg').className = 'Hide';
}

function scoop()
{
	document.getElementById('scoopform').className = 'Show';
		document.getElementById('scoop').className = 'Hide';
}
function pp()
{
window.open('../ppolicy','newwindow1','toolbar=0,top=0,left=0,width=550, height=390,scrollbars=1,resizable=1');
}
function tm()
{
window.open('../tm','newwindow1','toolbar=0,top=0,left=0,width=550, height=390,scrollbars=1,resizable=1');
}
document.write('<tr><td></td></tr><tr><td align="center"><br /><div id="footer"><div class="contextcopy">&nbsp;&nbsp;&nbsp;&nbsp;Copyright &copy; 2008 dimdim Inc. All rights reserved.<br/>&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:pp()">Privacy Policy</a> |  <a href="javascript:tm()">Trademark &amp; Copyright Policy</a> |  <a href="../aboutus/dimdim_managementteam.html">About us</a></div></div><br /></td></tr>');

//document.write('<tr><td align="center"><a href="http://del.icio.us/post?url=http%3A%2F%2Fwww.iconshock.com%2Fvista-icons.php&amp;title=Vista+icons" target="_blank"><img src="../images/delicious.png" style="border: 0pt none ;" alt="Del.icio.us" height="16" width="16"></a>&nbsp;&nbsp;<a href="http://furl.net/storeIt.jsp?u=http%3A%2F%2Fwww.iconshock.com%2Fvista-icons.php&amp;t=Vista+icons" target="_blank"><img src="../images/furl.gif" style="border: 0pt none ;" alt="FURL" height="16" width="16"></a>&nbsp;&nbsp;<a href="http://myweb2.search.yahoo.com/myresults/bookmarklet?u=http%3A%2F%2Fwww.iconshock.com%2Fvista-icons.php&amp;t=Vista+icons" target="_blank"><img src="../images/yahoomyweb.png" style="border: 0pt none ;" alt="Yahoo" height="16" width="16"></a>&nbsp;&nbsp;<a href="http://www.google.com/bookmarks/mark?op=edit&amp;bkmk=http%3A%2F%2Fwww.iconshock.com%2Fvista-icons.php&amp;title=Vista+icons" target="_blank"><img src="../images/Google.gif" style="border: 0pt none ;" alt="Google Bookmarks" height="16" width="16"></a>&nbsp;&nbsp;<a href="http://blinklist.com/index.php?Action=Blink/addblink.php&amp;Url=http%3A%2F%2Fwww.iconshock.com%2Fvista-icons.php&amp;Title=Vista+icons" target="_blank"><img src="../images/blinklist.png" style="border: 0pt none ;" alt="Blinklist" height="16" width="16"></a>&nbsp;&nbsp;<a href="http://digg.com/submit?phase=2&amp;url=http%3A%2F%2Fwww.iconshock.com%2Fvista-icons.php&amp;title=Vista+icons" target="_blank"><img src="../images/digg.png" style="border: 0pt none ;" alt="Digg" height="16" width="16"></a>&nbsp;&nbsp;<a href="http://ma.gnolia.com/bookmarklet/add?url=http%3A%2F%2Fwww.iconshock.com%2Fvista-icons.php&amp;title=Vista+icons" target="_blank"><img src="../images/magnolia.png" style="border: 0pt none ;" alt="Ma.gnolia" height="16" width="16"></a>&nbsp;&nbsp;													<a href="http://www.stumbleupon.com/submit?url=http%3A%2F%2Fwww.iconshock.com%2Fvista-icons.php&amp;title=Vista+icons" target="_blank"><img src="../images/su.png" style="border: 0pt none ;" alt="StumbleUpon" height="16" width="16"></a>&nbsp;&nbsp;													<a href="http://www.technorati.com/faves?add=http%3A%2F%2Fwww.iconshock.com%2Fvista-icons.php" target="_blank"><img src="../images/technorati.jpg" style="border: 0pt none ;" alt="Technorati" height="16" width="16"></a>&nbsp;&nbsp;													<a href="http://www.newsvine.com/_wine/save?u=http%3A%2F%2Fwww.iconshock.com%2Fvista-icons.php&amp;h=Vista+icons" target="_blank"><img src="../images/newsvine.jpg" style="border: 0pt none ;" alt="Newsvine" height="16" width="16"></a>&nbsp;&nbsp;													<a href="http://reddit.com/submit?url=http%3A%2F%2Fwww.iconshock.com%2Fvista-icons.php&amp;title=Vista+icons" target="_blank"><img src="../images/reddit.png" style="border: 0pt none ;" alt="Reddit" height="16" width="16"></a>&nbsp;&nbsp;													<a href="http://tailrank.com/?link_href=http%3A%2F%2Fwww.iconshock.com%2Fvista-icons.php&amp;titleVista+icons" target="_blank"><img src="../images/tailrank.bmp" style="border: 0pt none ;" alt="Tailrank" height="16" width="16"></a>&nbsp;&nbsp;													<a href="http://bluedot.us/Authoring.aspx?u=http%3A%2F%2Fwww.iconshock.com%2Fvista-icons.php&amp;t=Vista+icons" target="_blank"><img src="../images/bluedot.png" style="border: 0pt none ;" alt="Bluedot" height="16" width="16"></a>&nbsp;&nbsp;													<a href="http://netvouz.com/action/submitBookmark?url=http%3A%2F%2Fwww.iconshock.com%2Fvista-icons.php&amp;title=Vista+icons" target="_blank"><img src="../images/Netvouz.jpg" style="border: 0pt none ;" alt="Netvouz" height="16" width="16"></a>&nbsp;&nbsp;													<a href="http://blogmarks.net/my/new.php?mini=1&amp;simple=1&amp;url=http%3A%2F%2Fwww.iconshock.com%2Fvista-icons.php&amp;title=Vista+icons" target="_blank"><img src="../images/blogmarks.png" style="border: 0pt none ;" alt="Blogmarks" height="16" width="16"></a>&nbsp;&nbsp;													<a href="http://www.simpy.com/simpy/LinkAdd.do?href=http%3A%2F%2Fwww.iconshock.com%2Fvista-icons.php" target="_blank"><img src="../images/simpy.png" style="border: 0pt none ;" alt="Simpy" height="16" width="16"></a>&nbsp;&nbsp;													<a href="http://www.diigo.com/post?url=http%3A%2F%2Fwww.iconshock.com%2Fvista-icons.php&amp;title=Vista+icons" target="_blank"><img src="../images/diigo.gif" style="border: 0pt none ;" alt="Diigo" height="16" width="16"></a>&nbsp;&nbsp;													<a href="http://friendsite.com/users/bookmarks/?u=http%3A%2F%2Fwww.iconshock.com%2Fvista-icons.php" target="_blank"><img src="../images/friends.gif" style="border: 0pt none ;" alt="Friendsite" height="16" width="16"></a>&nbsp;&nbsp;		</td></tr>');


function con()
{
	document.getElementById('contact').className = 'show';
	document.getElementById('lnk').className = 'Hide';	
}

function down()
{
	document.getElementById('contact').className = 'Hide';
	document.getElementById('lnk').className = 'Hide';	
	document.getElementById('downloadtd').className = 'show';
}