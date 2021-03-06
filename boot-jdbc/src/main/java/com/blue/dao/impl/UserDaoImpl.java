package com.blue.dao.impl;

import com.blue.dao.UserDao;
import com.blue.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author zch
 * @Description
 * @createDate 2018/10/27
 **/
@Repository
public class UserDaoImpl extends UserDao {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<User> findAll() throws SQLException {
        String sql = String.format("SELECT * from %s ", tableName());
        List<User> users = jdbcTemplate.query(sql, new UserRowMapper());
        return users;
    }

    @Override
    public User findByID(String id) throws SQLException {
        String sql = String.format("SELECT * from %s where id =?", tableName());
        User user = jdbcTemplate.queryForObject(sql, new UserRowMapper(), id);
        return user;
    }

    @Override
    public List<User> findAll(int page, int var) throws SQLException {
        String sql = String.format("SELECT * from %s limit ?,?", tableName());
        List<User> users = jdbcTemplate.query(sql, new UserRowMapper(), page, var);
        return users;
    }

    @Override
    public void addUser(User user) throws SQLException {
        StringBuilder builder = new StringBuilder(124);
        builder.append("insert into ").append(tableName()).append(" (").append(fields()).append(")");
        builder.append(" ").append("values(");
        Field[ ] fields = User.class.getDeclaredFields( );
        for (int i = 0; i < fields.length; i++) {
            if (i == fields.length-1){
                builder.append("?");
            }else {
                builder.append("?,");
            }
        }
        builder.append(")");
        logger.info("-----------------------------==>");
        logger.info(builder.toString());
        jdbcTemplate.update(builder.toString(),user.getId(), user.getCreate(), user.getName(), user.getUsername(), user.getPassword(), user.getBirthday(), user.getAddress(), user.getAccount(), user.getPermission(), user.getGroup(), user.getSex(), user.getJurisdiction(), user.getRole(),user.getAge(),user.getCreateDate());
    }

    @Override
    public void batch(List<User> users) throws SQLException {
        StringBuilder builder = new StringBuilder(124);
        builder.append("insert into ").append(tableName()).append("(").append(fields()).append(")");
        builder.append(" ").append("values(");
        Field[ ] fields = User.class.getDeclaredFields( );
        for (int i = 0; i < fields.length; i++) {
            if (i == fields.length-1){
                builder.append("?");
            }else {
                builder.append("?,");
            }
        }
        builder.append(")");
        builder.append(")");
        logger.info(builder.toString());
        List<Object[]> params = new ArrayList<>();
        for (User user : users) {
            params.add(new Object[]{user.getId(), user.getCreate(), user.getName(), user.getUsername(), user.getPassword(), user.getBirthday(), user.getAddress(), user.getAccount(), user.getPermission(), user.getGroup(), user.getSex(), user.getJurisdiction(), user.getRole(),user.getAge(),user.getCreateDate()});
        }
        jdbcTemplate.batchUpdate(builder.toString(), params);
    }

    @Override
    public void delete(String id) throws SQLException {
        String sql = String.format("DELETE from %s where id=?", tableName());
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void update(User user) throws SQLException {
        StringBuilder builder = new StringBuilder(124);
        builder.append("UPDATE ").append(tableName()).append(" ").append(" u ").append("SET ");
        Field[ ] fields = User.class.getDeclaredFields( );
        final String name = User.class.getSimpleName().substring(0,1);
        for (int i = 0; i < fields.length; i++) {
            if (i == fields.length-1){
                builder.append(name).append(".").append("'").append(fields[i].getName()).append("'").append("=").append("?").append(" ");
            }else {
                builder.append(name).append(".").append("'").append(fields[i].getName()).append("'").append("=").append("?");
            }

        }
        builder.append("WHERE").append(name).append(".").append("'id'").append("=").append("?");
        jdbcTemplate.update(builder.toString(), user.getRole(),
                user.getUsername(),
                user.getName(),
                user.getSex(),
                user.getAccount(),
                user.getAddress(),
                user.getGroup(),
                user.getPassword(),
                user.getJurisdiction(),
                user.getPermission(),
                user.getAge(),
                user.getBirthday(),
                user.getCreateDate(),
                user.getId());
    }

    private String tableName() {
        return "'spring-boot-user'";
    }

    private String fields() {
        StringBuilder builder = new StringBuilder(124);
        Field[ ] fields = User.class.getDeclaredFields( );
        for (int i = 0; i < fields.length; i++) {
            if (i == fields.length-1){
                builder.append(" '").append(fields[i].getName()).append("' ");
            }else {
                builder.append(" '").append(fields[i].getName()).append("'").append(", ");
            }

        }
        return builder.toString();
    }

    class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setAge(rs.getInt("'age'"));
            user.setUsername(rs.getString("'username'"));
            user.setPassword(rs.getString("'password'"));
            user.setRole(rs.getString("'role'"));
            user.setPermission(rs.getString("'permission'"));
            user.setJurisdiction(rs.getString("'jurisdiction'"));
            user.setBirthday(rs.getDate("'birthday'"));
            user.setName(rs.getString("'name'"));
            user.setAddress(rs.getString("'address'"));
            user.setAccount(rs.getString("'account'"));
            user.setGroup(rs.getString("'group'"));
            user.setSex(rs.getString("'sex'"));
            user.setId(rs.getString("'id'"));
            user.setCreateDate(rs.getDate("'createDate'"));
            return user;
        }
    }

    public static void main(String[] args) {
    }

}
