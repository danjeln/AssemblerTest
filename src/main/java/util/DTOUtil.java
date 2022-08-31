package util;

import dto.MasterDTO;
import entities.MasterEntity;
import exception.AssemblerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DTOUtil {

	static Logger logger = LoggerFactory.getLogger(DTOUtil.class);

	public static String getEntityClassNameFromDTO(MasterDTO dto) {
		return getEntityClassNameFromDTO(dto.getClass().getSimpleName());
	}

	public static String getEntityClassNameFromDTO(String dto) {
		return (dto != null ? dto.replace("DTO", "") : "");
	}

	public static String getDTOClassNameFromEntity(MasterEntity entity) {
		return getDTOClassNameFromEntity(entity.getClass().getSimpleName());
	}

	public static String getDTOClassNameFromEntity(String entity) {
		return entity.endsWith("DTO") ? entity : entity + "DTO";
	}

	public static Class<?> getDTOClass(String dtoClassName) throws AssemblerException {
		Class<?> c = null;
		try {
			c = Class.forName(dtoClassName);
		} catch (ClassNotFoundException e) {
			try {
				c = Class.forName("dto." + dtoClassName);
			} catch (ClassNotFoundException e1) {
				logger.error("Kan inte hitta DTOklass från " + dtoClassName, e1);
				throw new AssemblerException("Kan inte hitta DTOKlass " + dtoClassName, e1);
			}

		}
		return c;
	}

	public static Class<?> getEntityClass(String entityClassName) throws AssemblerException {
		Class<?> c = null;
		try {
			c = Class.forName(entityClassName);
		} catch (ClassNotFoundException e) {
			try {
				c = Class.forName("entities." + entityClassName);
			} catch (ClassNotFoundException e1) {
				logger.error("Kan inte hitta entityklass från " + entityClassName, e1);
				throw new AssemblerException("Kan inte hitta entityKlass" + entityClassName, e1);
			}

		}
		return c;
	}

}
