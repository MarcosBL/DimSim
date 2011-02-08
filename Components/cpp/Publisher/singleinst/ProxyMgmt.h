/**	\file	ProxyMgmt.h
 *	\brief	Header for class CProxyMgmt
 *	\author	Bharat Varma Nadimpalli
 *	\date	04-21-2007
 */

#if (!defined(_DIMDIM_PROXY_MGMT))
#define _DIMDIM_PROXY_MGMT

class CProxyMgmt
{
public:
	
	/**	\brief		Constructor
	 */
	CProxyMgmt();

	/**	\brief		Destructor
	 */
	virtual ~CProxyMgmt();

	void ShowProxyDlg(HWND hParent);

protected:

	HWND			m_hProxyWnd;
	bool			m_bIsProxyWndVisible;

	static BOOL CALLBACK ProxyDlgProc(HWND hwnd, UINT uMsg, WPARAM wParam, LPARAM lParam);
};

#endif
