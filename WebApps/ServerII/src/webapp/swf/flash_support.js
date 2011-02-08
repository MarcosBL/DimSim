
/**
 *	Primary method that writes the movie into the page.
 */
 
createAndWriteMovieElement = function(element, movieUrl, id2, height, width, version, color)
{
	var so = new SWFObject(movieUrl, id2, height, width, version, color);
	so.addParam("WMODE","OPAQUE");
	so.write(element);
}

setSlideIndexInFlash = function(nodeId, slideIndex)
{
	var e = document.getElementById(nodeId);
	if (e.setSlideIndex)
	{
		e.setSlideIndex(slideIndex);
	}
}

sendAnnotationMessageInFlash = function(nodeId, message)
{
	var e = document.getElementById(nodeId);
	if (e.changeAnnotations)
	{
		e.changeAnnotations(message);
	}
}


/**
 *	This callback function is always called from from within a flash
 *	movie through the exteral interface.
 */

function callFromFlash(streamName,eventName)
{
//	alert("PPT EVENT:"+streamName+"--"+eventName);
	getTopWindow(window).callFromFlash(streamName,eventName);
}

function getTopWindow(w)
{
	if (w.parent == w)
	{
		return w;
	}
	else
	{
		return getTopWindow(w.parent);
	}
}

/**
 *	This mean that the external interface registration from within the
 *	flash movie control is complete. This function may or may not be used
 *	by each movie.
 */
 
function flashMovieLoaded()
{
}

