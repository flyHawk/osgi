package it.flyhawk.remotedictquery;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import it.flyhawk.dictquery.DictQueryService;
import it.flyhawk.remotedictquery.impl.RemoteDictQueryImpl;

public class RemoteDictQueryActivator implements BundleActivator {

	private static BundleContext context;
	private ServiceRegistration<?> serviceRegistration;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		RemoteDictQueryActivator.context = bundleContext;
		Dictionary<String, String> props = new Hashtable<String,String>();
		props.put("queryType", "remote");
		serviceRegistration = context.registerService(DictQueryService.class.getName(), new RemoteDictQueryImpl(), props);
	}

	public void stop(BundleContext bundleContext) throws Exception {
		serviceRegistration.unregister();
		RemoteDictQueryActivator.context = null;
	}

}
