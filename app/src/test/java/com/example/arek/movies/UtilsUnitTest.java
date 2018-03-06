package com.example.arek.movies;

import android.util.Log;

import com.example.arek.movies.utils.DbUtils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UtilsUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void listToString(){
        List<Integer> l = new ArrayList();
//        l.add(1);
//        l.add(2);
//        l.add(3);

//        String s = DbUtils.genreIdsToString(l);
//
//        List<Integer> l1 = DbUtils.genreStringToIds(s);
//
//        assertTrue(l1.containsAll(l));
//        System.out.println(s.toString());
//        System.out.println(l1.toString());
    }

    @Test
    public void listRemove(){
        List<Long> list = new ArrayList<>();
        list.add(3l);
        list.add(1l);
        list.add(1l);
        list.add(3l);
        list.add(4l);

        long l = 1l;

        while (list.contains(l))
        list.remove(l);

        System.out.print(list.toString());

    }
}