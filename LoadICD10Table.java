/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Sandile
 */
public class LoadICD10Table extends HttpServlet {

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
            out.println("<title>Servlet LoadICD10Table</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet LoadICD10Table at " + request.getContextPath() + "</h1>");
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

        String num = request.getParameter("ICD10table").toString();
        HttpSession sess = request.getSession();
        /*  
         PrintWriter out = response.getWriter();
         try {
         /* TODO output your page here. You may use following sample code. 
         out.println("<!DOCTYPE html>");
         out.println("<html>");
         out.println("<head>");
         out.println("<title>Servlet LoadICD10Table</title>");
         out.println("</head>");
         out.println("<body>");
         out.println("<h1>Servlet LoadICD10Table at ---" + num+ " </h1>");
         out.println("</body>");
         out.println("</html>");
         } finally {
         out.close();
         } */
        sess.setAttribute("one", "false");
        sess.setAttribute("two", "false");
        sess.setAttribute("three", "false");
        sess.setAttribute("four", "false");
        if (num.contains("1")) {
            sess.setAttribute("one", "true");

        } else if (num.contains("2")) {
            sess.setAttribute("two", "true");

        } else if (num.contains("3")) {
            sess.setAttribute("three", "true");

        } else if (num.contains("4")) {
            sess.setAttribute("four", "true");
        }
        sess.setAttribute("main", "ref");
        sess.setAttribute("tab", "icd10");
        sess.setAttribute("tableN", num.trim());
        sess.setAttribute("iCD10Result", "Life");
        response.sendRedirect("Admin.jsp");
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
