package com.writemaster.platform.common;

public enum ReportReason {
  SPAM {
    @Override
    public String toString() {
      return "SPAM";
    }
  },
  HATE_SPEECH {
    @Override
    public String toString() {
      return "HATE_SPEECH";
    }
  },
  VIOLENCE {
    @Override
    public String toString() {
      return "VIOLENCE";
    }
  },
  NUDITY {
    @Override
    public String toString() {
      return "NUDITY";
    }
  },
  MISINFORMATION {
    @Override
    public String toString() {
      return "MISINFORMATION";
    }
  }
}
