package kg.itschool.dao.impl;

import kg.itschool.dao.MentorDao;
import kg.itschool.model.Mentor;

import java.sql.*;

public class MentorDaoImpl implements MentorDao {

    public MentorDaoImpl() {

        Connection connection = null;
        Statement statement = null;

        try {
            System.out.println("Connecting database...");
            connection = getConnection();
            System.out.println("Connection succeeded");

            String ddlQuery = "CREATE TABLE IF NOT EXISTS tb_mentors(" +
                    "id BIGSERIAL, " +
                    "first_name VARCHAR(50) NOT NULL, " +
                    "last_name VARCHAR(50) NOT NULL, " +
                    "phone_number CHAR(13) NOT NULL, " +
                    "email VARCHAR(100) NOT NULL UNIQUE, " +
                    "dob DATE NOT NULL CHECK (dob < NOW()), " +
                    "salary MONEY NOT NULL, " +
                    "date_created TIMESTAMP NOT NULL DEFAULT NOW(), " +
                    "" +
                    "CONSTRAINT pk_mentor_id PRIMARY KEY (id), " +
                    "CONSTRAINT chk_mentor_salary CHECK (salary > MONEY(0)), " +
                    "CONSTRAINT chk_mentor_first_name CHECK (LENGTH(first_name > 2));";

            System.out.println("Creating statement...");
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
    public Mentor save(Mentor mentor) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Mentor savedMentor = null;

                try {
                    System.out.println("Connecting database...");
                    connection = getConnection();
                    System.out.println("Connection succeeded");

                    String createQuery = "INSERT INTO tb_mentors(first_name, last_name, phone_number, email, dob, salary, date_created), " +
                            "VALUES(?, ?, ?, ?, ?, MONEY(?), ?)";

                    String readQuery = "SELECT * FROM tb_mentors ORDER BY id DESC LIMIT 1";

                    preparedStatement = connection.prepareStatement(createQuery);
                    preparedStatement.setString(1, mentor.getFirstName());
                    preparedStatement.setString(2, mentor.getLastName());
                    preparedStatement.setString(3, mentor.getPhoneNumber());
                    preparedStatement.setString(4, mentor.getEmail());
                    preparedStatement.setDate(5, Date.valueOf(mentor.getDob()));
                    preparedStatement.setString(6, mentor.getSalary() + "");
                    preparedStatement.setTimestamp(7, Timestamp.valueOf(mentor.getDateCreated()));

                    preparedStatement.execute();
                    close(preparedStatement);

                    preparedStatement = connection.prepareStatement(readQuery);
                    resultSet = preparedStatement.executeQuery();

                    resultSet.next();

                    savedMentor = new Mentor();
                    savedMentor.setId(resultSet.getLong("id"));
                    savedMentor.setFirstName(resultSet.getString("first_name"));
                    savedMentor.setLastName(resultSet.getString("last_name"));
                    savedMentor.setPhoneNumber(resultSet.getString("phone_number"));
                    savedMentor.setEmail(resultSet.getString("email"));
                    savedMentor.setDob(resultSet.getDate("dob").toLocalDate());
                    savedMentor.setSalary(Double.valueOf(resultSet.getString("salary").replaceAll("[^\\d\\.]+", "")));
                    savedMentor.setDateCreated(resultSet.getTimestamp("date_created").toLocalDateTime());

                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    close(resultSet);
                    close(preparedStatement);
                    close(connection);
                }
                return savedMentor;
    }

    @Override
    public Mentor findById(Long id) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Mentor mentor = null;

        try {
            connection = getConnection();

            String readQuery = "SELECT * FROM tb_mentors WHERE id =?";

            preparedStatement = connection.prepareStatement(readQuery);
            preparedStatement.setLong(1, id);

            resultSet = preparedStatement.executeQuery();
            resultSet.next();

            mentor = new Mentor();
            mentor.setId(resultSet.getLong("id"));
            mentor.setFirstName(resultSet.getString("first_name"));
            mentor.setLastName(resultSet.getString("last_name"));
            mentor.setPhoneNumber(resultSet.getString("phone_number"));
            mentor.setEmail(resultSet.getString("email"));
            mentor.setDob(resultSet.getDate("dob").toLocalDate());
            mentor.setSalary(Double.valueOf(resultSet.getString("salary").replaceAll("[^\\d\\.]+", "")));
            mentor.setDateCreated(resultSet.getTimestamp("date_create").toLocalDateTime());


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(resultSet);
            close(preparedStatement);
            close(connection);
        } return mentor;
    }

    private void close (AutoCloseable closeable) {
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