// Header

#include "stdafx.h"
#include "resource.h"
#include "commctrl.h"
#include "singleinst.h"
#include "ProxyMgmt.h"
#include <fstream>

// Implementation

CProxyMgmt::CProxyMgmt()
{
	m_bIsProxyWndVisible = false;
}

CProxyMgmt::~CProxyMgmt()
{
}

void CProxyMgmt::ShowProxyDlg(HWND hParent)
{
	int result = 0;
	if (true == m_bIsProxyWndVisible)
		::SetForegroundWindow(m_hProxyWnd);
	else
		DialogBoxParam(hAppInstance, MAKEINTRESOURCE(IDD_PUBPROXY_DLG), hParent, (DLGPROC) ProxyDlgProc, (LONG)this);
}

BOOL CALLBACK CProxyMgmt::ProxyDlgProc(HWND hwnd, UINT uMsg, WPARAM wParam, LPARAM lParam)
{
	CProxyMgmt *_this = (CProxyMgmt *) GetWindowLong(hwnd, GWL_USERDATA);

	switch (uMsg)
	{

	case WM_INITDIALOG:
		{
			SetWindowLong(hwnd, GWL_USERDATA, lParam);
			CProxyMgmt* _this = (CProxyMgmt*) lParam;
			_this->m_bIsProxyWndVisible = true;
			_this->m_hProxyWnd = hwnd;

			InitCommonControls();
			::SetForegroundWindow(hwnd);
			return TRUE;
		}
	
	case WM_COMMAND:
		switch (LOWORD(wParam))
		{
		case IDCANCEL:
			EndDialog(hwnd, IDCANCEL);
			_this->m_bIsProxyWndVisible = false;
			return TRUE;
		case IDOK:
			{
				char proxyString[1024];
				::GetWindowTextA(GetDlgItem(hwnd, IDC_EDIT_PPROXY), proxyString, 1024);
				std::string proxyLocation(proxyString);

				// Do some very simple filtering of input

				if (proxyLocation.find("://") != proxyLocation.npos || 
					proxyLocation.find("\\") != proxyLocation.npos)
				{
					std::string sFileName(getenv("APPDATA"));
					sFileName.append("\\Dimdim\\proxyloc");
					std::ofstream fos(sFileName.c_str());

					if (proxyLocation.find("://") == proxyLocation.npos)
					{
						// Make a copy of the file and put location as LOCALFILE

						std::string sProxyFile(sFileName);
						sProxyFile.append(".dat");

						CopyFileA(proxyLocation.c_str(), sProxyFile.c_str(), FALSE);

						fos<<"LOCALFILE";
					}
					else
					{
						fos << proxyLocation.c_str();
					}

					fos.close();
				}

				EndDialog(hwnd, IDOK);
				_this->m_bIsProxyWndVisible = false;
			}
			return TRUE;
		}
		return 0;
	}
	return 0;
}