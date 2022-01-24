package de.diedavids.jmix.softreference.cuba;

import de.diedavids.jmix.softreference.cuba.test_support.Document;
import io.jmix.core.DataManager;
import io.jmix.core.Id;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SoftReferenceCubaTest {

	@Autowired
	DataManager dataManager;

	@Test
	void contextLoads() {
	}

	@Test
	void testDocumentSave() {
		Document document = dataManager.create(Document.class);
		document.setName("abc");

		Document document1 = dataManager.save(document);
		assertEquals(document, document1);

		Document document2 = dataManager.load(Id.of(document)).one();
		assertEquals(document, document2);
	}
}
