package com.inventory.ManagementSystem.service;

import com.inventory.ManagementSystem.dto.UserResponse;
import com.inventory.ManagementSystem.model.Staff;
//import com.inventory.ManagementSystem.model.Staffs;
import com.inventory.ManagementSystem.repository.StaffRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffService {

    @Autowired
    private StaffRepo staffRepo;

   /* public Staff newStaff(RegistrationRequest request) {

        if (staffRepo.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }



        return staffRepo.save(staff);
    }*/

    public List<UserResponse> allStaff() {

        List<Staff> staffs = staffRepo.findAll();

        return staffs.stream().map(staff ->{
            UserResponse response = new UserResponse();
             response.setId(staff.getId());
             response.setEmail(staff.getEmail());
             response.setFirstname(staff.getFirstname());
             response.setLastname(staff.getLastname());
             response.setRoles(staff.getRoles());

             return response;
            }

        ).toList();

    }

    public UserResponse getStaff(int id) {
        Staff staff =  staffRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Staff not found"));


        //Response Mapper

        UserResponse response = new UserResponse();

        response.setId(staff.getId());
        response.setEmail(staff.getEmail());
        response.setFirstname(staff.getFirstname());
        response.setLastname(staff.getLastname());


        return  response;
    }


    public UserResponse updateStaff(int id, Staff staff) {

        Staff existing = staffRepo.findById(id).orElseThrow(() -> new RuntimeException("Staff not found"));

        if (staff.getEmail() != null && !staff.getEmail().isBlank()) {
            existing.setEmail(staff.getEmail());
        }

        if (staff.getFirstname() != null && !staff.getFirstname().isBlank()) {
            existing.setFirstname(staff.getFirstname());
        }

        if (staff.getLastname() != null && !staff.getLastname().isBlank()) {
            existing.setLastname(staff.getLastname());
        }

        if (staff.getPhoneNumber() != null && !staff.getPhoneNumber().isBlank()) {
            existing.setPhoneNumber(staff.getPhoneNumber());
        }

        if (staff.getUsername() != null && !staff.getUsername().isBlank()) {
            existing.setUsername(staff.getUsername());
        }

        Staff staffSaved =  staffRepo.save(existing);

        UserResponse response = new UserResponse();
        response.setId(staffSaved.getId());
        response.setEmail(staffSaved.getEmail());
        response.setFirstname(staffSaved.getFirstname());
        response.setLastname(staffSaved.getLastname());
        response.setUsername(staffSaved.getUsername());
        response.setRoles(staffSaved.getRoles());

        return response;
    }


    public UserResponse getProfile(String email) {

        Staff staff = staffRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("Staff not found"));

        System.out.println(staff.getFirstname());
        UserResponse response = new UserResponse();

        response.setId(staff.getId());
        response.setEmail(staff.getEmail());
        response.setFirstname(staff.getFirstname());
        response.setLastname(staff.getLastname());
        response.setUsername(staff.getUsername());
        response.setRoles(staff.getRoles());

        return response;
    }

    public void deleteStaff(int id) {

        Staff existing = staffRepo.findById(id).orElseThrow(() -> new RuntimeException("Staff not found"));

        staffRepo.delete(existing);
    }

}
