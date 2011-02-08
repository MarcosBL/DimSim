
function guidPart()
{
 var v1 =  ( ( (1+Math.random())*0x10000) |0) .toString(16).substring(1) ;
 return v1;
}

function guid6Parts()
{
  return guidPart()+guidPart()+guidPart()+guidPart()+guidPart()+guidPart();
}

