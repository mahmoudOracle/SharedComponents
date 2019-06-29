package view.util;

import java.util.List;
import java.util.Map;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.share.logging.ADFLogger;

import oracle.binding.AttributeBinding;
import oracle.binding.BindingContainer;
import oracle.binding.OperationBinding;

import oracle.jbo.ApplicationModule;
import oracle.jbo.Key;
import oracle.jbo.Row;
import oracle.jbo.RowIterator;
import oracle.jbo.RowSetIterator;
import oracle.jbo.ViewCriteria;
import oracle.jbo.ViewObject;
import oracle.jbo.server.DBTransaction;
import oracle.jbo.server.EntityImpl;
import oracle.jbo.server.ViewRowImpl;
import oracle.jbo.uicli.binding.JUCtrlListBinding;

public class IteratorUtils {

    private ADFLogger _logger = ADFLogger.createADFLogger(IteratorUtils.class);

    public static final ADFLogger LOGGER = ADFLogger.createADFLogger(IteratorUtils.class);

    public static BindingContainer getBindings() { // Access Data Control
        return BindingContext.getCurrent().getCurrentBindingsEntry();
    }

    public static FacesContext getFacesContext() { // Access Faces Context - JSF Resources
        return FacesContext.getCurrentInstance();
    }

    public static AttributeBinding AccessAttribute(String AttributeName) { // Access AttributeBinding
        return (AttributeBinding) getBindings().get(AttributeName);
    }

    public static DCIteratorBinding AccessIteratorBinding(String IteratorName) { // Access IteratorBinding
        return (DCIteratorBinding) getBindings().get(IteratorName);
    }

    public static OperationBinding AccessOperation(String OperationName) { // Access OperationBinding
        return (OperationBinding) getBindings().getOperationBinding(OperationName);
    }

    public static Map GetParameters(String MyOperation) { // Access Map
        return getBindings().getOperationBinding(MyOperation).getParamsMap();
    }

    public static ViewObject GetVO(String IterBinding) { // Access VO of IteratorBinding
        DCIteratorBinding MyIterator = (DCIteratorBinding) getBindings().get(IterBinding);
        return MyIterator.getViewObject();
    }

    public static String GetWhereClause(String IterBinding) { // Access VO Where of IteratorBinding
        DCIteratorBinding MyIterator = (DCIteratorBinding) getBindings().get(IterBinding);
        return MyIterator.getViewObject().getWhereClause();
    }

    public static Object[] GetWhereClauseParams(String IterBinding) { // Access VO Where Parameters of IteratorBinding
        DCIteratorBinding MyIterator = (DCIteratorBinding) getBindings().get(IterBinding);
        return MyIterator.getViewObject().getWhereClauseParams();
    }

    public static Row GetCurrentRow(String IterBinding) { // Access Current Row of IteratorBinding
        DCIteratorBinding MyIterator = (DCIteratorBinding) getBindings().get(IterBinding);
        return MyIterator.getCurrentRow();
    }

    public static ViewCriteria GetViewCriteria(String IterBinding) { // Access VC Row of IteratorBinding
        DCIteratorBinding MyIterator = (DCIteratorBinding) getBindings().get(IterBinding);
        return MyIterator.getViewCriteria();
    }

    public static Long GetRowCount(String IterBinding) { // Access Estimated Row Count of IteratorBinding
        DCIteratorBinding MyIterator = (DCIteratorBinding) getBindings().get(IterBinding);
        return MyIterator.getEstimatedRowCount();
    }

    public static Application GetAppModuleUsingFacesContext() { // Access AM of IteratorBinding
        return getFacesContext().getApplication();
    }


    public static ApplicationModule GetAppModuleUsingIteratorBinding(String IterBinding) { // Access AM of IteratorBinding
        DCIteratorBinding MyIterator = (DCIteratorBinding) getBindings().get(IterBinding);
        return MyIterator.getDataControl().getApplicationModule();
    }


    /** function return application database transaction object */
    public static DBTransaction getDefaultDBTransaction() {
        return (DBTransaction) getDefaultApplicationModule().getTransaction();
    }

    /** function return ApplicationModule Object */
    public static ApplicationModule getDefaultApplicationModule() {
        return (ApplicationModule) JSFUtils.resolveExpression("#{data.AppModuleDataControl.dataProvider}");
    }

    public static void printIteratorInfo(String iteratorName) {

        try {

            System.out.println("----------------------------");
            System.out.println("------- IteratorInfo -------!!!");

            DCIteratorBinding iterator = findIterator(iteratorName);

            // Определить количе�?тво запи�?ей

            RowSetIterator rsi = iterator.getRowSetIterator();


            System.out.println("||||||||||||||||||||||||||");
            System.out.println("Records Count : " + rsi.getRowCount());


            // Данные из �?троки, на которую указывает итератор

            Row r = rsi.getCurrentRow();

            int numAttrs = rsi.getCurrentRow().getAttributeCount();


            System.out.println("AttributeCount : " + numAttrs);
            System.out.println("Current Row Index: " + rsi.getCurrentRowIndex());

            String rowDataStr = "";


            for (int columnNo = 0; columnNo < numAttrs; columnNo++) {
                Object attrData = rsi.getCurrentRow().getAttribute(columnNo);
                rowDataStr += (attrData + "\t");
            }

            System.out.println("Values) " + rowDataStr);

            System.out.println("||||||||||||||||||||||||||");


        } catch (Exception ex) {
            System.out.println("!!! Exception :: printIteratorInfo " + ex.toString());
        }
    }


    public static void printIteratorValues(String iteratorName) {

        try {

            DCIteratorBinding iterator = findIterator(iteratorName);

            // Определить количе�?тво запи�?ей

            RowSetIterator rsi = iterator.getRowSetIterator();

            rsi.reset();

            while (rsi.getCurrentRow() != null) {

                String rowDataStr = "";
                int numAttrs = rsi.getCurrentRow().getAttributeCount();

                for (int columnNo = 0; columnNo < numAttrs; columnNo++) {
                    Object attrData = rsi.getCurrentRow().getAttribute(columnNo);
                    rowDataStr += (attrData + "\t");
                }

                System.out.println(rsi.getCurrentRowIndex() + ") " + rowDataStr);
                rsi.next();

            }

        } catch (Exception ex) {
            System.out.println("!!! Exception :: printIteratorValues " + ex.toString());
        }

    }

    // GET VALUE FROM ITERATOR

    public static Object getValFromIterator(String iteratorName, String attrName) {
        try {
            return (Object) findIterator(iteratorName).getCurrentRow().getAttribute(attrName);
        } catch (Exception ex) {
            //*******************************Check below *************************************
            //new GetValueFromIteratorException(iteratorName, attrName);
            //new GetValueFromIteratorException(ex);
            return "";
        }
    }

    // БЫЛО !!!

    //    public static    Object getValFromIterator(String iteratorName, String attrName){
    //
    //        try {
    //           DCIteratorBinding dcItteratorBindings =  ADFUtils.findIterator(iteratorName);
    //           ViewObject vo = dcItteratorBindings.getViewObject();
    //           Row rowSelected = vo.getCurrentRow();
    //           return (rowSelected.getAttribute(attrName));
    //        } catch (Exception e) {
    //            System.out.println("!!! Exception :: getValFromIteratorByIteratorName: " + e.getMessage());
    //            return "";
    //        }
    //    }

    // БЫЛО END!

    //
    //    public static    String getValFromIteratorVer2(String vo, String attrName) {
    //        try {
    //            BindingContainer bindings = BindingContext.getCurrent().getCurrentBindingsEntry();
    //            JUCtrlHierBinding fclb = (JUCtrlHierBinding)bindings.get(vo);
    //            return fclb.getRowIterator().getCurrentRow().getAttribute(attrName).toString();
    //        } catch (Exception e) {
    //            System.out.println("*GetValFromIterator*" + e.getMessage());
    //            return "";
    //        }
    //    }

    //    public static    String getValFromIteratorList(String vo, String attrName) {
    //        try {
    //            BindingContainer bindings = BindingContext.getCurrent().getCurrentBindingsEntry();
    //            JUCtrlListBinding fclb = (JUCtrlListBinding)bindings.get(vo);
    //            return fclb.getRowIterator().getCurrentRow().getAttribute(attrName).toString();
    //        } catch (Exception e) {
    //            System.out.println("*GetValFromIterator*[" + vo + "]" + e.getMessage());
    //            return "";
    //        }
    //    }


    //  Set Iterator Position

    public static void setIteratorPositionByAttrValue(String iteratorName, String attrName, String attrValue) {
        try {
            DCIteratorBinding iterator = findIterator(iteratorName);
            RowSetIterator rsi = iterator.getRowSetIterator();

            rsi.reset();

            Boolean found = false;

            while (rsi.getCurrentRow() != null) {
                for (int columnNo = 0; columnNo < rsi.getRowCount(); columnNo++) {
                    Object attrData = rsi.getCurrentRow().getAttribute(attrName);

                    if (attrData.equals(attrValue)) {
                        found = true;
                        // System.out.println("attrData " + attrData);
                        return;
                    }
                }

                if (found) {
                    return;
                }
                // System.out.println(rsi.getCurrentRowIndex() + ") " + rowDataStr);
                rsi.next();
            }
            rsi.reset();

        } catch (Exception ex) {
            System.out.println("!!! Exception :: setIteratorPosition " + ex.toString());
        }
    }


    public static void setCurrentRow(String vo, String keyValue) {
        String trap = "";
        try {
            trap = "1";
            BindingContainer bindings = BindingContext.getCurrent().getCurrentBindingsEntry();
            trap = "2";
            JUCtrlListBinding fclb = (JUCtrlListBinding) bindings.get(vo);
            trap = "3";
            Key k = new Key(new Object[] { keyValue });
            trap = "4";
            RowIterator ri = fclb.getRowIterator();
            trap = "4.1";
            Row[] sRow = ri.findByKey(k, 1);
            trap = "5";
            fclb.getRowIterator().setCurrentRow(sRow[0]);
        } catch (Exception ex) {
            System.out.println("!!! Exception || BeanIndex.setCurrentRow " + ex.getMessage() + " trap:" + trap);
        }
    }


    /** function take viewobject name and refresh this view object */
    public static void refreshViewObject(String viewObjectName) {
        ViewObject vo = getDefaultApplicationModule().findViewObject(viewObjectName);
        vo.executeQuery();
    }

    /** function take iterator name and refresh the view object of that iterator without losing the current row */
    public static void refreshVOByIteratorName(String iteratorName) {
        try {
            DCIteratorBinding locationsIter = findIterator(iteratorName);
            Row lRow = locationsIter.getCurrentRow();
            Key key = null;
            if (lRow != null) {
                key = lRow.getKey();
            }
            locationsIter.getViewObject().executeQuery();
            if (key != null) {
                locationsIter.setCurrentRowWithKey(key.toStringFormat(true));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** function take iterator name and refresh this iterator */
    public static void refreshIterator(String iteratorName) {
        DCIteratorBinding dciter = findIterator(iteratorName);
        dciter.executeQuery();
        dciter.refresh(DCIteratorBinding.RANGESIZE_UNLIMITED);
    }

    /** function take iterator name and the attribute name then sum all the attribute values for all rows in the iterator */
    public static double getSumOfAttribute(String iteratorName, String attributeName) {
        DCIteratorBinding dciter = findIterator(iteratorName);
        double sum = 0;
        Row[] rows = dciter.getViewObject().getAllRowsInRange();
        for (Row r : rows) {
            if (r != null && r.getAttribute(attributeName) != null) {
                sum = sum + Double.parseDouble(r.getAttribute(attributeName).toString());
            }
        }
        return sum;
    }

    /** function take iterator name and the attribute name then sum all the attribute values for all rows in the iterator and return long value*/
    public static long getSumOfAttributeAsLong(String iteratorName, String attributeName) {
        DCIteratorBinding dciter = findIterator(iteratorName);
        long sum = 0;
        Row[] rows = dciter.getViewObject().getAllRowsInRange();
        for (Row r : rows) {
            if (r != null && r.getAttribute(attributeName) != null) {
                sum = sum + Long.parseLong(r.getAttribute(attributeName).toString());
            }
        }
        return sum;
    }


    /** function take iterator name  then return the current row of the iterator */
    public static Row getCurrentRow(String iteratorName) {
        if (findIterator(iteratorName).getCurrentRow() != null) {
            return findIterator(iteratorName).getCurrentRow();
        }
        return null;
    }

    /** function used to rollback any changes to the last commit point without changing current row of the passed iterator, Cancel All changes happened on the iterator*/
    public static void rollbackAndBeInTheCurrentRow(String iteratorName) {
        try {
            DCIteratorBinding locationsIter = findIterator(iteratorName);
            Row lRow = locationsIter.getCurrentRow();
            Key key = null;
            if (lRow != null) {
                key = lRow.getKey();
                System.out.println("Printing KEy Value is >> " + key.toString());
            }
            IteratorUtils.executeOperation("Rollback");
            if (key != null) {
                locationsIter.setCurrentRowWithKey(key.toStringFormat(true));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** function For Cancelling any Changes Happened In The Row (Create New Row or Update Row) */
    public static void cancelChangesInCurrentRow(String iteratorName) {
        DCIteratorBinding iterBinding = IteratorUtils.getIterator(iteratorName);
        ViewObject vo = iterBinding.getViewObject();
        Row currentRow = vo.getCurrentRow();
        currentRow.refresh(Row.REFRESH_UNDO_CHANGES | Row.REFRESH_REMOVE_NEW_ROWS | Row.REFRESH_WITH_DB_FORGET_CHANGES);
    }

    /** function For Cancelling any Changes Happened In The Row (Create New Row or Update Row) */
    public static void cancelChangesInCurrentRow2(String iteratorName) {
        findIterator("Employees1Iterator").getCurrentRow()
            .refresh(Row.REFRESH_UNDO_CHANGES | Row.REFRESH_REMOVE_NEW_ROWS | Row.REFRESH_WITH_DB_FORGET_CHANGES);
    }

    /** function to cancel changes in Iterator, All changes happened on the iterator */
    public static void cancelChangesInIter(String Iter) {
        cancelChangesForRows(Iter, false);
    }

    private static void cancelChangesForRows(String name, boolean isVo) {
        ViewObject vo = null;
        if (isVo) {
            vo = VOUtils.getViewObjectByName(name);
        } else {
            vo = findIterator(name).getViewObject();
        }

        if (vo == null) {
            return;
        }

        Row[] rows = vo.getAllRowsInRange();
        if (rows == null) {
            return;
        }

        for (int i = 0; i < rows.length; i++) {
            Row r = rows[i];
            if (r != null) {
                r.refresh(Row.REFRESH_REMOVE_NEW_ROWS | Row.REFRESH_UNDO_CHANGES);
            }
        }
    }

    public static boolean isRowStatusNew(Row rw) {
        return EntityImpl.STATUS_NEW == getRowStatus(rw);
    }

    public static boolean isRowStatusUpdate(Row rw) {
        return EntityImpl.STATUS_MODIFIED == getRowStatus(rw);
    }

    public static byte getRowStatus(Row rw) {
        if (rw != null) {
            ViewRowImpl myRow = (ViewRowImpl) rw;
            EntityImpl entityImpl = myRow.getEntity(0);
            return entityImpl.getEntityState();
        }
        return -1;
    }

    /** function take iterator name and return iterator object */
    public static DCIteratorBinding getIterator(String iteratorName) {
        DCIteratorBinding iter = (DCIteratorBinding) getBindings().get(iteratorName);
        if (iter == null) {
            throw new RuntimeException("Iterator '" + iteratorName + "' not found");
        }
        return iter;
    }

    /**
     * Find an iterator binding in the current binding container by name.
     *
     * @param name iterator binding name
     * @return iterator binding
     */
    public static DCIteratorBinding findIterator(String name) {
        DCIteratorBinding iter = ADFUtils.getDCBindingContainer().findIteratorBinding(name);
        if (iter == null) {
            throw new RuntimeException("Iterator '" + name + "' not found");
        }
        return iter;
    }


    /**
     * @param bindingContainer
     * @param iterator
     * @return
     */
    public static DCIteratorBinding findIteratorByBindingContainer(String bindingContainer, String iterator) {
        DCBindingContainer bindings = (DCBindingContainer) JSFUtils.resolveExpression("#{" + bindingContainer + "}");
        if (bindings == null) {
            throw new RuntimeException("Binding container '" + bindingContainer + "' not found");
        }
        DCIteratorBinding iter = bindings.findIteratorBinding(iterator);
        if (iter == null) {
            throw new RuntimeException("Iterator '" + iterator + "' not found");
        }
        return iter;
    }


    /**
     * Find an operation binding in the current binding container by name.
     *
     * @param name operation binding name
     * @return operation binding
     */
    public static OperationBinding findOperation(String name) {
        OperationBinding op = ADFUtils.getDCBindingContainer().getOperationBinding(name);
        if (op == null) {
            throw new RuntimeException("Operation '" + name + "' not found");
        }
        return op;
    }

    // as an idea to deal with finding any operation keep in mind or try another time with it.
    public static OperationBinding findAnyOperation() {
        OperationBinding op = (OperationBinding) ADFUtils.getDCBindingContainer().getOperationBindings();
        if (op == null) {
            throw new RuntimeException("Operation not found");
        }
        return op;
    }

    /**
     * Find an operation binding in the current binding container by name.
     *
     * @param bindingContianer binding container name
     * @param opName operation binding name
     * @return operation binding
     */
    public static OperationBinding findOperationInCurrentBindContainer(String bindingContianer, String opName) {
        DCBindingContainer bindings = (DCBindingContainer) JSFUtils.resolveExpression("#{" + bindingContianer + "}");
        if (bindings == null) {
            throw new RuntimeException("Binding container '" + bindingContianer + "' not found");
        }
        OperationBinding op = bindings.getOperationBinding(opName);
        if (op == null) {
            throw new RuntimeException("Operation '" + opName + "' not found");
        }
        return op;
    }

    // Wael Abdeen Class
    public static String ExecuteMethod(String MethodName) { // Execute Method
        OperationBinding operationBinding = AccessOperation(MethodName);
        Object result = operationBinding.execute();
        if (!operationBinding.getErrors().isEmpty()) {
            return null;
        }
        return null;
    }


    /** function take operation name (from page definition) and execute this operation (e.g Commit, Rollback, Next, CreateInsert, Delete, .....) */
    public static void executeOperation(String operationName) {
        OperationBinding operationBinding = getBindings().getOperationBinding(operationName);
        operationBinding.execute();
    }

    public static void executeOperationBinding(String exp) {
        ((OperationBinding) JSFUtils.resolveExpression(exp)).execute();
    }


    /**
     * @param opList
     */
    public static void printOperationBindingExceptions(List opList) {
        if (opList != null && !opList.isEmpty()) {
            for (Object error : opList) {
                LOGGER.severe(error.toString());
            }
        }
    }

    public static Object executeOperationBindingNHandleErr(String methodAction, Map param) {
        OperationBinding ob = findOperation(methodAction);

        if (param != null) {
            Map paramOps = ob.getParamsMap();
            paramOps.putAll(param);
        }
        Object result = ob.execute();
        if (!ob.getErrors().isEmpty()) {
            JSFUtils.addFacesErrorMessage("An error occured while serving your request.");
            printOperationBindingExceptions(ob.getErrors());
        }
        return result;
    }


    /** function used to commit changes to database*/
    public static void commit() {
        getDefaultDBTransaction().commit();
    }

    /** function used to rollback any changes to the last commit point*/
    public static void rollback() {
        executeOperation("Rollback");
    }


} // THe End of Class;
