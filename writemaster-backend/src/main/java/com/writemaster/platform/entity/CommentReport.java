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
@Table(name = "comment_reports")
public class CommentReport {
  @EmbeddedId
  private CommentPKReport commentPKReport;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "reporter_id", referencedColumnName = "id", insertable = false, updatable = false)
  @JsonBackReference
  private Author reporter;

  @ManyToOne
  @JoinColumn(name = "comment_id", referencedColumnName = "id", insertable = false, updatable = false)
  @JsonBackReference
  private Comment comment;

  @Column(name = "reason")
  private String reason;

  @Column(name = "note", columnDefinition = "longtext")
  private String note;

  @Column(name = "state_of_reviewal")
  private String stateOfReviewal;

  @Column(name = "reviewed_on")
  private Date reviewedOn;
}
