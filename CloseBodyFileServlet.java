/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import database.BodyFile;
import database.BodyFileDb;
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
@WebServlet(name = "CloseBodyFileServlet", urlPatterns = {"/CloseBodyFileServlet"})
public class CloseBodyFileServlet extends HttpServlet {

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
        String deathregister = request.getParameter("selectedbodyfile");
        String reason = request.getParameter("closebodyreason");
        BodyFileDb bfdb = new BodyFileDb(t.getDbdetail());
        bfdb.init();
        bfdb.readBodyFile(deathregister);
        BodyFile bodyfile = bfdb.getBodyFile();
        String date = t.getDateTime().split(" ")[0];
        bodyfile.setDateFileClosed(date);
        bodyfile.setBodyFileStatus(true);
        BodyFileDb bfdb2 = new BodyFileDb(t.getDbdetail(), bodyfile);
        bfdb2.init();
        bfdb2.edit();
        HttpSession sess = request.getSession();
        sess.setAttribute("closedbody", true);
        t.makeAuditTrail("Body File Manually Closed", "Death register number: "+deathregister +". Reason: "+reason, sess.getAttribute("personnelnumber").toString() , "Open body file tab");
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
