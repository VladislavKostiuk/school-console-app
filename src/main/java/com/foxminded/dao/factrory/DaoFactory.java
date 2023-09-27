package com.foxminded.dao.factrory;

import com.foxminded.constants.DbProperties;
import com.foxminded.constants.ErrorMessages;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DaoFactory {

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(
                    DbProperties.URL, DbProperties.USER, DbProperties.PASSWORD
            );
        } catch (SQLException e) {
            throw new RuntimeException(ErrorMessages.INCORRECT_DB_PROPERTIES);
        }
    }

}
