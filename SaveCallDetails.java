/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import database.DbDetail;
import database.DeathCall;
import database.DeathCallDb;
import database.Incident;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Asheen
 */
@WebServlet(name = "SaveCallDetails", urlPatterns = {"/SaveCallDetails"})
public class SaveCallDetails extends HttpServlet {

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
        
        DeathCall dcall = new DeathCall();
        String calltime = request.getParameter("edit_callhour") + ":" + request.getParameter("edit_callminute")+ ":00"; 
        dcall.setTimeOfCall(calltime);
        dcall.setNumberOfCaller(request.getParameter("edit_phonenumber"));
        dcall.setNameOfCaller(request.getParameter("edit_callname"));
        dcall.setInstitution(request.getParameter("edit_callinstitution"));
        dcall.setSceneAddress(request.getParameter("edit_calladdress"));        
        dcall.setProvince(request.getParameter("province"));
        dcall.setRegion(request.getParameter("region"));
        dcall.setSceneConditions(request.getParameter("edit_callcondition"));
        Incident inc = new Incident(request.getParameter("edit_lognumber"));
        dcall.setIncident(inc);
        Tools t = new Tools();
        DbDetail dbdetail = t.getDbdetail();
        DeathCallDb calldb = new DeathCallDb(dcall,dbdetail);
        String persal = request.getSession().getAttribute("personnelnumber").toString();                
        calldb.init();        
        calldb.edit();
        t.makeAuditTrail("Incident has been edited", "Call details of incident "+request.getParameter("edit_lognumber") +" has been edited", persal, "Edit Call Details Tab");
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
