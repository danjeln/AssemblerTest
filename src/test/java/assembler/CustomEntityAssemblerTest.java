package assembler;

import datalog.Datalog;
import dto.CustomEntityDTO;
import entities.CustomEntity;
import exception.AssemblerException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import datalog.ChangedData;
import datalog.ChangedDataType;

import java.util.List;

import static org.junit.Assert.*;

public class CustomEntityAssemblerTest {

    @Before
    public void setup() {
        ChangedData.INSTANCE.reset();
    }

    @After
    public void tearDown() {
        List<Datalog> lst = getChangedProperties();
        assertTrue(lst.size() > 0);
    }

    @SuppressWarnings("unchecked")
    private List<Datalog> getChangedProperties() {
        return (List<Datalog>) ChangedData.INSTANCE.getData(ChangedDataType.CHANGED_PROPERTIES);
    }

    @Test
    public void CustomAssemblerConversionTest() throws AssemblerException {
        CustomEntity t = new CustomEntity();
        t.setCustomValue("Testing1234");
        t.setYesOrNo(true);
        AbstractAssembler assembler = AssemblerFactory.INSTANCE.createAssembler("CustomEntity");
        CustomEntityDTO dto = (CustomEntityDTO) assembler.getDTOFromEntity(t);

        dto.setCustomValue("Hej");

        CustomEntity t2 = (CustomEntity) assembler.getEntityFromDTO(dto,t);

        assertNotNull(t2.getDatum1());
        System.out.println(dto.isYesOrNo());

        List<Datalog> lst = getChangedProperties();
        assertEquals("customValue", lst.get(0).getDescription());



    }
}