package view.util;

import com.sun.faces.mgbean.ManagedBeanCreationException;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;

import oracle.adf.share.ADFContext;

public class ExpressionUtils {


    // Работающий привер вызвова
    // ExpressionUtils.setValueByExpression("#{bindings.lov_My.attributeFromValueList}", valueChangeEvent.getNewValue());

    public static void setValueByExpression(String el, Object val) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ELContext elContext = facesContext.getELContext();
        ExpressionFactory expressionFactory = facesContext.getApplication().getExpressionFactory();
        ValueExpression exp = expressionFactory.createValueExpression(elContext, el, Object.class);
        exp.setValue(elContext, val);
    }


    // Работающий привер вызвова
    // ExpressionUtils.getValueByExpression("#{bindings.lov_My.attribute}"));

    public static String getValueByExpression(String data) {
        FacesContext fc = FacesContext.getCurrentInstance();
        Application app = fc.getApplication();
        ExpressionFactory elFactory = app.getExpressionFactory();
        ELContext elContext = fc.getELContext();
        ValueExpression valueExp = elFactory.createValueExpression(elContext, data, Object.class);
        String Message = null;
        Object obj = valueExp.getValue(elContext);
        if (obj != null) {
            Message = obj.toString();
        }
        return Message;
    }

    public static Object invokeEL(String el, Class[] paramTypes, Object[] params) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ELContext elContext = facesContext.getELContext();
        ExpressionFactory expressionFactory = facesContext.getApplication().getExpressionFactory();
        MethodExpression exp = expressionFactory.createMethodExpression(elContext, el, Object.class, paramTypes);

        return exp.invoke(elContext, params);
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


    public static ValueExpression getValueExpression(String name) {
        FacesContext facesCtx = FacesContext.getCurrentInstance();
        Application app = facesCtx.getApplication();
        ExpressionFactory elFactory = app.getExpressionFactory();
        ELContext elContext = facesCtx.getELContext();
        return elFactory.createValueExpression(elContext, name, Object.class);
    }


    //Evaluate EL expression like "#{xxx}"
    public static Object evaluateEL2(String elString) {
        FacesContext _facesCtx = FacesContext.getCurrentInstance();
        Application app = _facesCtx.getApplication();
        ExpressionFactory elFactory = app.getExpressionFactory();
        ELContext elContext = _facesCtx.getELContext();
        ValueExpression valExp = elFactory.createValueExpression(elContext, elString, Object.class);
        return valExp.getValue(elContext);
    } //evaluateEL


} // The End of Class;
