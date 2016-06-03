package com.github.rzo1.crawler;

import com.github.rzo1.db.impl.PostgresDBServiceImpl;
import edu.uci.ics.crawler4j.crawler.CrawlController;

/**
 * Created by rz on 03.06.2016.
 */
public class PostgresCrawlerFactory implements CrawlController.WebCrawlerFactory<PostgresWebCrawler> {

    private final String dbUrl;
    private final String dbUser;
    private final String dbPw;

    public PostgresCrawlerFactory(String dbUrl, String dbUser, String dbPw) {
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPw = dbPw;
    }

    public PostgresWebCrawler newInstance() throws Exception {
        return new PostgresWebCrawler(new PostgresDBServiceImpl(dbUrl,dbUser,dbPw,"org.postgresql.Driver"));
    }
}
