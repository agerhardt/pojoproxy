package de.age.util.pojo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.TreeMap;

public class StrictPojoProxy {

	@SuppressWarnings("unchecked")
	public static <T> T createProxy(Class<T> proxyInterface) {
		return (T) Proxy.newProxyInstance(proxyInterface.getClassLoader(),
				new Class[] { proxyInterface }, new PojoInvocationHandler(
						proxyInterface));
	}
	
	protected static class PojoInvocationHandler implements InvocationHandler {

		private static final String METHOD_TOSTRING = "toString";

		private static final String PREFIX_SET = "set";

		private static final String PREFIX_GET = "get";

		private static final String TOSTRING_FORMAT = "Proxy(%s)%s";
		
		private final Class<?> proxyInterface;
		private final Map<String, Object> values = new TreeMap<>();
		
		public PojoInvocationHandler(Class<?> proxyInterface) {
			this.proxyInterface = proxyInterface;
		}
		
		Map<String, Object> getValues() {
			return values;
		}
		
		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
			if (method.getName().startsWith(PREFIX_SET)) {
				values.put(getAttributeName(method), args[0]);
				return null;
			} else if (method.getName().startsWith(PREFIX_GET)) {
				return values.get(getAttributeName(method));
			} else if (method.getName().equals(METHOD_TOSTRING)) {
				return String.format(TOSTRING_FORMAT, proxyInterface.getName(), values);
			} else {
				throw new IllegalArgumentException("Unknwon method: [" + method + "]");
			}
		}
		
		private String getAttributeName(Method method) {
			String name = method.getName();
			return Character.toLowerCase(name.charAt(3)) + name.substring(4);
		}
		
	}

}
