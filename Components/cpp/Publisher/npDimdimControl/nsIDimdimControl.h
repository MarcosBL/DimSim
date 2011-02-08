/*
 * DO NOT EDIT.  THIS FILE IS GENERATED FROM nsIDimdimControl.idl
 */

#ifndef __gen_nsIDimdimControl_h__
#define __gen_nsIDimdimControl_h__


#ifndef __gen_nsISupports_h__
#include "nsISupports.h"
#endif

/* For IDL files that don't want to include root IDL files. */
#ifndef NS_NO_VTABLE
#define NS_NO_VTABLE
#endif

/* starting interface:    nsIDimdimControl */
#define NS_IDIMDIMCONTROL_IID_STR "0c7259e0-d93f-11dc-95ff-0800200c9a66"

#define NS_IDIMDIMCONTROL_IID \
  {0x0c7259e0, 0xd93f, 0x11dc, \
    { 0x95, 0xff, 0x08, 0x00, 0x20, 0x0c, 0x9a, 0x66 }}

class NS_NO_VTABLE nsIDimdimControl : public nsISupports {
 public: 

  NS_DEFINE_STATIC_IID_ACCESSOR(NS_IDIMDIMCONTROL_IID)

  /* long getVersion (); */
  NS_IMETHOD GetVersion(PRInt32 *_retval) = 0;

  /* long performAction (in AString args); */
  NS_IMETHOD PerformAction(const nsAString & args, PRInt32 *_retval) = 0;

  /* AString getProperty (in AString args); */
  NS_IMETHOD GetProperty(const nsAString & args, nsAString & _retval) = 0;

  /* long setProperty (in AString args); */
  NS_IMETHOD SetProperty(const nsAString & args, PRInt32 *_retval) = 0;

};

/* Use this macro when declaring classes that implement this interface. */
#define NS_DECL_NSIDIMDIMCONTROL \
  NS_IMETHOD GetVersion(PRInt32 *_retval); \
  NS_IMETHOD PerformAction(const nsAString & args, PRInt32 *_retval); \
  NS_IMETHOD GetProperty(const nsAString & args, nsAString & _retval); \
  NS_IMETHOD SetProperty(const nsAString & args, PRInt32 *_retval); 

/* Use this macro to declare functions that forward the behavior of this interface to another object. */
#define NS_FORWARD_NSIDIMDIMCONTROL(_to) \
  NS_IMETHOD GetVersion(PRInt32 *_retval) { return _to GetVersion(_retval); } \
  NS_IMETHOD PerformAction(const nsAString & args, PRInt32 *_retval) { return _to PerformAction(args, _retval); } \
  NS_IMETHOD GetProperty(const nsAString & args, nsAString & _retval) { return _to GetProperty(args, _retval); } \
  NS_IMETHOD SetProperty(const nsAString & args, PRInt32 *_retval) { return _to SetProperty(args, _retval); } 

/* Use this macro to declare functions that forward the behavior of this interface to another object in a safe way. */
#define NS_FORWARD_SAFE_NSIDIMDIMCONTROL(_to) \
  NS_IMETHOD GetVersion(PRInt32 *_retval) { return !_to ? NS_ERROR_NULL_POINTER : _to->GetVersion(_retval); } \
  NS_IMETHOD PerformAction(const nsAString & args, PRInt32 *_retval) { return !_to ? NS_ERROR_NULL_POINTER : _to->PerformAction(args, _retval); } \
  NS_IMETHOD GetProperty(const nsAString & args, nsAString & _retval) { return !_to ? NS_ERROR_NULL_POINTER : _to->GetProperty(args, _retval); } \
  NS_IMETHOD SetProperty(const nsAString & args, PRInt32 *_retval) { return !_to ? NS_ERROR_NULL_POINTER : _to->SetProperty(args, _retval); } 

#if 0
/* Use the code below as a template for the implementation class for this interface. */

/* Header file */
class nsDimdimControl : public nsIDimdimControl
{
public:
  NS_DECL_ISUPPORTS
  NS_DECL_NSIDIMDIMCONTROL

  nsDimdimControl();

private:
  ~nsDimdimControl();

protected:
  /* additional members */
};

/* Implementation file */
NS_IMPL_ISUPPORTS1(nsDimdimControl, nsIDimdimControl)

nsDimdimControl::nsDimdimControl()
{
  /* member initializers and constructor code */
}

nsDimdimControl::~nsDimdimControl()
{
  /* destructor code */
}

/* long getVersion (); */
NS_IMETHODIMP nsDimdimControl::GetVersion(PRInt32 *_retval)
{
    return NS_ERROR_NOT_IMPLEMENTED;
}

/* long performAction (in AString args); */
NS_IMETHODIMP nsDimdimControl::PerformAction(const nsAString & args, PRInt32 *_retval)
{
    return NS_ERROR_NOT_IMPLEMENTED;
}

/* AString getProperty (in AString args); */
NS_IMETHODIMP nsDimdimControl::GetProperty(const nsAString & args, nsAString & _retval)
{
    return NS_ERROR_NOT_IMPLEMENTED;
}

/* long setProperty (in AString args); */
NS_IMETHODIMP nsDimdimControl::SetProperty(const nsAString & args, PRInt32 *_retval)
{
    return NS_ERROR_NOT_IMPLEMENTED;
}

/* End of implementation class template. */
#endif


#endif /* __gen_nsIDimdimControl_h__ */
