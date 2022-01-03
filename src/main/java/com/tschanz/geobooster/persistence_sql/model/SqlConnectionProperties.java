package com.tschanz.geobooster.persistence_sql.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@EqualsAndHashCode
public class SqlConnectionProperties {
    private String title;
    private String url;
    private String username;
    private String password;
    private String schema;
    private SqlDialect sqldialect;
}
