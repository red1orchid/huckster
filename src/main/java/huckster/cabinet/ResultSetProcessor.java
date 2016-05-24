package huckster.cabinet;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by PerevalovaMA on 24.05.2016.
 */
@FunctionalInterface
interface ResultSetProcessor {
    void process(ResultSet resultSet) throws SQLException;
}