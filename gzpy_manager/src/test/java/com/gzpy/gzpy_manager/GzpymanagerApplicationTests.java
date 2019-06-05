package com.gzpy.gzpy_manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListeners;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.context.junit4.SpringRunner;

import com.gzpy.demo.dao.DepartMapper;
import com.gzpy.demo.dao.EmployeeMapper;
import com.gzpy.demo.po.Department;
import com.gzpy.demo.po.Employee;
/**
 * 测试类也要在主程序包路径下面
 * */
@RunWith(SpringRunner.class)
@SpringBootTest
@EnableScheduling
public class GzpymanagerApplicationTests {

	@Autowired
	JdbcTemplate template;
	@Autowired
	DepartMapper mapper;
	@Autowired
	EmployeeMapper employeeDao;
	@Autowired
	RedisTemplate<Object,Object> redisTemplate;
	@Autowired
	AmqpTemplate amqbTemplate;
	
	
	@Test
	public void contextLoads() {
		List list = template.queryForList("select * from department");
		Department d = template.query(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement("select * from department where id =?");
				ps.setInt(1, 1);
				return ps;
			}
		}, new ResultSetExtractor<Department>() {

			@Override
			public Department extractData(ResultSet rs) throws SQLException, DataAccessException {
				if(rs.next()) {
					Department depart = new Department();
					depart.setId(rs.getInt(1));
					depart.setDepartmentName(rs.getString(2));
					return depart;
				}
				return null;
			}
		});
		
		System.out.println(list);
		System.out.println(d);
	}

	@Test
	public void mybatisTest() {
		Department depart = new Department();
		depart.setDepartmentName("红跑车");
		System.out.println(mapper.addDepart(depart));
		System.out.println(depart);
	}
	
	@Test
	public void mybatis2() {
//		Employee employee = new  Employee();
//		employee.setLastName("刘睢县");
//		employee.setGender(0);
//		employee.setEmail("123456@we.com");
//		employee.setDid(1);
//		System.out.println(employeeDao.addEmployee(employee));
	    System.out.println(employeeDao.queryEmployeeById(1));
	}

	/**
	 * 
	 * */
	@Test
	public void redis01() {
		Employee e= employeeDao.queryEmployeeById(1);
		Jackson2JsonRedisSerializer<Object> jackson = new Jackson2JsonRedisSerializer<Object>(Object.class);
		/**
		 * 在bean创建时，我们只需要设置setDefaultSerializer，因为redisTemplate继承了InitializingBean
		 * 初始化的时候就会调用afterPropertiesSet将DefaultSerializer的值复值给KeySerializer、
		 * ValueSerializer等，
		 * 而在这里测试，我们取到了这个bean，设置DefaultSerializer是不会将其复制给上述序列化器的，所以我给他们手动赋值了
		 * 
		 * */
	//	redisTemplate.setDefaultSerializer(jackson);
		//redisTemplate.setKeySerializer(jackson);
		//redisTemplate.setValueSerializer(jackson);
        redisTemplate.opsForValue().set("niu1", e);
	}
	
	@Test
	public void rabbitMq() {
		Employee e = new Employee();
		e.setDid(100);
		e.setEmail("1592214579@qq.com");
		e.setLastName("最后一个神");
        amqbTemplate.convertAndSend("mdj.direct","mdj",e);
	}
	
	@Test
	public void receiveMq() {
		System.out.println("接受");
		Object o = amqbTemplate.receiveAndConvert("rabbit02_test");
	}
	
	@Test
	public void testSchedule() {
		while(true) {
			
		}
	}
	
}
