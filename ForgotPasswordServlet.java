/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import AssistiveClasses.ClassSendMailTLS;
import AssistiveClasses.testMail;
import database.Employee;
import database.EmployeeDb;
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
 * @author Asheen
 */
@WebServlet(name = "ForgotPasswordServlet", urlPatterns = {"/ForgotPasswordServlet"})
public class ForgotPasswordServlet extends HttpServlet {

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
        Tools t = new Tools();
        
        String persalnum = request.getParameter("personnelnumber_forgot").toString();

        Employee e = new Employee();
        EmployeeDb edb = new EmployeeDb(e,t.getDbdetail());
        edb.init();  
        HttpSession sess = request.getSession();
        e = edb.fecthEmployee(persalnum);
        if(e == null){
            sess.setAttribute("error_reset", "Invalid persal number");
        }
        else{
            ClassSendMailTLS email = new ClassSendMailTLS();
            String password = t.makePassword(6);
            e.setPassword(password);
            EmployeeDb newedb = new EmployeeDb(e,t.getDbdetail());
            newedb.init();
            if(newedb.editPassword().contains("fail")){
                sess.setAttribute("error_reset", "Password could not be reset.");
            }
            else{
                try{
                    email.sendMail(e.getEmail(), "Dear "+e.getName()+" "+e.getSurname()+"\n \nYou have requested to reset your password. Your new password is: \n"+ password+"\nPlease log in with this password. You will be asked to choose another password.\n \nRegards\nGauteng Pathology Services");
                    sess.setAttribute("password_reset", "Please check your email for your new password.");
                }
                catch(Exception error){
                    sess.setAttribute("error_reset", "Password could not be reset.");
                }
                
            }            
        }
        t.makeAuditTrail("Reset Password", "User reset password", persalnum, "Forgot password page");
        response.sendRedirect("ForgotPassword.jsp");
        
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
