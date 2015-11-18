package com.socialcommunity.controller;

import java.security.NoSuchAlgorithmException;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.socialcommunity.constants.Status;
import com.socialcommunity.domain.Person;
import com.socialcommunity.domain.Search;
import com.socialcommunity.domain.UpdatePerson;
import com.socialcommunity.service.AdminService;


@Controller
public class AdminController {

	protected static Logger logger = Logger.getLogger("Admincontroller");
	private String username;
	private String DOB;
	private Status currentStatus;
	private Status updateTheStatus;
	private int numberOfUsers;
	private List<Status> statusList = new ArrayList<Status>();
	
	
	public Status getUpdateTheStatus() {
		return updateTheStatus;
	}

	public void setUpdateTheStatus(Status updateTheStatus) {
		this.updateTheStatus = updateTheStatus;
	}
	@Resource(name="adminService")
	private AdminService adminService;
	
	/**
     * Retrieves the add page
     * 
     * @return the name of the JSP page
     */
    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String adminPage(Model model) {
    	numberOfUsers = adminService.getUserCount();
    	model.addAttribute("numberOfUsers", numberOfUsers);
    	logger.debug("Received request to show admin page");
    	return "admin";
    }
    
    @RequestMapping(value = "/admin/editUserDetails", method = RequestMethod.GET)
    public String searchPerson(@ModelAttribute("searchPerson") Search search,BindingResult result) throws NoSuchAlgorithmException {
		logger.debug("Received request to search a person");
		
    	
		System.out.println(search.getSearchString());
		
		Person searchResult = adminService.getSearchResult(search.getSearchString());
		username = searchResult.getUsername();
		DOB = searchResult.getDOB();
		currentStatus = searchResult.getStatus();		
		updateTheStatus = null;
		
		return "redirect:/editUserDetails";
	}
    @RequestMapping(value = "/editUserDetails", method = RequestMethod.GET)
    public String editUserDetailsPage(Model model) {
    	statusList.add(Status.ACTIVE);
    	statusList.add(Status.BLOCK);
    	model.addAttribute("username", username);
    	model.addAttribute("DOB", DOB);
    	model.addAttribute("status",currentStatus);
    	model.addAttribute("statusList", statusList);
    	if (updateTheStatus != null) {
    		model.addAttribute("message","Update Successful");
    	} else if (username == null) {
    		model.addAttribute("message","No Records Found");
    	}
    	model.addAttribute("updatePerson", new UpdatePerson());
    	logger.debug("Received request to show editUserDetails page");
    	return "editUserDetails";
    }
    @RequestMapping(value = "/editUserDetails/updatePerson", method = RequestMethod.GET)
    public String updatePerson(@RequestParam String updateStatus, @ModelAttribute("updatePerson") UpdatePerson update,BindingResult result) throws NoSuchAlgorithmException {
		logger.debug("Received request to search a person");   	
		System.out.println(update.getUpdateStatus());		
		username = update.getUserName();
		if (updateStatus.equalsIgnoreCase("ACTIVE")) {
			updateTheStatus = Status.ACTIVE;
		} else if (updateStatus.equalsIgnoreCase("BLOCK")) {
			updateTheStatus = Status.BLOCK;
		}	
		adminService.updatePerson(updateTheStatus, username);
		return "redirect:/editUserDetails";
	}
   /* @RequestMapping(value = "/updatePerson", method = RequestMethod.GET)
    public String updatePersonFlow(Model model) {
    	model.addAttribute("message","Update Successful");
    	logger.debug("Received request to show admin page");
    	return "redirect:/editUserDetails";
    }*/
}