package com.ibm.docs.test.gui.cases;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.ibm.docs.test.gui.cases.spreadsheet.DIH_WEB_010;
import com.ibm.docs.test.gui.cases.spreadsheet.DOH_CUR_010;

/**
 * Suite test with reporter.
 * 
 * @author xinyutan@cn.ibm.com
 * 
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
	StartReporter.class,
	DIH_WEB_010.class,
	DOH_CUR_010.class,
	Finally.class
})
public class SuiteTest {

}
