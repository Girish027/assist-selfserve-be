/**
 * 
 */
package com.tfsc.ilabs.selfservice.testcontainer;

import java.util.ArrayList;
import java.util.List;

import com.tfsc.ilabs.selfservice.workflow.models.WorkflowPage;

/**
 * @author subhasish.c
 *
 */
public class WorkflowPageContainer {
	
	 public static WorkflowPage createWorkflowPage() {
		 WorkflowPage workflowPage = new WorkflowPage();
		 workflowPage.setPageOrder(1);
		 workflowPage.setPageTemplate(PageTemplateContainer.getPageTemplateList().get(0));
		 workflowPage.setWorkflowTemplate(WorkflowTemplateContainer.getSingletonWorkflow("name"));
		 return workflowPage;
	 }
	 
	 public static List<WorkflowPage> createWorkflowPageList() {
		 WorkflowPage workflowPage = new WorkflowPage();
		 workflowPage.setPageOrder(1);
		 workflowPage.setPageTemplate(PageTemplateContainer.getPageTemplateList().get(0));
		 workflowPage.setWorkflowTemplate(WorkflowTemplateContainer.getSingletonWorkflow("name"));
		 WorkflowPage workflowPage2 = new WorkflowPage();
		 workflowPage2.setPageOrder(1);
		 workflowPage2.setSectionName("SECTION");
		 workflowPage2.setPageTemplate(PageTemplateContainer.getPageTemplateList().get(1));
		 workflowPage2.setWorkflowTemplate(WorkflowTemplateContainer.getSingletonWorkflow("name"));
		 List<WorkflowPage> workFlowPages = new ArrayList<>();
		 workFlowPages.add(workflowPage);
		 workFlowPages.add(workflowPage2);
		 return workFlowPages;
	 }

}
