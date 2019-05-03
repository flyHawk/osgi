package it.flyahwk.dictqueryweb;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

import it.flyhawk.dictquery.DictQueryService;

public class QueryServlet extends HttpServlet implements ServiceListener {

	private static final long serialVersionUID = 1L;

	private BundleContext context;

	private ServiceReference<?> m_ref = null;
	private DictQueryService dictQueryService;

	public QueryServlet(BundleContext context) {
		this.context = context;
	}

	public void init() throws ServletException {
		synchronized (this) {
			try {
				context.addServiceListener(this,
						"(&(objectClass=" + DictQueryService.class.getName() + ")" + "(queryType=*))");
				ServiceReference<?>[] refs = null;
				refs = context.getServiceReferences(DictQueryService.class.getName(), "(queryType=*)");
				if (refs != null) {
					m_ref = refs[0];
					dictQueryService = (DictQueryService) context.getService(m_ref);
				}
			} catch (InvalidSyntaxException e) {
				e.printStackTrace();
			}

		}
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("QueryServlet: starting processing the httpRequest.");
		String word = req.getParameter("word");
		String result = "";

		if (dictQueryService != null) {
			result = dictQueryService.queryWord(word);
		} else {
			result = "No QueryService Avaliable.";
		}

		PrintWriter pw = new PrintWriter(resp.getOutputStream());
		pw.write(result);
		pw.close();
		System.out.println("QueryServlet: Ending processing the httpRequest.");
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	public void destroy() {
		if (m_ref != null) {
			context.ungetService(m_ref);
		}
		if (dictQueryService != null) {
			dictQueryService = null;
		}
	}

	@Override
	public synchronized void serviceChanged(ServiceEvent event) {

		if (event.getType() == ServiceEvent.REGISTERED) {
			if (m_ref == null) {
				m_ref = event.getServiceReference();
				dictQueryService = (DictQueryService) context.getService(m_ref);
			}
		} else if (event.getType() == ServiceEvent.UNREGISTERING) {
			if (event.getServiceReference() == m_ref) {
				context.ungetService(m_ref);
				m_ref = null;
				dictQueryService = null;

				ServiceReference<?>[] refs = null;
				try {
					refs = context.getServiceReferences(DictQueryService.class.getName(), "(queryType=*)");
				} catch (InvalidSyntaxException e) {
					e.printStackTrace();
				}
				if (refs != null) {
					m_ref = refs[0];
					dictQueryService = (DictQueryService) context.getService(m_ref);
				}
			}
		}
	}
}
