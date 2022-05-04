package com.writemaster.platform.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;


@Getter
@Setter
public class AuthorDTO {
  private int id;
  private String username;
  
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String password;
  
  private String firstName;
  
  private String lastName;
  
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String email;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private Date birthDate;
  
  private Date createdDate;
  
  private Set<String> authorities;
  
  private String realName;

  private String avatarUrl;

  private String bio;
}
