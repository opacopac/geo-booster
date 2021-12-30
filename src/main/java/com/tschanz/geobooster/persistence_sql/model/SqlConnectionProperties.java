package com.tschanz.geobooster.persistence_sql.model;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SqlConnectionProperties {
    private String title;
    private String url;
    private String username;
    private String password;
    private String schema;
    private SqlDialect sqldialect;
}
