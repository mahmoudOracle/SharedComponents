package view.util;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

public class UpdateProcess {

    public static void update(ValueChangeEvent valueChangeEvent){
        FacesContext contxt = FacesContext.getCurrentInstance();
        valueChangeEvent.getComponent().processUpdates(contxt);
    }

} //The end of class;