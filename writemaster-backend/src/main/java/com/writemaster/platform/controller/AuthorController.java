package com.writemaster.platform.controller;

import com.writemaster.platform.dto.AuthorDTO;
import com.writemaster.platform.payload.UpdateAvatarPayload;
import com.writemaster.platform.payload.UpdateBioPayload;
import com.writemaster.platform.service.AuthorService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/authors")
public class AuthorController {
  private final AuthorService authorService;

  public AuthorController(AuthorService authorService) {
    this.authorService = authorService;
  }

  @ApiOperation(value = "Create new author")
  @ApiResponses({
          @ApiResponse(code = 201, message = "Author created"),
          @ApiResponse(code = 401, message = "Invalid payload data"),
          @ApiResponse(code = 409, message = "Username have been registered")
  })
  @PostMapping(value = "/register",
          consumes = MediaType.APPLICATION_JSON_VALUE,
          produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<AuthorDTO> createAuthor(@RequestBody AuthorDTO payload) {
    authorService.createAuthor(payload);
    return new ResponseEntity<>(payload, HttpStatus.CREATED);
  }
  
  @ApiOperation(value = "Get all authors")
  @ApiResponse(code = 200, message = "All authors fetched")
  @GetMapping(value = "/")
  public ResponseEntity<List<AuthorDTO>> getAllAuthors() {
  	List<AuthorDTO> authors = authorService.getAllAuthors();
  	return ResponseEntity.ok(authors);
  }
  
  @ApiOperation(value = "Find author by username")
  @ApiResponse(code = 200, message = "Author fetched")
  @GetMapping(value = "/{username}")
  public ResponseEntity<AuthorDTO> getAuthorByUsername(@PathVariable String username) {
  	AuthorDTO author = authorService.findAuthorByUsername(username);
  	return ResponseEntity.ok(author);
  }

  @ApiOperation(value = "Upload user's avatar")
  @ApiResponse(code = 200, message = "Avatar updated")
  @PostMapping(value = "/avatar")
  public ResponseEntity<AuthorDTO> updateAvatar(@RequestBody UpdateAvatarPayload updateAvatarPayload) {
    AuthorDTO author = authorService.updateAvatar(updateAvatarPayload);
    return ResponseEntity.ok(author);
  }

  @ApiOperation(value = "Delete user's avatar")
  @ApiResponse(code = 200, message = "Avatar deleted")
  @DeleteMapping(value = "/avatar")
  public ResponseEntity<AuthorDTO> deleteAvatar(@RequestBody UpdateAvatarPayload updateAvatarPayload) throws Exception {
    AuthorDTO author = authorService.deleteAvatar(updateAvatarPayload);
    return ResponseEntity.ok(author);
  }

  @ApiOperation(value = "Upload user's avatar")
  @ApiResponse(code = 200, message = "Avatar updated")
  @PostMapping(value = "/bio")
  public ResponseEntity<AuthorDTO> updateBio(@RequestBody UpdateBioPayload updateBioPayload) {
    AuthorDTO author = authorService.updateBio(updateBioPayload);
    return ResponseEntity.ok(author);
  }
}
