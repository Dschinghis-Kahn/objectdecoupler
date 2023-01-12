package net.dschinghiskahn.objectdecoupler;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ObjectDecouplerTest implements IObjectReceiver<Integer> {

    private int expextedNumber;
    private boolean result;
    private ObjectDecoupler<Integer> objectDecoupler;

    @Override
    public void receiveObject(Integer object) {
        if (!Integer.valueOf(expextedNumber).equals(object)) {
            result = false;
        }
        expextedNumber++;
        try {
            Thread.sleep(2);
        } catch (InterruptedException e) {
            System.err.println(getClass().getSimpleName()+" - Error while sleeping!\n" + e.getLocalizedMessage());
        }
    }

    @Before
    public void before() throws InterruptedException {
        expextedNumber = 0;
        result = true;
        objectDecoupler = new ObjectDecoupler<Integer>();
        objectDecoupler.registerObjectReceiver(this);
    }

    @After
    public void after() throws InterruptedException {
        objectDecoupler.stop();
    }

    @Test(timeout = 1000)
    public void fewIntegers() throws InterruptedException {
        System.out.println(getClass().getSimpleName()+" - Running test: fewIntegers()");
        objectDecoupler.add(0);
        objectDecoupler.add(1);
        objectDecoupler.add(2);
        objectDecoupler.add(3);
        objectDecoupler.add(4);
        while (!objectDecoupler.isEmpty()) {
            Thread.sleep(10);
        }
        Assert.assertTrue(result);
        Assert.assertEquals("Count incorrect!", 5, expextedNumber);
    }

    @Test(timeout = 5000)
    public void manyIntegers() throws InterruptedException {
    	System.out.println(getClass().getSimpleName()+" - Running test: manyIntegers()");
        for (int i = 0; i < 1050; i++) {
            objectDecoupler.add(i);
        }
        while (!objectDecoupler.isEmpty()) {
            Thread.sleep(10);
        }
        Assert.assertTrue(result);
        Assert.assertEquals("Count incorrect!", 1050, expextedNumber);
    }
}
