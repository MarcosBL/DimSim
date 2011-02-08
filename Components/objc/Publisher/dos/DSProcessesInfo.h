/*
 Prototypes for process info functions
 
 Copyright (c) 2003 - 2006 Night Productions, by Darkshadow.  All Rights Reserved.
 http://www.nightproductions.net/developer.htm
 darkshadow@nightproductions.net
 
 May be used freely, but keep my name/copyright in the header.
 
 There is NO warranty of any kind, express or implied; use at your own risk.
 Responsibility for damages (if any) to anyone resulting from the use of this
 code rests entirely with the user.
 
 --> getProcessInfoByPID(int pidNum) - returns a dictionary of info on the process.  Contains:
 Key:			    Value:
 DSProcessName		    NSString of the process's name
 DSProcessPIDNumber	    NSNumber (int) of the process's ID
 DSProcessStartTime	    NSDate of when the process started
 DSProcessFlags		    NSArray of strings, each a different flag.
 DSProcessFlagValue	    NSNumber (unsigned long) of the flag value.
 DSProcessStatus		    NSString of the process's status
 DSProcessStatusValue	    NSNumber (unsigned long) of the status value.
 DSProcessSystemPriority     NSNumber (int) of the process's system priority
 DSProcessNiceValue	    NSNumber (int) of the process's nice value.
 DSProcessParentPID	    NSNumber (int) of the process's parent PID
 DSProcessOwner		    NSString of the process's owner
 DSProcessArguments	    NSString of the process's arguments
 DSProcessEnvironment	    NSArray of strings, each a different environment setting
 
 *Note: You must have sufficient privs to read the process args & env.  Really, unless
 you are root, you only have sufficient privs to read the args & env of procs
 owned by whoever is running the code.
 
 --> getProcessInfoByName(NSString *name) - returns an array of dictionaries, each a different
 process.  See above for the contents of the dictionary.
 *Note: works something like grep - it'll match any process name that contains *name*.
 i.e. 'in' will match init, mach_init, and any other process that contains 'in'.
 --> allProcessesInfo(void) - returns an array of dictionaries, each a different process.
 See above for the contents of the dictionary.
 --> allProcesses(void) - returns an array of strings, each a different process name.
 --> isProcessRunningByPID(int pidNum) - returns YES or NO, depending if the given PID is
 running.
 --> isProcessRunningByName(NSString *name) - returns YES or NO, depending if the given
 process is running.
 *Note: matches *exactly* - it'll match only a process name that IS *name*.
 returns YES on first found match.
 
 -------------------------------------------------------
 
 * Sometime around the end of 2003 / beginning of 2004 - initial release
 * December 15, 2004 - Fixed a bug that had isProcessRunningByPid() always returning TRUE.
 * April 02, 2006 - Fixed a few memory leaks, changed the implementation of some of the
 internal code to be quicker, cleaned up the code a bit, fixed a bug where
 I was returning a _released_ object - I'm sad to admit that, but even more sad still that
 it actually worked prior to Tiger....
 */

#ifndef DSProcessesInfo_H
#define DSProcessesInfo_H

#import <Foundation/Foundation.h>

extern NSString *DSProcessName;
extern NSString *DSProcessPIDNumber;
extern NSString *DSProcessStartTime;
extern NSString *DSProcessFlags;
extern NSString *DSProcessFlagValue;
extern NSString *DSProcessStatus;
extern NSString *DSProcessStatusValue;
extern NSString *DSProcessSystemPriority;
extern NSString *DSProcessNiceValue;
extern NSString *DSProcessParentPID;
extern NSString *DSProcessOwner;
extern NSString *DSProcessArguments;
extern NSString *DSProcessEnvironment;


NSDictionary *  getProcessInfoByPID __P((int));

NSArray *       getProcessInfoByName __P((NSString *)),
*	allProcessesInfo __P((void)),
*	allProcesses __P((void));

BOOL		isProcessRunningByPID __P((int)),
isProcessRunningByName __P((NSString *));

#endif
