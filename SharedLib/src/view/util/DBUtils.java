package view.util;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import oracle.jbo.server.DBTransaction;
import oracle.jbo.server.DBTransactionImpl;
import oracle.jbo.server.ApplicationModuleImpl;
import view.util.impl.vo.CONSTANTS_VO;
public class DBUtils {
    
    /** function return application database transaction object */
    // takes the defaulta applicaiton Module -- we can add code for named app module >>> getApplicationModuleForDataControl
    public static  DBTransaction getDefaultDBTransaction() {
        return (DBTransaction) IteratorUtils.getDefaultApplicationModule().getTransaction();
    }

    public static  DBTransaction getDBTransaction(){
        ApplicationModuleImpl am = (ApplicationModuleImpl) ADFUtils.getApplicationModuleForDataControl(CONSTANTS_VO.AppModuleName);
        DBTransaction db = null;
        db = am.getDBTransaction();            
        return db;
    }


/** function return current application database connection */
  public static  Connection getConnection()
  {
    DBTransactionImpl dbt = (DBTransactionImpl) getDefaultDBTransaction();
    Connection conn = dbt.getPersistManagerConnection();
    return conn;
  }

  
/** function used to close preparedStatement */
  public static void close(PreparedStatement stat)
  {
    try
    {
      if(stat != null)
      {
        stat.close();
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

/** function used to close ResultSet */
  public static  void close(ResultSet rs)
  {
    try
    {
      if(rs != null)
      {
        rs.close();
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

/** function used to close CallableStatement */
  public static  void close(java.sql.CallableStatement cstat)
  {
    try
    {
      if(cstat != null)
      {
        cstat.close();
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
           
/** function take a double number and integer number then round the double number with the integer number (e.g  roundToDecimals(15.22545112, 3) then the output will be 15.225) */ 
  public static  double roundToDecimals(double d, int c)
  {
    int temp = (int) ((d * Math.pow(10, c)));
    return (((double) temp) / Math.pow(10, c));
  }

/** function take sql statement and return the result value e.g (getSqlDescription("select username from users where user_id=10") --> function will return username value) */
  public static  String getSqlDescription(String sql)
  {
    PreparedStatement stat = null;
    ResultSet rs = null;
    try
    {
      stat = getDefaultDBTransaction().createPreparedStatement(sql, 1);
      rs = stat.executeQuery();
      while (rs.next())
      {
        return rs.getString(1);
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      close(rs);
      close(stat);
    }
    return "";
  }

  /** function take sql statement and return the result as double */
  public static  double getSqlAsDuoble(String sql)
  {
    PreparedStatement stat = null;
    ResultSet rs = null;
    try
    {
      stat = getDefaultDBTransaction().createPreparedStatement(sql, 1);
      rs = stat.executeQuery();
      while (rs.next())
      {
        return rs.getDouble(1);
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      close(rs);
      close(stat);
    }
    return 0;
  }
  
  
  /** function take sql statement and return the result as long */
  public static  long getSqlAsLong(String sql)
  {
    PreparedStatement stat = null;
    ResultSet rs = null;
    try
    {
      stat = getDefaultDBTransaction().createPreparedStatement(sql, 1);
      rs = stat.executeQuery();
      while (rs.next())
      {
        return rs.getLong(1);
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      close(rs);
      close(stat);
    }
    return 0;
  }

  

    // For Information
    
//    public static  DBTransaction getDBTransaction(){
//        BindingContext bindingContext = BindingContext.getCurrent();
//        DCDataControl dc = bindingContext.getDefaultDataControl();
//        AppModuleImpl am = (AppModuleImpl) dc.getDataProvider();
//        DBTransaction dbT = am.getDBTransaction();
//        return dbT;
//    }
    

    
} // The End of Class;
