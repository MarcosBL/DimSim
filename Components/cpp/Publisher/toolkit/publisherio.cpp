// Header

#include "stdafx.h"
#include "publisherio.h"

#include "xproperty.h"
#include "Translator.h"

#define BUFSIZE 8192
#define PIPE_TIMEOUT 5000
const LPTSTR BROWSER_PUBLISHER_CHANNEL = _T("\\\\.\\pipe\\10df8770-cbeb-11dc-95ff-0800200c9a66");
const UINT identifierLength = 16;

CPublisherIO* CPublisherIO::_instance = NULL;

CPublisherIO::CPublisherIO()
{
	m_property = NULL;
	m_hThread = NULL;
	m_sRegistrationKey.clear();
}

CPublisherIO::~CPublisherIO()
{
	m_sRegistrationKey.clear();
	m_property = NULL;

	executePipeQuery(_T("Shutdown"));

	if (NULL != m_hThread)
		CloseHandle(m_hThread);
}

CPublisherIO* CPublisherIO::getInstance()
{
	if(NULL == _instance)
	{
		_instance = new CPublisherIO();
	}

	return _instance;
}

void CPublisherIO::removeInstance()
{
	if (_instance)
	{
		delete _instance;
	}

	_instance = NULL;
}

void CPublisherIO::setPropertyObject(CXProperty* property)
{
	m_property = property;
}

void CPublisherIO::start()
{
	m_hThread = CreateThread(NULL,0,run,0,0,NULL);
}

BOOL CPublisherIO::validateRegistration(CXProperty* property, bool bAffliction)
{
	if (NULL == property)
		return FALSE;
	if (m_sRegistrationKey.length() > 0)
	{
		if (0 == (property->retrieveRegistration()).compare(m_sRegistrationKey))
			return TRUE;
		return FALSE;
	}

	if (true == bAffliction)
		return FALSE; // A mighty destructor called this.

	if (false == executePipeQuery(_T("ServerUp")))
	{
		m_property = property;
		start();
		m_sRegistrationKey = m_property->retrieveRegistration();
		return TRUE;
	}
	return FALSE;
}

DWORD CPublisherIO::pipeServer()
{
	BOOL fConnected;
	LPTSTR lpszPipename = BROWSER_PUBLISHER_CHANNEL;
	DWORD cbBytesRead;
	BOOL fSuccess;

	m_hPipe = CreateNamedPipe ( lpszPipename,
		PIPE_ACCESS_INBOUND, // read/write access
		PIPE_TYPE_MESSAGE | // message type pipe
		PIPE_READMODE_MESSAGE | // message-read mode
		PIPE_WAIT, // blocking mode
		1, // max. instances
		BUFSIZE, // output buffer size
		BUFSIZE, // input buffer size
		PIPE_TIMEOUT, // client time-out
		NULL); // no security attribute

	if (m_hPipe == INVALID_HANDLE_VALUE)
	{
		return 1;
	}

	for (;;)
	{
		// Wait for the client to connect; if it succeeds,
		// the function returns a nonzero value. If the function returns
		// zero, GetLastError returns ERROR_PIPE_CONNECTED.

		fConnected = ConnectNamedPipe(m_hPipe, NULL) ? TRUE : (GetLastError() == ERROR_PIPE_CONNECTED);
		if (fConnected)
		{
			TCHAR chRequest[BUFSIZE];
			fSuccess = ReadFile (m_hPipe, // handle to pipe
				chRequest, // buffer to receive data
				BUFSIZE, // size of buffer
				&cbBytesRead, // number of bytes read
				NULL); // not overlapped I/O

			chRequest[cbBytesRead] = _T('\0');

			if (! fSuccess || cbBytesRead == 0)
			{
				::MessageBox(0, _T("IO Error"), _T("IO Error"), 0);
				break;
			}


			// Set this to XProperty
			std::wstring message(chRequest);
			
			// Ugly hack, but unavoidable when bits per pixel is changed during a meeting. :((

			if (message.at(message.length() - 1) == message.at(message.length() - 2))
				message.assign(message.substr(0, message.length() - 1));
			if (message.at(0) == message.at(1))
				message.assign(message.substr(1, message.length()));

			if (message.find(_T("{screencast")) == 0)
			{
				BSTR chMessage = ::SysAllocString(message.c_str());
				m_property->setScreencastResult(chMessage);
				::SysFreeString(chMessage);

				if (message.compare(_T("{screencastResult:\"1\"}")) == 0)
					CTranslator::executeScreencasterMenuW(_T("enable"));
				else
					CTranslator::executeScreencasterMenuW(_T("disable"));
			}
			else if (message.find(_T("ServerUp")) == 0)
			{
				// don't do anything
			}
			else if (message.find(_T("Shutdown")) == 0)
			{
				try
				{
					FlushFileBuffers(m_hPipe);
				}
				catch(...)
				{
				}
				try
				{
					DisconnectNamedPipe(m_hPipe);
				}
				catch(...)
				{
				}
				break;
			}
			else if (message.find(_T("Warning")) == 0)
			{
				CTranslator::executeScreencasterMenuW(_T("warning"));
			}
			else if (message.find(_T("driverready")) == 0)
			{
				m_property->setDriverStatus(1);
			}

			try
			{
				FlushFileBuffers(m_hPipe);
			}
			catch(...)
			{
			}
			try
			{
				DisconnectNamedPipe(m_hPipe);
			}
			catch(...)
			{
			}
		}
		else
		{
			// The client could not connect, so close the pipe.
			try
			{
				CloseHandle(m_hPipe);
			}
			catch(...)
			{
			}
		}
	}
	try
	{
		CloseHandle(m_hPipe);
	}
	catch(...)
	{
	}

	return 1;
}

DWORD CPublisherIO::run(LPVOID lParam)
{
	return CPublisherIO::getInstance()->pipeServer();
}

bool CPublisherIO::executePipeQuery(std::wstring msg)
{
	HANDLE hPipe; 
	BOOL fSuccess; 
	DWORD cbWritten, dwMode; 

	// Try to open a named pipe; wait for it, if necessary. 

	while (1) 
	{ 
		hPipe = CreateFile(
			BROWSER_PUBLISHER_CHANNEL,   // pipe name 
			GENERIC_WRITE, 
			0,              // no sharing 
			NULL,           // default security attributes
			OPEN_EXISTING,  // opens existing pipe 
			0,              // default attributes 
			NULL);          // no template file 

		// Break if the pipe handle is valid. 

		if (hPipe != INVALID_HANDLE_VALUE) 
			break; 

		// Exit if an error other than ERROR_PIPE_BUSY occurs. 

		if (GetLastError() != ERROR_PIPE_BUSY) 
		{
			// Error: could not open pipe
			return false;
		}

		if (!WaitNamedPipe(BROWSER_PUBLISHER_CHANNEL, 500)) 
		{ 
			// Error: could not open pipe
			return false;
		} 
	} 

	// The pipe connected; change to message-read mode. 

	dwMode = PIPE_READMODE_MESSAGE; 
	fSuccess = SetNamedPipeHandleState( 
		hPipe,    // pipe handle 
		&dwMode,  // new pipe mode 
		NULL,     // don't set maximum bytes 
		NULL);    // don't set maximum time 
	if (!fSuccess) 
	{
		// Error: SetNamedPipeHandleState failed
		return false;
	}

	// Send a message to the pipe server. 

	fSuccess = WriteFile( 
		hPipe,                  // pipe handle 
		msg.c_str(),             // message 
		(DWORD)(msg.size() * sizeof(wchar_t)), // message length 
		&cbWritten,             // bytes written 
		NULL);                  // not overlapped 
	if (!fSuccess) 
	{
		// Error: WriteFile failed
		return false;
	}
	try
	{
		CloseHandle(hPipe); 
	}
	catch(...)
	{
		}
	return true; 
}