/**	\file	PublisherTray.h
 *	\brief	Header for class CPublisherTray
 *	\author	Bharat Varma Nadimpalli
 *	\date	12-04-2007
 */

#if (!defined(_DIMDIM_PUBLISHER_PUBLISHERTRAY))
#define _DIMDIM_PUBLISHER_PUBLISHERTRAY

#include <windows.h>
#include <lmcons.h>

// Constants

extern const UINT	DIMDIM_PUBLISHER_TRAY_ABOUT;
extern const UINT	DIMDIM_PUBLISHER_TRAY_STOPSCRNCSTR;
extern const UINT	DIMDIM_PUBLISHER_TRAY_STOPCNVRTR;
extern const UINT	DIMDIM_PUBLISHER_TRAY_KILL;
extern const TCHAR*	DIMDIM_PUBLISHER_TRAY_CLASS_NAME;

class CPublisherProperties;
class CProxyMgmt;

/** \brief	Handles the tray icon for Dimdim Publisher
 *
 *	CPublisherTray is responsible for the tray icon management and the outer-most layer of 
 *	communication between the publisher and the functional components.
 *
 *	The messages sent by the functional components reach CPublisherTray first and are then
 *	passed on to CPublisher for further handling.
 */
class CPublisherTray
{
public:
	
	/**	\brief		Constructor
	 */
	CPublisherTray();

	/**	\brief		Destructor
	 */
	virtual ~CPublisherTray();

	void SetNIDProgress(bool bVal = false);
	void ShowBalloon(std::wstring sMessage, std::wstring type = _T("info"));

protected:
	
	// Tray icon handling

	/**	\brief		Adds icon to the system tray
	 *	\param		none
	 *	\returns	none
	 */
	void AddToTray();

	/**	\brief		Removes icon from the system tray
	 *	\param		none
	 *	\returns	none
	 */
	void RemoveFromTray();

	/**	\brief		Removes the tray icon and destroys its menu object
	 *	\param		none
	 *	\returns	none
	 */
	void cleanup();

	/**	\brief		Sets a tooltip for the tray icon, which is shown when mouse hovers over the icon
	 *	\param		The tooltip to be set (optionally, an empty string)
	 *	\returns	none
	 *
	 *	If an empty string is passed to be set as the tool tip, a fixed default string is set.
	 */
	void SetToolTip(std::wstring sTip = _T(""));

	/**	\brief		Inform the tray icon of a change in its data, so it can refresh its state with the new data.
	 *	\param		Message to be sent to the tray
	 *	\returns	none
	 */
	void SendTrayMsg(DWORD msg);

	static LRESULT CALLBACK WndProc(HWND hwnd, UINT iMsg, WPARAM wParam, LPARAM lParam);

	// Fields
protected:

	HWND			m_hwnd;
	HMENU			m_hmenu;
	NOTIFYICONDATAW	m_nid;

	// The icon handles
	HICON			m_iNormal;
	HICON			m_iProgress;

	CPublisherProperties* m_pubProps;
	CPublisherProperties* GetPropObj() {return m_pubProps;}

	CProxyMgmt* m_proxyMgmt;
	CProxyMgmt* getProxyObj() {return m_proxyMgmt;}
};

#endif
