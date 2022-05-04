package com.writemaster.platform.service;

import com.writemaster.platform.dto.AuthorDTO;
import com.writemaster.platform.payload.UpdateAvatarPayload;
import com.writemaster.platform.payload.UpdateBioPayload;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AuthorService {
  List<AuthorDTO> getAllAuthors();

  AuthorDTO findAuthorByUsername(String username);

  boolean checkExistenceByUsername(String username);

  AuthorDTO findAuthorById(int id);

  void createAuthor(AuthorDTO authorDTO);

  AuthorDTO updateAvatar(UpdateAvatarPayload updateAvatarPayload);

  AuthorDTO deleteAvatar(UpdateAvatarPayload updateAvatarPayload) throws Exception;

  AuthorDTO updateBio(UpdateBioPayload updateBioPayload);
}
