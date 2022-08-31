package assembler;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import assembler.annotation.LoggingDescriptor;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import request.RequestData;
import request.RequestDataType;


/**
 * Används av DefaultAssemblern för att hålla ett object och ett specifikt field
 * använder reflection för att läsa ut innehållet och skriva ner innehållet i objektet
 * 
 * Har även de statiska metoderna för att logga ändringar på objekten.
 * 
 * @author u0064563
 *
 */


public class AssemblerObject {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	private Object o;
	private Field f;
	
	public AssemblerObject(Object o, Field f) {
		this.o = o;
		this.f = f;
	}
	
	public Field getField() {
		return f;
	}
	
	public Object getObject() {
		return o;
	}
	
	public Object getValue() {
		try {
			boolean accessible = f.isAccessible();
			f.setAccessible(true);
			Object value = f.get(o);
			f.setAccessible(accessible);
			return value;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			logger.warn("kan inte läsa ut värdet för: " + f.getName() + " i klassen: " + o.getClass().getSimpleName());
			return null;
		}
	}

	public void setValue(Object value, int direction) {
		Object oo = getValue();
		boolean eq = true;
		if (oo != null) {
			if (oo instanceof Date) {
				DateTime dt1 = new DateTime(oo);
				DateTime dt2 = new DateTime(value);
				eq = dt1.equals(dt2);
			} else {
				eq = oo.equals(value);
			}
		} else if (oo == null && value != null) {
			eq = false;
		}
		if (!eq && direction == 1) {
			logger.debug(String.format("Sätter värdet %s till %s i %s från tidigare %s", f.getName(), value,
					o.getClass().getSimpleName(), oo));
			propertyChanged(o, f);
		}
		try {
			boolean accessible = f.isAccessible();
			f.setAccessible(true);
			f.set(o, value);
			f.setAccessible(accessible);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			logger.warn("kan inte sätta värdet " + value + " för: " + f.getName() + " i klassen: "
					+ o.getClass().getSimpleName());
		}

	}
	
	public static void propertyChanged(Object o, Field f) {
		propertyChanged(o, f, "");
	}

	public static void propertyChanged(Object o, Field f, String info) {
		LoggingDescriptor ld = f.getAnnotation(LoggingDescriptor.class);
		if (ld == null) {
			ld = o.getClass().getAnnotation(LoggingDescriptor.class);
		}
		String loggString = f.getName();
		if (ld != null) {
			loggString = ld.description();
			if (ld.ignore()) {
				return;
			}
		}
		propertyChanged(loggString+ (!"".equals(info) ? " (" + info + ")" : ""));
	}

	public static void propertyChanged(String name) {
		List<String> prop = (List<String>) RequestData.INSTANCE.getData(RequestDataType.CHANGED_PROPERTIES);
		if (prop == null) {
			prop = new ArrayList<>();
		}
		prop.add(name);
		RequestData.INSTANCE.addData(RequestDataType.CHANGED_PROPERTIES, prop);
	}
	
	
}
