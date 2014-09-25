/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import database.BodyAddress;
import database.BodyAtMortuary;
import database.BodyAtScene;
import database.BodyAtSceneDb;
import database.BodyDb;
import database.BodyFile;
import database.BodyFileDb;
import database.DbDetail;
import database.Incident;
import database.IncidentDb;
import database.Member;
import database.MemberDb;
import database.Property;
import database.PropertyDb;
import database.Witness;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Bandile
 */
@WebServlet(name = "editAtSceneDetails", urlPatterns = {"/editAtSceneDetails"})
public class EditAtSceneDetails extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        Tools t = new Tools();
        DbDetail dbdetail = t.getDbdetail();
        BodyDb bodyDb = new BodyDb(dbdetail);
        BodyAtSceneDb bodyAtSceneDb = new BodyAtSceneDb(dbdetail);
        MemberDb memberDb = new MemberDb(dbdetail);
        
        //Incident Log number: request.getParameter("edit_at_scene_lognmber");   
        
        BodyAtScene bodyAtScene = new BodyAtScene(new BodyAtMortuary(request.getParameter("edit_at_scene_deathregister")));  
        //reading original bodyAtScene details from database before making any changes
            bodyAtSceneDb.setBodyAtScene(bodyAtScene);
            bodyAtSceneDb.init();
            out.println("reading Body Details:::" + bodyAtSceneDb.read()); 
            bodyAtScene = bodyAtSceneDb.getBodyAtScene();
        //end of reading bodyAtScene details
        bodyAtScene.setDateTimeBodyFound(t.checkDate(request.getParameter("edit_bodyFoundDate")) + " " + t.checkTime(request.getParameter("edit_bodyFoundTime")));
        bodyAtScene.setAllegedInjuryDateTime(t.checkDate(request.getParameter("edit_inAllegedInjuryDate")) + " " + t.checkTime(request.getParameter("edit_inAllegedInjuryTime")));
        bodyAtScene.setAllegedDeathDateTime(t.checkDate(request.getParameter("edit_inAllegedDeathDate")) + " " + t.checkTime(request.getParameter("edit_inAllegedDeathTime")));
        bodyAtScene.setSceneDateTime(t.checkDate(request.getParameter("edit_ReceivedSceneDate")) + " " + t.checkTime(request.getParameter("edit_ReceivedSceneTime")));
        bodyAtScene.setFacilityDateTime(t.checkDate(request.getParameter("edit_ReceivedFacilityDate")) + " " + t.checkTime(request.getParameter("edit_ReceivedFacilityTime")));
        if (request.getParameter("SceneType").equals("Select")!=true){
            bodyAtScene.setSceneIncidentOccured(request.getParameter("SceneType"));
        }
        bodyAtScene.setPlaceOfDeath(request.getParameter("edit_DeathAddress"));
        if (request.getParameter("externalcircumstance").equals("Select")!=true){
            bodyAtScene.setExternalCircumstanceOfInjury(request.getParameter("externalcircumstance"));
        }
        
        Member pathologistOnScene = new Member();
        //pathologistOnScene.setIdMember(idMember); uncomment this and get this id from a hiiden field in the UI(jsp) when it is implemented
        //reading original member details from database before making any changes
            memberDb.setMember(pathologistOnScene);
            memberDb.init();
            out.println("reading Pathologist member:::" + memberDb.read());
            pathologistOnScene = memberDb.getMember();
        //end of reding member details
        if (request.getParameter("edit_pathologistAtScene").equals("Yes")){
            bodyAtScene.setPathOnScene(true);
            //Pathologist on scene MIGHT NEED TO ADD SEPERATE TABLE
            
            pathologistOnScene.setName(request.getParameter("edit_pathologistBodyName"));
            pathologistOnScene.setSurname(request.getParameter("edit_pathologistBodySurname"));
            //pathologistOnScene.setPersonnelNumber(request.getParameter(null));
            //pathologistOnScene.setContactNumber(request.getParameter(null));
            if (request.getParameter("edit_pathologistBodyRank").equals("Select")!=true){
                pathologistOnScene.setRank(request.getParameter("edit_pathologistBodyRank"));
            }
            pathologistOnScene.setDeathRegisterNumber(bodyAtScene.getBody().getDeathRegisterNumber());
            //end of Pathologist on scene
        }else{
            bodyAtScene.setPathOnScene(false);
        }
        
        
        //build body received from
            Member receivedFrom = new Member();
            //receivedFrom.setIdMember(idMember); uncomment this and get this id from a hiiden field in the UI(jsp) when it is implemented
            //reading original member details from database before making any changes
                memberDb.setMember(receivedFrom);
                memberDb.init();
                out.println("reading receivedFrom member:::" + memberDb.read());
                receivedFrom = memberDb.getMember();
            //end of reding member details
            receivedFrom.setName(request.getParameter("edit_receivedBodyFromName"));
            receivedFrom.setSurname(request.getParameter("edit_receivedFromBodySurname"));
            String organization = request.getParameter("organization");
            /**
             * Organization is not mandatory, so if they don't select an organization make sure
             * that you save none into the database
             */
            if(organization.equals("Select")){
                receivedFrom.setOrganization("None");
            }
            else{
                receivedFrom.setOrganization(organization);
            }
            receivedFrom.setDeathRegisterNumber(bodyAtScene.getBody().getDeathRegisterNumber());
        //end of building received from
        
        //SAPS member
            Member SAPSmember = new Member();
            //SAPSmember.setIdMember(idMember);uncomment this and get this id from a hiiden field in the UI(jsp) when it is implemented
            //reading original member details from database before making any changes
                memberDb.setMember(SAPSmember);
                memberDb.init();
                out.println("reading SAPS member:::" + memberDb.read());
                SAPSmember = memberDb.getMember();
            //end of reading member details
            SAPSmember.setName(request.getParameter("edit_SAPSmemberBodyName"));
            SAPSmember.setSurname(request.getParameter("edit_SAPSmemberBodySurname"));
            SAPSmember.setContactNumber(request.getParameter("edit_SAPSmemberBodyCell"));
            SAPSmember.setOrganization("SAPS"); //SAPS
            if (request.getParameter("edit_SAPSmemberBodyRank").equals("Select")!=true){
                SAPSmember.setRank(request.getParameter("edit_SAPSmemberBodyRank"));
            }
            SAPSmember.setDeathRegisterNumber(bodyAtScene.getBody().getDeathRegisterNumber());
        // end of SAPS member
        
        //FPSmemeber
            Member FPSmemeber = new Member();
            //FPSmemeber.setIdMember(idMember);uncomment this and get this id from a hiiden field in the UI(jsp) when it is implemented
            //reading original member details from database before making any changes
                memberDb.setMember(FPSmemeber);
                memberDb.init();
                out.println("reading FPS member:::" + memberDb.read());
                FPSmemeber = memberDb.getMember();
            //end of reading member details
            FPSmemeber.setName(request.getParameter("edit_FPSmemberBodyName"));
            FPSmemeber.setSurname(request.getParameter("edit_FPSmemberBodySurname"));
            FPSmemeber.setPersonnelNumber(request.getParameter("edit_FPSmemberBodyPersal"));
            FPSmemeber.setContactNumber(request.getParameter("edit_FPSmemberBodyCell"));
            if (request.getParameter("edit_FPSmemberBodyRank").equals("Select")!=true){
                FPSmemeber.setRank(request.getParameter("edit_FPSmemberBodyRank"));
            }
            FPSmemeber.setDeathRegisterNumber(bodyAtScene.getBody().getDeathRegisterNumber());
        //end of FPS member
           
            
        //Body Details
        bodyAtScene.getBody().setIncident(new Incident(request.getParameter("edit_at_scene_lognmber")));
        bodyAtScene.getBody().setBodyType(request.getParameter("bodypart"));
        bodyAtScene.getBody().setNameOfDeceased(request.getParameter("edit_atSceneBodyName"));
        bodyAtScene.getBody().setSurnameOfDeceased(request.getParameter("edit_atSceneBodySurname"));
        if(request.getParameter("edit_recieve_at_scene_id_type").equals("ID")){
            bodyAtScene.getBody().setID(request.getParameter("edit_atSceneBodyID"));
        }else if(request.getParameter("edit_recieve_at_scene_id_type").equals("Passport")){
            bodyAtScene.getBody().setPassport(request.getParameter("edit_atSceneBodyID"));
        }
        
        //building body address
            BodyAddress bodyAddress = new BodyAddress();
            bodyAddress.setBuilding(request.getParameter("edit_atSceneBodyAddressBuilding"));
            bodyAddress.setStreet(request.getParameter("edit_atSceneBodyAddressStreet"));
            bodyAddress.setSuburb(request.getParameter("edit_atSceneBodyAddressSuburb"));
            bodyAddress.setCity(request.getParameter("edit_atSceneBodyAddressCity"));
            bodyAddress.setPostCode(request.getParameter("edit_atSceneBodyAddressPostalCode"));
            if (request.getParameter("province").equals("Select")!=true){
                bodyAddress.setProvince(request.getParameter("province"));
            }
            if (request.getParameter("region").equals("Select")!=true){
                bodyAddress.setRegion(request.getParameter("region"));
            }
            bodyAddress.setMagisterialDistrict(request.getParameter("edit_atSceneBodyAddressMagisterialDistrict"));
        //end of building body address
        bodyAtScene.getBody().setBodyAddress(bodyAddress);
        if (request.getParameter("race").equals("Select")!=true){
            bodyAtScene.getBody().setRace(request.getParameter("race"));
        }
        if (request.getParameter("gender").equals("Select")!=true){
            bodyAtScene.getBody().setGender(request.getParameter("gender"));
        }
        if(request.getParameter("edit_atSceneBodyEstAge").equals("Age")!=true){
            if(request.getParameter("edit_at_scene_body_estimated_age_type").equals("Month")){
                bodyAtScene.getBody().setEstimatedAgeMonth(Integer.parseInt(request.getParameter("edit_atSceneBodyEstAge")));
                bodyAtScene.getBody().setAgeOnDateFound(Integer.parseInt(request.getParameter("edit_atSceneBodyEstAge"))); //field not given by UI
            }else if(request.getParameter("edit_at_scene_body_estimated_age_type").equals("Year")){
                bodyAtScene.getBody().setEstimatedAgeYear(Integer.parseInt(request.getParameter("edit_atSceneBodyEstAge")));
                bodyAtScene.getBody().setAgeOnDateFound(Integer.parseInt(request.getParameter("edit_atSceneBodyEstAge"))); //field not given by UI
            }
            
        }
      
        //editing body into database
        bodyDb.setBody(bodyAtScene.getBody());
        bodyDb.init();
        out.println("editing body :::" + bodyDb.edit());
        //end of body inserting
        //editing Body Address into Body Address table
        bodyDb.init();
        out.println("editing body address :::" + bodyDb.editBodyAddresss());
        //end of editing Body Address
        
        //editing BodyAtScene into Database
        bodyAtSceneDb.setBodyAtScene(bodyAtScene);
        bodyAtSceneDb.init();
        out.println("editing bodyAtScene :::" + bodyAtSceneDb.edit());
        //end editing BodyAtScene
        
        //NOTE: must add all other things such as members and property after adding the body, due to foreign key constraints
        
        //editing body recieved from member
        memberDb.setMember(receivedFrom);
        memberDb.init();
        out.println("editing BodyReceivedFromMem :::" + memberDb.edit_by_ID());
        //end of editing body received from member
        
        //editing SAPS member
        memberDb.setMember(SAPSmember);
        memberDb.init();
        out.println("editing SAPSmem  :::" + memberDb.edit_by_ID());
        //end editing SAPS member
        
        //editing FPS member
        //memberDb = new MemberDb(dbdetail);
        memberDb.setMember(FPSmemeber);
        memberDb.init();
        out.println("editing FPSmem :::" + memberDb.edit_by_ID());
        //end editing FPS member
        
        //editing Pathologist member
        if(bodyAtScene.isPathOnScene()){
            memberDb.setMember(pathologistOnScene);
            memberDb.init();
            out.println("editing Pathmem :::" + memberDb.edit_by_ID());
        }
        //end editing Pathologist member
        
        /*BodyFile atSceneBodyFile = new BodyFile(bodyAtScene.getBody().getDeathRegisterNumber());
        //reading original bodyfile details from database before making any changes
            BodyFileDb atSceneBodyFileDb = new BodyFileDb(dbdetail, atSceneBodyFile);
            atSceneBodyFileDb.init();
            out.println("Reading Body File:::" + atSceneBodyFileDb.read());
            atSceneBodyFile = atSceneBodyFileDb.getBodyFile();
        //end of reading bodyfile details
        /*SHOULD INSERT CHANGES TO BODYFILE HERE
        //String currentSystemDate = t.getDateTime().split(" ")[0];
        //atSceneBodyFile.setDateFileOpened(currentSystemDate);
        //editing bodyfile
        atSceneBodyFileDb.setBodyFile(atSceneBodyFile);
        atSceneBodyFileDb.init();
        out.println("editing Body File:::" + atSceneBodyFileDb.edit());
        //end editing bodyfile*/
        
        //Property
        PropertyDb atScene_propertyDb = new PropertyDb(dbdetail);
        int count_saps = Integer.parseInt(request.getParameter("edit_saps_property_counter").toString());
        for(int i=0;i<count_saps;i++){
            String saps_prop_des = "edit_SAPSpropertyDescr"+Integer.toString(i+1);
            String saps_prop_name = "edit_SAPSpropertyName"+Integer.toString(i+1);
            String saps_prop_surname = "edit_SAPSpropertySurname"+Integer.toString(i+1);
            if(request.getParameter(saps_prop_des) != null){
                Property propertySAPS = new Property();
                propertySAPS.setDeathRegisterNumber(bodyAtScene.getBody().getDeathRegisterNumber());
                propertySAPS.setDescription(request.getParameter(saps_prop_des));
                propertySAPS.setSAPS_name(request.getParameter(saps_prop_name));
                propertySAPS.setSAPS_surname(request.getParameter(saps_prop_surname));
                //Not null unmentioned fields
                Witness[] witnesses = {new Witness("null","null"), new Witness("null","null")};
                propertySAPS.setWitnesses(witnesses);
                propertySAPS.setDate(t.checkDate(request.getParameter("bodyFoundDate")));
                propertySAPS.setSAPS_taken(true);
                propertySAPS.setReleased(false);
                //put the code to add this property into the database here
                atScene_propertyDb.setProperty(propertySAPS);
                atScene_propertyDb.init();
                out.println("adding property :::" + atScene_propertyDb.add());
            }
        }
        
        int count_fps = Integer.parseInt(request.getParameter("edit_fps_property_counter").toString());
        for(int i=0;i<count_fps;i++){
            String fps_prop_des = "edit_atSceneFPSpropertyDescr"+Integer.toString(i+1);
            String fps_prop_persal = "edit_atSceneFPSpersal"+Integer.toString(i+1);
            //out.println("FPS prop des table number: " + fps_prop_des);
            if(request.getParameter(fps_prop_des) != null){
                Property propertyFPS = new Property();
                propertyFPS.setDeathRegisterNumber(bodyAtScene.getBody().getDeathRegisterNumber());
                propertyFPS.setDescription(request.getParameter(fps_prop_des));
                //out.println("********FPS PROP DES*********: " + request.getParameter(fps_prop_des));
                propertyFPS.setTakenBy(request.getParameter(fps_prop_persal));
                //out.println("********FPS PROP PERSEL*********: " + request.getParameter(fps_prop_persal));
                //Not null unmentioned fields
                Witness[] witnesses = {new Witness("null","null"), new Witness("null","null")};
                propertyFPS.setWitnesses(witnesses);
                propertyFPS.setDate(t.checkDate(request.getParameter("bodyFoundDate")));
                propertyFPS.setSAPS_taken(false);
                propertyFPS.setReleased(false);
                //put the code to add this property into the database here
                atScene_propertyDb.setProperty(propertyFPS);
                atScene_propertyDb.init();
                out.println("adding property :::" + atScene_propertyDb.add());
            }
        }
        
        //end Property
        
        //Increase Body Count for the relevent incident
        IncidentDb incidentDb = new IncidentDb( new Incident(request.getParameter("at_scene_lognmber")), dbdetail);
        incidentDb.init();
        out.println(incidentDb.read());
        incidentDb.init();
        out.println(incidentDb.IncreaseBodyCount());
        HttpSession sess = request.getSession();
        sess.setAttribute("edit_atScene", true);
        response.sendRedirect("Home.jsp");
        
        /*try {
            /* TODO output your page here. You may use following sample code. 
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet editAtSceneDetails</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet editAtSceneDetails at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        } finally {            
            out.close();
        }*/
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
