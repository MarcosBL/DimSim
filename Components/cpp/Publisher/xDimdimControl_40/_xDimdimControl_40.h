

/* this ALWAYS GENERATED file contains the definitions for the interfaces */


 /* File created by MIDL compiler version 6.00.0361 */
/* at Thu Nov 27 19:35:33 2008
 */
/* Compiler settings for _xDimdimControl_40.idl:
    Oicf, W1, Zp8, env=Win32 (32b run)
    protocol : dce , ms_ext, c_ext, robust
    error checks: allocation ref bounds_check enum stub_data 
    VC __declspec() decoration level: 
         __declspec(uuid()), __declspec(selectany), __declspec(novtable)
         DECLSPEC_UUID(), MIDL_INTERFACE()
*/
//@@MIDL_FILE_HEADING(  )

#pragma warning( disable: 4049 )  /* more than 64k source lines */


/* verify that the <rpcndr.h> version is high enough to compile this file*/
#ifndef __REQUIRED_RPCNDR_H_VERSION__
#define __REQUIRED_RPCNDR_H_VERSION__ 475
#endif

#include "rpc.h"
#include "rpcndr.h"

#ifndef __RPCNDR_H_VERSION__
#error this stub requires an updated version of <rpcndr.h>
#endif // __RPCNDR_H_VERSION__

#ifndef COM_NO_WINDOWS_H
#include "windows.h"
#include "ole2.h"
#endif /*COM_NO_WINDOWS_H*/

#ifndef ___xDimdimControl_40_h__
#define ___xDimdimControl_40_h__

#if defined(_MSC_VER) && (_MSC_VER >= 1020)
#pragma once
#endif

/* Forward Declarations */ 

#ifndef __Ixpublisher_FWD_DEFINED__
#define __Ixpublisher_FWD_DEFINED__
typedef interface Ixpublisher Ixpublisher;
#endif 	/* __Ixpublisher_FWD_DEFINED__ */


#ifndef ___IxpublisherEvents_FWD_DEFINED__
#define ___IxpublisherEvents_FWD_DEFINED__
typedef interface _IxpublisherEvents _IxpublisherEvents;
#endif 	/* ___IxpublisherEvents_FWD_DEFINED__ */


#ifndef __Cxpublisher_FWD_DEFINED__
#define __Cxpublisher_FWD_DEFINED__

#ifdef __cplusplus
typedef class Cxpublisher Cxpublisher;
#else
typedef struct Cxpublisher Cxpublisher;
#endif /* __cplusplus */

#endif 	/* __Cxpublisher_FWD_DEFINED__ */


/* header files for imported files */
#include "prsht.h"
#include "mshtml.h"
#include "mshtmhst.h"
#include "exdisp.h"
#include "objsafe.h"

#ifdef __cplusplus
extern "C"{
#endif 

void * __RPC_USER MIDL_user_allocate(size_t);
void __RPC_USER MIDL_user_free( void * ); 

#ifndef __Ixpublisher_INTERFACE_DEFINED__
#define __Ixpublisher_INTERFACE_DEFINED__

/* interface Ixpublisher */
/* [unique][helpstring][dual][uuid][object] */ 


EXTERN_C const IID IID_Ixpublisher;

#if defined(__cplusplus) && !defined(CINTERFACE)
    
    MIDL_INTERFACE("078FA70F-278A-4028-ADA4-3C2D8B733A7F")
    Ixpublisher : public IDispatch
    {
    public:
        virtual /* [helpstring][id] */ HRESULT STDMETHODCALLTYPE getProperty( 
            /* [in] */ BSTR args,
            /* [retval][out] */ BSTR *buf) = 0;
        
        virtual /* [helpstring][id] */ HRESULT STDMETHODCALLTYPE getVersion( 
            /* [retval][out] */ LONG *retval) = 0;
        
        virtual /* [helpstring][id] */ HRESULT STDMETHODCALLTYPE performAction( 
            /* [in] */ BSTR args,
            /* [retval][out] */ LONG *retval) = 0;
        
        virtual /* [helpstring][id] */ HRESULT STDMETHODCALLTYPE setProperty( 
            /* [in] */ BSTR args,
            /* [retval][out] */ LONG *retval) = 0;
        
    };
    
#else 	/* C style interface */

    typedef struct IxpublisherVtbl
    {
        BEGIN_INTERFACE
        
        HRESULT ( STDMETHODCALLTYPE *QueryInterface )( 
            Ixpublisher * This,
            /* [in] */ REFIID riid,
            /* [iid_is][out] */ void **ppvObject);
        
        ULONG ( STDMETHODCALLTYPE *AddRef )( 
            Ixpublisher * This);
        
        ULONG ( STDMETHODCALLTYPE *Release )( 
            Ixpublisher * This);
        
        HRESULT ( STDMETHODCALLTYPE *GetTypeInfoCount )( 
            Ixpublisher * This,
            /* [out] */ UINT *pctinfo);
        
        HRESULT ( STDMETHODCALLTYPE *GetTypeInfo )( 
            Ixpublisher * This,
            /* [in] */ UINT iTInfo,
            /* [in] */ LCID lcid,
            /* [out] */ ITypeInfo **ppTInfo);
        
        HRESULT ( STDMETHODCALLTYPE *GetIDsOfNames )( 
            Ixpublisher * This,
            /* [in] */ REFIID riid,
            /* [size_is][in] */ LPOLESTR *rgszNames,
            /* [in] */ UINT cNames,
            /* [in] */ LCID lcid,
            /* [size_is][out] */ DISPID *rgDispId);
        
        /* [local] */ HRESULT ( STDMETHODCALLTYPE *Invoke )( 
            Ixpublisher * This,
            /* [in] */ DISPID dispIdMember,
            /* [in] */ REFIID riid,
            /* [in] */ LCID lcid,
            /* [in] */ WORD wFlags,
            /* [out][in] */ DISPPARAMS *pDispParams,
            /* [out] */ VARIANT *pVarResult,
            /* [out] */ EXCEPINFO *pExcepInfo,
            /* [out] */ UINT *puArgErr);
        
        /* [helpstring][id] */ HRESULT ( STDMETHODCALLTYPE *getProperty )( 
            Ixpublisher * This,
            /* [in] */ BSTR args,
            /* [retval][out] */ BSTR *buf);
        
        /* [helpstring][id] */ HRESULT ( STDMETHODCALLTYPE *getVersion )( 
            Ixpublisher * This,
            /* [retval][out] */ LONG *retval);
        
        /* [helpstring][id] */ HRESULT ( STDMETHODCALLTYPE *performAction )( 
            Ixpublisher * This,
            /* [in] */ BSTR args,
            /* [retval][out] */ LONG *retval);
        
        /* [helpstring][id] */ HRESULT ( STDMETHODCALLTYPE *setProperty )( 
            Ixpublisher * This,
            /* [in] */ BSTR args,
            /* [retval][out] */ LONG *retval);
        
        END_INTERFACE
    } IxpublisherVtbl;

    interface Ixpublisher
    {
        CONST_VTBL struct IxpublisherVtbl *lpVtbl;
    };

    

#ifdef COBJMACROS


#define Ixpublisher_QueryInterface(This,riid,ppvObject)	\
    (This)->lpVtbl -> QueryInterface(This,riid,ppvObject)

#define Ixpublisher_AddRef(This)	\
    (This)->lpVtbl -> AddRef(This)

#define Ixpublisher_Release(This)	\
    (This)->lpVtbl -> Release(This)


#define Ixpublisher_GetTypeInfoCount(This,pctinfo)	\
    (This)->lpVtbl -> GetTypeInfoCount(This,pctinfo)

#define Ixpublisher_GetTypeInfo(This,iTInfo,lcid,ppTInfo)	\
    (This)->lpVtbl -> GetTypeInfo(This,iTInfo,lcid,ppTInfo)

#define Ixpublisher_GetIDsOfNames(This,riid,rgszNames,cNames,lcid,rgDispId)	\
    (This)->lpVtbl -> GetIDsOfNames(This,riid,rgszNames,cNames,lcid,rgDispId)

#define Ixpublisher_Invoke(This,dispIdMember,riid,lcid,wFlags,pDispParams,pVarResult,pExcepInfo,puArgErr)	\
    (This)->lpVtbl -> Invoke(This,dispIdMember,riid,lcid,wFlags,pDispParams,pVarResult,pExcepInfo,puArgErr)


#define Ixpublisher_getProperty(This,args,buf)	\
    (This)->lpVtbl -> getProperty(This,args,buf)

#define Ixpublisher_getVersion(This,retval)	\
    (This)->lpVtbl -> getVersion(This,retval)

#define Ixpublisher_performAction(This,args,retval)	\
    (This)->lpVtbl -> performAction(This,args,retval)

#define Ixpublisher_setProperty(This,args,retval)	\
    (This)->lpVtbl -> setProperty(This,args,retval)

#endif /* COBJMACROS */


#endif 	/* C style interface */



/* [helpstring][id] */ HRESULT STDMETHODCALLTYPE Ixpublisher_getProperty_Proxy( 
    Ixpublisher * This,
    /* [in] */ BSTR args,
    /* [retval][out] */ BSTR *buf);


void __RPC_STUB Ixpublisher_getProperty_Stub(
    IRpcStubBuffer *This,
    IRpcChannelBuffer *_pRpcChannelBuffer,
    PRPC_MESSAGE _pRpcMessage,
    DWORD *_pdwStubPhase);


/* [helpstring][id] */ HRESULT STDMETHODCALLTYPE Ixpublisher_getVersion_Proxy( 
    Ixpublisher * This,
    /* [retval][out] */ LONG *retval);


void __RPC_STUB Ixpublisher_getVersion_Stub(
    IRpcStubBuffer *This,
    IRpcChannelBuffer *_pRpcChannelBuffer,
    PRPC_MESSAGE _pRpcMessage,
    DWORD *_pdwStubPhase);


/* [helpstring][id] */ HRESULT STDMETHODCALLTYPE Ixpublisher_performAction_Proxy( 
    Ixpublisher * This,
    /* [in] */ BSTR args,
    /* [retval][out] */ LONG *retval);


void __RPC_STUB Ixpublisher_performAction_Stub(
    IRpcStubBuffer *This,
    IRpcChannelBuffer *_pRpcChannelBuffer,
    PRPC_MESSAGE _pRpcMessage,
    DWORD *_pdwStubPhase);


/* [helpstring][id] */ HRESULT STDMETHODCALLTYPE Ixpublisher_setProperty_Proxy( 
    Ixpublisher * This,
    /* [in] */ BSTR args,
    /* [retval][out] */ LONG *retval);


void __RPC_STUB Ixpublisher_setProperty_Stub(
    IRpcStubBuffer *This,
    IRpcChannelBuffer *_pRpcChannelBuffer,
    PRPC_MESSAGE _pRpcMessage,
    DWORD *_pdwStubPhase);



#endif 	/* __Ixpublisher_INTERFACE_DEFINED__ */



#ifndef __xDimdimControl_40_LIBRARY_DEFINED__
#define __xDimdimControl_40_LIBRARY_DEFINED__

/* library xDimdimControl_40 */
/* [helpstring][uuid][version] */ 


EXTERN_C const IID LIBID_xDimdimControl_40;

#ifndef ___IxpublisherEvents_DISPINTERFACE_DEFINED__
#define ___IxpublisherEvents_DISPINTERFACE_DEFINED__

/* dispinterface _IxpublisherEvents */
/* [helpstring][uuid] */ 


EXTERN_C const IID DIID__IxpublisherEvents;

#if defined(__cplusplus) && !defined(CINTERFACE)

    MIDL_INTERFACE("A1E3BC92-F828-4820-8832-4377621768F7")
    _IxpublisherEvents : public IDispatch
    {
    };
    
#else 	/* C style interface */

    typedef struct _IxpublisherEventsVtbl
    {
        BEGIN_INTERFACE
        
        HRESULT ( STDMETHODCALLTYPE *QueryInterface )( 
            _IxpublisherEvents * This,
            /* [in] */ REFIID riid,
            /* [iid_is][out] */ void **ppvObject);
        
        ULONG ( STDMETHODCALLTYPE *AddRef )( 
            _IxpublisherEvents * This);
        
        ULONG ( STDMETHODCALLTYPE *Release )( 
            _IxpublisherEvents * This);
        
        HRESULT ( STDMETHODCALLTYPE *GetTypeInfoCount )( 
            _IxpublisherEvents * This,
            /* [out] */ UINT *pctinfo);
        
        HRESULT ( STDMETHODCALLTYPE *GetTypeInfo )( 
            _IxpublisherEvents * This,
            /* [in] */ UINT iTInfo,
            /* [in] */ LCID lcid,
            /* [out] */ ITypeInfo **ppTInfo);
        
        HRESULT ( STDMETHODCALLTYPE *GetIDsOfNames )( 
            _IxpublisherEvents * This,
            /* [in] */ REFIID riid,
            /* [size_is][in] */ LPOLESTR *rgszNames,
            /* [in] */ UINT cNames,
            /* [in] */ LCID lcid,
            /* [size_is][out] */ DISPID *rgDispId);
        
        /* [local] */ HRESULT ( STDMETHODCALLTYPE *Invoke )( 
            _IxpublisherEvents * This,
            /* [in] */ DISPID dispIdMember,
            /* [in] */ REFIID riid,
            /* [in] */ LCID lcid,
            /* [in] */ WORD wFlags,
            /* [out][in] */ DISPPARAMS *pDispParams,
            /* [out] */ VARIANT *pVarResult,
            /* [out] */ EXCEPINFO *pExcepInfo,
            /* [out] */ UINT *puArgErr);
        
        END_INTERFACE
    } _IxpublisherEventsVtbl;

    interface _IxpublisherEvents
    {
        CONST_VTBL struct _IxpublisherEventsVtbl *lpVtbl;
    };

    

#ifdef COBJMACROS


#define _IxpublisherEvents_QueryInterface(This,riid,ppvObject)	\
    (This)->lpVtbl -> QueryInterface(This,riid,ppvObject)

#define _IxpublisherEvents_AddRef(This)	\
    (This)->lpVtbl -> AddRef(This)

#define _IxpublisherEvents_Release(This)	\
    (This)->lpVtbl -> Release(This)


#define _IxpublisherEvents_GetTypeInfoCount(This,pctinfo)	\
    (This)->lpVtbl -> GetTypeInfoCount(This,pctinfo)

#define _IxpublisherEvents_GetTypeInfo(This,iTInfo,lcid,ppTInfo)	\
    (This)->lpVtbl -> GetTypeInfo(This,iTInfo,lcid,ppTInfo)

#define _IxpublisherEvents_GetIDsOfNames(This,riid,rgszNames,cNames,lcid,rgDispId)	\
    (This)->lpVtbl -> GetIDsOfNames(This,riid,rgszNames,cNames,lcid,rgDispId)

#define _IxpublisherEvents_Invoke(This,dispIdMember,riid,lcid,wFlags,pDispParams,pVarResult,pExcepInfo,puArgErr)	\
    (This)->lpVtbl -> Invoke(This,dispIdMember,riid,lcid,wFlags,pDispParams,pVarResult,pExcepInfo,puArgErr)

#endif /* COBJMACROS */


#endif 	/* C style interface */


#endif 	/* ___IxpublisherEvents_DISPINTERFACE_DEFINED__ */


EXTERN_C const CLSID CLSID_Cxpublisher;

#ifdef __cplusplus

class DECLSPEC_UUID("5100F713-1B48-4A6B-9985-EDDFB7F1C0DF")
Cxpublisher;
#endif
#endif /* __xDimdimControl_40_LIBRARY_DEFINED__ */

/* Additional Prototypes for ALL interfaces */

unsigned long             __RPC_USER  BSTR_UserSize(     unsigned long *, unsigned long            , BSTR * ); 
unsigned char * __RPC_USER  BSTR_UserMarshal(  unsigned long *, unsigned char *, BSTR * ); 
unsigned char * __RPC_USER  BSTR_UserUnmarshal(unsigned long *, unsigned char *, BSTR * ); 
void                      __RPC_USER  BSTR_UserFree(     unsigned long *, BSTR * ); 

/* end of Additional Prototypes */

#ifdef __cplusplus
}
#endif

#endif


