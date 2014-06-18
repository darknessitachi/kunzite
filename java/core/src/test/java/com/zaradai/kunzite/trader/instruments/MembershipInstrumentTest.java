/**
 * Copyright 2014 Zaradai
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zaradai.kunzite.trader.instruments;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class MembershipInstrumentTest {
    private static final String TEST_ID = "test";
    private MembershipInstrument uut;
    private InstrumentResolver instrumentResolver;
    @Mock
    List<MembershipInstrument> membersMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        instrumentResolver = mock(InstrumentResolver.class);

        uut = createMemberInstrument();
    }

    private MembershipInstrument createMemberInstrumentWithMock() {
        return new MembershipInstrument(instrumentResolver) {
            @Override
            public InstrumentType getType() {
                return InstrumentType.Option;
            }

            @Override
            protected List<MembershipInstrument> createMembersList() {
                return membersMock;
            }
        };
    }

    private MembershipInstrument createMemberInstrument() {
        return new MembershipInstrument(instrumentResolver) {
            @Override
            public InstrumentType getType() {
                return InstrumentType.Option;
            }
        };
    }


    @Test
    public void shouldAddToWithInstrument() throws Exception {
        MembershipInstrument testIns = mock(MembershipInstrument.class);

        uut.addTo(testIns);

        verify(testIns).add(uut);
    }

    @Test
    public void shouldAddToWithInstrumentId() throws Exception {
        MembershipInstrument testIns = mock(MembershipInstrument.class);
        when(instrumentResolver.resolveInstrument(TEST_ID)).thenReturn(testIns);
        uut.addTo(TEST_ID);

        verify(testIns).add(uut);
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailAddToWithInvalidInstrument() throws Exception {
        uut.addTo((MembershipInstrument) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailAddToWithInvalidInstrumentId() throws Exception {
        uut.addTo((String) null);
    }

    @Test
    public void shouldAddWithInstrument() throws Exception {
        uut = createMemberInstrumentWithMock();
        MembershipInstrument testIns = createMemberInstrument();

        uut.add(testIns);

        verify(membersMock).add(testIns);
    }

    @Test
    public void shouldAddWithInstrumentId() throws Exception {
        uut = createMemberInstrumentWithMock();
        MembershipInstrument testIns = mock(MembershipInstrument.class);
        when(instrumentResolver.resolveInstrument(TEST_ID)).thenReturn(testIns);

        uut.add(TEST_ID);

        verify(membersMock).add(testIns);
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailAddWithInvalidInstrument() throws Exception {
        uut.add((MembershipInstrument) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailAddWithInvalidInstrumentId() throws Exception {
        uut.add((String) null);
    }

    @Test
    public void shouldReturnTrueForIsMemberOfAnyIfAdded() throws Exception {
        MembershipInstrument testIns = mock(MembershipInstrument.class);

        uut.addTo(testIns);

        assertThat(uut.isMemberOfAny(), is(true));
    }

    @Test
    public void shouldBeAMemberOfIfAddedUsingInstrument() throws Exception {
        MembershipInstrument testIns = createMemberInstrument();

        uut.addTo(testIns);

        assertThat(uut.isMemberOf(testIns), is(true));
    }

    @Test
    public void shouldBeAMemberOfIfAddedUsingInstrumentId() throws Exception {
        MembershipInstrument testIns = createMemberInstrument();
        when(instrumentResolver.resolveInstrument(TEST_ID)).thenReturn(testIns);

        uut.addTo(TEST_ID);

        assertThat(uut.isMemberOf(TEST_ID), is(true));
    }

    @Test
    public void shouldGetMembers() throws Exception {
       MembershipInstrument testIns = mock(MembershipInstrument.class);

        uut.add(testIns);

        assertThat(uut.getMembers().size(), is(1));
    }

    @Test
    public void shouldGetInstrument() throws Exception {
        MembershipInstrument testIns = mock(MembershipInstrument.class);
        when(instrumentResolver.resolveInstrument(TEST_ID)).thenReturn(testIns);

        MembershipInstrument res = uut.getInstrument(TEST_ID);

        verify(instrumentResolver).resolveInstrument(TEST_ID);
    }
}
