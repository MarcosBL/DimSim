#if (!defined(_DIMDIM_PUBLISHER_IO))
#define _DIMDIM_PUBLISHER_IO

#include <windows.h>
#include <string>

class CXProperty;
class CPublisherIO
{
public:
	virtual ~CPublisherIO();

	static CPublisherIO* getInstance();
	static void removeInstance();
	static DWORD WINAPI run(LPVOID lParam);


	void start();
	bool executePipeQuery(std::wstring msg);
	void setPropertyObject(CXProperty* property);

	DWORD pipeServer();
	BOOL validateRegistration(CXProperty* property, bool bAffliction = false);

private:
	HANDLE m_hThread;
	HANDLE m_hPipe;
	static CPublisherIO* _instance;
	CXProperty* m_property;
	

	CPublisherIO();
	std::wstring m_sRegistrationKey;
};

#endif
