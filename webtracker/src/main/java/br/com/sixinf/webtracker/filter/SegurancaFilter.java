/**
 * 
 */
package br.com.sixinf.webtracker.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import br.com.sixinf.webtracker.entidades.Usuario;

/**
 * @author maicon
 * 
 */
@WebFilter(filterName = "segurancaFilter", urlPatterns = { "*.xhtml" })
public class SegurancaFilter implements Filter {
	
	private static final Logger LOG = Logger.getLogger(SegurancaFilter.class); 

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		try {
			
			HttpServletRequest req = (HttpServletRequest) request;
			HttpServletResponse res = (HttpServletResponse) response;
			HttpSession ses = req.getSession(false);

			String reqURI = req.getRequestURI();
			if (reqURI.indexOf(".xhtml") >= 0
					&& reqURI.indexOf("login.xhtml") < 0 
					&& reqURI.indexOf("autocadastro.xhtml") < 0
					&& reqURI.indexOf("javax.faces.resource") < 0
					&& (ses == null || ses.getAttribute(Usuario.SESSION_ID) == null)) {
				req.getRequestDispatcher("/pages/login.xhtml")
						.forward(req, res);
			} else {
				chain.doFilter(request, response);
			}
			
		} catch (Throwable t) {
			LOG.error("Erro no filtro de requisicoes 'SegurancaFilter'", t);
		}

	}

	@Override
	public void init(FilterConfig config) throws ServletException {

	}

}
