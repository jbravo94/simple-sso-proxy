package dev.heinzl.simplessoproxy.utils;

public interface Validator<T> {
    boolean isValid(T entity);
}
