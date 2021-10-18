package com.tfsc.ilabs.selfservice.common.models;

public enum PublishType {
    DEFAULT, // Publish first to test, then to live
    TEST_ONLY,
    LIVE_ONLY
}
