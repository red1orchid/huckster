package huckster.cabinet.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by PerevalovaMA on 16.06.2016.
 */
@FunctionalInterface
interface ResultSetExecuteProcessor {
    void process(ResultSet resultSet) throws SQLException;
}