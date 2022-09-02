package assembler;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import datalog.ChangedData;

public class AssemblerFactoryTest {

	@Before
	public void setup() {
		ChangedData.INSTANCE.reset();
	}
	
	@Test
	public void createAssemblerTest() {
		AbstractAssembler assembler = AssemblerFactory.INSTANCE.createAssembler("Fantasy");
		assertTrue(assembler!=null && assembler instanceof AbstractAssembler);
	}


	
}
