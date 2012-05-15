package de.age.util.pojo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.TreeMap;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class PojoFormatMatcher extends BaseMatcher<String> {

	private Map<String, Object> attributes;

	public PojoFormatMatcher(Map<String, Object> values) {
		this.attributes = values;
	}

	@Override
	public boolean matches(Object item) {
		if (item instanceof String) {
			String s = (String) item;
			String attributeList = s.substring(s.indexOf("{") + 1, s.length() - 1);
			String[] split = attributeList.split(",");
			Map<String, String> foundAttributes = new TreeMap<>();
			for (String keyValuePairString : split) {
				String[] keyValuePair = keyValuePairString.split("=");
				String key = keyValuePair[0].trim();
				String value = keyValuePair[1];
				foundAttributes.put(key, value);
			}
			Map<String, Object> missingAttributes = new TreeMap<>();
			Map<String, String> additionalAttributes = new TreeMap<>();
			Map<String, String[]> wrongAttributes = new TreeMap<>();
			for (Map.Entry<String, String> entry : foundAttributes.entrySet()) {
				if (!attributes.containsKey(entry.getKey())) {
					additionalAttributes.put(entry.getKey(), entry.getValue());
				} else {
					Object o = attributes.get(entry.getKey());
					String toStringValue;
					if (o == null) {
						toStringValue = "null";
					} else {
						toStringValue = o.toString();
					}
					if (!toStringValue.equals(entry.getValue())) {
						wrongAttributes.put(entry.getKey(), new String[] { entry.getValue(), toStringValue });
					}
				}
			}
			for (Map.Entry<String, Object> entry : attributes.entrySet()) {
				String toStringValue = foundAttributes.get(entry.getKey());
				if (toStringValue == null) {
					missingAttributes.put(entry.getKey(), entry.getValue());
				}
			}
			if (missingAttributes.isEmpty() && additionalAttributes.isEmpty() && wrongAttributes.isEmpty()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("expecting " + attributes.toString());
	}

	public static PojoFormatMatcher strictFormatMatching(Object proxy) {
		if (!Proxy.isProxyClass(proxy.getClass())) {
			throw new IllegalArgumentException("Not a proxy");
		}
		InvocationHandler handler = Proxy.getInvocationHandler(proxy);
		if (handler instanceof StrictPojoProxy.PojoInvocationHandler) {
			StrictPojoProxy.PojoInvocationHandler pojoHandler = (StrictPojoProxy.PojoInvocationHandler) handler;
			return new PojoFormatMatcher(pojoHandler.getValues());
		} else {
			throw new IllegalArgumentException("Wrong invocationhandler");
		}
	}

}
