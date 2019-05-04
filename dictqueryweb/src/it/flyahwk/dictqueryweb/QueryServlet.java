package it.flyahwk.dictqueryweb;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

import it.flyhawk.dictquery.DictQueryService;

public class QueryServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private HttpService httpService; 
	private DictQueryService dictQueryService;
	private static final String SERVLET_ALIAS = "/query.do";
	private static final String STATIC_ALIAS = "/";

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("QueryServlet: starting processing the httpRequest.");
		String word = req.getParameter("word");
		String result = "";

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
	
	public void setHttpService(HttpService httpService) {
		this.httpService = httpService;
		registerWebResources();
	}
	
	public void unsetHttpService(HttpService httpService) {
		if( this.httpService != httpService) {
			return;
		}
		unregisterWebResources();
		this.httpService = null;
	}
	
	public void setDictQueryService(DictQueryService dictQueryService) {
		this.dictQueryService = dictQueryService;
	}
	
	public void unsetDictQueryService(DictQueryService dictQueryService) {
		if(this.dictQueryService != dictQueryService) {
			return;
		}
		this.dictQueryService = null;
	}
	
	private void registerWebResources() {
		HttpContext httpContext = httpService.createDefaultHttpContext();
		try {
			// 注册静态资源
			httpService.registerResources(STATIC_ALIAS, "/resources", httpContext);
			// 注册servlet
			httpService.registerServlet(SERVLET_ALIAS, this, null, httpContext);
		
		} catch (NamespaceException | ServletException e) {
			e.printStackTrace();
		}
	}

	private void unregisterWebResources() {
		httpService.unregister(STATIC_ALIAS);
		httpService.unregister(SERVLET_ALIAS);
	}
}
