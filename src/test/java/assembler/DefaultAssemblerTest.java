package assembler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import dto.*;
import entities.*;
import exception.AssemblerException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import request.RequestData;
import request.RequestDataType;


/**
 * Test för defaultassembler
 * 
 * @author u0064563
 *
 */
public class DefaultAssemblerTest {

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

	/**
	 * Enkelt test som för över en sträng mellan entity och dto
	 * 
	 * @throws AssemblerException
	 */
	@Test
	public void StringConversionTest() throws AssemblerException {
		TestString t = new TestString();
		t.setString("Testing1234");
		AbstractAssembler assembler = AssemblerFactory.INSTANCE.createAssembler();
		TestStringDTO dto = (TestStringDTO) assembler.getDTOFromEntity(t);

		assertEquals(t.getString(), dto.getString());

		TestString t2 = (TestString) assembler.getEntityFromDTO(dto);
		assertEquals(t2.getString(), dto.getString());

	}

	/**
	 * För över en sträng mappad mot annan property mellan entity och dto
	 * 
	 * @throws AssemblerException
	 */
	@Test
	public void MappedStringConversionTest() throws AssemblerException {
		TestMappedString t = new TestMappedString();
		t.setString("Testing1234");
		AbstractAssembler assembler = AssemblerFactory.INSTANCE.createAssembler("Default");
		TestMappedStringDTO dto = (TestMappedStringDTO) assembler.getDTOFromEntity(t);

		assertEquals(t.getString(), dto.getBrandNewValue());

		TestMappedString t2 = (TestMappedString) assembler.getEntityFromDTO(dto);
		assertEquals(t2.getString(), dto.getBrandNewValue());
	}

	/**
	 * För över en sträng mellan dto och entity. vid första överföringen skickas
	 * originalentity med som referens, då har inte värdet ändrats. Vid andra
	 * transfer har värdet ändrats och det skall loggas. Den kommer då logga(lagra i
	 * changed properties listan) fältets loggingdescriptor
	 * 
	 * @throws AssemblerException
	 */
	@Test
	public void ChangedStringConversionTest() throws AssemblerException {
		TestChangedStringDTO dto = new TestChangedStringDTO();
		TestChangedString entity = new TestChangedString();
		dto.setString("test");
		entity.setString("test");

		AbstractAssembler assembler = AssemblerFactory.INSTANCE.createAssembler("Default");
		entity = (TestChangedString) assembler.getEntityFromDTO(dto, entity);
		List<String> lst = getChangedProperties();
		assertTrue(lst == null);

		entity.setString("test2");
		entity = (TestChangedString) assembler.getEntityFromDTO(dto, entity);
		lst = getChangedProperties();
		assertTrue(lst.size() == 1);
		assertEquals("En string har ändrats", lst.get(0));

	}

	/**
	 * Enkelt test som testar överföring av datum mellan entity och dto
	 * 
	 * @throws AssemblerException
	 */
	@Test
	public void DateConversionTest() throws AssemblerException {
		TestDate d = new TestDate();
		d.setDatum(new Date());
		AbstractAssembler assembler = AssemblerFactory.INSTANCE.createAssembler("Default");
		TestDateDTO dto = (TestDateDTO) assembler.getDTOFromEntity(d);

		assertEquals(d.getDatum(), dto.getDatum());

		TestDate d2 = (TestDate) assembler.getEntityFromDTO(dto);
		assertEquals(d2.getDatum(), dto.getDatum());

	}

	/**
	 * För över ett helt objekt med barn som också är entiteter mellan en parent
	 * entity och en dto
	 * 
	 * @throws AssemblerException
	 */
	@Test
	public void ObjectConversionTest() throws AssemblerException {
		TestObjectParent p = new TestObjectParent();
		p.setParent("test parent");
		TestObjectChild c = new TestObjectChild();
		c.setChild("test child");
		p.setChild(c);

		AbstractAssembler assembler = AssemblerFactory.INSTANCE.createAssembler("Default");
		TestObjectParentDTO dto = (TestObjectParentDTO) assembler.getDTOFromEntity(p);

		assertEquals(p.getParent(), dto.getParent());
		assertTrue(dto.getChild() instanceof TestObjectChildDTO);
		assertEquals(p.getChild().getChild(), dto.getChild().getChild());

		TestObjectParent p2 = (TestObjectParent) assembler.getEntityFromDTO(dto);

		assertEquals(p2.getParent(), dto.getParent());
		assertTrue(p2.getChild() instanceof TestObjectChild);
		assertEquals(p2.getChild().getChild(), dto.getChild().getChild());

	}

	/**
	 * För över lista med entity med enkla typer mellan entity och dto
	 * 
	 * @throws AssemblerException
	 */
	@Test
	public void ListStringConversionTest() throws AssemblerException {

		TestString s = new TestString();
		s.setString("test");
		TestList<TestString> t = new TestList<>();
		t.addToList(s);

		AbstractAssembler assembler = AssemblerFactory.INSTANCE.createAssembler("Default");
		TestListDTO<TestStringDTO> dto = (TestListDTO<TestStringDTO>) assembler.getDTOFromEntity(t);

		assertTrue(dto.getList().size() == 1);
		assertEquals(t.getList().get(0).getString(), dto.getList().get(0).getString());

		TestList<TestString> t2 = (TestList<TestString>) assembler.getEntityFromDTO(dto);
		assertTrue(t2.getList().size() == 1);
		assertEquals(t2.getList().get(0).getString(), dto.getList().get(0).getString());

	}

	/**
	 * För över lista med enkla typer mellan entity och dto
	 * 
	 * @throws AssemblerException
	 */
	@Test
	public void ListObjectConversionTest() throws AssemblerException {

		TestList<String> t = new TestList<>();
		t.addToList("Test1");
		t.addToList("Test2");

		AbstractAssembler assembler = AssemblerFactory.INSTANCE.createAssembler("Default");
		TestListDTO<String> dto = (TestListDTO<String>) assembler.getDTOFromEntity(t);

		assertTrue(dto.getList().size() == 2);
		assertEquals(t.getList().get(0), dto.getList().get(0));

		dto.addToList("Test3");

		TestList<String> t2 = (TestList<String>) assembler.getEntityFromDTO(dto);
		assertTrue(t2.getList().size() == 3);
		assertEquals(t2.getList().get(0), dto.getList().get(0));

	}

	/**
	 * För över en lista med strängar från entity till dto, lägger sedan till ett
	 * nytt object på dto'n. loggar sedan tillägget i listan samt det ändrade
	 * värdet.
	 * 
	 * @throws AssemblerException
	 */
	@Test
	public void AddedToListConversionTest() throws AssemblerException {

		TestString s = new TestString();
		s.setString("test");
		s.setId(1l);
		TestList<TestString> t = new TestList<>();
		t.addToList(s);

		AbstractAssembler assembler = AssemblerFactory.INSTANCE.createAssembler("Default");
		TestListDTO<TestStringDTO> dto = (TestListDTO<TestStringDTO>) assembler.getDTOFromEntity(t);

		assertTrue(dto.getList().size() == 1);
		assertEquals(t.getList().get(0).getString(), dto.getList().get(0).getString());

		TestStringDTO dto2 = new TestStringDTO("test2");
		dto2.setId(2l);
		dto.addToList(dto2);

		TestList<TestString> t2 = (TestList<TestString>) assembler.getEntityFromDTO(dto, t);
		assertTrue(t2.getList().size() == 2);
		List<String> lst = getChangedProperties();
		assertTrue(lst.size() == 2);

	}

}
