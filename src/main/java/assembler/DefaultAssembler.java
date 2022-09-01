package assembler;


import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import assembler.annotation.AssemblerProperty;
import dto.MasterDTO;
import entities.MasterEntity;
import exception.AssemblerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.DTOUtil;


/**
 * Hanterar transfer av data mellan generell entity och dto
 * 
 * Den läser upp alla fält som finns i dto/entity beroende på riktning och
 * försöker hitta ett matchande fält i mottagande klass med hjälp av namn och
 * Annoteringen @AssemblerProperty. Skulle inget fält finnas loggas en varning.
 * (beroende på riktning)
 * 
 * Den har i princip tre beteenden när den överför fält mellan entitet/dto och
 * dto/entitet. 1. fältet är en AMasterDTO/MasterEntity, då anropar den sig
 * självt med objektet som argument 2. fältet är av enkel typ (String, Date,
 * Number etc.), då överför den datat direkt till mottagande klass 3. fältet är
 * en kollektion som innehåller objekt av typen: 3.1 MasterDTO/MasterEntity då
 * anropas den sig självt med objektet som argument tills listan är genomarbetad
 * 3.2 enkel typ, då överför den objekt för objekt i listan tills listan är
 * genomarbetad 3.3 fältet är en kollektion som innehåller objekt av typen: ...
 * 
 * När en kollektion överförs kommer den att först bedöma om det är ett nytt
 * objekt i listan eller om det är ett befintligt. Beroende på kommer den att
 * anropa rätt metod i Assemblern. Detta har att göra med mallobjekt som
 * beskrivs ytterligare i AbstractAssembler Efter den gått igenom nya och
 * ändrade objekt i kollektionen kommer den att bedöma om något har tagits bort
 * och i sin tur radera det objektet i mottagande klass. Det är speciellt
 * viktigt om det är getEntityFromDTO som anropas och resultatet skall mergas,
 * eller persisteras efter operationen.
 * 
 * @author nybda
 *
 */



public class DefaultAssembler extends AbstractAssembler {

	Logger logger = LoggerFactory.getLogger(getClass());

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public MasterDTO getDTOFromEntity(MasterEntity entity, MasterDTO dto) throws AssemblerException {
		if (entity == null) {
			return null;
		}
		String dtoClass = DTOUtil.getDTOClassNameFromEntity(entity);
		Class<?> dc = DTOUtil.getDTOClass(dtoClass);
		if (dto == null) {
			dto = (MasterDTO) getNewInstanceOfClass(dc);
		}

		Class<?> c = entity.getClass();
		List<Field> fields = getAllFields(c);
		for (Field f : fields) {
			AssemblerObject dtoObject = getFieldOfDTO(dto, f.getName());
			AssemblerObject entityObject = new AssemblerObject(entity, f);
			if (dtoObject != null) {
				if (MasterEntity.class.isAssignableFrom(f.getType())) {
					AbstractAssembler assembler = AssemblerFactory.INSTANCE
							.createAssembler(f.getType().getSimpleName());
					MasterDTO child = assembler.getDTOFromEntity((MasterEntity) entityObject.getValue(),
							(MasterDTO) dtoObject.getValue());
					if (child != null) {
						dtoObject.setValue(child, 0);
					}
				} else if (isOfSimpleType(f.getType())) {
					dtoObject.setValue(toAppropriateType(entityObject.getValue()), 0);
				} else if (Collection.class.isAssignableFrom(f.getType())) {
					Collection<?> lst = (Collection<?>) entityObject.getValue();
					if (lst != null) {
						Object[] arr = new Object[lst.size()];
						int i = 0;
						for (Object co : lst) {
							if (co instanceof MasterEntity) {
								AbstractAssembler assembler = AssemblerFactory.INSTANCE
										.createAssembler(co.getClass().getSimpleName());
								int fi = i;
								MasterDTO remoteDTO = (MasterDTO) Optional
										.ofNullable(((List<?>) dtoObject.getValue())).map(x -> x.get(fi)).orElse(null);
								arr[i++] = assembler.getDTOFromEntity((MasterEntity) co, remoteDTO);
							} else if (isOfSimpleType(co.getClass())) {
								arr[i++] = co;
							} else {
								logger.warn("entity: " + entity.getClass().getSimpleName()
										+ " har en collection vi inte kan hantera -> " + co.getClass().getSimpleName());
							}

						}
						dtoObject.setValue(new LinkedList(Arrays.asList(arr)), 0);
					}

				} else {
					logger.warn("kan inte hantera fältet: " + f.getName() + " av typen " + f.getType().getSimpleName());
				}
			}

		}

		return dto;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public MasterEntity getEntityFromDTO(MasterDTO dto, MasterEntity entity) throws AssemblerException {
		if (dto == null) {
			return null;
		}
		String entityClass = DTOUtil.getEntityClassNameFromDTO(dto);
		Class<?> dc = DTOUtil.getEntityClass(entityClass);
		if (entity == null) {
			entity = (MasterEntity) getNewInstanceOfClass(dc);
		}

		Class<?> c = dto.getClass();
		List<Field> fields = getAllFields(c);
		for (Field f : fields) {
			boolean ignore = false;
			String mapsTo = f.getName();
			if (f.getAnnotation(AssemblerProperty.class) != null) {
				ignore = f.getAnnotation(AssemblerProperty.class).ignore();
				mapsTo = (!"".equals(f.getAnnotation(AssemblerProperty.class).mapsTo())
						? f.getAnnotation(AssemblerProperty.class).mapsTo()
						: mapsTo);
			}
			if (!ignore) {
				AssemblerObject entityObject = getFieldOfEntity(entity, mapsTo);
				AssemblerObject dtoObject = new AssemblerObject(dto, f);
				if (entityObject != null) {
					if (MasterDTO.class.isAssignableFrom(f.getType())) {
						AbstractAssembler assembler = AssemblerFactory.INSTANCE
								.createAssembler(DTOUtil.getEntityClassNameFromDTO(f.getType().getSimpleName()));

						MasterEntity child = assembler.getEntityFromDTO((MasterDTO) dtoObject.getValue(),
								(MasterEntity) entityObject.getValue());
						if (child != null) {
							entityObject.setValue(child, 1);
						}
					} else if (isOfSimpleType(f.getType())) {
						entityObject.setValue(dtoObject.getValue(), 1);
					} else if (Collection.class.isAssignableFrom(f.getType())) {
						Collection lst = (Collection) dtoObject.getValue();
						Collection dst = (Collection) entityObject.getValue();
						if (lst != null && dst == null) {
							Object[] arr = new Object[lst.size()];
							int i = 0;
							for (Object co : lst) {
								if (co instanceof MasterDTO) {
									AbstractAssembler assembler = AssemblerFactory.INSTANCE.createAssembler(
											DTOUtil.getEntityClassNameFromDTO(co.getClass().getSimpleName()));
									int fi = i;
									MasterEntity remoteEntity = (MasterEntity) Optional
											.ofNullable(((List<?>) entityObject.getValue())).map(x -> x.get(fi))
											.orElse(null);
									arr[i++] = assembler.getEntityFromDTO((MasterDTO) co, remoteEntity);
								} else if (isOfSimpleType(co.getClass())) {
									arr[i++] = co;
								} else {
									logger.warn("entity: " + entity.getClass().getSimpleName()
											+ " har en collection vi inte kan hantera -> "
											+ co.getClass().getSimpleName());
								}
							}
							entityObject.setValue(Arrays.asList(arr), 1);
						} else if (lst != null && dst != null) {
							// hantera ändrade, borttagna och tillagda rader
							List<Integer> handledHashCodes = new ArrayList<>();
							Object[] arr = new Object[lst.size()];
							int i = 0;
							for (Object co : lst) {
								if (co instanceof MasterDTO) {
									AbstractAssembler assembler = AssemblerFactory.INSTANCE.createAssembler(
											DTOUtil.getEntityClassNameFromDTO(co.getClass().getSimpleName()));
									MasterEntity remoteEntity = null;
									for (Object dco : dst) {
										if (dco instanceof MasterEntity) {
											if (((MasterEntity) dco).getId() != null && ((MasterEntity) dco).getId()
													.equals(((MasterDTO) co).getId())) {
												remoteEntity = (MasterEntity) dco;
												dst.remove(dco);
												break;
											}
										} else {
											logger.error("kan inte mappa en dto mot en icke masterentity");
										}
									}
									if (remoteEntity == null) {
										// item tillagt i lista
										AssemblerObject.propertyChanged(entity, entityObject.getField(), "tillägg");
									}
									MasterEntity updatedEntity = assembler.getEntityFromDTO((MasterDTO) co,
											remoteEntity);
									dst.add(updatedEntity);
									handledHashCodes.add(updatedEntity.hashCode());
								} else if (isOfSimpleType(co.getClass())) {
									arr[i++] = co;
								}
							}
							if (i > 0) {
								dst.clear();
								dst.addAll(new LinkedList(Arrays.asList(arr)));
							} else {
								Iterator<Object> iter = dst.iterator();
								while (iter.hasNext()) {
									Object o = iter.next();
									if (!handledHashCodes.contains(o.hashCode())) {
										// item borttaget ur lista
										AssemblerObject.propertyChanged(entity, entityObject.getField(), "borttag");
										iter.remove();
									}
								}
							}
						} else if (lst == null && dst != null) {
							// inget inskickat.. hur gör vi då? i dagsläget struntar vi i dessa properties
						}

					} else {
						logger.warn(
								"kan inte hantera fältet: " + f.getName() + " av typen " + f.getType().getSimpleName());
					}
				} else {
					logger.warn("mottagande klass " + entity.getClass().getSimpleName() + " saknar fältet: "
							+ f.getName() + " av typen " + f.getType().getSimpleName());
				}
			}

		}

		return entity;
	}

	private List<Field> getAllFields(List<Field> fields, Class<?> type) {
		fields.addAll(Arrays.asList(type.getDeclaredFields()));

		if (type.getSuperclass() != null) {
			getAllFields(fields, type.getSuperclass());
		}

		return fields;

	}

	private List<Field> getAllFields(Class<?> type) {
		List<Field> fields = new ArrayList<>();
		return getAllFields(fields, type);

	}

	private AssemblerObject getFieldOfDTO(MasterDTO dto, String name) {
		List<Field> fields = getAllFields(dto.getClass());
		for (Field f : fields) {
			if (f.getAnnotation(AssemblerProperty.class) != null) {
				if (name.equals(f.getAnnotation(AssemblerProperty.class).mapsTo())) {
					if (!f.getAnnotation(AssemblerProperty.class).ignore()) {
						return new AssemblerObject(dto, f);
					}
				}
			}
		}
		for (Field f : fields) {
			if (f.getName().equals(name)) {
				if (f.getAnnotation(AssemblerProperty.class) != null) {
					if (!f.getAnnotation(AssemblerProperty.class).ignore()) {
						return new AssemblerObject(dto, f);
					}
				} else {
					return new AssemblerObject(dto, f);
				}
			}
		}
		return null;
	}

	private AssemblerObject getFieldOfEntity(MasterEntity entity, String name) {
		List<Field> fields = getAllFields(entity.getClass());
		for (Field f : fields) {
			if (f.getName().equals(name)) {
				return new AssemblerObject(entity, f);
			}
		}
		return null;
	}

	private Object getNewInstanceOfClass(Class<?> c) throws AssemblerException {
		try {
			return c.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			logger.error("kan inte skapa klass av typen " + c.getSimpleName());
			throw new AssemblerException("Kan inte skapa klass", e);
		}
	}

	private boolean isOfSimpleType(Class<?> c) {
		return c.isPrimitive() || c.equals(String.class) || c.equals(Date.class) || Number.class.isAssignableFrom(c)
				|| c.equals(Timestamp.class);
	}

	private Object toAppropriateType(Object o) {
		if (o instanceof java.sql.Date) {
			return new Date(((java.sql.Date) o).getTime());
		}
		return o;
	}

}
