package com.aps.poccargasqlite.config;

import javax.sql.DataSource;
import org.sqlite.SQLiteDataSource;

public class CustomSqliteDatasource {

    public DataSource criarDatasource() {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl("jdbc:sqlite:banco.db");
        return dataSource;
    }

}
