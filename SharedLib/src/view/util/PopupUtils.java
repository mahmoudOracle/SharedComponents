package view.util;

import javax.faces.component.UIComponent;
import oracle.adf.view.rich.component.rich.RichPopup;

public class PopupUtils {

   public  boolean hideParentPopup(UIComponent component){
        RichPopup parentPopUp=retriveParentPopUp(component);
        if(parentPopUp==null){
            return false;
            }
            parentPopUp.cancel();
            return true;
        }
    
    private  RichPopup retriveParentPopUp(UIComponent component){
        if(component==null){
            return null;
        }
        
        if(component instanceof RichPopup){
            return (RichPopup)component; 
        }
         return retriveParentPopUp(component.getParent());
    }
    
    
    public  void closeDoubleParentPopup (UIComponent component){
        
        RichPopup firstParentPopup = retriveParentPopUp(component);
        RichPopup secondParentPopup = retriveParentPopUp(firstParentPopup.getParent());
        
        hideParentPopup(secondParentPopup);
    }
    
} // The End of Class;
