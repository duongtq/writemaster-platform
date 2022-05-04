package com.writemaster.platform.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "posts")
public class Post {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @NonNull
  @Column(name = "title", length = 50)
  private String title;

  @Column(name = "description")
  private String description;

  @Column(name = "content", columnDefinition = "longtext")
  private String content;

  @Column(name = "created_date")
  private Date createdDate;

  @Column(name = "likes")
  private Integer numberOfLikes;

  @Column(name = "comments")
  private Integer numberOfComments;

  @Column(name = "bookmarks")
  private Integer numberOfBookmarks;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "author_id")
  private Author author;

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JsonManagedReference
  private Set<Like> likes;

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JsonManagedReference
  private Set<Comment> comments;

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JsonManagedReference
  private Set<Bookmark> bookmarks;

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JsonManagedReference
  private Set<PostReport> postReports;
}
