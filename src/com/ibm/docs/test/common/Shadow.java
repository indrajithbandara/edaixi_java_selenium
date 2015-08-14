package com.ibm.docs.test.common;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * The class is used to create a shadowed object.
 * 
 * @author liuzhe@cn.ibm.com
 *
 */
public class Shadow implements MethodInterceptor, Shadowed {
	
	private Object[] objects = null;

	private Object[] results = null;
	
	private boolean shadowEnabled = true;
	
	protected Shadow(Object[] objects) {
		this.objects = objects;
		this.results = new Object[objects.length];
	}
	
	@Override
	public Object getShadow(int i) {
		return objects[i];
	}

	@Override
	public Object getShadowResult(int i) {
		return results[i];
	}

	@Override
	public int getShadowSize() {
		return results.length;
	}
	
	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		if (method.getDeclaringClass().equals(Shadowed.class)) {
			return proxy.invoke(this, args);
		}
		Object result = proxy.invokeSuper(obj, args);
		if (shadowEnabled)
			for (int i = 0; i < objects.length; i++) {
				results[i] = proxy.invoke(objects[i], args);
			}

		return result;
	}

	/**
	 * Create a shadowed object. The shadowed object means an object has one or more shadow objects. When the object's method
	 * is called, the shadow objects' same method also will be called simultaneously!
	 * @param superClass the class to create object
	 * @param argumentTypes the construction argument types
	 * @param arguments  the construction arguments
	 * @param shadowArguments the construction arguments for shadow objects
	 * @return
	 * @throws Exception
	 */
	public static Object createShadowed(Class<?> superClass, Class<?>[] argumentTypes, Object[] arguments, Object[][] shadowArguments) throws Exception {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(superClass);
		enhancer.setInterfaces(new Class[]{Shadowed.class});
		Constructor<?> constructor = superClass.getConstructor(argumentTypes);
		Object[] objects = new Object[shadowArguments.length];
		for (int i = 0; i < shadowArguments.length; i++) {
			objects[i] = constructor.newInstance(shadowArguments[i]);
		}
		enhancer.setCallback(new Shadow(objects));
		return enhancer.create(argumentTypes, arguments);
	}

	@Override
	public void setShadowEnabled(boolean enabled) {
		shadowEnabled = enabled;
	}
}

