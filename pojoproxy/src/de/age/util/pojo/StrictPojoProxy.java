package de.age.util.pojo;

import java.lang.reflect.Proxy;

public class StrictPojoProxy {

	@SuppressWarnings("unchecked")
	public static <T> T createProxy(Class<T> proxyInterface) {
		return (T) Proxy.newProxyInstance(proxyInterface.getClassLoader(),
				new Class[] { proxyInterface }, new PojoInvocationHandler<T>(
						proxyInterface));
	}

}
