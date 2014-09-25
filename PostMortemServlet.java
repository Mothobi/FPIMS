/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import AssistiveClasses.SetDbDetail;
import database.BodyAtMortuary;
import database.BodyFile;
import database.BodyFileDb;
import database.PostMortem;
import database.PostMortemDb;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Mubien Nakhooda Coachlab 2013
 */
public class PostMortemServlet extends HttpServlet {

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
            
            //Needed for Postmortem Constructor
            BodyAtMortuary body = new BodyAtMortuary();
            body.setDeathRegisterNumber(request.getSession().getAttribute("death_register_number").toString());

            PostMortem postmortem = new PostMortem(
 
                    "",
 
 
                request.getParameter("ICDlevel1").split(" ")[0] + " " + request.getParameter("ICDlevel2").split(" ")[0] + " " + request.getParameter("ICDlevel3").split(" ")[0] + " " + request.getParameter("ICDlevel4").split(" ")[0],
                request.getParameter("findingsmortem"), //
                request.getParameter("findingsdeath"), //
                false,
                false,
                request.getParameter("findingsnumber"), //
                body,
                null //LabRecord, Has been removed from database but still exists in Postmortem Class?
            );

            PostMortemDb postmortemDB = new PostMortemDb(postmortem, dbSet.getDbdetail());
            postmortemDB.init();    
            System.out.println(postmortemDB.add());

            BodyFile bodyFile = new BodyFile(request.getSession().getAttribute("death_register_number").toString());
            BodyFileDb bodyFileDB = new BodyFileDb(dbSet.getDbdetail(), bodyFile);
            bodyFileDB.init();
            System.out.println(bodyFileDB.read());
            bodyFileDB.getBodyFile().setPostMortemCompleted(true);
            bodyFileDB.init();
            System.out.println(bodyFileDB.edit());
            
            request.getSession().setAttribute("_PostMortem", "true");
            response.sendRedirect("Home.jsp");
    }

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
