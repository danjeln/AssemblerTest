package assembler;

import dto.MasterDTO;
import entities.MasterEntity;
import exception.AssemblerException;

public class CustomEntityAssembler extends AbstractAssembler {
    @Override
    public MasterDTO getDTOFromEntity(MasterEntity entity, MasterDTO dto) throws AssemblerException {
        DefaultAssembler assembler = new DefaultAssembler();
        return assembler.getDTOFromEntity(entity, dto);
    }

    @Override
    public MasterEntity getEntityFromDTO(MasterDTO dto, MasterEntity entity) throws AssemblerException {
        DefaultAssembler assembler = new DefaultAssembler();
        return assembler.getEntityFromDTO(dto, entity);
    }
}
