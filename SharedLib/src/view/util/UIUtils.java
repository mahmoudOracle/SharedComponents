package view.util;


import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import oracle.adf.model.BindingContext;
import oracle.adf.view.rich.component.rich.input.RichInputListOfValues;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectBooleanCheckbox;
import oracle.adf.view.rich.component.rich.nav.RichButton;

import oracle.adf.view.rich.component.rich.nav.RichCommandButton;
import oracle.adf.view.rich.component.rich.nav.RichCommandToolbarButton;

import oracle.binding.AttributeBinding;
import oracle.binding.BindingContainer;

import oracle.jbo.server.ViewRowImpl;
import oracle.jbo.uicli.binding.JUCtrlListBinding;

import org.apache.myfaces.trinidad.context.RequestContext;
import org.apache.myfaces.trinidad.event.DisclosureEvent;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;

import view.util.impl.ui.UIUtilsPrintImpl;
import view.util.impl.ui.UIUtilsShowMessageImpl;
import view.util.impl.ui.UIUtilsUpdateImpl;


public class UIUtils {


    // --------------------------------------------------------------
    // PRINT UIComponent INFO
    // --------------------------------------------------------------
    public AttributeBinding AccessAttribute(String AttributeName) { // Access AttributeBinding
        return (AttributeBinding) IteratorUtils.getBindings().get(AttributeName);
    }
    
    
    public static void printUIComponentInfo(ValueChangeEvent valueChangeEvent) {
        UIComponent uic = valueChangeEvent.getComponent();
        UIUtilsPrintImpl.printUIComponentInfo(uic);
    }
    
    public static void printUIComponentInfo(ActionEvent actionEvent) {
        UIComponent uic =  actionEvent.getComponent();
        UIUtilsPrintImpl.printUIComponentInfo(uic);
    }
    
    public static void printUIComponentInfo(DisclosureEvent disclosureEvent) {
        UIComponent uic =  disclosureEvent.getComponent();
        UIUtilsPrintImpl.printUIComponentInfo(uic);
    }

    public static void printUIComponentInfo(String uicID) {
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@");
        System.out.println("@@ Stirng UIC ID " + uicID);
        UIUtilsPrintImpl.printUIComponentInfo(UIUtils.getUIComponentByAbsoluteID(uicID));
    }
    
    public static void printUIComponentInfo(UIComponent uic) {
        UIUtilsPrintImpl.printUIComponentInfo(uic);
    }

    // --------------------------------------------------------------
    // UPDATE UIComponent
    // --------------------------------------------------------------
    
    public static void updateUIComponentByAbsoluteID(String uuid){
        UIUtilsUpdateImpl.updateUIByUUID(uuid);
    }
    
    public static void updateUIComponent(UIComponent uic){
        UIUtilsUpdateImpl.updateUIByUUID(uic);
    }
    
    // --------------------------------------------------------------
    // GET UIComponent
    // --------------------------------------------------------------
    
    public static UIComponent getUIComponent(ValueChangeEvent valueChangeEvent) {
        UIComponent uic = valueChangeEvent.getComponent();
        return uic;
    }
    
    public static UIComponent getUIComponent(ActionEvent actionEvent) {
        UIComponent uic = actionEvent.getComponent();
        return uic;
    }
    
    public static UIComponent getUIComponent(DisclosureEvent disclosureEvent) {
        UIComponent uic =  disclosureEvent.getComponent();
        return uic;
    }

    public static UIComponent getUIComponentByAbsoluteID(String uiID){
        FacesContext fctx =  FacesContext.getCurrentInstance();
        UIComponent ui = fctx.getViewRoot().findComponent(uiID);
        
        if (ui == null) {
            new RuntimeException();
        } 
        return ui;
    }
    
    // 
    
    public static String getUIComponentAbsoluteID(ActionEvent actionEvent) {
        UIComponent uic = actionEvent.getComponent();
        return getUIComponentAbsoluteID(uic);
    }
    
    public static String getUIComponentAbsoluteID(UIComponent uic){
        String uicID = UIUtilsPrintImpl.getUIComponentAbsoluteID(uic.getClientId());
        return uicID;
    }
    
    
    ///////////////////////////////////////////////
    // Show Message on Screen
    ///////////////////////////////////////////////
    
    
    public static void showMessageOnScreen(String message){
        UIUtilsShowMessageImpl.onScreen(message);
    }
    
    
    //Show message of type - FATAL, ERROR, WARN, INFO
    public static void showMessageOnScreen(String msgType, String msg){
        UIUtilsShowMessageImpl.onScreen(msgType, msg);
    }


    ///////////////////////////////////////////////
    // Run Button Programmatically by JS
    ///////////////////////////////////////////////
    
    public static void runButtonByJS(String buttonId){
        FacesContext context = FacesContext.getCurrentInstance();

        StringBuilder script = new StringBuilder();

        script.append("var comp1 = AdfPage.PAGE.findComponentByAbsoluteId('")
              .append(buttonId)
              .append("'); ");

        script.append("var actionEvent = new AdfActionEvent(comp1); ");
        script.append("actionEvent.forceFullSubmit(); ");
        script.append("actionEvent.noResponseExpected(); ");
        script.append("actionEvent.queue(); ");

        ExtendedRenderKitService erks = Service.getService(context.getRenderKit(), ExtendedRenderKitService.class);
        erks.addScript(context, script.toString());
    }
    
    
    
    public static void runButtonByJS(RichButton button){
        runButtonByJS(button.getClientId(FacesContext.getCurrentInstance()));
    }

    public void RefreshItem(UIComponent MyComponent) {
        RequestContext.getCurrentInstance().addPartialTarget(MyComponent);
    }


    public Object AccessLovList(String bindingListName, String AttName) {
        BindingContainer bindings = BindingContext.getCurrent().getCurrentBindingsEntry();
        JUCtrlListBinding list = (JUCtrlListBinding) bindings.get(bindingListName);
        return list.getAttribute(AttName);
    }

     public Object AccessList(String MyBinding, String AttName) {
        JUCtrlListBinding MyList = (JUCtrlListBinding) AccessAttribute(MyBinding);
        ViewRowImpl MyRow = (ViewRowImpl) MyList.getSelectedValue();
        return MyRow.getAttribute(AttName);
    } 


    public void EnableDisableItem(UIComponent MyComponent, Boolean isValid) { // enable & Disable Items
        if (MyComponent instanceof RichInputText) {
            ((RichInputText) MyComponent).setDisabled(!isValid);
        } else if (MyComponent instanceof RichCommandButton) {
            ((RichCommandButton) MyComponent).setDisabled(!isValid);
        } else if (MyComponent instanceof RichCommandToolbarButton) {
            ((RichCommandToolbarButton) MyComponent).setDisabled(!isValid);
        } else if (MyComponent instanceof RichSelectBooleanCheckbox) {
            ((RichSelectBooleanCheckbox) MyComponent).setDisabled(!isValid);
        } else if (MyComponent instanceof RichInputListOfValues) {
            ((RichInputListOfValues) MyComponent).setDisabled(!isValid);
        }
    }

    public void VisableInvisableItem(UIComponent MyComponent,
                                     // Apearing the Items
                                     Boolean isVisable) {
        if (MyComponent instanceof RichInputText) {
            ((RichInputText) MyComponent).setVisible(isVisable);
        } else if (MyComponent instanceof RichCommandButton) {
            ((RichCommandButton) MyComponent).setVisible(isVisable);
        } else if (MyComponent instanceof RichCommandToolbarButton) {
            ((RichCommandToolbarButton) MyComponent).setVisible(isVisable);
        } else if (MyComponent instanceof RichSelectBooleanCheckbox) {
            ((RichSelectBooleanCheckbox) MyComponent).setVisible(isVisable);
        } else if (MyComponent instanceof RichInputListOfValues) {
            ((RichInputListOfValues) MyComponent).setVisible(isVisable);
        }
    }


} // The End of Class;
