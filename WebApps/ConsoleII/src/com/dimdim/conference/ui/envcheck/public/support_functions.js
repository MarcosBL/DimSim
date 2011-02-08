
/**
 *	This function returns and integer as
 *	0 - good email,
 *	1 - bad email,
 *	2 - may be bad email.
 */
 
validateEmail  = function( givenEmail )
{
	var email = /^[^@]+@[^@.]+\.[^@]*\w\w$/  ;
	
	var regExpEmail = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*\.(\w{2}|(com|net|org|edu|int|mil|gov|arpa|biz|aero|name|coop|info|pro|museum))$/;
	
	if (!regExpEmail.test(givenEmail))
	{
		return 1;
	}
	
	if (!email.test(givenEmail))
	{
		return 1;
	}

	var email2 = /^[A-Za-z][\w.-]+@\w[\w.-]+\.[\w.-]*[A-Za-z][A-Za-z]$/  ;
	if (!email2.test(givenEmail))
	{
		return 2;
	}
  
	return 0;
}

