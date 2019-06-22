package view.util;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;

import javax.faces.context.FacesContext;

import oracle.adf.model.binding.DCIteratorBinding;

import oracle.jbo.ApplicationModule;
import oracle.jbo.Row;
import oracle.jbo.ViewCriteriaManager;
import oracle.jbo.ViewObject;


import view.util.impl.vo.CONSTANTS_VO;
import view.util.impl.vo.VCUtilsImpl;
import view.util.impl.vo.VOUtilsImpl;

import org.apache.myfaces.trinidad.event.SelectionEvent;

public class VOUtils {

    public void cancelChangesForRows(String name, boolean isVo) {
        ViewObject vo = null;
        if (isVo) {
            vo = getViewObjectByName(name);
        } else {
            vo = IteratorUtils.getIterator(name).getViewObject();
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
    ///////////////////////////////////////////////
    // View Objects
    ///////////////////////////////////////////////

    // Print

    public static void printViewObjectInfo(String voName) {
        printViewObjectInfo(getViewObjectByName(voName));
    }

    public static void printViewObjectInfo(ViewObject vo) {
        VOUtilsImpl.printViewObjectInfo(vo);
    }

    // GET

    public static ViewObject getViewObjectByName(String ViewObjectName) {
        ApplicationModule am = ADFUtils.getApplicationModuleForDataControl(CONSTANTS_VO.AppModuleName);
        ViewObject vo = am.findViewObject(ViewObjectName);
        return vo;
    }

    /** function take the name of the view object and return this view object */
    public static ViewObject getViewObjectByName1(String viewObjectName) {
        return IteratorUtils.getDefaultApplicationModule().findViewObject(viewObjectName);
    }


    ///////////////////////////////////////////////
    // View Criterias
    ///////////////////////////////////////////////


    public static void printViewCriteriaNames(String voName) {
        printViewCriteriaNames(getViewObjectByName(voName));
    }

    public static void printViewCriteriaNames(ViewObject vo) {
        VCUtilsImpl.printViewCriteriaNames(vo);
    }

    public static void printApplyViewCriteriaNames(String voName) {
        printApplyViewCriteriaNames(getViewObjectByName(voName));
    }

    public static void printApplyViewCriteriaNames(ViewObject vo) {
        VCUtilsImpl.printApplyViewCriteriaNames(vo);
    }


    /** function take view object name and delete all rows in this viewobject */
    public static void clearViewObject(String viewObjectName) {
        ViewObject vo = IteratorUtils.getDefaultApplicationModule().findViewObject(viewObjectName);
        vo.executeQuery();
        while (vo.hasNext()) {
            vo.next().remove();
            if (vo.getEstimatedRowCount() > 0) {
                vo.first();
            }
        }
        if (vo.getEstimatedRowCount() > 0) {
            vo.getCurrentRow().remove();
        }
    }


    private static Object invokeMethod(String expr, Class returnType, Class[] argTypes, Object[] args) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ELContext elctx = fc.getELContext();
        ExpressionFactory elFactory = fc.getApplication().getExpressionFactory();
        MethodExpression methodExpr = elFactory.createMethodExpression(elctx, expr, returnType, argTypes);
        return methodExpr.invoke(elctx, args);
    }


    /** function take iterator name and return the view object associated to this iterator */
    public  ViewObject getViewObjectFromIterator(String iteratorName) {
        return IteratorUtils.getIterator(iteratorName).getViewObject();
    }

    /** function can be called when the developer override table selectionListener and want to make the selected row is the current row (e.g makeTableSelectedRowCurrentRow("#{bindings.UsersView1.treeModel.makeCurrent}", selectionEvent)) */
    public static void makeTableSelectedRowCurrentRow(String exp, SelectionEvent selectionEvent) {
        invokeMethod(exp, null, new Class[] { SelectionEvent.class }, new Object[] { selectionEvent });
    }

    /** function take iterator name and attribute name and value then set the attribute with the value for all rows in the iterator */
    public void setValueToAllRowsInTheIterator(String iteratorName, String attributeName, Object value) {
        DCIteratorBinding itemIter = IteratorUtils.getIterator(iteratorName);
        for (int i = 0; i < itemIter.getViewObject().getEstimatedRowCount(); i++) {
            Row r = itemIter.getRowAtRangeIndex(i);
            r.setAttribute(attributeName, value);
        }
    }


    /** function to cancel changes in ViewObject */
    public void cancelChangesInViewObject(String voName) {
        cancelChangesForRows(voName, true);
    }

    //    // GET
    //
    //    public static ViewCriteria getViewCriteriaByName(ViewObject vo, String vcName) {
    //    }
    //
    //    // ADD
    //
    //    public static void addViewCriteria(ViewObject vo, String vcName) {
    //    }
    //
    //
    //    // Clear
    //
    //    public static void clearViewCriteriaByName(String voName, String viewCriteriaForDeletion) {
    //    }
    //
    //    public static void clearViewCriteriaByName(ViewObject vo, String viewCriteriaForDeletion) {
    //    }
    //
    //
    //    public static void clearViewCriteriaByNameAndExecuteViewObject(ViewObject vo, String viewCriteriaForDeletion) {
    //    }
    //
    //    public static void clearViewCriteriaByNameAndExecuteViewObject(String viewObjectName, String viewCriteriaForDeletion) {
    //    }
    //
    //
    //    public static void clearCutternViewCriterias(ViewObject vo) {
    //    }
    //
    //    public static void clearCutternViewCriteriasAndExecuteViewObject (ViewObject vo) {
    //    }
    //
    //
    //    ///////////////////////////////////////////////
    //    // Where Clauses
    //    ///////////////////////////////////////////////
    //
    //    public static void printWhereClause (ViewObject vo) {
    //    }
    //
    //
    //    public static void applyWhereClauseToViewObjectAndExecuteViewObject (ViewObject vo, String WhereClause) {
    //    }
    //
    //    public static void clearWhereClauseToViewObject (ViewObject vo) {
    //    }
    //
    //    public static void clearWhereClauseToViewObjectAndExecuteViewObject (ViewObject vo) {
    //    }


} // The End of Class;
