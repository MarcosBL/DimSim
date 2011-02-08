// xpublisher.h : Declaration of the Cxpublisher
#pragma once
#include "resource.h"       // main symbols
#include <atlctl.h>

#include "xproperty.h"
#include "Translator.h"
#include "publisherio.h"

// Ixpublisher
[
	object,
	uuid(078FA70F-278A-4028-ADA4-3C2D8B733A7F),
	dual,
	helpstring("Ixpublisher Interface"),
	pointer_default(unique)
]
__interface Ixpublisher : public IDispatch
{
	[id(1), helpstring("method getProperty")] HRESULT getProperty([in] BSTR args, [out,retval] BSTR* buf);
	[id(2), helpstring("method getVersion")] HRESULT getVersion([out,retval] LONG* retval);
	[id(3), helpstring("method performAction")] HRESULT performAction([in] BSTR args, [out,retval] LONG* retval);
	[id(4), helpstring("method setProperty")] HRESULT setProperty([in] BSTR args, [out,retval] LONG* retval);
};


// _IxpublisherEvents
[
	uuid("A1E3BC92-F828-4820-8832-4377621768F7"),
	dispinterface,
	helpstring("_IxpublisherEvents Interface")
]
__interface _IxpublisherEvents
{
};

// Cxpublisher
[
	coclass,
	threading("apartment"),
	vi_progid("xDimdimControl_40.xpublisher"),
	progid("xDimdimControl_40.xpublisher.1"),
	version(1.0),
	uuid("5100F713-1B48-4A6B-9985-EDDFB7F1C0DF"),
	helpstring("xpublisher Class"),
	event_source("com"),
	registration_script("control.rgs")
]
class ATL_NO_VTABLE Cxpublisher : 
	public Ixpublisher,
	public IPersistStreamInitImpl<Cxpublisher>,
	public IOleControlImpl<Cxpublisher>,
	public IOleObjectImpl<Cxpublisher>,
	public IOleInPlaceActiveObjectImpl<Cxpublisher>,
	public IViewObjectExImpl<Cxpublisher>,
	public IOleInPlaceObjectWindowlessImpl<Cxpublisher>,
	public CComControl<Cxpublisher>,
	public IObjectSafetyImpl<Cxpublisher, INTERFACESAFE_FOR_UNTRUSTED_CALLER>
{
private:
	CXProperty* m_property;
	void setupDriver()
	{
		m_property->setDriverStatus(0);
		Sleep(100);
		CTranslator::executePublisherCheckDriverW();
		HANDLE driverTimer = CreateEvent(NULL, true, false, _T("driverTimer"));
		int iCount = 0;
		while (true)
		{
			if (0 != m_property->driverInstalled() || iCount >= 15)
			{
				break;
			}
			::WaitForSingleObject(driverTimer, 500);
			iCount += 1;
		}
		CloseHandle(driverTimer);
	}
public:

	Cxpublisher()
	{
		m_property = new CXProperty();
		CTranslator::setWorkingDirectoryW(_T("xDimdimControl_40.dll"));
	}

	~Cxpublisher()
	{
		if (TRUE == CPublisherIO::getInstance()->validateRegistration(m_property, true))
			CPublisherIO::removeInstance();

		if (NULL != m_property)
		{
			delete m_property;
			m_property = 0;
		}
	}

DECLARE_OLEMISC_STATUS(OLEMISC_RECOMPOSEONRESIZE | 
	OLEMISC_INVISIBLEATRUNTIME |
	OLEMISC_CANTLINKINSIDE | 
	OLEMISC_INSIDEOUT | 
	OLEMISC_ACTIVATEWHENVISIBLE | 
	OLEMISC_SETCLIENTSITEFIRST
)


BEGIN_PROP_MAP(Cxpublisher)
	PROP_DATA_ENTRY("_cx", m_sizeExtent.cx, VT_UI4)
	PROP_DATA_ENTRY("_cy", m_sizeExtent.cy, VT_UI4)
	// Example entries
	// PROP_ENTRY("Property Description", dispid, clsid)
	// PROP_PAGE(CLSID_StockColorPage)
END_PROP_MAP()


BEGIN_MSG_MAP(Cxpublisher)
	CHAIN_MSG_MAP(CComControl<Cxpublisher>)
	DEFAULT_REFLECTION_HANDLER()
END_MSG_MAP()
// Handler prototypes:
//  LRESULT MessageHandler(UINT uMsg, WPARAM wParam, LPARAM lParam, BOOL& bHandled);
//  LRESULT CommandHandler(WORD wNotifyCode, WORD wID, HWND hWndCtl, BOOL& bHandled);
//  LRESULT NotifyHandler(int idCtrl, LPNMHDR pnmh, BOOL& bHandled);

	__event __interface _IxpublisherEvents;
// IViewObjectEx
	DECLARE_VIEW_STATUS(VIEWSTATUS_SOLIDBKGND | VIEWSTATUS_OPAQUE)

// Ixpublisher
public:
		HRESULT OnDraw(ATL_DRAWINFO& di)
		{
		RECT& rc = *(RECT*)di.prcBounds;
		// Set Clip region to the rectangle specified by di.prcBounds
		HRGN hRgnOld = NULL;
		if (GetClipRgn(di.hdcDraw, hRgnOld) != 1)
			hRgnOld = NULL;
		bool bSelectOldRgn = false;

		HRGN hRgnNew = CreateRectRgn(rc.left, rc.top, rc.right, rc.bottom);

		if (hRgnNew != NULL)
		{
			bSelectOldRgn = (SelectClipRgn(di.hdcDraw, hRgnNew) != ERROR);
		}

		Rectangle(di.hdcDraw, rc.left, rc.top, rc.right, rc.bottom);
		SetTextAlign(di.hdcDraw, TA_CENTER|TA_BASELINE);
		LPCTSTR pszText = _T("ATL 7.0 : xpublisher");
		TextOut(di.hdcDraw, 
			(rc.left + rc.right) / 2, 
			(rc.top + rc.bottom) / 2, 
			pszText, 
			lstrlen(pszText));

		if (bSelectOldRgn)
			SelectClipRgn(di.hdcDraw, hRgnOld);

		return S_OK;
	}


	DECLARE_PROTECT_FINAL_CONSTRUCT()

	HRESULT FinalConstruct()
	{
		return S_OK;
	}
	
	void FinalRelease() 
	{
	}
	STDMETHOD(getProperty)(BSTR args, BSTR* buf);
	STDMETHOD(getVersion)(LONG* retval);
	STDMETHOD(performAction)(BSTR args, LONG* retval);
	STDMETHOD(setProperty)(BSTR args, LONG* retval);
};

