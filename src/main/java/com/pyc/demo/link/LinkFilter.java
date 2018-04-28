package com.pyc.demo.link;

@FunctionalInterface
public interface LinkFilter {
    boolean accept(String url);

}