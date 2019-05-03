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
import org.osgi.util.tracker.ServiceTracker;
import it.flyhawk.dictquery.DictQueryService;

public class QueryServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private BundleContext context;

	private ServiceTracker m_tracker = null;

	public QueryServlet(BundleContext context) {
		this.context = context;
	}

	public void init() throws ServletException {
		try {
			m_tracker = new ServiceTracker(context, context.createFilter("(&(objectClass=" + DictQueryService.class.getName() + ")" + "(queryType=*))"), null);
			m_tracker.open();
		} catch (InvalidSyntaxException e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("QueryServlet: starting processing the httpRequest.");
		String word = req.getParameter("word");
		String result = "";

		// 当有多个可用服务时，随机选择一个
		DictQueryService dictQueryService = (DictQueryService)m_tracker.getService();
		if(dictQueryService!=null) {
			// 当没有服务可用时，返回为null，因此需要判断是否为空。
			result = dictQueryService.queryWord(word);
		}else {
			result = "No DictQueryService Avaliable!";
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
		m_tracker.close();
	}
}
