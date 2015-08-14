package com.ibm.docs.test.gui.support;

import java.util.ArrayList;
import java.util.List;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.MultipleFailureException;
import org.junit.runners.model.Statement;

import com.ibm.docs.test.common.Config;

/**
 * The language suite rule.
 * 
 * @author xinyutan@cn.ibm.com
 * 
 */
public class LanguageSuite implements TestRule {

	private TestEnv env;

	private List<Throwable> errors = new ArrayList<Throwable>();

	public LanguageSuite(TestEnv env) {
		this.env = env;
	}

	@Override
	public Statement apply(final Statement base, final Description description) {
		return new Statement() {

			@Override
			public void evaluate() throws Throwable {
				GLan gLan = description.getAnnotation(GLan.class);
				boolean hub = Config.getInstance().get("browser", "")
						.startsWith("http://");
				if (gLan != null && !hub) {
					String[] languages = gLan.value();
					for (String language : languages) {
						env.gLanguage = language;
						try {
							base.evaluate();
						} catch (Throwable e) {
							errors.add(e);
						}
					}
					if (!errors.isEmpty()) {
						throw new MultipleFailureException(errors);
					}

				} else {
					base.evaluate();
				}

			}
		};
	}

}
