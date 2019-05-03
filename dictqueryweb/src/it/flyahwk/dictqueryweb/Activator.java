package it.flyahwk.dictqueryweb;

import javax.servlet.ServletException;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

public class Activator implements BundleActivator {

	private static BundleContext context;
	private HttpService httpService;
	private static final String SERVLET_ALIAS = "/query.do";
	private static final String STATIC_ALIAS = "/";

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		ServiceReference<?> serviceReference = context.getServiceReference(HttpService.class.getName());
		if ( serviceReference !=null) {
			httpService = (HttpService)context.getService(serviceReference);
			registerWebResources();
		}else {
			System.out.println("HttpService unavailable!");
		}
	}

	public void stop(BundleContext bundleContext) throws Exception {
		if( httpService !=null) {
			unregisterWebResources();
			httpService = null;
		}
		Activator.context = null;
	}
	
	private void registerWebResources() {
		HttpContext httpContext = httpService.createDefaultHttpContext();
		try {
			// 注册静态资源
			httpService.registerResources(STATIC_ALIAS, "/resources", httpContext);
			// 注册servlet
			httpService.registerServlet(SERVLET_ALIAS, new QueryServlet(context), null, httpContext);
		
		} catch (NamespaceException | ServletException e) {
			e.printStackTrace();
		}
	}

	private void unregisterWebResources() {
		httpService.unregister(STATIC_ALIAS);
		httpService.unregister(SERVLET_ALIAS);
	}
}
