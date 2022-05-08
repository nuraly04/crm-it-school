package kg.itschool.dao.impl;

import kg.itschool.dao.GroupDao;
import kg.itschool.model.Group;

import java.sql.*;

public class GroupDaoImpl implements GroupDao {

    public GroupDaoImpl () {

        Connection connection = null;
        Statement statement = null;

        try {
            System.out.println("Connecting database...");
            connection = getConnection();
            System.out.println("Connection succeeded");

            String ddlQuery = "CREATE TABLE IF NOT EXISTS tb_groups(" +
                    "id BIGSERIAL, " +
                    "group_name VARCHAR(50) NOT NULL, " +
                    "group_time DATE NOT NULL, " +
                    "date_created TIMESTAMP NOT NULL DEFAULT NOW()" +
                    "" +
                    "CONSTRAINT pk_group_id PRIMARY KEY (id)" +
                    "CONSTRAINT chk_group_name CHECK (LENGTH(name) > 2));";

            System.out.println("Create statement...");
            statement = connection.createStatement();
            System.out.println("Executing create table statement...");
            statement.execute(ddlQuery);
        } catch (SQLException e) {
            System.out.println("Some error occurred");
            e.printStackTrace();
        } finally {
            close(statement);
            close(connection);
        }
    }

    @Override
    public Group save(Group group) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Group savedGroup = null;

        try {
            System.out.println("Connecting database...");
            connection = getConnection();
            System.out.println("Connection succeeded");

            String createQuery = "INSERT INTO tb_groups(group_name, group_time, date_created), " +
                    "VALUES(?, ?, ?);";

            String readQuery = "SELECT * FROM tb_groups ORDER BY id DESC LIMIT 1";

            preparedStatement = connection.prepareStatement(createQuery);
            preparedStatement.setString(1, group.getGroupName());
            preparedStatement.set(2, Timestamp.valueOf(group.getGroupTime()));
            preparedStatement.setTimestamp(3, Timestamp.valueOf(group.getDateCreated()));

            preparedStatement.execute();
            close(preparedStatement);

            preparedStatement = connection.prepareStatement(readQuery);
            resultSet = preparedStatement.executeQuery();

            resultSet.next();

            savedGroup = new Group();
            savedGroup.setId(resultSet.getLong("id"));
            savedGroup.setGroupName(resultSet.getString("group_name"));
            savedGroup.setGroupTime(resultSet.getDate("group_time"));
            savedGroup.setDateCreated(resultSet.getTimestamp("date_created").toLocalDateTime());

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(resultSet);
            close(preparedStatement);
            close(connection);
        } return savedGroup;
    }

    @Override
    public Group findById(Long id) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Group group = null;

        try {
            connection = getConnection();

            String readQuery = "SELECT * FROM tb_groups WHERE id = ?";

            preparedStatement = connection.prepareStatement(readQuery);
            preparedStatement.setLong(1, id);

            resultSet =preparedStatement.executeQuery();
            resultSet.next();

            group = new Group();
            group.setId(resultSet.getLong("id"));
            group.setGroupName(resultSet.getString("group_name"));
            group.setGroupTime();
            group.setDateCreated(resultSet.getTimestamp("date_created").toLocalDateTime());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(resultSet);
            close(preparedStatement);
            close(connection);
        } return group;
    }

    private void close(AutoCloseable closeable) {
        try {
            System.out.println(closeable.getClass().getSimpleName() + " close...");
            closeable.close();
            System.out.println(closeable.getClass().getSimpleName() + " closed");
        } catch (Exception e) {
            System.out.println("Could not close " + closeable.getClass().getSimpleName());
            e.printStackTrace();
        }
    }

}
