package com.imdbcrawler;

import java.io.IOException;

import com.imdbcrawler.services.CrawlerService;

public final class Main {    
    public static void main(String[] args) throws IOException {
        CrawlerService crawlerService = new CrawlerService();
        crawlerService.getData();
    }
}