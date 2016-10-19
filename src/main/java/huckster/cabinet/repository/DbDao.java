package huckster.cabinet.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by PerevalovaMA on 04.08.2016.
 */
public class DbDao {
    private static final Logger LOG = LoggerFactory.getLogger(DbDao.class);
    protected DataSource pool;

    public DbDao() {
        try {
            InitialContext ctx = new InitialContext();
            pool = (DataSource) ctx.lookup("java:comp/env/jdbc/huckster");
            if (pool == null) {
                throw new ServletException("Unknown DataSource 'jdbc/huckster'");
            }
        } catch (NamingException | ServletException ex) {
            LOG.error("Failed to create connection pool", ex);
        }
    }

    protected <T> Optional<T> selectValue(String sql,
                                          ResultSetSelectProcessor<T> processor,
                                          Object... params) throws SQLException {
        T value = null;
        try (Connection dbConnection = pool.getConnection();
             PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            int cnt = 0;
            for (Object param : params) {
                ps.setObject(++cnt, param);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    value = processor.process(rs);
                }
            }
        }
        return Optional.ofNullable(value);
    }

    protected void execute(String sql,
                           Integer fetchSize,
                           ResultSetExecuteProcessor processor,
                           Object... params) throws SQLException {
        try (Connection dbConnection = pool.getConnection();
             PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            int cnt = 0;
            for (Object param : params) {
                ps.setObject(++cnt, param);
            }
            if (fetchSize != null) {
                ps.setFetchSize(fetchSize);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    processor.process(rs);
                }
            }
        }
    }

    protected void executeUpdate(String sql, Object... params) throws SQLException {
        try (Connection dbConnection = pool.getConnection();
             PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            int cnt = 0;
            for (Object param : params) {
                ps.setObject(++cnt, param);
            }
            ps.executeUpdate();
        }
    }

    protected List<List> makeTable(String sql, Integer fetchSize, int columns, Object... params) throws SQLException {
        List<List> table = new ArrayList<>();

        execute(sql, fetchSize, (rs) -> {
            List<String> row = new ArrayList<>();
            for (int i = 1; i <= columns; i++) {
                row.add(rs.getString(i));
            }
            table.add(row);
        }, params);

        return table;
    }
}
