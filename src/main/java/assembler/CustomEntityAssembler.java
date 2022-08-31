package assembler;

import dto.CustomEntityDTO;
import dto.MasterDTO;
import entities.CustomEntity;
import entities.MasterEntity;
import exception.AssemblerException;

public class CustomEntityAssembler extends AbstractAssembler{
    @Override
    public MasterDTO getDTOFromEntity(MasterEntity entity, MasterDTO dto) throws AssemblerException {
        DefaultAssembler assembler = new DefaultAssembler();
        CustomEntityDTO res = (CustomEntityDTO) assembler.getDTOFromEntity(entity, dto);
        //res.setCustomValue(((CustomEntity)entity).getCustomValue());
        return res;
    }

    @Override
    public MasterEntity getEntityFromDTO(MasterDTO dto, MasterEntity entity) throws AssemblerException {
        DefaultAssembler assembler = new DefaultAssembler();
        return assembler.getEntityFromDTO(dto, entity);
    }
}
