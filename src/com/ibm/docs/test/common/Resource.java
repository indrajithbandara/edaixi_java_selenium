package com.ibm.docs.test.common;

/**
 * Represent a resource
 * 
 * @author liuzhe@cn.ibm.com
 *
 */
public class Resource {
	
	private String value;

	private ResourcePool pool;
	
	private boolean released = false;
	
	protected Resource(ResourcePool pool, String value) {
		this.pool = pool;
		this.value = value;
	}

	public String getValue() {
		if (released)
			throw new IllegalStateException("Resource is released!");
		return value;
	}
	
	public void release() {
		if (!released) {
			pool.release(this);
			released = true;
		}
	}

	@Override
	public String toString() {
		return value;
	}
}
