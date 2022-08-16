package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.data.Employee;
import com.udacity.jdnd.course3.critter.data.Pet;
import com.udacity.jdnd.course3.critter.data.Schedule;
import com.udacity.jdnd.course3.critter.service.ScheduleService;
import com.udacity.jdnd.course3.critter.user.CustomerDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    ScheduleService scheduleService;

    private ScheduleDTO convertScheduleToScheduleDTO(Schedule schedule){
        List<Long> employeesIds = schedule.getEmployee().stream().map(employee -> employee.getId()).collect(Collectors.toList());
        List<Long> petIds = schedule.getPets().stream().map(pet -> pet.getId()).collect(Collectors.toList());
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        BeanUtils.copyProperties(schedule, scheduleDTO);
        scheduleDTO.setId(schedule.getId());
        scheduleDTO.setEmployeeIds(employeesIds);
        scheduleDTO.setPetIds(petIds);
        return  new ScheduleDTO(schedule.getId(), employeesIds, petIds, schedule.getDate(), schedule.getEmployeeSkill());
    }

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule(scheduleDTO.getDate(),scheduleDTO.getActivities());
        ScheduleDTO dtoSchedule;
        try{
            dtoSchedule = convertScheduleToScheduleDTO(scheduleService.saveSchedule(schedule, scheduleDTO.getEmployeeIds(), scheduleDTO.getPetIds()));
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "####ERROR####: Schedule error, schedule was not saved");
        }

        return dtoSchedule;
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        List<Schedule> schedules;
        try{
            schedules = scheduleService.getAllSchedules();
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "####ERROR####: Schedule error, no schedules available");
        }
        return schedules.stream().map(this::convertScheduleToScheduleDTO).collect(Collectors.toList());
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        List<Schedule> schedules;
        try{
            schedules = scheduleService.getPetSchedule(petId);
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "####ERROR####: Schedule error, schedule petId: " + petId + " not found", e);
        }
        return schedules.stream().map(this::convertScheduleToScheduleDTO).collect(Collectors.toList());
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        List<Schedule> schedules;
        try{
            schedules = scheduleService.getEmployeeSchedule(employeeId);
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "####ERROR####: Schedule error, schedule employee id: " + employeeId + " not found", e);
        }
        return schedules.stream().map(this::convertScheduleToScheduleDTO).collect(Collectors.toList());
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        List<Schedule> schedules;
        try{
            schedules = scheduleService.getCustomerSchedule(customerId);
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "####ERROR####: Schedule error, schedule customer id: " + customerId + " not found", e);
        }
        return schedules.stream().map(this::convertScheduleToScheduleDTO).collect(Collectors.toList());
    }

}
