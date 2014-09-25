/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import database.BodyAddress;
import database.BodyAtMortuary;
import database.BodyAtMortuaryDb;
import database.BodyDb;
import database.BodyFile;
import database.BodyFileDb;
import database.DbDetail;
import database.Incident;
import database.IncidentDb;
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
@WebServlet(name = "editAtMortuaryDetails", urlPatterns = {"/editAtMortuaryDetails"})
public class EditAtMortuaryDetails extends HttpServlet {

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
        BodyAtMortuaryDb bodyAtMortuaryDb = new BodyAtMortuaryDb(dbdetail);
        /**
         * Death register number: request.getParameter("at_mort_deathregister")
         * Incident log number: request.getParameter("at_mort_lognmber")
         */        
        BodyAtMortuary bodyAtMortuary = new BodyAtMortuary(request.getParameter("edit_at_mort_deathregister"));
        //reading original bodyAtScene details from database before making any changes
            bodyAtMortuaryDb.setBodyAtMortuary(bodyAtMortuary);
            bodyAtMortuaryDb.init();
            out.println("reading Body At Mortuary Details:::" + bodyAtMortuaryDb.read());//this read function has not been coded yet.
            bodyAtMortuary = bodyAtMortuaryDb.getBodyAtMortuary();
        //end of reading bodyAtScene details
        bodyAtMortuary.setBodyHandedOverToPerNumber(request.getParameter("edit_employee"));
        String receivedFrom = request.getParameter("edit_employee_handing");
        
        //out.println("Employee handed over to: " + request.getParameter("employee"));
        //out.println("Employee recieved from: "+request.getParameter("employee_handing"));
            /**
             * receivedFrom is not mandatory, so if they don't select an organization make sure
             * that you save none into the database
             */
            if(receivedFrom.equals("Select")!=true){
                bodyAtMortuary.setBodyReceivedFromPerNumber(receivedFrom);
            }else{
                bodyAtMortuary.setBodyReceivedFromPerNumber("null");
            }
        String organization = request.getParameter("edit_organization");
            /**
             * Organization is not mandatory, so if they don't select an organization make sure
             * that you save none into the database
             */
            if(organization.equals("Select")!=true){
                bodyAtMortuary.setBodyHandOverFromOrganization(organization);
            }else{
                bodyAtMortuary.setBodyHandOverFromOrganization("null");
            }
        
        //Body Details
        Incident linked_incident = new Incident(request.getParameter("edit_at_mort_lognmber"));
        bodyAtMortuary.setIncident(linked_incident);
        bodyAtMortuary.setBodyType(request.getParameter("edit_bodypart"));
        bodyAtMortuary.setNameOfDeceased(request.getParameter("edit_atMortBodyName"));
        bodyAtMortuary.setSurnameOfDeceased(request.getParameter("edit_atMortBodySurname"));
        if (request.getParameter("edit_recieve_at_mort_id_type").equals("ID")){
            bodyAtMortuary.setID(request.getParameter("edit_atMortBodyID"));
        }else if (request.getParameter("edit_recieve_at_mort_id_type").equals("Passport")){
            bodyAtMortuary.setPassport(request.getParameter("edit_atMortBodyID"));
        }
        
        //building body address
            BodyAddress bodyAddress = new BodyAddress();
            bodyAddress.setBuilding(request.getParameter("edit_atMortuaryBodyAddressBuilding"));
            bodyAddress.setStreet(request.getParameter("edit_atMortuaryBodyAddressStreet"));
            bodyAddress.setSuburb(request.getParameter("edit_atMortuaryBodyAddressSuburb"));
            bodyAddress.setCity(request.getParameter("edit_atMortuaryBodyAddressCity"));
            bodyAddress.setPostCode(request.getParameter("edit_atMortuaryAddressPostalCode"));
            if (request.getParameter("edit_province").equals("Select")!=true){
                bodyAddress.setProvince(request.getParameter("edit_province"));
            }
            if (request.getParameter("edit_region").equals("Select")!=true){
                bodyAddress.setRegion(request.getParameter("edit_region"));
            }
            //bodyAddress.setMagisterialDistrict(request.getParameter("atSceneBodyAddressMagisterialDistrict"));
        //end of building body address
        bodyAtMortuary.setBodyAddress(bodyAddress);
        if (request.getParameter("edit_race").equals("Select")!=true){
            bodyAtMortuary.setRace(request.getParameter("edit_race"));
        }
        if (request.getParameter("edit_gender").equals("Select")!=true){
            bodyAtMortuary.setGender(request.getParameter("edit_gender"));
        }
        if (request.getParameter("edit_atMortuaryBodyEstAge").equals("Age")!=true){
            if(request.getParameter("edit_at_mortuary_body_estimated_age").equals("Months")){
                bodyAtMortuary.setEstimatedAgeMonth(Integer.parseInt(request.getParameter("edit_atMortuaryBodyEstAge")));
                bodyAtMortuary.setAgeOnDateFound(Integer.parseInt(request.getParameter("edit_atMortuaryBodyEstAge"))); //not given by UI
            }else if(request.getParameter("edit_at_mortuary_body_estimated_age").equals("Years")){
                bodyAtMortuary.setEstimatedAgeYear(Integer.parseInt(request.getParameter("edit_atMortuaryBodyEstAge")));
                bodyAtMortuary.setAgeOnDateFound(Integer.parseInt(request.getParameter("edit_atMortuaryBodyEstAge"))); //not given by UI
            }
        }
        //end of Body details
        /*/body fields that are not given by the UI input
        bodyAtMortuary.setDateOfBirth("0000-00-00");
        bodyAtMortuary.setIdentifiedDateTime("0000-00-00 00:00");
        bodyAtMortuary.setBodyStatus(false);
        bodyAtMortuary.setDateBodyReceived("0000-00-00");
        bodyAtMortuary.setDateBodyReleased("0000-00-00");
        bodyAtMortuary.setBodyReleased(false);
        bodyAtMortuary.setBodyReleaseTo(null);*/
        //end of body fiels that are not given by the UI
        
        //inserting body into database
        bodyDb.setBody(bodyAtMortuary);
        bodyDb.init();
        out.println("editing body :::" + bodyDb.edit());
        //end of body inserting
        //inserting Body Address into Body Address table
        bodyDb.init();
        out.println("editing body address :::" + bodyDb.editBodyAddresss());
        //end of inserting Body Address
        
        //inserting BodyAtMortuary into Database
        bodyAtMortuaryDb.setBodyAtMortuary(bodyAtMortuary);
        bodyAtMortuaryDb.init();
        out.println("editing body at mortuary :::" + bodyAtMortuaryDb.edit());
        //end inserting BodyAtMortuary
        
        /*/POPULATING BODYFILE TABLE
        BodyFile atMortuaryBodyFile = new BodyFile(bodyAtMortuary.getDeathRegisterNumber());
        String currentSystemDate = t.getDateTime().split(" ")[0];
        atMortuaryBodyFile.setDateFileOpened(currentSystemDate);
        /*
         * There is no need to set the other attributes of this bodyfile since they are initialized in it's constructor
         *
        BodyFileDb atMortuaryBodyFileDb = new BodyFileDb(dbdetail, atMortuaryBodyFile);
        atMortuaryBodyFileDb.init();
        out.println("adding body file:::" + atMortuaryBodyFileDb.add());
        //END OF POPULATING BODYFILE TABLE*/
        
        //property
        PropertyDb atMort_propertyDb = new PropertyDb(dbdetail);
        int count_fps = Integer.parseInt(request.getParameter("edit_fps_property_counter_mort").toString());
        for(int i=0;i<count_fps;i++){
            String fps_prop_des = "edit_atMortFPSpropertyDescr"+Integer.toString(i+1);
            String fps_prop_persal = "edit_atMortFPSpropertyPersal"+Integer.toString(i+1);
            if(request.getParameter(fps_prop_des) != null){
                Property atMort_propertyFPS = new Property();
                atMort_propertyFPS.setDeathRegisterNumber(bodyAtMortuary.getDeathRegisterNumber());
                atMort_propertyFPS.setDescription(request.getParameter(fps_prop_des));
                atMort_propertyFPS.setTakenBy(request.getParameter(fps_prop_persal));
                //Not null unmentioned fields
                Witness[] witnesses = {new Witness("null","null"), new Witness("null","null")};
                atMort_propertyFPS.setWitnesses(witnesses);
                atMort_propertyFPS.setDate(t.getDateTime().split(" ")[0]); //GET BACK TO THIS
                atMort_propertyFPS.setSAPS_taken(false);
                atMort_propertyFPS.setReleased(false);
                //put the code to add this property into the database here
                atMort_propertyDb.setProperty(atMort_propertyFPS);
                atMort_propertyDb.init();
                out.println("adding property :::" + atMort_propertyDb.add());
            }
        }
        //property end
        
        //Increase Body Count for the relevent incident
        IncidentDb incidentDb = new IncidentDb(linked_incident, dbdetail);
        incidentDb.init();
        out.println(incidentDb.read());
        incidentDb.init();
        out.println(incidentDb.IncreaseBodyCount());
        HttpSession sess = request.getSession();
        sess.setAttribute("edit_atMortuary", true);
        response.sendRedirect("Home.jsp");
        
        /*try {
            /* TODO output your page here. You may use following sample code. 
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet editAtMortuaryDetails</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet editAtMortuaryDetails at " + request.getContextPath() + "</h1>");
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
