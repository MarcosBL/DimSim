// Header

#include "stdafx.h"
#include "AdvancedProps.h"
#include "resource.h"

// Implementation

CAdvancedProps::CAdvancedProps(HWND hWnd) : m_hWnd(hWnd)
{
}

CAdvancedProps::~CAdvancedProps()
{
}

void CAdvancedProps::EnableLocalDump(bool bVal)
{
	SendMessage(GetDlgItem(m_hWnd, IDC_CHECK_ENABLEDUMP), BM_SETCHECK, (bVal == true), 0);
}

void CAdvancedProps::EnableLogging(bool bVal)
{
	SendMessage(GetDlgItem(m_hWnd, IDC_CHECK_LOG), BM_SETCHECK, (bVal == true), 0);
}	

bool CAdvancedProps::LocalDumpEnabled()
{
	if (SendMessage(GetDlgItem(m_hWnd, IDC_CHECK_ENABLEDUMP), BM_GETCHECK, 0, 0) == BST_CHECKED) 
		return true;

	return false;
}

bool CAdvancedProps::LogEnabled()
{
	if (SendMessage(GetDlgItem(m_hWnd, IDC_CHECK_LOG), BM_GETCHECK, 0, 0) == BST_CHECKED) 
		return true;

	return false;
}