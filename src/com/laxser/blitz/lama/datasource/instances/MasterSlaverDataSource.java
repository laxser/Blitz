package com.laxser.blitz.lama.datasource.instances;


import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.sql.DataSource;


import org.springframework.util.Assert;

import com.laxser.blitz.lama.annotation.UseMaster;
import com.laxser.blitz.lama.core.SQLThreadLocal;
/**
 * 
 * @author laxser
 * @ contact qifan.du@renren-inc.com
 * date: 2012-4-11
 */
public class MasterSlaverDataSource implements DataSource {

    private List<DataSource> masters = Collections.emptyList();

    private List<DataSource> slavers = Collections.emptyList();

    private Random random = new Random();

    public void setMaster(DataSource master) {
        this.masters = new ArrayList<DataSource>();
        this.masters.add(master);
    }

    public void setMasters(List<DataSource> masters) {
        this.masters = new ArrayList<DataSource>(masters);
    }

    public void setSlavers(List<DataSource> slavers) {
        this.slavers = new ArrayList<DataSource>(slavers);
    }

    public Connection getConnection() throws SQLException {
        return getDataSource().getConnection();
    }

    public Connection getConnection(String username, String password) throws SQLException {
        return getDataSource().getConnection(username, password);
    }

    protected DataSource getDataSource() throws SQLException {
        SQLThreadLocal local = SQLThreadLocal.get();
        Assert.notNull(local, "this is jade's bug; class SQLThreadLocalWrapper "
                + "should override all the DataAccess interface methods.");
        boolean write = false;
        if (local.isWriteType()) {
            write = true;
        } else if (local.getModifier().getMethod().isAnnotationPresent(UseMaster.class)) {
            write = true;
        }
        DataSource dataSource;
        if (write) {
            dataSource = randomGet(masters);
        } else {
            dataSource = randomGet(slavers);
        }
        if (dataSource == null) {
            throw new SQLException("could't get " + (write ? "Write" : "Read")
                    + " dataSource for SQL: " + local.getSql());
        }
        return dataSource;
    }

    protected DataSource randomGet(List<DataSource> dataSources) {
        if (dataSources.size() == 0) {
            return null;
        }
        int index = random.nextInt(dataSources.size()); // 0.. size
        return dataSources.get(index);
    }

    //---------------------------------------

    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @SuppressWarnings({ "rawtypes" })
    public boolean isWrapperFor(Class arg0) throws SQLException {
        return false;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Object unwrap(Class arg0) throws SQLException {
        return null;
    }

}
