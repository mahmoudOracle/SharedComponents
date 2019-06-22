package view.util;

import java.util.Enumeration;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionUtils {

    public static void printSessionVars(){

        System.out.println("SESSION VARS");

        System.out.println("");
        System.out.println(" [ SESSION VARS ] ");
        System.out.println();

        HttpSession session = SessionUtils.getSession();

        Enumeration sessionNames = session.getAttributeNames();
        String sessionName = null;
        Object sessionValue = null;

        while (sessionNames.hasMoreElements()) {
          sessionName = (String)sessionNames.nextElement();
          sessionValue = session.getAttribute(sessionName);
          System.out.println("\t Session name: " + sessionName +
                             ", value: " + sessionValue);
        }

        System.out.println();
        System.out.println(" [ SESSION VARS ] ");
        System.out.println("");
    }


    // Write Attribute to session

    public static void setStringAttributeToSession(String attrName, String attrValue){
        SessionUtils.getSession().setAttribute(attrName, attrValue);
    }

    public static String getStringAttributeFromSession(String attrName){
        return (String) SessionUtils.getSession().getAttribute(attrName);
    }

    // Write Object to Session

    public static void writeObjectToSession(String objName, Object obj) {
        SessionUtils.getSession().setAttribute(objName, obj);
    }

    public static Object getObjectFromSession(String objName) {
        Object res = null;
        if(SessionUtils.getSession() != null)
          res = SessionUtils.getSession().getAttribute(objName);
        return res;

    }

    /////////////////////////////////////////////

    public static HttpSession getSession() {
        try {
            ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletRequest request = (HttpServletRequest)ectx.getRequest();
            return request.getSession();
        } catch (Exception e) {
            System.out.println("*SessionUtils.HttpSession*" + e.getMessage());
            return null;
        }
    }

    /////////////////////////////////////////////

    public static void invalidate(){
        System.out.println("INVALIDATE SESSION");
        getSession().invalidate();
        System.out.println("INVALIDATE SESSION");
    }

} // The End of Class;
