    /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import AssistiveClasses.ClassSendMailTLS;
import AssistiveClasses.SetDbDetail;
import database.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Sandile
 */
public class ReferenceListServlet extends HttpServlet {

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
            out.println("<title>Servlet ReferenceListServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ReferenceListServlet at " + request.getContextPath() + "</h1>");
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
        //database connection
        SetDbDetail DBdet = new SetDbDetail();

        DbDetail dbDetail = DBdet.getDbdetail();;
        String result = "";

        //if it comes from add user form
        if (request.getParameter("form").equals("AddUser")) {
            //JOptionPane.showMessageDialog(response, "yey");
            try {
                String personnel = request.getParameter("personnelNumber");
                String password = random(8);

                String name = request.getParameter("firstName");
                String surname = request.getParameter("surname");
                String email = request.getParameter("email");
                ClassSendMailTLS sendmail = new ClassSendMailTLS();
                sendmail.sendMail(email.trim(), "Hi "+ name+" "+surname +"\n Your Login credentials to the Gauteng MMS system are as follows: \n Persal Number: "+ personnel+"\n Password: " + password+"\n \n Gauteng MMS Admin");
                boolean active = Boolean.valueOf(request.getParameter("active"));
                int level = Integer.parseInt(request.getParameter("level"));



                Employee emp = new Employee(personnel, password, name, surname, "Gauteng MMS", level, email, active);
                DatabaseConnector empDb = new EmployeeDb(emp, dbDetail);

                boolean status = empDb.init();
                result = empDb.add();

                if (result.equals("successful")) {
                    //if save is successful, return a message to page
                    HttpSession sess = request.getSession();
                    sess.setAttribute("main", "user");
                    sess.setAttribute("tab", "Adduser");
                    sess.setAttribute("result", "Employee has been successfuly saved to database");
                    response.sendRedirect("Admin.jsp");

                } else {
                    //if save is not successful
                    HttpSession sess = request.getSession();
                    sess.setAttribute("main", "user");
                    sess.setAttribute("tab", "Adduser");
                    sess.setAttribute("result", "Employee did not save: because " + result);
                    response.sendRedirect("Admin.jsp");

                }
            } catch (Exception ex) {
                HttpSession sess = request.getSession();
                String mess = "";
                sess.setAttribute("main", "user");
                sess.setAttribute("tab", "Adduser");
                if(ex.getMessage().contains("javax.mail")){
                  mess = "there is no internet connection found on this computer";
                }else{
                   mess= "an exception was found in ReferenceListServlet.java, the exception is: "+ex.getMessage();
                }
                sess.setAttribute("result",  "Employee did not save: because " + mess);
                response.sendRedirect("Admin.jsp");
            }

        } else if (request.getParameter("form").equals("AddInsitution")) {

            String insitutionName = request.getParameter("txtInsitution");

            ReferenceListDb emp = new ReferenceListDb("institution", "idInstitution", "type", insitutionName, dbDetail);
            emp.init();
            result = emp.add().trim();
            //if save is successful, return a message to page
            if (result.equals("successful")) {
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "insti");
                sess.setAttribute("insti", "Institution has been successfuly saved to database");
                response.sendRedirect("Admin.jsp");
            } else {
                //if save is not successful
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "insti");
                sess.setAttribute("insti", "Institution did not save: because " + result);
                response.sendRedirect("Admin.jsp");

            }

        } else if (request.getParameter("form").equals("AddAnalysis")) {
            //Get infor from text box
            String analysisName = request.getParameter("txtAnalysis");

            ReferenceListDb emp = new ReferenceListDb("analysis", "idAnalysis", "type", analysisName, dbDetail);
            emp.init();
            result = emp.add().trim();
            //if save is successful, return a message to page
            if (result.equals("successful")) {
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "analysis");
                sess.setAttribute("analysisResult", "Analysis Type has been successfuly saved to database");
                response.sendRedirect("Admin.jsp");
            } else {
                //if save is not successful
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "analysis");
                sess.setAttribute("analysisResult", "Analysis Type did not save: because " + result);
                response.sendRedirect("Admin.jsp");

            }


        } else if (request.getParameter("form").equals("AddProperty")) {

            //Get infor from text box
            String rankName = request.getParameter("txtProperty");

            ReferenceListDb emp = new ReferenceListDb("propertytype", "idPropertyType", "type", rankName, dbDetail);
            emp.init();
            result = emp.add().trim();
            //if save is successful, return a message to page
            if (result.equals("successful")) {
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "property");
                sess.setAttribute("propertyResult", "Property type has been successfuly saved to database");
                response.sendRedirect("Admin.jsp");
            } else {
                //if save is not successful
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "property");
                sess.setAttribute("rankResult", "Property type did not save because " + result);
                response.sendRedirect("Admin.jsp");
            }


        } else if (request.getParameter("form").equals("AddVehicle")) {

            //Get infor from text box
            String vehicleReNum = request.getParameter("txtVehicle");

            ReferenceListDb emp = new ReferenceListDb("vehicle", "e", "registrationNumber", vehicleReNum, dbDetail);
            emp.init();
            result = emp.add().trim();
            //if save is successful, return a message to page
            if (result.equals("successful")) {
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "vehi");
                sess.setAttribute("vehicleResult", "Vehicle registration number has been successfuly saved to database");
                response.sendRedirect("Admin.jsp");
            } else {
                //if save is not successful
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "vehi");
                sess.setAttribute("vehicleResult", "Vehicle registration number did not save because " + result);
                response.sendRedirect("Admin.jsp");

            }


        } else if (request.getParameter("form").equals("AddRank")) {

            //Get infor from text box
            String rankName = request.getParameter("txtRank");

            ReferenceListDb emp = new ReferenceListDb("rank", "idRank", "type", rankName, dbDetail);
            emp.init();
            result = emp.add().trim();
            //if save is successful, return a message to page
            if (result.equals("successful")) {
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "rank");
                sess.setAttribute("rankResult", "Rank type has been successfuly saved to database");
                response.sendRedirect("Admin.jsp");
            } else {
                //if save is not successful
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "rank");
                sess.setAttribute("rankResult", "Rank type did not save: because " + result);
                response.sendRedirect("Admin.jsp");

            }


        } else if (request.getParameter("form").equals("AddGender")) {
            //Code to store Gender to database
            //Get infor from text box
            String genderName = request.getParameter("txtGender");

            ReferenceListDb emp = new ReferenceListDb("gender", "idGender", "type", genderName, dbDetail);
            emp.init();
            result = emp.add().trim();
            //if save is successful, return a message to page
            if (result.equals("successful")) {
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "gender");
                sess.setAttribute("genderResult", "Gender type has been successfuly saved to database");
                response.sendRedirect("Admin.jsp");
            } else {
                //if save is not successful
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "gender");
                sess.setAttribute("genderResult", "Gender type did not save: because " + result);
                response.sendRedirect("Admin.jsp");

            }


        } else if (request.getParameter("form").equals("AddOccu")) {
            //Code to store Occupation type to database
            //Get infor from text box
            String occuName = request.getParameter("txtOccu");

            ReferenceListDb emp = new ReferenceListDb("occupation", "idOccupation", "type", occuName, dbDetail);
            emp.init();
            result = emp.add().trim();
            //if save is successful, return a message to page
            if (result.equals("successful")) {
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "occu");
                sess.setAttribute("occupationResult", "Occupation Category has been successfuly saved to database");
                response.sendRedirect("Admin.jsp");
            } else {
                //if save is not successful
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "occu");
                sess.setAttribute("occupationResult", "Occupation Category did not save: because " + result);
                response.sendRedirect("Admin.jsp");

            }


        } else if (request.getParameter("form").equals("AddRace")) {
            //Code to store Race type to database
            //Get infor from text box
            String raceName = request.getParameter("txtRace");

            ReferenceListDb emp = new ReferenceListDb("race", "idRace", "type", raceName, dbDetail);
            emp.init();
            result = emp.add().trim();
            //if save is successful, return a message to page
            if (result.equals("successful")) {
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "race");
                sess.setAttribute("raceResult", "Race type has been successfuly saved to database");
                response.sendRedirect("Admin.jsp");
            } else {
                //if save is not successful
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "race");
                sess.setAttribute("raceResult", "Race type did not save: because " + result);
                response.sendRedirect("Admin.jsp");

            }


        } else if (request.getParameter("form").equals("AddMarital")) {
            //Code to store Marital Status type to database   
            //Get infor from text box
            String MaritalName = request.getParameter("txtMarital");

            ReferenceListDb emp = new ReferenceListDb("maritalstatus", "idMartalStatus", "type", MaritalName, dbDetail);
            emp.init();
            result = emp.add().trim();
            //if save is successful, return a message to page
            if (result.equals("successful")) {
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "marital");
                sess.setAttribute("maritalResult", "Marital Status type has been successfuly saved to database");
                response.sendRedirect("Admin.jsp");
            } else {
                //if save is not successful
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "marital");
                sess.setAttribute("maritalResult", "Marital Status type did not save: because " + result);
                response.sendRedirect("Admin.jsp");

            }


        } else if (request.getParameter("form").equals("AddProvince")) {
            //Code to add province to database 
            //Get infor from text box
            String provinceName = request.getParameter("txtProvince");

            ReferenceListDb emp = new ReferenceListDb("province", "idProvince", "type", provinceName, dbDetail);
            emp.init();
            result = emp.add().trim();
            //if save is successful, return a message to page
            if (result.equals("successful")) {
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "province");
                sess.setAttribute("provinceResult", " The Province has been successfuly saved to database");
                response.sendRedirect("Admin.jsp");
            } else {
                //if save is not successful
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "province");
                sess.setAttribute("provinceResult", "The Province did not save: because " + result);
                response.sendRedirect("Admin.jsp");

            }


        } else if (request.getParameter("form").equals("AddRegion")) {
            //Code to store IDC10 codes to database 
            //Get infor from text box
            String regionName = request.getParameter("txtRegion");
            String provinceName = request.getParameter("ProvRegionList");
            HttpSession sess = request.getSession();
            sess.setAttribute("provTab", provinceName);
            ReferenceListDb emp = new ReferenceListDb("region", "e", "type", "province", regionName, provinceName, dbDetail);
            emp.init();
            result = emp.addRegion().trim();

            //if save is successful, return a message to page
            if (result.equals("successful")) {
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "region");
                sess.setAttribute("regionResult", "The region has been successfuly saved to database");
                response.sendRedirect("Admin.jsp");
            } else {
                //if save is not successful
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "region");
                sess.setAttribute("regionResult", "The region did not save: because " + result);
                response.sendRedirect("Admin.jsp");

            }


        } else if (request.getParameter("form").equals("AddManner")) {
            //Code to store Manner of death to database 
            //Get infor from text box
            String mannerName = request.getParameter("txtManner");

            ReferenceListDb emp = new ReferenceListDb("mannerofdeath", "idMannerOfDeath", "type", mannerName, dbDetail);
            emp.init();
            result = emp.add().trim();
            //if save is successful, return a message to page
            if (result.equals("successful")) {
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "manner");
                sess.setAttribute("mannerResult", "The manner of death has been successfuly saved to database");
                response.sendRedirect("Admin.jsp");
            } else {
                //if save is not successful
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "manner");
                sess.setAttribute("mannerResult", "The manner of death did not save: because " + result);
                response.sendRedirect("Admin.jsp");

            }


        } else if (request.getParameter("form").equals("AddSample")) {
            //Code to store Sanple types to database 
            //Get infor from text box
            String sampleName = request.getParameter("txtSample");

            ReferenceListDb emp = new ReferenceListDb("sample", "idSample", "type", sampleName, dbDetail);
            emp.init();
            result = emp.add().trim();
            //if save is successful, return a message to page
            if (result.equals("successful")) {
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "sample");
                sess.setAttribute("sampleResult", "Sample type has been successfuly saved to database");
                response.sendRedirect("Admin.jsp");
            } else {
                //if save is not successful
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "sample");
                sess.setAttribute("sampleResult", "Sample type did not save: because " + result);
                response.sendRedirect("Admin.jsp");

            }


        } else if (request.getParameter("form").equals("AddStatus")) {
            //Code to add body status category to database    
            //Get infor from text box
            String statusName = request.getParameter("txtStatus");

            ReferenceListDb emp = new ReferenceListDb("bodystatus", "idBodyStatus", "state", statusName, dbDetail);
            emp.init();
            result = emp.add().trim();
            //if save is successful, return a message to page
            if (result.equals("successful")) {
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "status");
                sess.setAttribute("statusResult", "Status Category has been successfuly saved to database");
                response.sendRedirect("Admin.jsp");
            } else {
                //if save is not successful
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "status");
                sess.setAttribute("statusResult", "Status category type did not save: because " + result);
                response.sendRedirect("Admin.jsp");

            }


        } else if (request.getParameter("form").equals("AddRelationship")) {
            //Get infor from text box
            String relationshipName = request.getParameter("txtRelationship");

            ReferenceListDb emp = new ReferenceListDb("relationship", "idRelationship", "type", relationshipName, dbDetail);
            emp.init();
            result = emp.add().trim();
            //if save is successful, return a message to page
            if (result.equals("successful")) {
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "relationship");
                sess.setAttribute("relationshipResult", "Relationship type has been successfuly saved to database");
                response.sendRedirect("Admin.jsp");
            } else {
                //if save is not successful
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "relationship");
                sess.setAttribute("relationshipResult", "Relationship type did not save: because " + result);
                response.sendRedirect("Admin.jsp");

            }


        } else if (request.getParameter("form").equals("AddBodyPart")) {
            //Get infor from text box
            String bodyPartName = request.getParameter("txtBodyPart");

            ReferenceListDb emp = new ReferenceListDb("bodypart", "id", "type", bodyPartName, dbDetail);
            emp.init();
            result = emp.add().trim();
            //if save is successful, return a message to page
            if (result.equals("successful")) {
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "bodyPart");
                sess.setAttribute("bodyPartResult", "Body part type has been successfuly saved to database");
                response.sendRedirect("Admin.jsp");
            } else {
                //if save is not successful
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "bodyPart");
                sess.setAttribute("bodyPartResult", "Body part type did not save because " + result);
                response.sendRedirect("Admin.jsp");

            }


        } else if (request.getParameter("form").equals("AddSpecialCur")) {
            //Get infor from text box
            String specialCurName = request.getParameter("txtSpecialCur");

            ReferenceListDb emp = new ReferenceListDb("specialcircumstance", "idSpecialCircumstance", "type", specialCurName, dbDetail);
            emp.init();
            result = emp.add().trim();
            //if save is successful, return a message to page
            if (result.equals("successful")) {
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "specialCur");
                sess.setAttribute("specialCurResult", "Special circumstance type has been successfuly saved to database");
                response.sendRedirect("Admin.jsp");
            } else {
                //if save is not successful
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "specialCur");
                sess.setAttribute("specialCurResult", "Special circumstance type did not save because " + result);
                response.sendRedirect("Admin.jsp");

            }


        } else if (request.getParameter("form").equals("AddExternalCause")) {
            String externalcause = request.getParameter("txtExternalCause");

            ReferenceListDb emp = new ReferenceListDb("externalcircumstance", "idExternalCircumstance", "type", externalcause, dbDetail);
            emp.init();
            result = emp.add().trim();
            //if save is successful, return a message to page
            if (result.equals("successful")) {
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "exCause");
                sess.setAttribute("externalCauseResults", "External cause type has been successfuly saved to database");
                response.sendRedirect("Admin.jsp");
            } else {
                //if save is not successful
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "exCause");
                sess.setAttribute("externalCauseResults", "External cause type did not save because " + result);
                response.sendRedirect("Admin.jsp");

            }



        } else if (request.getParameter("form").equals("AddSealType")) {
            String sealType = request.getParameter("txtSealType");
            ReferenceListDb emp = new ReferenceListDb("seal", "idSeal", "type", sealType, dbDetail);
            emp.init();
            result = emp.add().trim();
            //if save is successful, return a message to page
            if (result.equals("successful")) {
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "seal");
                sess.setAttribute("sealTypeResults", "Seal type has been successfuly saved to database");
                response.sendRedirect("Admin.jsp");
            } else {
                //if save is not successful
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "seal");
                sess.setAttribute("sealTypeResults", "Seal type did not save because " + result);
                response.sendRedirect("Admin.jsp");

            }

        } else if (request.getParameter("form").equals("AddScene")) {
            String scene = request.getParameter("txtScene");
            ReferenceListDb emp = new ReferenceListDb("scenetype", "idSceneType", "type", scene, dbDetail);
            emp.init();
            result = emp.add().trim();
            //if save is successful, return a message to page
            if (result.equals("successful")) {
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "scene");
                sess.setAttribute("sceneResult", "Scene type has been successfuly saved to database");
                response.sendRedirect("Admin.jsp");
            } else {
                //if save is not successful
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "scene");
                sess.setAttribute("sceneResult", "Scene type did not save because " + result);
                response.sendRedirect("Admin.jsp");

            }

        } else if (request.getParameter("form").equals("AddReleaseType")) {
            String releaseType = request.getParameter("txtReleaseType");
            ReferenceListDb emp = new ReferenceListDb("releasedtype", "idReleasedType", "type", releaseType, dbDetail);
            emp.init();
            result = emp.add().trim();
            //if save is successful, return a message to page
            if (result.equals("successful")) {
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "releaseType");
                sess.setAttribute("releaseTypeResult", "Release type has been successfuly saved to database");
                response.sendRedirect("Admin.jsp");
            } else {
                //if save is not successful
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "releaseType");
                sess.setAttribute("releaseTypeResult", "Release type did not save because " + result);
                response.sendRedirect("Admin.jsp");

            }

        } else if (request.getParameter("form").equals("AddReleaseTo")) {
            String releaseTo = request.getParameter("txtReleaseTo");
            ReferenceListDb emp = new ReferenceListDb("releasedto", "idReleasedTo", "type", releaseTo, dbDetail);
            emp.init();
            result = emp.add().trim();
            //if save is successful, return a message to page
            if (result.equals("successful")) {
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "releaseTo");
                sess.setAttribute("releaseToResult", "Release-to type has been successfuly saved to database");
                response.sendRedirect("Admin.jsp");
            } else {
                //if save is not successful
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "releaseTo");
                sess.setAttribute("releaseToResult", "Release-to type did not save because " + result);
                response.sendRedirect("Admin.jsp");

            }

        }else if (request.getParameter("form").equals("AddOrganisationType")) {
            String type = request.getParameter("txtOrganisationType");
            OrganisationType OrgType = new OrganisationType(type);
            OrganisationTypeDB emp = new OrganisationTypeDB(OrgType, dbDetail);
            emp.init();
            result = emp.add().trim();
            //if save is successful, return a message to page
            if (result.equals("successful")) {
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "organisationType");
                sess.setAttribute("organisationTypeResult", "Organisation type has been successfuly saved to database");
                response.sendRedirect("Admin.jsp");
            } else {
                //if save is not successful
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "organisationType");
                sess.setAttribute("organisationTypeResult", "Organisation type did not save because " + result);
                response.sendRedirect("Admin.jsp");

            }

        }else if (request.getParameter("form").equals("AddOrganisation")) {
            String name = request.getParameter("txtOrganisationName");
            String contact = request.getParameter("txtOrganisationContact");
            String type = request.getParameter("organisationTypeLst");
            Organization Org = new Organization(name, contact, type);
            OrganizationDb emp = new OrganizationDb(Org, dbDetail);
            emp.init();
            result = emp.add().trim();
            //if save is successful, return a message to page
            if (result.equals("successful")) {
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "organisation");
                sess.setAttribute("organisationResult", "Organisation has been successfuly saved to database");
                response.sendRedirect("Admin.jsp");
            } else {
                //if save is not successful
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "organisation");
                sess.setAttribute("organisationResult", "Organisation did not save because " + result);
                response.sendRedirect("Admin.jsp");

            }

        }else if (request.getParameter("form").equals("AddBodyStorage")) {
            String nane = request.getParameter("txtBodyStorageName");
            Integer size =  Integer.parseInt(request.getParameter("txtBodyStorageSize").trim());
            BodyStorage bs = new BodyStorage(size, nane);
            BodyStorageDb emp = new BodyStorageDb(dbDetail, bs);
            emp.init();
            result = emp.add().trim();
            //if save is successful, return a message to page
            if (result.equals("successful")) {
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "bodyStorage");
                sess.setAttribute("bodyStorageResult", "Body Storage has been successfuly saved to database");
                response.sendRedirect("Admin.jsp");
            } else {
                //if save is not successful
                HttpSession sess = request.getSession();
                sess.setAttribute("main", "ref");
                sess.setAttribute("tab", "bodyStorage");
                sess.setAttribute("bodyStorageResult", "Body Storage did not save because " + result);
                response.sendRedirect("Admin.jsp");

            }

        }
    }

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
