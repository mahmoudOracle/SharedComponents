package view.util;

import oracle.adf.controller.ControllerContext;

public class ControllerContextUtils {
    
    public  void printControllerContextInfo(){
        
        System.out.println();
        System.out.println("Controller Context");
        
        
        String str_1 = ControllerContext.getInstance().getCurrentViewPort().getViewId();
        System.out.println("str_1 " + str_1);
        
        String url = ControllerContext.getInstance().getGlobalViewActivityURL(str_1);
        System.out.println("url " + url);
        

        System.out.println("Controller Context");
        System.out.println();
    }
    
}// The End of Class;
