package assembler;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

/**
 * AssemblerFactory skapar en assembler som passar ihop med den entitet/dto som skall transferera data
 * Om ingen specifik assembler finns används defaultassemblern
 *
 * @author nybda
 */


public enum AssemblerFactory {
    INSTANCE;

    static Logger logger = LoggerFactory.getLogger(AssemblerFactory.class);

    public AbstractAssembler createAssembler() {
        return new DefaultAssembler();
    }

    public AbstractAssembler createAssembler(String entityClass) {
        Class<?> c = null;
        AbstractAssembler assembler = null;
        try {
            c = Class.forName(entityClass + "Assembler");
        } catch (ClassNotFoundException e) {
            try {
                c = Class.forName("assembler." + entityClass + "Assembler");
            } catch (ClassNotFoundException e1) {
                logger.debug("Kan inte hitta assembler för: " + entityClass + ", använder defaultassembler");
            }
        }

        if (c != null) {
            try {
                assembler = (AbstractAssembler) c.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e1) {
                logger.error("Kan inte instansiera assembler: " + entityClass);
            }
        }
        if (assembler == null) {
            assembler = new DefaultAssembler();
        }
        return assembler;
    }

}
