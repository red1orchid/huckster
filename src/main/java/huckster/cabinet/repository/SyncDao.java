package huckster.cabinet.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by PerevalovaMA on 11.11.2016.
 */
public class SyncDao {
    private static final Logger LOG = LoggerFactory.getLogger(SyncDao.class);
    private static Set<Integer> activeStates;

    static {
        init();
    }

    public static void init() {
        SettingsDao dao = new SettingsDao();
        try {
            activeStates = dao.getSynchronizingCompanies();
        } catch (SQLException e) {
            activeStates = new HashSet<>();
            LOG.error("Error while loading sync states for companies", e);
        }
    }

    public static boolean getSyncState(int companyId) {
        return activeStates.contains(companyId);
    }

    public static void setSyncState(int companyId, boolean state) {
        if (state) {
            activeStates.add(companyId);
        } else {
            activeStates.remove(companyId);
        }
    }
}
