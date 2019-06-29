package view.util;

import oracle.adf.model.BindingContext;
import oracle.binding.BindingContainer;
import oracle.jbo.Row;
import oracle.jbo.uicli.binding.JUCtrlListBinding;

public class BindingsUtils {

    // same as ADFUtils.findBindingContainer(String pageDefName) but public
    public static BindingContainer findBindingContainer(String pageDefName) {
        BindingContext bctx = ADFUtils.getDCBindingContainer().getBindingContext();
        BindingContainer foundContainer = bctx.findBindingContainer(pageDefName);
        return foundContainer;
    }

    public static Object getAttributeByBindings_listOfValues(String listOfValuesName, String attrName) {
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
    public static BindingContainer getBindingsContOfOtherPage(String pageUsageId) {
        return (BindingContainer) ExpressionUtils.evaluateEL("#{data." + pageUsageId + "}");
    }

    public static BindingContainer getBindingsContainerByPageDef(String pageDef) {
        return getBindingsContOfOtherPage(pageDef);
    }

} // The End of Class;
