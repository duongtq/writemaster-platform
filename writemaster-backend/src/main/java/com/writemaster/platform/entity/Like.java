package com.writemaster.platform.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "likes")
public class Like {

  @EmbeddedId
  private LikePK likePK;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "author_id", referencedColumnName = "id", insertable = false, updatable = false)
  @JsonBackReference
  private Author author;

  @ManyToOne
  @JoinColumn(name = "post_id", referencedColumnName = "id", insertable = false, updatable = false)
  @JsonBackReference
  private Post post;

  @Column(name = "liked")
  private Boolean liked;
}
