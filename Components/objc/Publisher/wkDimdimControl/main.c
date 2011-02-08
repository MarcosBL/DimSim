/*
 IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. ("Apple") in
 consideration of your agreement to the following terms, and your use, installation, 
 modification or redistribution of this Apple software constitutes acceptance of these 
 terms.  If you do not agree with these terms, please do not use, install, modify or 
 redistribute this Apple software.
 
 In consideration of your agreement to abide by the following terms, and subject to these 
 terms, Apple grants you a personal, non-exclusive license, under Apple’s copyrights in 
 this original Apple software (the "Apple Software"), to use, reproduce, modify and 
 redistribute the Apple Software, with or without modifications, in source and/or binary 
 forms; provided that if you redistribute the Apple Software in its entirety and without 
 modifications, you must retain this notice and the following text and disclaimers in all 
 such redistributions of the Apple Software.  Neither the name, trademarks, service marks 
 or logos of Apple Computer, Inc. may be used to endorse or promote products derived from 
 the Apple Software without specific prior written permission from Apple. Except as expressly
 stated in this notice, no other rights or licenses, express or implied, are granted by Apple
 herein, including but not limited to any patent rights that may be infringed by your 
 derivative works or by other works in which the Apple Software may be incorporated.
 
 The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, 
 EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, 
 MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS 
 USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.
 
 IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL 
 DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS 
 OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, 
 REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND 
 WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR 
 OTHERWISE, EVEN IF APPLE HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

#import "includes.h"
#import "PluginObject.h"

NPNetscapeFuncs* browser;

NPError NPP_New(NPMIMEType pluginType, NPP instance, uint16 mode, int16 argc, char* argn[], char* argv[], NPSavedData* saved);
NPError NPP_Destroy(NPP instance, NPSavedData** save);
NPError NPP_SetWindow(NPP instance, NPWindow* window);
NPError NPP_NewStream(NPP instance, NPMIMEType type, NPStream* stream, NPBool seekable, uint16* stype);
NPError NPP_DestroyStream(NPP instance, NPStream* stream, NPReason reason);
int32   NPP_WriteReady(NPP instance, NPStream* stream);
int32   NPP_Write(NPP instance, NPStream* stream, int32 offset, int32 len, void* buffer);
void    NPP_StreamAsFile(NPP instance, NPStream* stream, const char* fname);
void    NPP_Print(NPP instance, NPPrint* platformPrint);
int16   NPP_HandleEvent(NPP instance, void* event);
void    NPP_URLNotify(NPP instance, const char* URL, NPReason reason, void* notifyData);
NPError NPP_GetValue(NPP instance, NPPVariable variable, void *value);
NPError NPP_SetValue(NPP instance, NPNVariable variable, void *value);

#pragma export on
// Mach-o entry points
NPError NP_Initialize(NPNetscapeFuncs *browserFuncs);
NPError NP_GetEntryPoints(NPPluginFuncs *pluginFuncs);
void NP_Shutdown(void);
// For compatibility with CFM browsers.
int main(NPNetscapeFuncs *browserFuncs, NPPluginFuncs *pluginFuncs, NPP_ShutdownProcPtr *shutdown);
#pragma export off

typedef void (* FunctionPointer)(void);
typedef void (* TransitionVector)(void);
static FunctionPointer functionPointerForTVector(TransitionVector);
static TransitionVector tVectorForFunctionPointer(FunctionPointer);

// Mach-o entry points
NPError NP_Initialize(NPNetscapeFuncs* browserFuncs)
{
    browser = browserFuncs;
    return NPERR_NO_ERROR;
}

NPError NP_GetEntryPoints(NPPluginFuncs* pluginFuncs)
{
    pluginFuncs->version = 11;
    pluginFuncs->size = sizeof(pluginFuncs);
    pluginFuncs->newp = NPP_New;
    pluginFuncs->destroy = NPP_Destroy;
    pluginFuncs->setwindow = NPP_SetWindow;
    pluginFuncs->newstream = NPP_NewStream;
    pluginFuncs->destroystream = NPP_DestroyStream;
    pluginFuncs->asfile = NPP_StreamAsFile;
    pluginFuncs->writeready = NPP_WriteReady;
    pluginFuncs->write = (NPP_WriteProcPtr)NPP_Write;
    pluginFuncs->print = NPP_Print;
    pluginFuncs->event = NPP_HandleEvent;
    pluginFuncs->urlnotify = NPP_URLNotify;
    pluginFuncs->getvalue = NPP_GetValue;
    pluginFuncs->setvalue = NPP_SetValue;
    
    return NPERR_NO_ERROR;
}

void NP_Shutdown(void)
{
}

// For compatibility with CFM browsers.
int main(NPNetscapeFuncs *browserFuncs, NPPluginFuncs *pluginFuncs, NPP_ShutdownProcPtr *shutdown)
{
    browser = malloc(sizeof(NPNetscapeFuncs));
    bzero(browser, sizeof(NPNetscapeFuncs));
    
    browser->size = browserFuncs->size;
    browser->version = browserFuncs->version;
    
    // Since this is a mach-o plug-in and the browser is CFM because it is calling main, translate
    // our function points into TVectors so the browser can call them.
    browser->geturl = (NPN_GetURLProcPtr)functionPointerForTVector((TransitionVector)browserFuncs->geturl);
    browser->posturl = (NPN_PostURLProcPtr)functionPointerForTVector((TransitionVector)browserFuncs->posturl);
    browser->requestread = (NPN_RequestReadProcPtr)functionPointerForTVector((TransitionVector)browserFuncs->requestread);
    browser->newstream = (NPN_NewStreamProcPtr)functionPointerForTVector((TransitionVector)browserFuncs->newstream);
    browser->write = (NPN_WriteProcPtr)functionPointerForTVector((TransitionVector)browserFuncs->write);
    browser->destroystream = (NPN_DestroyStreamProcPtr)functionPointerForTVector((TransitionVector)browserFuncs->destroystream);
    browser->status = (NPN_StatusProcPtr)functionPointerForTVector((TransitionVector)browserFuncs->status);
    browser->uagent = (NPN_UserAgentProcPtr)functionPointerForTVector((TransitionVector)browserFuncs->uagent);
    browser->memalloc = (NPN_MemAllocProcPtr)functionPointerForTVector((TransitionVector)browserFuncs->memalloc);
    browser->memfree = (NPN_MemFreeProcPtr)functionPointerForTVector((TransitionVector)browserFuncs->memfree);
    browser->memflush = (NPN_MemFlushProcPtr)functionPointerForTVector((TransitionVector)browserFuncs->memflush);
    browser->reloadplugins = (NPN_ReloadPluginsProcPtr)functionPointerForTVector((TransitionVector)browserFuncs->reloadplugins);
    browser->geturlnotify = (NPN_GetURLNotifyProcPtr)functionPointerForTVector((TransitionVector)browserFuncs->geturlnotify);
    browser->posturlnotify = (NPN_PostURLNotifyProcPtr)functionPointerForTVector((TransitionVector)browserFuncs->posturlnotify);
    browser->getvalue = (NPN_GetValueProcPtr)functionPointerForTVector((TransitionVector)browserFuncs->getvalue);
    browser->setvalue = (NPN_SetValueProcPtr)functionPointerForTVector((TransitionVector)browserFuncs->setvalue);
    browser->invalidaterect = (NPN_InvalidateRectProcPtr)functionPointerForTVector((TransitionVector)browserFuncs->invalidaterect);
    browser->invalidateregion = (NPN_InvalidateRegionProcPtr)functionPointerForTVector((TransitionVector)browserFuncs->invalidateregion);
    browser->forceredraw = (NPN_ForceRedrawProcPtr)functionPointerForTVector((TransitionVector)browserFuncs->forceredraw);
    browser->getJavaEnv = (NPN_GetJavaEnvProcPtr)functionPointerForTVector((TransitionVector)browserFuncs->getJavaEnv);
    browser->getJavaPeer = (NPN_GetJavaPeerProcPtr)functionPointerForTVector((TransitionVector)browserFuncs->getJavaPeer);
    
    // Scripting functions appeared in NPAPI version 14
    if (browser->version >= 14) {
        browser->getstringidentifier = (NPN_GetStringIdentifierProcPtr)functionPointerForTVector((TransitionVector)browserFuncs->getstringidentifier);
        browser->getstringidentifiers = (NPN_GetStringIdentifiersProcPtr)functionPointerForTVector((TransitionVector)browserFuncs->getstringidentifiers);
        browser->getintidentifier = (NPN_GetIntIdentifierProcPtr)functionPointerForTVector((TransitionVector)browserFuncs->getintidentifier);
        browser->identifierisstring = (NPN_IdentifierIsStringProcPtr)functionPointerForTVector((TransitionVector)browserFuncs->identifierisstring);
        browser->utf8fromidentifier = (NPN_UTF8FromIdentifierProcPtr)functionPointerForTVector((TransitionVector)browserFuncs->utf8fromidentifier);
        browser->createobject = (NPN_CreateObjectProcPtr)functionPointerForTVector((TransitionVector)browserFuncs->createobject);
        browser->retainobject = (NPN_RetainObjectProcPtr)functionPointerForTVector((TransitionVector)browserFuncs->retainobject);
        browser->releaseobject = (NPN_ReleaseObjectProcPtr)functionPointerForTVector((TransitionVector)browserFuncs->releaseobject);
        browser->invoke = (NPN_InvokeProcPtr)functionPointerForTVector((TransitionVector)browserFuncs->invoke);
        browser->invokeDefault = (NPN_InvokeDefaultProcPtr)functionPointerForTVector((TransitionVector)browserFuncs->invokeDefault);
        browser->evaluate = (NPN_EvaluateProcPtr)functionPointerForTVector((TransitionVector)browserFuncs->evaluate);
        browser->getproperty = (NPN_GetPropertyProcPtr)functionPointerForTVector((TransitionVector)browserFuncs->getproperty);
        browser->setproperty = (NPN_SetPropertyProcPtr)functionPointerForTVector((TransitionVector)browserFuncs->setproperty);
        browser->removeproperty = (NPN_RemovePropertyProcPtr)functionPointerForTVector((TransitionVector)browserFuncs->removeproperty);
        browser->hasproperty = (NPN_HasPropertyProcPtr)functionPointerForTVector((TransitionVector)browserFuncs->hasproperty);
        browser->hasmethod = (NPN_HasMethodProcPtr)functionPointerForTVector((TransitionVector)browserFuncs->hasmethod);
        browser->releasevariantvalue = (NPN_ReleaseVariantValueProcPtr)functionPointerForTVector((TransitionVector)browserFuncs->releasevariantvalue);
        browser->setexception = (NPN_SetExceptionProcPtr)functionPointerForTVector((TransitionVector)browserFuncs->setexception);
    }
    
    pluginFuncs->version = 11;
    pluginFuncs->size = sizeof(pluginFuncs);
    pluginFuncs->newp = (NPP_NewProcPtr)tVectorForFunctionPointer((FunctionPointer)NPP_New);
    pluginFuncs->destroy = (NPP_DestroyProcPtr)tVectorForFunctionPointer((FunctionPointer)NPP_Destroy);
    pluginFuncs->setwindow = (NPP_SetWindowProcPtr)tVectorForFunctionPointer((FunctionPointer)NPP_SetWindow);
    pluginFuncs->newstream = (NPP_NewStreamProcPtr)tVectorForFunctionPointer((FunctionPointer)NPP_NewStream);
    pluginFuncs->destroystream = (NPP_DestroyStreamProcPtr)tVectorForFunctionPointer((FunctionPointer)NPP_DestroyStream);
    pluginFuncs->asfile = (NPP_StreamAsFileProcPtr)tVectorForFunctionPointer((FunctionPointer)NPP_StreamAsFile);
    pluginFuncs->writeready = (NPP_WriteReadyProcPtr)tVectorForFunctionPointer((FunctionPointer)NPP_WriteReady);
    pluginFuncs->write = (NPP_WriteProcPtr)tVectorForFunctionPointer((FunctionPointer)NPP_Write);
    pluginFuncs->print = (NPP_PrintProcPtr)tVectorForFunctionPointer((FunctionPointer)NPP_Print);
    pluginFuncs->event = (NPP_HandleEventProcPtr)tVectorForFunctionPointer((FunctionPointer)NPP_HandleEvent);
    pluginFuncs->urlnotify = (NPP_URLNotifyProcPtr)tVectorForFunctionPointer((FunctionPointer)NPP_URLNotify);
    pluginFuncs->getvalue = (NPP_GetValueProcPtr)tVectorForFunctionPointer((FunctionPointer)NPP_GetValue);
    pluginFuncs->setvalue = (NPP_SetValueProcPtr)tVectorForFunctionPointer((FunctionPointer)NPP_SetValue);
    
    *shutdown = (NPP_ShutdownProcPtr)tVectorForFunctionPointer((FunctionPointer)NP_Shutdown);
    
    return NPERR_NO_ERROR;
}

NPError NPP_New(NPMIMEType pluginType, NPP instance, uint16 mode, int16 argc, char* argn[], char* argv[], NPSavedData* saved)
{
    // Scripting functions appeared in NPAPI version 14
    if (browser->version >= 14)
        instance->pdata = browser->createobject (instance, getPluginClass());
	
    return NPERR_NO_ERROR;
}

NPError NPP_Destroy(NPP instance, NPSavedData** save)
{
	PluginObject* obj = instance->pdata;
	@try
	{
		if (obj)
		{
			DimdimCleanup(obj->m_DimdimPluginObject);
			browser->releaseobject((NPObject*)obj);
//			while (((NPObject*)obj)->referenceCount > 0)
//				browser->releaseobject((NPObject*)obj);
		}
	}
	@catch(NSException* exception)
	{
	}
	instance->pdata = 0;
    return NPERR_NO_ERROR;
}

NPError NPP_SetWindow(NPP instance, NPWindow* window)
{
    return NPERR_NO_ERROR;
}


NPError NPP_NewStream(NPP instance, NPMIMEType type, NPStream* stream, NPBool seekable, uint16* stype)
{
    *stype = NP_ASFILEONLY;
    return NPERR_NO_ERROR;
}

NPError NPP_DestroyStream(NPP instance, NPStream* stream, NPReason reason)
{
    return NPERR_NO_ERROR;
}

int32 NPP_WriteReady(NPP instance, NPStream* stream)
{
    return 0;
}

int32 NPP_Write(NPP instance, NPStream* stream, int32 offset, int32 len, void* buffer)
{
    return 0;
}

void NPP_StreamAsFile(NPP instance, NPStream* stream, const char* fname)
{
}

void NPP_Print(NPP instance, NPPrint* platformPrint)
{
	
}

int16 NPP_HandleEvent(NPP instance, void* event)
{
	return 1;
}

void NPP_URLNotify(NPP instance, const char* url, NPReason reason, void* notifyData)
{
	
}

static bool shouldRetainReturnedNPObjects(NPP instance)
{
    // This check is necessary if you want your exposed NPObject to not leak in WebKit-based browsers (including
    // Safari) released prior to Mac OS X 10.5 (Leopard).
    //
    // Earlier versions of WebKit retained the NPObject returned from NPP_GetValue(NPPVpluginScriptableNPObject).
    // However, the NPRuntime API says NPObjects should be retained by the plug-in before they are returned.  WebKit
    // versions later than 420 do not retain returned NPObjects automatically; plug-ins are required to retain them
    // before returning from NPP_GetValue(), as in other browsers.
    static const unsigned webKitVersionNumberWithRetainFix = 420;
    static const char* const webKitVersionPrefix = " AppleWebKit/";
    const char *userAgent = browser->uagent(instance);
    if (userAgent) {
        // Find " AppleWebKit/" in the user agent string
        char *webKitVersionString = strstr(userAgent, webKitVersionPrefix);
        if (!webKitVersionString)
            return true; // Not WebKit
		
        // Skip past " AppleWebKit/"
        webKitVersionString += strlen(webKitVersionPrefix);
        
        // Convert the version string into an integer.  There are some trailing junk characters after the version
        // number, but atoi() is smart enough to handle those.
        int webKitVersion = atoi(webKitVersionString);
        
        // Should not retain returned NPObjects when running in versions of WebKit earlier than 420
        if (webKitVersion && webKitVersion < webKitVersionNumberWithRetainFix)
            return false;
    }
    
    return true;
}

NPError NPP_GetValue(NPP instance, NPPVariable variable, void *value)
{
    if (variable == NPPVpluginScriptableNPObject) {
        void **v = (void **)value;
        PluginObject *obj = instance->pdata;
        
        // Returned objects are expected to be retained in most browsers, but not all.
        // See comments in shouldRetainReturnedNPObjects().
        if (obj && shouldRetainReturnedNPObjects(instance))
		{
            browser->retainobject((NPObject*)obj);
		}
        
        *v = obj;
        return NPERR_NO_ERROR;
    }
    return NPERR_GENERIC_ERROR;
}

NPError NPP_SetValue(NPP instance, NPNVariable variable, void *value)
{
    return NPERR_GENERIC_ERROR;
}

// function pointer converters

static FunctionPointer functionPointerForTVector(TransitionVector tvp)
{
#ifdef __ppc__
    const uint32 temp[6] = {0x3D800000, 0x618C0000, 0x800C0000, 0x804C0004, 0x7C0903A6, 0x4E800420};
    uint32 *newGlue = NULL;
    
    if (tvp != NULL) {
        newGlue = (uint32 *)malloc(sizeof(temp));
        if (newGlue != NULL) {
            unsigned i;
            for (i = 0; i < 6; i++) newGlue[i] = temp[i];
            newGlue[0] |= ((UInt32)tvp >> 16);
            newGlue[1] |= ((UInt32)tvp & 0xFFFF);
            MakeDataExecutable(newGlue, sizeof(temp));
        }
    }
    
    return (FunctionPointer)newGlue;
#else
    // Just use the function pointer on other architectures
    return (FunctionPointer)tvp;
#endif /* __ppc__ */
}

static TransitionVector tVectorForFunctionPointer(FunctionPointer fp)
{
#ifdef __ppc__
    FunctionPointer *newGlue = NULL;
    if (fp != NULL) {
        newGlue = (FunctionPointer *)malloc(2 * sizeof(FunctionPointer));
        if (newGlue != NULL) {
            newGlue[0] = fp;
            newGlue[1] = NULL;
        }
    }
    return (TransitionVector)newGlue;
#else
    // Just use the function pointer on other architectures
    return (TransitionVector)fp;
#endif /* __ppc__ */
}
