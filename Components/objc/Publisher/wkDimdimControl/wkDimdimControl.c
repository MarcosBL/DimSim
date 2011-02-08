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

#import <Foundation/Foundation.h>

#import "wkDimdimControl.h"
#import "includes.h"
#import "Translator.h"

static void DimdimPluginInvalidate(NPObject *obj);
static bool DimdimPluginHasProperty(NPObject *obj, NPIdentifier name);
static bool DimdimPluginHasMethod(NPObject *obj, NPIdentifier name);
static bool DimdimPluginGetProperty(NPObject *obj, NPIdentifier name, NPVariant *variant);
static bool DimdimPluginSetProperty(NPObject *obj, NPIdentifier name, const NPVariant *variant);
static bool DimdimPluginInvoke(NPObject *obj, NPIdentifier name, const NPVariant *args, uint32_t argCount, NPVariant *result);
static bool DimdimPluginInvokeDefault(NPObject *obj, const NPVariant *args, uint32_t argCount, NPVariant *result);
static NPObject *DimdimPluginAllocate(NPP npp, NPClass *theClass);
static void DimdimPluginDeallocate(NPObject *obj);

static NPClass DimdimPluginClass = { 
    NP_CLASS_STRUCT_VERSION,
    DimdimPluginAllocate, 
    DimdimPluginDeallocate, 
    DimdimPluginInvalidate,
    DimdimPluginHasMethod,
    DimdimPluginInvoke,
    DimdimPluginInvokeDefault,
    DimdimPluginHasProperty,
    DimdimPluginGetProperty,
    DimdimPluginSetProperty,
};

NPClass *getDimdimPluginClass(void)
{
    return &DimdimPluginClass;
}

static bool identifiersInitialized = false;

// We really don't need any properties.
// However, leaving 1 property till we figure out
// how to remove properties completely.

#define ID_VERSION_PROPERTY					0
#define NUM_PROPERTY_IDENTIFIERS			1

static NPIdentifier DimdimPluginPropertyIdentifiers[NUM_PROPERTY_IDENTIFIERS];
static const NPUTF8 *DimdimPluginPropertyIdentifierNames[NUM_PROPERTY_IDENTIFIERS] = {
    "version"
};

#define ID_GET_VERSION_METHOD				0
#define ID_PERFORM_ACTION_METHOD			1
#define ID_GET_PROPERTY_METHOD				2
#define ID_SET_PROPERTY_METHOD				3
#define NUM_METHOD_IDENTIFIERS				4

static NPIdentifier DimdimPluginMethodIdentfiers[NUM_METHOD_IDENTIFIERS];
static const NPUTF8 *DimdimPluginMethodIdentfierNames[NUM_METHOD_IDENTIFIERS] = {
    "getVersion",
    "performAction",
	"getProperty",
	"setProperty"
};

static void initializeIdentifiers(void)
{
    browser->getstringidentifiers(DimdimPluginPropertyIdentifierNames, NUM_PROPERTY_IDENTIFIERS, DimdimPluginPropertyIdentifiers);
    browser->getstringidentifiers(DimdimPluginMethodIdentfierNames, NUM_METHOD_IDENTIFIERS, DimdimPluginMethodIdentfiers);
}

bool DimdimPluginHasProperty(NPObject *obj, NPIdentifier name)
{
    int i;
    for (i = 0; i < NUM_PROPERTY_IDENTIFIERS; i++)
        if (name == DimdimPluginPropertyIdentifiers[i])
            return true;
    return false;
}

bool DimdimPluginHasMethod(NPObject *obj, NPIdentifier name)
{
    int i;
    for (i = 0; i < NUM_METHOD_IDENTIFIERS; i++)
        if (name == DimdimPluginMethodIdentfiers[i])
            return true;
    return false;
}

bool DimdimPluginGetProperty(NPObject *obj, NPIdentifier name, NPVariant *variant)
{
	if (name == DimdimPluginPropertyIdentifiers[ID_VERSION_PROPERTY])
	{
		// Do nothing except returning true for now
		return true;
	}
    return false;
}

bool DimdimPluginSetProperty(NPObject *obj, NPIdentifier name, const NPVariant *variant)
{
	
	if (name == DimdimPluginPropertyIdentifiers[ID_VERSION_PROPERTY])
	{
		// Do nothing except returning true for now
		return true;
	}

	return false;
}

bool DimdimPluginInvoke(NPObject *obj, NPIdentifier name, const NPVariant *args, uint32_t argCount, NPVariant *result)
{
	DimdimPluginObject* instance = (DimdimPluginObject*) obj;
	if (name == DimdimPluginMethodIdentfiers[ID_GET_PROPERTY_METHOD])
	{
		// Three property values may be queried -
		//
		// 1. windowList - Return JSON buffer of HANDLE-Window Caption pairs :: NOT SUPPORTED FOR OSX CURRENTLY. WILL BE ADDED WHEN WE HAVE APPLICATION SHARING SUPPORT
		// 2. screencastResult
		// 3. BWProfile
		
		NPString npArgs = NPVARIANT_TO_STRING(args[0]);
		NSString* nsArgs = [[NSString alloc] initWithCString:npArgs.UTF8Characters length:npArgs.UTF8Length];
		
		NSMutableDictionary* argMap = [[NSMutableDictionary alloc] init];
		[argMap removeAllObjects];
		
		[Translator browserArgsToMap:nsArgs argMap:argMap];
		
		[nsArgs release];
		
		NSString* query = [argMap objectForKey:@"name"];
		
		[argMap release];

		if (query == nil)
		{
			STRINGZ_TO_NPVARIANT("", *result);
			return true;
		}
		
		if ([query compare:@"screencastResult"] == NSOrderedSame)
		{
			NSString* screencastResult = [[NSString alloc] initWithCString:[[instance->pluginProperty getScreencastResult:true] UTF8String]];
			if ([screencastResult compare:@"{screencastResult:\"1\"}"] == NSOrderedSame)
			{
				if (NO == [instance->messageClient isComponentAlive:@"OSXvnc-server"])
				{
					[instance->pluginProperty setScreencastResult:@"{screencastResult:\"5\"}"];
					STRINGZ_TO_NPVARIANT("{screencastResult:\"19\"}", *result);
					[screencastResult release];
					[Translator executePublisherKill];
					return true; 
				}				
			}
			
			STRINGZ_TO_NPVARIANT([screencastResult UTF8String], *result);
			[screencastResult release];
			return true;
		}
		
		if ([query compare:@"BWProfile"] == NSOrderedSame)
		{
			NSString* profile = [Translator getScreencastProfile];
			STRINGZ_TO_NPVARIANT([profile UTF8String], *result);
			[profile release];
			return true;
		}
		
		STRINGZ_TO_NPVARIANT("", *result);
		return true;
	}
	
	if (name == DimdimPluginMethodIdentfiers[ID_SET_PROPERTY_METHOD])
	{
		// 2 properties can be set
		// {screenURL:<value>,screenProfile:<value>}
		
		NPString npArgs = NPVARIANT_TO_STRING(args[0]);
		NSString* nsArgs = [[NSString alloc] initWithCString:npArgs.UTF8Characters length:npArgs.UTF8Length];
		
		NSMutableDictionary* argMap = [[NSMutableDictionary alloc] init];
		[argMap removeAllObjects];
		[Translator jsonArgsToMap:nsArgs argMap:argMap];
		[nsArgs release];
		
		NSEnumerator *enumerator = [argMap keyEnumerator];
		id key;
		
		while ((key = [enumerator nextObject])) 
		{
			if ([key compare:@"screenURL"] == NSOrderedSame)
			{
				[instance->pluginProperty setScreencastURL:[argMap objectForKey:key]];
				return true;
			}
			else if ([key compare:@"dscURL"] == NSOrderedSame)
			{
				// not supported
			}
			else if ([key compare:@"dmsURL"] == NSOrderedSame)
			{
				// not supported
			}
			else
			{
				// setScreencastProfile
				[Translator setScreencastProfile:[[argMap objectForKey:key] intValue]];
			}
		}
		
		[argMap release];
		
		INT32_TO_NPVARIANT(1, *result);
		
		return true;
	}
	
	if (name == DimdimPluginMethodIdentfiers[ID_GET_VERSION_METHOD])
	{
		DOUBLE_TO_NPVARIANT([instance->pluginProperty getVersion], *result);
		return true;
	}
	
	if (name == DimdimPluginMethodIdentfiers[ID_PERFORM_ACTION_METHOD])
	{
		NPString npArgs = NPVARIANT_TO_STRING(args[0]);
		NSString* nsArgs = [[NSString alloc] initWithCString:npArgs.UTF8Characters length:npArgs.UTF8Length];
		NSMutableDictionary* argMap = [[NSMutableDictionary alloc] init];
		[argMap removeAllObjects];
		
		[Translator browserArgsToMap:nsArgs argMap:argMap];
		
		[nsArgs release];
		
		NSString* operation = [argMap valueForKey:@"operation"];
		
		if (operation != nil)
		{
			if ([operation compare:@"screencast"] == NSOrderedSame)
			{
				NSString* reg = [argMap objectForKey:@"reg"];
				if (reg != nil)
				{
					[instance->pluginProperty setRegistration:reg];
					int retval = [instance->messageClient isRegistrationAllowed:[instance->pluginProperty retrieveRegistration]];
					
					if (0 == retval)
					{
						INT32_TO_NPVARIANT(-1, *result);
						return true;
					}
					else if (-1 == retval)
					{
						INT32_TO_NPVARIANT(-2, *result);
						return true;
					}
					
					if (1 == retval)
					{
						// register
						[instance->messageClient registerWithServer:reg];
					}
				}
				else
				{
					INT32_TO_NPVARIANT(-1, *result);
					return true;
				}
				
				
				NSString* action = [argMap objectForKey:@"action"];
				if (action != nil)
				{
					if ([action compare:@"share"] == NSOrderedSame)
					{
						// Start up the tray icon
						[Translator executePublisherRun];
						
						// reset screencastResult
						
						// execute screencaster with handle as 0 for now
						// this will change when we support application sharing
						
						if ([[instance->pluginProperty getScreencastResult:false] compare:@"{screencastResult:\"1\"}"] == NSOrderedSame)
						{
							// This will come in to picture when we have seamless sharing between app and desktop share
							// Send new handle to screencaster here for application sharing
						}
						else
						{
							[instance->pluginProperty resetScreencastResult];
							// execute screencaster
							[Translator executeScreencasterShareAndConnect:@"0" connectURL:[instance->pluginProperty getScreencastURL]];
						}
						
						INT32_TO_NPVARIANT(1, *result);
						return true;
					}
					else
					{
						// execute screencaster stop here
						[Translator executeScreencasterStop];
						
						INT32_TO_NPVARIANT(1, *result);
						return true;
					}
				}
				else
				{
					INT32_TO_NPVARIANT(1, *result);
					return true;
				}
			}
			else if ([[argMap objectForKey:operation] compare:@"mint"] == NSOrderedSame)
			{
				NSString* action = [argMap objectForKey:@"action"];
				if (action != nil)
				{
					if ([action compare:@"run"] == NSOrderedSame)
					{
						// execute publisher here
						[Translator executePublisherRun];
					}
					else
					{
						// kill publisher here
						[Translator executePublisherKill];
					}
				}
				else
				{
					INT32_TO_NPVARIANT(1, *result);
					return true;
				}
			}
		}
	
		[argMap release];
		return true;
	}
	
    return true;
}

bool DimdimPluginInvokeDefault(NPObject *obj, const NPVariant *args, uint32_t argCount, NPVariant *result)
{
	return true;
}

void DimdimPluginInvalidate(NPObject *obj)
{
    // Release any remaining references to JavaScript objects.
}

NPObject* DimdimPluginAllocate(NPP npp, NPClass *theClass)
{
	DimdimPluginObject* newInstance = malloc(sizeof(DimdimPluginObject));
    
    if (!identifiersInitialized) {
        identifiersInitialized = true;
        initializeIdentifiers();
    }
	
	newInstance->pluginProperty = [[xproperty alloc] init];
	newInstance->messageClient = [[DimdimMessageClient alloc] init];
	[newInstance->messageClient setProperty:newInstance->pluginProperty];
	
    return &newInstance->header;
}

void DimdimPluginDeallocate(NPObject *obj)
{
    free(obj);
}

void DimdimCleanup(NPObject* obj)
{
	DimdimPluginObject* instance = (DimdimPluginObject*)obj;
	
	int retval = [instance->messageClient isRegistrationAllowed:[instance->pluginProperty retrieveRegistration]];
	if (retval == 1 || retval == 2)
	{
		[Translator executePublisherKill];
		[Translator executeScreencasterStop];
		[instance->messageClient deregisterFromServer];
		
		// Kill the message server
		[Translator executeMessageServerKill];
	}
	
	[instance->pluginProperty release];
}