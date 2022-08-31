package assembler;

import dto.MasterDTO;
import entities.MasterEntity;
import exception.AssemblerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Superklass för alla assemblers. Målet är att överföra data mellan en dto och
 * entitet eller tvärtom.
 * <p>
 * Man kan i den implementerade metoden skicka en ett object som används som
 * mall för överföringen. Om man skickar in null, eller använder snabbmetoderna
 * för lista etc så får man inte med något mall-objekt. Om mall objekt saknas så
 * kommer ett sådant att skapas.
 * <p>
 * Vid överföring från dto till entity som sedermera skall persisteras måste ett
 * mallobjekt skickas med, om entiteten redan är persisterad. annars tappar man
 * referenser till listor etc
 * <p>
 * t.ex. du har ett persisterat objekt som skall uppdateras med data. Det
 * mottagande objektet har en List<MasterEntity> property, då måste
 * entitymanagern veta vilken lista den har för att kunna merga entiteten efter
 * uppdatering därför måste en entitymanager.find göras på originalobjektet
 * t.ex.
 * <p>
 * MasterEntity me = em.find(Arende.class, 1L); MasterEntity updated =
 * assembler.getEntityFromDto(dto, me);
 *
 * @author u0064563
 */

public abstract class AbstractAssembler {

    Logger logger = LoggerFactory.getLogger(getClass());

    // överför en lista av entiteter till en lista av dto
    public List<MasterDTO> getDTOListFromEntityList(List<MasterEntity> list) {
        return list.stream().map(p -> {
            try {
                return getDTOFromEntity(p, null);
            } catch (AssemblerException e) {
                logger.error("Kan inte omvandla entity till dto i lista", e);
                throw new RuntimeException();
            }
        }).collect(Collectors.toList());
    }

    // överför en lista av dto till en lista av entiteter
    public List<MasterEntity> getEntityListFromDTOList(List<MasterDTO> list) {
        return list.stream().map(p -> {
            try {
                return getEntityFromDTO(p, null);
            } catch (AssemblerException e) {
                logger.error("Kan inte omvandla dto till entity i lista", e);
                throw new RuntimeException();
            }
        }).collect(Collectors.toList());
    }

    // detta är den implementerade metoden som gör själva överföringen
    public abstract MasterDTO getDTOFromEntity(MasterEntity entity, MasterDTO dto) throws AssemblerException;

    // detta är den implementerade metoden som gör själva överföringen
    public abstract MasterEntity getEntityFromDTO(MasterDTO dto, MasterEntity entity) throws AssemblerException;

    public MasterDTO getDTOFromEntity(MasterEntity entity) throws AssemblerException {
        return getDTOFromEntity(entity, null);
    }

    public MasterEntity getEntityFromDTO(MasterDTO dto) throws AssemblerException {
        return getEntityFromDTO(dto, null);
    }

}
