package it.corsobackendtree.treebooking;

import it.corsobackendtree.treebooking.DAO.entities.UserDAO;
import it.corsobackendtree.treebooking.DAO.repositories.BookingRepo;
import it.corsobackendtree.treebooking.DAO.repositories.CookieAuthRepo;
import it.corsobackendtree.treebooking.DAO.repositories.EventRepo;
import it.corsobackendtree.treebooking.DAO.repositories.UserRepo;
import it.corsobackendtree.treebooking.services.SecurityService;
import it.corsobackendtree.treebooking.services.UserService;
import it.corsobackendtree.treebooking.views.UserView;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.Date;
import static it.corsobackendtree.treebooking.views.UserView.defaultZoneId;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class TreebookingApplicationTests {
	@Mock
	private CookieAuthRepo cookieAuthRepo;
	@Mock
	private UserRepo userRepo;
	@Mock
	private EventRepo eventRepo;
	@Mock
	private BookingRepo bookingRepo;
	@Autowired
	UserService userService;
	@Autowired
	SecurityService securityService;

	@Test
	void contextLoads() {
	}

	@Test
	void signUpTest(){
		UserView userViewTest = getMockUser();
		HttpServletResponse responseTest = mock(HttpServletResponse.class);
		UserDAO userDAOTest = userService.signUpUser(
				userViewTest, securityService, userRepo, cookieAuthRepo, responseTest);
		assertEquals(userViewTest.getUsername(), userDAOTest.getUsername());
		assertNotEquals(userViewTest.getPassword(), userDAOTest.getPassword());
	}

	@Test
	void checkPasswordTest(){
		String password ="password";
		Integer salt = 5;
		String cripted = securityService.computeHash(password, salt);
		boolean test = userService.checkPassword(password, cripted, securityService, salt);
		assertTrue(test);
		boolean test_2 = userService.checkPassword("password2", cripted, securityService, salt);
		assertFalse(test_2);
	}

	private UserView getMockUser(){
		UserView userViewTest = mock(UserView.class);
		when(userViewTest.getUsername()).thenReturn("TestUser");
		when(userViewTest.getName()).thenReturn("TestName");
		when(userViewTest.getSurname()).thenReturn("TestSurname");
		when(userViewTest.getPassword()).thenReturn("TestPassword");
		when(userViewTest.getGender()).thenReturn(Gender.MALE);
		when(userViewTest.getBirthDate()).thenReturn(Date.from(
				LocalDate.of(1990,11,11).atStartOfDay(defaultZoneId).toInstant()));
		return userViewTest;
	}
}
