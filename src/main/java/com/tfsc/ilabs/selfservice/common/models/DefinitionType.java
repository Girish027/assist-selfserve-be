package com.tfsc.ilabs.selfservice.common.models;

public enum DefinitionType {
	CREATE, UPDATE, VIEW, BULK;

	public static boolean contains(String s) {
		try {
			DefinitionType.valueOf(s.toUpperCase());
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
