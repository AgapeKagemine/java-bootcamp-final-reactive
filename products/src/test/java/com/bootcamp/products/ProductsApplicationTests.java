package com.bootcamp.products;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.annotation.Annotation;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.web.reactive.config.EnableWebFlux;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ProductsApplicationTests {

	@Autowired
	private ApplicationContext applicationContext;

	@Test
	void contextLoads() {
		assertThat(applicationContext).isNotNull();
	}

	@Test
	void testMainMethodDoesNotThrowException() {
		assertDoesNotThrow(() -> ProductsApplication.main(new String[] {}));
	}

	@Test
	void testClassIsAnnotatedWithSpringBootApplication() {
		assertTrue(ProductsApplication.class.isAnnotationPresent(SpringBootApplication.class));
	}

	@Test
	void testClassIsAnnotatedWithEnableWebFlux() {
		assertTrue(ProductsApplication.class.isAnnotationPresent(EnableWebFlux.class));
	}

	@Test
	void testClassIsAnnotatedWithEnableR2dbcRepositories() {
		assertTrue(ProductsApplication.class.isAnnotationPresent(EnableR2dbcRepositories.class));
	}

	@Test
	void testClassHasExactlyThreeAnnotations() {
		Annotation[] annotations = ProductsApplication.class.getAnnotations();
		assertEquals(3, annotations.length);

		assertTrue(Arrays.stream(annotations)
				.allMatch(a -> a instanceof SpringBootApplication
						|| a instanceof EnableWebFlux
						|| a instanceof EnableR2dbcRepositories));
	}

}
