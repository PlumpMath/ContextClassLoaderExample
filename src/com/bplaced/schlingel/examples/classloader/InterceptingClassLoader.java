package com.bplaced.schlingel.examples.classloader;

import java.lang.reflect.Field;
import java.util.Vector;

/**
 * This class loader calls an event handler when a class has been loaded. During initialization it
 * iterates through the already loaded classes to also notify the subscriber of classes which have been
 * already loaded. 
 */
public class InterceptingClassLoader extends ClassLoader {
	private ClassLoader core;
	private ClassLoaderEventHandler handler;

	public InterceptingClassLoader(ClassLoader loader) {
		this.core = loader;
	}

	@SuppressWarnings("rawtypes")
	public void init() {
		// Rather hackish solution to access the already loaded classes
		// Another way would be to define a instance of Instrumentarium
		// (http://docs.oracle.com/javase/6/docs/api/java/lang/instrument/Instrumentation.html)
		// But to keep things simple, I use reflection here.
		try {
			Field f = ClassLoader.class.getDeclaredField("classes");
			f.setAccessible(true);
			@SuppressWarnings("unchecked")
			Vector<Class> classes = (Vector<Class>) f.get(core);
			
			for(Class clazz : classes) {
				handleClassLoading(clazz);
			}
		} catch (IllegalArgumentException e) {
			rethrowRuntimeException(e);
		} catch (IllegalAccessException e) {
			rethrowRuntimeException(e);
		} catch (NoSuchFieldException e) {
			rethrowRuntimeException(e);
		} catch (SecurityException e) {
			rethrowRuntimeException(e);
		}
	}
	
	private void rethrowRuntimeException(Throwable t) {
		throw new RuntimeException(t);
	}

	public void setClassLoaderEventHandler(ClassLoaderEventHandler handler) {
		this.handler = handler;
	}

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		Class<?> clazz = core.loadClass(name);
		handleClassLoading(clazz);

		return clazz;
	}

	private void handleClassLoading(Class<?> clazz) {
		if (handler != null) {
			handler.onClassLoaded(clazz);
		}
	}

}
