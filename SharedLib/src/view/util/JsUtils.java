package view.util;

import javax.faces.context.FacesContext;

import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;

public class JsUtils {
    
    public static void runJS(String jsCode){
        FacesContext context = FacesContext.getCurrentInstance();
        ExtendedRenderKitService erks = Service.getService(context.getRenderKit(), ExtendedRenderKitService.class);
        erks.addScript(context, jsCode);
    }
    
    public static void runJS(StringBuilder jsCode){
        runJS(jsCode.toString());
        
    }

} // The End of Class;
