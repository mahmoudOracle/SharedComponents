package view.util;

import java.util.Locale;
import java.util.MissingResourceException;  
 import java.util.ResourceBundle;

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import oracle.adf.share.logging.ADFLogger;

import oracle.javatools.resourcebundle.BundleFactory;

public class ResourceBundleUtils {
    
    private static ADFLogger LOGGER = ADFLogger.createADFLogger(ResourceBundleUtils.class);
    private static String message_not_found = " not found in resource bundle";

    private static ResourceBundle getBundle(String baseName) {
        FacesContext ctx = getFacesContext();
        UIViewRoot uiRoot = ctx.getViewRoot();
        Locale locale = uiRoot.getLocale();
        ClassLoader ldr = Thread.currentThread().getContextClassLoader();
        return ResourceBundle.getBundle(baseName, locale, ldr);
    }
    public static   String getResourceBundleKey(String resourceBundlePath, String key) {  
      ResourceBundle bundle = getResourceBundle(resourceBundlePath);  
      return (String)getResourceBundleKey(bundle, key);  
    }


    private static String getStringSafely(ResourceBundle bundle, String key, String defaultValue) {
        String resource = null;
        try {
            resource = bundle.getString(key);
        } catch (MissingResourceException mrex) {
            if (defaultValue != null) {
                resource = defaultValue;
            } else {
                resource = "Missing resource: " + key;
            }
        }
        return resource;
    }


    private static FacesContext getFacesContext() {  
      return FacesContext.getCurrentInstance();  
    }  
    
    private static ResourceBundle getResourceBundle(String resourceBundlePath) {  
      return ResourceBundle.getBundle(resourceBundlePath, getFacesContext().getViewRoot().getLocale());  
    }  

    private static Object getResourceBundleKey(ResourceBundle resourceBundle, String key) {   
      try {  
        return (Object)resourceBundle.getString(key);  
      } catch (MissingResourceException mrExp) {  
        LOGGER.severe(key + message_not_found);
        throw new RuntimeException(key + message_not_found);
      }  
    } 
	
	
	
/** function take the resource bundle base Name and the key and return the value from resource bundle */
  public static String getStringFromBundle(String baseName, String key)
  {
    ResourceBundle bundle = getBundle(baseName);
    return getStringSafely(bundle, key, null);
  }

/** function return current application locale */
  public static   Locale getLocale()
  {
    return getFacesContext().getViewRoot().getLocale();
  }

      
/** function used to set current local*/
  public static   void setBrowserLocal(String locale)
  {
    FacesContext ctx = FacesContext.getCurrentInstance();
    UIViewRoot uiRoot = ctx.getViewRoot();
    Locale bLocale = new Locale(locale);
    uiRoot.setLocale(bLocale);
  }

/** function return the current browser local ( e.g. "ar" - "en" - ....) */
  public static   String getBrowserLocal()
  {
    FacesContext ctx = FacesContext.getCurrentInstance();
    UIViewRoot uiRoot = ctx.getViewRoot();
    return uiRoot.getLocale().toString();
  }
	
    public static  void SwitchLocale(String _pLocale) {
        if (_pLocale != null && !_pLocale.equals("")) {
            FacesContext fc = FacesContext.getCurrentInstance();
            Locale locale = new Locale(_pLocale);
            fc.getViewRoot().setLocale(locale);
        }    
    }

    public static  String AccessBundleItemValue(String BundleName, String BundleNameItem) {
        ResourceBundle bundle = BundleFactory.getBundle(BundleName);
        return bundle.getString(BundleNameItem);
    }


} // The End of Class;
