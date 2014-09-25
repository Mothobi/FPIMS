/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import database.Body;
import database.BodyAddress;
import database.BodyDb;
import database.DbDetail;
import AssistiveClasses.SetDbDetail;
import database.BodyAtMortuary;
import database.Incident;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Sandile
 */
public class AddBodyFile extends HttpServlet {

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
        try {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AddBody     File</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AddBodyFile at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        } finally {
            out.close();
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
        PrintWriter out = response.getWriter();
        SetDbDetail dbSet = new SetDbDetail();
        BodyAtMortuary body = new BodyAtMortuary();

        //Getting data for Members at scene 

        String rName = request.getParameter("Rname");
        String rSurname = request.getParameter("Rsurname");
        String Organisation = request.getParameter("organisation");

        String sName = request.getParameter("Sname");
        String Ssame = request.getParameter("Ssurname");
        String Scell = request.getParameter("Scell");
        String Srank = request.getParameter("Srank");

        String Fname = request.getParameter("Fname");
        String Fsurname = request.getParameter("Fsurname");
        String Fpersal = request.getParameter("Fpersal");
        String Fcell = request.getParameter("Fcell");
        String Frank = request.getParameter("Frank");

        String atScene = request.getParameter("atScene");
        String Pname = request.getParameter("Pname");
        String Psurname = request.getParameter("Psurname");
        String Prank = request.getParameter("Prank");



        //Getting data for Body Table
        String deathRegNumber = request.getParameter("deathRegister");
        String bodyGender = request.getParameter("BMgender");
        String bodyRace = request.getParameter("BMrace");
        String bodyName = request.getParameter("BMname");
        String bodySurname = request.getParameter("BMsurname");
        String bodyAddress = request.getParameter("BMaddress");
        int estAgeYear = Integer.parseInt(request.getParameter("BMage").trim());
        int estAgeMonth;
        String bodyID = request.getParameter("BMid");
        String bodyPassport;

        String incidentNumber = request.getParameter("inncidentNum");
        String bodyType = request.getParameter("BodyClass");

        try {

 
            body.setDeathRegisterNumber(deathRegNumber);
            body.setGender(bodyGender);
            body.setRace(bodyRace);
            body.setNameOfDeceased(bodyName);
            body.setSurnameOfDeceased(bodySurname);
            BodyAddress bodyAddressClass = new BodyAddress(bodyAddress, "", "", "", "", "", "", "");
            body.setBodyAddress(bodyAddressClass);
            body.setEstimatedAgeYear(estAgeYear);
            body.setID(bodyID);
            Incident inci = new Incident(incidentNumber);
            body.setIncident(inci);
            body.setBodyType(bodyType);

            BodyDb bodydb = new BodyDb(dbSet.getDbdetail(), body);
            bodydb.init();
            String BodySaveresults = bodydb.add();
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AddBody     File</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet   " + BodySaveresults + "</h1>");
            out.println("</body>");
            out.println("</html>");


        } catch (NullPointerException ex) {

            try {
                /* TODO output your page here. You may use following sample code. */
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Servlet AddBody     File</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Servlet  NULLLLL " + ex.toString() + "</h1>");
                out.println("</body>");
                out.println("</html>");
            } finally {
                out.close();
            }
        }
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
