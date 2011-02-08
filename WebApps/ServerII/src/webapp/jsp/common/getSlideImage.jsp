<%
	String presentationsRoot = (String)request.getAttribute("presentationsRoot");
	String conferenceKey = (String)request.getAttribute("conferenceKey");
	String presentationId = (String)request.getAttribute("presentationId");
	boolean thumbnail = ((Boolean)request.getAttribute("thumbnail")).booleanValue();
	int slideIndex = ((Integer)request.getAttribute("slideIndex")).intValue();
	
	String url = null;
	if (conferenceKey != null)
	{
		if (thumbnail)
		{
			url = presentationsRoot+"/"+conferenceKey+"/"+presentationId+"/thumbnails/slide"+slideIndex+".jpg";
		}
		else
		{
			url = presentationsRoot+"/"+conferenceKey+"/"+presentationId+"/slide"+slideIndex+".jpg";
		}
	}
	else
	{
		url = "images/blank_full.jpg";
	}
	response.sendRedirect(url);
%>
