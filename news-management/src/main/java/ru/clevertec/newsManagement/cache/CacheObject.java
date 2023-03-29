package ru.clevertec.newsManagement.cache;

import lombok.*;

@ToString
public class CacheObject {

    @Getter
    @Setter
    private Object obj;

    @Getter
    @Setter
    private String key;

    @Getter
    private int clickSize;

    public void addClickSize() {
        clickSize++;
    }

}
