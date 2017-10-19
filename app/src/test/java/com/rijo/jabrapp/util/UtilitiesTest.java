package com.rijo.jabrapp.util;

import com.rijo.jabrapp.Mock.MockedStrings;
import com.rijo.jabrapp.dataRepository.Const;
import com.rijo.jabrapp.domains.product.Products;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;

/**
 * Created by rijogeorge on 8/20/17.
 */
public class UtilitiesTest {
    @Test
    public void downloadProductsFromUrlTest() throws Exception {
        //when
        String downloadStr=Utilities.downloadProductsFromUrl(Const.walmartPaginatedUrl);
        //then
        assertTrue(downloadStr.length()>0);
    }

    @Test
    public void getClassFromJSONStringTest() throws Exception {
        //when
        Products products=Utilities.getClassFromJSONString(MockedStrings.paginatedJsonStr, Products.class);
        //then
        assertTrue(products!=null);
    }

    @Test
    public void covertInputStreamToStringTest() throws Exception {
        String string="Rijo George";
        InputStream stream=new ByteArrayInputStream(string.getBytes());
        //using reflection to access private method
        Class c=Class.forName("com.rijo.jabrapp.util.Utilities");
        Method method = c.getDeclaredMethod("covertInputStreamToString", InputStream.class);
        method.setAccessible(true);
        String result=(String)method.invoke(c,stream);
        assertEquals(string,result);
    }
}