package view.util;

import oracle.adf.share.ADFContext;

public class SecUtils {
    
    public  Boolean IsSecurityTurnedOn(){
        return ADFContext.getCurrent().getSecurityContext().isAuthorizationEnabled();
    }
    
    public  Boolean IsAuthenticated(){
        return ADFContext.getCurrent().getSecurityContext().isAuthenticated();
    }
    
    public  String getCurrentUser(){
        return ADFContext.getCurrent().getSecurityContext().getUserName();
    }
    
    public  Boolean isUserInRole(String role){
        return ADFContext.getCurrent().getSecurityContext().isUserInRole(role);
    }  
    
    // Get logged in user roles
    public String[] getUserRoles(){
        return ADFContext.getCurrent().getSecurityContext().getUserRoles();
    }// getUserRoles

    // Print logged in user roles using SOP
    public void printUserRoles(){
        for (String role : getUserRoles())
            System.out.println("Role: " + role);
    }// printUserRoles

} // The End of Class;
