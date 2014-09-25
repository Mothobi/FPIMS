/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import database.BodyAddress;
import database.BodyAtMortuary;
import database.BodyDb;
import database.DbDetail;
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
 * @author Chester
 */
@WebServlet(name = "DeceasedAddressServlet", urlPatterns = {"/DeceasedAddressServlet"})
public class DeceasedAddressServlet extends HttpServlet {

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
        BodyAtMortuary body = new BodyAtMortuary(request.getParameter("deceasedDeathRegisterNr2"));
        BodyAddress bodyAddress = new BodyAddress();
        bodyAddress.setBuilding(request.getParameter("deceasedbuilding"));
        bodyAddress.setStreet(request.getParameter("deceaesedstreet"));
        bodyAddress.setSuburb(request.getParameter("deceasedsub"));
        bodyAddress.setCity(request.getParameter("deceasedcity"));
        bodyAddress.setPostCode(request.getParameter("postalcode"));
        bodyAddress.setProvince(request.getParameter("province"));
        bodyAddress.setRegion(request.getParameter("region"));
        bodyAddress.setMagisterialDistrict(request.getParameter("MagisterialD"));
        Tools t = new Tools();
        DbDetail dbdetail = t.getDbdetail();
        body.setBodyAddress(bodyAddress);
        BodyDb bodyDb = new BodyDb(dbdetail,body);
        bodyDb.init();
        bodyDb.editBodyAddresss();
        HttpSession sess = request.getSession();
        String personnelnumber = sess.getAttribute("personnelnumber").toString();
        new Tools().makeAuditTrail("Deceased address details", "Edit/Confirmed deceased address details "+ body.getDeathRegisterNumber(), personnelnumber, "Deceased Address details");
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
