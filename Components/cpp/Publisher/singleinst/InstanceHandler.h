/**	\file	InstanceHandler.h
 *	\brief	Header for class CInstanceHandler
 *	\author	Bharat Varma Nadimpalli
 *	\date	12-04-2007
 */

#if (!defined(_DIMDIM_PUBLISHER_INSTANCEHANDLER))
#define _DIMDIM_PUBLISHER_INSTANCEHANDLER

/** \brief	Makes sure only one instance of the Publisher subsystem exists
 */
class CInstanceHandler
{

public:
	/**	\brief		Constructor
	 */
	CInstanceHandler();

	/**	\brief		Destructor
	 */
	~CInstanceHandler();

	/**	\brief		Manages singularity of the publisher subsystem
	 *	\param		none
	 *	\returns	'TRUE' if called for the first time. 'FALSE' otherwise.
	 *
	 *	Creates a named mutex the first time the method is called.
	 *	From the second time onwards, detects that the named mutex exists and returns FALSE.
	 *	This is used to prevent creation of the publisher more than once.
	 */
	BOOL init();

	/**	\brief		Destroyes the mutex created in init()
	 *	\param		none
	 *	\returns	Any error in releasing the named mutex
	 */
	DWORD release();

private:
	HANDLE m_instanceMutex;

};

#endif
