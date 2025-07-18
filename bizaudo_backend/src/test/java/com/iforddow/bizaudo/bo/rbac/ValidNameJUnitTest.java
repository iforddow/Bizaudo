package com.iforddow.bizaudo.bo.rbac;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ValidNameJUnitTest {

    public ValidNameJUnitTest() {}

    @BeforeAll
    public static void setUpClass() {
    }
    @AfterAll
    public static void tearDownClass() {}

    @BeforeEach
    public void setUp() {}

    @AfterEach
    public void tearDown() {}

    @Test
    public void testValidName1() {

        RoleBO roleBO = new RoleBO();

        String name = roleBO.getValidCodeName("HR Assistant manager");

        assertEquals("HrAssistantManager", name);
    }

    @Test
    public void testValidName2() {

        RoleBO roleBO = new RoleBO();

        String name = roleBO.getValidCodeName("HR-Assistant/MANAGER");

        assertEquals("HrAssistantManager", name);
    }

    @Test
    public void testValidName3() {

        RoleBO roleBO = new RoleBO();

        String name = roleBO.getValidCodeName("HR7978Assistant/...MANAGER");

        assertEquals("HrAssistantManager", name);
    }
}
