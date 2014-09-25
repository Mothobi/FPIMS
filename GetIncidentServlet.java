/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import database.DbDetail;
import database.DeathCall;
import database.Incident;
import database.IncidentDb;
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
@WebServlet(name = "GetIncidentServlet", urlPatterns = {"/GetIncidentServlet"})
public class GetIncidentServlet extends HttpServlet {

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
        Incident incident = t.getIncidentDetail(request.getParameter("selected_edit_incident"));
        
        HttpSession sess = request.getSession();
        sess.setAttribute("lognumber",incident.getIncidentLogNumber());
        sess.setAttribute("circumstance_of_death",incident.getCircumstanceOfDeath());
        String[] date = incident.getDateOfIncident().split("-");
        sess.setAttribute("year",date[0]);
        sess.setAttribute("month",date[1]);
        sess.setAttribute("day",date[2]);
        sess.setAttribute("place_found",incident.getPlaceBodyFound());
        sess.setAttribute("sap_reference_number",incident.getReferenceNumber());
        sess.setAttribute("special_circumstances",incident.getSpecialCircumstances());
        String[] time = incident.getTimeOfIncident().split(":");
        sess.setAttribute("hour",time[0]);
        sess.setAttribute("minute",time[1]);
        sess.setAttribute("number_of_bodies",incident.getNumberOfBodies());
        sess.setAttribute("bodies_recieved",incident.getBodyCount());
        
        DeathCall call = t.getDeathCall(incident); 
        String[] call_time = call.getTimeOfCall().split(":");
        sess.setAttribute("call_hour",call_time[0]);
        sess.setAttribute("call_minute",call_time[1]);
        sess.setAttribute("call_number",call.getNumberOfCaller() );        
        sess.setAttribute("call_name", call.getNameOfCaller());
        sess.setAttribute("call_institution",call.getInstitution());
        sess.setAttribute("call_address",call.getSceneAddress());
        sess.setAttribute("call_province",call.getProvince());
        sess.setAttribute("call_region",call.getRegion());
        sess.setAttribute("call_condition", call.getSceneConditions());
        
        sess.setAttribute("go_to_editincident", true);
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
