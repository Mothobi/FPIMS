/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import AssistiveClasses.SetDbDetail;
import database.Body;
import database.BodyAtMortuary;
import database.BodyDb;
import database.BodyFile;
import database.BodyFileDb;
import database.DbDetail;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Mubien Nakhooda Coachlab 2013
 */
public class LinkBodyContent extends HttpServlet {

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
            PrintWriter out = response.getWriter();

            try {

                DbDetail dbDetail = new SetDbDetail().getDbdetail();

                Body body = new BodyAtMortuary();
                body.setDeathRegisterNumber(request.getParameter("data"));

                BodyDb bodyDB = new BodyDb(dbDetail, body);
                bodyDB.init();
                bodyDB.read();

                body = bodyDB.getBody();
                String id = body.getID();
                if (id.equals("")) {
                    id = body.getPassport();
                }

                BodyFile bodyFile = new BodyFile();
                bodyFile.setDeathRegisterNumber(request.getParameter("data"));

                BodyFileDb bodyFileDB = new BodyFileDb(dbDetail, bodyFile);
                bodyFileDB.init();
                bodyFileDB.read();
                bodyFile = bodyFileDB.getBodyFile();

                out.println(body.getDeathRegisterNumber() + " " + body.getNameOfDeceased() + " "
                        + body.getSurnameOfDeceased() + " " + id + " " + bodyFile.isBodyIdentified());
            } finally {
                out.close();
            }
        } else if (request.getParameter("type").equals("save")) {
            
                BodyFileDb bodyFileDB = new BodyFileDb(new SetDbDetail().getDbdetail());
                bodyFileDB.init();
                System.out.println(bodyFileDB.linkBody(request.getParameter("data1"), request.getParameter("data2")));                
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
