package com.writemaster.platform.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class CloudinaryConfig {
  @Value("${cloudinary.apiKey}")
  private String CLOUDINARY_API_KEY;

  @Value("${cloudinary.apiSecret}")
  private String CLOUDINARY_API_SECRET;

  @Value("${cloudinary.cloudName}")
  private String CLOUDINARY_NAME;

  @Bean
  public Cloudinary getCloudinary() {
    Cloudinary cloudinary = new Cloudinary();
    cloudinary.config.apiKey = CLOUDINARY_API_KEY;
    cloudinary.config.apiSecret = CLOUDINARY_API_SECRET;
    cloudinary.config.cloudName = CLOUDINARY_NAME;
    return cloudinary;
  }
}
