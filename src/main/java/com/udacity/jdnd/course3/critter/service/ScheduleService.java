package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.data.Customer;
import com.udacity.jdnd.course3.critter.data.Employee;
import com.udacity.jdnd.course3.critter.data.Pet;
import com.udacity.jdnd.course3.critter.data.Schedule;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import com.udacity.jdnd.course3.critter.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;

@Service
@Transactional
public class ScheduleService {
    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    PetRepository petRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    CustomerRepository customerRepository;

    public Schedule saveSchedule(Schedule schedule, List<Long> empoyeeIds, List<Long> petIds){
        List<Pet> pets = petRepository.findAllById(petIds);
        List<Employee> employees = employeeRepository.findAllById(empoyeeIds);

        schedule.setPets(pets);
        schedule.setEmployee(employees);

        if(schedule == null){
            schedule = new Schedule();
        }

        return scheduleRepository.save(schedule);
    }

    public List<Schedule> getAllSchedules(){
        List<Schedule> allSchedules = scheduleRepository.findAll();
        return allSchedules;
    }

    public List<Schedule> getEmployeeSchedule(Long employeeId){
        Employee employee = employeeRepository.getOne(employeeId);
        List<Schedule> schedules = scheduleRepository.findByEmployee(employee);
        return schedules;
    }

    public List<Schedule> getPetSchedule(Long petId){
        Pet pet = petRepository.getOne(petId);
        List<Schedule> schedules = scheduleRepository.findByPets(pet);
        return schedules;
    }

    public List<Schedule> getCustomerSchedule(Long customerId){
        Customer customer = customerRepository.getOne(customerId);
        List<Pet> pets = customer.getPets();
        List<Schedule> schedules = new LinkedList<>();

        pets.forEach(pet -> {
            List<Schedule> petsSchedule = scheduleRepository.findByPets(pet);
            schedules.addAll(petsSchedule);
        });
        return schedules;
    }

}
