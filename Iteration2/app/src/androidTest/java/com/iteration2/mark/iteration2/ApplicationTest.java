/*

Claritas: ‘Clarity through innovation’

Unit Test Script

Project: SocBox

Module: Audio Handler

Test Script Name: AudioHandlerTester.java

Associated Code File Name: AudioHandler.java

Description: This class tests the functionality of the Audio Handler Module to match the

requirements found in the User Stories and Design Specification.

Initial Authors: Andrew Perry

              Carlos Archila

Change History:

Version: 0.1

Author: Andrew Perry

Change: Created original version

Date: 11/03/15

User Story Traceabilty:

Tag(s):U/PS Entire module

Requirements in not covered by test script:

U/PS entire module
 Justification: the testing needs to be done once the code has been collated with the remaining user stories

*/
package com.iteration2.mark.iteration2;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ActivityInstrumentationTestCase2<MainJava> {
    public Activity activity;
    private Button button;

    public ApplicationTest() {
        super(MainJava.class);
    }

    protected void setUp() throws Exception{
        super.setUp();
        setActivityInitialTouchMode(false);
        activity = getActivity();
    }



    public void testIntents() {

        /*TextView textPanel = (TextView) this.activity.findViewById(R.id.textPanel);
        assertEquals(100, textPanel.getX());
        assertEquals(100, textPanel.getY());*/
        button = (Button)this.activity.findViewById(R.id.nextSlide);
       // Instrumentation.ActivityMonitor am = getInstrumentation().addMonitor(MainJava.class.getName(), null, true);
        button.performClick();
        //am.waitForActivityWithTimeout(500000);
        //assertEquals(1, am.getHits());
        }

    protected void tearDown() throws Exception{super.tearDown();}
}