/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import database.DbDetail;
import database.DeathCall;
import database.DeathCallDb;
import database.Incident;
import database.IncidentDb;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Administrator
 */
@WebServlet(name = "LogIncidentServlet", urlPatterns = {"/LogIncidentServlet"})
public class LogIncidentServlet extends HttpServlet {

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
        
        //initialize variables of the incident database
        Incident incident = new Incident();        
        incident.setIncidentLogNumber(request.getParameter("fpsnumber"));
        incident.setReferenceNumber(request.getParameter("SAPSnumber"));
        //String date = request.getParameter("detailyear")+"-"+request.getParameter("detailmonth")+"-"+request.getParameter("detailday");
        incident.setDateOfIncident(request.getParameter("IncidentDate"));
        String time = request.getParameter("InciBodyFoundTime") + ":00";
        incident.setTimeOfIncident(time);
        incident.setNumberOfBodies(Integer.parseInt(request.getParameter("numberofbodies" )));
        incident.setPlaceBodyFound(request.getParameter("placefound"));
        incident.setCircumstanceOfDeath(request.getParameter("circumstancesofdeath"));
        incident.setSpecialCircumstances(request.getParameter("specialcircumstance"));
        incident.setStatus(true);
        
        //Connects to the database. Initializes  url, dbName, username and password by calling DbDetail with four parameters;
        Tools t = new Tools();
        DbDetail dbdetail = t.getDbdetail();//here we initialize dbdtail with the four params 
        
        //Initializes IncidentDb with an Incident and Database Context. Where incident is the infor from the web and dbdetail is the database being populated
        IncidentDb incidentdb = new IncidentDb(incident, dbdetail);
        //connects to the database
        incidentdb.init();
        //insert Indicident data into database
        incidentdb.add();
        
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()); //Gets a string with the current date and the time as "yyyy-MM-dd HH:mm:ss"
        String[] datetime = timestamp.split(" "); //Split the timestamp into and array with the date then the time as [yyyy-MM-dd, HH:mm:ss]
        
        DeathCall dcall = new DeathCall();
        
        String calltime = request.getParameter("TimeOfCall")+ ":00"; 
        dcall.setTimeOfCall(calltime);// what are we doing here
        
        dcall.setDateOfCall(datetime[0]); //gets the date from the datetime array
        dcall.setNumberOfCaller(request.getParameter("phonenumber"));
        dcall.setNameOfCaller(request.getParameter("name"));
        dcall.setInstitution(request.getParameter("institution"));
        dcall.setSceneAddress(request.getParameter("address"));        
        dcall.setProvince(request.getParameter("province"));
        dcall.setRegion(request.getParameter("region"));
        dcall.setSceneConditions(request.getParameter("condition"));
        dcall.setIncident(incident);// what are we doing here
        
        DeathCallDb calldb = new DeathCallDb(dcall,dbdetail);
        calldb.init();// why are we initializing calldb after unlike incidentdb
        calldb.add();
        
        HttpSession sess = request.getSession();
        sess.setAttribute("incidentlogged", "Incident created succesully");// this is what we want on our jsp page Dispactch vehicle
        
        sess.setAttribute("incident", incident);// adds a property called incident to the session and it gives the data in the variable incident
        
        sess.setAttribute("new_lognumber",request.getParameter("fpsnumber"));
        
        String personnelnumber = sess.getAttribute("personnelnumber").toString();
        t.makeAuditTrail("Log Incident", "Created new incident "+request.getParameter("fpsnumber"), personnelnumber, "Log Incident Tab");
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
