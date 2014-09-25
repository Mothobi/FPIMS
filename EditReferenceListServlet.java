/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import AssistiveClasses.ClassSendMailTLS;
import AssistiveClasses.SetDbDetail;
import database.DatabaseConnector;
import database.DbDetail;
import database.Employee;
import database.EmployeeDb;
import database.OrganisationType;
import database.OrganisationTypeDB;
import database.ReferenceListDb;
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
public class EditReferenceListServlet extends HttpServlet {

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
            out.println("<title>Servlet EditReferenceListServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet EditReferenceListServlet at " + request.getContextPath() + "</h1>");
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
        //this is the new item being edited 
        String item = request.getParameter("item");
        String Olditem = request.getParameter("Olditem");
        String result = "";
        SetDbDetail DBdet = new SetDbDetail();
        DbDetail dbDetail = DBdet.getDbdetail();


        if (request.getParameter("table").equals("Insitution")) {

            ReferenceListDb emp = new ReferenceListDb("institution", "idInstitution", "type", Olditem, dbDetail);
            emp.setNewData(item);
            emp.init();
            result = emp.edit().trim();
            //if save is successful, return a message to page
            if (result.equals("successful")) {
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "insti");
                sess.setAttribute("insti", "Institution has been successfuly edited to database ");
                response.sendRedirect("Admin.jsp");
            } else {
                //if save is not successful
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "insti");
                sess.setAttribute("insti", "Institution did not edit because " + result);
                response.sendRedirect("Admin.jsp");
            }

        } else if (request.getParameter("table").equals("analysis")) {

            ReferenceListDb emp = new ReferenceListDb("analysis", "idAnalysis", "type", Olditem, dbDetail);
            emp.setNewData(item);
            emp.init();
            result = emp.edit().trim();
            //if save is successful, return a message to page
            if (result.equals("successful")) {
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "analysis");
                sess.setAttribute("analysisResult", "Analysis Type has been successfuly edited");
                response.sendRedirect("Admin.jsp");
            } else {
                //if save is not successful
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "analysis");
                sess.setAttribute("analysisResult", "Analysis Type did not edit because " + result);
                response.sendRedirect("Admin.jsp");

            }


        } else if (request.getParameter("table").equals("propertytype")) {

            ReferenceListDb emp = new ReferenceListDb("propertytype", "idPropertyType", "type", Olditem, dbDetail);
            emp.setNewData(item);
            emp.init();
            result = emp.edit().trim();
            //if save is successful, return a message to page
            if (result.equals("successful")) {
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "property");
                sess.setAttribute("propertyResult", "Property type has been successfuly edited");
                response.sendRedirect("Admin.jsp");
            } else {
                //if save is not successful
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "property");
                sess.setAttribute("rankResult", "Property type did not edit because " + result);
                response.sendRedirect("Admin.jsp");
            }


        } else if (request.getParameter("table").equals("vehicle")) {

            ReferenceListDb emp = new ReferenceListDb("vehicle", "e", "registrationNumber", Olditem, dbDetail);
            emp.setNewData(item);
            emp.init();
            result = emp.edit().trim();
            //if save is successful, return a message to page
            if (result.equals("successful")) {
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "vehi");
                sess.setAttribute("vehicleResult", "Vehicle registration number has been successfuly edited");
                response.sendRedirect("Admin.jsp");
            } else {
                //if save is not successful
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "vehi");
                sess.setAttribute("vehicleResult", "Vehicle registration number did not edit because " + result);
                response.sendRedirect("Admin.jsp");

            }


        } else if (request.getParameter("table").equals("rank")) {

            ReferenceListDb emp = new ReferenceListDb("rank", "idRank", "type", Olditem, dbDetail);
            emp.setNewData(item);
            emp.init();
            result = emp.edit().trim();
            //if save is successful, return a message to page
            if (result.equals("successful")) {
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "rank");
                sess.setAttribute("rankResult", "Rank type has been successfuly edited");
                response.sendRedirect("Admin.jsp");
            } else {
                //if save is not successful
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "rank");
                sess.setAttribute("rankResult", "Rank type did not edit because " + result);
                response.sendRedirect("Admin.jsp");

            }


        } else if (request.getParameter("table").equals("gender")) {

            ReferenceListDb emp = new ReferenceListDb("gender", "idGender", "type", Olditem, dbDetail);
            emp.setNewData(item);
            emp.init();
            result = emp.edit().trim();
            //if save is successful, return a message to page
            if (result.equals("successful")) {
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "gender");
                sess.setAttribute("genderResult", "Gender type has been successfuly edited");
                response.sendRedirect("Admin.jsp");
            } else {
                //if save is not successful
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "gender");
                sess.setAttribute("genderResult", "Gender type did not edit: because " + result);
                response.sendRedirect("Admin.jsp");

            }


        } else if (request.getParameter("table").equals("occupation")) {

            ReferenceListDb emp = new ReferenceListDb("occupation", "idOccupation", "type", Olditem, dbDetail);
            emp.setNewData(item);
            emp.init();
            result = emp.edit().trim();
            //if save is successful, return a message to page
            if (result.equals("successful")) {
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "occu");
                sess.setAttribute("occupationResult", "Occupation Category has been successfuly edited");
                response.sendRedirect("Admin.jsp");
            } else {
                //if save is not successful
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "occu");
                sess.setAttribute("occupationResult", "Occupation Category did not edit: because " + result);
                response.sendRedirect("Admin.jsp");

            }


        } else if (request.getParameter("table").equals("race")) {

            ReferenceListDb emp = new ReferenceListDb("race", "idRace", "type", Olditem, dbDetail);
            emp.setNewData(item);
            emp.init();
            result = emp.edit().trim();
            //if save is successful, return a message to page
            if (result.equals("successful")) {
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "race");
                sess.setAttribute("raceResult", "Race type has been successfuly edited");
                response.sendRedirect("Admin.jsp");
            } else {
                //if save is not successful
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "race");
                sess.setAttribute("raceResult", "Race type did not edit: because " + result);
                response.sendRedirect("Admin.jsp");

            }


        } else if (request.getParameter("table").equals("maritalstatus")) {

            ReferenceListDb emp = new ReferenceListDb("maritalstatus", "idMartalStatus", "type", Olditem, dbDetail);
            emp.setNewData(item);
            emp.init();
            result = emp.edit().trim();
            //if save is successful, return a message to page
            if (result.equals("successful")) {
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "marital");
                sess.setAttribute("maritalResult", "Marital Status type has been successfuly edited");
                response.sendRedirect("Admin.jsp");
            } else {
                //if save is not successful
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "marital");
                sess.setAttribute("maritalResult", "Marital Status type did not edit: because " + result);
                response.sendRedirect("Admin.jsp");

            }


        } else if (request.getParameter("table").equals("province")) {

            ReferenceListDb emp = new ReferenceListDb("province", "idProvince", "type", Olditem, dbDetail);
            emp.setNewData(item);
            emp.init();
            result = emp.edit().trim();
            //if save is successful, return a message to page
            if (result.equals("successful")) {
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "province");
                sess.setAttribute("provinceResult", " The Province has been successfuly edited");
                response.sendRedirect("Admin.jsp");
            } else {
                //if save is not successful
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "province");
                sess.setAttribute("provinceResult", "The Province did not edit: because " + result);
                response.sendRedirect("Admin.jsp");

            }


        } else if (request.getParameter("table").equals("region")) {
            HttpSession sess = request.getSession();
            String province = request.getParameter("editProv");
            ReferenceListDb emp = new ReferenceListDb("region", "e", "type", "province", Olditem, province, dbDetail);
            emp.setNewData(item);
            emp.init();
            sess.setAttribute("provTab", province);
            result = emp.editReg().trim();
            //if save is successful, return a message to page
            if (result.equals("successful")) {

                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "region");
                sess.setAttribute("regionResult", "The region has been successfuly edited");
                response.sendRedirect("Admin.jsp");
            } else {
                //if save is not successful
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "region");
                sess.setAttribute("regionResult", "The region did not edit: because " + result);
                response.sendRedirect("Admin.jsp");

            }

        } else if (request.getParameter("table").equals("mannerofdeath")) {

            ReferenceListDb emp = new ReferenceListDb("mannerofdeath", "idMannerOfDeath", "type", Olditem, dbDetail);
            emp.init();
            emp.setNewData(item);
            emp.init();
            result = emp.edit().trim();
            //if save is successful, return a message to page
            if (result.equals("successful")) {
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "manner");
                sess.setAttribute("mannerResult", "The manner of death has been successfuly edited");
                response.sendRedirect("Admin.jsp");
            } else {
                //if save is not successful
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "manner");
                sess.setAttribute("mannerResult", "The manner of death did not edit: because " + result);
                response.sendRedirect("Admin.jsp");

            }


        } else if (request.getParameter("table").equals("sample")) {

            ReferenceListDb emp = new ReferenceListDb("sample", "idSample", "type", Olditem, dbDetail);
            emp.setNewData(item);
            emp.init();
            result = emp.edit().trim();
            //if save is successful, return a message to page
            if (result.equals("successful")) {
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "sample");
                sess.setAttribute("sampleResult", "Sample type has been successfuly edited");
                response.sendRedirect("Admin.jsp");
            } else {
                //if save is not successful
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "sample");
                sess.setAttribute("sampleResult", "Sample type did not edit because " + result);
                response.sendRedirect("Admin.jsp");

            }


        } else if (request.getParameter("table").equals("bodystatus")) {

            ReferenceListDb emp = new ReferenceListDb("bodystatus", "idBodyStatus", "state", Olditem, dbDetail);
            emp.setNewData(item);
            emp.init();
            result = emp.edit().trim();
            //if save is successful, return a message to page
            if (result.equals("successful")) {
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "status");
                sess.setAttribute("statusResult", "Status Category has been successfuly edited");
                response.sendRedirect("Admin.jsp");
            } else {
                //if save is not successful
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "status");
                sess.setAttribute("statusResult", "Status category type did not edit: because " + result);
                response.sendRedirect("Admin.jsp");

            }


        } else if (request.getParameter("table").equals("relationship")) {

            ReferenceListDb emp = new ReferenceListDb("relationship", "idRelationship", "type", Olditem, dbDetail);
            emp.setNewData(item);
            emp.init();
            result = emp.edit().trim();
            //if save is successful, return a message to page
            if (result.equals("successful")) {
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "relationship");
                sess.setAttribute("relationshipResult", "Relationship type has been successfuly edited");
                response.sendRedirect("Admin.jsp");
            } else {
                //if save is not successful
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "relationship");
                sess.setAttribute("relationshipResult", "Relationship type did not edit because " + result);
                response.sendRedirect("Admin.jsp");

            }


        } else if (request.getParameter("table").equals("bodypart")) {

            ReferenceListDb emp = new ReferenceListDb("bodypart", "id", "type", Olditem, dbDetail);
            emp.setNewData(item);
            emp.init();
            result = emp.edit().trim();
            //if save is successful, return a message to page
            if (result.equals("successful")) {
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "bodyPart");
                sess.setAttribute("bodyPartResult", "Body part type has been successfuly edited");
                response.sendRedirect("Admin.jsp");
            } else {
                //if save is not successful
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "bodyPart");
                sess.setAttribute("bodyPartResult", "Body part type did not edit because " + result);
                response.sendRedirect("Admin.jsp");

            }


        } else if (request.getParameter("table").equals("specialcircumstance")) {

            ReferenceListDb emp = new ReferenceListDb("specialcircumstance", "idSpecialCircumstance", "type", Olditem, dbDetail);
            emp.setNewData(item);
            emp.init();
            result = emp.edit().trim();
            //if save is successful, return a message to page
            if (result.equals("successful")) {
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "specialCur");
                sess.setAttribute("specialCurResult", "Special circumstance type has been successfuly edited to database");
                response.sendRedirect("Admin.jsp");
            } else {
                //if save is not successful
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "specialCur");
                sess.setAttribute("specialCurResult", "Special circumstance type did not edit because " + result);
                response.sendRedirect("Admin.jsp");

            }


        } else if (request.getParameter("table").equals("externalcircumstance")) {

            ReferenceListDb emp = new ReferenceListDb("externalcircumstance", "idExternalCircumstance", "type", Olditem, dbDetail);
            emp.setNewData(item);
            emp.init();
            result = emp.edit().trim();
            //if save is successful, return a message to page
            if (result.equals("successful")) {
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "exCause");
                sess.setAttribute("externalCauseResults", "External cause type has been successfuly edited to database");
                response.sendRedirect("Admin.jsp");
            } else {
                //if save is not successful
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "exCause");
                sess.setAttribute("externalCauseResults", "External cause type did not edit because " + result);
                response.sendRedirect("Admin.jsp");

            }


        } else if (request.getParameter("table").equals("seal")) {
            ReferenceListDb emp = new ReferenceListDb("seal", "idSeal", "type", Olditem, dbDetail);
            emp.setNewData(item);
            emp.init();
            result = emp.edit().trim();
            //if save is successful, return a message to page
            if (result.equals("successful")) {
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "seal");
                sess.setAttribute("sealTypeResults", "Seal type has been successfuly edited to database");
                response.sendRedirect("Admin.jsp");
            } else {
                //if save is not successful
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "seal");
                sess.setAttribute("sealTypeResults", "Seal type did not edit because " + result);
                response.sendRedirect("Admin.jsp");

            }

        } else if (request.getParameter("table").equals("scene")) {
            ReferenceListDb emp = new ReferenceListDb("scenetype", "idSceneType", "type", Olditem, dbDetail);
            emp.setNewData(item);
            emp.init();
            result = emp.edit().trim();
            //if save is successful, return a message to page
            if (result.equals("successful")) {
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "scene");
                sess.setAttribute("sceneResult", "Scene type has been successfuly deleted");
                response.sendRedirect("Admin.jsp");
            } else {
                //if save is not successful
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "scene");
                sess.setAttribute("sceneResult", "Scene type did not delete because " + result);
                response.sendRedirect("Admin.jsp");

            }

        } else if (request.getParameter("table").equals("releasedtype")) {
            ReferenceListDb emp = new ReferenceListDb("releasedtype", "idReleasedType", "type", Olditem, dbDetail);
            emp.setNewData(item);
            emp.init();
            result = emp.edit().trim();
            //if save is successful, return a message to page
            if (result.equals("successful")) {
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "releaseType");
                sess.setAttribute("releaseTypeResult", "Release type has been successfuly deleted");
                response.sendRedirect("Admin.jsp");
            } else {
                //if save is not successful
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "releaseType");
                sess.setAttribute("releaseTypeResult", "Release type did not delete because " + result);
                response.sendRedirect("Admin.jsp");

            }

        } else if (request.getParameter("table").equals("releasedto")) {
            ReferenceListDb emp = new ReferenceListDb("releasedto", "idReleasedTo", "type", Olditem, dbDetail);
            emp.setNewData(item);
            emp.init();
            result = emp.edit().trim();
            //if save is successful, return a message to page
            if (result.equals("successful")) {
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "releaseTo");
                sess.setAttribute("releaseToResult", "Release-To type has been successfuly deleted");
                response.sendRedirect("Admin.jsp");
            } else {
                //if save is not successful
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "releaseTo");
                sess.setAttribute("releaseToResult", "Release-To type did not delete because " + result);
                response.sendRedirect("Admin.jsp");

            }

        }else if (request.getParameter("table").equals("organizationtype")) {
             
            OrganisationType OrgType = new OrganisationType(Olditem);
            OrganisationTypeDB emp = new OrganisationTypeDB(OrgType, dbDetail);
            emp.init();
            result = emp.delete();
            //if save is successful, return a message to page
            if (result.equals("successful")) {
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "organisationType");
                sess.setAttribute("organisationTypeResult", "Organisation type has been successfuly edited  ");
                response.sendRedirect("Admin.jsp");
            } else {
                //if save is not successful
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "organisationType");
                sess.setAttribute("organisationTypeResult", "Organisation type did not edit because " + result );
                response.sendRedirect("Admin.jsp");

            }

        } else {

            PrintWriter out = response.getWriter();
            try {
                /* TODO output your page here. You may use following sample code. */
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Servlet edit</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Servlet edit at " + request.getContextPath() + "</h1>");
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
