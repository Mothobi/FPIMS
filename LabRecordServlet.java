/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import AssistiveClasses.SetDbDetail;
import database.ForensicSampleDb;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Mubien Nakhooda Coachlab 2013
 */
public class LabRecordServlet extends HttpServlet {

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
        
        SetDbDetail dbSet = new SetDbDetail();
        
        ForensicSampleDb sampleDB = new ForensicSampleDb(dbSet.getDbdetail());
        sampleDB.init();
        System.out.println("Fetch Sample: " + sampleDB.read(request.getParameter("seal")));
        
        sampleDB.getforensicSample().setDateReceived(request.getParameter("year") + "-" + request.getParameter("month") + "-" + request.getParameter("day"));
        sampleDB.getforensicSample().setReceived(true);
        
        System.out.println("Edit Sample Date Received: " + sampleDB.edit());
        
        request.getSession().setAttribute("_labRecord", "true");
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
