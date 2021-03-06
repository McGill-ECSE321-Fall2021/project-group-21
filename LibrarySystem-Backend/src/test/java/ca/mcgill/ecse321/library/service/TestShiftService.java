package ca.mcgill.ecse321.library.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.ArgumentMatchers.anyLong;

import java.sql.Time;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.mcgill.ecse321.library.dao.ShiftRepository;
import ca.mcgill.ecse321.library.models.ApplicationUser;
import ca.mcgill.ecse321.library.models.Citizen;
import ca.mcgill.ecse321.library.models.HeadLibrarian;
import ca.mcgill.ecse321.library.models.Librarian;
import ca.mcgill.ecse321.library.models.Shift;
import ca.mcgill.ecse321.library.models.Shift.DayOfWeek;

/*
 * @Author: Joris Ah-Kane
 */

@ExtendWith(MockitoExtension.class)
public class TestShiftService {
    
    @Mock
	private ShiftRepository shiftRepository;
	
	@InjectMocks
	private ShiftService service;

    private static final DayOfWeek day1 = DayOfWeek.Monday;
	private static final Time startTime = Time.valueOf("08:00:00");
	private static final Time endTime = Time.valueOf("17:00:00");
    private static final Long shiftCode = 123L;
	private static final ApplicationUser librarian_user = new Librarian();
	private static final ApplicationUser headlib_user = new HeadLibrarian();
    private static final Long wrongShiftCode = 420L;

    @BeforeEach
	public void SetMockUp() {
		lenient().when(shiftRepository.findShiftByShiftCode(anyLong())).thenAnswer((InvocationOnMock invocation) -> {
			if (invocation.getArgument(0).equals(shiftCode)) {
				Shift aShift = new Shift();
                aShift.setShiftCode(shiftCode);
				aShift.setStartTime(startTime);
				aShift.setEndTime(endTime);
                aShift.setDay(day1);
				aShift.setApplicationUser(librarian_user);
				return aShift;
			}
			else {
				return null;
			}
		});	
	}

    //-------------------------------------------------------------------------------------------
    //--------------------------------------TEST CREATE------------------------------------------
    //-------------------------------------------------------------------------------------------
    @Test
	public void testCreateShift() {
        assertEquals(0, service.getAllShifts().size());
        
        Long shiftCode = 123L;
		Time startTime = Time.valueOf("06:00:00");
		Time endTime = Time.valueOf("12:30:00");
        DayOfWeek day = DayOfWeek.Monday;
		ApplicationUser applicationUser = new Librarian();

        Shift aShift = null;
        try {
            aShift = service.createShift(shiftCode, startTime, endTime, day, applicationUser);
        } catch (IllegalArgumentException e){
            fail();
        }
        assertNotNull(aShift);
        assertEquals(shiftCode, aShift.getShiftCode());
        assertEquals(startTime, aShift.getStartTime());
        assertEquals(endTime, aShift.getEndTime());
        assertEquals(day ,aShift.getDay());
    }

    //now test the fail cases
    @Test
	public void testCreateShiftNoStart() {
        Long shiftCode = 123L;
		Time startTime = null;
		Time endTime = Time.valueOf("12:30:00");
        DayOfWeek day = DayOfWeek.Monday;
		ApplicationUser applicationUser = new Librarian();

        Shift aShift = null;
        String error = "";
        try {
            aShift = service.createShift(shiftCode, startTime, endTime, day, applicationUser);
        } catch (IllegalArgumentException e){
            error = e.getMessage();
        }
        
        assertNull(aShift);
        assertEquals(error, "Shift must have a starting time");
    }

    @Test
	public void testCreateShiftNoEnd() {
        Long shiftCode = 123L;
		Time startTime = Time.valueOf("06:00:00");
		Time endTime = null;
        DayOfWeek day = DayOfWeek.Monday;
		ApplicationUser applicationUser = new Librarian();

        Shift aShift = null;
        String error = "";
        try {
            aShift = service.createShift(shiftCode, startTime, endTime, day, applicationUser);
        } catch (IllegalArgumentException e){
            error = e.getMessage();
        }
        
        assertNull(aShift);
        assertEquals(error, "Shift must have a ending time");
    }

    @Test
	public void testCreateShiftNoDay() {
        Long shiftCode = 123L;
		Time startTime = Time.valueOf("06:00:00");
		Time endTime = Time.valueOf("12:30:00");
        DayOfWeek day = null;
		ApplicationUser applicationUser = new Librarian();

        Shift aShift = null;
        String error = "";
        try {
            aShift = service.createShift(shiftCode, startTime, endTime, day, applicationUser);
        } catch (IllegalArgumentException e){
            error = e.getMessage();
        }
        
        assertNull(aShift);
        assertEquals(error, "Shift must be on a day of the week");
    }

    @Test
	public void testCreateShiftEndBeforeStart() {
        Long shiftCode = 123L;
        LocalTime startTime = LocalTime.parse("10:00");
		LocalTime endTime = LocalTime.parse("09:00");
        DayOfWeek day = DayOfWeek.Monday;
		ApplicationUser applicationUser = new Librarian();

        Shift aShift = null;
        String error = "";
        try {
            aShift = service.createShift(shiftCode,Time.valueOf(startTime), Time.valueOf(endTime), day, applicationUser);
        } catch (IllegalArgumentException e){
            error = e.getMessage();
        }

        assertNull(aShift);
        assertEquals(error, "Shift end time cannot be before its start time");
    }

	@Test
	public void testCreateShiftNullUser() {
        Long shiftCode = 123L;
        LocalTime startTime = LocalTime.parse("09:00");
		LocalTime endTime = LocalTime.parse("10:00");
        DayOfWeek day = DayOfWeek.Monday;
		ApplicationUser applicationUser = null;

        Shift aShift = null;
        String error = "";
        try {
            aShift = service.createShift(shiftCode,Time.valueOf(startTime), Time.valueOf(endTime), day, applicationUser);
        } catch (IllegalArgumentException e){
            error = e.getMessage();
        }

        assertNull(aShift);
        assertEquals(error, "ApplicationUser cannot be empty");
    }

	@Test
	public void testCreateShiftWrongUser() {
        Long shiftCode = 123L;
        LocalTime startTime = LocalTime.parse("09:00");
		LocalTime endTime = LocalTime.parse("10:00");
        DayOfWeek day = DayOfWeek.Monday;
		ApplicationUser applicationUser = new Citizen();

        Shift aShift = null;
        String error = "";
        try {
            aShift = service.createShift(shiftCode,Time.valueOf(startTime), Time.valueOf(endTime), day, applicationUser);
        } catch (IllegalArgumentException e){
            error = e.getMessage();
        }

        assertNull(aShift);
        assertEquals(error, "Shifts can only be assigned to Librarians or the Headlibrarian");
    }

    //-------------------------------------------------------------------------------------------
    //---------------------------------------TEST GETS-------------------------------------------
    //-------------------------------------------------------------------------------------------

    @Test
	public void testGetExistingShift() {
		assertEquals(shiftCode, service.getShift(shiftCode).getShiftCode());
	}
	
	@Test
	public void testGetNonExistingShift() {
		assertNull(service.getShift(wrongShiftCode));
	}

    //-------------------------------------------------------------------------------------------
    //--------------------------------------TEST DELETE------------------------------------------
    //-------------------------------------------------------------------------------------------

    @Test
    public void testDeleteshift(){
        Long shiftCode = 123L;
        LocalTime startTime = LocalTime.parse("10:00");
		LocalTime endTime = LocalTime.parse("12:00");
        DayOfWeek day = DayOfWeek.Monday;
		ApplicationUser applicationUser = new Librarian();

        Shift aShift = service.createShift(shiftCode,Time.valueOf(startTime), Time.valueOf(endTime), day, applicationUser);

        assertNotNull(aShift);  //make sure the creation was successful

        try {
            aShift = service.deleteShift(aShift);
        } catch (IllegalArgumentException e){
            fail();
        }
        
        assertNull(aShift);
    }

    @Test
    public void testDeleteShiftNull(){
        Shift aShift = null;
        String error = "";

        try {
            aShift = service.deleteShift(aShift);
        } catch (IllegalArgumentException e){
            error = e.getMessage();
        }

        assertNull(aShift);
        assertEquals(error, "Input shift cannot be null");
    }
    //-------------------------------------------------------------------------------------------
    //--------------------------------------TEST UPDATE------------------------------------------
    //-------------------------------------------------------------------------------------------

	//----------------------------------SHIFT UPDATE SUCCESS--------------------------------------
    @Test
	public void testUpdateShift() {
		
		DayOfWeek day = DayOfWeek.Tuesday;
		LocalTime startTime = LocalTime.parse("09:00");
		LocalTime endTime = LocalTime.parse("10:30");
        Long shiftCode = 123L;
		ApplicationUser applicationUser = new Librarian();


		Shift S1 = service.createShift(shiftCode, Time.valueOf(startTime), Time.valueOf(endTime), day, applicationUser);
		
		Shift S2 = null;
		Time startTime2 =  Time.valueOf(LocalTime.parse("06:30"));
		Time endTime2 =  Time.valueOf(LocalTime.parse("11:30"));
		DayOfWeek day2 = DayOfWeek.Saturday;
		ApplicationUser applicationUser2 = new HeadLibrarian();
		
		try {
			S2 = service.updateShift(S1, startTime2, endTime2, day2, applicationUser2);
		} catch(IllegalArgumentException e) {
		 	fail();
		}
		
		assertNotNull(S2);
		assertEquals(startTime2,S2.getStartTime());
		assertEquals(endTime2, S2.getEndTime());
		assertEquals(day2, S2.getDay());
		assertEquals(applicationUser2, S2.getApplicationUser());
		assertEquals(shiftCode,S2.getShiftCode()); //Unchanged
	}

	//----------------------------------NULL SHIFT UPDATE FAIL---------------------------------------

	@Test
	public void testUpdateShiftNoShift(){
		Shift S1 = null;
		
		Shift S2 = null;
		Time startTime2 =  Time.valueOf(LocalTime.parse("06:30"));
		Time endTime2 =  Time.valueOf(LocalTime.parse("11:30"));
		DayOfWeek day2 = DayOfWeek.Saturday;
		ApplicationUser applicationUser2 = new HeadLibrarian();
		
		String error = "";
		try {
			S2 = service.updateShift(S1, startTime2, endTime2, day2, applicationUser2);
		} catch(IllegalArgumentException e) {
			error = e.getMessage();
		}
		assertNull(S2);
		assertEquals(error, "Input shift cannot be null");
    
	}
	//---------------------------------------DAY UPDATE FAIL---------------------------------------

	@Test
    public void testUpdateShiftDayNullDay(){
        DayOfWeek day = DayOfWeek.Tuesday;
		LocalTime startTime = LocalTime.parse("09:00");
		LocalTime endTime = LocalTime.parse("10:30");
        Long shiftCode = 123L;
		ApplicationUser applicationUser = new Librarian();

		
		Shift S1 = service.createShift(shiftCode, Time.valueOf(startTime), Time.valueOf(endTime), day, applicationUser);

		Shift S2 = null;
		Shift.DayOfWeek day2 = null;
		String error = "";
		try {
			S2 = service.updateShift(S1, Time.valueOf(startTime), Time.valueOf(endTime), day2, applicationUser);
		} catch(IllegalArgumentException e) {
            error = e.getMessage();
		}
		//we expect a fail
		assertNull(S2);
		assertEquals(error, "Shift must be on a day of the week");
    }

    //------------------------------START AND END TIMES UPDATE FAILS--------------------------------------

    @Test
	public void testUpdateShiftNullStartTime() {
		
		DayOfWeek day = DayOfWeek.Tuesday;
		LocalTime startTime = LocalTime.parse("09:00");
		LocalTime endTime = LocalTime.parse("10:30");
        Long shiftCode = 123L;
		ApplicationUser applicationUser = new Librarian();
		
		Shift S1 = service.createShift(shiftCode, Time.valueOf(startTime), Time.valueOf(endTime), day, applicationUser);
		
		Shift S2 = null;
		Time startTime2 =  null;
		String error = "";
		try {
			S2= service.updateShift(S1, startTime2, Time.valueOf(endTime), day, applicationUser);
		} catch(IllegalArgumentException e) {
            error = e.getMessage();
		}
		
		assertNull(S2);
		assertEquals(error, "Shift must have a starting time");
	}

    @Test
	public void testUpdateShiftNullEndTime() {
		
		DayOfWeek day = DayOfWeek.Tuesday;
		LocalTime startTime = LocalTime.parse("09:00");
		LocalTime endTime = LocalTime.parse("10:30");
        Long shiftCode = 123L;
		ApplicationUser applicationUser = new Librarian();

		Shift S1 = service.createShift(shiftCode, Time.valueOf(startTime), Time.valueOf(endTime), day, applicationUser);
		
		Shift S2 = null;
		Time endTime2 =  null;
		String error = "";
		try {
			S2= service.updateShift(S1, Time.valueOf(startTime), endTime2, day, applicationUser);
		} catch(IllegalArgumentException e) {
            error = e.getMessage();
		}
		
		assertNull(S2);
		assertEquals(error, "Shift must have a ending time");
	}

    @Test
	public void testUpdateShiftWrongTime() {
		DayOfWeek day = DayOfWeek.Tuesday;
		LocalTime startTime = LocalTime.parse("09:00");
		LocalTime endTime = LocalTime.parse("10:30");
        Long shiftCode = 123L;
		ApplicationUser applicationUser = new Librarian();

		Shift S1 = service.createShift(shiftCode, Time.valueOf(startTime), Time.valueOf(endTime), day, applicationUser);
		
		Shift S2 = null;
		Time endTime2 =  Time.valueOf(LocalTime.parse("08:30"));
		String error = "";
		try {
			S2= service.updateShift(S1, Time.valueOf(startTime), endTime2, day, applicationUser);
		} catch(IllegalArgumentException e) {
            error = e.getMessage();
		}
		
		assertNull(S2);
		assertEquals(error, "Shift end time cannot be before its start time");
	}

	//----------------------------ASSOCIATED EMPLOYEE UPDATE FAILS------------------------------------
	
	@Test
	public void testUpdateShiftWrongUser(){
		Long shiftCode = 123L;
		Time startTime = Time.valueOf("06:00:00");
		Time endTime = Time.valueOf("12:30:00");
        DayOfWeek day = DayOfWeek.Monday;
		ApplicationUser applicationUser = new Librarian();

        Shift S1 = service.createShift(shiftCode, startTime, endTime, day, applicationUser);
        Shift S2 = null;
		ApplicationUser applicationUser2 = new Citizen();
		String error = "";

		try {
			S2= service.updateShift(S1, startTime, endTime, day, applicationUser2);
		} catch(IllegalArgumentException e) {
            error = e.getMessage();
		}
		
		assertNull(S2);
		assertEquals(error, "Shifts can only be assigned to Librarians or the Headlibrarian");
	}

	@Test
	public void testUpdateShiftNullUser(){
		Long shiftCode = 123L;
		Time startTime = Time.valueOf("06:00:00");
		Time endTime = Time.valueOf("12:30:00");
        DayOfWeek day = DayOfWeek.Monday;
		ApplicationUser applicationUser = new Librarian();

        Shift S1 = service.createShift(shiftCode, startTime, endTime, day, applicationUser);
        Shift S2 = null;
		ApplicationUser applicationUser2 = null;
		String error = "";

		try {
			S2= service.updateShift(S1, startTime, endTime, day, applicationUser2);
		} catch(IllegalArgumentException e) {
            error = e.getMessage();
		}
		
		assertNull(S2);
		assertEquals(error, "ApplicationUser cannot be empty");
	}
}
