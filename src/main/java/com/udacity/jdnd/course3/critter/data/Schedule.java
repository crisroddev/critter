package com.udacity.jdnd.course3.critter.data;

import com.udacity.jdnd.course3.critter.user.EmployeeSkill;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Table(name = "schedule")
@Entity
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToMany(targetEntity = Employee.class)
    private List<Employee> employee;

    @ManyToMany(targetEntity = Pet.class)
    private List<Pet> pets;

    private LocalDate date;

    @ElementCollection
    private Set<EmployeeSkill> employeeSkill;

    public Schedule(){}

    public Schedule(Long id, List<Employee> employee, List<Pet> pets, LocalDate date, Set<EmployeeSkill> employeeSkill) {
        this.id = id;
        this.employee = employee;
        this.pets = pets;
        this.date = date;
        this.employeeSkill = employeeSkill;
    }

    public Schedule(LocalDate date, Set<EmployeeSkill> activities) {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Employee> getEmployee() {
        return employee;
    }

    public void setEmployee(List<Employee> employee) {
        this.employee = employee;
    }

    public List<Pet> getPets() {
        return pets;
    }

    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Set<EmployeeSkill> getEmployeeSkill() {
        return employeeSkill;
    }

    public void setEmployeeSkill(Set<EmployeeSkill> employeeSkill) {
        this.employeeSkill = employeeSkill;
    }
}
