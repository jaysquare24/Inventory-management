package com.inventory.ManagementSystem.service;

import com.inventory.ManagementSystem.dto.RegistrationRequest;
import com.inventory.ManagementSystem.dto.UserResponse;
import com.inventory.ManagementSystem.exception.UserNotFoundException;
import com.inventory.ManagementSystem.model.Staff;
import com.inventory.ManagementSystem.model.Supplier;
import com.inventory.ManagementSystem.repository.SupplierRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierService {

    @Autowired
    private SupplierRepo supplierRepo;

    public UserResponse createSupplier(RegistrationRequest request){

        if (supplierRepo.existsByEmail(request.getEmail())) {
            throw  new RuntimeException("An Existing User possess this email") ;
        }

        //Request Mapper
        Supplier user = new Supplier();

        user.setEmail(request.getEmail());
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setAddress(request.getAddress());
        user.setCompanyName(request.getCompanyName());


        Supplier savedSupplier = supplierRepo.save(user);

        //Response Mapper
        UserResponse response = new UserResponse();

        response.setId(savedSupplier.getId());
        response.setEmail(savedSupplier.getEmail());
        response.setFirstname(savedSupplier.getFirstname());
        response.setLastname(savedSupplier.getLastname());
        response.setAddress(savedSupplier.getAddress());
        response.setCompanyName(savedSupplier.getCompanyName());

        return  response;

    }

    public List<UserResponse> getAllSuppliers(){


        List<Supplier> suppliers = supplierRepo.findAll();

        return suppliers.stream().map(supplier ->{
                    UserResponse response = new UserResponse();
                    response.setId(supplier.getId());
                    response.setEmail(supplier.getEmail());
                    response.setFirstname(supplier.getFirstname());
                    response.setLastname(supplier.getLastname());
                    response.setAddress(supplier.getAddress());
                    response.setCompanyName(supplier.getCompanyName());

                    return response;
                }

        ).toList();

    }



    public UserResponse getSupplier(int id){

        if (!supplierRepo.existsById(id)){
            throw new UserNotFoundException("Supplier does not exists");
        }

        Supplier supplier =  supplierRepo.findById(id).get();

        //Response Mapper

        UserResponse response = new UserResponse();

        response.setId(supplier.getId());
        response.setEmail(supplier.getEmail());
        response.setFirstname(supplier.getFirstname());
        response.setLastname(supplier.getLastname());
        response.setCompanyName(supplier.getCompanyName());
        response.setAddress(supplier.getAddress());


        return  response;
    }


    public  void deleteSupplier(int id) {
        if  (!supplierRepo.existsById(id) ){
            throw new RuntimeException ("Supplier does not exist");

        }


        supplierRepo.deleteById(id) ;
    }

    public UserResponse updateSupplier(int id, RegistrationRequest supplier) {


        Supplier existingSupplier = supplierRepo.findById(id).orElseThrow(() -> new RuntimeException("Supplier not found"));

        if(supplier.getFirstname() != null && !supplier.getFirstname().isBlank()) {
            existingSupplier.setFirstname(supplier.getFirstname());
        }

        if(supplier.getLastname() != null && !supplier.getLastname().isBlank()) {
            existingSupplier.setLastname(supplier.getLastname());
        }

        if(supplier.getEmail() != null && !supplier.getEmail().isBlank()) {
            existingSupplier.setEmail(supplier.getEmail());
        }

        if(supplier.getAddress() != null && !supplier.getAddress().isBlank()) {
            existingSupplier.setAddress(supplier.getAddress());
        }

        if(supplier.getCompanyName() != null && !supplier.getCompanyName().isBlank()) {
            existingSupplier.setCompanyName(supplier.getCompanyName());
        }


        Supplier savedSupplier =  supplierRepo.save(existingSupplier);

        //Response Mapper
        UserResponse response = new UserResponse();

        response.setId(savedSupplier.getId());
        response.setEmail(savedSupplier.getEmail());
        response.setFirstname(savedSupplier.getFirstname());
        response.setLastname(savedSupplier.getLastname());
        response.setAddress(savedSupplier.getAddress());
        response.setCompanyName(savedSupplier.getCompanyName());

        return  response;

    }
}
