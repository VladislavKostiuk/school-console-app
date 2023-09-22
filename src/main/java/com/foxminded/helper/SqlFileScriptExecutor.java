package com.foxminded.helper;

import com.foxminded.constants.ErrorMessages;
import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.util.Optional;

public class SqlFileScriptExecutor {
    private final ScriptRunner scriptRunner;

    public SqlFileScriptExecutor(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException(ErrorMessages.CONNECTION_CANT_BE_NULL);
        }

        scriptRunner = new ScriptRunner(connection);
        scriptRunner.setLogWriter(null);
    }

    public void executeScript(String fileName) {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
        Reader scriptReader = new InputStreamReader(
                Optional.ofNullable(inputStream).orElseThrow(() ->
                        new IllegalArgumentException(String.format(ErrorMessages.FILE_WAS_NOT_FOUND, fileName)))
        );
        scriptRunner.runScript(scriptReader);
    }
}
