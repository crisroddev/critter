package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.data.Customer;
import com.udacity.jdnd.course3.critter.data.Employee;
import com.udacity.jdnd.course3.critter.data.Pet;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Users.
 *
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    CustomerService customerService;

    @Autowired
    EmployeeService employeeService;

    // Convert Entities to DTOs
    public CustomerDTO convertCustomerToCustomerDTO(Customer customer){
        List<Long> petIds = customer.getPets().stream().map(Pet::getId).collect(Collectors.toList());
        return  new CustomerDTO(customer.getId(),customer.getName(), customer.getPhoneNumber(), customer.getNotes(), petIds);
    }

    public EmployeeDTO convertEmployeeToEmployeeDTO(Employee employee){
        return new EmployeeDTO(employee.getId(), employee.getName(), employee.getEmployeeSkills(), employee.getAvailability());
    }

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        Customer customer = new Customer(customerDTO.getId(), customerDTO.getName(), customerDTO.getPhoneNumber(), customerDTO.getNotes());
        List<Long> petIds = customerDTO.getPetIds();
        CustomerDTO dtoCustomer;
        try{
            dtoCustomer = convertCustomerToCustomerDTO(customerService.saveCustomer(customer, petIds));

        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "####ERROR####: Customer not saved", e);
        }
        return dtoCustomer;
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers;
        try{
            customers = customerService.getAllCustomers();
        } catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "####ERROR####: There are no customers", e);
        }
        return customers.stream().map(this::convertCustomerToCustomerDTO).collect(Collectors.toList());
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId){
        Customer customer;
        try{
            customer = customerService.getCustomerByPetId(petId);
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "####ERROR####: Owner retrieving petId" + petId + "does not exists", e);
        }
        return convertCustomerToCustomerDTO(customer);
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee = new Employee(employeeDTO.getId(), employeeDTO.getName(), employeeDTO.getSkills(), employeeDTO.getDaysAvailable());
        EmployeeDTO dtoEmployee;
        try{
            dtoEmployee = convertEmployeeToEmployeeDTO(employeeService.saveEmployee(employee));

        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "####ERROR####: Saving Employee", e);
        }
        return dtoEmployee;
    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        Employee employee;
        try{
            employee = employeeService.getEmployeeById(employeeId);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "####ERROR####: Employee Id: " + employeeId + " does not exists", e);
        }
        return convertEmployeeToEmployeeDTO(employee);
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        try{
            employeeService.setAvailability(daysAvailable, employeeId);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "####ERROR####: Employee Id: " + employeeId + " does not exists", e);
        }
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        List<Employee> employees = employeeService.getEmployeeByService(employeeDTO.getDate(), employeeDTO.getSkills());

        return employees
                .stream()
                .map(this::convertEmployeeToEmployeeDTO)
                .collect(Collectors.toList());
    }
}
