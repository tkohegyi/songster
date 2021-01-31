package org.rockhill.songster.web.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;
import org.rockhill.songster.web.helper.MockControllerBase;
import org.rockhill.songster.web.json.CurrentUserInformationJson;
import org.rockhill.songster.web.provider.CurrentUserProvider;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

/**
 * Unit test for {@link HomeController} class.
 */
public class HomeControllerTest {

    @InjectMocks
    private HomeController underTest;
    @Mock
    private CurrentUserProvider currentUserProvider;
    @Mock
    private CurrentUserInformationJson currentUserInformationJson;
    @Mock
    private HttpServletRequest httpServletRequest;
    @Mock
    private Logger logger;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Whitebox.setInternalState(underTest, "currentUserProvider", currentUserProvider);
        Whitebox.setInternalState(underTest, "logger", logger);
        doReturn(currentUserInformationJson).when(currentUserProvider).getUserInformation(null);
    }

    @Test
    public void pseudoHome() {
        //given - no precondition
        //when
        String result = underTest.pseudoHome();
        //then
        assertEquals("redirect:/songster/", result); //redirect to real home
    }

    @Test
    public void realHomeLoggedIn() {
        //given - no precondition
        currentUserInformationJson.isLoggedIn = true;
        //when
        String result = underTest.realHome(null);
        // then
        assertEquals("redirect:/songsterSecure/songs", result);
    }

    @Test
    public void realHomeNotLoggedIn() {
        //given - no precondition
        currentUserInformationJson.isLoggedIn = false;
        //when
        String result = underTest.realHome(null);
        // then
        assertEquals("redirect:/songster/login", result);
    }

    @Test
    public void favicon() {
        //given - no precondition
        //when
        String result = underTest.favicon();
        // then
        assertEquals("redirect:/resources/img/favicon.ico", result);
    }

    @Test
    public void robots() {
        //given - no precondition
        //when
        String result = underTest.robots();
        // then
        assertEquals("redirect:/resources/robots.txt", result);
    }

    @Test
    public void siteMapXml() {
        //given - no precondition
        //when
        String result = underTest.siteMapXml();
        // then
        assertEquals("redirect:/resources/sitemap.xml", result);
    }

    @Test
    public void siteMapText() {
        //given - no precondition
        //when
        String result = underTest.siteMapText();
        // then
        assertEquals("redirect:/resources/sitemap.txt", result);
    }

    @Test
    public void securityText() {
        //given - no precondition
        //when
        String result = underTest.securityText();
        // then
        assertEquals("redirect:/resources/security.txt", result);
    }

    @Test
    public void e404() {
        //given
        doReturn("A").when(httpServletRequest).getRemoteHost();
        doReturn("C").when(httpServletRequest).getMethod();
        //when
        String result = underTest.e404(httpServletRequest);
        // then
        assertEquals("E404", result);
        verify(logger).info("E404 caused by: {}, method: {}, URI: {}", "A", "C", "unknown");
    }

    @Test
    public void e500() {
        //given
        doReturn("A").when(httpServletRequest).getRemoteHost();
        doReturn("C").when(httpServletRequest).getMethod();
        //when
        String result = underTest.e500(httpServletRequest);
        // then
        assertEquals("E404", result);
        verify(logger).info("E500 caused by: {}, method: {}, URI: {}", "A", "C", "unknown");
    }

    @Test
    public void getLoggedInUserInfo() {
        //given
        MockControllerBase mockControllerBase = new MockControllerBase();
        String expected = mockControllerBase.mockGetJsonString("loggedInUserInfo", currentUserInformationJson);
        //when
        ResponseEntity<String> result = underTest.getLoggedInUserInfo(null);
        //then
        String json = result.getBody();
        assertEquals(expected, json);
    }

}