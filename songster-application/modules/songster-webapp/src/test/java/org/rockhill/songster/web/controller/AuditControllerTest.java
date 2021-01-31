package org.rockhill.songster.web.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;
import org.rockhill.songster.web.helper.DummyTestObject;
import org.rockhill.songster.web.json.CurrentUserInformationJson;
import org.rockhill.songster.web.json.TableDataInformationJson;
import org.rockhill.songster.web.provider.AuditProvider;
import org.rockhill.songster.web.provider.CurrentUserProvider;
import org.slf4j.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

/**
 * Unit test for {@link AuditController} class.
 */
public class AuditControllerTest {

    @Mock
    private Logger logger;
    @Mock
    private CurrentUserProvider currentUserProvider;
    @Mock
    private CurrentUserInformationJson currentUserInformationJson;
    @Mock
    private AuditProvider auditProvider;

    @InjectMocks
    private AuditController underTest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Whitebox.setInternalState(underTest, "currentUserProvider", currentUserProvider);
        Whitebox.setInternalState(underTest, "auditProvider", auditProvider);
        Whitebox.setInternalState(underTest, "logger", logger);
        doReturn(currentUserInformationJson).when(currentUserProvider).getUserInformation(null);
    }

    @Test
    public void auditCallByAdministratorShallReturnWithAuditJsp() {
        currentUserInformationJson.isAdmin = true;
        //when
        String result = underTest.audit(null);
        //then
        assertEquals("audit", result);
    }

    @Test
    public void auditCallByNonAdministratorShallRedirectToHome() {
        currentUserInformationJson.isAdmin = false;
        //when
        String result = underTest.audit(null);
        //then
        assertEquals("redirect:/songster/", result);
    }

    @Test
    public void getAuditTrailByDaysByAdministratorShallReturnWithTheInfo() {
        currentUserInformationJson.isAdmin = true;
        DummyTestObject expected = new DummyTestObject();
        doReturn(expected).when(auditProvider).getAuditTrailOfLastDays(1L);
        //when
        TableDataInformationJson result = underTest.getAuditTrailByDays(null, "1");
        //then
        assertEquals(expected, result.data);
    }

    @Test
    public void getAuditTrailByDaysByAdministratorButWrongDataShallReturnWithNone() {
        currentUserInformationJson.isAdmin = true;
        DummyTestObject expected = new DummyTestObject();
        doReturn(expected).when(auditProvider).getAuditTrailOfLastDays(1L);
        //when
        TableDataInformationJson result = underTest.getAuditTrailByDays(null, "not a number");
        //then
        assertNull(result);
        verify(logger).warn("Rouge request to getAuditTrailByDays endpoint with bad days parameter.");
    }

    @Test
    public void getAuditTrailByDaysByNotAdministratorShallReturnWithNone() {
        currentUserInformationJson.isAdmin = false;
        DummyTestObject expected = new DummyTestObject();
        doReturn(expected).when(auditProvider).getAuditTrailOfLastDays(1L);
        //when
        TableDataInformationJson result = underTest.getAuditTrailByDays(null, "1");
        //then
        assertNull(result);
    }

}