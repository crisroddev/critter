package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.data.Employee;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeService {
    @Autowired
    EmployeeRepository employeeRepository;

    public Employee saveEmployee(Employee employee){
        Employee employee1 = employeeRepository.save(employee);
        return employee1;
    }

    public Employee getEmployeeById(Long employeeId){
        Employee employee = employeeRepository.getOne(employeeId);
        return employee;
    }

    public List<Employee> getEmployeeByService(LocalDate localDate, Set<EmployeeSkill> skills){
        List<Employee> employees =
                employeeRepository
                        .findByAvailability(localDate.getDayOfWeek())
                        .stream()
                        .filter(employee -> employee.getEmployeeSkills().containsAll(skills))
                        .collect(Collectors.toList());
        return employees;
    }

    public void setAvailability(Set<DayOfWeek> days, Long employeeId){
        Employee employee = employeeRepository.getOne(employeeId);
        employee.setAvailability(days);
        employeeRepository.save(employee);
    }
}
