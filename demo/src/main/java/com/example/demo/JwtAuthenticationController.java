package com.example.demo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rishi.controller.Response;

@RestController
@CrossOrigin
@RequestMapping("/web")
public class JwtAuthenticationController {

	@Autowired
	private ManagerDao managerDao;

	@Autowired
	private EmployeeDao employeeDao;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@RequestMapping("/authenticate")
	public Response authenticate(HttpServletRequest request,
			@NotNull @RequestParam(value = "userId", defaultValue = "") String userId,
			@NotNull @RequestParam(value = "password", defaultValue = "") String password)
			throws UsernameNotFoundException {
		Response response = new Response();
		try {
			Manager manager = managerDao.findByEmailAndPassword(userId, password);
			if (manager == null) {
				response.setStatus("Failure");
				response.setMessage("Email or Password wrong");
				return response;
			} else {
				response.setStatus("Success");
				response.setResponse(manager.getFirstname());
				final String token = jwtTokenUtil.generateToken(manager);
				response.setToken(token);
			}
		} catch (Exception e) {
			response.setStatus("Failure");
			response.setMessage("Server Error ");
		}
		return response;
	}

	@RequestMapping("/getEmployeeData")
	public Response getEmployeeData(@RequestParam(value = "managerId", defaultValue = "") String managerId) {
		Response response = new Response();
		try {
			Manager manager = managerDao.findByEmail(managerId);
			List<Employee> empList = employeeDao.findByManager(manager);
			response.setResponse(empList);
			response.setStatus("Success");
		} catch (Exception e) {
			response.setStatus("Failure");
			response.setMessage("Server Error.");
			return response;
		}
		return response;
	}

	@RequestMapping("/deleteEmployee")
	public Response deleteEmployee(@RequestParam(value = "id", defaultValue = "") long id) {
		Response response = new Response();
		try {
			employeeDao.delete(id);
			response.setStatus("Success");
			response.setMessage("Delete SuccessFully.");
		} catch (Exception e) {
			response.setStatus("Failure");
			response.setMessage("Server Error.");
			return response;
		}
		return response;
	}

	@RequestMapping("/saveEmployeeData")
	public Response saveEmployeeData(HttpServletRequest request,
			@NotNull @RequestParam(value = "managerId", defaultValue = "-1") String managerId,
			@RequestParam(value = "firstname", defaultValue = "-1") String firstname,
			@RequestParam(value = "lastname", defaultValue = "-1") String lastname,
			@RequestParam(value = "city", defaultValue = "-1") String city,
			@RequestParam(value = "mobile", defaultValue = "-1") String mobile,
			@RequestParam(value = "dob", defaultValue = "-1") String dob,
			@RequestParam(value = "emailId", defaultValue = "-1") String emailId,
			@RequestParam(value = "id", defaultValue = "-1") long id) {
		Response response = new Response();
		try {
			Manager manager = managerDao.findByEmail(managerId);
			if (manager == null) {
				response.setStatus("Failure");
				response.setMessage("Please Use Valid Login Id.");
				return response;
			}
			Employee employee = employeeDao.findOne(id);
			if (employee == null) {
				employee = new Employee();
				Employee email = employeeDao.findByEmailId(emailId);
				if (email != null) {
					response.setStatus("Failure");
					response.setMessage("This Email Id is Already Exists.");
					return response;
				}
				employee.setEmailId(emailId);
				employee.setManager(manager);
				employee.setFirstname(firstname);
				employee.setLastname(lastname);
				employee.setCity(city);
				employee.setMobile(mobile);
				Date date = new SimpleDateFormat("dd/MM/yyyy").parse(dob);
				employee.setDob(date);
			} else {
				employee.setManager(manager);
				employee.setFirstname(firstname);
				employee.setLastname(lastname);
				employee.setCity(city);
				employee.setMobile(mobile);
				Date date = new SimpleDateFormat("dd/MM/yyyy").parse(dob);
				employee.setDob(date);
			}
			employeeDao.save(employee);
			response.setStatus("Success");
		} catch (Exception e) {
			response.setStatus("Failure");
			response.setMessage("Server Error");
		}
		return response;
	}

	@RequestMapping("/registerManager")
	public Response registerManager(HttpServletRequest request,
			@RequestParam(value = "emailId", defaultValue = "-1") String emailId,
			@RequestParam(value = "address", defaultValue = "-1") String address,
			@RequestParam(value = "lastname", defaultValue = "-1") String lastname,
			@RequestParam(value = "firstname", defaultValue = "-1") String firstname,
			@RequestParam(value = "password", defaultValue = "-1") String password) {
		Response response = new Response();
		try {
			Manager manager = managerDao.findByEmail(emailId);
			if (manager != null) {
				response.setStatus("Failure");
				response.setMessage("This Email Id is Already Exists.");
				return response;
			}
			manager = new Manager();
			manager.setFirstname(firstname);
			manager.setLastname(lastname);
			manager.setEmail(emailId);
			manager.setAddress(address);
			manager.setPassword(password);
			managerDao.save(manager);
			response.setStatus("Success");
		} catch (Exception e) {
			response.setStatus("Failure");
			response.setMessage("Server Error");
		}
		return response;
	}
}
