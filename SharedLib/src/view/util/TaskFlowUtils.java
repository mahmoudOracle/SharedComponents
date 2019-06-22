package view.util;

import oracle.adf.controller.ControllerContext;
import oracle.adf.controller.TaskFlowContext;
import oracle.adf.controller.TaskFlowId;
import oracle.adf.controller.ViewPortContext;

public class TaskFlowUtils {
    
    
    public static void printTaskFlowInformation() {
        
        TaskFlowId tfId = TaskFlowUtils.getTaskFlowId();
        
        if (tfId !=null){
            System.out.println("  [ Locat Task Flow Id] " + tfId.getLocalTaskFlowId());
            System.out.println("  [ Dockument Name] " + tfId.getDocumentName());
            System.out.println("  [ Fully Qualified Task Flow Name] " + tfId.getFullyQualifiedName());
        } else {
            System.out.println("  [NO Task Flow Information] ");
        }
    }
    
    private static TaskFlowId getTaskFlowId() {
        ControllerContext controllerContext = ControllerContext.getInstance();
        ViewPortContext currentViewPort = controllerContext.getCurrentViewPort();
        TaskFlowContext taskFlowContext = currentViewPort.getTaskFlowContext();
        TaskFlowId taskFlowId = taskFlowContext.getTaskFlowId();
        return taskFlowId;
    }
    
} // The End of Class;
