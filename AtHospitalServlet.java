/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import database.BodyAddress;
import database.BodyAtMortuary;
import database.BodyAtHospital;
import database.BodyAtHospitalDb;
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
 * @author Mothobi
 */
@WebServlet(name = "AtHospitalServlet", urlPatterns = {"/AtHospitalServlet"})
public class AtHospitalServlet extends HttpServlet {

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
        
        // Hospital member handing over the body
            Member Hospmember = new Member();
            Hospmember.setName(request.getParameter("HospMemberBodyName"));
            Hospmember.setSurname(request.getParameter("HospMemberBodySurname"));
            Hospmember.setContactNumber(request.getParameter("HospMemberBodyCell"));
                        
            String organization = request.getParameter("organization");
                 if(organization.equals("Select")){
                Hospmember.setOrganization("None");
            }
            else{
                Hospmember.setOrganization(organization);
            }
        //FPS memeber receiving body at hospital
            Member FPSmember = new Member();
            FPSmember.setPersonnelNumber(request.getParameter("FPSmemberBodyPersal"));
            
        
                
      //Incident Log number: request.getParameter("at_scene_lognmber");   
        BodyAtHospital bodyAtHospital = new BodyAtHospital(new BodyAtMortuary(request.getParameter("at_hosp_deathregister")));
             
        //Body Details
        bodyAtHospital.getBody().setIncident(new Incident(request.getParameter("at_hosp_lognmber")));
        bodyAtHospital.getBody().setBodyType(request.getParameter("bodypart"));
        bodyAtHospital.getBody().setNameOfDeceased(request.getParameter("fromHospBodyName"));
        bodyAtHospital.getBody().setSurnameOfDeceased(request.getParameter("fromHospBodySurname"));
        
        if(request.getParameter("recieve_from_hosp_id_type").equals("ID")){
            bodyAtHospital.getBody().setID(request.getParameter("atHospBodyID"));
        }
          else if(request.getParameter("recieve_from_hosp_id_type").equals("Passport")){
              bodyAtHospital.getBody().setPassport(request.getParameter("atHospBodyID"));
        }
       
        //building body address
            BodyAddress bodyAddress = new BodyAddress();
            bodyAddress.setBuilding(request.getParameter("atHospBodyAddressBuilding"));
            bodyAddress.setStreet(request.getParameter("atHospBodyAddressStreet"));
            bodyAddress.setSuburb(request.getParameter("atHospBodyAddressSuburb"));
            bodyAddress.setCity(request.getParameter("atHospBodyAddressCity"));
            bodyAddress.setPostCode(request.getParameter("atHospBodyAddressPostalCode"));
            //end of building body address
            bodyAtHospital.getBody().setBodyAddress(bodyAddress);
            
            if (request.getParameter("province").equals("Select")!=true){
                bodyAddress.setProvince(request.getParameter("province"));
            }
            if (request.getParameter("region").equals("Select")!=true){
                bodyAddress.setRegion(request.getParameter("region"));
            }
            bodyAddress.setMagisterialDistrict(request.getParameter("atHospBodyAddressMagisterialDistrict"));
       
           
            if (request.getParameter("race").equals("Select")!=true){
            bodyAtHospital.getBody().setRace(request.getParameter("race"));
              }
           if (request.getParameter("gender").equals("Select")!=true){
            bodyAtHospital.getBody().setGender(request.getParameter("gender"));
             }
        if(request.getParameter("atHospBodyEstAge").equals("Age")!=true){
            if(request.getParameter("at_hosp_body_estimated_age").equals("Month")){
                bodyAtHospital.getBody().setEstimatedAgeMonth(Integer.parseInt(request.getParameter("atHospBodyEstAge")));
                bodyAtHospital.getBody().setAgeOnDateFound(Integer.parseInt(request.getParameter("atHospBodyEstAge"))); //field not given by UI
            }else if(request.getParameter("at_hosp_body_estimated_age").equals("Year")){
                bodyAtHospital.getBody().setEstimatedAgeYear(Integer.parseInt(request.getParameter("atHospBodyEstAge")));
                bodyAtHospital.getBody().setAgeOnDateFound(Integer.parseInt(request.getParameter("atHospBodyEstAge"))); //field not given by UI
            }
            
        }
        bodyAtHospital.setAllegedDeathDateTime(t.checkDate(request.getParameter("hospAllegedDeathDate")) + " " + t.checkTime(request.getParameter("HospAllegedDeathTime")));
        bodyAtHospital.setHospitalDateTime(t.checkDate(request.getParameter("bodyReceiveHospDate")) + " " + t.checkTime(request.getParameter("bodyReceiveHospTime")));
        bodyAtHospital.setFacilityDateTime(t.checkDate(request.getParameter("bodyReceiveAtFacilFromHospDate")) + " " + t.checkTime(request.getParameter("bodyReceiveAtFacilFromHospTime")));
       
        FPSmember.setDeathRegisterNumber(bodyAtHospital.getBody().getDeathRegisterNumber());
        
        
        //inserting body into database
        BodyDb bodyDb = new BodyDb(dbdetail, bodyAtHospital.getBody());
        bodyDb.init();
        out.println("adding body :::" + bodyDb.add());
        //end of body inserting
        
        //inserting Body Address into Body Address table
        bodyDb.init();
        out.println("adding body address :::" + bodyDb.addBodyAddress());
        //end of inserting Body Address
        
        //inserting BodyAtHospital into Database
        BodyAtHospitalDb bodyAtHospitalDb = new BodyAtHospitalDb(dbdetail,bodyAtHospital);
        bodyAtHospitalDb.init();
        out.println("adding body at Hospital :::" + bodyAtHospitalDb.add());
        //end inserting BodyAtHospital
        
        //NOTE: must add all other things such as members and property after adding the body, due to foreign key constraints
        
        
        MemberDb memberDb = new MemberDb(dbdetail);
        //inserting body recieved from member
        memberDb.setMember(Hospmember);
        memberDb.init();
        out.println("adding BodyReceivedFrom Mem :::" + memberDb.add());
        //end of inserting body received from member*/
        
         
        
        //insertin FPS member
        //memberDb = new MemberDb(dbdetail);
        memberDb.setMember(FPSmember);
        memberDb.init();
        out.println("adding FPSmem :::" + memberDb.add());
        //end inserting FPS member
        
                
        //POPULATING BODYFILE TABLE
        BodyFile atHospitalBodyFile = new BodyFile(bodyAtHospital.getBody().getDeathRegisterNumber());
        String currentSystemDate = t.getDateTime().split(" ")[0];
        atHospitalBodyFile.setDateFileOpened(currentSystemDate);
        /*
         * There is no need to set the other attributes of this bodyfile since they are initialized in it's constructor
         */
        BodyFileDb atHospitalBodyFileDb = new BodyFileDb(dbdetail, atHospitalBodyFile);
        atHospitalBodyFileDb.init();
        out.println("Adding Body File:::" + atHospitalBodyFileDb.add());
        //END OF POPULATING BODYFILE TABLE
               
         //property
        PropertyDb atHosp_propertyDb = new PropertyDb(dbdetail);
        int count_fps = Integer.parseInt(request.getParameter("hosp_property_counter").toString());
        for(int i=0;i<count_fps;i++){
            String fps_prop_des = "fps_prop_des"+Integer.toString(i+1);
            String fps_prop_persal = "fps_prop_persal"+Integer.toString(i+1);
            if(request.getParameter(fps_prop_des) != null){
                Property atHosp_propertyFPS = new Property();
                atHosp_propertyFPS.setDeathRegisterNumber(bodyAtHospital.getBody().getDeathRegisterNumber());
                atHosp_propertyFPS.setDescription(request.getParameter(fps_prop_des));
                atHosp_propertyFPS.setTakenBy(request.getParameter(fps_prop_persal));
                //Not null unmentioned fields
                Witness[] witnesses = {new Witness("null","null"), new Witness("null","null")};
                atHosp_propertyFPS.setWitnesses(witnesses);
                atHosp_propertyFPS.setDate(t.getDateTime().split(" ")[0]); //GET BACK TO THIS
                atHosp_propertyFPS.setSAPS_taken(false);
                atHosp_propertyFPS.setReleased(false);
                atHosp_propertyFPS.setLocationReceived("atHosp-FPS");
                //put the code to add this property into the database here
                atHosp_propertyDb.setProperty(atHosp_propertyFPS);
                atHosp_propertyDb.init();
                out.println("adding property :::" + atHosp_propertyDb.add());
            }
        }
        //property end
       
        
        
               
        
        
        //Increase Body Count for the relevent incident
        IncidentDb incidentDb = new IncidentDb( new Incident(request.getParameter("at_hosp_lognmber")), dbdetail);
        incidentDb.init();
        out.println(incidentDb.read());
        incidentDb.init();
        out.println(incidentDb.IncreaseBodyCount());
        HttpSession sess = request.getSession();
        sess.setAttribute("atHospital", true);
        response.sendRedirect("Home.jsp");

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


