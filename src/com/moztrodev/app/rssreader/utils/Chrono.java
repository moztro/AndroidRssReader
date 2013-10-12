/**
 * 
 */
package com.moztrodev.app.rssreader.utils;

import android.util.SparseArray;

/**
 * @author DANIEL
 *
 */
public class Chrono {
	SparseArray<Long> hm = new SparseArray<Long>();
	int i = Integer.valueOf(-1);

    public Chrono() {

    }

    public Integer start() {
            hm.put(i, System.currentTimeMillis());
            return i--;
    }
    
    public void start(Integer id) {
            hm.put(id, System.currentTimeMillis());
    }

    public long stop(Integer id) {
            Long l = hm.get(id);
            hm.remove(id);
            return System.currentTimeMillis() - l;
    }

    public long checkpoint(Integer id) {
            Long l = hm.get(id);
            return System.currentTimeMillis() - l;
    }
}
