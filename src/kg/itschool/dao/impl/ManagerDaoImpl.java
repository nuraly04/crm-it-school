package kg.itschool.dao.impl;

import kg.itschool.dao.ManagerDao;
import kg.itschool.dao.impl.daoutil.Log;
import kg.itschool.model.Manager;

import java.sql.*;

public class ManagerDaoImpl implements ManagerDao {

    public ManagerDaoImpl() {

        Connection connection = null;
        Statement statement = null;

        try {
            Log.info(this.getClass().getSimpleName(), Connection.class.getSimpleName() , "Establishing connection");
            connection = getConnection();
            Log.info(this.getClass().getSimpleName(), connection.getClass().getSimpleName(), "Connection established");

            String ddlQuery = "CREATE TABLE IF NOT EXISTS tb_managers(" +
                    "id BIGSERIAL, " +
                    "first_name VARCHAR(50) NOT NULL, " +
                    "last_name VARCHAR(50) NOT NULL, " +
                    "email VARCHAR(100) NOT NULL UNIQUE, " +
                    "phone_number CHAR(13) NOT NUll, " +
                    "salary MONEY NOT NULL, " +
                    "dob DATE NOT NULL CHECK (dob < NOW()), " +
                    "date_created TIMESTAMP NOT NULL DEFAULT NOW(), " +
                    "" +
                    "CONSTRAINT pk_manager_id PRIMARY KEY (id), " +
                    "CONSTRAINT chk_manager_salary CHECK (salary > MONEY(0)), " +
                    "CONSTRAINT chk_manager_first_name CHECK (LENGTH(first_name)> 2));";

            System.out.println("Creating Statement...");
            statement = connection.createStatement();
            System.out.println("Executing create table statement...");
            statement.execute(ddlQuery);

        } catch (SQLException e) {
            Log.info(this.getClass().getSimpleName(), e.getStackTrace()[0].getClass().getSimpleName(), e.getMessage());
            e.printStackTrace();
        } finally {
            close(statement);
            close(connection);
        }
    }

    @Override
    public Manager save (Manager manager) {

            Connection connection = null;
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;
            Manager savedManager = null;

            try {
                System.out.println("Connecting database...");
                connection = getConnection();
                System.out.println("Connection succeeded");

                String createQuery = "INSERT INTO tb_managers(last_name, first_name, phone_number, email, dob, date_created, salary), " +
                        "VALUES(?, ?, ?, ?, ?, MONEY(?), ?)";

                String readQuery = "SELECT * FROM tb_managers ORDER BY id DESC LIMIT 1";

                preparedStatement = connection.prepareStatement(createQuery);
                preparedStatement.setString(2, manager.getFirstName());
                preparedStatement.setString(1, manager.getLastName());
                preparedStatement.setString(3, manager.getPhoneNumber());
                preparedStatement.setString(4, manager.getEmail());
                preparedStatement.setDate(5, Date.valueOf(manager.getDob()));
                preparedStatement.setString(7, manager.getSalary() + "");
                preparedStatement.setTimestamp(6, Timestamp.valueOf(manager.getDateCreated()));

                preparedStatement.execute();
                close(preparedStatement);

                preparedStatement = connection.prepareStatement(readQuery);
                resultSet = preparedStatement.executeQuery();

                resultSet.next();

                savedManager = new Manager();
                savedManager.setId(resultSet.getLong("id"));
                savedManager.setFirstName(resultSet.getString("first_name"));
                savedManager.setLastName(resultSet.getString("last_name"));
                savedManager.setEmail(resultSet.getString("email"));
                savedManager.setPhoneNumber(resultSet.getString("phone_number"));
                savedManager.setSalary(Double.valueOf(resultSet.getString("salary").replaceAll("[^\\d\\.]+", "")));
                savedManager.setDob(resultSet.getDate("dob").toLocalDate());
                savedManager.setDateCreated(resultSet.getTimestamp("date_created").toLocalDateTime());

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                close(resultSet);
                close(preparedStatement);
                close(connection);
            }
            return savedManager;
        }


    @Override
    public Manager findById (Long id) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Manager manager = null;

        try {
            connection = getConnection();

            String readQuery = "SELECT * FROM tb_managers WHERE id = ?";

            preparedStatement = connection.prepareStatement(readQuery);
            preparedStatement.setLong(1, id);

            resultSet =preparedStatement.executeQuery();
            resultSet.next();

            manager = new Manager();
            manager.setId(resultSet.getLong("id"));
            manager.setFirstName(resultSet.getString("first_name"));
            manager.setLastName(resultSet.getString("last_name"));
            manager.setEmail(resultSet.getString("email"));
            manager.setPhoneNumber(resultSet.getString("phone_number"));
            manager.setSalary(Double.valueOf(resultSet.getString("salary").replaceAll("[^\\d\\.]+", "")));
            manager.setDob(resultSet.getDate("dob").toLocalDate());
            manager.setDateCreated(resultSet.getTimestamp("date_created").toLocalDateTime());

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(resultSet);
            close(preparedStatement);
            close(connection);
        } return  manager;
    }

    private void close(AutoCloseable closeable) {
        try {
            System.out.println(closeable.getClass().getSimpleName() + " closing...");
            closeable.close();
            System.out.println(closeable.getClass().getSimpleName() + " closed");
        } catch (Exception e) {
            System.out.println("Could not close " + closeable.getClass().getSimpleName());
            e.printStackTrace();
        }
    }
}
