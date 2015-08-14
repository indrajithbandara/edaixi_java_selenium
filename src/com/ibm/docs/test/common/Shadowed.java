package com.ibm.docs.test.common;

public interface Shadowed {
	
	public Object getShadow(int i);
	
	public Object getShadowResult(int i);
	
	public int getShadowSize();

	public void setShadowEnabled(boolean enabled);
}
