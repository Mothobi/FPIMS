/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import database.*;
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
 * @author Asheen
 */
@WebServlet(name = "SaveIncidentDetails", urlPatterns = {"/SaveIncidentDetails"})
public class SaveIncidentDetails extends HttpServlet {

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
        
        Incident incident = new Incident();
        incident.setIncidentLogNumber(request.getParameter("editfpsnumber"));
        incident.setReferenceNumber(request.getParameter("editSAPSnumber"));
        Tools t = new Tools();
        String month = Integer.toString(t.getMonthNumber(request.getParameter("edit_incident_month").toString()));
        String date = request.getParameter("editdetailyear")+"-"+month+"-"+request.getParameter("edit_incident_day");
        incident.setDateOfIncident(date);
        String time = request.getParameter("edit_incident_hour") +":"+ request.getParameter("edit_incident_minute") + ":00";
        incident.setTimeOfIncident(time);
        incident.setNumberOfBodies(Integer.parseInt(request.getParameter("editnumberofbodies" )));
        incident.setPlaceBodyFound(request.getParameter("editplacefound"));
        incident.setCircumstanceOfDeath(request.getParameter("editcircumstancesofdeath"));
        incident.setSpecialCircumstances(request.getParameter("specialcircumstance"));
        HttpSession sess = request.getSession();
        incident.setBodyCount(Integer.parseInt(sess.getAttribute("bodies_recieved").toString()));        
        incident.setStatus(true);
        
        DbDetail dbdetail = t.getDbdetail();
        IncidentDb incidentdb = new IncidentDb(incident, dbdetail);
        String persal = request.getSession().getAttribute("personnelnumber").toString(); 
        incidentdb.init();
        out.println(incidentdb.edit());
        t.makeAuditTrail("Incident has been edited", "Incident details of incident "+request.getParameter("editfpsnumber") +" has been edited", persal, "Edit Incident Details Tab");
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
