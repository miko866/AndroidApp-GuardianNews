package com.example.guardinanews;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NewsTest {

    private News SUT;

    @Before
    public void setUp() throws Exception {
        this.SUT = new News("Michal", "10.02.2020", "http:www.google.com", "NoOne", "Fantasy");
    }

    @Test
    public void getNews(){
        assertEquals("Michal", SUT.getTitle());
        assertEquals("10.02.2020", SUT.getDate());
        assertEquals("http:www.google.com", SUT.getUrl());
        assertEquals("NoOne", SUT.getAuthor());
        assertEquals("Fantasy", SUT.getSection());
    }
}