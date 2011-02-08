#import <Cocoa/Cocoa.h>

@class DimdimMessageClient;
@interface IPTrayhandler : NSObject /* Specify a superclass (eg: NSObject or NSView) */
{
	DimdimMessageClient* m_messageClient;
	NSWindow* m_propWnd;
	
	NSMatrix* m_profileArea;
	NSMatrix* m_adminArea;
	
	NSButtonCell* m_profileRadioButton;
	NSButtonCell* m_adminCheckBox;
	
	NSAutoreleasePool* m_autoReleasePool;
}

- (SEL) cancelAction : (void*) sender;
-(SEL) applyAction : (void*) sender;

@end
