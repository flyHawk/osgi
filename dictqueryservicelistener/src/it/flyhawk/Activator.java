package it.flyhawk;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

/**
 * 利用OSGI框架的事件机制监听服务事件
 * @author Administrator
 *
 */
public class Activator implements BundleActivator, ServiceListener {

	private static BundleContext context;
	// 默认服务监听器会监听所有服务的事件，在这里只监听字典查询服务
	private static final String serviceFilter = "(objectClass=it.flyhawk.dictquery.DictQueryService)";

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		System.out.println("DictQueryServiceListener: Starting to listen for service events.");
        context.addServiceListener(this, serviceFilter);
	}

	public void stop(BundleContext bundleContext) throws Exception {
		context.removeServiceListener(this);
		Activator.context = null;
		System.out.println("DictQueryServiceListener: Stopped listening for service events.");
	}

	@Override
	public void serviceChanged(ServiceEvent event) {
		ServiceReference<?> serviceReference = event.getServiceReference();
		String[] objectClass = (String[]) serviceReference.getProperty("objectClass");
		// 服务属性（查询类别，表示是从本地查询服务还是远程查询服务)
		String queryType = (String)serviceReference.getProperty("queryType");
		if (event.getType() == ServiceEvent.REGISTERED) {
			System.out.println("DictQueryServiceListener: Service of type " + objectClass[0] + " ["+ queryType + "] registered.");
		} else if (event.getType() == ServiceEvent.UNREGISTERING) {
			System.out.println("DictQueryServiceListener: Service of type " + objectClass[0] + " ["+ queryType + "] unregistered.");
		} else if (event.getType() == ServiceEvent.MODIFIED) {
			System.out.println("DictQueryServiceListener: Service of type " + objectClass[0] + " ["+ queryType + "] modified.");
		}
	}

}
