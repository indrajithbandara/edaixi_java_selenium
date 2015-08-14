package com.ibm.docs.test.common;

import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.commons.exec.CommandLine;

/**
 * The class implements a pool to manage a bunch of limited resources. 
 * The requester must call ResourcePool.acquire() to acquire a resource firstly before using the resource.
 * Finally the requester must call Resource.release() to release the resource.
 * If all resources in pool are occupied, the new request will be blocked util some resource is released.
 * 
 * 
 * @author liuzhe@cn.ibm.com
 *
 */
public class ResourcePool {
	private static final HashMap<String, ResourcePool> pools = new HashMap<String, ResourcePool>();
	
	private BlockingQueue<String> free = null;

	public ResourcePool(String[] values) {
		free = new ArrayBlockingQueue<String>(values.length);
		for (String v : values) 
			free.offer(v);	
	}
	
	protected Resource acquire(long timeout) {
		try {
			String v = free.poll(timeout, TimeUnit.MILLISECONDS);
			return v == null ? null : new Resource(this, v);
		} catch (InterruptedException e) {
			
		}
        return null;
    }

	protected void release(Resource resource) {
    	free.offer(resource.getValue());
    }
	
    protected static synchronized ResourcePool getPool(String name) {
    	ResourcePool pool = pools.get(name);
    	if (pool == null) {
    		String values = Config.getInstance().get("pool." + name);
    		if (values == null)
    			throw new IllegalArgumentException(String.format("Could not find pool.%s in configuration!", name));
    		pool = new ResourcePool(parseArgs(values));
    		pools.put(name, pool);
    	}
    	return pool;
    }
    
	public static String[] parseArgs(String args) {
		return args == null ? new String[0] : CommandLine.parse("E " + args).getArguments();
	}

    public static Resource acquire(String name, long timeout) {
    	return getPool(name).acquire(timeout);
    }
    
}
