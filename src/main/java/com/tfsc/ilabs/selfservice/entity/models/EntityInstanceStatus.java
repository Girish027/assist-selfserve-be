package com.tfsc.ilabs.selfservice.entity.models;

public enum EntityInstanceStatus {
    DRAFT,

    TEST_PROMOTION_FAILED,
    PROMOTED_TO_TEST ,

    LIVE_PROMOTION_FAILED,
    PROMOTED_TO_LIVE,

    DISCARDED
}
