package com.github.rzo1.db.impl;

import com.github.rzo1.db.PostgresDBService;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import org.slf4j.Logger;

import java.beans.PropertyVetoException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class PostgresDBServiceImpl implements PostgresDBService {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(PostgresDBServiceImpl.class);

    private ComboPooledDataSource comboPooledDataSource;

    private PreparedStatement insertKeyStatement;

    public PostgresDBServiceImpl(String dbUrl, String dbUser, String dbPw, String driver) throws
            PropertyVetoException, SQLException {
        comboPooledDataSource = new ComboPooledDataSource();
        comboPooledDataSource.setDriverClass(driver);
        comboPooledDataSource.setJdbcUrl(dbUrl);
        comboPooledDataSource.setUser(dbUser);
        comboPooledDataSource.setPassword(dbPw);

        createDatabase();
    }

    private void createDatabase() throws SQLException {

        ///XXX should be done via DDL script
        comboPooledDataSource.getConnection().createStatement().executeUpdate(
                "CREATE SEQUENCE id_master_seq" +
                        "  INCREMENT 1" +
                        "  MINVALUE 1 " +
                        "  MAXVALUE 9223372036854775807" +
                        "  START 6 " +
                        "  CACHE 1;")
        ;
        comboPooledDataSource.getConnection().createStatement().executeUpdate(
                "CREATE TABLE webpage" +
                        " ( " +
                        "  id bigint NOT NULL," +
                        "  html TEXT," +
                        "  text TEXT," +
                        "  url varchar(4096)," +
                        "  seen timestamp without time zone NOT NULL," +
                        "  primary key (id)" +
                        ")");

        insertKeyStatement = comboPooledDataSource.getConnection().prepareStatement("insert into webpage values " +
                "(nextval('id_master_seq'),?,?,?,?)");

    }

    @Override
    public void store(Page page) {

        if (page.getParseData() instanceof HtmlParseData) {
            try {

                HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();

                insertKeyStatement.setString(1, htmlParseData.getHtml());
                insertKeyStatement.setString(2, htmlParseData.getText());
                insertKeyStatement.setString(3, page.getWebURL().getURL());
                insertKeyStatement.setTimestamp(4, new Timestamp(new java.util.Date().getTime()));
                insertKeyStatement.executeUpdate();
            } catch (SQLException e) {
                logger.error("SQL Exception while storing webpage for url'{}'", page.getWebURL().getURL(), e);
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void close() {
        if (comboPooledDataSource != null) {
            comboPooledDataSource.close();
        }
    }
}
