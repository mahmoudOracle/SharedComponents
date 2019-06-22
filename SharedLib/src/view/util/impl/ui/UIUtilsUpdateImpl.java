package view.util.impl.ui;

import javax.faces.component.UIComponent;

import javax.faces.context.FacesContext;

import oracle.adf.view.faces.bi.component.chart.UIBarChart;
import oracle.adf.view.faces.bi.component.gantt.UIProjectGantt;
import oracle.adf.view.rich.component.rich.data.RichListView;
import oracle.adf.view.rich.component.rich.fragment.RichPageTemplate;
import oracle.adf.view.rich.component.rich.fragment.RichRegion;
import oracle.adf.view.rich.component.rich.input.RichSelectItem;
import oracle.adf.view.rich.component.rich.layout.RichPanelBox;
import oracle.adf.view.rich.component.rich.layout.RichPanelFormLayout;
import oracle.adf.view.rich.component.rich.layout.RichPanelGroupLayout;
import oracle.adf.view.rich.component.rich.layout.RichPanelStretchLayout;
import oracle.adf.view.rich.component.rich.layout.RichShowDetailItem;
import oracle.adf.view.rich.component.rich.output.RichOutputText;
import oracle.adf.view.rich.component.rich.input.RichSelectManyCheckbox;
import oracle.adf.view.rich.component.rich.nav.RichButton;
import oracle.adf.view.rich.context.AdfFacesContext;

public class UIUtilsUpdateImpl {
    
    public static void updateUIByUUID(String uiID){
    //  System.out.println("uiID " + uiID);
        FacesContext fctx =  FacesContext.getCurrentInstance();
        UIComponent uic = fctx.getViewRoot().findComponent(uiID);
        updateUIByUUID(uic);
    }
    
    public static void updateUIByUUID(UIComponent uic){

        if (uic == null) {
            uicISNull();
            return;
        } 
        
        else if (uic instanceof RichOutputText){
            updateRichOutputTextUIC(uic);
        } 
        
        else if (uic instanceof RichButton){// import the button package it is changed from above
            updateRichButtonUIC(uic);
        }
        
        else if(uic instanceof RichSelectItem){
            updateRichSelectItemByUIC(uic);
        } 
        
        else if(uic instanceof RichSelectManyCheckbox){            
            updateRichSelectManyCheckboxByUIC(uic);
        } 
        
        else if(uic instanceof RichRegion){
            updateRegionByUIC(uic);
        } else
        
        if(uic instanceof UIProjectGantt){
            updateGanttByUIC(uic);
        } 
        
        else if(uic instanceof RichPanelFormLayout){
            updateRichPanelFormLayoutByUIC(uic);
        } 
        
        else if(uic instanceof RichPanelStretchLayout){            
            updateRichPanelStretchLayoutByUIC(uic);
        }
        
        else if(uic instanceof RichPanelGroupLayout){
            updateRichPanelGroupLayoutByUIC(uic);
        } 
        
        else if (uic instanceof UIBarChart){
            updateUIBarChartByUIC(uic);
        } 
        
        else if (uic instanceof RichPageTemplate){
            updateRichPageTemplateByUIC(uic);
        } 
        
        else if (uic instanceof RichPanelBox){
            updateRichPanelBoxByUIC(uic);
        }
        
        else if (uic instanceof RichShowDetailItem){
            updateRichShowDetailItemByUIC(uic);
        } 
        
        else if (uic instanceof RichListView){
            updateRichListViewByUIC(uic);
        } 
        
        else {
            throw new RuntimeException("NO Method FOUND TO UPDATE UIC");
        }
    }
    
    private static void updateRichOutputTextUIC(UIComponent uic){
            RichOutputText rot = (RichOutputText)uic;
            AdfFacesContext.getCurrentInstance().addPartialTarget(rot);
    }
    
    private static void updateRichButtonUIC(UIComponent uic){
            RichButton rb = (RichButton)uic;
            AdfFacesContext.getCurrentInstance().addPartialTarget(rb);
    }
    
    private static void updateRichSelectItemByUIC(UIComponent uic){
            RichSelectItem rsi = (RichSelectItem)uic;
            AdfFacesContext.getCurrentInstance().addPartialTarget(rsi);
    }
    
    private static void updateRegionByUIC(UIComponent uic){
            RichRegion rr = (RichRegion)uic;
            AdfFacesContext.getCurrentInstance().addPartialTarget(rr);
    }
    
    private static void updateGanttByUIC(UIComponent uic){
        UIProjectGantt gantt = (UIProjectGantt) uic;
        AdfFacesContext.getCurrentInstance().addPartialTarget(gantt);
    }   
    
    private static void updateRichPanelFormLayoutByUIC(UIComponent uic){
        RichPanelFormLayout rpfl = (RichPanelFormLayout) uic;
        AdfFacesContext.getCurrentInstance().addPartialTarget(rpfl);
    }
    
    private static void updateRichPanelStretchLayoutByUIC(UIComponent uic){
        RichPanelStretchLayout rpsl = (RichPanelStretchLayout) uic;
        AdfFacesContext.getCurrentInstance().addPartialTarget(rpsl);
    }
    
    private static void updateRichPanelGroupLayoutByUIC(UIComponent uic){
        RichPanelGroupLayout rpg = (RichPanelGroupLayout) uic;
        AdfFacesContext.getCurrentInstance().addPartialTarget(rpg);
    } 
    
    private static void updateUIBarChartByUIC(UIComponent uic){
        UIBarChart bc = (UIBarChart) uic;
        AdfFacesContext.getCurrentInstance().addPartialTarget(bc);
    }
    
    private static void updateRichPageTemplateByUIC(UIComponent uic){
        RichPageTemplate rpt = (RichPageTemplate) uic;
        AdfFacesContext.getCurrentInstance().addPartialTarget(rpt);
    }
    
    private static void updateRichPanelBoxByUIC(UIComponent uic){
        RichPanelBox rpb = (RichPanelBox) uic;
        AdfFacesContext.getCurrentInstance().addPartialTarget(rpb);
    }
    
    private static void updateRichShowDetailItemByUIC(UIComponent uic){
        RichShowDetailItem rsdi = (RichShowDetailItem) uic;
        AdfFacesContext.getCurrentInstance().addPartialTarget(rsdi);
    }
    
    private static void updateRichListViewByUIC(UIComponent uic){
        RichListView rlv = (RichListView) uic;
        AdfFacesContext.getCurrentInstance().addPartialTarget(rlv);
    }
    
    private static void updateRichSelectManyCheckboxByUIC(UIComponent uic){
        RichSelectManyCheckbox rsmcb = (RichSelectManyCheckbox) uic;
        AdfFacesContext.getCurrentInstance().addPartialTarget(rsmcb);
    }
    
    // Exception
    
    private static void uicISNull() {
        new RuntimeException();
    }
    
    
} // The End of Class;
