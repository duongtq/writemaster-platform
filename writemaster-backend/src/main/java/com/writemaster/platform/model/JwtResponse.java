package com.writemaster.platform.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class JwtResponse implements Serializable {
  private static final long serialVersionUID = 1L;
  private int userId;
  private String username;
  private String email;
  private Set<String> authorities;
  private String jwtToken;
  private String avatarUrl;
  private String bio;
}
