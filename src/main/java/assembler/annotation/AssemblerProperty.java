package assembler.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

/**
 * 
 * Används av DefaultAssemblern för att bedöma hur vissa properties skall hanteras vid transfern
 * t.ex. kan man använda mapsto för att bestämma att en viss property i dto'n skall mappas mot en annan
 * property i entiteten. t.ex. 
 * 
 *  @AssemblerProperty(mapsto="postort")
 * private String ort;
 * 
 * Man kan även välja att ignorera vissa properties som kanske bara skall finnas i dto'n eller räknas
 * fram av en annan assembler etc.
 * 
 * @author nybda
 *
 */
@Retention(RUNTIME)
public @interface AssemblerProperty {
	public boolean ignore() default false;

	public String mapsTo() default "";
}
