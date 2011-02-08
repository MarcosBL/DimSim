/**	\file	PublisherProperties.h
 *	\brief	Header for class CPublisherProperties
 *	\author	Bharat Varma Nadimpalli
 *	\date	01-08-2007
 */

#if (!defined(_DIMDIM_PUBLISHER_PROPERTIES))
#define _DIMDIM_PUBLISHER_PROPERTIES

class CSimpleProps;
class CAdvancedProps;

class CPublisherProperties
{
public:
	
	/**	\brief		Constructor
	 */
	CPublisherProperties();

	/**	\brief		Destructor
	 */
	virtual ~CPublisherProperties();

	void ShowPropDialog(HWND hParent);

protected:

	HWND			m_hPropWnd;
	HWND			m_hSimplePropWnd;
	HWND			m_hAdvancedPropWnd;
	HWND			m_hTab;
	bool			m_bIsPropDlgVisible;


	CSimpleProps* m_simpleProps;
	CAdvancedProps* m_advancedProps;

	static BOOL CALLBACK PropDlgProc(HWND hwnd, UINT uMsg, WPARAM wParam, LPARAM lParam);
	static BOOL CALLBACK SimplePropDlgProc(HWND hwnd, UINT uMsg, WPARAM wParam, LPARAM lParam);
	static BOOL CALLBACK AdvancedPropDlgProc(HWND hwnd, UINT uMsg, WPARAM wParam, LPARAM lParam);
};

#endif
