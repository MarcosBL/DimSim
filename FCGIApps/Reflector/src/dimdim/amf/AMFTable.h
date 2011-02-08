//$$<!--TOFUTAG-->$$//
 /**************************************************************************
  *                                                                        *
  *               DDDDD   iii             DDDDD   iii                      *
  *               DD  DD      mm mm mmmm  DD  DD      mm mm mmmm           *
  *               DD   DD iii mmm  mm  mm DD   DD iii mmm  mm  mm          *
  *               DD   DD iii mmm  mm  mm DD   DD iii mmm  mm  mm          *
  *               DDDDDD  iii mmm  mm  mm DDDDDD  iii mmm  mm  mm          *
  *                                                                        *
  **************************************************************************/
/* *************************************************************************
	THIS FILE IS PART OF THE DIMDIM CODEBASE. TO VIEW LICENSE AND EULA
	FOR THIS CODE VISIT http://www.dimdim.com
   ************************************************************************ */

/* ------------------------------------------------------
    File Name  : dssAMFTable.h
    Author     : Saurav Mohapatra
    Email      : Saurav.Mohapatra@dimdim.com
    Created On : Sun Jun 04 04:34:57 GMT+05:30 2006
  ------------------------------------------------------- */
#ifndef _DIMDIM_AMF_TABLE_H_
#define _DIMDIM_AMF_TABLE_H_
#include "AMFVariant.h"
#include "AMFStringUTF8.h"
namespace dm
{
	namespace amf
	{
		class AMFTableEntry;
		class AMFTable;
		class AMFVariant;

		///
		///	this class represents an AMF Table entry (a name value pair)
		///
		class DSSAMFAPI AMFTableEntry
		{
		public:
			AMFTableEntry();
			virtual ~AMFTableEntry();

			AMFStringUTF8* getKey();
			AMFVariant* getValue();

			void setKey(const String keyStr);
			void setValue(AMFVariant* val);

			void read(InputStream* input);
			void write(OutputStream* output);
			std::ostream& dump(std::ostream& os) const;

		private:
			AMFStringUTF8 key;
			AMFVariant*   value;
			DSS_FORCE_BY_REF_ONLY(AMFTableEntry);
		};



	///
	///	An AMFTable is a collection of name-value pairs (AMFTableEntry)
	///
	class DSSAMFAPI AMFTable : public AMFVariant
	{
	public:
		AMFTable();
		virtual ~AMFTable();

		AMFVariant* get(const String key);
		AMFVariant* getValueAt(int iPos);
		AMFTableEntry* getEntryAt(int iPos);
		size_t getEntrySize() { return entries.size(); }
		void  put(const String key, AMFVariant* var);
		bool  containsKey(const String key);
		void clear();

		void write(OutputStream* output);
		void read(InputStream* input);
		std::ostream& dump(std::ostream& os) const;

	private:
		std::vector<AMFTableEntry*> entries;
		DSS_FORCE_BY_REF_ONLY(AMFTable);
	};
};
};


#endif
