package assembler;

import assembler.annotation.LoggingDescriptor;
import datalog.ChangedData;
import datalog.ChangedDataType;
import datalog.Datalog;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Används av DefaultAssemblern för att hålla ett object och ett specifikt field
 * använder reflection för att läsa ut innehållet och skriva ner innehållet i objektet
 * <p>
 * Har även de statiska metoderna för att logga ändringar på objekten.
 *
 * @author nybda
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
            boolean accessible = f.canAccess(o);
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
            propertyChanged(o, f, oo != null ? oo.toString() : null, value != null ? value.toString() : null);
        }
        try {
            boolean accessible = f.canAccess(o);
            f.setAccessible(true);
            f.set(o, value);
            f.setAccessible(accessible);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            logger.warn("kan inte sätta värdet " + value + " för: " + f.getName() + " i klassen: "
                    + o.getClass().getSimpleName());
        }

    }

    public static void propertyChanged(Object o, Field f, String oldValue, String newValue) {
        propertyChanged(o, f, "", oldValue, newValue);
    }

    public static void propertyChanged(Object o, Field f, String info, String oldValue, String newValue) {
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
        propertyChanged(loggString + (!"".equals(info) ? " (" + info + ")" : ""), oldValue, newValue);
    }

    public static void propertyChanged(String description, String oldValue, String newValue) {
        List<Datalog> prop = (List<Datalog>) ChangedData.INSTANCE.getData(ChangedDataType.CHANGED_PROPERTIES);
        if (prop == null) {
            prop = new ArrayList<>();
        }
        prop.add(new Datalog(description, oldValue, newValue));
        ChangedData.INSTANCE.addData(ChangedDataType.CHANGED_PROPERTIES, prop);
    }


}
