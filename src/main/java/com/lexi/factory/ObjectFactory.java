package com.lexi.factory;

public interface ObjectFactory<T> {
    T getObject() throws Exception;
}