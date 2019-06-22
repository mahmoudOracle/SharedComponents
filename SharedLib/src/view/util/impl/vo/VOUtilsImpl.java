package view.util.impl.vo;

import java.util.Enumeration;
import java.util.Hashtable;

import oracle.jbo.AttributeDef;
import oracle.jbo.Row;
import oracle.jbo.StructureDef;
import oracle.jbo.Variable;
import oracle.jbo.VariableValueManager;
import oracle.jbo.ViewObject;

public class VOUtilsImpl {

    private static final int MAX_PRINT = 25;    
    
    /**
     * @return void
     */
    public static void printViewObjectInfo(ViewObject vo){
        
        System.out.println();
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        System.out.println("# Data Control : " + vo.getName());
        
        printViewObjectSQLQuery(vo);
        printViewObjectVariables(vo);
        printViewObjectDataInfo(vo);
        // printViewObjectAttributes(vo);
        // printViewObjectAttributeValue(vo, "myAttr");
        printViewObjectData(vo);
        
       
       // Check again to find solution of it 18-01-2019
       
      //  VCUtilsPrintInfoImpl.printViewCriteriaNames(vo);
      //  VCUtilsPrintInfoImpl.printApplyViewCriteriaNames(vo);
        
        System.out.println();
        System.out.println("# Data Control : " + vo.getName());
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
    }
    
    /**
     * @return void
     */
    private static void printViewObjectSQLQuery(ViewObject vo){
           System.out.println();
           System.out.println("==================");
           System.out.println("=== Query: ====");
           System.out.println(vo.getQuery());
           System.out.println("==================");
    }
    

    /**
     * @return void
     */
    private static void printViewObjectVariables(ViewObject vo){
        
        System.out.println();
        System.out.println("  Variables List:");
        System.out.println();
        
        Hashtable<String,String> myHashTable = getFieldsNamesAndValues(vo);
        
        Enumeration names;
        String key;
        
        names = myHashTable.keys();
          while(names.hasMoreElements()) {
             key = (String) names.nextElement();
             System.out.println("\t# [Name:] " + key + " [Value:] " +
             myHashTable.get(key));
          }
        
    }
    

     /**
      * @return Hashtable
      */
    public static Hashtable getFieldsNamesAndValues (ViewObject vo){
        
        VariableValueManager lEnsureVariableManager = vo.ensureVariableManager();
        Variable[] lVariables = lEnsureVariableManager.getVariables();
        int lCount = lEnsureVariableManager.getVariableCount();


        Hashtable<String,String> myHashTable = new Hashtable<String,String>();
                           
        // if variables found dump them
        if (lCount > 0)
        {
            
            for (int i = 0; i < lCount; i++)
            {
                Object lObject = lEnsureVariableManager.getVariableValue(lVariables[i]);
                
                myHashTable.put(lVariables[i].getName(), (lObject != null ?  lObject.toString() : "null"));
                
            }
        }
        
        return myHashTable;
    }
    
    private static void printViewObjectDataInfo(ViewObject vo){
           System.out.println();
           System.out.println("___________________");
           System.out.println("Rows Count " + vo.getRowCount());
           System.out.println("___________________");
           System.out.println();  
    }
    
    
    private static void printViewObjectAttributes(ViewObject vo){
           System.out.println();
           System.out.println("=== ViewObject ATTRIBUTES =====");
           
            vo.reset();
            vo.first();
            Row row = vo.getCurrentRow();
            
            String viewObjName = vo.getName();
                System.out.println("Printing attribute for a row in VO '"+ viewObjName+"'");
                StructureDef def = row.getStructureDef();
                StringBuilder sb = new StringBuilder();
                int numAttrs = def.getAttributeCount();
                AttributeDef[] attrDefs = def.getAttributeDefs();
                for (int z = 0; z < numAttrs; z++) {
                  Object value = row.getAttribute(z);
                  sb.append(z > 0 ? "  " : "")
                    .append(attrDefs[z].getName())
                    .append("=")
                    .append(value == null ? "<null>" : value)
                    .append(z < numAttrs - 1 ? "\n" : "");
                }
                System.out.println(sb.toString());
    
                System.out.println("=== NO MORE ATTRIBUTES =====");
    }
    
    
    private static void printViewObjectData(ViewObject vo){
        
        int maxRowForPrint = MAX_PRINT;
        
           System.out.println();
           System.out.println("=== ViewObject DATA =====");

           vo.reset();
           vo.first();
           
        System.out.println("=== VO DATA =====");
        
        
           while ((vo.getCurrentRow() != null) && (maxRowForPrint > 0)) {
               
                Row row = vo.getCurrentRow();
                String rowDataStr = "";
               
                int numAttrs = vo.getAttributeCount();
               
                for (int columnNo = 0; columnNo < numAttrs; columnNo++){
                     Object attrData = row.getAttribute(columnNo);
                     rowDataStr += (attrData + "\t");
                  }
               
                  System.out.println((vo.getCurrentRowIndex() + 1) + ") " + rowDataStr);
               
               vo.next();
               
                maxRowForPrint--;
            }

            System.out.println("=== NO MORE DATA =====");
    }
    
    
    private static void printViewObjectAttributeValue(ViewObject vo, String attrName){
        
        int maxRowForPrint = MAX_PRINT;
        
         vo.reset();
         vo.first();
           
        System.out.println("=== DATA FROM VO FOR ATTRIBUTE " + attrName + "====== BEGIN");
        
        
           while ((vo.getCurrentRow() != null) && (maxRowForPrint > 0)) {
               
                Row row = vo.getCurrentRow();
                String rowDataStr = "";

                     Object attrData = row.getAttribute(attrName);
                     rowDataStr += (attrData + "\t");
               
                  System.out.println((vo.getCurrentRowIndex() + 1) + ") " + rowDataStr);
               
               vo.next();
               
                maxRowForPrint--;
            }

             System.out.println("=== DATA FROM VO FOR ATTRIBUTE " + attrName + "===== END");
       
    }

    
} // The End of Class;
