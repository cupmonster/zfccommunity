package com.zfc.community.mode;

import com.zfc.community.mapper.UserMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class Mybatis {
    //@Autowired
    //private User user;
    public void retainUserInformation(User user) throws IOException {
        //1.读取配置文件
        InputStream in = Resources.getResourceAsStream("sql_session_config.xml");
        //2.创建SqlSessionFactory工厂
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        SqlSessionFactory factory = builder.build(in);
        //3.使用工厂生产SqlSession对象
        SqlSession session = factory.openSession(true);
        //4.使用SqlSession创建dao接口的对象
        UserMapper userMapper = session.getMapper(UserMapper.class);
        //5.使用代理对象执行方法
        userMapper.insert(user);
        //6.释放资源
        session.commit();
        session.close();
        in.close();
    }
}
