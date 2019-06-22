package view.actions;


import java.io.IOException;
import java.io.OutputStream;

import java.util.ArrayList;

import javax.faces.event.ActionEvent;

import model.shared.exceptions.messages.BundleUtils;

import oracle.adf.controller.ControllerContext;

import oracle.adf.view.rich.component.rich.data.RichTable;

import oracle.jbo.ViewObject;



import view.util.ADFUtils;
import view.util.JSFUtils;

import com.sun.jmx.snmp.Timestamp;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import java.util.HashMap;
import java.util.Map;

import javax.faces.context.FacesContext;

/* import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook; */

import oracle.adf.model.BindingContainer;
import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;

import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.share.ADFContext;

public class CommonActions {

    public static final class Operations {
        public static final String COMMIT = "Commit";
        public static final String ROLLBACK = "RollBack";
        public static final String INSERT = "CreateInsert";
        public static final String DELETE = "Remove";
    }

    /**
     * Common actionListener for all commit operations.
     *
     * @param actionEvent
     */
    public void commit(final ActionEvent actionEvent) {
        if (ADFUtils.hasChanges()) {
            // allow derived beans to handle before commit actions
            onBeforeCommit(actionEvent);
            // allow derived beans to handle commit actions
            onCommit(actionEvent);
            // allow derived beans to handle after commit actions
            onAfterCommit(actionEvent);
        } else {
            // display "No changes to commit" message
            JSFUtils.addFacesInformationMessage(BundleUtils.loadMessage("00002"));
        }
    }

    /**
     * Base class before commit implementation.
     *
     * @param actionEvent
     */
    protected void onBeforeCommit(final ActionEvent actionEvent) {
    }

    /**
     * Base class commit implementation.
     *
     * @param actionEvent
     */
    protected void onCommit(final ActionEvent actionEvent) {
        // execute commit
        ADFUtils.execOperation(Operations.COMMIT);
    }

    /**
     * Base class after commit implementation.
     *
     * @param actionEvent
     */
    protected void onAfterCommit(final ActionEvent actionEvent) {
        // display "Changes were committed successfully" message
        JSFUtils.addFacesInformationMessage(BundleUtils.loadMessage("00003"));
    }


    /**
     * Common actionListener for all rollback operations.
     *
     * @param actionEvent
     */
    public void rollback(final ActionEvent actionEvent) {
        if (ADFUtils.hasChanges()) {
            // allow derived beans to handle before rollback actions
            onBeforeRollback(actionEvent);
            // allow derived beans to handle rollback actions
            onRollback(actionEvent);
            // allow derived beans to handle after rollback actions
            onAfterRollback(actionEvent);
        } else {
            // display "No changes to rollback" message
            JSFUtils.addFacesInformationMessage(BundleUtils.loadMessage("00004"));
        }
    }

    /**
     * Base class before rollback implementation.
     *
     * @param actionEvent
     */
    protected void onBeforeRollback(final ActionEvent actionEvent) {
    }

    /**
     * Base class rollback implementation.
     *
     * @param actionEvent
     */
    protected void onRollback(final ActionEvent actionEvent) {
        ADFUtils.execOperation(Operations.ROLLBACK);
    }

    /**
     * Base class after rollback implementation.
     *
     * @param actionEvent
     */
    protected void onAfterRollback(final ActionEvent actionEvent) {
        // display "Changes were rolled back successfully" message
        JSFUtils.addFacesInformationMessage(BundleUtils.loadMessage("00005"));
    }

    /**
     * Common actionListener for all create operations.
     *
     * @param actionEvent
     */
    public void create(final ActionEvent actionEvent) {
        if (ADFUtils.hasChanges()) {
            onCreatePendingChanges(actionEvent);
        } else {
            onContinueCreate(actionEvent);
        }
    }

    /**
     * Base class before create implementation.
     *
     * @param actionEvent
     */
    protected void onBeforeCreate(final ActionEvent actionEvent) {
        // commit before creating a new record
        ADFUtils.execOperation(Operations.COMMIT);
    }

    /**
     * Base class create implementation.
     *
     * @param actionEvent
     */
    public void onCreate(final ActionEvent actionEvent) {
        ADFUtils.execOperation(Operations.INSERT);
    }

    /**
     * Base class after create implementation.
     *
     * @param actionEvent
     */
    protected void onAfterCreate(final ActionEvent actionEvent) {
    }

    /**
     * Handles changes before creating a new row.
     *
     * @param actionEvent
     */
    public void onCreatePendingChanges(final ActionEvent actionEvent) {
        ADFUtils.showPopup("CreatePendingChanges");
    }

    /**
     * Called from the "CreatePendingChanges" popup to continue
     * with the new record creation.
     *
     * @param actionEvent
     */
    public void onContinueCreate(final ActionEvent actionEvent) {
        CommonActions actions = getCommonActions();
        actions.onBeforeCreate(actionEvent);
        actions.onCreate(actionEvent);
        actions.onAfterCreate(actionEvent);
    }

    /**
     * Common actionListener for all delete operations.
     *
     * @param actionEvent
     */
    public void delete(final ActionEvent actionEvent) {
        onConfirmDelete(actionEvent);
    }

    /**
     * Base class before delete implementation.
     *
     * @param actionEvent
     */
    protected void onBeforeDelete(final ActionEvent actionEvent) {
    }

    /**
     * Base class delete implementation.
     *
     * @param actionEvent
     */
    public void onDelete(final ActionEvent actionEvent) {
        ADFUtils.execOperation(Operations.DELETE);
    }

    /**
     * Base class after delete implementation.
     *
     * @param actionEvent
     */
    protected void onAfterDelete(final ActionEvent actionEvent) {
        // commit before creating a new record
        ADFUtils.execOperation(Operations.COMMIT);
    }

    /**
     * Handles changes before deleting a row.
     *
     * @param actionEvent
     */
    public void onConfirmDelete(final ActionEvent actionEvent) {
        ADFUtils.showPopup("DeleteConfirmation");
    }

    /**
     * Called from the "DeleteConfirmation" popup to continue
     * with the record deletion.
     *
     * @param actionEvent
     */
    public void onContinueDelete(final ActionEvent actionEvent) {
        CommonActions actions = getCommonActions();
        actions.onBeforeDelete(actionEvent);
        actions.onDelete(actionEvent);
        actions.onAfterDelete(actionEvent);
    }

    /**
     * Helper to get the CommonActions for subclassed beans.
     *
     * @return the CommonActions bean.
     */
    private CommonActions getCommonActions() {
        CommonActions actions =
            (CommonActions) JSFUtils.getExpressionObjectReference("#{" + getManagedBeanName() + "}");
        if (actions == null) {
            actions = this;
        }
        return actions;
    }

    /**
     * Helper to get the managed bean name from the page name.
     *
     * @return the managed bean name.
     */
    private String getManagedBeanName() {
        return getPageId().replace("/", "").replace(".jspx", "");
    }

    /**
     * Helper to get the page name.
     *
     * @return the page name
     */
    public String getPageId() {
        ControllerContext ctx = ControllerContext.getInstance();
        return ctx.getCurrentViewPort()
                  .getViewId()
                  .substring(ctx.getCurrentViewPort()
                                .getViewId()
                                .lastIndexOf("/"));
    }


    /**
     * @param facesContext
     * @param outputStream
     * @param table
     * @param viewObj
     * @param pMap
     * @throws IOException
     *
     * This Method is a generic method that can be used to export the table data to excel with out any eror.
     * This uses Appache POI. for this to work we should add the appache poi.jar to the application.
     *
     * step-1: Add this to the jsff in the place of export listener
     *
     *
     * step-2: In the bean just pass the table binding, view object which holds the data, the columns which should be exported to the exportTableListenerDirector
     *
     * To change the way the cell appears in the excel you need to change the Style in createStyles()
     */
    
    public static BindingContainer getBindingContainer() {
        //return (BindingContainer)JSFUtils.resolveExpression("#{bindings}");
        return (BindingContainer) BindingContext.getCurrent().getCurrentBindingsEntry();
    }

    public static DCBindingContainer getDCBindingContainer() {
        return (DCBindingContainer) getBindingContainer();
    }

    /*  public void generateExcel(FacesContext facesContext, OutputStream outputStream) throws IOException {
        try {


            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet worksheet = workbook.createSheet("POI Worksheet");

            DCBindingContainer bindings = (DCBindingContainer) BindingContext.getCurrent().getCurrentBindingsEntry();
            DCIteratorBinding dcIteratorBindings = bindings.findIteratorBinding("ViewObj1Iterator");
            HSSFRow excelrow = null;

            // Get all the rows of a iterator
            oracle.jbo.Row[] rows = dcIteratorBindings.getAllRowsInRange();
            int i = 0;


            for (oracle.jbo.Row row : rows) {

                //print header on first row in excel
                if (i == 0) {
                    excelrow = (HSSFRow) worksheet.createRow((short) i);
                    short j = 0;
                    for (String colName : row.getAttributeNames()) {

                        HSSFCell cellA1 = excelrow.createCell((short) j);
                        cellA1.setCellValue(colName);
                        j++;

                    }
                }

                //print data from second row in excel
                ++i;
                short j = 0;
                excelrow = worksheet.createRow((short) i);
                for (String colName : row.getAttributeNames()) {
                    System.out.println("hello " + row.getAttribute(colName));
                    System.out.println("hello " + colName);
                    HSSFCell cell = excelrow.createCell(j);
                    if (colName.equalsIgnoreCase("DepartmentId")) {


                        cell.setCellValue(row.getAttribute(colName).toString());
                        System.out.println("colName " + colName + "row.getAttribute(colName).toString()" +
                                           row.getAttribute(colName).toString());
                    }
                    //logic for cell formatting
                    if (colName.equalsIgnoreCase("DepartmenName")) {
                        cell.setCellValue(row.getAttribute(colName).toString());

                    }
                    //make it double if you want and convert accordingly
                    else if (colName.equalsIgnoreCase("LocationId")) {
                        cell.setCellValue(row.getAttribute(colName).toString());
                    } else if (colName.equalsIgnoreCase("ManagerId")) {
                        if (null != row.getAttribute(colName)) {
                            cell.setCellValue(row.getAttribute(colName).toString());
                        }
                    } else
                        cell.setCellValue(row.getAttribute(colName).toString());

                    j++;

                }

                worksheet.createFreezePane(0, 1, 0, 1);
            }
            workbook.write(outputStream);
            outputStream.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }

    } */
}
