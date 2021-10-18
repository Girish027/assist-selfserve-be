package com.tfsc.ilabs.selfservice.testutils;

import com.tfsc.ilabs.selfservice.common.models.NameLabel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by prasada on 19-09-2019.
 */

public class TestConstants {
    public static final String CLIENT_ID = "test_client";
    public static final String ACCOUNT_ID = "test_account";
    public static final String WORKFLOW_ID = "test_workflow";
    public static final String PAGE_TEMPLATE_ID = "test_pagetemplate";
    public static final String PAGE_ID = "test_page";
    public static final String STATUS = "test_status";
    public static final String ID = "test_id";
    public static final String TYPE = "CREATE";
    public static final String TOKEN = "test-token";
    public static final int WORKFLOW_INSTANCE_ID = 1;
    public static final List<NameLabel> ENTITIES =
            Stream.of(
            new NameLabel("test_entity1", "test_label1"),
            new NameLabel("test_entity2", "test_label2"),
            new NameLabel("test_entity3", "test_label3"))
            .collect(Collectors.toCollection(ArrayList::new));
    //public static final List<NameLabel> ENTITIES = new ArrayList<>( List.of(new NameLabel("test_entity1", "test_label1"), new NameLabel("test_entity2", "test_label2"), new NameLabel("test_entity3", "test_label3")));
    public static final String MENU_GROUP_NAME = "test_client";
}
