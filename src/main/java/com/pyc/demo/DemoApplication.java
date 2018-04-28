package com.pyc.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**@MapperScan("com.base.mapper")
 * @author yansou
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.pyc.demo"})
@MapperScan(basePackages={"com.pyc.demo.mapper"})
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}
