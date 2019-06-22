package view.util.impl.ui;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import javax.faces.event.ValueChangeEvent;

import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.input.RichSelectOneListbox;


public class UIUtilsPrintImpl {
    
    public static void printUIComponentInfo(UIComponent uic) {
           
           try{
               System.out.println();
               System.out.println("@@@@@@@@@@ printUIComponentInfo @@@@@@@@@@@@@@@@");
               System.out.println(uic);
               System.out.println("ID: " + uic.getId());
               System.out.println("ABSOLUTE ID: " + getUIComponentAbsoluteID(uic.getClientId()));
               System.out.println("ABSOLUTE ID WITH ADD PARAMS: " + uic.getClientId());
               System.out.println(uic.getRendererType());

               // printUIComponentChildInfo(uic);
               
               System.out.println("@@@@@@@@@@ printUIComponentInfo @@@@@@@@@@@@@@@@");
               System.out.println();
               
               }  catch (Exception ex){
              System.out.println("!!! Exception || getUIComponentInfo " + ex.toString()); 
           }
       }
       
       private static void printUIComponentChildInfo(UIComponent uic){
           try{
               System.out.println();
               System.out.println("----------");
               System.out.println("Children: " + uic.getChildCount());
               System.out.println(uic.getChildren());
               System.out.println();
               
               }  catch (Exception ex){
              System.out.println("!!! Exception || getUIComponentChildInfo " + ex.toString()); 
           }
       }
       
       
       public static String getUIComponentAbsoluteID(String inputString){
           
           String res = inputString.replace(":0:", ":");
           res =  res.replace(":1:", ":");
           res =  res.replace(":2:", ":");
           res =  res.replace(":3:", ":");
           res =  res.replace(":4:", ":");
           
           return res;
       }
    
    

} // The End of Class;
