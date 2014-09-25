/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import database.DatabaseConnector;
import database.DbDetail;
import AssistiveClasses.SetDbDetail;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import database.Employee;
import database.EmployeeDb;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Sandile
 */
public class EditUser extends HttpServlet {

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
            out.println("<title>Servlet EditUser</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet EditUser at hi ih " + request.getParameter("Id") + "</h1>");
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
        String employeeID = "-";
        int id = 00;
        try {
            employeeID = request.getParameter("Id");
            SetDbDetail DBdet = new SetDbDetail();
            DbDetail dbDetail = DBdet.getDbdetail();

            //Get all employees
            EmployeeDb emplo = new EmployeeDb(dbDetail);
            emplo.init();
            ArrayList<Employee> employeeList = emplo.employeeList();
            int pos = -1;
            for (int i = 0; i < employeeList.size(); i++) {
                if (employeeList.get(i).getPersonnelNumber().trim().equals(employeeID.trim())) {
                    pos = i;

                }

            }
            Employee found = employeeList.get(pos);

            HttpSession sess = request.getSession();
            sess.setAttribute("name", found.getName());
            sess.setAttribute("surname", found.getSurname().trim());
            sess.setAttribute("email", found.getEmail().trim());
            id = found.getAccess();
            sess.setAttribute("personnel", found.getPersonnelNumber().trim());
            sess.setAttribute("level", found.getAccess());
            sess.setAttribute("active", found.isActive());
            sess.setAttribute("Eresult", "");

            response.sendRedirect("EditAdmin.jsp");

        } catch (SQLException ex) {
            Logger.getLogger(EditUser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            try {
                /* TODO output your page here. You may use following sample code. */
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Servlet EditUser</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Servlet Error: Not connected to database </h1>");
                out.println("</body>");
                out.println("</html>");
            } finally {
                out.close();
            }

        }

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
        SetDbDetail DBdet = new SetDbDetail();
        DbDetail dbDetail = DBdet.getDbdetail();
        if (request.getParameter("form").equals("EditUser")) {

            String name = request.getParameter("firstName");
            String surname = request.getParameter("surname");
            String personnel = request.getParameter("personnelNumber");
            String email = request.getParameter("email");

            boolean active = Boolean.valueOf(request.getParameter("active"));
            int level = Integer.parseInt(request.getParameter("level"));

            Employee emp = new Employee(personnel, "", name, surname, "Gauteng MMS", level, email, active);
            DatabaseConnector empDb = new EmployeeDb(emp, dbDetail);



            boolean status = empDb.init();
            String result = empDb.edit();
            if (result.equals("successful")) {
                //if save is successful, return a message to page
                HttpSession sess = request.getSession();
                sess.setAttribute("name", name);
                sess.setAttribute("surname", surname);
                sess.setAttribute("email", email);
                sess.setAttribute("personnel", personnel);
                sess.setAttribute("level", level);
                sess.setAttribute("active", active);
                sess.setAttribute("Eresult", "Employee has been successfuly edited");
                response.sendRedirect("EditAdmin.jsp");

            } else {
                //if save is not successful
                HttpSession sess = request.getSession();
                sess.setAttribute("name", name);
                sess.setAttribute("surname", surname);
                sess.setAttribute("email", email);
                sess.setAttribute("personnel", personnel);
                sess.setAttribute("level", level);
                sess.setAttribute("active", active);
                sess.setAttribute("Eresult", "Edit was not successful: because " + result);
                response.sendRedirect("EditAdmin.jsp");

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
