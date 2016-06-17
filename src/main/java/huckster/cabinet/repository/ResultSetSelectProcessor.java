package huckster.cabinet.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by PerevalovaMA on 24.05.2016.
 */
@FunctionalInterface
interface ResultSetSelectProcessor<T> {
    T process(ResultSet resultSet) throws SQLException;
}