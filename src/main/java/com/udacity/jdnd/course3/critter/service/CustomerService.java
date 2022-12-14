package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.data.Customer;
import com.udacity.jdnd.course3.critter.data.Pet;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerService {
    @Autowired
    PetRepository petRepository;

    @Autowired
    CustomerRepository customerRepository;

    public Customer saveCustomer(Customer customer, List<Long> petIds){
        List<Pet> petsOwnByCustomer = new ArrayList<>();
        if(petIds != null && !petIds.isEmpty()){
            petsOwnByCustomer = petIds.stream().map((id) -> petRepository.getOne(id)).collect(Collectors.toList());
        }
        customer.setPets(petsOwnByCustomer);
        return customerRepository.save(customer);
    }

    public Customer getCustomerByPetId(Long petId){
        Customer customer = petRepository.getOne(petId).getCustomer();
        return customer;
    }

    public List<Customer> getAllCustomers(){
        List<Customer> customers = customerRepository.findAll();
        return customers;
    }
}
