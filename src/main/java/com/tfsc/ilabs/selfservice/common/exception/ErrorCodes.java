package com.tfsc.ilabs.selfservice.common.exception;

public enum ErrorCodes {
	INVALID_PAGE_TYPE("Invalid page type."),

	WORK_FLOW_TEMPLATE_NOT_FOUND("Workflow not found.");

	private final String description;

	ErrorCodes(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

}
