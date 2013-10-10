package net.nan21.dnet.core.scheduler;

import java.io.InputStream;
import java.net.URL;

import org.quartz.simpl.InitThreadContextClassLoadHelper;

public class MyInitThreadContextClassLoadHelper extends
		InitThreadContextClassLoadHelper {

	private ClassLoader initClassLoader;

	public void initialize() {
		// initClassLoader = Thread.currentThread().getContextClassLoader();
	}

	public Class<?> loadClass(String name) throws ClassNotFoundException {
		return initClassLoader.loadClass(name);
	}

	@SuppressWarnings("unchecked")
	public <T> Class<? extends T> loadClass(String name, Class<T> clazz)
			throws ClassNotFoundException {
		return (Class<? extends T>) loadClass(name);
	}

	public URL getResource(String name) {
		return initClassLoader.getResource(name);
	}

	public InputStream getResourceAsStream(String name) {
		return initClassLoader.getResourceAsStream(name);
	}

	public ClassLoader getClassLoader() {
		return this.initClassLoader;
	}

	public void setClassLoader(ClassLoader cl) {
		this.initClassLoader = cl;
	}
}
