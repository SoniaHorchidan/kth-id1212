package server.integration;

import server.model.Account;
import server.model.File;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FileCatalogDAO {
    private static final String USERS_TABLE = "User";
    private static final String FILES_TABLE = "File";
    private Connection connection;
    private PreparedStatement createAccountStmt;
    private PreparedStatement findUserStmt;
    private PreparedStatement listAllFilesStmt;
    private PreparedStatement createFileStmt;
    private PreparedStatement updateFileStmt;
    private PreparedStatement deleteFileStmt;

    public FileCatalogDAO() throws FileCatalogDBException {
        try {
            connection = connectToFileCatalogDB();
            prepareStatements();
        } catch (SQLException | ClassNotFoundException ex) {
            throw new FileCatalogDBException("Could not connect to data source", ex);
        }
    }

    private static Connection connectToFileCatalogDB() throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        return DriverManager.getConnection("jdbc:sqlite:db/myDataBase.db");
    }

    public void createAccount(Account account) throws FileCatalogDBException {
        String failureMsg = "Could not create the account. Username is already taken.";
        try {
            createAccountStmt.setString(1, account.getUsername());
            createAccountStmt.setString(2, account.getPassword());
            int rows = createAccountStmt.executeUpdate();
            if (rows != 1) {
                throw new FileCatalogDBException(failureMsg);
            }
        } catch (SQLException ex) {
            throw new FileCatalogDBException(failureMsg, ex);
        }
    }

    public Account login(String username, String password) throws FileCatalogDBException {
        String invalidCredMsg = "Could not login into account. Invalid credentials.";
        String failureMsg = "Could not login. Please try again.";
        Account user = null;
        try {
            user = findAccountByName(username);
            if (user == null || !password.equals(user.getPassword()))
                throw new FileCatalogDBException(invalidCredMsg);
            else
                return user;
        } catch (FileCatalogDBException ex) {
            throw new FileCatalogDBException(failureMsg, ex);
        }

    }


    public Account findAccountByName(String name) throws FileCatalogDBException {
        ResultSet result = null;
        try {
            findUserStmt.setString(1, name);
            result = findUserStmt.executeQuery();
            if (result.next()) {
                int id = result.getInt("id");
                String userName = result.getString("username");
                String password = result.getString("password");
                return new Account(id, userName, password);
            }
        } catch (SQLException ex) {
            throw new FileCatalogDBException("Can't find account by username: " + name, ex);
        } finally {
            try {
                if (result != null) {
                    result.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public List<File> listFiles() throws FileCatalogDBException {
        List<File> listOfFiles = new ArrayList();
        ResultSet result = null;
        try {
            result = listAllFilesStmt.executeQuery();
            while (result.next()) {
                String name = result.getString("name");
                String ownerName = result.getString("owner");
                int size = Integer.parseInt(result.getString("size"));
                int writePermission = Integer.parseInt(result.getString("writePermission"));
                int readPermission = Integer.parseInt(result.getString("readPermission"));
                boolean readPerm = false;
                if (readPermission == 1)
                    readPerm = true;
                boolean writePerm = false;
                if (writePermission == 1)
                    writePerm = true;
                File file = new File(name, ownerName, size, writePerm, readPerm);
                listOfFiles.add(file);
            }
            return listOfFiles;
        } catch (SQLException ex) {
            throw new FileCatalogDBException("Cannot list files", ex);
        } finally {
            try {
                if (result != null) {
                    result.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void createFile(File file) throws FileCatalogDBException {
        try {
            createFileStmt.setString(1, file.getName());
            createFileStmt.setString(2, file.getOwner());
            createFileStmt.setInt(3, file.getSize());
            createFileStmt.setBoolean(4, file.hasWritePermission());
            createFileStmt.setBoolean(5, file.hasReadPermission());
            createFileStmt.executeUpdate();
        } catch (SQLException ex) {
            try {
                createFileStmt.close();
                prepareCreateFileStatement();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            throw new FileCatalogDBException("Could not not create file. ", ex);
        }
    }

    public void updateFile(String fileName, int newSize) throws FileCatalogDBException {
        try {
            updateFileStmt.setInt(1, newSize);
            updateFileStmt.setString(2, fileName);
            updateFileStmt.executeUpdate();
        } catch (SQLException ex) {
            try {
                createFileStmt.close();
                prepareUpdateFileStatement();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            throw new FileCatalogDBException("Could not not update file. ", ex);
        }
    }

    public void deleteFile(String fileName) throws FileCatalogDBException {
        try {
            deleteFileStmt.setString(1, fileName);
            deleteFileStmt.executeUpdate();
        } catch (SQLException ex) {
            throw new FileCatalogDBException("Could not delete the file.", ex);
        }
    }


    private void prepareCreateFileStatement() throws SQLException {
        createFileStmt = connection.prepareStatement("INSERT INTO "
                + FILES_TABLE + " (name, owner, size, writePermission, readPermission)"
                + " VALUES (?, ?, ?, ?, ?)");
    }

    private void prepareUpdateFileStatement() throws SQLException {
        updateFileStmt = connection.prepareStatement("UPDATE "
                + FILES_TABLE + " SET size = ? WHERE name = ?");
    }

    private void prepareStatements() throws SQLException {
        createAccountStmt = connection.prepareStatement("INSERT INTO "
                + USERS_TABLE + " (username, password)" + " VALUES (?, ?)");

        findUserStmt = connection.prepareStatement("SELECT * FROM "
                + USERS_TABLE + " WHERE username = ?");

        listAllFilesStmt = connection.prepareStatement("SELECT * FROM "
                + FILES_TABLE);

        prepareCreateFileStatement();

        prepareUpdateFileStatement();

        deleteFileStmt = connection.prepareStatement("DELETE FROM "
                + FILES_TABLE + " WHERE name = ?");
    }


}
