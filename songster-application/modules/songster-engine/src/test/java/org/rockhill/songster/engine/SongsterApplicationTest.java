package org.rockhill.songster.engine;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.rockhill.songster.bootstrap.ApplicationBootstrap;

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * Test class of {@link SongsterApplication} class.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(SongsterApplication.class)
public class SongsterApplicationTest {

    private final String[] arguments = {"blah", "blah"};

    @Mock
    private ApplicationBootstrap applicationBootstrap;

    @InjectMocks
    private SongsterApplication underTest;

    @Before
    public void setUp() throws Exception {
        underTest = PowerMockito.mock(SongsterApplication.class);
        whenNew(SongsterApplication.class).withNoArguments().thenReturn(underTest);
        applicationBootstrap = spy(new ApplicationBootstrap());
        whenNew(ApplicationBootstrap.class).withNoArguments().thenReturn(applicationBootstrap);
        doNothing().when(applicationBootstrap).bootstrap(arguments);
    }

    @Test
    public void testMain() {
        SongsterApplication.main(arguments);
        assertArrayEquals(arguments, Whitebox.getInternalState(SongsterApplication.class, "arguments"));
        verify(applicationBootstrap).bootstrap(arguments);
    }
}