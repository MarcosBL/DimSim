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
File Name  : dssAMFMixedArray.cpp
Author     : Bharat Varma
Email      : bharat@dimdim.com
Created On : Mon April 30 14:51:45 GMT+05:30 2007
------------------------------------------------------- */
#include "AMFMixedArray.h"
#include "AMFNull.h"
#include "AMFCodec.h"
#include "AMFTable.h"

namespace dm
{
	namespace amf
	{
		using amf::AMFTableEntry;

		AMFMixedArray::AMFMixedArray()
		{
		}

		AMFMixedArray::~AMFMixedArray()
		{
		//	clear();
		}

		AMFVariant* AMFMixedArray::get(const String key)
		{
			for(size_t s = 0; s < entries.size(); s++)
			{
				AMFTableEntry* entry = entries[s];
				if(entry && entry->getKey()->getValue() == key)
				{
					return entry->getValue();
				}
			}
			return 0;
		}

		AMFVariant* AMFMixedArray::getValueAt(int iPos)
		{
			AMFTableEntry* entry = entries[iPos];
			if (entry)
				return entry->getValue();
			return 0;
		}

		AMFTableEntry* AMFMixedArray::getEntryAt(int iPos)
		{
			return entries[iPos];
		}

		void  AMFMixedArray::put(const String key, AMFVariant* var)
		{

			for(size_t s = 0; s < entries.size(); s++)
			{
				AMFTableEntry* entry = entries[s];
				if(entry && entry->getKey()->getValue() == key)
				{
					//this shall delete the old value
					AMFVariant* oldVal = entry->getValue();
					DDSS_SAFE_DELETE_PTR(oldVal);
					entry->setValue(var);
					return;
				}
			}

			// create a new entry and shove it into the list
			AMFTableEntry* entry = new AMFTableEntry();
			entry->setKey(key);
			entry->setValue(var);
			entries.push_back(entry);
		}
		bool  AMFMixedArray::containsKey(const String key)
		{
			return get(key) != 0;
		}
		void  AMFMixedArray::clear()
		{
			Helper::clearPtrVector(entries);
		}
		void AMFMixedArray::write(OutputStream* output)
		{
			output->writeLong(entries.size());

			for(size_t s = 0; s < entries.size(); s++)
			{
				AMFTableEntry* entry = entries[s];
				entry->write(output);
			}
			//write end of object
			u8 octets[3];
			memset(octets,0,3);
			octets[2] = AMFConstants::AMF_TYPE_ENDOFOBJECT;
			output->write(octets,3);
		}
		void AMFMixedArray::read(InputStream* input)
		{
			bool objFinished = false;
			u8 octets[3];

			u32 entrySize = 0;
			input->readLong(&entrySize);

			while(!input->eof() && !objFinished)
			{
				input->peek(octets,3);
				//input->read(octets,3);
				if(octets[0] == 0 && octets[1] == 0 && octets[2] == AMFConstants::AMF_TYPE_ENDOFOBJECT)
				{
					input->read(octets,3);
					objFinished = true;
					break;
				}
				else
				{
					AMFTableEntry* entry = new AMFTableEntry();
					entry->read(input);
					entries.push_back(entry);
				}
			}
		}

		u8 AMFMixedArray::getType() const
		{
			return AMFConstants::AMF_TYPE_MIXEDARRAY;
		}

		std::ostream& AMFMixedArray::dump(std::ostream& os) const
		{
			os<<std::endl<<std::endl;
			for(size_t s = 0; s < entries.size(); s++)
			{
				os<<"["<<(u32)s<<"] :: ";
				entries[s]->dump(os);
				os<<std::endl;
			}
			return os;
		}

	};
};
