package servlets;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import database.EmployeeDb;
import database.Employee;
import database.DatabaseConnector;
import database.DbDetail;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.JOptionPane;

/**
 *
 * @author Sandile
 */
public class Handler extends HttpServlet {

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
            out.println("<title>Servlet Handler</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Handler at " + request.getContextPath() + "</h1>");
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

        String result = "";

           //if it comes from add user form
        if (request.getParameter("form").equals("AddUser")) {
            //JOptionPane.showMessageDialog(response, "yey");
            System.out.println("right place!");
            String personnel = request.getParameter("personnelNumber");
            String password = random(8);
            String name = request.getParameter("firstName");
            String surname = request.getParameter("surname");
            String rank = request.getParameter("rank");
            String email = request.getParameter("email");
             
            boolean active = Boolean.valueOf(request.getParameter("Active"));
            String level = request.getParameter("level");

            int numLevel = 0;
            if (level.equals("Supervisor")) {
                numLevel = 1;
            } else if (level.equals("FPS Officer")) {
                numLevel = 2;
            } else if (level.equals("Pathologist")) {
                numLevel = 3;
            } else if (level.equals("Administrator")) {
                numLevel = 4;
            }
            DbDetail dbDetail = new DbDetail("/mydb", "localhost", "root", "msandas777");
            Employee emp = new Employee(personnel, password, name, surname, rank, numLevel, email, active);
            DatabaseConnector empDb = new EmployeeDb(emp, dbDetail);
            boolean status = empDb.init();
            result = empDb.add();

            if (result.equals("successful")) {
               //if save is successful, return a message to page
                HttpSession sess = request.getSession();
                sess.setAttribute("tab", "AddUser");
                sess.setAttribute("result", "Employee has been successfuly saved to database");
                response.sendRedirect("/Home.jsp");
                
            } else {
                //if save is not successful
                   HttpSession sess = request.getSession();
                sess.setAttribute("tab", "AddUser");
                sess.setAttribute("result", result );
                response.sendRedirect("/Home.jsp");
                
                
             
          
       

        }

        }
    }
    //returns the page name; Got it from http://stackoverflow.com/questions/6007678/how-to-get-page-name-in-jsp-or-jstl

    public static String extractPageNameFromURLString(String urlString) {
        if (urlString == null) {
            return null;
        }
        int lastSlash = urlString.lastIndexOf("/");
        //if (lastSlash==-1) lastSlash = 0;
        String pageAndExtensions = urlString.substring(lastSlash + 1);
        int lastQuestion = pageAndExtensions.lastIndexOf("?");
        if (lastQuestion == -1) {
            lastQuestion = pageAndExtensions.length();
        }
        String result = pageAndExtensions.substring(0, lastQuestion);
        return result;
    }
    //generated password of length n of type string

    private String random(int n) {

        char[] chars = "abcdefghijklmnopqrstuvwxyz1234567890".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();

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
