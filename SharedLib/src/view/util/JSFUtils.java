package view.util;

import java.io.IOException;

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.servlet.http.HttpServletRequest;

/////////////////////////////////////////////

import java.util.HashMap;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import javax.ws.rs.RedirectionException;

import oracle.adf.model.binding.DCIteratorBinding;

import oracle.adf.share.ADFContext;

import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.binding.BindingContainer;

import oracle.jbo.Row;
import oracle.jbo.ViewObject;

import org.apache.myfaces.trinidad.context.RequestContext;
import org.apache.myfaces.trinidad.event.DisclosureEvent;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;

/**
 * General useful static utilies for working with JSF.
 * NOTE: Updated to use JSF 1.2 ExpressionFactory.
 *
 * @author Duncan Mills
 * @author Steve Muench
 * @author Ric Smith
 *
 * $Id: JSFUtils.java 2383 2007-09-17 16:25:37Z drmills $
 */
public class JSFUtils {


    // Informational getters

    /**
     * Get view id of the view root.
     * @return view id of the view root
     */
    public  String getRootViewId() {
        return IteratorUtils.getFacesContext().getViewRoot().getViewId();               // getViewRoot().getViewId();
    }


    
    /*
    * Internal method to pull out the correct local
    * message bundle
    */



    /**
     * Get component id of the view root.
     * @return component id of the view root
     */
    public static String getRootViewComponentId() {
        return IteratorUtils.getFacesContext().getViewRoot().getId();
    }

         
    private static ResourceBundle getBundle() {
        FacesContext ctx = IteratorUtils.getFacesContext();
        UIViewRoot uiRoot = ctx.getViewRoot();
        Locale locale = uiRoot.getLocale();
        ClassLoader ldr = Thread.currentThread().getContextClassLoader();
        return ResourceBundle.getBundle(ctx.getApplication().getMessageBundle(), locale, ldr);
    }
        
         
    private static final String NO_RESOURCE_FOUND = "Missing resource: ";

    /**
     * Method for taking a reference to a JSF binding expression and returning
     * the matching object (or creating it).
     * @param expression EL expression
     * @return Managed object
     */
    public static Object resolveExpression(String expression) {
        FacesContext facesContext = IteratorUtils.getFacesContext();
        Application app = facesContext.getApplication();
        ExpressionFactory elFactory = app.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        ValueExpression valueExp = elFactory.createValueExpression(elContext, expression, Object.class);
        return valueExp.getValue(elContext);
    }

    public static void setValueToEL(String el, Object val) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ELContext elContext = facesContext.getELContext();
        ExpressionFactory expressionFactory = facesContext.getApplication().getExpressionFactory();
        ValueExpression exp = expressionFactory.createValueExpression(elContext, el, Object.class);
        exp.setValue(elContext, val);
    }


    public static String resolveRemoteUser() {
        FacesContext facesContext = IteratorUtils.getFacesContext();
        ExternalContext ectx = facesContext.getExternalContext();
        return ectx.getRemoteUser();
    }

    public static String resolveUserPrincipal() {
        FacesContext facesContext = IteratorUtils.getFacesContext();
        ExternalContext ectx = facesContext.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) ectx.getRequest();
        return request.getUserPrincipal().getName();
    }

    public static Object resloveMethodExpression(String expression, Class returnType, Class[] argTypes,
                                                 Object[] argValues) {
        FacesContext facesContext = IteratorUtils.getFacesContext();
        Application app = facesContext.getApplication();
        ExpressionFactory elFactory = app.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        MethodExpression methodExpression =
            elFactory.createMethodExpression(elContext, expression, returnType, argTypes);
        return methodExpression.invoke(elContext, argValues);
    }

    /**
     * Method for taking a reference to a JSF binding expression and returning
     * the matching Boolean.
     * @param expression EL expression
     * @return Managed object
     */
    public static Boolean resolveExpressionAsBoolean(String expression) {
        return (Boolean) resolveExpression(expression);
    }

    /**
     * Method for taking a reference to a JSF binding expression and returning
     * the matching String (or creating it).
     * @param expression EL expression
     * @return Managed object
     */
    public static String resolveExpressionAsString(String expression) {
        return (String) resolveExpression(expression);
    }

    /**
     * Convenience method for resolving a reference to a managed bean by name
     * rather than by expression.
     * @param beanName name of managed bean
     * @return Managed object
     */
    public static Object getManagedBeanValue(String beanName) {
        StringBuffer buff = new StringBuffer("#{");
        buff.append(beanName);
        buff.append("}");
        return resolveExpression(buff.toString());
    }

    /**
     * Method for setting a new object into a JSF managed bean
     * Note: will fail silently if the supplied object does
     * not match the type of the managed bean.
     * @param expression EL expression
     * @param newValue new value to set
     */
    public static void setExpressionValue(String expression, Object newValue) {
        FacesContext facesContext = IteratorUtils.getFacesContext();
        Application app = facesContext.getApplication();
        ExpressionFactory elFactory = app.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        ValueExpression valueExp = elFactory.createValueExpression(elContext, expression, Object.class);

        //Check that the input newValue can be cast to the property type
        //expected by the managed bean.
        //If the managed Bean expects a primitive we rely on Auto-Unboxing
        //I could do a more comprehensive check and conversion from the object
        //to the equivilent primitive but life is too short
        Class bindClass = valueExp.getType(elContext);
        if (bindClass.isPrimitive() || bindClass.isInstance(newValue)) {
            valueExp.setValue(elContext, newValue);
        }
    }

    /**
     * Convenience method for setting the value of a managed bean by name
     * rather than by expression.
     * @param beanName name of managed bean
     * @param newValue new value to set
     */
    public static void setManagedBeanValue(String beanName, Object newValue) {
        StringBuffer buff = new StringBuffer("#{");
        buff.append(beanName);
        buff.append("}");
        setExpressionValue(buff.toString(), newValue);
    }


    /**
     * Convenience method for setting Session variables.
     * @param key object key
     * @param object value to store
     */
    @SuppressWarnings("unchecked")
    public static void storeOnSession(String key, Object object) {
        FacesContext ctx = IteratorUtils.getFacesContext();
        Map sessionState = ctx.getExternalContext().getSessionMap();
        sessionState.put(key, object);
    }

    /**
     * Convenience method for getting Session variables.
     * @param key object key
     * @return session object for key
     */
    public static Object getFromSession(String key) {
        FacesContext ctx = IteratorUtils.getFacesContext();
        Map sessionState = ctx.getExternalContext().getSessionMap();
        return sessionState.get(key);
    }

    public static String getFromHeader(String key) {
        FacesContext ctx = IteratorUtils.getFacesContext();
        ExternalContext ectx = ctx.getExternalContext();
        return ectx.getRequestHeaderMap().get(key);
    }

    /**
     * Convenience method for getting Request variables.
     * @param key object key
     * @return session object for key
     */
    public static Object getFromRequest(String key) {
        FacesContext ctx = IteratorUtils.getFacesContext();
        Map sessionState = ctx.getExternalContext().getRequestMap();
        return sessionState.get(key);
    }

    /**
     * Pulls a String resource from the property bundle that
     * is defined under the application &lt;message-bundle&gt; element in
     * the faces config. Respects Locale
     * @param key string message key
     * @return Resource value or placeholder error String
     */
    public static String getStringFromBundle(String key) {
        ResourceBundle bundle = getBundle();
        return getStringSafely(bundle, key, null);
    }


    /**
     * Convenience method to construct a <code>FacesMesssage</code>
     * from a defined error key and severity
     * This assumes that the error keys follow the convention of
     * using <b>_detail</b> for the detailed part of the
     * message, otherwise the main message is returned for the
     * detail as well.
     * @param key for the error message in the resource bundle
     * @param severity severity of message
     * @return Faces Message object
     */
    public static FacesMessage getMessageFromBundle(String key, FacesMessage.Severity severity) {
        ResourceBundle bundle = getBundle();
        String summary = getStringSafely(bundle, key, null);
        String detail = getStringSafely(bundle, key + "_detail", summary);
        FacesMessage message = new FacesMessage(summary, detail);
        message.setSeverity(severity);
        return message;
    }

    /**
     * Add JSF info message.
     * @param msg info message string
     */
    public static void addFacesInformationMessage(String msg) {
        FacesContext ctx = IteratorUtils.getFacesContext();
        FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, "");
        ctx.addMessage(getRootViewComponentId(), fm);
    }

    /**
     * Add JSF error message.
     * @param msg error message string
     */
    public static void addFacesErrorMessage(String msg) {
        FacesContext ctx = IteratorUtils.getFacesContext();
        FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, "");
        ctx.addMessage(getRootViewComponentId(), fm);
    }

    /**
     * Add JSF error message for a specific attribute.
     * @param attrName name of attribute
     * @param msg error message string
     */
    public static void addFacesErrorMessage(String attrName, String msg) {
        // TODO: Need a way to associate attribute specific messages
        //       with the UIComponent's Id! For now, just using the view id.
        //TODO: make this use the internal getMessageFromBundle?
        FacesContext ctx = IteratorUtils.getFacesContext();
        FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, attrName, msg);
        ctx.addMessage(getRootViewComponentId(), fm);
    }

    /**
     * Add JSF error message for a specific attribute.
     * @param attrName name of attribute
     * @param msg error message string
     */
    public static void addFacesErrorMessage(String compId, String attrName, String msg) {
        // TODO: Need a way to associate attribute specific messages
        //       with the UIComponent's Id! For now, just using the view id.
        //TODO: make this use the internal getMessageFromBundle?
        FacesContext ctx = IteratorUtils.getFacesContext();
        FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, attrName, msg);
        ctx.addMessage(compId, fm);
    }

	
//** function take a message and display it as error message (error icon will appear) */  
  public static void showErrorMessage(String message) 
  {
    FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, null, null);
    fm.setDetail(message);
    FacesContext.getCurrentInstance().addMessage(null, fm);
  }

/** function take a message and display it as information message (information icon will appear) */  
  public static void showSuccessfulMessage(String message) 
  {
    FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_INFO, null, null);
    fm.setDetail(message);
    FacesContext.getCurrentInstance().addMessage(null, fm);
  }

/** function take a message and display it as warning message (warn icon will appear) */  
  public static void showWarnMessage(String message) 
  {
    FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_WARN, null, null);
    fm.setDetail(message);
    FacesContext.getCurrentInstance().addMessage(null, fm);
	
  }

    /**
     * Get an HTTP Request attribute.
     * @param name attribute name
     * @return attribute value
     */
    public static Object getRequestAttribute(String name) {
        return IteratorUtils.getFacesContext().getExternalContext()
                                .getRequestMap()
                                .get(name);
    }

    /**
     * Set an HTTP Request attribute.
     * @param name attribute name
     * @param value attribute value
     */
    public static void setRequestAttribute(String name, Object value) {
        IteratorUtils.getFacesContext().getExternalContext()
                         .getRequestMap()
                         .put(name, value);
    }

    /*
   * Internal method to proxy for resource keys that don't exist
   */

    private static String getStringSafely(ResourceBundle bundle, String key, String defaultValue) {
        String resource = null;
        try {
            resource = bundle.getString(key);
        } catch (MissingResourceException mrex) {
            if (defaultValue != null) {
                resource = defaultValue;
            } else {
                resource = NO_RESOURCE_FOUND + key;
            }
        }
        return resource;
    }

    /**
     * Locate an UIComponent in view root with its component id. Use a recursive way to achieve this.
     * Taken from http://www.jroller.com/page/mert?entry=how_to_find_a_uicomponent
     * @param id UIComponent id
     * @return UIComponent object // by going to the af document tag of the page and chooose property initialFocusId to refer to the desired component
     */
    public static UIComponent findComponentInRoot(String id) {
        UIComponent component = null;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext != null) {
            UIComponent root = facesContext.getViewRoot();
            component = findComponent(root, id);
        }
        return component;
    }

    /**
     * Locate an UIComponent from its root component.
     * Taken from http://www.jroller.com/page/mert?entry=how_to_find_a_uicomponent
     * @param base root Component (parent)
     * @param id UIComponent id
     * @return UIComponent object
     */
    public static UIComponent findComponent(UIComponent base, String id) {
        if (id.equals(base.getId()))
            return base;

        UIComponent children = null;
        UIComponent result = null;
        Iterator childrens = base.getFacetsAndChildren();
        while (childrens.hasNext() && (result == null)) {
            children = (UIComponent) childrens.next();
            if (id.equals(children.getId())) {
                result = children;
                break;
            }
            result = findComponent(children, id);
            if (result != null) {
                break;
            }
        }
        return result;
    }

    private static void writeJavaScriptToClient(String script) {
        FacesContext fctx = FacesContext.getCurrentInstance();
        ExtendedRenderKitService erks = Service.getRenderKitService(fctx, ExtendedRenderKitService.class);
        erks.addScript(fctx, script);
    }


	/** function take URL and open it in new window */
  public static void openUrlInNewWindow(String url)
  {
    StringBuilder strb = new StringBuilder("window.open('" + url + "');");
    writeJavaScriptToClient(strb.toString());
  }

	
    /**
     * Method to create a redirect URL. The assumption is that the JSF servlet mapping is
     * "faces", which is the default
     *
     * @param view the JSP or JSPX page to redirect to
     * @return a URL to redirect to
     */
    public static String getPageURL(String view) {
        FacesContext facesContext = IteratorUtils.getFacesContext();
        ExternalContext externalContext = facesContext.getExternalContext();
        String url = ((HttpServletRequest) externalContext.getRequest()).getRequestURL().toString();
        StringBuffer newUrlBuffer = new StringBuffer();
        newUrlBuffer.append(url.substring(0, url.lastIndexOf("faces/")));
        newUrlBuffer.append("faces");
        String targetPageUrl = view.startsWith("/") ? view : "/" + view;
        newUrlBuffer.append(targetPageUrl);
        return newUrlBuffer.toString();
    }
    
    /**
    * @param URL
    * This would redirect to the passed Url
    */
    public static void redirect(String URL) {
    //logger.info(“Now_redirecting_it_to :”+URL);
    FacesContext context = FacesContext.getCurrentInstance();
    ExternalContext externalContext = context.getExternalContext();
    HttpServletRequest req = (HttpServletRequest)context.getExternalContext().getRequest();
    String protocol = req.isSecure() ? "https" : "http";
    String redirectURL =
    protocol + "://" + req.getServerName() + ":" + req.getServerPort() + "" + req.getContextPath() + URL;
    try {
    externalContext.redirect(redirectURL);
    } catch (IOException e) {
    // log URL that failed
    // throw it back as RuntimeException
   // logger.severe(“RedirectionException failed ” + e.getMessage());
    throw new RuntimeException(e);
    }
    }

    /**
     * Create value binding for EL exression
     * @param expression
     * @return Object
     */
    public static Object getExpressionObjectReference(String expression) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ELContext elctx = fc.getELContext();
        ExpressionFactory elFactory = fc.getApplication().getExpressionFactory();
        return elFactory.createValueExpression(elctx, expression, Object.class).getValue(elctx);
    }

    /**
     * Recipe: Using a custom af:table selection listener.
     *
     * Invokes a method expression.
     *
     * @param expr
     * @param returnType
     * @param argType
     * @param argument
     * @return
     */
    public static Object invokeMethodExpression(String expr, Class returnType, Class argType, Object argument) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ELContext elctx = fc.getELContext();
        ExpressionFactory elFactory = fc.getApplication().getExpressionFactory();
        MethodExpression methodExpr =
            elFactory.createMethodExpression(elctx, expr, returnType, new Class[] { argType });
        return methodExpr.invoke(elctx, new Object[] { argument });
    }


    private static Map<String, SelectItem> selectItems = new HashMap<String, SelectItem>();


    public static void addFacesInfoMessage(String msg) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, "");
        ctx.addMessage(getRootViewComponentId(), fm);
    }

    public static SelectItem getSelectItem(String value) {
        SelectItem item = selectItems.get(value);
        if (item == null) {
            item = createNewSelectItem(value, value);
            selectItems.put(value, item);
        }
        return item;
    }

    public static SelectItem createNewSelectItem(String label, String value) {
        return new SelectItem(value, label);
    }

    public static HttpServletResponse getResponse() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) ctx.getExternalContext().getResponse();
        return response;

    }

    public static BindingContainer getBindingContainer() {
        return (BindingContainer) JSFUtils.resolveExpression("#{bindings}");
    }

    public static boolean hasRecords(String iteratorName) {
        Row[] rows = getAllRows(iteratorName);
        if ((rows == null) || (rows.length == 0)) {
            return false;
        }
        return true;
    }


    public static void setIteratorPosition(String iteratorName, String whereClause) throws Exception {
        ViewObject viewObject = getViewObject(iteratorName);
        if (viewObject.getWhereClause() != null) {
            viewObject.setWhereClauseParams(null);
            viewObject.executeQuery();

        }
        viewObject.setWhereClause(whereClause);
        //        viewObject.defineNamedWhereClauseParam(param, null, null);
        //        viewObject.setNamedWhereClauseParam(param, value);
        viewObject.executeQuery();
    }


    public static ViewObject getViewObject(String iteratorName) {
        ViewObject viewObject = null;
        BindingContainer bindings = JSFUtils.getBindingContainer();
        if (bindings != null) {
            DCIteratorBinding iter = (DCIteratorBinding) bindings.get(iteratorName);
            viewObject = iter.getViewObject();
        }
        return viewObject;
    }

    public static Row[] getAllRows(String iteratorName) {
        ViewObject vObject = getViewObject(iteratorName);
        vObject.executeQuery();
        Row[] rows = vObject.getAllRowsInRange();
        return rows;
    }

    public static ViewObject executeViewObject(String iteratorName) {
        ViewObject vObject = getViewObject(iteratorName);
        vObject.executeQuery();
        System.out.println("....Total rows..." + vObject.getRowCount());
        return vObject;
    }


    public static Row getCurrentRow(String iteratorName) {
        BindingContainer bindings = getBindingContainer();
        Row currentRow = null;
        if (bindings != null) {

            DCIteratorBinding iter = (DCIteratorBinding) bindings.get(iteratorName);
            ViewObject vObject = iter.getViewObject();
            currentRow = vObject.getCurrentRow();
        }
        return currentRow;
    }

    public static void executeIterator(String iteratorName) {
        BindingContainer bindings = getBindingContainer();
        if (bindings != null) {
            DCIteratorBinding iter = (DCIteratorBinding) bindings.get(iteratorName);
            ViewObject vObject = iter.getViewObject();
            vObject.executeQuery();
        }
    }

    public static Object getCurrentRowAttribute(String iteratorName, String attributeName) {
        Row row = getCurrentRow(iteratorName);
        return row.getAttribute(attributeName);
    }

    public static Object getFromRequestParameterMap(String key) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        return ctx.getExternalContext()
                  .getRequestParameterMap()
                  .get(key);

    }

    public static void postMessageToFacesContext(FacesMessage.Severity severity, String summary, String detail) {
        postMessage(null, severity, summary, detail);
    }

    public static void postMessage(String componentId, FacesMessage.Severity severity, String summary, String detail) {
        IteratorUtils.getFacesContext().addMessage(componentId, new FacesMessage(severity, summary, detail));
    }


    public static String getURLFromServletContext(String paramName) {

        ServletContext sc = (ServletContext) FacesContext.getCurrentInstance()
                                                         .getExternalContext()
                                                         .getContext();
        String urlLink = sc.getInitParameter(paramName);
        if (urlLink != null)
            return urlLink;
        else
            return "";
    }


    public static MethodExpression getSDMethodExpression(String name) {
        Class[] argtypes = new Class[1];
        argtypes[0] = DisclosureEvent.class;
        FacesContext facesCtx = FacesContext.getCurrentInstance();
        Application app = facesCtx.getApplication();
        ExpressionFactory elFactory = app.getExpressionFactory();
        ELContext elContext = facesCtx.getELContext();
        return elFactory.createMethodExpression(elContext, name, null, argtypes);
    }

    // return a methodexpression like a control flow case action or ADF pagedef action

    public static MethodExpression getMethodExpression(String name) {
        Class[] argtypes = new Class[1];
        argtypes[0] = ActionEvent.class;
        FacesContext facesCtx = FacesContext.getCurrentInstance();
        Application app = facesCtx.getApplication();
        ExpressionFactory elFactory = app.getExpressionFactory();
        ELContext elContext = facesCtx.getELContext();
        return elFactory.createMethodExpression(elContext, name, null, argtypes);
    }

    /**
    * @return the current Locale language and Country code
    */
    public static String getLanguageAndCountryCode() {
    ADFContext adfctx = ADFContext.getCurrent();
    String langCountryCode = adfctx.getLocale().toString();
    return langCountryCode != null ? langCountryCode.replace('_', '-') : langCountryCode;
    }

    /**
    * Store application scope attribute
    * @param name
    * @param value
    */
    public static void storeOnApplication(String name, Object value) {
    FacesContext ctx = IteratorUtils.getFacesContext();
    Map applicationState = ctx.getExternalContext().getApplicationMap();
    applicationState.put(name, value);

    //ADFContext.getCurrent().getApplicationScope().put(name, value);
    }

    /**
    * Store page flow scope attribute
    * @param name
    * @param value
    */
    public static void storeOnPageFlow(String name, Object value) {
    RequestContext.getCurrentInstance().getPageFlowScope().put(name, value);

    //ADFContext.getCurrent().getPageFlowScope().put(name, value);
    }

    /**
    * Store view scope attribute
    * @param name
    * @param value
    */
    public static void storeOnView(String name, Object value) {
    RequestContext.getCurrentInstance().getViewMap().put(name, value);

    //ADFContext.getCurrent().getViewScope().put(name, value);
    }

    /**
    * Convenience method for setting request variables.
    * @param key object key
    * @param object value to store
    */
    public static void storeOnRequest(String key, Object object) {
    FacesContext ctx = IteratorUtils.getFacesContext();
    Map sessionState = ctx.getExternalContext().getRequestMap();
    sessionState.put(key, object);

    //ADFContext.getCurrent().getRequestScope().put(name, value);
    }

    /**
     * Refresh an UIComponent.
     * “faces”, which is the default
     *
     * @param component Component to be refreshed
     */
    public static void refreshComponent(UIComponent component) {
        if (component != null) {
            AdfFacesContext.getCurrentInstance().addPartialTarget(component);
        }
    }
    

    /*
    to add java script programatically
    */
    public void addJavascript(String jsStr) {
    if (null == jsStr || "".equals(jsStr.trim()))
    return;
    FacesContext fctx = FacesContext.getCurrentInstance();
    ExtendedRenderKitService erks = null;
    erks = org.apache.myfaces.trinidad.util.Service.getRenderKitService(fctx, ExtendedRenderKitService.class);
    erks.addScript(fctx, jsStr);
    }

    /**
    *Get Client Ip Address
    * @return
    */
    public static String getClientIpAddress() {
    HttpServletRequest request =
    (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();

    //Get the client ip address
    String ipAddress = request.getHeader("X-FORWARDED-FOR");
    if (ipAddress == null) {
    ipAddress = request.getRemoteAddr();
    }
    return ipAddress;
    }

    /**
    * This is generic method to add any new cookie
    * @param response
    * @param cookieName
    * @param cookieValue
    */
    public static void setCookieValue(HttpServletResponse response, String cookieName, String cookieValue) {
    if (cookieValue != null && !cookieValue.contains(";HttpOnly; secure")) {
    cookieValue += ";HttpOnly; secure";
    }
    Cookie newCookie = new Cookie(cookieName, cookieValue);
    newCookie.setSecure(true);
    newCookie.setPath("/");
    newCookie.setMaxAge(365 * 24 * 60 * 60);
    response.addCookie(newCookie);
    }

    /**
    *Code to retrieve value from cookie
    * @param cookieName
    * @return
    */
    public static String retrieveFromCookie(String cookieName) {
    FacesContext vFacesContext = FacesContext.getCurrentInstance();
    ExternalContext vExternalContext = vFacesContext.getExternalContext();
    Map vRequestCookieMap = vExternalContext.getRequestCookieMap();
    Cookie vMyCookie = (Cookie)vRequestCookieMap.get(cookieName);
    String cookieVal = null;
    if (vMyCookie != null) {
    cookieVal = vMyCookie.getValue();
    }
    return cookieVal;
    }

}
