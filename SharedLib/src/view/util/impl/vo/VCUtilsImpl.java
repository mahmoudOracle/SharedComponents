package view.util.impl.vo;

import java.util.Iterator;
import java.util.List;

import oracle.jbo.ViewCriteria;
import oracle.jbo.ViewCriteriaItem;
import oracle.jbo.ViewCriteriaManager;
import oracle.jbo.ViewCriteriaRow;
import oracle.jbo.ViewObject;


public class VCUtilsImpl {

    ///////////////////////////////////////////////
    // View Criterias
    ///////////////////////////////////////////////
    
    
    public static void printViewCriteriaNames(ViewObject vo) {
        System.out.println();
        System.out.println("VC List");
        ViewCriteriaManager vcm = vo.getViewCriteriaManager();
        String[] allViewCriteriaNames = vcm.getAllViewCriteriaNames();
        
        if (allViewCriteriaNames == null || allViewCriteriaNames.length == 0){
            System.out.println("\t# " + "There aren't VC");
            return;
        }
        
        for(int i = 0; i <= allViewCriteriaNames.length  - 1; i++) {
          System.out.println("\t# " + (i + 1) + ") " + allViewCriteriaNames[i]);
        }
    }
    
    
    public static void printApplyViewCriteriaNames(ViewObject vo) {

        System.out.println();
        System.out.println("  Applied VC List:");
        System.out.println();
        
        ViewCriteriaManager vcm = vo.getViewCriteriaManager();
        String[] allApplyViewCriteriaNames = vcm.getApplyViewCriteriaNames();
        
        if (allApplyViewCriteriaNames == null || allApplyViewCriteriaNames.length == 0){
            System.out.println("\t# " + "There aren't applied VC");
            return;
        }
        
        for(int i = 0; i <= allApplyViewCriteriaNames.length  - 1; i++) {
          System.out.println("\t# " + (i + 1) + ") " + allApplyViewCriteriaNames[i]);
        }
        
    }
    
    // Print ViewCriteria Items
    
//    public static void printViewCriteriaItems(ViewCriteria vc) {
//        
//        List list = vc.getRows(); 
//        
//        Iterator iter1 = list.iterator();
//        while (iter1.hasNext()) {
//               
//              ViewCriteriaRow row = (ViewCriteriaRow)iter1.next();
//
//              List vcitems = row.getCriteriaItems();
//              Iterator itemiter = vcitems.iterator();
//
//             while (itemiter.hasNext()) {
//                          
//               ViewCriteriaItem vcitem = (ViewCriteriaItem)itemiter.next();
//               System.out.println("vcitemname in vcrow: " + vcitem.getName());
//               System.out.println("vcitemvalue in vcrow: " + vcitem.getValue().toString());
//             }
//        }
//    }

} // The End of Class;
