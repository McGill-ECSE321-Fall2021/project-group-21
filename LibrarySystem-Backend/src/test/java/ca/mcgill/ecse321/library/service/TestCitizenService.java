package ca.mcgill.ecse321.library.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import ca.mcgill.ecse321.library.dao.CitizenRepository;
import ca.mcgill.ecse321.library.models.Citizen;

@ExtendWith(MockitoExtension.class)
public class TestCitizenService {
	
	@Mock
	private CitizenRepository citizenRepository;
	
	@InjectMocks
	private CitizenService service;
	
	private static final String fullname = "Noshin Chowdhury";
	private static final String username = "KidA";
	private static final String password = "noshinKidA";
	private static final String address = "1650, YoWorld";
	private static final Boolean onlineAccountActive = true;
	private static final Boolean isLocal = false;
	private static final Double balance = 520.0;
	private static final Long cardID = 123L;
	private static final Long wrongID = 909l;
	
	@BeforeEach
	public void setMockOutput() {

//		lenient().when(citizenRepository.save(any(Citizen.class))).
//		thenAnswer(returnParameterAsAnswer);
		
		lenient().when(citizenRepository.findCitizenByCardID(anyLong())).thenAnswer((InvocationOnMock invocation) -> {
			if (invocation.getArgument(0).equals(cardID)) {
				
				Citizen aCitizen = new Citizen();
				aCitizen.setFullName(fullname);
				aCitizen.setUsername(username);
				aCitizen.setPassword(password);
				aCitizen.setAddress(address);
				aCitizen.setOnlineAccountActivated(onlineAccountActive);
				aCitizen.setIsLocal(isLocal);
				aCitizen.setBalance(balance);
				aCitizen.setCardID(cardID);

				return aCitizen;
			} else {
			return null;
			}
		});

	}
	
	@Test
	public void testCreatCitizen() {
		assertEquals(0, service.getAllCitizen().size());
		String fullname = "Noshin Chowdhury";
		String username = "KidA";
		String password = "noshinKidA";
		String address = "1650, YoWorld";
		Boolean onlineAccountActive = true;
		Boolean isLocal = false;
		Double balance = 520.0;
		Long cardID = 123L;
		
		Citizen citizen = null;
		try {
			citizen = service.creatCitizen(fullname, username, password, address, onlineAccountActive, isLocal, balance,cardID);
		} catch(IllegalArgumentException e) {
			fail();
		}

	//	System.out.println("citizen:" + citizenRepository.findCitizenByCardID(cardID).getFullName());
		assertNotNull(citizen);
		assertEquals(fullname, citizen.getFullName());
		assertEquals(username, citizen.getUsername());
		assertEquals(password, citizen.getPassword());
		assertEquals(address, citizen.getAddress());
		assertEquals(onlineAccountActive, citizen.getOnlineAccountActivated());
		assertEquals(isLocal, citizen.getIsLocal());
		assertEquals(balance,citizen.getBalance());
		assertEquals(cardID, citizen.getCardID());
	}
	
	@Test
	public void testCreateCitizenWithNoCardID() {
		String fullname = "aa";
		String username = "KidA";
		String password = "noshinKidA";
		String address = "1650, YoWorld";
		Boolean onlineAccountActive = true;
		Boolean isLocal = false;
		Double balance = 520.0;
		Long cardID = null;
		
		Citizen citizen = null;
		String error = "";
		try {
			citizen = service.creatCitizen(fullname, username, password, address, onlineAccountActive, isLocal, balance,cardID);
		} catch(IllegalArgumentException e) {
			error = e.getMessage();
		}
		
		assertNull(citizen);
		assertEquals(error,"CardID can't be empty");
	}
	
	@Test
	public void testCreateCitizenWithNoFullname() {
		String fullname = null;
		String username = "KidA";
		String password = "noshinKidA";
		String address = "1650, YoWorld";
		Boolean onlineAccountActive = true;
		Boolean isLocal = false;
		Double balance = 520.0;
		Long cardID = 123L;
		
		Citizen citizen = null;
		String error = "";
		try {
			citizen = service.creatCitizen(fullname, username, password, address, onlineAccountActive, isLocal, balance,cardID);
		} catch(IllegalArgumentException e) {
			error = e.getMessage();
		}
		
		assertNull(citizen);
		assertEquals(error,"Fullname can't be empty");
	}
	
	@Test
	public void testCreateCitizenWithNoUsername() {
		String fullname = "Noshin Chowdhury";
		String username = null;
		String password = "noshinKidA";
		String address = "1650, YoWorld";
		Boolean onlineAccountActive = true;
		Boolean isLocal = false;
		Double balance = 520.0;
		Long cardID = 123L;
		Citizen citizen = null;
		String error = "";
		try {
			citizen = service.creatCitizen(fullname, username, password, address, onlineAccountActive, isLocal, balance,cardID);
		} catch(IllegalArgumentException e) {
			error = e.getMessage();
		}
		
		assertNull(citizen);
		assertEquals(error,"Username can't be empty");
	}
	
	@Test
	public void testCreateCitizenWithNoPassword() {
		String fullname = "Noshin Chowdhury";
		String username = "KidA";
		String password = null;
		String address = "1650, YoWorld";
		Boolean onlineAccountActive = true;
		Boolean isLocal = false;
		Double balance = 520.0;
		Long cardID = 123L;
		Citizen citizen = null;
		String error = "";
		try {
			citizen = service.creatCitizen(fullname, username, password, address, onlineAccountActive, isLocal, balance,cardID);
		} catch(IllegalArgumentException e) {
			error = e.getMessage();
		}
		
		assertNull(citizen);
		assertEquals(error,"password can't be empty");
	}
	
	@Test
	public void testCreateCitizenWithNoAddress() {
		String fullname = "Noshin Chowdhury";
		String username = "KidA";
		String password = "noshinKidA";
		String address = null;
		Boolean onlineAccountActive = true;
		Boolean isLocal = false;
		Double balance = 520.0;
		Long cardID = 123L;
		
		Citizen citizen = null;
		String error = "";
		try {
			citizen = service.creatCitizen(fullname, username, password, address, onlineAccountActive, isLocal, balance,cardID);
		} catch(IllegalArgumentException e) {
			error = e.getMessage();
		}
		
		assertNull(citizen);
		assertEquals(error,"Address can't be empty");
	}
	
	@Test
	public void testCreateCitizenWithNoOnlineActive() {
		String fullname = "Noshin Chowdhury";
		String username = "KidA";
		String password = "noshinKidA";
		String address = "1650, YoWorld";
		Boolean onlineAccountActive = null;
		Boolean isLocal = false;
		Double balance = 520.0;
		Long cardID = 123L;
		
		Citizen citizen = null;
		String error = "";
		try {
			citizen = service.creatCitizen(fullname, username, password, address, onlineAccountActive, isLocal, balance,cardID);
		} catch(IllegalArgumentException e) {
			error = e.getMessage();
		}
		assertNull(citizen);
		assertEquals(error,"onlineAccountActivated can't be empty");
	}
	
	@Test
	public void testCreateCitizenWithNoIsLocal() {
		String fullname = "Noshin Chowdhury";
		String username = "KidA";
		String password = "noshinKidA";
		String address = "1650, YoWorld";
		Boolean onlineAccountActive = true;
		Boolean isLocal = null;
		Double balance = 520.0;
		Long cardID = 123L;
		
		Citizen citizen = null;
		String error = "";
		try {
			citizen = service.creatCitizen(fullname, username, password, address, onlineAccountActive, isLocal, balance,cardID);
		} catch(IllegalArgumentException e) {
			error = e.getMessage();
		}
		assertNull(citizen);
		assertEquals(error,"isLocal can't be empty");
	}
	
	@Test
	public void testCreateCitizenWithNoBalance() {
		String fullname = "Noshin Chowdhury";
		String username = "KidA";
		String password = "noshinKidA";
		String address = "1650, YoWorld";
		Boolean onlineAccountActive = true;
		Boolean isLocal = false;
		Double balance = null;
		Long cardID = 123L;
		
		Citizen citizen = null;
		String error = "";
		try {
			citizen = service.creatCitizen(fullname, username, password, address, onlineAccountActive, isLocal, balance,cardID);
		} catch(IllegalArgumentException e) {
			error = e.getMessage();
		}
		assertNull(citizen);
		assertEquals(error,"Balance can't be empty");
	}
	
	@Test
	public void testDeleteCitizen() {
		String fullname = "Noshin Chowdhury";
		String username = "KidA";
		String password = "noshinKidA";
		String address = "1650, YoWorld";
		Boolean onlineAccountActive = true;
		Boolean isLocal = false;
		Double balance = 520.;
		Long cardID = 123L;
		
		Citizen citizen = null;
		try {
			citizen = service.creatCitizen(fullname, username, password, address, onlineAccountActive, isLocal, balance,cardID);
		} catch(IllegalArgumentException e) {
			fail();
		}
		try {
			citizen=service.deleteCitizen(citizen);
			} catch(IllegalArgumentException e) {
				fail();
			}
		assertNull(citizen);
	}

	@Test
	public void testUpdateCitizenFullname() {
		String fullname = "Noshin Chowdhury";
		String username = "KidA";
		String password = "noshinKidA";
		String address = "1650, YoWorld";
		Boolean onlineAccountActive = true;
		Boolean isLocal = false;
		Double balance = 520.;
		Long cardID = 123L;
		
		Citizen citizen = null;
		try {
			citizen = service.creatCitizen(fullname, username, password, address, onlineAccountActive, isLocal, balance,cardID);
		} catch(IllegalArgumentException e) {
			fail();
		}
		String newname="Kaifan Zheng";
		try {
		citizen=service.editCitizenFullname(citizen, newname);
		} catch(IllegalArgumentException e) {
			fail();
		}
		assertNotNull(citizen);
		assertEquals(newname, citizen.getFullName());
	}
	
	@Test
	public void testUpdateCitizenUsername() {
		String fullname = "Noshin Chowdhury";
		String username = "KidA";
		String password = "noshinKidA";
		String address = "1650, YoWorld";
		Boolean onlineAccountActive = true;
		Boolean isLocal = false;
		Double balance = 520.;
		Long cardID = 123L;
		
		Citizen citizen = null;
		try {
			citizen = service.creatCitizen(fullname, username, password, address, onlineAccountActive, isLocal, balance,cardID);
		} catch(IllegalArgumentException e) {
			fail();
		}
		String newusername="KidB";
		try {
		citizen=service.editCitizenUsername(citizen, newusername);
		} catch(IllegalArgumentException e) {
			fail();
		}
		assertNotNull(citizen);
		assertEquals(newusername, citizen.getUsername());
	}
	
	@Test
	public void testUpdateCitizenPassword() {
		String fullname = "Noshin Chowdhury";
		String username = "KidA";
		String password = "noshinKidA";
		String address = "1650, YoWorld";
		Boolean onlineAccountActive = true;
		Boolean isLocal = false;
		Double balance = 520.;
		Long cardID = 123L;
		
		Citizen citizen = null;
		try {
			citizen = service.creatCitizen(fullname, username, password, address, onlineAccountActive, isLocal, balance,cardID);
		} catch(IllegalArgumentException e) {
			fail();
		}
		String newpassword="noshinKidB";
		try {
		citizen=service.editCitizenPassword(citizen, newpassword);
		} catch(IllegalArgumentException e) {
			fail();
		}
		assertNotNull(citizen);
		assertEquals(newpassword, citizen.getPassword());
	}
	
	@Test
	public void testUpdateCitizenAddress() {
		String fullname = "Noshin Chowdhury";
		String username = "KidA";
		String password = "noshinKidA";
		String address = "1650, YoWorld";
		Boolean onlineAccountActive = true;
		Boolean isLocal = false;
		Double balance = 520.;
		Long cardID = 123L;
		
		Citizen citizen = null;
		try {
			citizen = service.creatCitizen(fullname, username, password, address, onlineAccountActive, isLocal, balance,cardID);
		} catch(IllegalArgumentException e) {
			fail();
		}
		String newaddress="1234, Yo";
		try {
		citizen=service.editCitizenAddress(citizen, newaddress);
		} catch(IllegalArgumentException e) {
			fail();
		}
		assertNotNull(citizen);
		assertEquals(newaddress, citizen.getAddress());
	}
	
	@Test
	public void testUpdateCitizenonlineAccountActive() {
		String fullname = "Noshin Chowdhury";
		String username = "KidA";
		String password = "noshinKidA";
		String address = "1650, YoWorld";
		Boolean onlineAccountActive = true;
		Boolean isLocal = false;
		Double balance = 520.;
		Long cardID = 123L;
		
		Citizen citizen = null;
		try {
			citizen = service.creatCitizen(fullname, username, password, address, onlineAccountActive, isLocal, balance,cardID);
		} catch(IllegalArgumentException e) {
			fail();
		}
		boolean newstatus = false;
		try {
		citizen=service.editCitizenOnlineAccountActivated(citizen, newstatus);
		} catch(IllegalArgumentException e) {
			fail();
		}
		assertNotNull(citizen);
		assertEquals(newstatus, citizen.getOnlineAccountActivated());
	}
	
	@Test
	public void testUpdateCitizenisLocal() {
		String fullname = "Noshin Chowdhury";
		String username = "KidA";
		String password = "noshinKidA";
		String address = "1650, YoWorld";
		Boolean onlineAccountActive = true;
		Boolean isLocal = false;
		Double balance = 520.;
		Long cardID = 123L;
		
		Citizen citizen = null;
		try {
			citizen = service.creatCitizen(fullname, username, password, address, onlineAccountActive, isLocal, balance,cardID);
		} catch(IllegalArgumentException e) {
			fail();
		}
		boolean newstatus = true;
		try {
		citizen=service.editCitizenIsLocal(citizen, newstatus);
		} catch(IllegalArgumentException e) {
			fail();
		}
		assertNotNull(citizen);
		assertEquals(newstatus, citizen.getIsLocal());
	}
	
	@Test
	public void testUpdateCitizenBalance() {
		String fullname = "Noshin Chowdhury";
		String username = "KidA";
		String password = "noshinKidA";
		String address = "1650, YoWorld";
		Boolean onlineAccountActive = true;
		Boolean isLocal = false;
		Double balance = 520.;
		Long cardID = 123L;
		
		Citizen citizen = null;
		try {
			citizen = service.creatCitizen(fullname, username, password, address, onlineAccountActive, isLocal, balance,cardID);
		} catch(IllegalArgumentException e) {
			fail();
		}
		Double newbalance = 521.;
		try {
		citizen=service.editCitizenBalance(citizen, newbalance);
		} catch(IllegalArgumentException e) {
			fail();
		}
		assertNotNull(citizen);
		assertEquals(newbalance, citizen.getBalance());
	}
	
	@Test
	public void testGetExistingCitizen() {
		assertEquals(cardID, service.getCitizenByID(cardID).getCardID());
	}
	
	@Test
	public void testGetNonExistingCitizen() {
		assertNull(service.getCitizenByID(wrongID));
	}
	
}
