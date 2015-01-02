package com.bplaced.schlingel.examples.classloader;

public class App {
	private static class LoggingClassLoaderEventHandler implements ClassLoaderEventHandler {
		@Override
		public void onClassLoaded(Class<?> clazz) {
			System.out.printf("[LOADED] %s\n", clazz.getName());
		}
	}
	
	public static void main(String[] args) {
		ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
		InterceptingClassLoader loggingClassLoader = new InterceptingClassLoader(currentClassLoader);
		loggingClassLoader.setClassLoaderEventHandler(new LoggingClassLoaderEventHandler());
		Thread.currentThread().setContextClassLoader(loggingClassLoader);
		loggingClassLoader.init();
		
		System.out.printf("Hello World!\n\n");
	}

}
