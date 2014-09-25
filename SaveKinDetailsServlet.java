/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;
import database.DbDetail;
import database.Kin;
import database.KinDb;
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
@WebServlet(name = "SaveKinDetailsServlet", urlPatterns = {"/SaveKinDetailsServlet"})
public class SaveKinDetailsServlet extends HttpServlet {

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
        Kin kin = new Kin();
        HttpSession sess = request.getSession();
        kin.setName(request.getParameter("KinName"));
        kin.setSurname(request.getParameter("KinSurname"));
        String kinIdType = request.getParameter("identificationtype");
        if(kinIdType.contains("ID"))
        {
            kin.setID(request.getParameter("KinIDNumber"));
            kin.setPassport(null);
        }
        else if(kinIdType.contains("Passport"))
        {
            kin.setPassport(request.getParameter("KinIDNumber"));
            kin.setID(null);
        }
        kin.setRelationWithDeceased(request.getParameter("KinRelationship"));
        kin.setContactNumber(request.getParameter("KinContact"));
        kin.setAddress(request.getParameter("KinRes"));
        kin.setWorkAddress(request.getParameter("KinWork"));
        kin.setBody_idDeathRegisterNumber((String)sess.getAttribute("deceasedDeathRegisterNumber"));
        Tools t = new Tools();
        DbDetail dbdetail = t.getDbdetail();
        KinDb kinDb = new KinDb(kin, dbdetail);
        kinDb.init();
        kinDb.edit();
        kinDb.init();
        kinDb.add();
        sess.setAttribute("kinDetail", true);
        String personnelnumber = sess.getAttribute("personnelnumber").toString();
        new Tools().makeAuditTrail("Kin/Informant details", "Edit/Confirmed kin details "+ kin.getBody_idDeathRegisterNumber(), personnelnumber, "Kin/Informant details");
        response.sendRedirect("Home.jsp");
        out.close();
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
