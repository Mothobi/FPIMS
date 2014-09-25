/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import database.BodyAtMortuary;
import database.BodyDb;
import database.Recipient;
import database.RecipientDb;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Cya
 */
@WebServlet(name = "SaveRecipientDetails", urlPatterns = {"/SaveRecipientDetails"})
public class SaveRecipientDetails extends HttpServlet {

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
        
        Recipient recipient = new Recipient();
                
        recipient.setName(request.getParameter("RecipientName"));
        recipient.setSurname(request.getParameter("RecipientSurname"));       
        recipient.setIdType(request.getParameter("Recipientidentificationtype"));
        recipient.setID(request.getParameter("RecipientIDNumber"));
        recipient.setAddress(request.getParameter("RecipientAddres"));
        recipient.setContactNumber(request.getParameter("RecipientContact"));
        recipient.setBody_idDeathRegisterNumber(request.getParameter("RecipientDeathRegisterNumber"));
               
        Tools tool = new Tools();
        RecipientDb recipientDb = new  RecipientDb(recipient, tool.getDbdetail());
        recipientDb.init();
        System.out.println(recipientDb.add());
        
        BodyAtMortuary bodtAtMortuary = new BodyAtMortuary(request.getParameter("RecipientDeathRegisterNumber"));
        BodyDb bodyDB = new BodyDb(tool.getDbdetail(), bodtAtMortuary);
        bodyDB.init();
        System.out.println(bodyDB.read());
        bodyDB.getBody().setBodyReleased(true);
        bodyDB.getBody().setBodyReleaseTo(request.getParameter("releasedto"));
        bodyDB.getBody().setBodyReleasedType(request.getParameter("releasedtype"));
        bodyDB.getBody().setDateBodyReleased(tool.getDateTime());
        
        bodyDB.init();
        System.out.println(bodyDB.edit());
                
        request.getSession().setAttribute("_recipientDetail", true);
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
