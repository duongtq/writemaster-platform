package com.writemaster.platform.controller;

import com.writemaster.platform.dto.AuthorDTO;
import com.writemaster.platform.model.JwtRequest;
import com.writemaster.platform.model.JwtResponse;
import com.writemaster.platform.security.JwtTokenUtil;
import com.writemaster.platform.security.JwtUserDetailsServiceImpl;
import com.writemaster.platform.service.AuthorService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1")
public class JwtAuthenticationController {
  private final Logger logger = LoggerFactory.getLogger(JwtAuthenticationController.class);
  private final AuthenticationManager authenticationManager;
  private final JwtTokenUtil jwtTokenUtil;
  private final JwtUserDetailsServiceImpl userDetailsService;
  private final AuthorService authorService;

  public JwtAuthenticationController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, JwtUserDetailsServiceImpl userDetailsService, AuthorService authorService) {
    this.authenticationManager = authenticationManager;
    this.jwtTokenUtil = jwtTokenUtil;
    this.userDetailsService = userDetailsService;
    this.authorService = authorService;
  }

  @ApiOperation(value = "Authenticate user")
  @ApiResponse(code = 200, message = "Token received")
  @PostMapping(value = "/authenticate")
  public ResponseEntity<JwtResponse> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) {
    authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
    UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
    AuthorDTO authorDTO = authorService.findAuthorByUsername(authenticationRequest.getUsername());

    String token = jwtTokenUtil.generateToken(userDetails);
    JwtResponse jwtResponse = new JwtResponse(authorDTO.getId(), authorDTO.getUsername(), authorDTO.getEmail(), authorDTO.getAuthorities(), token, authorDTO.getAvatarUrl(), authorDTO.getBio());
    logger.warn("JWT Response: {}", jwtResponse);
    return ResponseEntity.ok(jwtResponse);
  }

  private void authenticate(String username, String password) throws DisabledException, BadCredentialsException {
    try {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    } catch (DisabledException e) {
      throw new DisabledException("USER_DISABLED", e);
    } catch (BadCredentialsException e) {
      throw new BadCredentialsException("INVALID_CREDENTIALS", e);
    }
  }
}
