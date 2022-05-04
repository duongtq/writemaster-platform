package com.writemaster.platform.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "authors")
public class Author {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @NonNull
  @Column(name = "username", unique = true, nullable = false)
  private String username;

  @NonNull
  @Column(name = "password")
  private String password;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "email", unique = true, nullable = false)
  private String email;

  @Column(name = "birth_date")
  @Temporal(TemporalType.TIMESTAMP)
  private Date birthDate;

  @Column(name = "created_date")
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdDate;

  @Column(name = "avatar_url", columnDefinition = "longtext")
  private String avatarUrl;

  @Column(name = "bio", columnDefinition = "longtext")
  private String bio;

  @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<Post> posts;

  @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JsonManagedReference
  private Set<Authority> authorities;

  @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JsonManagedReference
  private Set<Comment> comments;

  @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JsonManagedReference
  private Set<Like> likes;

  @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JsonManagedReference
  private Set<Bookmark> bookmarks;

  @OneToMany(mappedBy = "reporter", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JsonManagedReference
  private Set<PostReport> postReports;

  @OneToMany(mappedBy = "reporter", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JsonManagedReference
  private Set<CommentReport> commentReports;

  @Override
  public String toString() {
    return "Author [id=" + id + ", username=" + username + ", password=" + password + ", firstName=" + firstName
            + ", lastName=" + lastName + ", email=" + email + ", birthDate=" + birthDate + ", createdDate="
            + createdDate + ", posts=" + posts;
  }

  public String getRealName() {
    return this.firstName + " " + this.lastName;
  }
}
