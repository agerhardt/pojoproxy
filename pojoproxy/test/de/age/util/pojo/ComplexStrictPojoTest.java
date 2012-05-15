package de.age.util.pojo;

import static de.age.util.pojo.PojoFormatMatcher.strictFormatMatching;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ComplexStrictPojoTest {

	private static interface ComplexPojoInterface {
		public String getStringValue();
		
		public void setStringValue(String aValue);
		
		public int getIntValue();
		
		public void setIntValue(int aValue);
	}

	private ComplexPojoInterface proxy;
	
	@Before
	public void setUp() {
		proxy = StrictPojoProxy.createProxy(ComplexPojoInterface.class);
	}
	
	@After
	public void tearDown() {
		proxy = null;
	}

	@Test
	public void toStringReturnsRepresentationOfValues() {
		String testValue = "testValue";
		int intValue = 17;
		proxy.setStringValue(testValue);
		proxy.setIntValue(intValue);
		assertThat(proxy.toString(), is(strictFormatMatching(proxy)));
	}

}
