package org.markdownj.dingus;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.markdownj.MarkdownProcessor;

public class DingusServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if(getCurrentPath(request).equals("/")) {
            request.setAttribute("showSource",  false);
            request.setAttribute("showPreview", false);
            request.getRequestDispatcher("dingus.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Page Not Found");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if(getCurrentPath(request).equals("/")) {
            MarkdownProcessor markdown = new MarkdownProcessor();
            String markup = request.getParameter("markdown");
            String view   = request.getParameter("view");
            boolean showSource  = true;
            boolean showPreview = true;

            if (view != null) {
                if ("source".equals(view)) {
                    showSource  = true;
                    showPreview = false;
                } else if ("preview".equals(view)) {
                    showSource  = false;
                    showPreview = true;
                } // otherwise, default to showing both
            }

            request.setAttribute("markup", markup);
            request.setAttribute("html",   markdown.markdown(markup));
            request.setAttribute("showSource",  showSource);
            request.setAttribute("showPreview", showPreview);
            request.getRequestDispatcher("dingus.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Page Not Found");
        }
    }

    private String getCurrentPath(HttpServletRequest request) {
        return request.getRequestURI().substring(request.getContextPath().length());
    }
}
