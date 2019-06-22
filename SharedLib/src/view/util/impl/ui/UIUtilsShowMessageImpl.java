package view.util.impl.ui;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import javax.faces.application.FacesMessage;

public class UIUtilsShowMessageImpl {
    
    public static void onScreen(String message){
        FacesMessage msg = new FacesMessage(message);
        msg.setSeverity(FacesMessage.SEVERITY_INFO);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    
    //Show message of type - FATAL, ERROR, WARN, INFO
    public static void onScreen(String msgType, String msg){
        FacesMessage fm = new FacesMessage(msg);
        if (msgType.equals("FATAL")) fm.setSeverity(FacesMessage.SEVERITY_FATAL);
        else if (msgType.equals("ERROR")) fm.setSeverity(FacesMessage.SEVERITY_ERROR);
        else if (msgType.equals("WARN")) fm.setSeverity(FacesMessage.SEVERITY_WARN);
        else fm.setSeverity(FacesMessage.SEVERITY_INFO);
        
        FacesContext _facesCtx = FacesContext.getCurrentInstance();
        _facesCtx.addMessage(null, fm);
    }//showMessage
    
} // The End Of Class;
