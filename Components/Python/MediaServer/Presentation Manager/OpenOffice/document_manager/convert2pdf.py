# -*- coding: ISO-8859-1 -*-
# Convert2PDF.py script
# Part of PDFCreator
# License: GPL
# Homepage: http://www.pdfforge.org/products/pdfcreator
# python 2.5, pywin32 2.5.1
# Version: 1.0.0.0
# Date: December, 24. 2007
# Author: Frank Heindörfer
# Comments: This script convert a printable file in a pdf-file using 
#           the com interface of PDFCreator.

##############################################################
# Minor modifications made for compatibility with
# Dimdim Media Server

# The script will now support only one conversion at time
# Both input and output files will be expected as
# parameters.

##############################################################

import win32com.client as com
from win32print import SetDefaultPrinter
import os
import sys

from time import sleep
from pythoncom import PumpWaitingMessages, PumpMessages

# Dimdim Media Server is going to be dealing with
# some very heavy presentations. This may take up to
# to minutes for all the messages to be pumped
# and the PDF to be created properly

sleepTime = 1  # in seconds
maxTime = 120   # in seconds
ReadyState = 0

class clsPDFCreatorEvents(object):
	def OneReady(self):
		global ReadyState
		ReadyState = 1
	def OneError(self):
		ReadyState = 0
		global PDFCreator
		PDFCreator.cErrorDetail("Description")
		errNum = PDFCreator.cErrorDetail("Number")
		PDFCreator = None
		os._exit(errNum)

if len(sys.argv) <= 2:
	os._exit(-255)

# Initialize the driver

PDFCreator = com.Dispatch('PDFCreator.clsPDFCreator')
PDFCreatorEvents = com.DispatchWithEvents(PDFCreator, clsPDFCreatorEvents)
ProgramIsRunning = PDFCreator.cProgramIsRunning
PDFCreator.cStart("/NoProcessingAtStartup", 1)

options = PDFCreator.cOptions
oldOptions = PDFCreator.cOptions
options.UseAutosave = 1
options.UseAutosaveDirectory = 1
options.AutosaveFormat = 0                            # 0 = PDF
SetDefaultPrinter('PDFCreator')
PDFCreator.cClearCache
PDFCreator.cPrinterStop = 0

ifname = str(sys.argv[1])
ofname = str(sys.argv[2])

# The caller needs to make sure
# the file exists. However, the PDFCreator
# is going to determine if the file is actually
# printable

if not PDFCreator.cIsPrintable(ifname):
    exit()

ReadyState = 0

# setup output directory and filename

options.AutosaveDirectory = os.path.dirname(ofname)
options.AutosaveFilename = os.path.splitext(os.path.basename(ofname))[0]
PDFCreator.cSaveOptions(options)
PDFCreator.cPrintFile(ifname)

c = 0
while (ReadyState == 0) and (c < (maxTime / sleepTime)):
	c = c + 1
	sleep(sleepTime)
	PumpWaitingMessages()
#if ReadyState == 0:
#	exit()

PDFCreator.cOptions = options
PDFCreator.cSaveOptions(oldOptions)
PDFCreator.cClose
PDFCreator = None