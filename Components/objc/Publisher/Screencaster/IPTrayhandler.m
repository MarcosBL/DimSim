#import "IPTrayhandler.h"
#import "includes.h"

#import "ProfileManager.h"
#import "DimdimMessageClient.h"

#import <unistd.h>

@implementation IPTrayhandler

- (void) dealloc
{
	[m_messageClient deregisterFromServer];
	[m_messageClient release];
	[gMenuBarIcon release];
	[m_autoReleasePool release];
	
	[super dealloc];
}

- (void) awakeFromNib
{
	// awakeFromNib is called only once.
	
	// Initialize the message client
	m_messageClient = [[DimdimMessageClient alloc] init];
	if (NO == [m_messageClient registerWithServer])
	{
		[m_messageClient release];
		exit(1);
	}
	
	// Menu bar icon
	gMenuBarIcon = [[[NSStatusBar systemStatusBar] 
					 statusItemWithLength:NSVariableStatusItemLength]
					retain];
	[gMenuBarIcon setHighlightMode:YES];
	[gMenuBarIcon setEnabled:YES];
	[gMenuBarIcon setTitle:[NSString stringWithString:@""]]; 
	[gMenuBarIcon setTarget:self];
	//[gMenuBarIcon setImage:[NSImage imageNamed:@"red"]];
	[gMenuBarIcon setImage:[NSImage imageNamed:@"MenuBarMacOff.png"]];
	[gMenuBarIcon setToolTip:@"Screencaster version 4.0 build 090808"];
	
	NSMenu* menuList = [[NSMenu alloc] initWithTitle:@"Menu"];
	
	NSMenuItem* aboutEntry = [[NSMenuItem alloc] initWithTitle:@"About" action:@selector(About:) keyEquivalent:@""];
	[aboutEntry setEnabled:YES];
	[aboutEntry setTarget:self];
	[menuList addItem:aboutEntry];
	[aboutEntry release];
	
	NSMenuItem* propEntry = [[NSMenuItem alloc] initWithTitle:@"Properties" action:@selector(Properties:) keyEquivalent:@""];
	[propEntry setEnabled:YES];
	[propEntry setTarget:self];
	[menuList addItem:propEntry];
	[propEntry release];
	
	NSMenuItem* quitEntry = [[NSMenuItem alloc] initWithTitle:@"Quit" action:@selector(Quit:) keyEquivalent:@""];
	[quitEntry setEnabled:YES];
	[quitEntry setTarget:self];
	[menuList addItem:quitEntry];
	[quitEntry release];
	
	[gMenuBarIcon setMenu:menuList];
	[menuList release];
}


- (void) About : (id) sender
{
	NSAlert* aboutAlert = [NSAlert alertWithMessageText:@"About" 
										  defaultButton:@"Ok" 
										alternateButton:nil 
											otherButton:nil 
							  informativeTextWithFormat:@"Screencaster version 4.0 build 090808"];
	
	[aboutAlert setAlertStyle:NSInformationalAlertStyle];
	[aboutAlert runModal];
}

- (void) Properties : (id) sender
{
	
	m_autoReleasePool = [[NSAutoreleasePool alloc] init];
	
	// Initialize UI components
	
	// Profile Prototype
	m_profileRadioButton = [[[NSButtonCell alloc] init] autorelease];
	[m_profileRadioButton setButtonType:NSRadioButton];
	[m_profileRadioButton setTitle:@""];
	
	// Admin Prototype
	m_adminCheckBox = [[[NSButtonCell alloc] init] autorelease];
	[m_adminCheckBox setButtonType:NSSwitchButton];
	[m_adminCheckBox setTitle:@""];
	
	
	// Properties Window
	m_propWnd = [[NSWindow alloc] initWithContentRect:NSMakeRect(0, 0, 285, 170)
											styleMask:NSClosableWindowMask  | NSTitledWindowMask
											  backing:NSBackingStoreBuffered 
												defer:false];
	
	
	// Profile Area
	m_profileArea = [[[NSMatrix alloc] initWithFrame:NSMakeRect(20, 95, 200, 50)
												mode:NSRadioModeMatrix 
										   prototype:m_profileRadioButton
										numberOfRows:3 
									 numberOfColumns:1] autorelease];
	
	// Admin Area
	m_adminArea = [[[NSMatrix alloc] initWithFrame:NSMakeRect(20, 45, 100, 50)
											  mode:NSSwitchButton 
										 prototype:m_adminCheckBox
									  numberOfRows:2
								   numberOfColumns:1] autorelease];
	
	// Apply Button
	
	NSButton* applyButton = [[[NSButton alloc] initWithFrame:NSMakeRect(130, 0, 80, 30)] autorelease];
	[applyButton setTitle:@"Apply"];
	[applyButton setBezelStyle:NSRoundedBezelStyle];
	[applyButton setTarget:self];
	[applyButton setAction:@selector(applyAction:)];
	
	// Cancel Button
	
	NSButton* cancelButton = [[[NSButton alloc] initWithFrame:NSMakeRect(205, 0, 80, 30)] autorelease];
	[cancelButton setTitle:@"Cancel"];
	[cancelButton setBezelStyle:NSRoundedBezelStyle];
	[cancelButton setTarget:self];
	[cancelButton setAction:@selector(cancelAction:)];
	
	// Bandwidth Profiles
	
	[[m_profileArea cellAtRow:0 column:0] setTitle:@"High bandwidth profile"];
	[[m_profileArea cellAtRow:1 column:0] setTitle:@"Medium bandwidth profile"];
	[[m_profileArea cellAtRow:2 column:0] setTitle:@"Low bandwidth profile"];
	[m_profileArea sizeToFit];
	
	// Administration
	
	[[m_adminArea cellAtRow:0 column:0] setTitle:@"Enable Logging"];
	[[m_adminArea cellAtRow:1 column:0] setTitle:@"Create local dump"];
	[m_adminArea sizeToFit];
	
	
	// Organize layout
	
	[[m_propWnd contentView] addSubview:applyButton];
	[[m_propWnd contentView] addSubview:cancelButton];
	[[m_propWnd contentView] addSubview:m_profileArea];
	[[m_propWnd contentView] addSubview:m_adminArea];
	
	// Initialize profile view
	
	if ([ProfileManager RetrieveConfig:@"BlockSize"] == 64)
	{
		[m_profileArea selectCellAtRow:0 column:0];
	}
	else if ([ProfileManager RetrieveConfig:@"BlockSize"] == 16)
	{
		[m_profileArea selectCellAtRow:2 column:0];
	}
	else
	{
		[m_profileArea selectCellAtRow:1 column:0];
	}
	
	// Initialize admin view
	
	if ([ProfileManager RetrieveConfig:@"Logging"] < 2)
	{
		[[m_adminArea cellAtRow:0 column:0] setState:0];
	}
	else
	{
		[[m_adminArea cellAtRow:0 column:0] setState:1];
	}
	
	if ([ProfileManager RetrieveConfig:@"OperationType"] == 1 || 
		[ProfileManager RetrieveConfig:@"OperationType"] == 3)
	{
		[[m_adminArea cellAtRow:1 column:0] setState:1];
	}
	else
	{
		[[m_adminArea cellAtRow:1 column:0] setState:0];
	}
	
	
	[m_propWnd orderFront:sender];
	[m_propWnd setLevel:NSModalPanelWindowLevel];
	[m_propWnd center];
	
	[m_autoReleasePool release];
}

- (void) Quit : (id) sender
{
	// Stop screencaster
	system("killall OSXvnc-server");
	
	// Get rid of the message client and the tray icon
	[m_messageClient deregisterFromServer];
	[m_messageClient release];
	[gMenuBarIcon release];
	
	exit(1);
}

- (SEL) cancelAction : (void*) sender
{
	[m_propWnd performClose:NULL];
	return NULL;
}

-(SEL) applyAction : (void*) sender
{
	if ([m_profileArea selectedRow] == 0)
	{
		[ProfileManager enforceHighBWProfile];
	}
	else if ([m_profileArea selectedRow] == 2)
	{
		[ProfileManager enforceLowBWProfile];
	}	
	else
	{
		[ProfileManager enforceMediumBWProfile];
	}
	
	if ([[m_adminArea cellAtRow:0 column:0] intValue] == 1)
	{
		[ProfileManager UpdateConfig:@"Logging" withValue:2];
	}
	else
	{
		[ProfileManager UpdateConfig:@"Logging" withValue:0];
	}
	
	if ([[m_adminArea cellAtRow:1 column:0] intValue] == 1)
	{
		[ProfileManager UpdateConfig:@"OperationType" withValue:3];
	}
	else
	{
		[ProfileManager UpdateConfig:@"OperationType" withValue:2];
	}
	
	[m_propWnd performClose:NULL];
	
	return NULL;
}

@end
