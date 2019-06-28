package view.util;

import java.util.Map;
import java.util.Set;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.adfinternal.controller.state.PageFlowScope;


public class ScopesUtils {
    
    
    /** function return application facesContext */
    public  FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    
    ///////////////////////////////////////////////
    // PageFlowScope
    ///////////////////////////////////////////////

    public  void printPageFlowScopeVar2() {
      AdfFacesContext facesCtx= null;
      facesCtx= AdfFacesContext.getCurrentInstance();
      Map<String, Object> scopeVar= facesCtx.getPageFlowScope();
      for ( String key  : scopeVar.keySet() ) {

        // System.out.println("key: " + key);
        // System.out.println("value: " + scopeVar.get(key));

        System.out.println("[key] " + key + ", [value] " + scopeVar.get(key));
      }
    }


    public  void printPageFlowScope(){
        printPageFlowScope(getCurrntPageFlowScope());
    }


    public  void printPageFlowScope(PageFlowScope pageFlowScope){

        Set<Map.Entry<String, Object>> entrySet = pageFlowScope.entrySet();

        System.out.println();
        System.out.println("  [ PAGE FLOW SCOPE ]");

        for (Map.Entry<String, Object> entry : entrySet) {
            String key = entry.getKey();
            Object value = entry.getValue();

            System.out.println("    [key] " + key + ", [value] " + value.toString());
        }

        System.out.println("  [ PAGE FLOW SCOPE ]");
        System.out.println();
    }

    // -----------------------------------------------------

    public  PageFlowScope getCurrntPageFlowScope(){
        return (PageFlowScope)AdfFacesContext.getCurrentInstance().getPageFlowScope();
    }

    // -----------------------------------------------------

    public  void setVarToPageFlowScope(String varNameInPageFlowScope, String newValue){
        ADFContext.getCurrent().getPageFlowScope().put(varNameInPageFlowScope, newValue);
    }

    public  Object getObjectFromPageFlowScope(String varNameInPageFlowScope){
        return (Object)ADFContext.getCurrent().getPageFlowScope().get(varNameInPageFlowScope);
    }

    ///////////////////////////////////////////////
    // SessionScope
    ///////////////////////////////////////////////

    public  void setVarToSessionScope(String varNameInPageFlowScope, String newValue){
        ADFContext.getCurrent().getSessionScope().put(varNameInPageFlowScope, newValue);
    }

    public  void setObjectToSessionScope(Object varNameInPageFlowScope, String newValue){
        ADFContext.getCurrent().getSessionScope().put(varNameInPageFlowScope, newValue);
    }
	
	
	
/** function take session variable name and object then save this object inside the variable name in the session scope */
  public  void putInSession(String key, Object object)
  {
    try
    {
      FacesContext ctx = getFacesContext();
      HttpSession session = (HttpSession) ctx.getExternalContext().getSession(true);
      session.setAttribute(key, object);
    }
    catch (Exception e)
    {
      System.err.println("storeOnSession -- " + e);
    }
  }

/** function take session variable name and return the value inside this variable name from the session scope */
  public  Object getFromSession(String key)
  {
    try
    {
      FacesContext ctx = getFacesContext();
      Map sessionState = ctx.getExternalContext().getSessionMap();
      return sessionState.get(key);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return null;
  }

/** function take request variable name and object then save this object inside the variable name in the request scope */
  public  void putInRequest(String name, Object value)
  {
    getFacesContext().getExternalContext().getRequestMap().put(name, value);
  }
  
/** function take request variable name and return the value inside this variable name from the request scope */
  public  Object getFromRequest(String name)
  {
    return getFacesContext().getExternalContext().getRequestMap().get(name);
  }

    public Object getScopeParameterValue(String ParameterName, String Typ) {
        if (Typ.equalsIgnoreCase("SessionScope")) {
            return ADFContext.getCurrent()
                             .getSessionScope()
                             .get(ParameterName);
        } else {
            return ADFContext.getCurrent()
                             .getPageFlowScope()
                             .get(ParameterName);
        }
    }


    public void putScopeParameterValue(String ParameterName, Object value, String Typ) {
        if (Typ.equalsIgnoreCase("SessionScope")) {
            ADFContext.getCurrent()
                      .getSessionScope()
                      .put(ParameterName, value);
        } else {
            ADFContext.getCurrent()
                      .getPageFlowScope()
                      .put(ParameterName, value);
        }
    }
    
    
    //    private FacesContext _facesCtx = FacesContext.getCurrentInstance();
    //    private ADFContext _adfCtx = ADFContext.getCurrent();
    //    private AdfFacesContext _adfFacesCtx = AdfFacesContext.getCurrentInstance();
    //    private ExternalContext _extCtx = _facesCtx.getExternalContext();

    //
    //    // #{applicationScope}
    //
    //    FacesContext fctx = FacesContext.getCurrentInstance();
    //    fctx.getExternalContext().getApplicationMap();
    //    or
    //    ADFContext adfCtx = ADFContext.getCurrent();
    //    adfCtx.getApplicationScope();
    //
    //
    //    // #{sessionScope}
    //
    //    FacesContext fctx = null;
    //    fctx = FacesContext.getCurrentInstance();
    //    fctx.getExternalContext().getSessionMap();
    //    or
    //    ADFContext adfCtx = ADFContext.getCurrent();
    //    adfCtx.getSessionScope();
    //
    //
    //    // #{requestScope}
    //
    //    FacesContext fctx = null;
    //    fctx = FacesContext.getCurrentInstance();
    //    fctx.getExternalContext().getRequestMap();
    //    or
    //    ADFContext adfCtx = ADFContext.getCurrent();
    //    adfCtx.getRequestScope();
    //
    //
    //    // #{pageFlowScope}
    //
    //    AdfFacesContext adfFacesContext = null;
    //    adfFacesContext = AdfFacesContext.getCurrentInstance();
    //    Map _pageFlowScope = adfFacesContext.getPageFlowScope();
    //
    //
    //    // #{viewScope}
    //
    //    AdfFacesContext adfFacesContext = null;
    //    adfFacesContext = AdfFacesContext.getCurrentInstance();
    //    Map _viewScope = adfctx.getViewScope();
    //    or
    //    ADFContext adfCtx = ADFContext.getCurrent();
    //    adfCtx.getViewScope()
    //
    //
    //    // #{backingBeanScope}
    //
    //    AdfFacesContext adfFacesContext = null;
    //    adfFacesContext = AdfFacesContext.getCurrentInstance();
    //    BackingBeanScopeProviderImpl provider =
    //    adfFacesContext. getBackingBeanScopeProvider();
    //    Map backingBeanScope = null;
    //    backingBeanScope = provider.getCurrentScope();




    //     //Web App context root
    //    public String getWebAppContextRoot(){
    //        return _extCtx.getRequestContextPath();
    //    }//getWebAppContextRoot
    //
    //    //Get Application Scope
    //    public Map<String, Object> getApplicationScope(){
    //        return _adfCtx.getApplicationScope();
    //    }//getApplicationScope
    //
    //    //Get Session Scope
    //    public Map<String, Object> getSessionScope(){
    //        return _adfCtx.getSessionScope();
    //    }//getSessionScope
    //
    //    //Get PageFlowScope
    //    public Map<String,Object> getPageFlowScope(){
    //        return _adfFacesCtx.getPageFlowScope();
    //    }//getPageFlowScope
    //
    //    //Get Alternative PageFlowScope
    //    public Map<String,Object> getPageFlowScope2(){
    //        return _adfCtx.getPageFlowScope();
    //    }//getPageFlowScope2
    //
    //    //Get ViewScope
    //    public Map<String,Object> getViewScope(){
    //        return _adfFacesCtx.getViewScope();
    //    }//getViewScope
    //
    //    //Get Request Scope
    //    public Map<String, String> getRequestScope(){
    //        return _adfCtx.getRequestScope();
    //    }//getRequestScope





}// The End of Class;
