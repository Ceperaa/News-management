package ru.clevertec.newsManagement.cache;

import lombok.*;

@ToString
public class CacheObject<K, V> {

    @Getter
    @Setter
    private V obj;

    @Getter
    @Setter
    private K key;

    @Getter
    private int clickSize;

    public void addClickSize() {
        clickSize++;
    }

}
