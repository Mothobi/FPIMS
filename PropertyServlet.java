/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import AssistiveClasses.SetDbDetail;
import database.Property;
import database.PropertyDb;
import database.Witness;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Mubien Nakhooda Coachlab 2013
 */
@WebServlet(name = "PropertyServlet", urlPatterns = {"/PropertyServlet"})
public class PropertyServlet extends HttpServlet {

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
        Witness[] witness = {new Witness("",""), new Witness("","")};
                
        Property property = new Property(                
            request.getParameter("seal"),
            request.getParameter("descriptions"),
            request.getParameter("year") + "-" + request.getParameter("month") + "-" + request.getParameter("day"),
            request.getParameter("propertytype"),
            "",
            request.getParameter("taken"),
            witness, //Witness
            "",
            "",
            false,
            request.getSession().getAttribute("death_register_number").toString(),
            false
        );
        
        PropertyDb propertyDB = new PropertyDb(dbSet.getDbdetail(), property);
        propertyDB.init();    
        System.out.println(propertyDB.add());
       
        request.getSession().setAttribute("_Property", "true");
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
