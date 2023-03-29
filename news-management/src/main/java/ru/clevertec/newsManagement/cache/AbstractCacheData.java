package ru.clevertec.newsManagement.cache;

import java.util.Map;

public abstract class AbstractCacheData {


    protected static String getKey(String id, String elementName) {
        return String.format("%s_%s", id, elementName);
    }

    protected boolean isMaxSize(Map cache,int maxSize) {
        return cache.size() >= maxSize;
    }
}
