package de.age.util.pojo;

import static de.age.util.pojo.PojoFormatMatcher.strictFormatMatching;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.lang.reflect.Proxy;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class SimpleStrictPojoProxyTest {
	
	private static interface SimplePojoInterface {
		public String getStringValue();
		
		public void setStringValue(String aValue);
	}

	private SimplePojoInterface proxy;
	
	@Before
	public void setUp() {
		proxy = StrictPojoProxy.createProxy(SimplePojoInterface.class);
	}
	
	@After
	public void tearDown() {
		proxy = null;
	}
	
	@Test
	public void canCreateProxy() {
		assertThat(proxy, is(notNullValue()));
	}
	
	@Test
	public void createdObjectIsProxy() {
		assertThat(Proxy.isProxyClass(proxy.getClass()), is(true));
	}
	
	@Test
	public void canSetAndGetValue() {
		String testValue = "testValue";
		proxy.setStringValue(testValue);
		assertThat(proxy.getStringValue(), is(equalTo(testValue)));
	}
	
	@Test
	public void toStringReturnsRepresentationOfValues() {
		String testValue = "testValue";
		proxy.setStringValue(testValue);
		assertThat(proxy.toString(), is(strictFormatMatching(proxy)));
	}

	@Test
	public void sameInstancesAreEqual() {
		String testValue = "testValue";
		proxy.setStringValue(testValue);
		assertThat(proxy.equals(proxy), is(true));
	}
	
	@Test
	public void comparingToNullIsNotEqual() {
		String testValue = "testValue";
		proxy.setStringValue(testValue);
		assertThat(proxy.equals(null), is(false));
	}
	
	@Test
	public void comparingToDifferentObjectIsNotEqual() {
		String testValue = "testValue";
		proxy.setStringValue(testValue);
		assertThat(proxy.equals(new Object()), is(false));
	}
	
	@Test
	public void comparingToEqualProxyIsEqual() {
		String testValue = "testValue";
		proxy.setStringValue(testValue);
		SimplePojoInterface anotherProxy = StrictPojoProxy.createProxy(SimplePojoInterface.class);
		anotherProxy.setStringValue(testValue);
		assertThat(proxy.equals(anotherProxy), is(true));
	}
	
	@Test
	public void comparingToCorrectClassWithDifferentValuesIsNotEqual() {
		String testValue = "testValue";
		proxy.setStringValue(testValue);
		SimplePojoInterface anotherProxy = StrictPojoProxy.createProxy(SimplePojoInterface.class);
		anotherProxy.setStringValue(testValue + testValue);
		assertThat(proxy.equals(anotherProxy), is(false));
	}
	
	@Ignore
	@Test
	public void comparingToObjectImplementingInterfaceWithSameValuesIsEqual() {
		fail();
	}
	
	@Ignore
	@Test
	public void methodCallToStringWithWrongSignature() {
		fail();
	}
	
	@Ignore
	@Test
	public void methodCallEqualsWithWrongSignature() {
		fail();
	}
	
	@Ignore
	@Test
	public void methodCallSetterWithWrongSignature() {
		fail();
	}		
	
	@Ignore
	@Test
	public void methodCallGetterWithWrongSignature() {
		fail();
	}

}
