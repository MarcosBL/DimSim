// Header

#include "stdafx.h"
#include "singleinst.h"

#include "PublisherProperties.h"
#include "PublisherTray.h"
#include "ProxyMgmt.h"

#include "Publisher.h"
#include "resource.h"

#include "toolkit/ProfileManager.h"

// Balloon tooltips need IE version to be >= 0x0500.
// Tooltip code will now work only on Windows 2000 and above

// Constants

const TCHAR*	DIMDIM_PUBLISHER_TRAY_CLASS_NAME	= _T("Dimdim Publisher Tray Icon");

// Implementation

CPublisherTray::CPublisherTray()
{
	// Create a dummy window to handle tray icon messages
	WNDCLASSEX wndclass;

	wndclass.cbSize			= sizeof(wndclass);
	wndclass.style			= 0;
	wndclass.lpfnWndProc	= CPublisherTray::WndProc;
	wndclass.cbClsExtra		= 0;
	wndclass.cbWndExtra		= 0;
	wndclass.hInstance		= hAppInstance;
	wndclass.hIcon			= LoadIcon(NULL, IDI_APPLICATION);
	wndclass.hCursor		= LoadCursor(NULL, IDC_ARROW);
	wndclass.hbrBackground	= (HBRUSH) GetStockObject(WHITE_BRUSH);
	wndclass.lpszMenuName	= (const TCHAR *) NULL;
	wndclass.lpszClassName	= DIMDIM_PUBLISHER_TRAY_CLASS_NAME;
	wndclass.hIconSm		= LoadIcon(NULL, IDI_APPLICATION);

	RegisterClassEx(&wndclass);

	m_hwnd = CreateWindow(DIMDIM_PUBLISHER_TRAY_CLASS_NAME,
				DIMDIM_PUBLISHER_TRAY_CLASS_NAME,
				WS_OVERLAPPEDWINDOW,
				CW_USEDEFAULT,
				CW_USEDEFAULT,
				200, 200,
				NULL,
				NULL,
				hAppInstance,
				NULL);
	if (m_hwnd == NULL)
	{
		PostQuitMessage(0);
		return;
	}

	SetWindowLong(m_hwnd, GWL_USERDATA, (LONG) this);

	// Load the icons for the tray
	m_iNormal = LoadIcon(hAppInstance, MAKEINTRESOURCE(IDI_DIMDIM_PUB_TRAY));
	m_iProgress = LoadIcon(hAppInstance, MAKEINTRESOURCE(IDI_DIMDIM_PUB_TRAY_PROGRESS));

	// Load the popup menu
	m_hmenu = LoadMenu(hAppInstance, MAKEINTRESOURCE(IDR_PUB_TRAY_MENU));

	SetToolTip();

	// Install the tray icon!
	AddToTray();

	m_pubProps = NULL;
	m_proxyMgmt = NULL;
}

CPublisherTray::~CPublisherTray()
{
	cleanup();
}

void CPublisherTray::cleanup()
{
	// Remove the tray icon
	RemoveFromTray();
	
	// Destroy the loaded menu
	if (m_hmenu != NULL)
		DestroyMenu(m_hmenu);
}

void CPublisherTray::AddToTray()
{
	SendTrayMsg(NIM_ADD);
}

void CPublisherTray::RemoveFromTray()
{
	SendTrayMsg(NIM_DELETE);
}

void CPublisherTray::SetToolTip(std::wstring sTip)
{
	if (sTip.length() > 0)
		wsprintf(m_nid.szTip, _T("%s"), sTip.c_str());
	else
		wsprintf(m_nid.szTip, _T("Screencaster 4.0 build 112608"));
	SendTrayMsg(NIM_MODIFY);
}

void CPublisherTray::SendTrayMsg(DWORD msg)
{
	// Create the tray icon message
	m_nid.hWnd = m_hwnd;
	m_nid.cbSize = sizeof(m_nid);
	m_nid.uID = IDI_DIMDIM_PUB_TRAY;			// never changes after construction
	m_nid.hIcon = m_iNormal;
	m_nid.uFlags = NIF_ICON | NIF_MESSAGE | NIF_TIP;
	m_nid.uCallbackMessage = WM_TRAYNOTIFY;
	
	
	// Send the message
	if (Shell_NotifyIcon(msg, &m_nid))
	{
		EnableMenuItem(m_hmenu, ID_PUB_ABOUT, MF_ENABLED);
		EnableMenuItem(m_hmenu, ID_IMPORTPACFILE, MF_ENABLED);
//		if (1 == CPublisher::getInstance()->isDriverInstalled())
//			EnableMenuItem(m_hmenu, ID_PUB_INSTALL, MF_GRAYED);
//		else
		EnableMenuItem(m_hmenu, ID_PUB_INSTALL, MF_ENABLED);
		EnableMenuItem(m_hmenu, ID_PROPERTIES, MF_ENABLED);
		EnableMenuItem(m_hmenu, ID_PUB_EXIT, MF_ENABLED);
	}
}

void CPublisherTray::SetNIDProgress(bool bVal)
{
	if (true == bVal)
	{
		m_nid.hIcon = m_iProgress;
	}
	else
	{
		m_nid.hIcon = m_iNormal;
	}

	Shell_NotifyIcon(NIM_MODIFY, &m_nid);
}

void CPublisherTray::ShowBalloon(std::wstring sMessage, std::wstring type)
{
	m_nid.hIcon = m_iProgress;
	
	if (type.compare(_T("info")) == 0)
		m_nid.dwInfoFlags = NIIF_INFO;
	else if (type.compare(_T("warning")) == 0)
		m_nid.dwInfoFlags = NIIF_WARNING;
	else
		m_nid.dwInfoFlags = NIIF_ERROR;

	m_nid.uFlags |= NIF_INFO;
	m_nid.uTimeout = 2000;
	wsprintf(m_nid.szInfoTitle, _T("Screencaster"));
	wsprintf(m_nid.szInfo, sMessage.c_str());
	::Shell_NotifyIcon(NIM_MODIFY, &m_nid);
}

// Process window messages
LRESULT CALLBACK CPublisherTray::WndProc(HWND hwnd, UINT iMsg, WPARAM wParam, LPARAM lParam)
{
	CPublisherTray* _this = (CPublisherTray*) GetWindowLong(hwnd, GWL_USERDATA);
	
	switch (iMsg)
	{
		case WM_DIMDIM_SCREENCASER_FORCELOW:
		{
			_this->ShowBalloon(_T("Unable to activate Driver. Forcing 8-bit screenshare"), _T("warning"));
		}
		break;
	case WM_DIMDIM_SCREENCASTER_PROGRESS:
		{
			_this->SetToolTip(_T("Screencasting is currently in progress"));
			_this->SetNIDProgress(true);
			EnableMenuItem(_this->m_hmenu, ID_PROPERTIES, MF_GRAYED);
			EnableMenuItem(_this->m_hmenu, ID_PUB_INSTALL, MF_GRAYED);
		}
		break;

	case WM_DIMDIM_CONVERTER_PROGRESS:
		{
			_this->SetToolTip(_T("Conversion operation is currently in progress"));
			_this->SetNIDProgress(true);
			EnableMenuItem(_this->m_hmenu, ID_PROPERTIES, MF_GRAYED);
			EnableMenuItem(_this->m_hmenu, ID_PUB_INSTALL, MF_GRAYED);
		}
		break;

	case WM_DIMDIM_IDLE:
		{
			_this->SetToolTip();
			_this->SetNIDProgress();
			EnableMenuItem(_this->m_hmenu, ID_PROPERTIES, MF_ENABLED);
//			if (1 == CPublisher::getInstance()->isDriverInstalled())
//				EnableMenuItem(_this->m_hmenu, ID_PUB_INSTALL, MF_GRAYED);
//			else
				EnableMenuItem(_this->m_hmenu, ID_PUB_INSTALL, MF_ENABLED);
		}
		break;

	case WM_CREATE:
		return 0;

	case WM_COMMAND:
		// User has clicked an item on the tray menu
		switch (LOWORD(wParam))
		{
		case ID_PUB_EXIT:
			{
				CPublisher::getInstance()->disconnectComponents();
				CProfileManager::removeInstance();
				_this->cleanup();
				PostQuitMessage(0);
				return 0;
			}
		case ID_PUB_INSTALL:
			{
				if (2 == CPublisher::getInstance()->isDriverInstalled())
				{
					::MessageBox(0, _T("A previous mirror driver install/uninstall action requires a system restart."), _T("Warning"), MB_ICONWARNING);
					return 0;
				}
				if (0 == CPublisher::getInstance()->isDriverInstalled())
				{
					CPublisher::getInstance()->invokeDriverInstall();
				}
				return 0;
			}
		case ID_PROPERTIES:
			{
				if (NULL == _this->m_pubProps)
					_this->m_pubProps = new CPublisherProperties();
				_this->m_pubProps->ShowPropDialog(_this->m_hwnd);
			}
			break;
		case ID_IMPORTPACFILE:
			{
				if (NULL == _this->m_proxyMgmt)
					_this->m_proxyMgmt = new CProxyMgmt();
				_this->m_proxyMgmt->ShowProxyDlg(_this->m_hwnd);
			}
			break;
		case ID_PUB_ABOUT:
			{
				// Show the About box
				::MessageBoxA(0, "Screencaster 4.0 build 112608", "About", 0);
			}
			break;
		}
		return 0;

	case WM_TRAYNOTIFY:
		{
			// Get the submenu to use as a pop-up menu
			HMENU submenu = GetSubMenu(_this->m_hmenu, 0);


			// What event are we responding to, RMB click?
			if (lParam==WM_RBUTTONUP)
			{
				if (submenu == NULL)
				{ 
					return 0;
				}

				// Make the first menu item the default (bold font)
				SetMenuDefaultItem(submenu, 0, TRUE);
				
				// Get the current cursor position, to display the menu at
				POINT mouse;
				GetCursorPos(&mouse);

				// There's a "bug"
				// (Microsoft calls it a feature) in Windows 95 that requires calling
				// SetForegroundWindow. To find out more, search for Q135788 in MSDN.
				//
				SetForegroundWindow(_this->m_nid.hWnd);

//				if (1 == CPublisher::getInstance()->isDriverInstalled())
//					EnableMenuItem(submenu, ID_PUB_INSTALL, MF_GRAYED);
//				else
				EnableMenuItem(submenu, ID_PUB_INSTALL, MF_ENABLED);

				// Display the menu at the desired position
				TrackPopupMenu(submenu,
						0, mouse.x, mouse.y, 0,
						_this->m_nid.hWnd, NULL);

				return 0;
			}
			
			// Or was there a LMB double click?
			if (lParam==WM_LBUTTONDBLCLK)
			{
				// double click: execute first menu item
				SendMessage(_this->m_nid.hWnd,
							WM_COMMAND, 
							GetMenuItemID(submenu, 0),
							0);
			}

			return 0;
		}

	case WM_CLOSE:
		CPublisher::getInstance()->disconnectComponents();
		_this->cleanup();
		PostQuitMessage(0);
		return 0;
		break;

	case WM_DESTROY:
		PostQuitMessage(0);
		return 0;
	}
	// Message not recognised
	return DefWindowProc(hwnd, iMsg, wParam, lParam);
}