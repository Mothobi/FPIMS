/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import database.Body;
import database.BodyAtMortuary;
import database.BodyDb;
import database.DbDetail;
import java.io.File;
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
@WebServlet(name = "DeceasedDetailsServlet", urlPatterns = {"/DeceasedDetailsServlet"})
public class DeceasedDetailsServlet extends HttpServlet {

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
        BodyAtMortuary body = new BodyAtMortuary(request.getParameter("deceasedDeathRegisterNr"));
        Tools t = new Tools();
        DbDetail dbdetail = t.getDbdetail();
        BodyDb bodyDb = new BodyDb(dbdetail,body);
        bodyDb.init();
        bodyDb.read();
        body.setNameOfDeceased(request.getParameter("DeceasedName"));
        //DeceasedNumbers
        String st = (String)request.getParameter("DeceasedNumber");
        if(st.equals("ID"))
        {
            body.setID(request.getParameter("DeceasedNumber"));
            body.setPassport(null);
        }
        else
        {
            body.setPassport(request.getParameter("DeceasedNumber"));
            body.setID(null);
        }
        body.setMaidenName(request.getParameter("DeceasedMaidenName"));
        body.setSurnameOfDeceased(request.getParameter("DeceasedSurname"));
        body.setIdentifiedDateTime(request.getParameter("deceasedbodyIdentifiedDate") + " " + request.getParameter("deceasedbodyIdentifiedTime"));
        body.setPlaceOfBirth(request.getParameter("deceasedPlaceBirth"));
        body.setDateOfBirth(request.getParameter("deceasedDateBirth"));
        body.setAgeOnDateFound(Integer.parseInt(request.getParameter("deceasedage")));
        body.setGender(request.getParameter("deceasedgender"));
        body.setMaritalStatus(request.getParameter("deceasedMaritalstatus"));
        body.setRace(request.getParameter("deceasedrace"));
        body.setOccupation(request.getParameter("deceasedOccupation"));
        body.setCitizen(request.getParameter("deceasedCitizenship"));
        String bodyStatus = request.getParameter("deceasedBodyStatus");
        if(bodyStatus.equals("identified"))
        {
             body.setBodyStatus(true);
        }
        else
        {
             body.setBodyStatus(false);
        }
        body.setAssignedTo(request.getParameter("deceasedFPS"));
        bodyDb = new BodyDb(dbdetail,body);
        bodyDb.init();
        bodyDb.edit();
        HttpSession sess = request.getSession();
        sess.setAttribute("deceasedDetail", "Deceased details added successfully");
        String personnelnumber = sess.getAttribute("personnelnumber").toString();
        new Tools().makeAuditTrail("Deceased details", "Edit/Confirmed deceased details "+ body.getDeathRegisterNumber(), personnelnumber, "Deceased details");
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
