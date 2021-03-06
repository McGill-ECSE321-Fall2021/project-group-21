package ca.mcgill.ecse321.library.controller;

import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.library.dto.ApplicationUserDto;
import ca.mcgill.ecse321.library.dto.ShiftDto;
import ca.mcgill.ecse321.library.models.*;
import ca.mcgill.ecse321.library.models.Shift.DayOfWeek;
import ca.mcgill.ecse321.library.service.CitizenService;
import ca.mcgill.ecse321.library.service.HeadLibrarianService;
import ca.mcgill.ecse321.library.service.LibrarianService;
import ca.mcgill.ecse321.library.service.ShiftService;

@CrossOrigin(origins = "*")
@RestController
public class ShiftController {

    @Autowired
    private ShiftService shiftService;
	
	@Autowired
	private LibrarianService librarianService;
	
	@Autowired
	private HeadLibrarianService headLibrarianService;
	
	@Autowired
	private CitizenService citizenService;
	/*
     * @Author: Joris Ah-Kane
     * Get all shifts, either by shiftcode or simply getting them all
     */

    @GetMapping(value = { "/shifts", "/shifts/"} )
	public List<ShiftDto> getAllShifts(){
		List<ShiftDto> shiftdtos = new ArrayList <>();
		for (Shift shift : shiftService.getAllShifts()) {
			shiftdtos.add(convertToDto(shift));
		}
		return shiftdtos;
	}

    @GetMapping(value = { "/shifts/{shiftCode}", "/shifts/{shiftCode}/"})
	public ShiftDto getLibrarianById(@PathVariable("shiftCode") Long shiftCode) throws IllegalArgumentException{
		return convertToDto(shiftService.getShift(shiftCode));
	}

	/*
     * @Author: Joris Ah-Kane
     * Create a shift
     */
    
    @PostMapping(value = { "/shifts/{shiftCode}", "/shifts/{shiftCode}/"} )
    public ShiftDto createShiftDto(@PathVariable("shiftCode") Long shiftCode, 
    @RequestParam String startTime, @RequestParam String endTime,
    @RequestParam String day, @RequestParam("cardID") Long cardID) throws IllegalArgumentException{

    	// convert string to enum type DayOfWeek
    	Shift.DayOfWeek dayOfWeek = null;
    	if (day.equalsIgnoreCase("Monday")) {
    		dayOfWeek = Shift.DayOfWeek.Monday;
    	}else if (day.equalsIgnoreCase("Tuesday")) {
    		dayOfWeek = Shift.DayOfWeek.Tuesday;
    	}else if (day.equalsIgnoreCase("Wednesday")) {
    		dayOfWeek = Shift.DayOfWeek.Wednesday;
    	}else if (day.equalsIgnoreCase("Thursday")) {
    		dayOfWeek = Shift.DayOfWeek.Thursday;
    	}else if (day.equalsIgnoreCase("Friday")) {
    		dayOfWeek = Shift.DayOfWeek.Friday;
    	}else if (day.equalsIgnoreCase("Saturday")) {
    		dayOfWeek = Shift.DayOfWeek.Saturday;
    	}else if (day.equalsIgnoreCase("Sunday")) {
    		dayOfWeek = Shift.DayOfWeek.Sunday;
    	}
    		
		
		Citizen c = citizenService.getCitizenByID(cardID);
		Librarian l = librarianService.getLibrarianByID(cardID);
		HeadLibrarian hl = headLibrarianService.getHeadLibrarianByID(cardID);
		ApplicationUser user = null;	
		//this will at most be one of these types of users
		if (c != null) user = c;
		if (l != null) user = l; 
		if (hl != null) user = hl; 
        //keep the user null if the user is a citizen

		Shift shift = shiftService.createShift(shiftCode,Time.valueOf(startTime),Time.valueOf(endTime), dayOfWeek, user);
		return convertToDto(shift);
	}
    
    
    /*
     * @Author: Joris Ah-Kane
     * Delete a shift
     */
    @DeleteMapping(value = { "/shifts/{shiftCode}", "/shifts/{shiftCode}/" })
	public void deleteLibrarian(@PathVariable("shiftCode") Long shiftCode) throws IllegalArgumentException {
		Shift shift = shiftService.getShift(shiftCode);
		shiftService.deleteShift(shift);
	}
    
    
    @PatchMapping(value = { "/shifts/{shiftCode}", "/shifts/{shiftCode}/" })
	public ShiftDto editShift(@PathVariable ("shiftCode") Long shiftCode, 
			@RequestParam String startTime, @RequestParam String endTime,
			@RequestParam String day, @RequestParam Long cardID)throws IllegalArgumentException{
			
			Citizen c = citizenService.getCitizenByID(cardID);
    		Librarian l = librarianService.getLibrarianByID(cardID);
    		HeadLibrarian hl = headLibrarianService.getHeadLibrarianByID(cardID);
    		ApplicationUser user = null;	
			if (c != null) user = c;	
    		if (l != null) user = l; 
    		if (hl != null) user = hl; 
    	
    		Shift.DayOfWeek dayOfWeek = null;
        	if (day.equalsIgnoreCase("Monday")) {
        		dayOfWeek = Shift.DayOfWeek.Monday;
        	}else if (day.equalsIgnoreCase("Tuesday")) {
        		dayOfWeek = Shift.DayOfWeek.Tuesday;
        	}else if (day.equalsIgnoreCase("Wednesday")) {
        		dayOfWeek = Shift.DayOfWeek.Wednesday;
        	}else if (day.equalsIgnoreCase("Thursday")) {
        		dayOfWeek = Shift.DayOfWeek.Thursday;
        	}else if (day.equalsIgnoreCase("Friday")) {
        		dayOfWeek = Shift.DayOfWeek.Friday;
        	}else if (day.equalsIgnoreCase("Saturday")) {
        		dayOfWeek = Shift.DayOfWeek.Saturday;
        	}else if (day.equalsIgnoreCase("Sunday")) {
        		dayOfWeek = Shift.DayOfWeek.Sunday;
        	}
		
    	Shift shift = shiftService.getShift(shiftCode);
		shiftService.updateShift(shift, Time.valueOf(startTime), Time.valueOf(endTime), dayOfWeek, user);
		return convertToDto(shift);
	}

	
	/*
     * @Author: Joris Ah-Kane
     * Helper methods
     */

    private ShiftDto convertToDto(Shift s){
        if(s == null) {
			throw new IllegalArgumentException("Input shift cannot be null. ");
		}

        ShiftDto shiftDto = new ShiftDto( s.getShiftCode(), s.getStartTime(), s.getEndTime(), s.getDay(), convertToDto(s.getApplicationUser()));
        return shiftDto;
    }

    private ApplicationUserDto convertToDto (ApplicationUser user) {
		if (user == null) {
			throw new IllegalArgumentException("There is no such an application user");
		}
		ApplicationUserDto userDto = new ApplicationUserDto(user.getCardID(), user.getFullName(), user.getAddress(), user.getUsername(), user.getPassword());
		return userDto;
	}
}
