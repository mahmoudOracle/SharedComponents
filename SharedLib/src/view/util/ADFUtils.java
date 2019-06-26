
package view.util;

import java.io.File;

import com.sun.faces.mgbean.ManagedBeanCreationException;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import java.sql.PreparedStatement;

import java.math.BigDecimal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.model.binding.DCParameter;
import oracle.adf.share.logging.ADFLogger;

import oracle.binding.AttributeBinding;
import oracle.binding.BindingContainer;
import oracle.binding.ControlBinding;
import oracle.binding.OperationBinding;

import oracle.jbo.ApplicationModule;
import oracle.jbo.Key;
import oracle.jbo.Row;
import oracle.jbo.uicli.binding.JUCtrlValueBinding;

import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;
//////////////////////////////////////////////////////////////
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import java.util.Locale;
import java.util.Properties;

import java.util.ResourceBundle;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;

import javax.faces.component.UIViewRoot;

import oracle.adf.controller.ControllerContext;
import oracle.adf.controller.TaskFlowId;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.layout.RichPanelTabbed;
import oracle.adf.view.rich.component.rich.layout.RichShowDetailItem;
import oracle.adf.view.rich.context.AdfFacesContext;

import javax.faces.context.FacesContext;

import javax.faces.event.PhaseId;

import javax.faces.event.ValueChangeEvent;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.adf.model.DataControlFrame;
import oracle.adf.model.binding.DCDataControl;
import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.RichQuery;
import oracle.adf.view.rich.component.rich.fragment.RichRegion;
import oracle.adf.view.rich.component.rich.input.RichInputListOfValues;
import oracle.adf.view.rich.component.rich.input.RichSelectBooleanCheckbox;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.layout.RichPanelSplitter;

import oracle.adf.view.rich.component.rich.nav.RichCommandButton;

import oracle.adf.view.rich.component.rich.nav.RichCommandToolbarButton;

import oracle.adf.view.rich.model.FilterableQueryDescriptor;

import oracle.adf.view.rich.model.QueryDescriptor;
import oracle.adf.view.rich.model.QueryModel;

import oracle.javatools.resourcebundle.BundleFactory;

import oracle.jbo.ViewCriteria;
import oracle.jbo.ViewObject;
import oracle.jbo.server.DBTransaction;

import oracle.jbo.server.SequenceImpl;

import oracle.jbo.server.ViewRowImpl;
import oracle.jbo.uicli.binding.JUCtrlListBinding;

import org.apache.myfaces.trinidad.context.RequestContext;
import org.apache.myfaces.trinidad.model.UploadedFile;

/**
 * Provides various utility methods that are handy to
 * have around when working with ADF.
 */

/**
 * A series of convenience functions for dealing with ADF Bindings.
 * Note: Updated for JDeveloper 11
 *
 * @author Duncan Mills
 * @author Steve Muench
 *
 * $Id: ADFUtils.java 2513 2007-09-20 20:39:13Z ralsmith $.
 */
public class ADFUtils {


    private  ServletContext getContext() {
        return (ServletContext) IteratorUtils.getFacesContext().getExternalContext().getContext();
    }


    public static Object invokeMethodExpression(String expr, Class returnType, Class[] argTypes, Object[] args) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ELContext elctx = fc.getELContext();
        ExpressionFactory elFactory = fc.getApplication().getExpressionFactory();
        MethodExpression methodExpr = elFactory.createMethodExpression(elctx, expr, returnType, argTypes);
        return methodExpr.invoke(elctx, args);
    }

    public static Object invokeMethodExpression(String expr, Class returnType, Class argType, Object argument) {
        return invokeMethodExpression(expr, returnType, new Class[] { argType }, new Object[] { argument });
    }


    public static final ADFLogger LOGGER = ADFLogger.createADFLogger(ADFUtils.class);


    /**
     * When a bounded task flow manages a transaction (marked as
     * requires-transaction, requires-new-transaction, or requires-
     * existing-transaction), then the task flow must issue any
     * commits or rollbacks that are needed.
     * This is essentially to keep the state of the transaction that
     * the task flow understands in synch with the state of the
     * transaction in the ADFbc layer.
     * Use this method to issue a commit in the middle of a task flow
     * while staying in the task flow.
     */
    public static void saveAndContinue() {
        Map sessionMap = FacesContext.getCurrentInstance()
                                     .getExternalContext()
                                     .getSessionMap();
        BindingContext context = (BindingContext) sessionMap.get(BindingContext.CONTEXT_ID);
        String currentFrameName = context.getCurrentDataControlFrame();
        DataControlFrame dcFrame = context.findDataControlFrame(currentFrameName);
        dcFrame.commit();
        dcFrame.beginTransaction(null);
    }


/** function used to rollback any changes to the last commit point without changing current row of the passed iterator*/
  
  public static void rollbackAndBeInTheCurrentRow(String iteratorName)
  {
    try
    {
      DCIteratorBinding locationsIter = IteratorUtils.findIterator(iteratorName);
      Row lRow = locationsIter.getCurrentRow();
      Key key = null;
      if (lRow != null)
      {
        key = lRow.getKey();
      }
      IteratorUtils.executeOperationBinding("Rollback");
      if (key != null)
      {
        locationsIter.setCurrentRowWithKey(key.toStringFormat(true));
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  



    /**
     * Get application module for an application module data control by name.
     * @param name application module data control name
     * @return ApplicationModule
     */
    public static ApplicationModule getApplicationModuleForDataControl(String name) {
        return (ApplicationModule) JSFUtils.resolveExpression("#{data." + name + ".dataProvider}");
    }

    /**
     * A convenience method for getting the value of a bound attribute in the
     * current page context programatically.
     * @param attributeName of the bound value in the pageDef
     * @return value of the attribute
     */
    public static Object getBoundAttributeValue(String attributeName) {
        return findControlBinding(attributeName).getInputValue();
    }
	
	/** function take iterator name and attribute name then return the value of the attribute from the current row of the iterator */
  public Object getAttributeFromIterator(String iteratorName, String attributeName)
  {
    if (IteratorUtils.findIterator(iteratorName).getCurrentRow() != null)
    {
      return IteratorUtils.findIterator(iteratorName).getCurrentRow().getAttribute(attributeName);
    }
    return null;
  }

    /**
     * A convenience method for setting the value of a bound attribute in the
     * context of the current page.
     * @param attributeName of the bound value in the pageDef
     * @param value to set
     */
    public static void setBoundAttributeValue(String attributeName, Object value) {
        findControlBinding(attributeName).setInputValue(value);
    }

	/** function take iterator name and attribute name and value then set this value to the attribute in the current row of the iterator */
  public void setAttributeInIterator(String iteratorName, String attributeName, Object value)
  {
    if (IteratorUtils.findIterator(iteratorName).getCurrentRow() != null)
    {
      IteratorUtils.findIterator(iteratorName).getCurrentRow().setAttribute(attributeName, value);
    }
  }
	
    /**
     * Returns the evaluated value of a pageDef parameter.
     * @param pageDefName reference to the page definition file of the page with the parameter
     * @param parameterName name of the pagedef parameter
     * @return evaluated value of the parameter as a String
     */
    public static Object getPageDefParameterValue(String pageDefName, String parameterName) {
        BindingContainer bindings = findBindingContainer(pageDefName);
        DCParameter param = ((DCBindingContainer) bindings).findParameter(parameterName);
        return param.getValue();
    }

    /**
     * Convenience method to find a DCControlBinding as an AttributeBinding
     * to get able to then call getInputValue() or setInputValue() on it.
     * @param bindingContainer binding container
     * @param attributeName name of the attribute binding.
     * @return the control value binding with the name passed in.
     *
     */
    public static AttributeBinding findControlBinding(BindingContainer bindingContainer, String attributeName) {
        if (attributeName != null) {
            if (bindingContainer != null) {
                ControlBinding ctrlBinding = bindingContainer.getControlBinding(attributeName);
                if (ctrlBinding instanceof AttributeBinding) {
                    return (AttributeBinding) ctrlBinding;
                }
            }
        }
        return null;
    }

    /**
     * Convenience method to find a DCControlBinding as a JUCtrlValueBinding
     * to get able to then call getInputValue() or setInputValue() on it.
     * @param attributeName name of the attribute binding.
     * @return the control value binding with the name passed in.
     *
     */
    public static AttributeBinding findControlBinding(String attributeName) {
        return findControlBinding(getBindingContainer(), attributeName);
    }

    /**
     * Return the current page's binding container.
     * @return the current page's binding container
     */
    public static BindingContainer getBindingContainer() {
        return (BindingContainer) JSFUtils.resolveExpression("#{bindings}");
    }

    /**
     * Return the Binding Container as a DCBindingContainer.
     * @return current binding container as a DCBindingContainer
     */
    public static DCBindingContainer getDCBindingContainer() {
        return (DCBindingContainer) getBindingContainer();
    }

    /**
     * Get List of ADF Faces SelectItem for an iterator binding.
     *
     * Uses the value of the 'valueAttrName' attribute as the key for
     * the SelectItem key.
     *
     * @param iteratorName ADF iterator binding name
     * @param valueAttrName name of the value attribute to use
     * @param displayAttrName name of the attribute from iterator rows to display
     * @return ADF Faces SelectItem for an iterator binding
     */
    public static List<SelectItem> selectItemsForIterator(String iteratorName, String valueAttrName,
                                                          String displayAttrName) {
        return selectItemsForIterator(IteratorUtils.findIterator(iteratorName), valueAttrName, displayAttrName);
    }

    /**
     * Get List of ADF Faces SelectItem for an iterator binding with description.
     *
     * Uses the value of the 'valueAttrName' attribute as the key for
     * the SelectItem key.
     *
     * @param iteratorName ADF iterator binding name
     * @param valueAttrName name of the value attribute to use
     * @param displayAttrName name of the attribute from iterator rows to display
     * @param descriptionAttrName name of the attribute to use for description
     * @return ADF Faces SelectItem for an iterator binding with description
     */
    public static List<SelectItem> selectItemsForIterator(String iteratorName, String valueAttrName,
                                                          String displayAttrName, String descriptionAttrName) {
        return selectItemsForIterator(IteratorUtils.findIterator(iteratorName), valueAttrName, displayAttrName, descriptionAttrName);
    }

    /**
     * Get List of ADF Faces SelectItem for an iterator binding.
     *
     * Uses the value of the 'valueAttrName' attribute as the key for
     * the SelectItem key.
     *
     * @param iter ADF iterator binding
     * @param valueAttrName name of value attribute to use for key
     * @param displayAttrNames array of attribute names from iterator rows to display
     * @return ADF Faces SelectItem for an iterator binding
     */
    public static List<SelectItem> selectItemsForIterator(DCIteratorBinding iter, String valueAttrName,
                                                          String[] displayAttrNames) {
        List<SelectItem> selectItems = new ArrayList<SelectItem>();
        for (Row r : iter.getAllRowsInRange()) {
            StringBuilder buf = new StringBuilder();
            for (int idx = 0; idx < displayAttrNames.length; idx++) {
                buf.append((String) r.getAttribute(displayAttrNames[idx]));
                if (idx < (displayAttrNames.length - 1)) {
                    buf.append("-");
                }
            }
            selectItems.add(new SelectItem(r.getAttribute(valueAttrName), buf.toString()));
        }
        return selectItems;
    }

    /**
     * Get List of ADF Faces SelectItem for an iterator binding.
     *
     * Uses the value of the 'valueAttrName' attribute as the key for
     * the SelectItem key.
     *
     * @param iteratorName ADF iterator binding name
     * @param valueAttrName name of the value attribute to use
     * @param displayAttrNames array of attribute names from iterator rows to display
     * @return ADF Faces SelectItem for an iterator binding
     */
    public static List<SelectItem> selectItemsForIterator(String iteratorName, String valueAttrName,
                                                          String[] displayAttrNames) {
        return selectItemsForIterator(IteratorUtils.findIterator(iteratorName), valueAttrName, displayAttrNames);
    }


    /**
     * Get List of ADF Faces SelectItem for an iterator binding with description.
     *
     * Uses the value of the 'valueAttrName' attribute as the key for
     * the SelectItem key.
     *
     * @param iter ADF iterator binding
     * @param valueAttrName name of value attribute to use for key
     * @param displayAttrName name of the attribute from iterator rows to display
     * @param descriptionAttrName name of the attribute for description
     * @return ADF Faces SelectItem for an iterator binding with description
     */
    public static List<SelectItem> selectItemsForIterator(DCIteratorBinding iter, String valueAttrName,
                                                          String displayAttrName, String descriptionAttrName) {
        List<SelectItem> selectItems = new ArrayList<SelectItem>();
        for (Row r : iter.getAllRowsInRange()) {
            selectItems.add(new SelectItem(r.getAttribute(valueAttrName), (String) r.getAttribute(displayAttrName),
                                           (String) r.getAttribute(descriptionAttrName)));
        }
        return selectItems;
    }

    /**
     * Get List of ADF Faces SelectItem for an iterator binding.
     *
     * Uses the value of the 'valueAttrName' attribute as the key for
     * the SelectItem key.
     *
     * @param iter ADF iterator binding
     * @param valueAttrName name of value attribute to use for key
     * @param displayAttrName name of the attribute from iterator rows to display
     * @return ADF Faces SelectItem for an iterator binding
     */
    public static List<SelectItem> selectItemsForIterator(DCIteratorBinding iter, String valueAttrName,
                                                          String displayAttrName) {
        List<SelectItem> selectItems = new ArrayList<SelectItem>();
        for (Row r : iter.getAllRowsInRange()) {
            selectItems.add(new SelectItem(r.getAttribute(valueAttrName), (String) r.getAttribute(displayAttrName)));
        }
        return selectItems;
    }

    /**
     * Get List of attribute values for an iterator.
     * @param iteratorName ADF iterator binding name
     * @param valueAttrName value attribute to use
     * @return List of attribute values for an iterator
     */
    public static List attributeListForIterator(String iteratorName, String valueAttrName) {
        return attributeListForIterator(IteratorUtils.findIterator(iteratorName), valueAttrName);
    }

    /**
     * Get a List of attribute values for an iterator.
     *
     * @param iter iterator binding
     * @param valueAttrName name of value attribute to use
     * @return List of attribute values
     */
    public static List attributeListForIterator(DCIteratorBinding iter, String valueAttrName) {
        List attributeList = new ArrayList();
        for (Row r : iter.getAllRowsInRange()) {
            attributeList.add(r.getAttribute(valueAttrName));
        }
        return attributeList;
    }

    /**
     * Get a List of attributes as a Map (of name, value) for an iterator.
     * @param iteratorName ADF iterator binding name
     * @param attrNames array of attribute names for attributes to be retrieved
     * @return List of attribute values for an iterator
     */
    public static List<Map<String, Object>> attributeListForIterator(String iteratorName, String[] attrNames) {
        return attributesListForIterator(IteratorUtils.findIterator(iteratorName), attrNames);
    }


    /**
     * Get List of Key objects for rows in an iterator.
     * @param iteratorName iterabot binding name
     * @return List of Key objects for rows
     */
    public static List<Key> keyListForIterator(String iteratorName) {
        return keyListForIterator(IteratorUtils.findIterator(iteratorName));
    }

    /**
     * Get List of Key objects for rows in an iterator.
     * @param iter iterator binding
     * @return List of Key objects for rows
     */
    public static List<Key> keyListForIterator(DCIteratorBinding iter) {
        List<Key> attributeList = new ArrayList<Key>();
        for (Row r : iter.getAllRowsInRange()) {
            attributeList.add(r.getKey());
        }
        return attributeList;
    }

    /**
     * Get List of Key objects for rows in an iterator using key attribute.
     * @param iteratorName iterator binding name
     * @param keyAttrName name of key attribute to use
     * @return List of Key objects for rows
     */
    public static List<Key> keyAttrListForIterator(String iteratorName, String keyAttrName) {
        return keyAttrListForIterator(IteratorUtils.findIterator(iteratorName), keyAttrName);
    }

    /**
     * Get List of Key objects for rows in an iterator using key attribute.
     *
     * @param iter iterator binding
     * @param keyAttrName name of key attribute to use
     * @return List of Key objects for rows
     */
    public static List<Key> keyAttrListForIterator(DCIteratorBinding iter, String keyAttrName) {
        List<Key> attributeList = new ArrayList<Key>();
        for (Row r : iter.getAllRowsInRange()) {
            attributeList.add(new Key(new Object[] { r.getAttribute(keyAttrName) }));
        }
        return attributeList;
    }

    /**
     * @param name
     * @return
     */
    public static JUCtrlValueBinding findCtrlBinding(String name) {
        JUCtrlValueBinding rowBinding = (JUCtrlValueBinding) getDCBindingContainer().findCtrlBinding(name);
        if (rowBinding == null) {
            throw new RuntimeException("CtrlBinding " + name + "' not found");
        }
        return rowBinding;
    }

   
	
    /**
     * Get List of ADF Faces SelectItem for an iterator binding.
     *
     * Uses the rowKey of each row as the SelectItem key.
     *
     * @param iteratorName ADF iterator binding name
     * @param displayAttrName name of the attribute from iterator rows to display
     * @return ADF Faces SelectItem for an iterator binding
     */
    public static List<SelectItem> selectItemsByKeyForIterator(String iteratorName, String displayAttrName) {
        return selectItemsByKeyForIterator(IteratorUtils.findIterator(iteratorName), displayAttrName);
    }

    /**
     * Get List of ADF Faces SelectItem for an iterator binding with discription.
     *
     * Uses the rowKey of each row as the SelectItem key.
     *
     * @param iteratorName ADF iterator binding name
     * @param displayAttrName name of the attribute from iterator rows to display
     * @param descriptionAttrName name of the attribute for description
     * @return ADF Faces SelectItem for an iterator binding with discription
     */
    public static List<SelectItem> selectItemsByKeyForIterator(String iteratorName, String displayAttrName,
                                                               String descriptionAttrName) {
        return selectItemsByKeyForIterator(IteratorUtils.findIterator(iteratorName), displayAttrName, descriptionAttrName);
    }

    /**
     * Get List of ADF Faces SelectItem for an iterator binding with discription.
     *
     * Uses the rowKey of each row as the SelectItem key.
     *
     * @param iter ADF iterator binding
     * @param displayAttrName name of the attribute from iterator rows to display
     * @param descriptionAttrName name of the attribute for description
     * @return ADF Faces SelectItem for an iterator binding with discription
     */
    public static List<SelectItem> selectItemsByKeyForIterator(DCIteratorBinding iter, String displayAttrName,
                                                               String descriptionAttrName) {
        List<SelectItem> selectItems = new ArrayList<SelectItem>();
        for (Row r : iter.getAllRowsInRange()) {
            selectItems.add(new SelectItem(r.getKey(), (String) r.getAttribute(displayAttrName),
                                           (String) r.getAttribute(descriptionAttrName)));
        }
        return selectItems;
    }

    /**
     * Get List of ADF Faces SelectItem for an iterator binding.
     *
     * Uses the rowKey of each row as the SelectItem key.
     *
     * @param iter ADF iterator binding
     * @param displayAttrName name of the attribute from iterator rows to display
     * @return List of ADF Faces SelectItem for an iterator binding
     */
    public static List<SelectItem> selectItemsByKeyForIterator(DCIteratorBinding iter, String displayAttrName) {
        List<SelectItem> selectItems = new ArrayList<SelectItem>();
        for (Row r : iter.getAllRowsInRange()) {
            selectItems.add(new SelectItem(r.getKey(), (String) r.getAttribute(displayAttrName)));
        }
        return selectItems;
    }

    /**
     * Find the BindingContainer for a page definition by name.
     *
     * Typically used to refer eagerly to page definition parameters. It is
     * not best practice to reference or set bindings in binding containers
     * that are not the one for the current page.
     *
     * @param pageDefName name of the page defintion XML file to use
     * @return BindingContainer ref for the named definition
     */
    private static BindingContainer findBindingContainer(String pageDefName) {
        BindingContext bctx = getDCBindingContainer().getBindingContext();
        BindingContainer foundContainer = bctx.findBindingContainer(pageDefName);
        return foundContainer;
    }


    /**
   * Additional utilities added to support Packt 'Oracle JDeveloper 11g Cookboo' book.
   *
   * @author Nick Haralabidis
   *
   */

    /**
     * Determines whether there are changes done to the current record.
     *
     * @return
     */
    public static boolean hasChanges() {
        // check for dirty transaction in both the model and the controller.
        return isBCTransactionDirty() || isControllerTransactionDirty();
    }

    /**
     * Executes a bound operation.
     *
     * @param operation, the bound operation id
     * @param parameters, the bound operation parameters
     * @return
     */
    public static boolean execOperation(String operation, String... parameters) {
        // for now return true; see recipe ? to more info
        return true;
    }

	/** function take popup id and will show this popup */
  public static void showPopup(String popupName)
  {
    StringBuilder strb = new StringBuilder("AdfPage.PAGE.findComponentByAbsoluteId(\"" + popupName + "\").show();");
    writeJavaScriptToClient(strb.toString());
  }

/** function take popup id and component id and will show this popup behind with this component */
  public static void showPopup(String popupName, String alignId)
  {
    StringBuilder strb = new StringBuilder("var pop = AdfPage.PAGE.findComponentByAbsoluteId(" + popupName + ");");
    strb.append("var hints = {};\n");
    strb.append("hints[AdfRichPopup.HINT_ALIGN_ID] = '" + alignId + "';");
    strb.append("pop.show(hints);");
    writeJavaScriptToClient(strb.toString());
  }
	
    /**
     * Displays a popup.
     *
     * @param popupId
     */
    public static void showPopup12(@SuppressWarnings("oracle.jdeveloper.java.unused-parameter") String popupId) {
    }

    public static void showPopup1(String clientId) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        StringBuilder javaScriptPopup = new StringBuilder("var popObj=AdfPage.PAGE.findComponent('" + clientId + "');");
        javaScriptPopup.append("popObj.show();");

        ExtendedRenderKitService erks = Service.getRenderKitService(ctx, ExtendedRenderKitService.class);
        erks.addScript(ctx, javaScriptPopup.toString());
        ctx.renderResponse();
    }

    public static void showPopup123(String popupId) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExtendedRenderKitService service = Service.getRenderKitService(facesContext, ExtendedRenderKitService.class);
        service.addScript(facesContext, "AdfPage.PAGE.findComponentByAbsoluteId('generic:" + popupId + "').show();");
    }

    public static void showPopup2(RichPopup popup) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        String popupId = popup.getClientId(ctx);
        ExtendedRenderKitService erkService = Service.getService(ctx.getRenderKit(), ExtendedRenderKitService.class);
        erkService.addScript(ctx,
                             "var hints = {autodismissNever:true}; " + "AdfPage.PAGE.findComponent('" + popupId +
                             "').show(hints);");
    }

    public static void showPopupJavaScript(String popupId) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExtendedRenderKitService service = Service.getRenderKitService(facesContext, ExtendedRenderKitService.class);
        service.addScript(facesContext, "AdfPage.PAGE.findComponentByAbsoluteId('" + popupId + "').show();");
    }

    public static void showPopupJavaScriptAdd() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExtendedRenderKitService service = Service.getRenderKitService(facesContext, ExtendedRenderKitService.class);
        service.addScript(facesContext,
                          "function closeTermsDialog(evt){\n" +
                          "  var checkbox = evt.getSource().findComponent(\"sbc2\");\n" +
                          "  if(checkbox.getSubmittedValue()==false){\n" + "    evt.cancel();\n" + "  }\n" + "}");
    }


    public static void hidePopupJavaScript(String popupId) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExtendedRenderKitService service = Service.getRenderKitService(facesContext, ExtendedRenderKitService.class);
        service.addScript(facesContext, "AdfPage.PAGE.findComponentByAbsoluteId('generic:" + popupId + "').hide();");
    }

    /**
     * Recipe: Determining whether the current transaction has pending changes.
     *
     * @return true/false whether there are pending changes in the BC.
     */
    public static boolean isBCTransactionDirty() {
        // get application module and check for dirty transaction
        ApplicationModule am = ADFUtils.getDCBindingContainer()
                                       .getDataControl()
                                       .getApplicationModule();
        return am.getTransaction().isDirty();
    }

    /**
     * Recipe: Determining whether the current transaction has pending changes.
     *
     * @return true/false whether there are pending changes in the controller.
     */
    public static boolean isControllerTransactionDirty() {
        // get data control and check for dirty transaction
        BindingContext bc = BindingContext.getCurrent();
        String currentDataControlFrame = bc.getCurrentDataControlFrame();
        return bc.findDataControlFrame(currentDataControlFrame).isTransactionDirty();
    }

    /**
     * Recipe: Determining whether the current transaction has pending changes.
     *
     * @return true/false whether there are pending changes.
     */
    public static boolean isTransactionDirty() {
        // check for dirty transaction in both the model and the controller.
        return isBCTransactionDirty() || isControllerTransactionDirty();
    }

    public static void expandSpilter(RichPanelSplitter splitter) {
        String clientId = splitter.getClientId(FacesContext.getCurrentInstance());
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExtendedRenderKitService service = org.apache
                                              .myfaces
                                              .trinidad
                                              .util
                                              .Service
                                              .getRenderKitService(facesContext, ExtendedRenderKitService.class);
        StringBuilder script = new StringBuilder();
        script.append("var panelSpliter = AdfPage.PAGE.findComponent('")
              .append(clientId)
              .append("');\n");
        script.append("panelSpliter.setCollapsed('');\n");
        service.addScript(facesContext, script.toString());
    }

    public static void collapseSplitter(RichPanelSplitter splitter) {
        String clientId = splitter.getClientId(FacesContext.getCurrentInstance());
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExtendedRenderKitService service = org.apache
                                              .myfaces
                                              .trinidad
                                              .util
                                              .Service
                                              .getRenderKitService(facesContext, ExtendedRenderKitService.class);
        StringBuilder script = new StringBuilder();
        script.append("var panelSpliter = AdfPage.PAGE.findComponent('")
              .append(clientId)
              .append("');\n");
        script.append("panelSpliter.setCollapsed(true);\n");
        service.addScript(facesContext, script.toString());
    }

    public static void closwBrowser() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExtendedRenderKitService erks = Service.getRenderKitService(facesContext, ExtendedRenderKitService.class);
        StringBuilder sb = new StringBuilder("window.close();");
        erks.addScript(facesContext, sb.toString());
        facesContext.renderResponse();
    }


   

    /**
     * Get a List of attributes as a Map (of name, value) for an iterator.
     * @param iter iterator binding
     * @param attrNames array of attribute names for attributes to be retrieved
     * @return List of attribute values
     */
    public static List<Map<String, Object>> attributesListForIterator(DCIteratorBinding iter, String[] attrNames) {
        List<Map<String, Object>> attributeList = new ArrayList<Map<String, Object>>();
        for (Row r : iter.getAllRowsInRange()) {
            Map<String, Object> alist = new HashMap<String, Object>();
            for (String aName : attrNames) {
                alist.put(aName, r.getAttribute(aName));
            }
            attributeList.add(alist);
        }
        return attributeList;
    }

   
    /**
     * Get the page flow scope
     * @return
     */
    public static Map getPageFlowScope() {
        return AdfFacesContext.getCurrentInstance().getPageFlowScope();
    }

    /**
     * Open a new browser tab/window starting a new bounded task flow.
     *
     * @param taskFlowId - id of bounded task flow to show in new window
     * @param taskFlowParams - params for the task flow (if any)
     * @param windowName - name of browser tab/window (window.name)
     * @param openInWindow - true will open a browser window (if settings of the browser
     *     allow this), false will open a new browser tab.
     */
    public static void launchTaskFlowInNewWindow(TaskFlowId taskFlowId, Map taskFlowParams, String windowName,
                                                 boolean openInWindow) {
        launchTaskFlowInNewWindow(taskFlowId, taskFlowParams, windowName, openInWindow, 1000, 750);

    }


    /**
     * Open a new browser tab/window starting a new bounded task flow.
     *
     * @param taskFlowId - id of bounded task flow to show in new window
     * @param taskFlowParams - params for the task flow (if any)
     * @param windowName - name of browser tab/window (window.name)
     * @param openInWindow - true will open a browser window (if settings of the browser
     *     allow this), false will open a new browser tab.
     * @param width
     * @param height
     */

    public static void launchTaskFlowInNewWindow(TaskFlowId taskFlowId, Map taskFlowParams, String windowName,
                                                 boolean openInWindow, int width, int height) {

        String url = ControllerContext.getInstance().getTaskFlowURL(false, taskFlowId, taskFlowParams);

        if (url == null) {
            throw new Error("Unable to launch window for task flow id " + taskFlowId);
        }

        FacesContext context = FacesContext.getCurrentInstance();
        ExtendedRenderKitService extendedRenderKitService =
            Service.getService(context.getRenderKit(), ExtendedRenderKitService.class);

        // Build javascript to open a new browser tab/window
        StringBuilder script = new StringBuilder();

        // Unable to get a named firefox tab to gain focus.  To workaround
        // issue we close the tab first, then open it.
        if (!openInWindow && windowName != null) {

            script.append("var hWinx = window.open(\"");
            script.append("about:blank"); // the URL
            script.append("\",\"");
            script.append(windowName);
            script.append("\"");
            script.append(");");
            script.append("\n");
            script.append("hWinx.close();\n");
        }

        // Set a variable with the window properties
        script.append("var winProps = \"status=yes,toolbar=no,copyhistory=no,width=" + width + ",height=" + height +
                      "\";");
        // If we aren't going to open in a new window, then clear the window properties
        if (!openInWindow) {
            script.append("winProps = '';");
        }
        // Set isOpenerValid to true if window.opener (a parent window) is defined and open
        script.append("var isOpenerValid = (typeof(window.opener) != 'undefined' && window.opener != undefined && !window.opener.closed);");
        // Set useProps to true if openInWindow is true or isOpenerValid is true
        script.append("var useProps = (" + openInWindow + " || isOpenerValid);");
        // Set win to the current window, unless we need to use the parent, then set to window.opener (the parent window)
        script.append("var win = window; if (typeof(isChildWindow) != 'undefined' && isChildWindow != undefined && isChildWindow == true && isOpenerValid) {win = window.opener;}");
        // Set hWin to the window returned by calling open on win
        script.append("var hWin = win.open(\"");
        script.append(url); // the URL
        script.append("\",\"");
        script.append(windowName);
        script.append("\"");
        script.append(", winProps");
        script.append(");");
        // Set focus to the window opened.
        script.append("hWin.focus();");

        extendedRenderKitService.addScript(context, script.toString());
    }

    public static void executeClientSideScript(String script) {
        FacesContext context = FacesContext.getCurrentInstance();
        ExtendedRenderKitService extendedRenderKitService =
            Service.getService(context.getRenderKit(), ExtendedRenderKitService.class);

        extendedRenderKitService.addScript(context, script);
    }


    /**
     * Get the id of the RichShowDetailItem which is currently disclosed within the
     * RichPanelTabbed or null if no children disclosed.
     *
     * @param panelTabbed
     * @return
     */
    public static String getDisclosedDetailItemId(RichPanelTabbed panelTabbed) {
        RichShowDetailItem item = getDisclosedDetailItem(panelTabbed);
        if (item != null) {
            return item.getId();
        }
        return null;
    }

    /**
     * Get the RichShowDetailItem which is currently disclosed within the
     * RichPanelTabbed or null if no children disclosed.
     *
     * @param panelTabbed
     * @return
     */
    public static RichShowDetailItem getDisclosedDetailItem(RichPanelTabbed panelTabbed) {
        if (panelTabbed != null) {
            Iterator iter = panelTabbed.getChildren().iterator();
            // Loop through all the child components
            while (iter.hasNext()) {
                UIComponent component = (UIComponent) iter.next();
                // Make sure we only check components that are detailItems
                if (component instanceof RichShowDetailItem) {
                    RichShowDetailItem detailItem = (RichShowDetailItem) component;
                    if (detailItem.isDisclosed()) {
                        return detailItem;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Helper method to check if a UI component value
     * is null or empty.
     * @param component UIComponent
     * @return true / false
     */
    public static boolean isEmpty(UIComponent component) {
        boolean isEmpty = false;
        if (component == null) {
            isEmpty = true;
        } else {
            // for a text field, check the value as a String
            if (component instanceof RichInputText) {
                RichInputText textField = (RichInputText) component;
                if (textField.getValue() == null || ((String) textField.getValue()).length() <= 0) {
                    isEmpty = true;
                }
            }

        }
        return isEmpty;
    }

    /**
     * Get the list containging the selected rows for the given table <br/>
     * Make sure the ADF table definition does not have the selection listener
     * and the make current set.
     * @param table
     * @return
     */
    public static List<Row> getSelectedRows(RichTable table) {
        List<Row> rows = new ArrayList<Row>();

        // get the selected row keys (iterator)
        Iterator keyIter = table.getSelectedRowKeys().iterator();
        // remember selected row keys
        Object oldKey = table.getRowKey();
        // loop for each selection
        while (keyIter.hasNext()) {

        }
        // restore originally selected rows
        table.setRowKey(oldKey);
        return rows;
    }

    public static void printRow(Row row) {
        System.out.println("\nSTART " + row.getKey() + " *********************");
        for (int i = 0; i < row.getAttributeCount(); i++) {
            System.out.println("row[" + row.getAttributeNames()[i] + "]:[" + row.getAttribute(i) + "]");

        }
        System.out.println("END   " + row.getKey() + "*********************\n");
    }


    /**
     * Get a List of attributes as a Map (of name, value) for an iterator.
     * @param iter iterator binding
     * @param keyAttrName attribut name for the key of the map
     * @param valueAttrName attribut name for the value
     * @return Map of attribute values
     */
    public static Map<Object, Object> attributesMapForIterator(DCIteratorBinding iter, String keyAttrName,
                                                               String valueAttrName) {
        Map<Object, Object> amap = new HashMap<Object, Object>();
        for (Row r : iter.getAllRowsInRange()) {
            amap.put(r.getAttribute(keyAttrName), r.getAttribute(valueAttrName));
        }
        return amap;
    }

    /**
     * Get a List of attributes as a Map (of name, value) for an iterator.
     * @param iteratorName ADF iterator binding name
     * @param keyAttrName attribut name for the key of the map
     * @param valueAttrName attribut name for the value
     * @return Map of attribute values
     */
    public static Map<Object, Object> attributeMapForIterator(String iteratorName, String keyAttrName,
                                                              String valueAttrName) {
        return attributesMapForIterator(IteratorUtils.findIterator(iteratorName), keyAttrName, valueAttrName);
    }


    /**
     * Programmatic evaluation of EL.
     *
     * @param el EL to evaluate
     * @return Result of the evaluation
     */
    /*     public static Object evaluateEL(String el) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ELContext elContext = facesContext.getELContext();
        ExpressionFactory expressionFactory = facesContext.getApplication().getExpressionFactory();
        ValueExpression exp = expressionFactory.createValueExpression(elContext, el, Object.class);

        return exp.getValue(elContext);
    } */

    public static Object evaluateEL(String el) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ELContext elContext = facesContext.getELContext();
        ExpressionFactory expressionFactory = facesContext.getApplication().getExpressionFactory();
        ValueExpression exp = expressionFactory.createValueExpression(elContext, el, Object.class);
        if (exp == null || elContext == null) {
            return null;
        }
        Object obj = null;
        try {
            obj = exp.getValue(elContext);
        } catch (ManagedBeanCreationException ex) {

        }
        return obj;
    }

    public static String getLocaleValue(String el, String code) {
        Map map = (Map) evaluateEL(el);
        if (map != null) {
            if (map.get(code) != null)
                return (String) map.get(code);
            else
                return code;
        }
        return "";
    }


    /**
     * Programmatic invocation of a method that an EL evaluates to.
     * The method must not take any parameters.
     *
     * @param el EL of the method to invoke
     * @return Object that the method returns
     */
    public static Object invokeEL(String el) {
        return invokeEL(el, new Class[0], new Object[0]);
    }

    /**
     * Programmatic invocation of a method that an EL evaluates to.
     *
     * @param el EL of the method to invoke
     * @param paramTypes Array of Class defining the types of the parameters
     * @param params Array of Object defining the values of the parametrs
     * @return Object that the method returns
     */
    public static Object invokeEL(String el, Class[] paramTypes, Object[] params) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ELContext elContext = facesContext.getELContext();
        ExpressionFactory expressionFactory = facesContext.getApplication().getExpressionFactory();
        MethodExpression exp = expressionFactory.createMethodExpression(elContext, el, Object.class, paramTypes);

        return exp.invoke(elContext, params);
    }

    public static Object invokeEL(String el, Class paramType, Object param) {
        return invokeEL(el, new Class[] { paramType }, new Object[] { param });
    }

    /**
     * Programmatic invocation of a method that an EL evaluates to.
     *
     * @param el EL of the method to invoke
     * @param paramTypes Array of Class defining the types of the
     * parameters
     * @param params Array of Object defining the values of the
     * parametrs
     * @return Object that the method returns
     */

    /*    public static Object invokeEL(String el, Class[] paramTypes, Object[] params) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ELContext elContext = facesContext.getELContext();
        ExpressionFactory expressionFactory = facesContext.getApplication().getExpressionFactory();
        MethodExpression exp = expressionFactory.createMethodExpression(elContext, el, Object.class, paramTypes);
        return exp.invoke(elContext, params);
    }
	 */

    /**
     * Sets a value into an EL object. Provides similar
     * functionality to
     * the &lt;af:setActionListener&gt; tag, except the
     * <code>from</code> is
     * not an EL. You can get similar behavior by using the
     * following...<br>
     * <code>setEL(<b>to</b>, evaluateEL(<b>from</b>))</code>
     *
     * @param el EL object to assign a value
     * @param val Value to assign
     */
    public static void setEL(String el, Object val) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ELContext elContext = facesContext.getELContext();
        ExpressionFactory expressionFactory = facesContext.getApplication().getExpressionFactory();
        ValueExpression exp = expressionFactory.createValueExpression(elContext, el, Object.class);
        exp.setValue(elContext, val);
    }


    public static String errorDialog(String errorMessage) {
        FacesContext context = FacesContext.getCurrentInstance();
        ((HttpSession) context.getExternalContext().getSession(false)).setAttribute("errorMessage", errorMessage);
        ViewHandler viewHandler = context.getApplication().getViewHandler();
        UIViewRoot dialog = viewHandler.createView(context, "/Error.jspx");
        HashMap properties = new HashMap();
        properties.put("width", new Integer(250));
        properties.put("height", new Integer(150));
        AdfFacesContext afContext = AdfFacesContext.getCurrentInstance();
        afContext.launchDialog(dialog, null, // not launched from any component
                               null, // no particular parameters
                               true, properties); // show it in a dialog
        context.renderResponse();
        return "";
    }


    /**
     * Initiates control flow in a task flow region.
     * @param region - RichRegion that represents taskflow.
     * @param outComeEl - action outcome, based on whic control flows
     */
    public static void performControlFlow(RichRegion region, String outComeEl) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        ExpressionFactory f = ctx.getApplication().getExpressionFactory();
        ELContext elctx = ctx.getELContext();
        MethodExpression m = f.createMethodExpression(elctx, outComeEl, String.class, new Class[] { });
        region.queueActionEventInRegion(m, null, null, false, -1, -1, PhaseId.ANY_PHASE);
    }

    public static HttpServletRequest getHttpServletRequest() {
        return ((HttpServletRequest) FacesContext.getCurrentInstance()
                                                 .getExternalContext()
                                                 .getRequest());
    }

    public static HttpSession getHttpSession() {
        return ((HttpSession) FacesContext.getCurrentInstance()
                                          .getExternalContext()
                                          .getSession(false));
    }


    /**
     *Get the ADF datacontrol reference given the control name
     * @param controlName data control name as given in data bindings file
     * @return
     */
    public static DCDataControl findDataControl(String controlName) {
        DCBindingContainer context = (DCBindingContainer) ADFUtils.evaluateEL("#{bindings}");
        DCDataControl dcf = context.findDataControl(controlName);
        return dcf;
    }


    public static void addFacesMessage(String id, FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(id, message);
        FacesContext.getCurrentInstance().renderResponse();
    }

    public static void addFacesMessage(String clientId, javax.faces.application.FacesMessage.Severity severity,
                                       String localizedText) {
        FacesContext.getCurrentInstance().addMessage(clientId, new FacesMessage(severity, "", localizedText));
        FacesContext.getCurrentInstance().renderResponse();
    }

    private static void writeJavaScriptToClient(String script) {
        FacesContext fctx = FacesContext.getCurrentInstance();
        ExtendedRenderKitService erks = Service.getRenderKitService(fctx, ExtendedRenderKitService.class);
        erks.addScript(fctx, script);
    }


    public static String showError(String clientId, String errorMessage) {

        FacesContext ctx = FacesContext.getCurrentInstance();
        StringBuilder javaScriptPopup = new StringBuilder("var popObj=AdfPage.PAGE.findComponent('" + clientId + "');");
        javaScriptPopup.append("if(popObj != null){var errorField = popObj.findComponent('errorMessage');if(errorField != null)");
        javaScriptPopup.append("{errorField.setValue('");
        javaScriptPopup.append(errorMessage);
        javaScriptPopup.append("')}");
        javaScriptPopup.append("popObj.show();}");

        ExtendedRenderKitService erks = Service.getRenderKitService(ctx, ExtendedRenderKitService.class);
        erks.addScript(ctx, javaScriptPopup.toString());
        ctx.renderResponse();
        return "";
    }

    public static void addPartialTarget(UIComponent attribute) {
        AdfFacesContext.getCurrentInstance().addPartialTarget(attribute);
    }


    public static void setFacesMessage(String title, String message) {
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_INFO, title, message);
        context.addMessage(null, fm);
    }



    public static void renderResponse() {
        FacesContext.getCurrentInstance().renderResponse();
    }


    public static void refreshWholePage() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String refreshpage = facesContext.getViewRoot().getViewId();
        ViewHandler viewHandler = facesContext.getApplication().getViewHandler();
        UIViewRoot viewroot = viewHandler.createView(facesContext, refreshpage);
        viewroot.setViewId(refreshpage);
        facesContext.setViewRoot(viewroot);
    }


    /**
     * Constant for signalling failed SRService checkout during eager test.
     */
    public static final String SRSERVICE_CHECKOUT_FAILED = "SRServiceFailed";


    //Setter and getter for session parameters
    public void setSessionParameter(String name, Object value) {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance()
                                                                      .getExternalContext()
                                                                      .getRequest();
        HttpSession session = request.getSession(false);
        session.setAttribute(name, value);
    }

    public Object getSessionParameter(String name) {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance()
                                                                      .getExternalContext()
                                                                      .getRequest();
        HttpSession session = request.getSession(false);
        return session.getAttribute(name);
    }


    /**
     * Method to check if the request is AJAX/PPR
     *
     */
    public static boolean isPprRequest() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return AdfFacesContext.getCurrentInstance().isPartialRequest(facesContext);
    }
	
	/** function return the current jsp name */
  public String getPageName()
  {
    FacesContext context = IteratorUtils.getFacesContext();
    String viewId = context.getViewRoot().getViewId();
    int dotIndex = viewId.indexOf(".");
    if (dotIndex < 0)
    {
      return viewId;
    }
    String fileName = viewId.substring(1, dotIndex);
    return fileName;
  }

/** function used to refresh the hole jsp page */
  public static void refreshPage()
  {
    FacesContext context = FacesContext.getCurrentInstance();
    String currentView = context.getViewRoot().getViewId();
    ViewHandler vh = context.getApplication().getViewHandler();
    UIViewRoot UIV = vh.createView(context, currentView);
    UIV.setViewId(currentView);
    context.setViewRoot(UIV);
  }

/** function take outcome and will navigate to the page */
  public static void navigateToPage(String outcome)
  {
    FacesContext fc = FacesContext.getCurrentInstance();
    fc.getApplication().getNavigationHandler().handleNavigation(fc, null, outcome);
  }

/** function For Cancelling any Changes Happened In The Row (Create New Row or Update Row) */
  public void cancelChangesInCurrentRow(String iteratorName) 
  {
    DCIteratorBinding iterBinding = IteratorUtils.getIterator(iteratorName);
    ViewObject vo = iterBinding.getViewObject();
    Row currentRow = vo.getCurrentRow();
    currentRow.refresh(Row.REFRESH_REMOVE_NEW_ROWS | Row.REFRESH_WITH_DB_FORGET_CHANGES);
  }
	
	
	  
/** function used for uploading file */
  public static void uploadFile(ValueChangeEvent valueChangeEvent, String fileLocation, String fileName)
  {
    UploadedFile file = (UploadedFile) valueChangeEvent.getNewValue();
    if (fileLocation == null)
    {
      fileLocation = "c:/";
    }
    InputStream in;
    FileOutputStream out;
    boolean exists = (new File(fileLocation)).exists();
    if (!exists)
    {
      (new File(fileLocation)).mkdirs();
    }
    if (file != null && file.getLength() > 0)
    {
      FacesContext context = FacesContext.getCurrentInstance();
      FacesMessage message = new FacesMessage("File Uploaded  " + file.getFilename() + " (" + file.getLength() + " bytes)");
      context.addMessage(valueChangeEvent.getComponent().getClientId(context), message);
      try
      {
        out = new FileOutputStream(fileLocation + "" + fileName);
        in = file.getInputStream();
        for (int bytes = 0; bytes < file.getLength(); bytes++)
        {
          out.write(in.read());
        }
        in.close();
        out.close();
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
    else
    {
      String filename = file != null? file.getFilename(): null;
      String byteLength = file != null? "" + file.getLength(): "0";
      FacesContext context = FacesContext.getCurrentInstance();
      FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, " " + " " + filename + " (" + byteLength + " bytes)", null);
      context.addMessage(valueChangeEvent.getComponent().getClientId(context), message);
    }
  }

/** function used for downloading file */
  public static void downloadFile(java.io.OutputStream outputStream, String fileName) throws IOException
  {
    try
    {
      File file = new File(fileName);
      byte[] b = new byte[(int) file.length()];
      FileInputStream fileInputStream = new FileInputStream(file);
      fileInputStream.read(b);
      outputStream.write(b);
      outputStream.flush();
    }
    catch (Exception e)
    {
      FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, null, null);
      fm.setDetail("No file found");
      FacesContext.getCurrentInstance().addMessage(null, fm);
    }
  }

/** function take database sequence name and return the next value for this sequence */  
  public static BigDecimal getSequenceNextValue(String sequenceName)
  {
    SequenceImpl seq = new SequenceImpl(sequenceName, IteratorUtils.getDefaultDBTransaction());
    return new BigDecimal(seq.getSequenceNumber().toString());
  }
  
  
	
/** function take dml sql statement and execute this statement then return number of records affects during the execution */
  public static int executeDML(String sql)
  {
    PreparedStatement stat = null;
    try
    {
      stat = IteratorUtils.getDefaultDBTransaction().createPreparedStatement(sql, 1);
      int result = stat.executeUpdate();
      return result;
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBUtils.close(stat);
    }
    return 0;
  }

  
  
/** function take the folder name (which is located inside Web Content folder) and will return the real path of that folder */   
  public String getContextRealPath(String folderName)
  {
    String path = "";
    try
    {
      path = getContext().getRealPath("/" + folderName);
    }
    catch (Exception e)
    {
      System.out.println(e);
    }
    return path;
  }

  
  
/** function take af:table component (RichTable object) and clear all columns filters */
  public static void clearTableFilters(RichTable tableComponent)
  {
    FilterableQueryDescriptor q = (FilterableQueryDescriptor) tableComponent.getFilterModel();
    Map<String, Object> m = q.getFilterCriteria();
    if (m != null)
    {
      m.clear();
    }
  }

/** function take af:query component (RichQuery object) and reset query fields  */  
  public static void resetQueryFields(RichQuery queryComponent)
  {
    QueryModel queryModel = queryComponent.getModel(); 
    QueryDescriptor queryDescriptor = queryComponent.getValue(); 
    queryModel.reset(queryDescriptor);
  }


/** function take componentId and set focus of this component  */  
  public static void setFocus(String componentId) 
  {
    FacesContext facesContext = FacesContext.getCurrentInstance();
    ExtendedRenderKitService service = Service.getRenderKitService(facesContext, ExtendedRenderKitService.class);
    service.addScript(facesContext, "comp = AdfPage.PAGE.findComponent('"+componentId+"'); comp.focus();");
  }



  public static void createNewFile(String filePath)
  {
    Writer writer = null;
    try
    {
      writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "utf-8"));
    }
    catch (IOException ex)
    {
    }
    finally
    {
      try
      {
        writer.close();
      }
      catch (Exception ex)
      {
      }
    }
  }
  
  
  
  
  public static void editPropertyFile(String filePath, String key, String value)
  {
    try
    {
      FileInputStream in = new FileInputStream(filePath);
      Properties props = new Properties();
      props.load(in);
      in.close();

      FileOutputStream out = new FileOutputStream(filePath);
      props.setProperty(key, value);
      props.store(out, null);
      out.close();
    }
    catch (Exception e)
    {
    }
  }
  
  
  
  public static String readValueFromPropertyFile(String filePath, String key)
  {
    Properties prop = new Properties();
    InputStream input = null;
    try
    {
      input = new FileInputStream(filePath);
      prop.load(input);
      String value = prop.getProperty(key);
      return value;
    }
    catch (Exception e)
    {
    }
    finally
    {
      if (input != null)
      {
        try
        {
          input.close();
        }
        catch (IOException e)
        {
        }
      }
    }
    return null;
  }
  
  
  
   public static String getClientComputerName()
  {
    HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    return request.getRemoteHost().split("\\.")[0];
  }
   
   
   
  
  public static void addCookie(String cookieName, String cookieValue)
  {
    FacesContext context = FacesContext.getCurrentInstance();
    HttpServletResponse response = (HttpServletResponse)context.getExternalContext().getResponse();

    Cookie cookie = new Cookie(cookieName, cookieValue);
    cookie.setMaxAge(60*60*24*365*5);
    response.addCookie(cookie);
  }

  public static void removeCookie(String cookieName)
  {
    FacesContext context = FacesContext.getCurrentInstance();
    HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
    HttpServletResponse response = (HttpServletResponse)context.getExternalContext().getResponse();
    Cookie[] cookies = request.getCookies();

    if (cookies != null)
    {
      for (int i = 0; i < cookies.length; i++)
      {
        String name = cookies[i].getName();

        if ((name == null) || (!name.equals(cookieName)))
          continue;
        cookies[i].setMaxAge(0);
        response.addCookie(cookies[i]);
        break;
      }
    }
  }

  public static String getCookieValue(String cookieName)
  {
    FacesContext context = FacesContext.getCurrentInstance();
    HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
    Cookie[] cookies = request.getCookies();

    if (cookies != null)
    {
      for (int i = 0; i < cookies.length; i++)
      {
        String name = cookies[i].getName();
        String value = cookies[i].getValue();
        int age = cookies[i].getMaxAge();

        if ((name != null) && (name.equals(cookieName)) && (age != 0))
        {
          return value;
        }
      }
    }
    return null;
  }
  
    

    public void ValidateItem(UIComponent MyComponent, FacesContext MyContext, String Header, String Footer, int Level) { // Force Validation
        FacesMessage MyMessage = new FacesMessage();
        if (Level == 1) {
            MyMessage = new FacesMessage(MyMessage.SEVERITY_FATAL, Header, Footer);
        } else if (Level == 2) {
            MyMessage = new FacesMessage(MyMessage.SEVERITY_ERROR, Header, Footer);
        } else if (Level == 3) {
            MyMessage = new FacesMessage(MyMessage.SEVERITY_WARN, Header, Footer);
        } else if (Level == 4) {
            MyMessage = new FacesMessage(MyMessage.SEVERITY_INFO, Header, Footer);
        }

        if (MyComponent instanceof RichInputText) {
            ((RichInputText) MyComponent).setValid(false);
        } else if (MyComponent instanceof RichSelectOneChoice) {
            ((RichSelectOneChoice) MyComponent).setValid(false);
        }

        MyContext.addMessage(MyComponent.getClientId(MyContext), MyMessage);

    }

    public void Validate(FacesContext MyContext, String Header, String Footer, int Level) { // Force Validation
        FacesMessage MyMessage = new FacesMessage();
        if (Level == 1) {
            MyMessage = new FacesMessage(MyMessage.SEVERITY_FATAL, Header, Footer);
        } else if (Level == 2) {
            MyMessage = new FacesMessage(MyMessage.SEVERITY_ERROR, Header, Footer);
        } else if (Level == 3) {
            MyMessage = new FacesMessage(MyMessage.SEVERITY_WARN, Header, Footer);
        } else if (Level == 4) {
            MyMessage = new FacesMessage(MyMessage.SEVERITY_INFO, Header, Footer);
        }

        MyContext.addMessage(null, MyMessage);

    }


    public void ShowDialog(RichPopup Pop) { // Run Dialog using its Popup
        RichPopup.PopupHints hints = new RichPopup.PopupHints();
        Pop.show(hints);
    }

   
    
    public Boolean CheckUserNamePassword(String User, String Password, String IPAddress, String Port,
                                         String InstanceName) {
        Boolean FinalResult = false;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection conn = null;
        try {
            conn =
                DriverManager.getConnection("jdbc:oracle:thin:@" + IPAddress + ":" + Port + ":" + InstanceName, User,
                                            Password);
        } catch (SQLException e) {
            FinalResult = false;
        }

        if (conn != null) {
            try {
                if (conn.isValid(20) == true) {
                    FinalResult = true;
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    FinalResult = false;
                }
            } catch (SQLException e) {
                FinalResult = false;
            }
        } else {
            FinalResult = false;
        }
        return FinalResult;
    }

    public Boolean isDirty(String MyIteratorBinding) {
        ApplicationModule AM = IteratorUtils.GetAppModuleUsingIteratorBinding(MyIteratorBinding);
        return AM.getTransaction().isDirty();
    }




    public static String RPad(String str, Integer length, char car) {
        return str + String.format("%" + (length - str.length()) + "s", "").replace(" ", String.valueOf(car));
    }

    public static String LPad(String str, Integer length, char car) {
        return String.format("%" + (length - str.length()) + "s", "").replace(" ", String.valueOf(car)) + str;
    }

 


   
    

 
    
}
