package assembler.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

/**
 * Används i entiteterna för att beskriva värdet av en property som ändras
 *
 * t.ex.
 *  @LoggingDescriptor(description="En string har ändrats")
 * 	private String string;
 *
 * 	Man kan även tala om att loggning av ändrignar av en viss property inte skall loggas
 * 	genom att sätta ignore till true
 *
 * @author nybda
 */
@Retention(RUNTIME)
public @interface LoggingDescriptor {
	public String description() default"";
	public boolean ignore() default false;
}

