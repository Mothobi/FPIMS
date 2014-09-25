
package servlets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import database.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Administrator
 */
public class Tools {

    private DbDetail dbdetail;

    /**
     * constructor for an instance of Tools it initializes the database connection settings
     */
    public Tools() {

 
        dbdetail = new DbDetail("localhost", "/mydb", "root", "password");
 
    }
    //end constructor
 
   
    /**
     * This is a method to manually add users into the database for testing purposes
     * @return A string indicating the success of failure of adding users
     */
    public String adduser(){
        Employee e = new Employee("11111111", "password", "User", "UserSurname", "Admin", 4, "user1@user.com", true);
        Employee e2 = new Employee("12345678", "123456", "User2", "UserSurname2", "Pathologist", 3, "user2@user.com", true);
        EmployeeDb db1 = new EmployeeDb(e, getDbdetail());
        db1.init();
        EmployeeDb db2 = new EmployeeDb(e2, getDbdetail());
        db2.init();
        db1.add();  
        return (db2.add());
    }

    /**
     * Try to log in a user, if the login is successful then return true,
     * otherwise return false
     *
     * @param personnelnumber the personnel number of the user
     * @param password the user's password
     * @return the access level of the user or -1 for invalid user
     */
    public int logIn(String personnelnumber, String password, HttpSession sess) {
        //check the database for this user
        Employee emp = new Employee(personnelnumber, password);
        EmployeeDb empdb = new EmployeeDb(emp, getDbdetail());
        empdb.init();
        String status = empdb.read();
        if (status.contains("fail")) {
            this.makeAuditTrail("Warning", "Unsuccessfull log in", personnelnumber, "Log In Screen");
            return -1;
        } else {
            this.makeAuditTrail("Log In", "Successfull log in", personnelnumber, "Log In Screen");
            sess.setAttribute("employee", emp);
            int access = Integer.parseInt(status.substring(0, 1));
            return access;
        }
    }
    //end logIn

    /**
     * Create an audit trail entry. Given the following parameters
     *
     * @param eventtype the type of event that occurred eg. login failed
     * @param eventmessage the message that needs to be stored
     * @param user the user that caused this event
     *
     */
    public void makeAuditTrail(String eventtype, String eventmessage, String user, String location) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        String[] datetime = timestamp.split(" ");
        AuditTrail adt = new AuditTrail(datetime[0], datetime[1], eventtype, eventmessage, user, location);
        AuditTrailDb adb = new AuditTrailDb(adt, getDbdetail());
        adb.init();
        adb.add();
    }
    //end makeAuditTrail

    /**
     * This will return the Incident Log number for an incident
     *
     * @return Incident Log number or error message
     *
     */
    public String getIncidentLogNumber() {
        Incident incident = new Incident();
        IncidentDb incidentdb = new IncidentDb(incident, getDbdetail());
        incidentdb.init();        
        String timestamp2 = new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(Calendar.getInstance().getTime());
        String datenum = timestamp2.split(" ")[0];
        try {
            int incidentnum = incidentdb.countOpenIncidents(datenum) + 1;
            String formated_num = String.format("%03d", incidentnum);
            String lognumber = formated_num + datenum;
            return lognumber;
        } catch (Exception e) {
            return "Error could not generate incident log number. " + e.getMessage();
        }
    }
    //end getIncidentLogNumber
    
    /**
     * This method creates a death register number. The province and facility are hard coded.
     * @return A string containing a death register number
     */
    public String makeDeathRegisterNumber(){
        String deathregister = "GP/DK/";
        BodyFileDb bfdb = new BodyFileDb(dbdetail);
        String date = this.getDateTime().split(" ")[0];      
        bfdb.init();
        try{
            int count = bfdb.countOpenBodyFile() + 1;
            String formated_num = String.format("%05d", count);
            String year = date.split("-")[0];
            deathregister = deathregister+formated_num+"/"+year;
            return deathregister;
        }
        catch(SQLException e){
            return e.getMessage();
        }
            
    }

    /**
     * This method makes and html drop down list for years
     * @param name The name and id you want to give the list
     * @param year_num This indicates the default value for the list
     * @return A html string for a drop down list for years
     */
    public String makeYear(String name, int year_num) {
        String out = "<select id=" + name + " name=" + name + ">";
        if (year_num == -1) {
            out = out + "<option selected='selected'>Year</option>";
        }
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        String[] datetime = timestamp.split(" ");
        String date[] = datetime[0].split("-");
        String year = date[0];
        int intyear = Integer.parseInt(year);
        for (int i = intyear; i >= (intyear - 150); i--) {
            if (i == year_num) {
                out = out + "<option selected='selected'>" + Integer.toString(i) + "</option>";
            } else {
                out = out + "<option>" + Integer.toString(i) + "</option>";
            }
        }
        out = out + "</select>";
        return out;
    }
    //end makeYear

    /**
     * This method makes a drop down list for months
     * @param name The name and id you want to give the list
     * @param month_num The default value for the list
     * @return A html string of a drop down list for months
     */
    public String makeMonth(String name, int month_num) {
        String out = "<select id=" + name + " name=" + name + ">";
        if (month_num == -1) {
            out = out + "<option selected='selected'>Month</option>";
        }
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        for (int i = 1; i < 13; i++) {
            if (i == month_num) {
                out = out + "<option selected='selected' num=" + i + ">" + months[i - 1] + "</option>";
            } else {
                out = out + "<option num=" + i + ">" + months[i - 1] + "</option>";
            }
        }
        out = out + "</select>";
        return out;
    }

    /**
     * This method returns the number of a month
     * @param month The month that needs to be converted to a number
     * @return The number of the month
     */
    public int getMonthNumber(String month) {
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        int index = 0;
        while ((index < 12) && !(months[index].equals(month))) {
            index++;
        }
        return index + 1;
    }

    /**
     * This
     * @param name
     * @param day_num
     * @return 
     */
    public String makeDay(String name, int day_num) {
        String out = "<select id=" + name + " name=" + name + ">";
        if (day_num == -1) {
            out = out + "<option selected='selected'>Day</option>";
        }
        for (int i = 1; i < 32; i++) {
            if (i == day_num) {
                out = out + "<option  selected='selected'>" + Integer.toString(i) + "</option>";
            } else {
                out = out + "<option>" + Integer.toString(i) + "</option>";
            }
        }
        out = out + "</select>";
        return out;
    }

    public String makeHour(String name, int hour_num) {
        String out = "<select name=" + name + " id=" + name + ">";
        if (hour_num == -1) {
            out = out + "<option selected='selected'>Hour</option>";
        }
        for (int i = 0; i < 24; i++) {
            if (hour_num == i) {
                out = out + "<option selected='selected'>" + Integer.toString(i) + "</option>";
            } else {
                out = out + "<option>" + Integer.toString(i) + "</option>";
            }
        }
        out = out + "</select>";
        return out;
    }

    public String makeMinute(String name, int min_num) {
        String out = "<select name=" + name + " id=" + name + ">";
        if (min_num == -1) {
            out = out + "<option selected='selected'>Minute</option>";
        }
        for (int i = 0; i < 60; i++) {
            if (min_num == i) {
                out = out + "<option selected='selected'>" + Integer.toString(i) + "</option>";
            } else {
                out = out + "<option>" + Integer.toString(i) + "</option>";
            }
        }
        out = out + "</select>";
        return out;
    }

    /**
     * This returns the contents of a reference list from the database
     *
     * @param listname name of the table containing the list elements
     * @return returns the contents of a reference list from the database or an
     * error
     */
    public ArrayList<String> getReferenceList(String listname, String field) {
        ReferenceListDb refdb = new ReferenceListDb(getDbdetail(), listname);
        refdb.setField2(field);
        refdb.init();
        try {
            return refdb.referenceList();
        } catch (Exception e) {
            ArrayList<String> error = new ArrayList<String>();
            error.add("Error loading list: " + e.getMessage());
            return error;
        }
    }
    //end getReferenceList
    
    public String makeICD10List(String listname, String field1, String field2, String selected, String def) {
        ArrayList<String> list1;
        ArrayList<String> list2;
        list1 = this.getReferenceList(listname, field1);
        list2 = this.getReferenceList(listname, field2);
        String out = "<select name='" + listname + "' id='" + listname + "'>";
        if (selected.equals("")) {
            out = out + "<option selected='slected'>"+def+"</option>";
        }
        int size = list1.size();
        for (int i = 0; i < size; i++) {
            String element1 = list1.get(i);
            String element2 = list2.get(i);
            if (element1.equals(selected)) {
                out = out + "<option selected='selected'>" + element1 + " "+ element2 + "</option>";
            } else {
                out = out + "<option>" + element1 + " "+ element2 + "</option>";
            }
        }
        out = out + "</select>";
        return out;
    }

    public String makeICD10List(String listname, String field1, String field2, String field3, String selected, String def) {
        ArrayList<String> list1;
        ArrayList<String> list2;
        ArrayList<String> list3;
        list1 = this.getReferenceList(listname, field1);
        list2 = this.getReferenceList(listname, field2);
        list3 = this.getReferenceList(listname, field3);
        String out = "<select name='" + listname + "' id='" + listname + "'>";
        if (selected.equals("")) {
            out = out + "<option selected='slected'>"+def+"</option>";
        }
        int size = list1.size();
        for (int i = 0; i < size; i++) {
            String element1 = list1.get(i);
            String element2 = list2.get(i);
            String element3 = list3.get(i);
            if (element1.equals(selected)) {
                out = out + "<option selected='selected'>" + element1 + " "+ element2 + " (" + element3 + ") " + "</option>";
            } else {
                out = out + "<option>" + element1 + " "+ element2 + " (" + element3 + ") " + "</option>";
            }
        }
        out = out + "</select>";
        return out;
    }
    
    public String makeReferenceList(String listname, String field, String selected) {
        ArrayList<String> list = new ArrayList<String>();
        list = this.getReferenceList(listname, field);
        String out = "<select name='" + listname + "' id='" + listname + "'>";
        if (selected.equals("")) {
            out = out + "<option selected='slected'>Select</option>";
        }
        int size = list.size();
        for (int i = 0; i < size; i++) {
            String element = list.get(i);
            if (element.equals(selected)) {
                out = out + "<option selected='selected'>" + element + "</option>";
            } else {
                out = out + "<option>" + element + "</option>";
            }
        }
        out = out + "</select>";
        return out;
    }

    /**
     * This will create a table with all the open incidents from the database
     */
    public String makeOpenIncidentsTable(String id) {
        IncidentDb indb = new IncidentDb(getDbdetail());
        indb.init();
        try {
            ArrayList<Incident> openincidents = indb.openIncidentList();

            String table = "<table class='tabledisplay' id='" + id + "'>"
                    + "<th class='tableheading'>FPS Incident Log Number</th>"
                    + "<th class='tableheading'>SAPS/ IR number reference number</th>"
                    + "<th class='tableheading'>Date</th>"
                    + "<th class='tableheading'>Time</th>"
                    + "<th class='tableheading'>Number of bodies</th>"
                    + "<th class='tableheading'>Number of bodies recieved</th>"
                    + "<th class='tableheading'>Place Body was found</th>"
                    + "<th class='tableheading'>Circumstances of death</th>"
                    + "<th class='tableheading'>Special Circumstances</th>";
            int size = openincidents.size();
            for (int i = 0; i < size; i++) {
                Incident inc = openincidents.get(i);
                table = table + "<tr class='tablerow' lognumber='" + inc.getIncidentLogNumber() + "'>"
                        + "<td>" + inc.getIncidentLogNumber() + "</td>"
                        + "<td class='tablecell'>" + inc.getReferenceNumber() + "</td>"
                        + "<td class='tablecell'>" + inc.getDateOfIncident() + "</td>"
                        + "<td class='tablecell'>" + inc.getTimeOfIncident() + "</td>"
                        + "<td class='tablecell'>" + Integer.toString(inc.getNumberOfBodies()) + "</td>"
                        + "<td class='tablecell'>" + Integer.toString(inc.getBodyCount()) + "</td>"
                        + "<td class='tablecell'>" + inc.getPlaceBodyFound() + "</td>"
                        + "<td class='tablecell'>" + inc.getCircumstanceOfDeath() + "</td>"
                        + "<td class='tablecell'>" + inc.getSpecialCircumstances() + "</td>"
                        + "</tr>";

            }
            table = table + "</table>";

            return table;
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    // end makeOPenIncidentsTable

    /**
     * This will create a table that has bodyRelease information from the
     * database
     */   
      public String bodyReleaseTable(String drnumber) {
            
            BodyDb bodyDb = new BodyDb(dbdetail, new BodyAtMortuary(drnumber));
            bodyDb.init();
            bodyDb.read();

            String table = "<table class='tabledisplay' id='releaseTable'>"
                    + "<th class='tableheading'>Death Register Number</th>"
                    + "<th class='tableheading'>Name</th>"
                    + "<th class='tableheading'>Surname</th>"
                    + "<th class='tableheading'>ID/Passport Number</th>"
                    + "<th class='tableheading'>Identification Status</th>";
            
                BodyFile bodyFile = new BodyFile(bodyDb.getBody().getDeathRegisterNumber());
                BodyFileDb bodyFileDB = new BodyFileDb(dbdetail, bodyFile);
                bodyFileDB.init();
                bodyFileDB.read();

                table += "<tr class='tablerow' drnumber='" + bodyDb.getBody().getDeathRegisterNumber() + "'>"
                        + "<td>" + bodyDb.getBody().getDeathRegisterNumber() + "</td>"
                        + "<td class='tablecell'>" + bodyDb.getBody().getNameOfDeceased() + "</td>"
                        + "<td class='tablecell'>" + bodyDb.getBody().getSurnameOfDeceased() + "</td>"
                        + "<td class='tablecell'>" + bodyDb.getBody().getID() + "</td>"
                        + "<td class='tablecell'>" + bodyFile.isBodyIdentified() + "</td>"
                        + "</tr>";

            table += "</table>";

            return table;
    }
    // end 

    /**
     * This will create a table that has all the linked body information for the
     * release body
     */   
         public String bodyReleaseLinkedTable(String drnumber) {
            
            String table = "<table class='tabledisplay' id='releaseLinkedTable'>"
                    + "<th class='tableheading'>Death Register Number</th>"
                    + "<th class='tableheading'>Name</th>"
                    + "<th class='tableheading'>Surname</th>"
                    + "<th class='tableheading'>ID/Passport Number</th>"
                    + "<th class='tableheading'>Identification Status</th>";
            
                BodyFileDb bodyFileDB = new BodyFileDb(dbdetail);
                bodyFileDB.init();

                ArrayList<BodyAtMortuary> list = bodyFileDB.getBodyLinkList(drnumber);
                for (BodyAtMortuary tmpBody : list) {
                                        
                    bodyFileDB = new BodyFileDb(dbdetail, new BodyFile(tmpBody.getDeathRegisterNumber()));
                    bodyFileDB.init();
                    bodyFileDB.read();
                    
                table += "<tr class='tablerow' drnumber='" + tmpBody.getDeathRegisterNumber() + "'>"
                        + "<td>" + tmpBody.getDeathRegisterNumber() + "</td>"
                        + "<td class='tablecell'>" + tmpBody.getNameOfDeceased() + "</td>"
                        + "<td class='tablecell'>" + tmpBody.getSurnameOfDeceased() + "</td>"
                        + "<td class='tablecell'>" + tmpBody.getID() + "</td>"
                        + "<td class='tablecell'>" + bodyFileDB.getBodyFile().isBodyIdentified() + "</td>"
                        + "</tr>";
                }
            table += "</table>";

            return table;
    }  
    // end 
      
      
 //     
   /*    public String bodyfile(String id){
        BodyDb bdyDb = new BodyDb( getDbdetail());
        BodyFileDb bdyfileDb = new BodyFileDb( getDbdetail());
       } */
    //   
    
    public String makeOpenBodyFileTable(String id){
        BodyDb bdyDb = new BodyDb(dbdetail);
        BodyFileDb bdyfileDb = new BodyFileDb(dbdetail);
       
        
        String table = "<table class='tabledisplay' id='" + id + "'>"
                    + "<th class='tableheading'>Death Register Number</th>"
                    + "<th class='tableheading'>Date Body Recieved</th>"
                    + "<th class='tableheading'>Incident Log Number</th>"
                    + "<th class='tableheading'>Status</th>";
        try {
            bdyfileDb.init();
            ArrayList<BodyFile> bodyfilelist = bdyfileDb.BodyFileList();
            
            int size = bodyfilelist.size();
            for (int i = 0; i < size; i++) {
                
                BodyFile file = bodyfilelist.get(i);
                String deathregister = file.getDeathRegisterNumber();
                BodyAtMortuary body = new BodyAtMortuary(deathregister);
                bdyDb.setBody(body);
                bdyDb.init();
                bdyDb.read();
                body = (BodyAtMortuary) bdyDb.getBody();
                Incident inc = body.getIncident();
                String status = "";
                if(file.getBodyFileStatus())
                {
                    status = "closed";
                }
                else
                {
                    status = "open";
                }
                table = table + "<tr class='tablerow' deathregisternumber='" + file.getDeathRegisterNumber() + "'>"
                        + "<td>" + file.getDeathRegisterNumber() + "</td>"
                        + "<td class='tablecell'>" +body.getDateBodyReceived()  + "</td>"
                         + "<td class='tablecell'>" + inc.getIncidentLogNumber() + "</td>"
                        + "<td class='tablecell'>" + status + "</td>"
                        + "</tr>";
            }
            table = table + "</table>";

            return table;
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    
    /*public String bodyfile(String id) {
        BodyDb bdyDb = new BodyDb(dbdetail);
        BodyFileDb bdyfileDb = new BodyFileDb(dbdetail);
        bdyDb.init();
        bdyfileDb.init();
        try {

            ArrayList<BodyAtMortuary> bodylist = bdyDb.getBodies();
            ArrayList<BodyFile> bodyfilelist = bdyfileDb.BodyFileList();

            String table = "<table class='tabledisplay' id='" + id + "'>"
                    + "<th class='tableheading'>Deah Register Number</th>"
                    + "<th class='tableheading'>Name</th>"
                    + "<th class='tableheading'>Surname</th>"
                    + "<th class='tableheading'>ID/Passport number</th>"
                    + "<th class='tableheading'>Deceased body status</th>";

            int size = bodylist.size();
            for (int i = 0; i < size; i++) {
                BodyAtMortuary inc = bodylist.get(i);
                BodyFile inc2 = bodyfilelist.get(i);
                table = table + "<tr class='tablerow' lognumber='" + inc.getDeathRegisterNumber() + "'>"
                        + "<td>" + inc.getDeathRegisterNumber() + "</td>"
                        + "<td class='tablecell'>" + inc.getNameOfDeceased() + "</td>"
                        + "<td class='tablecell'>" + inc.getSurnameOfDeceased() + "</td>"
                        + "<td class='tablecell'>" + inc.getID() + "</td>"
                        + "</tr>";
            }
            table = table + "</table>";

            return table;
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    // end  

    public String openbodyfile(String id) {
        BodyFileDb bdyfileDb = new BodyFileDb(dbdetail);
        bdyfileDb.init();
        try {

            ResultSet rs = bdyfileDb.cyasBodyFileRs();

            String table = "<table class='tabledisplay' id='" + id + "'>"
                    + "<th class='tableheading'>Death Register Number</th>"
                    + "<th class='tableheading'>Incident number</th>"                
                    + "<th class='tableheading'>Deceased body recieved</th>"
                    + "<th class='tableheading'>status</th>";
          
            while(rs.next()){
                  table = table +"<tr class='tablerow' regnumber='"+rs.getString("idDeathRegisterNumber")+"'>"
                   +"<td>"+  rs.getString("idDeathRegisterNumber") +"</td>"
                 + "<td class='tablecell'>" + rs.getString("incident_incidentLogNumber") +"</td>"
                  + "<td class='tablecell'>" + rs.getString("dateBodyReceived") +"</td>"
                    + "<td class='tablecell'>" + rs.getString("bodyfileStatus") +"</td>"  
                   + "</tr>"; 
            }
            table = table + "</table>";

            return table;
        } catch (Exception e) {
            return e.getMessage();
        }
    }*/

    // end makeOPenIncidentsTable
    public String getDateTime() {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        return timestamp;
    }
    //end getDateTime

    public Incident getIncidentDetail(String lognumber) 
    {
        IncidentDb idb = new IncidentDb(getDbdetail());
        idb.init();
        try {
            Incident incident = idb.findIncident(lognumber);
            return incident;
        } catch (Exception e) {
            return null;
        }
    }

    public DeathCall getDeathCall(Incident inc) {
        DeathCall call = new DeathCall(inc);
        DeathCallDb calldb = new DeathCallDb(call, dbdetail);
        calldb.init();
        calldb.read();
        call = calldb.getDeathCall();
        return call;
    }
    
    public BodyAtMortuary getBody(String deathRegisterNumber)
    {
        BodyAtMortuary body = new BodyAtMortuary(deathRegisterNumber);
        BodyDb bodyDb = new BodyDb(dbdetail, body);
        bodyDb.init();
        bodyDb.read();
        body = (BodyAtMortuary)bodyDb.getBody();
        return body;
    }
    public String makePropertyTable(String deathregister)
    {
        PropertyDb pDb = new PropertyDb(dbdetail);
        pDb.init();
        String table;
        try 
        {
            ArrayList<Property> properties = pDb.properties(deathregister);
            table = "<table class='tabledisplay' id='propertytable'>"
                    + "<th class='tableheading'>Select</th>"
                    + "<th class='tableheading'>Property Type</th>"
                    + "<th class='tableheading'>Description</th>"
                    + "<th class='tableheading'>Seal Number</th>";
            int size = properties.size();
            for (int i = 0; i < size; i++) 
            {
                
                Property property = properties.get(i);
                String property_type = property.getType();
                String property_description= property.getDescription();
                String property_seal = property.getSealNumber() ;
                if(property_type.equals("null")){
                    property_type="";
                }
                if(property_seal.equals("null")){
                    property_seal="";
                }
                table = table + "<tr class='tablerow' name='propertyId' proId='" + property.getIdProperty() + "'>"
                        +"<td class=tablecell><input type=checkbox name=checkbox[]></td>"
                        + "<td>" + property_type + "</td>"
                        + "<td class='tablecell'>" + property_description + "</td>"
                        + "<td class='tablecell'>" + property_seal + "</td>"
                        + "</tr>";
            }
            table = table + "</table>";
        } 
        catch (Exception e) 
        {
            table = e.getMessage();
        }
        return table;
    }
    //
    
    /**
     * This will create a table with all registered samples for that deathRegisterNumber
     */
    public String makeRegisteredSampleTable(String DRNumber){
        ForensicSampleDb forensicsampleDb = new ForensicSampleDb(getDbdetail());
        forensicsampleDb.init();
        try{
            ArrayList<ForensicSample> registeredSamples = forensicsampleDb.SampleList("deathRegisterNumber", DRNumber);
            
           String table = "";
           
            for(int i=0; i < registeredSamples.size(); i++){
                ForensicSample sample = registeredSamples.get(i);
                table +="<tr class='tablerow' sealnumber='"+ sample.getSealNumber() +"'>"
                        +"<td id='trSealNumber'>"+  sample.getSealNumber() +"</td>"
                        +"<td class='tablecell' id='trBrokenSeal'>" + sample.getBrokenSealNumber()+ "</td>"
                        + "<td class='tablecell' id='trDeathNumber'>" + sample.getDeathRegisterNumber() +"</td>"
                        + "<td class='tablecell' id='trLabNumber'>" + sample.getLabNumber()+"</td>"
                        +"<td class='tablecell' id='trReason'>" + sample.getReason()+ "</td>"
                        + "</tr>"; 
            }
            
            return table;
        }
        catch(Exception e){
            return e.getMessage();
        }
    }
    // end makeRegisteredSampleTable
    /**
     * @return the dbdetail
     */
    public DbDetail getDbdetail() {
        return dbdetail;
    }

    public String makePassword(int len_password) {

        char[] chars = "abcdefghijklmnopqrstuvwxyz1234567890".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < len_password; i++) 
        {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();

    }

    public String getOPenIncidentList(String listname, String selected) {
        IncidentDb idb = new IncidentDb(dbdetail);
        idb.init();
        ArrayList<Incident> list = new ArrayList<Incident>();
        String out = "<select name='" + listname + "' id='" + listname + "'>";
        boolean found = false;
        try 
        {
            list = idb.openIncidentList();
            
            if (selected.equals(""))
            {
                out = out + "<option selected='slected'>Select</option>";
            }
            int size = list.size();
            for (int i = 0; i < size; i++) {
                String element = list.get(i).getIncidentLogNumber();
                if (element.equals(selected)) {
                    out = out + "<option selected='selected'>" + element + "</option>";
                    found = true;
                } else {
                    out = out + "<option>" + element + "</option>";
                }
            }
            if(!(found) && !(selected.equals("")) ){
                out = out + "<option selected='selected'>" + selected + "</option>";
            }
            out = out + "</select>";
            return out;
        }
        catch(Exception e){
            out = out+"<option slected=selected>"+e.getMessage()+"</option>";
            out = out + "</selected>";
            return out;
        }
    }
    
    /**
     * This function creates the icon for the tab
     * @return a html string that loads the icon
     */
    public String makeIcon(){
        String icon = "<link rel='shortcut icon' href='Images/icon.ico'>";
        return icon;
    }
        
    public String checkDate(String inDate){
        if (inDate.equals("")){
            return "0001-01-01";
        }else{
            return inDate;
        }
    }
    
    public String checkTime(String inTime){
        if (inTime.equals("")){
            return "00:00";
        }else{
            return inTime;
        }
    }
    public Kin getKin(String death)
    {
        Kin kin = new Kin();
        kin.setBody_idDeathRegisterNumber(death);
        KinDb kinDb = new KinDb(kin,dbdetail);
        kinDb.init();
        kinDb.read();
        return kinDb.getkin();
    }
}

//end Tools class
