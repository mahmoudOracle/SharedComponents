package view.util;

import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.event.ValueChangeEvent;

import oracle.adf.model.BindingContext;

import oracle.binding.BindingContainer;

import oracle.jbo.Row;
import oracle.jbo.uicli.binding.JUCtrlListBinding;
import javax.faces.application.Application;

import javax.el.ExpressionFactory;
import javax.el.ELContext;
import javax.el.ValueExpression;


public class BindingsUtils {

    // same as ADFUtils.findBindingContainer(String pageDefName) but public
    public  BindingContainer findBindingContainer(String pageDefName) {
        BindingContext bctx = ADFUtils.getDCBindingContainer().getBindingContext();
        BindingContainer foundContainer =
            bctx.findBindingContainer(pageDefName);
        return foundContainer;
    }
    
    public  Object getAttributeByBindings_listOfValues(String listOfValuesName, String attrName){
        BindingContext bindingContext = BindingContext.getCurrent();
        BindingContainer bindings = bindingContext.getCurrentBindingsEntry();
        JUCtrlListBinding list = (JUCtrlListBinding) bindings.get(listOfValuesName);

        Row selectedRow = list.getCurrentRow();
        Object storeOb = selectedRow.getAttribute(attrName);
        return storeOb;
    }
    
    /**Method to get BindingContainer of Another page ,pageUsageId is the usageId of page defined in DataBindings.cpx file
     * @param pageUsageId
     * @return
     */
    public  BindingContainer getBindingsContOfOtherPage(String pageUsageId) {
        return (BindingContainer) ExpressionUtils.evaluateEL("#{data." + pageUsageId + "}");
    }
    
    public  MethodBinding getMethodBinding(String functionName) {
        try {
            Class[] parms = new Class[] { ValueChangeEvent.class };
            MethodBinding mb =
                FacesContext.getCurrentInstance().getApplication().createMethodBinding(functionName, parms);
            return mb;
        } catch (Exception e) {
            System.out.println("*Utils.getMethodBinding*" + e.getMessage());
            return null;
        }
    }
    
    public BindingContainer getBindingsContainerByPageDef(String pageDef) {
        return getBindingsContOfOtherPage(pageDef);
    }
    
    private Object resolvEl(String data) {
          FacesContext fc = FacesContext.getCurrentInstance();
          Application app = fc.getApplication();
          ExpressionFactory elFactory = app.getExpressionFactory();
          ELContext elContext = fc.getELContext();
          ValueExpression valueExp = elFactory.createValueExpression(elContext, data, Object.class);
          Object Message = valueExp.getValue(elContext);
          return Message;
    }
    
    private BindingContainer getBindingsContOfOtherPage1(String pageUsageId) {
      return (BindingContainer) resolvEl("#{data." + pageUsageId + "}");
    }
    
} // The End of Class;
