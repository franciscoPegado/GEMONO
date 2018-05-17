/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.ce.esp.filter;

import java.io.IOException;
import java.util.logging.LogRecord;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author francisco
 */
public class FilterLogin implements javax.servlet.Filter {

    private FilterConfig filterConfig = null;

    public void destroy() {
    }
    
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest rq = (HttpServletRequest) request;
        HttpServletResponse rs = (HttpServletResponse) response;
        HttpSession session = rq.getSession();
        String usuario = (String) session.getAttribute(LoginManager.SESSION_USU);
        if (usuario == null) {
            StringBuffer url = rq.getRequestURL();
            int pos = url.indexOf("admin");
            if (pos != -1) {
                rs.sendRedirect(session.getServletContext().getContextPath() + "/faces/loginAdmin.xhtml");//Se não logado devolve para a página de login
            } else {
                rs.sendRedirect(session.getServletContext().getContextPath() + "/faces/index.xhtml");//Se não logado devolve para a página de login
            }
        } else {
            chain.doFilter(rq, rs);//Se tudo ok continua, esse método diz ao conteiner que está tudo bem e pode continuar com a solicitação.
        }
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    public boolean isLoggable(LogRecord record) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
