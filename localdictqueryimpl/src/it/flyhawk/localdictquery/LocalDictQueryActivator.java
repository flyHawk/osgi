package it.flyhawk.localdictquery;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import it.flyhawk.dictquery.DictQueryService;
import it.flyhawk.localdictquery.impl.LocalDictQueryImpl;

public class LocalDictQueryActivator implements BundleActivator {

	private static BundleContext context;
	private ServiceRegistration<?> serviceRegistration;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		LocalDictQueryActivator.context = bundleContext;
		// 注册服务时可以指定服务的属性
		Dictionary<String, String> props = new Hashtable<String,String>();
		props.put("queryType", "local");
		serviceRegistration = context.registerService(DictQueryService.class.getName(), new LocalDictQueryImpl(), props);
	}

	public void stop(BundleContext bundleContext) throws Exception {
		// 注销服务
		serviceRegistration.unregister();
		LocalDictQueryActivator.context = null;
	}

}
