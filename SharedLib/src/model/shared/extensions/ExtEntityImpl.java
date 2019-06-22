package model.shared.extensions;

import model.shared.exceptions.DatabaseException;
import model.shared.exceptions.ExtAttrValException;
import model.shared.exceptions.messages.BundleUtils;

import oracle.jbo.server.EntityImpl;
import oracle.jbo.AttrValException;
import oracle.jbo.AttributeDef;
import oracle.jbo.AttributeList;
import oracle.jbo.DMLException;
import oracle.jbo.JboException;
import oracle.jbo.server.SequenceImpl;
import oracle.jbo.server.TransactionEvent;
import oracle.jbo.domain.Number;

import java.io.IOException;

import oracle.adf.share.ADFContext;

import oracle.jbo.Key;
import oracle.jbo.RowIterator;
import oracle.jbo.domain.Date;

import oracle.jbo.server.ViewObjectImpl;
import oracle.jbo.Row;
import oracle.jbo.Variable;
import oracle.jbo.common.VariableImpl;

public class ExtEntityImpl extends EntityImpl {
    static final String CREATESEQ_PROPERTY = "CreateSequence";
    static final String COMMITSEQ_PROPERTY = "CommitSequence";

    /**
     * Recipe: Using a custom property to populate a sequence attribute.
     *
     * Populates entity attributes from a database sequence defined generically using a
     * CREATESEQ_PROPERTY custom property.
     *
     * @param attributeList, the list of Entity object attributes.
     */
    @Override
    protected void create(AttributeList attributeList) {
        super.create(attributeList);
        // iterate all entity attributes
        for (AttributeDef atrbDef : this.getEntityDef().getAttributeDefs()) {
            // construct the custom property name from the entity name and attribute
            String propertyName = CREATESEQ_PROPERTY; //+ getEntityDef().getName() + atrbDef.getName();
            // check for a custom property called CREATESEQ_PROPERTY
            String sequenceName = (String) atrbDef.getProperty(propertyName);
            if (sequenceName != null) {
                // create the sequence based on the custom property sequence name
                SequenceImpl sequence = new SequenceImpl(sequenceName, this.getDBTransaction());
                // populate the attribute with the next sequence number
                Number seqNum = sequence.getSequenceNumber();
                this.populateAttributeAsChanged(atrbDef.getIndex(), seqNum);
                //setAttribute(atrbDef.getIndex(), seqNum);

            }
        }
    }

    /**
     * Recipe: Overriding doDML() to populate an attribute with a gapless sequence.
     *
     * @param operation, the DML operation.
     * @param transactionEvent, the transaction event.
     */
    
    /* @Override
       protected void doDML(int operation, TransactionEvent transactionEvent) {

           // check for insert operation
           if (DML_INSERT == operation) {
               // iterate all entity attributes
               for (AttributeDef atrbDef :
                    this.getEntityDef().getAttributeDefs()) {
                   // construct the custom property name from the entity name and attribute
                   String propertyName =
                       COMMITSEQ_PROPERTY; //+ getEntityDef().getName() +
                       atrbDef.getName();
                   // check for a custom property called COMMITSEQ_PROPERTY
                   String sequenceName =
                       (String)atrbDef.getProperty(propertyName);
                   if (sequenceName != null) {
                       // create the sequence based on the custom property sequence name
                       SequenceImpl sequence =
                           new SequenceImpl(sequenceName, this.getDBTransaction());
                       // populate the attribute with the next sequence number
                       this.populateAttributeAsChanged(atrbDef.getIndex(),
                                                       sequence.getSequenceNumber());
                   }
               }
           }

           super.doDML(operation, transactionEvent);
       } */


    @Override
    protected void doDML(int operation, TransactionEvent transactionEvent) {

        // check for insert operation
        if (DML_INSERT == operation) {
            // iterate all entity attributes
            for (AttributeDef atrbDef : this.getEntityDef().getAttributeDefs()) {
                // construct the custom property name from the entity name and attribute
                String propertyName = COMMITSEQ_PROPERTY; //+ getEntityDef().getName() + atrbDef.getName();
                // check for a custom property called COMMITSEQ_PROPERTY
                String sequenceName = (String) atrbDef.getProperty(propertyName);

                if (sequenceName != null) {
                    // create the sequence based on the custom property sequence name
                    SequenceImpl sequence = new SequenceImpl(sequenceName, this.getDBTransaction());
                    // populate the attribute with the next sequence number
                    Number seqNum = sequence.getSequenceNumber();
                    //this.populateAttributeAsChanged(atrbDef.getIndex(), seqNum);
                    this.populateAttributeAsChanged(atrbDef.getIndex(), sequence.getSequenceNumber());
                    
                }
            }
        }

        try {
            super.doDML(operation, transactionEvent);
        } catch (DMLException dmlException) {
            // Recipe: Handling and customizing database-related errors.
            throw new DatabaseException((Exception) dmlException.getDetails()[0]);
        } catch (JboException jboException) {
            throw jboException;
        } catch (Exception e) {
            e.printStackTrace();
        }
    } 

    // Calling the Below Method by super.isAttrValueChanged(this.LASTNAME);

    /**
     * Recipe: Using getPostedAttribute() to determine the posted attribute's value.
     *
     * Check if attribute's value differs from its posted value
     * @param attrIdx the attribute index
     * @return
     */
    protected boolean isAttrValueChanged(int attrIdx) {
        // get the attribute's posted value
        Object postedValue = getPostedAttribute(attrIdx);
        // get the attribute's current value
        Object newValue = getAttributeInternal(attrIdx);
        // return true is attribute value differs from its posted value
        return isAttributeChanged(attrIdx) &&
               ((postedValue == null && newValue != null) || (postedValue != null && newValue == null) ||
                (postedValue != null && newValue != null && !newValue.equals(postedValue)));
    }

    /**
     * Recipe: Using Groovy expressions to resolve validation error message tokens.
     *
     * Returns a parameter value from the parameters bundle using the parameter
     * key passed as an argument.
     *
     * @param parameterKey, the parameter key
     * @return the parameter value from the parameters bundle
     */
    public String getBundleParameter(String parameterKey) {
        // use BundleUtils to load the parameter
        return BundleUtils.loadParameter(parameterKey);
    }

    /**
     * Recipe: Overriding attribute validation exceptions.
     *
     * Throws custom ExtAttrValException attribute validation exception.
     *
     * @param attrib, the attribute index
     * @param value, the attribute value
     */
    protected void setAttributeInternal(int attrib, Object value) {
        try {
            super.setAttributeInternal(attrib, value);
        } catch (AttrValException e) {
            // throw custom attribute validation exception
            throw new ExtAttrValException(e.getErrorCode(), e.getErrorParameters());
        }
    }

    public Number getSequenceValue(String SequenceName) {
        SequenceImpl MySeq = new SequenceImpl(SequenceName, getDBTransaction());
        return MySeq.getSequenceNumber();
    }


    public static String RPad(String str, Integer length, char car) {
        return str + String.format("%" + (length - str.length()) + "s", "").replace(" ", String.valueOf(car));
    }

    public static String LPad(String str, Integer length, char car) {
        return String.format("%" + (length - str.length()) + "s", "").replace(" ", String.valueOf(car)) + str;
    }

    public Date ConvertFromSQLDateToJboDate(java.sql.Date mydate) {
        return new Date(mydate);
    }

    public Date ConvertFromUtilDateToJboDate(java.util.Date mydate) {
        java.sql.Date mysqldate = this.ConvertFromUtilDateToSqlDate(mydate);
        Date jboDate = this.ConvertFromSQLDateToJboDate(mysqldate);
        return jboDate;
    }

    public java.sql.Date ConvertFromJboDateToSqlDate(Date mydate) {
        return mydate.dateValue();
    }

    public java.util.Date ConvertFromSQLDateToUtilDate(java.sql.Date mydate) {
        java.util.Date myutildate = new java.util.Date(mydate.getTime());
        return myutildate;
    }

    public java.sql.Date ConvertFromUtilDateToSqlDate(java.util.Date mydate) {
        java.sql.Date mysqldate = new java.sql.Date(mydate.getTime());
        return mysqldate;
    }

    public java.util.Date ConvertFromJboDateToUtilDate(Date mydate) {
        java.sql.Date mysqldate = this.ConvertFromJboDateToSqlDate(mydate);
        java.util.Date myutildate = this.ConvertFromSQLDateToUtilDate(mysqldate);
        return myutildate;
    }

    public Object[] retrievetransactionDetails() { // this method shall be called from doDml Method in eache EO
        Object[] _result = new Object[] { ADFContext.getCurrent()
                                                    .getSessionScope()
                                                    .get("USERID"), ConvertFromUtilDateToJboDate(new java.util.Date())
        };

        if (_result[0] == null) {
            _result[0] = "Unknown User !";
        }
        return _result;
    }

    public boolean AuthUserProcess(String _username, String _password) {
        boolean _result = false;
        EncryptionDecryption enc = new EncryptionDecryption();
        if (!_username.isEmpty() && !_password.isEmpty()) {

            RowIterator AllUsers;
            try {
                AllUsers =
                    ((ViewObjectImpl) getDBTransaction().findViewObject("AllUsers"))
                    .findByAltKey("UserPassAltKey",
                                  new Key(new Object[] { _username, enc.EncodeDecode(_password, 1), "A" }), 1, true);
                if (AllUsers.getRowCount() > 0) {
                    _result = true;
                }
            } catch (IOException e) {
                throw new JboException("Sorry, Error while Encrypting User Password : " + e.getMessage());
            }
        }
        return _result;
    }

    public void UpdateUserPassword(String _username, String newPass) {
        EncryptionDecryption enc = new EncryptionDecryption();
        if (!_username.isEmpty()) {

            Row[] AllUsers =
                ((ViewObjectImpl) getDBTransaction().findViewObject("AllUsers"))
                .findByKey(new Key(new Object[] { _username }), 1);
            if (AllUsers.length > 0) {
                AllUsers[0].setAttribute("Password", newPass);
            }
        }
    }

    public void CreateNewUserFromRequest(String reqID) {

        Row[] req =
            ((ViewObjectImpl) getDBTransaction().findViewObject("UserRequests1"))
            .findByKey(new Key(new Object[] { reqID }), 1);
        if (req.length > 0) {
            Row UserRow = ((ViewObjectImpl) getDBTransaction().findViewObject("AllUsers")).createRow();
            UserRow.setAttribute("FirstName", req[0].getAttribute("FirstName"));
            UserRow.setAttribute("LastName", req[0].getAttribute("LastName"));
            UserRow.setAttribute("Password", req[0].getAttribute("Password"));
            UserRow.setAttribute("NationalId", req[0].getAttribute("NationalId"));
            UserRow.setAttribute("Country", req[0].getAttribute("Country"));
            UserRow.setAttribute("EduDegree", req[0].getAttribute("EduDegree"));
            UserRow.setAttribute("Dob", req[0].getAttribute("Dob"));
            UserRow.setAttribute("HireDate", req[0].getAttribute("HireDate"));
            UserRow.setAttribute("Gender", req[0].getAttribute("Gender"));
            UserRow.setAttribute("ExpireDate", req[0].getAttribute("ExpireDate"));
            UserRow.setAttribute("PhoneNo", req[0].getAttribute("PhoneNo"));
            UserRow.setAttribute("EmailAcc", req[0].getAttribute("EmailAcc"));
            UserRow.setAttribute("FaxNo", req[0].getAttribute("FaxNo"));
            UserRow.setAttribute("SkypeAcc", req[0].getAttribute("SkypeAcc"));
            UserRow.setAttribute("GoogleAcc", req[0].getAttribute("GoogleAcc"));
            UserRow.setAttribute("LinkedinAcc", req[0].getAttribute("LinkedinAcc"));
            UserRow.setAttribute("InstegramAcc", req[0].getAttribute("InstegramAcc"));
            UserRow.setAttribute("FbAcc", req[0].getAttribute("FbAcc"));
            UserRow.setAttribute("SkypeAcc", req[0].getAttribute("SkypeAcc"));
            ((ViewObjectImpl) getDBTransaction().findViewObject("AllUsers")).insertRow(UserRow);
        }

    }

    public void AutoNotification(String _fromuser, String _touser, String message) {
        Row NotRow = ((ViewObjectImpl) getDBTransaction().findViewObject("Notifications1")).createRow();
        NotRow.setAttribute("Type", "A");
        NotRow.setAttribute("FromUser", _fromuser);
        NotRow.setAttribute("ToUser", _touser);
        NotRow.setAttribute("Notes", message);
        ((ViewObjectImpl) getDBTransaction().findViewObject("Notifications1")).insertRow(NotRow);
    }


    public boolean isRoleAlreadyExists(String _roleid, String _user) {
        boolean _result = false;
        RowIterator sub =
            ((ViewObjectImpl) getDBTransaction().findViewObject("UserRoles1"))
            .findByAltKey("RoleUserAltKey", new Key(new Object[] { _roleid, _user }), 1, true);

        if (sub.getRowCount() > 0) {
            _result = true;
        }
        return _result;
    }

    public void assignDelgationRole(String RoleID, String _fromuser, String _touser) {
        ViewObjectImpl Roles = (ViewObjectImpl) getDBTransaction().findViewObject("UserRoles1");
        Row MyRow = Roles.createRow();
        MyRow.setAttribute("UserId", _touser);
        MyRow.setAttribute("RoleId", RoleID);
        MyRow.setAttribute("SubId", _fromuser);
        MyRow.setAttribute("Active", "A");
        Roles.insertRow(MyRow);
    }

    public void DelgateFromUserToAnother(String _fromuser, String _touser) {

        VariableImpl name1 = new VariableImpl("PUserID");
        VariableImpl name2 = new VariableImpl("PStatus");
        ViewObjectImpl Roles = (ViewObjectImpl) getDBTransaction().findViewObject("UserRoles1");
        RowIterator MyRoles =
            Roles.findByViewCriteriaWithBindVars(Roles.getViewCriteria("UserRolesVOCriteria"), -1,
                                                 Roles.QUERY_MODE_SCAN_DATABASE_TABLES, new Variable[] { name1, name2 },
                                                 new Object[] { _fromuser, "A" });

        if (MyRoles.getRowCount() > 0) {
            Row MyRow;
            while (MyRoles.hasNext()) {
                MyRow = MyRoles.next();
                if (!isRoleAlreadyExists(MyRow.getAttribute("RoleId").toString(), _touser)) {
                    assignDelgationRole(MyRow.getAttribute("RoleId").toString(), _fromuser, _touser);
                }
            }
        }
    }

    public void cancelDelgation(String _fromuser, String _touser) {

        VariableImpl name1 = new VariableImpl("PSubUserID");
        VariableImpl name2 = new VariableImpl("PSSubUserID");
        ViewObjectImpl Roles = (ViewObjectImpl) getDBTransaction().findViewObject("UserRoles1");
        RowIterator MyRoles =
            Roles.findByViewCriteriaWithBindVars(Roles.getViewCriteria("UserRolesVOCriteria1"), -1,
                                                 Roles.QUERY_MODE_SCAN_DATABASE_TABLES, new Variable[] { name1, name2 },
                                                 new Object[] { _fromuser, _touser });

        if (MyRoles.getRowCount() > 0) {
            Row MyRow;
            while (MyRoles.hasNext()) {
                MyRow = MyRoles.next();
                MyRow.remove();
            }
        }


    }

    @Override
    protected void initDefaults() {
        super.initDefaults();
        for (AttributeDef atrbDef : this.getEntityDef().getAttributeDefs()) {
            // construct the custom property name from the entity name and attribute
            String propertyName = CREATESEQ_PROPERTY + getEntityDef().getName() + atrbDef.getName();
            // check for a custom property called CREATESEQ_PROPERTY
            String sequenceName = (String) atrbDef.getProperty(propertyName);
            if (sequenceName != null) {
                this.populateAttributeAsChanged(atrbDef.getIndex(), -1);
            }
        }
    }
}
