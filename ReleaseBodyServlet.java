/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import AssistiveClasses.SetDbDetail;
import database.BodyAtMortuary;
import database.BodyDb;
import database.BodyFile;
import database.BodyFileDb;
import database.DbDetail;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Mubien Nakhooda Coachlab 2013
 */
public class ReleaseBodyServlet extends HttpServlet {

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
        if (request.getParameter("type").equals("load")) {
            
        DbDetail dbDetail = new SetDbDetail().getDbdetail();
        
            PrintWriter out = response.getWriter();
            try {      
                
                BodyFileDb bodyFileDb = new BodyFileDb(dbDetail);
                bodyFileDb.init();
                
                ArrayList<BodyAtMortuary> list = bodyFileDb.getBodyLinkList(request.getParameter("data"));
                String result = "";
                
                for (BodyAtMortuary tmp : list) {
                    
                    BodyFile bodyFile = new BodyFile(tmp.getDeathRegisterNumber());
                    BodyFileDb bodyFileDB = new BodyFileDb(dbDetail, bodyFile);
                    bodyFileDB.init();
                    bodyFileDB.read();
                    
                    result += tmp.getDeathRegisterNumber() + "`"
                            + tmp.getNameOfDeceased() + "`"
                            + tmp.getSurnameOfDeceased() + "`"
                            + tmp.getID() + "`";
                    
                    result += bodyFileDB.getBodyFile().isBodyIdentified();
                    result += "~";
                }
                
                out.println(result);
            } finally {
                out.close();
            }
        } 
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
