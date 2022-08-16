package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.data.Pet;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.user.CustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {

    @Autowired
    PetService petService;

    // First Convert to DTO
    private PetDTO convertPetToPetDTO(Pet pet){
        return new PetDTO(pet.getId(), pet.getType(), pet.getName(), pet.getCustomer().getId(), pet.getDate(), pet.getNotes());
    }

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        Pet pet = new Pet(petDTO.getType(), petDTO.getName(), petDTO.getBirthDate(), petDTO.getNotes());
        PetDTO DtoPet;
        try{
            DtoPet = convertPetToPetDTO(petService.savePet(pet, petDTO.getOwnerId()));

        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "####ERROR####: Pet not saved", e);
        }
        return  DtoPet;
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        Pet pet;
        try{
            pet = petService.getPetById(petId);
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "####ERROR####: Pet id " + petId + "was not found " , e);
        }
        return convertPetToPetDTO(pet);
    }

    @GetMapping()
    public List<PetDTO> getPets(){
        List<Pet> pets;
        try{
            pets = petService.getAllPets();
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "####ERROR####: There are no pets", e);
        }
        return pets.stream().map(this::convertPetToPetDTO).collect(Collectors.toList());
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        List<Pet> pets;
        try{
            pets = petService.getPetsByCustomerId(ownerId);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "####ERROR####: Customer has no pets", e);
        }
        return pets.stream().map(this::convertPetToPetDTO).collect(Collectors.toList());
    }
}
