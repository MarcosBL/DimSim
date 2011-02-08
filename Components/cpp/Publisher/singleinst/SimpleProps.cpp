// Header

#include "stdafx.h"
#include "SimpleProps.h"
#include "resource.h"

// Implementation

CSimpleProps::CSimpleProps(HWND hWnd) : m_hWnd(hWnd)
{
}

CSimpleProps::~CSimpleProps()
{
}

void CSimpleProps::SetHBProfile()
{
	SendMessage(GetDlgItem(m_hWnd, IDC_RADIO_HIBW), BM_SETCHECK, TRUE, 0);
	SendMessage(GetDlgItem(m_hWnd, IDC_RADIO_LOBW), BM_SETCHECK, FALSE, 0);
	SendMessage(GetDlgItem(m_hWnd, IDC_RADIO_MBW), BM_SETCHECK, FALSE, 0);
}

void CSimpleProps::SetLBProfile()
{
	SendMessage(GetDlgItem(m_hWnd, IDC_RADIO_HIBW), BM_SETCHECK, FALSE, 0);
	SendMessage(GetDlgItem(m_hWnd, IDC_RADIO_LOBW), BM_SETCHECK, TRUE, 0);
	SendMessage(GetDlgItem(m_hWnd, IDC_RADIO_MBW), BM_SETCHECK, FALSE, 0);
}

void CSimpleProps::SetMBProfile()
{
	SendMessage(GetDlgItem(m_hWnd, IDC_RADIO_HIBW), BM_SETCHECK, FALSE, 0);
	SendMessage(GetDlgItem(m_hWnd, IDC_RADIO_LOBW), BM_SETCHECK, FALSE, 0);
	SendMessage(GetDlgItem(m_hWnd, IDC_RADIO_MBW), BM_SETCHECK, TRUE, 0);
}

int CSimpleProps::GetCurrentProfile()
{
	if (SendMessage(GetDlgItem(m_hWnd, IDC_RADIO_LOBW), BM_GETCHECK, 0, 0) == BST_CHECKED) 
		return 2;
	if (SendMessage(GetDlgItem(m_hWnd, IDC_RADIO_MBW), BM_GETCHECK, 0, 0) == BST_CHECKED) 
		return 1;
	if (SendMessage(GetDlgItem(m_hWnd, IDC_RADIO_HIBW), BM_GETCHECK, 0, 0) == BST_CHECKED) 
		return 0;

	return 1;
}