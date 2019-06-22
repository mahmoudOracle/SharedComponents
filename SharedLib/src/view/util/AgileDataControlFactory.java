package view.util;

import oracle.adf.model.bc4j.DataControlFactoryImpl;


public class AgileDataControlFactory extends DataControlFactoryImpl {

    /*     Specify custom data control factory in the DataBindings.cpx file:
    <BC4JDataControl id="AgileDataModelServiceDataControl"
                     Package="agiledatamodel"
                    FactoryClass="agiledatamodel.datacontrol.AgileDataControlFactory" */
                        
                        
//Create custom data control factory returning the class name of your custom data control:
   
    @Override
    protected String getDataControlClassName() {
        return AgileDataControl.class.getName();
    }
}
