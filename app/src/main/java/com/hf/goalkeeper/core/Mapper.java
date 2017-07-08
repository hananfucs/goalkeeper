package com.hf.goalkeeper.core;

import java.util.HashMap;
import java.util.Objects;

/**
 * Created by hanan on 05/02/17.
 */

public class Mapper {
    private HashMap<Class, Object> mMap = new HashMap<>();

    public void setValueForKey(Class key, Object value) {
        mMap.put(key, value);
    }

    public Object getValueForKey(Class key) {
        return mMap.get(key);
    }
}
