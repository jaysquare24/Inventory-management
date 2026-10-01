package com.inventory.ManagementSystem.service;

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

    public Staff newStaff(Staff staff) {

        if (staffRepo.existsByEmail(staff.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        return staffRepo.save(staff);
    }

    public List<Staff> allStaff() {
        return staffRepo.findAll();
    }

    public Staff getStaff(int id) {
        return staffRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Staff not found"));
    }

    public Staff updateStaff(int id, Staff staff) {

        Staff existing = staffRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Staff not found"));

        existing.setFirstName(staff.getFirstName());
        existing.setLastName(staff.getLastName());
        existing.setEmail(staff.getEmail());
        existing.setPhoneNumber(staff.getPhoneNumber());
        existing.setPassword(staff.getPassword());


        return staffRepo.save(existing);
    }

    public Staff getProfile(String email) {

        return staffRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Staff not found"));
    }

    public void deleteStaff(int id) {

        Staff existing = staffRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Staff not found"));

        staffRepo.delete(existing);
    }

}
