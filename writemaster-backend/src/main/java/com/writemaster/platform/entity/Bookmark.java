package com.writemaster.platform.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bookmarks")
public class Bookmark {
  @Id
  @Column(name = "bookmarked_on")
  private Date bookmarkedOn;

  @ManyToOne
  @JoinColumn(name = "author_id", referencedColumnName = "id")
  @JsonBackReference
  private Author author;

  @ManyToOne
  @JoinColumn(name = "post_id", referencedColumnName = "id")
  @JsonBackReference
  private Post post;

}
