package de.age.util.pojo;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Map;

import org.junit.Test;

public class PojoInvocationHandlerTest {

	private static interface ComplexPojoInterface {
		public String getStringValue();
		
		public void setStringValue(String aValue);
		
		public int getIntValue();
		
		public void setIntValue(int aValue);
	}

	@Test
	public void allFieldsAreFound() {
		PojoInvocationHandler<ComplexPojoInterface> handler = new PojoInvocationHandler<>(ComplexPojoInterface.class);
		Map<String, Object> values = handler.getValues();
		assertThat(values.size(), is(2));
		assertThat(values.containsKey("stringValue"), is(true));
		assertThat(values.containsKey("intValue"), is(true));
	}
}
