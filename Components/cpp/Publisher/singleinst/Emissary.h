/**	\file	Emissary.h
 *	\brief	Header for class CEmissary
 *	\author	Bharat Varma Nadimpalli
 *	\date	12-04-2007
 */

#if (!defined(_DIMDIM_PUBLISHER_EMISSARY))
#define _DIMDIM_PUBLISHER_EMISSARY

#include "PublisherTray.h"

/**	\brief		Is a Singleton and is used by the outside world to communicate with internal workings of the publisher
 *
 *	When an operation is executed on the publisher instance by a browser interface, the arguments for the operation are 
 *	parsed and the appropriate message is conveyed either to the CPublisherTray object, or the CPublisher object, 
 *	which process the message further
 */
class CEmissary
{
public:
	/** \brief		Destructor
	 */
	virtual ~CEmissary();

	/** \brief		returns the singleton instance CEmissary
	 *	\param		none
	 *	\returns	a pointer to the singleton instance of CEmissary
	 */
	static CEmissary* getInstance();

	/** \brief		destroys and cleans up the singleton instance of CEmissary
	 *	\param		none
	 *	\returns	void
	 */
	static void removeInstance();

	/**	\brief		creates a publisher tray icon
	 *	\param		none
	 *	\returns	'false' if unsuccessful in creating the tray icon
	 */
	bool initTray();

	/**	\brief		starts a conversion process on the converter
	 *	\param		arguments for the conversion
	 *	\returns	none
	 */
	void converterStart(std::wstring args);

	/**	\brief		cancels a conversion process on the converter
	 *	\param		docID
	 *	\returns	none
	 */	
	void converterKill(std::wstring args);

	/**	\brief		starts the screencaster
	 *	\param		none
	 *	\returns	none
	 */
	void screencastRun();

	/**	\brief		connects the screencaster
	 *	\param		screencastURL
	 *	\returns	none
	 */
	void screencastConnect(std::wstring screencastURL);

	/**	\brief		shares a handle through screencaster
	 *	\param		window handle (0 for desktop)
	 *	\returns	none
	 */
	void screencastShare(std::wstring handle);

	/**	\brief		kills screencaster
	 *	\param		none
	 *	\returns	none
	 */
	void screencastKill();

	void screencastTrack();


	/**	\brief		cleans up anything created by the emissary
	 *	\param		none
	 *	\returns	none
	 */
	void cleanup();

	/**	\brief		exits the publisher instance
	 *	\param		none
	 *	\returns	none
	 */
	void exitTray();

	void setMenuItemState(std::wstring itemString, std::wstring state);

	void notifyBrowserInterface(std::wstring msg);

	void manageDriver();

private:

	/**	\brief		Constructor (private)
	 */
	CEmissary();

	/**	\brief		static instance of CEmissary
	 */
	static CEmissary* _emiInstance;

	CPublisherTray* m_pubTray;
};

#endif
