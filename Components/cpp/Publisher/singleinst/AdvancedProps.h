/**	\file	AdvancedProps.h
 *	\brief	Header for class CAdvancedProps
 *	\author	Bharat Varma Nadimpalli
 *	\date	01-09-2007
 */

#if (!defined(_DIMDIM_PUBLISHER_ADVANCEDPROPS))
#define _DIMDIM_PUBLISHER_ADVANCEDPROPS


class CAdvancedProps
{
public:
	
	/**	\brief		Constructor
	 */
	CAdvancedProps(HWND hWnd);

	/**	\brief		Destructor
	 */
	virtual ~CAdvancedProps();

	bool LocalDumpEnabled();
	bool LogEnabled();

	void EnableLocalDump(bool bVal);
	void EnableLogging(bool bVal);

protected:

	HWND m_hWnd;
	
};

#endif
