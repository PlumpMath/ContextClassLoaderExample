package com.bplaced.schlingel.examples.classloader;

public interface ClassLoaderEventHandler {
	public void onClassLoaded(Class<?> clazz);
}
