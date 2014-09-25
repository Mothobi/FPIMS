/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import database.DbDetail;
import database.InformantProperty;
import database.InformantPropertyDb;
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
 * @author Chester
 */
@WebServlet(name = "ReleasePropertyServlet", urlPatterns = {"/ReleasePropertyServlet"})
public class ReleasePropertyServlet extends HttpServlet {

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
            throws ServletException, IOException
    {
        response.setContentType("text/html;charset=UTF-8");
        String id = request.getParameter("selectedproperty");
        if(!id.equals(""))
        {
            Witness[] witnesses = {new Witness(request.getParameter("Witness1name"),request.getParameter("Witness1surname")),new Witness(request.getParameter("Witness2name"),request.getParameter("Witness2surname"))};
            Property prop = new Property(Integer.parseInt(id));
            PropertyDb propertyDb = new PropertyDb(new Tools().getDbdetail(),prop);
            propertyDb.init();
            propertyDb.read();

            prop = propertyDb.getProperty();
            prop.setWitnesses(witnesses);
            propertyDb.setProperty(prop);

            propertyDb.init();
            propertyDb.edit();
            InformantProperty property = new InformantProperty(request.getParameter("formantname"), request.getParameter("formantsurname"), request.getParameter("Adres"), request.getParameter("propertydescription"), request.getParameter("cash"), request.getParameter("othergood"), witnesses, "099888592");
            InformantPropertyDb proDb = new InformantPropertyDb(new Tools().getDbdetail(), property);
            proDb.init();
            proDb.add();
            HttpSession sess = request.getSession();
            sess.setAttribute("informantDetail", "Kin details added successfully");
            response.sendRedirect("Home.jsp");
        }
        else
        {
            response.sendRedirect("Home.jsp");
        }
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
