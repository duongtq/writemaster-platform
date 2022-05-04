package com.writemaster.platform.common;

public enum StateOfReviewal {
  WAITING_FOR_REVIEWAL {
    @Override
    public String toString() {
      return "WAITING_FOR_REVIEWAL";
    }
  },
  APPROVED {
    @Override
    public String toString() {
      return "APPROVED";
    }
  },
  REJECTED  {
    @Override
    public String toString() {
      return "REJECTED";
    }
  }
}
