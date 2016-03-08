package net.dschinghiskahn.objectdecoupler;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

@SuppressWarnings("PMD")
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
            Logger.getLogger(getClass()).error("Error while sleeping!", e);
        }
    }

    @BeforeClass
    public static void init() {
        Logger.getRootLogger().setLevel(Level.INFO);
        Logger.getRootLogger().removeAllAppenders();
        ConsoleAppender appender = new ConsoleAppender();
        appender.setLayout(new PatternLayout("%d %-5p: %m%n"));
        appender.activateOptions();
        Logger.getRootLogger().addAppender(appender);
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
        Logger.getLogger(getClass()).info("Running test: fewIntegers()");
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
        Logger.getLogger(getClass()).info("Running test: manyIntegers()");
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
