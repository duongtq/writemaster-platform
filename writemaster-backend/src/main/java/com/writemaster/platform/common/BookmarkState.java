package com.writemaster.platform.common;

public enum BookmarkState {
  CREATED {
    @Override
    public String toString() {
      return "CREATED";
    }
  },
  DELETED {
    @Override
    public String toString() {
      return "DELETED";
    }
  }
}
