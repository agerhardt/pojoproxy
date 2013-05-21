package de.age.util.pojo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.TreeMap;

class PojoInvocationHandler<T> implements InvocationHandler {

	private static final String METHOD_TOSTRING = "toString";
	
	private static final String METHOD_EQUALS = "equals";

	private static final String PREFIX_SET = "set";

	private static final String PREFIX_GET = "get";

	private static final String TOSTRING_FORMAT = "Proxy(%s)%s";
	
	private final Class<T> proxyInterface;
	private final Map<String, Object> values = new TreeMap<>();
	
	public PojoInvocationHandler(Class<T> proxyInterface) {
		this.proxyInterface = proxyInterface;
		fillMapWithFieldInfo();
	}
	
	private void fillMapWithFieldInfo() {
		Method[] methods = proxyInterface.getMethods();
		for (Method method : methods) {
			if (isGetter(method) || isSetter(method)) {
				values.put(getAttributeName(method), null);
			}
		}
	}

	Map<String, Object> getValues() {
		return values;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		if (isSetter(method)) {
			values.put(getAttributeName(method), args[0]);
			return null;
		} else if (isGetter(method)) {
			return values.get(getAttributeName(method));
		} else if (isToString(method)) {
			return String.format(TOSTRING_FORMAT, proxyInterface.getName(), values);
		} else if (isEquals(method)) {
			return compare(args[0]);
		} else {
			throw new IllegalArgumentException("Unknwon method: [" + method + "]");
		}
	}

	private boolean isEquals(Method method) {
		return method.getName().equals(METHOD_EQUALS);
	}

	private boolean isToString(Method method) {
		return method.getName().equals(METHOD_TOSTRING);
	}

	private boolean isSetter(Method method) {
		return method.getName().startsWith(PREFIX_SET);
	}

	private boolean isGetter(Method method) {
		return method.getName().startsWith(PREFIX_GET);
	}
	
	private boolean compare(Object object) {
		if (object == null) {
			return false;
		}
		if (!proxyInterface.isAssignableFrom(object.getClass())) {
			return false;
		}
		for (Map.Entry<String, Object> entry : values.entrySet()) {
			Object ownValue = entry.getValue();
			Object otherValue = getValue((T) object, entry.getKey());
			if (!valuesAreEqual(ownValue, otherValue)) {
				return false;
			}
		}
		return true;
	}

	private boolean valuesAreEqual(Object o, Object p) {
		if (o == p) {
			return true;
		}
		if (o == null || p == null) {
			return false;
		}
		return o.equals(p);
	}

	private Object getValue(T object, String attributeName) {
		try {
			Method method = proxyInterface.getMethod(getGetterName(attributeName));
			return method.invoke(object);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			return null;
		}
	}

	private String getGetterName(String attributeName) {
		return PREFIX_GET + Character.toUpperCase(attributeName.charAt(0)) + attributeName.substring(1);
	}

	private String getAttributeName(Method method) {
		String name = method.getName();
		return Character.toLowerCase(name.charAt(3)) + name.substring(4);
	}
	
}