package com.inventory.ManagementSystem.controller;

import com.inventory.ManagementSystem.dto.*;
import com.inventory.ManagementSystem.model.Staff;
import com.inventory.ManagementSystem.service.AuthService;
import com.inventory.ManagementSystem.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/")
@Tag(name = "Authentication", description = "APIs for user registration, login, password management and logout")
public class AuthController {

   @Autowired
   private AuthService authService;

   @Autowired
   private EmailService emailService;


   @PostMapping("auth/register")
   @Operation(
           summary = "Register a new user",
           description = "Registers a new staff user in the system"
   )
   @ApiResponses({
           @ApiResponse(responseCode = "200", description = "User registered successfully"),
           @ApiResponse(responseCode = "400", description = "Invalid registration details")
   })
   public Staff register(@RequestBody Staff user){
      return authService.register(user);
   }

   @PostMapping("auth/login")
   @Operation(
           summary = "Login user",
           description = "Authenticates a user and returns an authentication response"
   )
   @ApiResponses({
           @ApiResponse(responseCode = "200", description = "Login successful"),
           @ApiResponse(responseCode = "401", description = "Invalid email or password")
   })
   public ResponseEntity<CustomApiResponse<AuthResponse>> login(@RequestBody LoginRequest request) {

      AuthResponse authResponse = authService.login(request.getEmail(), request.getPassword());

      return ResponseEntity.ok(
              new CustomApiResponse<>(
                      true,
                      "Login successful",
                      authResponse
              )
      );
   }

   @PostMapping("auth/forget-password")
   @Operation(
           summary = "Forgot password",
           description = "Sends a password reset OTP to the user's email"
   )
   @ApiResponses({
           @ApiResponse(responseCode = "200", description = "OTP sent successfully"),
           @ApiResponse(responseCode = "400", description = "Invalid request")
   })
   public ResponseEntity<CustomApiResponse<Void>> forgetPassword(@RequestBody ForgetPasswordRequest request) {

      authService.forgetPassword(request.getEmail());

      return ResponseEntity.ok(
              new CustomApiResponse<>(
                      true,
                      "OTP sent successfully",
                      null
              )
      );
   }

   @PostMapping("auth/verify-otp")
   @Operation(
           summary = "Verify password reset OTP",
           description = "Verifies the OTP sent to the user's email"
   )
   @ApiResponses({
           @ApiResponse(responseCode = "200", description = "OTP verification successful"),
           @ApiResponse(responseCode = "400", description = "Invalid or expired OTP")
   })
   public ResponseEntity<CustomApiResponse<AuthResponse>> verifyOtp(@RequestBody VerifyOtpRequest otpDetails){
      AuthResponse authResponse = authService.verifyOtp(otpDetails.getOtp(), otpDetails.getEmail());
      return ResponseEntity.ok(
              new CustomApiResponse<>(
                      true,
                      "OTP verification successfully",
                      authResponse
              )
      );
   }

   @PostMapping("auth/reset-password")
   @Operation(
           summary = "Reset password",
           description = "Resets the user's password using the reset token"
   )
   @ApiResponses({
           @ApiResponse(responseCode = "200", description = "Password reset successfully"),
           @ApiResponse(responseCode = "400", description = "Invalid or expired reset token")
   })
   public ResponseEntity<CustomApiResponse<Void>> resetPassword(@RequestBody ResetPasswordRequest request) {

      authService.resetPassword(request.getToken(), request.getNewPassword());

      return ResponseEntity.ok(
              new CustomApiResponse<>(
                      true,
                      "Password reset successfully",
                      null
              )
      );
   }

   @PostMapping("auth/change-password")
   @Operation(
           summary = "Change password",
           description = "Changes the password of the currently authenticated user"
   )
   @ApiResponses({
           @ApiResponse(responseCode = "200", description = "Password changed successfully"),
           @ApiResponse(responseCode = "401", description = "User is not authenticated")
   })
   public ResponseEntity<CustomApiResponse<Void>> changePassword(@AuthenticationPrincipal UserDetails userDetails, @RequestBody ChangePasswordRequest request){
      authService.changePassword(userDetails.getUsername(), request);

      return ResponseEntity.ok(
              new CustomApiResponse<>(
                      true,
                      "Password changed successfully",
                      null
              )
      );
   }

   @PostMapping("logout")
   @Operation(
           summary = "Logout user",
           description = "Logs the user out of the system"
   )
   @ApiResponses({
           @ApiResponse(responseCode = "200", description = "Logged out successfully")
   })
   public String logout() {

      return "Logged out successfully";
   }



}