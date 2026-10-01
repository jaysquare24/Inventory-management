package com.inventory.ManagementSystem.service;

import com.inventory.ManagementSystem.model.Supplier;
import com.inventory.ManagementSystem.repository.SupplierRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierService {

    @Autowired
    private SupplierRepo supplierRepo;

    public Supplier createSupplier(Supplier supplier){

        if (supplierRepo.existsByEmail(supplier.getEmail())
                ||  supplierRepo.existsByUsername(supplier.getUsername())  )
        {
            throw  new RuntimeException("Email or Username  already exists") ;


        }



        return  supplierRepo.save(supplier);

    }

    public List<Supplier> getAllSuppliers(){


        return  supplierRepo.findAll();

    }



    public Supplier getSupplier(int id){

        if (!supplierRepo.existsById(id)){

            throw new RuntimeException("Supplier does not exists");

        }

        return  supplierRepo.findById(id).get();
    }


    public  void deleteSupplier(int id) {
        if  (!supplierRepo.existsById(id) ){

            throw new RuntimeException ("Supplier does not exist");


        }


        supplierRepo.deleteById(id) ;
    }

    public Supplier updateSupplier(int id, Supplier supplier) {


        Supplier existingSupplier = getSupplier(id);

        existingSupplier.setUsername(supplier.getUsername());
        existingSupplier.setEmail(supplier.getEmail());
        existingSupplier.setAddress(supplier.getAddress());
        existingSupplier.setCompanyName(supplier.getCompanyName());

        return  supplierRepo.save(existingSupplier);


    }
}
