/**	\file	SimpleProps.h
 *	\brief	Header for class CSimpleProps
 *	\author	Bharat Varma Nadimpalli
 *	\date	01-09-2007
 */

#if (!defined(_DIMDIM_PUBLISHER_SIMPLEPROPS))
#define _DIMDIM_PUBLISHER_SIMPLEPROPS


class CSimpleProps
{
public:
	
	/**	\brief		Constructor
	 */
	CSimpleProps(HWND hWnd);

	/**	\brief		Destructor
	 */
	virtual ~CSimpleProps();

	void SetHBProfile();
	void SetLBProfile();
	void SetMBProfile();

	int GetCurrentProfile();

protected:

	HWND m_hWnd;
	
};

#endif
