package com.inventory.ManagementSystem.service;

import com.inventory.ManagementSystem.dto.ChangePasswordRequest;
import com.inventory.ManagementSystem.dto.EmailDetails;
import com.inventory.ManagementSystem.exception.*;
import com.inventory.ManagementSystem.model.PasswordResetOtp;
import com.inventory.ManagementSystem.repository.PasswordResetOtpRepo;
import com.inventory.ManagementSystem.security.JwtService;
import com.inventory.ManagementSystem.dto.AuthResponse;
import com.inventory.ManagementSystem.model.Staff;
import com.inventory.ManagementSystem.repository.StaffRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AuthService {
    @Autowired
    private StaffRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private PasswordResetOtpRepo otpRepo;
    @Autowired
    private EmailService emailService;



    private final AuthenticationManager authenticationManager;

    public AuthService(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public Staff register(Staff user) {

        if(userRepo.existsByEmail(user.getEmail())){
            throw new EmailAlreadyExistsException("Email Already Exist");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return  userRepo.save(user);
    }


    public AuthResponse login(String email, String password) {

        try {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            email,
                            password
                    )
            );

            Staff user= userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException(
                            "Something went wrong"
                    )
            );
            String token =  jwtService.generateToken(user);

            return new AuthResponse(token);

        } catch (Exception e) {


            e.printStackTrace();

            throw e;
        }
    }
    @Transactional
    public void forgetPassword(String email) {

        Staff user = userRepo.findByEmail(email)
                .orElseThrow(
                        () -> new UserNotFoundException(
                                "User not found"
                        )
                );

        otpRepo.deleteByEmail(email);

        String otp = jwtService.generateOTP();

        PasswordResetOtp resetOtp = new PasswordResetOtp();
        resetOtp.setEmail(email);
        resetOtp.setOtp(otp);
        resetOtp.setExpiresAt(LocalDateTime.now().plusMinutes(10));

        otpRepo.save(resetOtp);

        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setSubject("OTP For Password Reset");
        emailDetails.setRecipient(email);
        emailDetails.setMessageBody("OTP: " + otp + '\n'
                                    +"OTP expires in 5 mininute" );

        emailService.sendMail(emailDetails);

    }

    public AuthResponse verifyOtp(String otp, String email) {

            PasswordResetOtp otpRecord =

                    otpRepo.findByEmail(email)
                            .orElseThrow(
                                    () -> new RuntimeException(
                                            "OTP not found"
                                    )
                            );

            if (!otpRecord.getOtp().equals(otp)) {
                throw new RuntimeException(
                        "Invalid OTP"
                );
            }

            if (otpRecord.getExpiresAt()
                    .isBefore(LocalDateTime.now())) {

                throw new RuntimeException(
                        "OTP expired"
                );
            }

            otpRepo.delete(otpRecord);

            String token =  jwtService.generateResetPasswordToken(email);

            return new AuthResponse(token);

    }

    public void resetPassword(String token, String newPassword) {

        if(!jwtService.isResetPasswordToken(token)){
            throw new InvalidOtpException(
                    "Invalid token"
            );
        }

        if(jwtService.isTokenExpired(token)){
            throw new OtpExpiredException(
                    "Reset token expired"
            );
        }

        String email = jwtService.extractUsername(token);

        Staff user = userRepo.findByEmail(email).orElseThrow(()-> new UserNotFoundException(
                "User not found"
        ));


        user.setPassword(
                passwordEncoder.encode(newPassword)
        );



        userRepo.save(user);

    }

    public void changePassword(String email, ChangePasswordRequest request) {
        Staff user = userRepo.findByEmail(email).orElseThrow(()-> new UserNotFoundException(
                "User not found"
        ));


        if(!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())){
            throw new IncorrectPasswordException("Current password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        user.setTokenVersion(
                user.getTokenVersion() + 1
        );

        userRepo.save(user);
    }
}
