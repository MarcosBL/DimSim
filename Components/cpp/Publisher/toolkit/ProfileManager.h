/**	\file	ProfileManager.h
 *	\brief	Header for class CProfileManager
 *	\author	Bharat Varma Nadimpalli
 *	\date	01-09-2007
 */

#if (!defined(_DIMDIM_PUBLISHER_PROFILEMANAGER))
#define _DIMDIM_PUBLISHER_PROFILEMANAGER

#include <map>
#include <string>

class CProfileManager
{
public:

	/**	\brief		Destructor
	 */
	virtual ~CProfileManager();

	void LoadConfig(std::string currentDir = "");
	int RetrieveConfig(std::string key);
	void CommitConfig(std::string currentDir = "");

	void enforceHighBWProfile();
	void enforceMediumBWProfile();
	void enforceLowBWProfile();

	void UpdateConfig(std::string key, int val);

	static CProfileManager* getInstance();
	static void removeInstance();

private:

	/**	\brief		Constructor
	 */
	CProfileManager();

	static CProfileManager* instance_;

protected:

	std::map<std::string, int> paramMap;
	std::map<std::string, std::string> descMap;
};

#endif
