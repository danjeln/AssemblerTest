package assembler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import dto.CustomEntityDTO;
import entities.CustomEntity;
import exception.AssemblerException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import request.RequestData;
import request.RequestDataType;

import java.util.List;

public class CustomEntityAssemblerTest {

    @Before
    public void setup() {
        RequestData.INSTANCE.reset();
    }

    @After
    public void tearDown() {
        List<String> lst = getChangedProperties();
        assertTrue(lst.size() > 0);
    }

    @SuppressWarnings("unchecked")
    private List<String> getChangedProperties() {
        return (List<String>) RequestData.INSTANCE.getData(RequestDataType.CHANGED_PROPERTIES);
    }

    @Test
    public void CustomAssemblerConversionTest() throws AssemblerException {
        CustomEntity t = new CustomEntity();
        t.setCustomValue("Testing1234");
        AbstractAssembler assembler = AssemblerFactory.INSTANCE.createAssembler();
        CustomEntityDTO dto = (CustomEntityDTO) assembler.getDTOFromEntity(t);

        dto.setCustomValue("Hej");

        CustomEntity t2 = (CustomEntity) assembler.getEntityFromDTO(dto,t);

        List<String> lst = getChangedProperties();
        assertEquals("customValue", lst.get(0));



    }
}