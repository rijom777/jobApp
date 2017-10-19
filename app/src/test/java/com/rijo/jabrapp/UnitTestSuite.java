package com.rijo.jabrapp;

import com.rijo.jabrapp.dataRepository.ProductRepositoryImplTest;
import com.rijo.jabrapp.ui.startupScreen.StartUpPresenterTest;
import com.rijo.jabrapp.util.UtilitiesTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by rijogeorge on 8/22/17.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({StartUpPresenterTest.class,
        ProductRepositoryImplTest.class,
        UtilitiesTest.class})
public class UnitTestSuite {
}
