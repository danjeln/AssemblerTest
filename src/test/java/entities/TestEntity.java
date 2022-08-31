package entities;

import assembler.annotation.LoggingDescriptor;

@SuppressWarnings("serial")
public class TestEntity extends MasterEntity{

	@LoggingDescriptor(ignore=true)
	private Long id;
	
}
