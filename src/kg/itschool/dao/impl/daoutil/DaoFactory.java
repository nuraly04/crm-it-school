package kg.itschool.dao.impl.daoutil;

import kg.itschool.dao.GroupDao;
import kg.itschool.dao.ManagerDao;
import kg.itschool.dao.MentorDao;
import kg.itschool.dao.impl.GroupDaoImpl;
import kg.itschool.dao.impl.ManagerDaoImpl;
import kg.itschool.dao.impl.MentorDaoImpl;

public abstract class DaoFactory {

    static {
        try {
            System.out.println("Driver loading...");
            Class.forName("org.postgresql.Driver");
            System.out.println("Driver load");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver loading failed");
            e.printStackTrace();
        }
    }

    public static ManagerDao getManagerDaoSql() {
        return new ManagerDaoImpl();
    }

    public static MentorDao getMentorDaoSql() {
        return new MentorDaoImpl();
    }

    public static GroupDao getGroupDaoSql() {
        return new GroupDaoImpl();
    }
}