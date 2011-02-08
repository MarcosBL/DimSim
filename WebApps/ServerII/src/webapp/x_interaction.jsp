<%
   String confKey = (String)request.getAttribute("confKey");
   String presenterId = (String)request.getAttribute("presenterId");
   String rtmpStream = (String)request.getAttribute("rtmpStream");	//	Presentation ID
   String resourceId = (String)request.getAttribute("resourceId");	//	Resource ID
   String presenterPassKey = (String)request.getAttribute("presenterPassKey");	//	Stream Name
   String rtmpUrl = (String)request.getAttribute("rtmpUrl");	//	Http URL
   String httpUrl = (String)request.getAttribute("httpUrl");	//	Streaming URL
   String wholeDesktop = (String)request.getAttribute("wholeDesktop");	//	Streaming URL
%>
<html>
<head>
<script type="text/javascript">

function startDTP()
{
alert("Starting X Interaction" );

				var meetingId = '<%=confKey%>';
				var presenterId = '<%=presenterId%>';
				var resourceId = '<%=resourceId%>';
				var streamName = '<%=rtmpStream%>';
				var rtmpUrl = '<%=rtmpUrl%>';
				var uploadUrlStr = '<%=httpUrl%>';
				var presenterPassKey = '<%=presenterPassKey%>';
<% if (wholeDesktop.equalsIgnoreCase("true")) { %>
				var shareWholeDesktop = 1;
<% } else { %>
				var shareWholeDesktop = 0;
<% } %>

			 document.embeds["DimdimDesktopPublisher1"].RunDesktopShare(meetingId ,
				presenterId,
				resourceId,
				streamName,
				rtmpUrl,
				uploadUrlStr,
				presenterPassKey,
				shareWholeDesktop);
				
}

</script>

</head>
<body onload="javascript:startDTP()">
	<embed id="DimdimDesktopPublisher1"
		type="application/mozilla-npruntime-scriptable-plugin" width=0 height=0>
</body>
</html>
