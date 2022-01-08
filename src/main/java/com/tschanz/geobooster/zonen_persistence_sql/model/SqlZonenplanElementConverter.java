package com.tschanz.geobooster.zonen_persistence_sql.model;

import com.tschanz.geobooster.persistence_sql.model.SqlResultsetConverter;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlElementConverter;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdConverter;
import com.tschanz.geobooster.zonen.model.Zonenplan;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;


@RequiredArgsConstructor
public class SqlZonenplanElementConverter implements SqlResultsetConverter<Zonenplan> {
    @Override
    public String getSelectQuery() {
        return String.format(
            "SELECT %s FROM Z_ZONENPLAN_E",
            String.join(",", SqlElementConverter.SELECT_COLS)
        );
    }


    @Override
    @SneakyThrows
    public Zonenplan fromResultSet(ResultSet row) {
        return new Zonenplan(
            SqlHasIdConverter.getId(row)
        );
    }
}
