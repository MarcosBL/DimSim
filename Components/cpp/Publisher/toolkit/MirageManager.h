/**	\file	MirageManager.h
 *	\brief	Header for class CMirageManager
 *	\author	Bharat Varma Nadimpalli
 *	\date	01-09-2007
 */

#if (!defined(_DIMDIM_PUBLISHER_MIRAGEMANAGER))
#define _DIMDIM_PUBLISHER_MIRAGEMANAGER

#include <windows.h>
#include <winreg.h>
#include <string>
#include <atlbase.h>

enum {	dfext_devmode_size_max	= 3072 };
struct DFEXT_DEVMODE : DEVMODE
{
	char	Extension[dfext_devmode_size_max];
};

struct	Esc_dmf_Qvi_IN
{
	ULONG	cbSize;

	ULONG	app_actual_version;
	ULONG	display_minreq_version;

	ULONG	connect_options;		// reserved. must be 0.
};

enum
{
	esc_qvi_prod_name_max	= 16,
};

#define	ESC_QVI_PROD_MIRAGE	"MIRAGE"

struct	Esc_dmf_Qvi_OUT
{
	ULONG	cbSize;

	ULONG	display_actual_version;
	ULONG	miniport_actual_version;
	ULONG	app_minreq_version;
	ULONG	display_buildno;
	ULONG	miniport_buildno;

	char	prod_name[esc_qvi_prod_name_max];
};

class CMirageManager
{
public:
	static UINT isDriverInstalled();
	static void invokeMirageInstaller(std::wstring currentDir);

private:
	static std::wstring createInstallerScript(std::wstring currentDir);
	static BOOL LookupDisplayDevices(DISPLAY_DEVICE& deviceInfo, DWORD& deviceNumber, bool bShouldLog = true);
	static LONG OpenDeviceRegistryKey(CRegKey& regkeyDevice, DISPLAY_DEVICE& deviceInfo);
	static LONG SetupDriver(CRegKey& regKeyDevice, DISPLAY_DEVICE& deviceInfo, Esc_dmf_Qvi_OUT& qvi_out);
	static LONG CommitDisplayChanges(DEVMODE* pdm, DISPLAY_DEVICE& deviceInfo);
	
	static void WriteDiagnostics(const std::wstring& data);
	static void CleanupDiagnostics();
};

#endif
