package view.util;

import oracle.adf.model.binding.DCIteratorBinding;

import oracle.jbo.ApplicationModule;
import oracle.jbo.SortCriteria;
import oracle.jbo.uicli.binding.JUApplication;

public class AgileDataControl extends JUApplication{

    @Override
    protected void applySortCriteria(DCIteratorBinding iter, SortCriteria[] sortBy) {
        //Some job
    }

    //We consider transaction as dirty only if BC transaction is dirty
    //all manipulations with transient VOs/attributes should not matter

   public boolean isTransactionDirty() {
        ApplicationModule am = getApplicationModule();
        return (am != null && am.getTransaction() != null && am.getTransaction().isDirty());
    } 

}
