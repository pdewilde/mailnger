package mailnger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import emailvalidator.*;

/**
 * Created by Parker on 6/27/2017.
 *
 * Takes
 */
@WebServlet(name = "Process")
public class Process extends HttpServlet {

    // Calls doGet
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Set response content type
        response.setContentType("text/html; charset=UTF-8");

        // Get Writer to Write HTML to Browser
        PrintWriter out = response.getWriter();


        try {
            // Boilerplate html
            out.println("<!doctype html>");
            out.println("");
            out.println("<html lang=\"en\">");
            out.println("<head>");
            out.println("  <meta charset=\"utf-8\">");
            out.println("  <title>Mailnger | Done</title>");
            out.println("");
            out.println("</head>");
            out.println("");
            out.println("<body>");
            out.println("  <h2>Mail List Self Service</h2>");

            // read user responses

            // Email address
            String email = request.getParameter("email").trim();

            // Ensure Email is valid
            EmailValidator validator = EmailValidator.getInstance();
            boolean isEmail = validator.isValid(email);

            // Project
            String project = request.getParameter("project").trim();

            // Any nonempty string is considered valid
            // Still needs to be SQL sanitized
            boolean validProject = false;
            if (project != null && project.length() != 0) {
                validProject = true;
            }

            // User Subscribing or Unsubscribing
            boolean subscribe = false;
            if (request.getParameter("action").equals("subscribe"))
                subscribe = true;

            //Check Validity Of Inputs
            if (isEmail && validProject) {
                // We Will Update Database and Inform User of Results
                boolean databaseUpdated = Updater.update(email, project, subscribe);
                if(databaseUpdated == true)
                    out.println("  <h3>Updated Successfully!</h3>");
                else {
                    out.println("  <h3>Sorry, Something Went Wrong On Our End</h3>");
                }
            } else {
                // Tell User What They Did Wrong
                if (!isEmail)
                    out.println("  <h3>Invalid Email :'" + email +"'</h3>");
                if (!validProject) {
                    out.println("  <h3>Invalid Project :'" + project +"'</h3>");
                }
            }

            // Footer
            out.println("<a href=\"\\\">Return To Home</a>");
            out.println("</body>");
            out.println("</html>");
        } finally {
            out.close();
        }
    }
}
