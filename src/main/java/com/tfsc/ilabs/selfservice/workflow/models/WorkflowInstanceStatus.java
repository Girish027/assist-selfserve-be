package com.tfsc.ilabs.selfservice.workflow.models;

/**
 * Created by ravi.b on 31-05-2019.
 */
public enum WorkflowInstanceStatus {
    
    DRAFT_EDIT ,
    DRAFT_SAVE ,

    TEST_PROMOTION_FAILED,
    PROMOTING_TO_TEST,
    PROMOTED_TO_TEST ,

    LIVE_PROMOTION_FAILED,
    PROMOTING_TO_LIVE,
    PROMOTED_TO_LIVE,

    DISCARDED
}
