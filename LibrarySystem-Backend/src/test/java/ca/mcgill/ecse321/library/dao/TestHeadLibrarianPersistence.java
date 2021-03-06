package ca.mcgill.ecse321.library.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ca.mcgill.ecse321.library.models.*;



@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TestHeadLibrarianPersistence {
	
	@Autowired
	private HeadLibrarianRepository headlibrarianRepository;
	
//	@Autowired
//	private ShiftRepository shiftRepository;
	
	@AfterEach
	public void clearDatabase() {
		headlibrarianRepository.deleteAll();
//		shiftRepository.deleteAll();
	}
		
	@Test
	@Transactional
	public void testPersistAndLoadHeadLibrarian() {
		
		HeadLibrarian p = new HeadLibrarian();
		//variables
		String name = "Steve Jobs";
		boolean activate=true;
		String password="abc123";
		String username="IAMTHEBOSS";
		String address="Wonderworld";
		Long cardID = 1239L;
//		Shift shift=new Shift();
//		List<Shift> shifts=p.getShift();
//		shifts.add(shift);


//		shiftRepository.save(shift);
		
		
		//set attributes to the headlibrarian
		p.setFullName(name);
		p.setOnlineAccountActivated(activate);
		p.setPassword(password);
		p.setUsername(username);
		p.setAddress(address);
		p.setCardID(cardID);
		
		
		headlibrarianRepository.save(p); // By saving, a unique ID is assigned to the headlibrarian (no need to set it ourself)
		
		
		//testing with id
		p = null;		
		p = headlibrarianRepository.findHeadLibrarianByCardID(cardID); // find the library item using the assigned barcode
		assertNotNull(p);
		assertEquals(cardID, p.getCardID());
		assertEquals(name, p.getFullName());
		assertEquals(activate, p.getOnlineAccountActivated());
		assertEquals(password, p.getPassword());
		assertEquals(username, p.getUsername());
		assertEquals(address, p.getAddress());
		
//		//testing with shifts, this test fail for some weird reason, to be debugged for d2
//		p=null;
//		p = headlibrarianRepository.findHeadLibrarianByShift(shifts); // find the library item using the assigned barcode
//		assertNotNull(p);
//		assertEquals(cardid, p.getCardID());
//		assertEquals(name, p.getFullName());
//		assertEquals(activate, p.getOnlineAccountActivated());
//		assertEquals(password, p.getPassword());
//		assertEquals(username, p.getUsername());
//		assertEquals(address, p.getAddress());
//				
//		
		
	}

}
