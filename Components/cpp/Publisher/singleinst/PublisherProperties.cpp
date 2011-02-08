// Header

#include "stdafx.h"
#include "resource.h"
#include "commctrl.h"
#include "singleinst.h"
#include "SimpleProps.h"
#include "AdvancedProps.h"
#include "toolkit/ProfileManager.h"
#include "PublisherProperties.h"

// Implementation

CPublisherProperties::CPublisherProperties()
{
	m_bIsPropDlgVisible = false;
	m_simpleProps = NULL;
}

CPublisherProperties::~CPublisherProperties()
{
}

void CPublisherProperties::ShowPropDialog(HWND hParent)
{
	int result = 0;
	if (true == m_bIsPropDlgVisible)
		::SetForegroundWindow(m_hPropWnd);
	else
		DialogBoxParam(hAppInstance, MAKEINTRESOURCE(IDD_PUBPROP_DLG), hParent, (DLGPROC) PropDlgProc, (LONG)this);
}

BOOL CALLBACK CPublisherProperties::PropDlgProc(HWND hwnd, UINT uMsg, WPARAM wParam, LPARAM lParam)
{
	CPublisherProperties *_this = (CPublisherProperties *) GetWindowLong(hwnd, GWL_USERDATA);

	switch (uMsg)
	{

	case WM_INITDIALOG:
		{
			SetWindowLong(hwnd, GWL_USERDATA, lParam);
			CPublisherProperties* _this = (CPublisherProperties*) lParam;
			_this->m_bIsPropDlgVisible = true;
			_this->m_hPropWnd = hwnd;

			InitCommonControls();

			_this->m_hTab = GetDlgItem(hwnd, IDC_TAB_DIMDIM);

			TCITEM item;
			item.mask = TCIF_TEXT; 
			item.pszText=_T("Simple");
			TabCtrl_InsertItem(_this->m_hTab, 0, &item);
			item.pszText = _T("Advanced");
			TabCtrl_InsertItem(_this->m_hTab, 1, &item);
			TabCtrl_SetCurSel(_this->m_hTab, 0);

			int tab_id = 0;

			_this->m_hSimplePropWnd = CreateDialogParam(hAppInstance, MAKEINTRESOURCE(IDD_SIMPLEPROPS_DLG), 
				hwnd, (DLGPROC)_this->SimplePropDlgProc, (LONG)_this);

			_this->m_hAdvancedPropWnd = CreateDialogParam(hAppInstance, MAKEINTRESOURCE(IDD_ADVPROPS_DLG), 
				hwnd, (DLGPROC)_this->AdvancedPropDlgProc, (LONG)_this);

			RECT rc;
			GetWindowRect(_this->m_hTab, &rc);
			MapWindowPoints(NULL, hwnd, (POINT *)&rc, 2);
			TabCtrl_AdjustRect(_this->m_hTab, FALSE, &rc);
			SetWindowPos(_this->m_hSimplePropWnd, HWND_TOP, rc.left, rc.top,
				rc.right - rc.left, rc.bottom - rc.top,
				(tab_id == 0) ? SWP_SHOWWINDOW : SWP_HIDEWINDOW);
			SetWindowPos(_this->m_hAdvancedPropWnd, HWND_TOP, rc.left, rc.top,
				rc.right - rc.left, rc.bottom - rc.top,
				(tab_id == 1) ? SWP_SHOWWINDOW : SWP_HIDEWINDOW);

			::SetForegroundWindow(hwnd);

			return TRUE;
		}
	case WM_NOTIFY:
		{
			LPNMHDR pn = (LPNMHDR)lParam;			
			switch (pn->idFrom) 
			{
			case IDC_TAB_DIMDIM:
				{
					int id = TabCtrl_GetCurSel(_this->m_hTab);
					DWORD style;
					if (pn->code == TCN_SELCHANGE) {
						style = SW_SHOW;
					} else if (pn->code == TCN_SELCHANGING) {
						style = SW_HIDE;
					} else {
						return 0;
					}
					// FIXME: Map between tab IDs and subdialogs in one place.
					const HWND subDialogList[2] = {_this->m_hSimplePropWnd, _this->m_hAdvancedPropWnd};
					if (id >= 2) {
						// Invalid tab ID.
						return 0;
					}
					HWND subDialog = subDialogList[id];
					ShowWindow(subDialog, style);						
					SetFocus(subDialog);
					return 0;
				}
			}
			return 0;
		}
	case WM_COMMAND:
		switch (LOWORD(wParam))
		{
		case IDCANCEL:
			EndDialog(hwnd, IDCANCEL);
			_this->m_bIsPropDlgVisible = false;
			return TRUE;
		case IDOK:
			{
				// Get current profile from the dialog
				int iVal = _this->m_simpleProps->GetCurrentProfile();
				if (0 == iVal)
					CProfileManager::getInstance()->enforceHighBWProfile();
				else if (1 == iVal)
					CProfileManager::getInstance()->enforceMediumBWProfile();
				else
					CProfileManager::getInstance()->enforceLowBWProfile();
				
				// Local dump policy
				if (false == _this->m_advancedProps->LocalDumpEnabled())
					iVal = 2;
				else
					iVal = 3;
				CProfileManager::getInstance()->UpdateConfig("OperationType", iVal);

				// Logging policy

				if (false == _this->m_advancedProps->LogEnabled())
					iVal = 0;
				else
					iVal = 2;
				CProfileManager::getInstance()->UpdateConfig("Logging", iVal);

				// Commit modifications to configuration

				CProfileManager::getInstance()->CommitConfig();

				EndDialog(hwnd, IDOK);
				_this->m_bIsPropDlgVisible = false;
			}
			return TRUE;
		}
		return 0;
	}
	return 0;
}

BOOL CALLBACK CPublisherProperties::SimplePropDlgProc(HWND hwnd, UINT uMsg, WPARAM wParam, LPARAM lParam)
{
	CPublisherProperties *_this = (CPublisherProperties *) GetWindowLong(hwnd, GWL_USERDATA);

	switch (uMsg)
	{
	case WM_INITDIALOG:
		{
			// Set Low Bandwidth profile by default
			SetWindowLong(hwnd, GWL_USERDATA, lParam);
			CPublisherProperties *_this = (CPublisherProperties *) GetWindowLong(hwnd, GWL_USERDATA);
			_this->m_simpleProps = new CSimpleProps(hwnd);
			// Initialize with Low Bandwidth Profile

			int iVal = CProfileManager::getInstance()->RetrieveConfig("BlockSize");
			if (16 == iVal)
				_this->m_simpleProps->SetLBProfile();
			else if (32 == iVal)
				_this->m_simpleProps->SetMBProfile();
			else
				_this->m_simpleProps->SetHBProfile();
		}
		return 0;
	case WM_COMMAND:

		switch (LOWORD(wParam))
		{
		case IDC_RADIO_LOBW:
			_this->m_simpleProps->SetLBProfile();
			return TRUE;

		case IDC_RADIO_HIBW:
			_this->m_simpleProps->SetHBProfile();
			return TRUE;

		case IDC_RADIO_MBW:
			_this->m_simpleProps->SetMBProfile();
			return TRUE;
		}
		return 0;
	}
	return 0;
}

BOOL CALLBACK CPublisherProperties::AdvancedPropDlgProc(HWND hwnd, UINT uMsg, WPARAM wParam, LPARAM lParam)
{
	CPublisherProperties *_this = (CPublisherProperties *) GetWindowLong(hwnd, GWL_USERDATA);

	switch (uMsg)
	{
	case WM_INITDIALOG:
		{
			// Set Low Bandwidth profile by default
			SetWindowLong(hwnd, GWL_USERDATA, lParam);
			CPublisherProperties *_this = (CPublisherProperties *) GetWindowLong(hwnd, GWL_USERDATA);
			_this->m_advancedProps = new CAdvancedProps(hwnd);

			int iVal = CProfileManager::getInstance()->RetrieveConfig("OperationType");
			if (3 == iVal)
				_this->m_advancedProps->EnableLocalDump(true);
			else
				_this->m_advancedProps->EnableLocalDump(false);

			iVal = CProfileManager::getInstance()->RetrieveConfig("Logging");
			if (0 == iVal)
				_this->m_advancedProps->EnableLogging(false);
			else
				_this->m_advancedProps->EnableLogging(true);
		}
		return 0;
	}
	return 0;
}