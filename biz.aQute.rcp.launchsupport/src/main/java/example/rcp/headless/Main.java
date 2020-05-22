package example.rcp.headless;

import java.util.Hashtable;
import java.util.concurrent.Callable;
import java.util.concurrent.Semaphore;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.osgi.service.runnable.ApplicationLauncher;
import org.eclipse.osgi.service.runnable.ParameterizedRunnable;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Main implements BundleActivator, ApplicationLauncher, Callable<Integer> {
	final Semaphore rendezVous = new Semaphore(0);
	ParameterizedRunnable runnable;
	Object context;

	@Override
	public void start(BundleContext context) throws Exception {
		@SuppressWarnings("unused")
		Class<IApplication> loadme = IApplication.class;
		Hashtable<String,Object> ht = new Hashtable<>();
		ht.put("main.thread", true);
		context.registerService(new String[] {ApplicationLauncher.class.getName(), Callable.class.getName()},this, ht);
		
	}

	@Override
	public void stop(BundleContext context) throws Exception {
	}

	@Override
	public Integer call() throws Exception {
		rendezVous.acquire();
		this.runnable.run(context);
		return 0;
	}

	@Override
	public void launch(ParameterizedRunnable runnable, Object context) {
		this.runnable=runnable;
		this.context=context;
		rendezVous.release();
	}

	@Override
	public void shutdown() {
	}

}
