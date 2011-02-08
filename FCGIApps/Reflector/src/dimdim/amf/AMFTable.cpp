//$$<!--TOFUTAG-->$$//
 /**************************************************************************
  *                                                                        *
  *               DDDDD   iii             DDDDD   iii                      *
  *               DD  DD      mm mm mmmm  DD  DD      mm mm mmmm           *
  *               DD   DD iii mmm  mm  mm DD   DD iii mmm  mm  mm          *
  *               DD   DD iii mmm  mm  mm DD   DD iii mmm  mm  mm          *
  *               DDDDDD  iii mmm  mm  mm DDDDDD  iii mmm  mm  mm          *
  *                                                                        *
  **************************************************************************
/* *************************************************************************
	THIS FILE IS PART OF THE DIMDIM CODEBASE. TO VIEW LICENSE AND EULA
	FOR THIS CODE VISIT http://www.dimdim.com
   ************************************************************************ */

/* ------------------------------------------------------
    File Name  : dssAMFTable.cpp
    Author     : Saurav Mohapatra
    Email      : Saurav.Mohapatra@dimdim.com
    Created On : Sun Jun 04 04:34:57 GMT+05:30 2006
  ------------------------------------------------------- */
#include "AMFTable.h"
#include "AMFCodec.h"
#include "AMFNull.h"
namespace dm
{
	namespace amf
	{
		AMFTableEntry::AMFTableEntry() : value(0)
		{
		}
		AMFTableEntry::~AMFTableEntry()
		{
			DSS_SAFE_DELETE_PTR(value);
		}

		AMFStringUTF8* AMFTableEntry::getKey()
		{
			return &key;
		}
		AMFVariant* AMFTableEntry::getValue()
		{
			return value;
		}

		void AMFTableEntry::setKey(const String keyStr)
		{
			key.setValue(keyStr);
		}
		void AMFTableEntry::setValue(AMFVariant* val)
		{
			DSS_SAFE_DELETE_PTR(value);
			value = val;
		}

		void AMFTableEntry::read(InputStream* input)
		{
			key.read(input);
			AMFVariant* val = AMFCodec::decode(input);
			setValue(val);
		}
		void AMFTableEntry::write(OutputStream* output)
		{
			key.write(output);
			if(value)
			{
				AMFCodec::encode(output,value);
			}
			else
			{
				AMFNull nullValue;
				AMFCodec::encode(output,&nullValue);
			}
		}
		std::ostream& AMFTableEntry::dump(std::ostream& os) const
		{
			os<<key.getValue()<<" = ";
			if(value)
			{
				value->dumpAll(os);
			}
			else
			{
				os<<"<null>";
			}
			return os;
		}

		AMFTable::AMFTable()
		{
		}
		AMFTable::~AMFTable()
		{
			clear();
		}

		AMFVariant* AMFTable::getValueAt(int iPos)
		{
			AMFTableEntry* entry = entries[iPos];
			if (entry)
				return entry->getValue();
			return 0;
		}

		AMFTableEntry* AMFTable::getEntryAt(int iPos)
		{
			return entries[iPos];
		}

		AMFVariant* AMFTable::get(const String key)
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
		void  AMFTable::put(const String key, AMFVariant* var)
		{

			for(size_t s = 0; s < entries.size(); s++)
			{
				AMFTableEntry* entry = entries[s];
				if(entry && entry->getKey()->getValue() == key)
				{
					//this shall delete the old value
					AMFVariant* oldVal = entry->getValue();
					DSS_SAFE_DELETE_PTR(oldVal);
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
		bool  AMFTable::containsKey(const String key)
		{
			return get(key) != 0;
		}
		void  AMFTable::clear()
		{
			Helper::clearPtrVector(entries);
		}
		void AMFTable::write(OutputStream* output)
		{
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
		void AMFTable::read(InputStream* input)
		{
			bool objFinished = false;
			u8 octets[3];
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

		std::ostream& AMFTable::dump(std::ostream& os) const
		{
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
