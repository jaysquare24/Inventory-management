package com.inventory.ManagementSystem.security;

import com.inventory.ManagementSystem.exception.UserNotFoundException;
import com.inventory.ManagementSystem.model.Staff;
import com.inventory.ManagementSystem.repository.StaffRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDataDetailsService implements UserDetailsService {

    @Autowired
    StaffRepo userRepo;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        Staff user = userRepo.findByEmail(email)
                .orElseThrow(
                        () -> new UserNotFoundException(
                                "User not found"
                        )
                );

        /*
        User user = userRepo.findByEmail(email)
            .orElseThrow(() ->
                    new UsernameNotFoundException("User not found"));
         */


        return new UserDataDetails(user);
    }



}
