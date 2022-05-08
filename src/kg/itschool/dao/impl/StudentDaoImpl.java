package kg.itschool.dao.impl;

import kg.itschool.dao.StudentDao;
import kg.itschool.dao.impl.daoutil.Log;
import kg.itschool.model.Student;

import javax.xml.transform.Result;
import java.sql.*;

public class StudentDaoImpl implements StudentDao {

    public StudentDaoImpl() {

        Connection connection = null;
        Statement statement = null;
        try {
            Log.info(this.getClass().getSimpleName(), Connection.class.getSimpleName(), "Establishing connection");
            connection = getConnection();
            Log.info(this.getClass().getSimpleName(), connection.getClass().getSimpleName(), "Connection establishing");

            String ddlQuery = "CREATE TABLE IF NOT EXISTS tb_students(" +
                    "id BIGSERIAL, " +
                    "first_name VARCHAR(50) NOT NULL, " +
                    "last_name VARCHAR(50) NOT NULL, " +
                    "phone_number CHAR(13) NOT NULL, " +
                    "email VARCHAR(100) NOT NULL UNIQUE, " +
                    "dob DATE NOT NULL CHECK( dob < now()), " +
                    "date_created TIMESTAMP NOT NULL DEFAULT NOW())";

            statement = connection.createStatement();
            statement.execute(ddlQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(statement);
            close(connection);
        }
    }

    @Override
    public Student save(Student student) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Student savedStudent = student;

        try {
            Log.info(this.getClass().getSimpleName(), Connection.class.getSimpleName(), "Establishing connection");
            connection = getConnection();
            Log.info(this.getClass().getSimpleName(), connection.getClass().getSimpleName(), "Connection establishing");

            String createQuery = "INSERT INTO tb_students(first_name, last_name, phone_number, email, dob, date_created), " +
                    "VALUES(?, ?, ?, ?, ?, ?)";

            String readQuery = "SELECT * FROM tb_students ORDER BY id DESC LIMIT 1";

            preparedStatement = connection.prepareStatement(createQuery);
            preparedStatement.setString(1, student.getFirstName());
            preparedStatement.setString(2, student.getLastName());
            preparedStatement.setString(3,student.getPhoneNumber());
            preparedStatement.setString(4, student.getEmail());
            preparedStatement.setString(5, student.getDob());
            preparedStatement.setTimestamp(6,Timestamp.valueOf( student.getDateCreated()));

            preparedStatement.execute();
            close(preparedStatement);

            preparedStatement = connection.prepareStatement(createQuery);
            resultSet = preparedStatement.executeQuery();

            resultSet.next();

            preparedStatement = conncetion.preparedStatement();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Student findById(Long id) {
        return null;
    }

    private void close (AutoCloseable closeable) {
        try {
            closeable.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

