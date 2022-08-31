package assembler.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

@Retention(RUNTIME)
public @interface LoggingDescriptor {
	public String description() default"";
	public boolean ignore() default false;
}

