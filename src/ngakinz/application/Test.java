package ngakinz.application;

import com.github.javafaker.Faker;

public class Test {
	
	
	public static void main(String[] args) {
		Faker faker = new Faker();
		System.out.println(faker.color().name());
	}

}
