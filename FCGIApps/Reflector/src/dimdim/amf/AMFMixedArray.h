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
    File Name  : dssAMFMixedArray.h
    Author     : Bharat Varma
    Email      : bharat@dimdim.com
    Created On : Mon April 30 14:44:57 GMT+05:30 2007
  ------------------------------------------------------- */
#ifndef _DIMDIM_AMF_MIXEDARRAY_H_
#define _DIMDIM_AMF_MIXEDARRAY_H_
#include "AMFVariant.h"
#include "AMFStringUTF8.h"

namespace dm
{
	namespace amf
	{
		///
		///	An AMF Mixed Array has the following structure
		///		-	Represented by a code 0x08
		///		-	A Long representing the highest numeric index in the array (0 if there are none of if all indices are negative)
		///		-	Key-Value pairs follow
		///		-	Ends with an empty UTF8 String (0x00 0x00) and 0x09 - Same as in case of AMFObject
		///
		///	A Mixed Array can have floating point or negative keys. It can also have non-integer keys
		///

		class AMFTableEntry;
		class AMFVariant;
		class AMFTable;

		class DSSAMFAPI AMFMixedArray : public AMFVariant
		{
		public:
			AMFMixedArray();
			virtual ~AMFMixedArray();

			AMFVariant* get(const String key);
			AMFVariant* getValueAt(int iPos);
			AMFTableEntry* getEntryAt(int iPos);

			void  put(const String key, AMFVariant* var);
			bool  containsKey(const String key);
			void clear();

			void write(OutputStream* output);
			void read(InputStream* input);
			std::ostream& dump(std::ostream& os) const;
			u32 getEntrySize() {return entries.size();}

			u8 getType() const;

		private:
			DSS_FORCE_BY_REF_ONLY(AMFMixedArray);					
			std::vector<AMFTableEntry*> entries;
		};
	};
};
#endif
