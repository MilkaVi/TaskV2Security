package se.repository;


import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import se.Mapping.UserMapping;
import se.config.BDconfig;
import se.domain.User;

import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {
    private List<User> users = new ArrayList<User>();

    public AnnotationConfigApplicationContext a = new AnnotationConfigApplicationContext(BDconfig.class);

    private JdbcTemplate jdbcTemplate = a.getBean("jdbc", JdbcTemplate.class);

    public UserRepositoryImpl() {
        String sql = "SELECT * FROM usr";
        users.addAll(jdbcTemplate.query(sql, new UserMapping()));// UserMapping()- явно указать как парсить
    }



    public void save(User user) {
        jdbcTemplate.update(
                "INSERT INTO usr (login, password, role) VALUES ( ?, ?, ?)",
                 user.getLogin(), user.getPassword(), user.getRole()
        );
        users.add(user);
    }

    public void delete(Integer id) {
        jdbcTemplate.update("delete from usr where id = ?", id);
    }

    public List<User> getAll() {
        List<User> user = new ArrayList<User>();
        String sql = "SELECT * FROM usr";
        user.addAll(jdbcTemplate.query(sql, new UserMapping()));// UserMapping()- явно указать как парсить
        return user;
    }


    @Override
    public User getByLogPass(String login, String password) {
        String sql = "select * from usr where login = '" + login + "'and password =  '" + password + "'";
        if (jdbcTemplate.query(sql, new UserMapping()).isEmpty())
            return null;
        return jdbcTemplate.query(sql, new UserMapping()).get(0);
    }

    @Override
    public User getByFileUser(Integer id) {
        String sql = "select * from usr where id = " + id;
        if (jdbcTemplate.query(sql, new UserMapping()).isEmpty())
            return null;
        return jdbcTemplate.query(sql, new UserMapping()).get(0);
    }

    @Override
    public Integer getId(User user) {
        String sql = "select * from usr where login = '" + user.getLogin()+ "'and password =  '" + user.getPassword() + "'";

        if (jdbcTemplate.query(sql, new UserMapping()).isEmpty())
            return null;
        return jdbcTemplate.query(sql, new UserMapping()).get(0).getId();
    }

    @Override
    public User getUserByUsername(String username) {
        String sql = "select * from usr where login = '" + username+ "'";
        if (jdbcTemplate.query(sql, new UserMapping()).isEmpty())
            return null;
        return jdbcTemplate.queryForObject(sql, new UserMapping());
    }


}