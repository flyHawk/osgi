package it.flyahwk.dictqueryweb;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import it.flyhawk.dictquery.DictQueryService;

public class QueryServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private BundleContext context;
	public QueryServlet(BundleContext context) {
		this.context = context;
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("QueryServlet: starting processing the httpRequest.");
		String word = req.getParameter("word");
		String result = "";
		// 获取服务
		DictQueryService dictQueryService = null;
		ServiceReference<?> sr = context.getServiceReference(DictQueryService.class.getName());
		if( sr != null) {
			dictQueryService = (DictQueryService)context.getService(sr);
			result = dictQueryService.queryWord(word);
			context.ungetService(sr);
		}else {
			result = "No QueryService Avaliable.";
		}
		
		PrintWriter pw = new PrintWriter(resp.getOutputStream());
		pw.write(result);
		pw.close();
		System.out.println("QueryServlet: Ending processing the httpRequest.");
	}
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req,resp);
	}
}
