package model.shared.extensions;

/**
 * Recipe: Creating and using generic extension interfaces.
 */
public interface ExtApplicationModule {
    // return some user authority level, based on the user's name    
    public int getUserAuthorityLevel();
    // get the Application Module statistics
    public void getAMPoolStatistics();
}
