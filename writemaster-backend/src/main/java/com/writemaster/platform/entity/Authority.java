package com.writemaster.platform.entity;

import javax.persistence.*;

@Entity
@Table(name = "authorities")
public class Authority {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "author_id")
  private Author author;

  @Column(name = "authority")
  private String authority;

  public Authority() {

  }

  public Authority(Author author, String authority) {
    this.author = author;
    this.authority = authority;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Author getAuthor() {
    return author;
  }

  public void setAuthor(Author author) {
    this.author = author;
  }

  public String getAuthority() {
    return authority;
  }

  public void setAuthority(String authority) {
    this.authority = authority;
  }

  @Override
  public String toString() {
    return "Authority{" +
            "id=" + id +
            ", authority='" + authority + '\'' +
            '}';
  }
}
