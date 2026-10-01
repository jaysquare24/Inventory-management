package com.inventory.ManagementSystem.controller;

import com.inventory.ManagementSystem.model.Staff;
//import com.inventory.ManagementSystem.model.Staffs;
import com.inventory.ManagementSystem.service.StaffService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staffs")
@Tag(name = "Staff", description = "APIs for managing staff")
public class StaffController {

    private  final StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Create a new staff",
            description = "Creates a new staff member in the management system"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Staff created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid staff data")
    })
    public Staff newStaff(@RequestBody Staff staff) {
        return staffService.newStaff(staff);
    }


    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Get all staff",
            description = "Returns a list of all staff members"
    )
    @ApiResponse(responseCode = "200", description = "Staff retrieved successfully")
    public List<Staff> allStaff() {
        return staffService.allStaff();
    }


    @GetMapping("/{id}")

    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    @Operation(
            summary = "Get staff by ID",
            description = "Returns a specific staff member using their ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Staff found"),
            @ApiResponse(responseCode = "404", description = "Staff not found")
    })
    public Staff getStaff(
            @Parameter(description = "ID of the staff member", example = "1")
            @PathVariable int id) {

        return staffService.getStaff(id);
    }


    @PutMapping("/{id}")

    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    @Operation(
            summary = "Update staff",
            description = "Updates an existing staff member using their ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Staff updated successfully"),
            @ApiResponse(responseCode = "404", description = "Staff not found")
    })
    public Staff updateStaff(
            @Parameter(description = "ID of the staff member", example = "1")
            @PathVariable int id,
            @RequestBody Staff staff) {

        return staffService.updateStaff(id, staff);
    }


    @GetMapping("/profile/{email}")

    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    @Operation(
            summary = "Get staff profile",
            description = "Returns a staff profile using the staff email"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile found"),
            @ApiResponse(responseCode = "404", description = "Staff profile not found")
    })
    public Staff getProfile(
            @Parameter(
                    description = "Email address of the staff member",
                    example = "staff@example.com"
            )
            @PathVariable String email) {

        return staffService.getProfile(email);
    }


    @DeleteMapping("/{id}")

    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Delete staff",
            description = "Deletes a staff member using their ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Staff deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Staff not found")
    })
    public ResponseEntity<String> deleteStaff(
            @Parameter(description = "ID of the staff member", example = "1")
            @PathVariable int id) {

        staffService.deleteStaff(id);

        return ResponseEntity.ok("Staff Deleted Successfully");
    }
}