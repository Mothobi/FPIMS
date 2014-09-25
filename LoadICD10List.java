package servlets;

import AssistiveClasses.SetDbDetail;
import database.ICD10DB;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Sandile
 */
public class LoadICD10List extends HttpServlet {
    
    private String getString(ArrayList<String> list) {
        String tmp = "";
        
        for (String tmpString: list) {
            tmp += tmpString + "~";
        }
        
        return tmp;
    }
    
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
                
        if (request.getParameter("type").equals("loadICD2")) {
            PrintWriter out = response.getWriter();
            try {              
             
               out.println(getString(new ICD10DB(new SetDbDetail().getDbdetail()).filterICD10Code2(request.getParameter("data"))));
                
            } finally {
                out.close();
            }
        } else if (request.getParameter("type").equals("loadICD3")) {
            PrintWriter out = response.getWriter();
            try {
                
                out.println(getString(new ICD10DB(new SetDbDetail().getDbdetail()).filterICD10Code3(request.getParameter("data"))));
                
            } finally {
                out.close();
            }
        } else if (request.getParameter("type").equals("loadICD4")) {
            PrintWriter out = response.getWriter();
            try {
                
                out.println(getString(new ICD10DB(new SetDbDetail().getDbdetail()).filterICD10Code4(request.getParameter("data"))));
                
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
