/**	\file	Publisher.h
 *	\brief	Header for class CPublisher
 *	\author	Bharat Varma Nadimpalli
 *	\date	12-04-2007
 */

#if (!defined(_DIMDIM_PUBLISHER))
#define _DIMDIM_PUBLISHER

#define			WM_DIMDIM_SCREENCASTER_PROGRESS				WM_USER+8128
#define			WM_DIMDIM_CONVERTER_PROGRESS				WM_USER+8129
#define			WM_DIMDIM_IDLE								WM_USER+8130
#define			WM_DIMDIM_SCREENCASER_FORCELOW				WM_USER+8131

/**	\brief		Is a Singleton and acts as a translator between browser interfaces and functional components of Dimdim Publisher
 *
 *	CPublisher translates requests sent out by browser interfaces and decides on the component used to execute
 *	this request. It also translates information made available by the functional components during the course 
 *	of execution to the browser interfaces, which then advise the Dimdim Conference Server on further action.
 *
 *	The functional components available at the time of writing this class are PPTX uploader
 *	and the Screencaster.
 *
 */
class CPublisher
{
public:
	/** \brief		Destructor
	 */
	virtual ~CPublisher(){};

	/** \brief		returns the singleton instance CPublisher
	 *	\param		none
	 *	\returns	a pointer to the singleton instance of CPublisher
	 */
	static CPublisher* getInstance();

	/** \brief		destroys and cleans up the singleton instance of CPublisher
	 *	\param		none
	 *	\returns	void
	 */
	static void removeInstance();

	/**	\brief		sends a message to a browser interface
	 *	\param		message to be sent
	 *	\returns	result
	 *
	 *	Communication between publisher and a browser interface
	 *	is done through a named pipe. The browser interface hosts a server
	 *	and whenever there is data to be sent, publisher opens a client
	 *	and puts the message on the pipe for the browser interface to read.
	 */
	bool notifyBrowserInterface(std::wstring msg);

	/**	\brief		performs an operation on the screencaster component
	 *	\param		operation to be executed
	 *	\param		optional argument list
	 *	\returns	result
	 *
	 *	'operation' can take 'connect', 'sharewindow', 'shareall', 'kill'
	 *	'argument'	can take a connect string, a window handle/name or an empty string.
	 *
	 *	The result returned from this operation merely denotes success of passing the 
	 *	operation to be executed to the screencaster, which is invariably 'true'.
	 *
	 *	The true operation execution status is conveyed to the browser interface directly
	 *	by the screencaster by connecting to the pipeserver of the browser interface.
	 *
	 *	This method only supports atomic operations. For a combination of operations,
	 *	this method needs to be called as many times as required, with appropriate 
	 *	operation and argument pairs, with reasons provided for such usage.
	 */
	bool executeScreencaster(std::wstring operation, std::wstring argument = _T(""));

	/**	\brief		performs an operation on the converter component
	 *	\param		operation to be executed
	 *	\param		optional argument list
	 *	\returns	result
	 *
	 *	'operation' can take 'convert' or 'cancel'.
	 *	'argument' in case of 'convert' takes dmsURL, confURL and the filePath
	 *	
	 *	The result returned from this operation merely denotes success of passing the 
	 *	operation to be executed to the converter, in case of 'convert'. In case of 
	 *	'cancel', result represents the actual execution result.
	 */
	bool executeConverter(std::wstring operation, std::wstring argument = _T(""));

	void screencastTrack();

	/**	\brief		searches for and kills screencaster/converter components
	 *	\param		none
	 *	\returns	none
	 *
	 *	Looks up the process table of host machine and checks if either
	 *	screencaster or converter is running.
	 *	If found, it does a 'clean kill' of that process.
	 *	This method is used *only* when 'Exit' option from the tray icon
	 *	is invoked, by user action.
	 */
	void disconnectComponents();

	void invokeDriverInstall();
	UINT isDriverInstalled();

private:
	/**	\brief		Constructor (private)
	 */
	CPublisher()
	{
	}

	/**	\brief		static instance of CPublisher
	 */
	static CPublisher* _pubInstance;
};

#endif
