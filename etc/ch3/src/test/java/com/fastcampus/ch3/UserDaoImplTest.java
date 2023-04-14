package com.fastcampus.ch3;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
public class UserDaoImplTest {

    @Autowired
    UserDaoImpl userDao;
    @Autowired
    UserDaoJdbcTemplate jdbcTemplate;


    @Before
    public void before() throws Exception {
        userDao.deleteAll();
    }
    @Test
    public void testDeleteUser() {
        int user4 = jdbcTemplate.deleteUser("user4");
        Assert.assertEquals(user4, 1);

    }
    @Test
    public void testSelectUser() throws Exception {
        User user1 = jdbcTemplate.selectUser("user1");
        System.out.println("user1 = " + user1);
    }
    @Test
    public void testInsertUser() {
        int i = jdbcTemplate.insertUser(new User("user4", "test", "test", "test@nate.com", new Date(), "test", new Date()));
        Assert.assertEquals(1, i);
    }

    @Test
    public void testUpdateUser() {

        int i = userDao.updateUser(new User("user1", "test", "test", "test@nate.com", new Date(), "test", new Date()));
        Assert.assertEquals(1, i);

    }
}