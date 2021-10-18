package com.tfsc.ilabs.selfservice.testcontainer;

import com.tfsc.ilabs.selfservice.workflow.models.Menu;
import com.tfsc.ilabs.selfservice.workflow.models.Node;

public class MenuContainer {
    public static Menu getMenu(int id, Node nodeParent) {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setMenuGroupName("nav");
        menu.setSeq(1);
        menu.setNodeParentChild(nodeParent);
        return menu;
    }
}
