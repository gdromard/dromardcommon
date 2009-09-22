/**
 * 	File : GString.java 11 juin 07
 */
package net.dromard.common.junit;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import junit.framework.Assert;
import junit.framework.TestCase;
import net.dromard.common.templating.reflect.Formatter;
import net.dromard.common.templating.reflect.ReflectTemplateTransformer;
import net.dromard.common.util.ObjectComparator;

/**
 * This class manage transformation with pattern $(parameter}.<br>
 * The replace is done by reflection searching the field value of a JavaBean.
 *
 * @author Gabriel DROMARD
 */
public class ReflectTemplateTransfomerTest extends TestCase {

    /**
     * See tests attributes for more details.
     * @testId             PLT_MIS_4002
     * @testName           ReflectTemplateTransformer tests
     * @testDescription    Test the ReflectTemplateTransformer class. Simple use case.
     * @testType           MIS
     */
    public void testTransform() throws Exception {
        final String gstring = "$(un) petit $(test) pour $(verifier) l'algorithme d'$(extraction) des $(champs). $(internalTest)$(internalTests)";
        final String internalGstring = "\nEt $(un) autre petit $(test) pour $(verifier) l'algorithme d'$(extraction) des sous $(champs). ";
        final String result = "Un petit Test pour Verifier l'algorithme d'Extraction des Champs. \nEt UN autre petit TEST pour VERIFIER l'algorithme d'EXTRACTION des sous CHAMPS. \nEt UN autre petit TEST pour VERIFIER l'algorithme d'EXTRACTION des sous CHAMPS. \nEt UN autre petit TEST pour VERIFIER l'algorithme d'EXTRACTION des sous CHAMPS. ";

        Object bean = new Test();

        String transformed = ReflectTemplateTransformer.transformUsingReflection(gstring, bean, new Formatter() {
            public String format(final Object object) throws Exception {
                if (object instanceof Set) {
                    Set objects = (Set) object;
                    String toReturn = "";
                    for (Iterator it = objects.iterator(); it.hasNext();) {
                        toReturn += format(it.next());
                    }
                    return toReturn;
                }
                if (object instanceof InternalTest) {
                    return ReflectTemplateTransformer.transformUsingReflection(internalGstring, object, new Formatter() {
                        public String format(final Object object) {
                            return object.toString();
                        }
                    });
                }
                return object.toString();
            }
        });
        System.out.println(transformed);
        Assert.assertEquals(result, transformed);
    }

    /**
     * See tests attributes for more details.
     * @testId             PLT_MIS_4002
     * @testName           ReflectTemplateTransformer tests
     * @testDescription    Test the ReflectTemplateTransformer class. Recursive use case.
     * @testType           MIS
     */
    public void testSubTransformation() throws Exception {
        final String gstring = "$(un) $(test) $(internalTest.un) $(internalTest.test) $(internalTest.subInternalTest.un) $(internalTest.subInternalTest.test)";
        final String result = "Un Test UN TEST 1 2";

        Object bean = new Test();
        String transformed = ReflectTemplateTransformer.transformUsingReflection(gstring, bean, new Formatter() {
            public String format(final Object object) {
                return object.toString();
            }
        });
        System.out.println(transformed);
        Assert.assertEquals(result, transformed);
    }

    public void testObjectComparator() throws Exception {
        Assert.assertTrue(ObjectComparator.equals(new Test(), new Test()));
        Assert.assertFalse(ObjectComparator.equals(new Test(), new InternalTest()));
    }
}

class Test {
    public String getUn() {
        return "Un";
    }

    public String getTest() {
        return "Test";
    }

    public String getVerifier() {
        return "Verifier";
    }

    public String getExtraction() {
        return "Extraction";
    }

    public String getChamps() {
        return "Champs";
    }

    public InternalTest getInternalTest() {
        return new InternalTest();
    }

    public Set getInternalTests() {
        Set set = new HashSet();
        set.add(new InternalTest());
        set.add(new InternalTest());
        return set;
    }
}

class InternalTest {
    public String getUn() {
        return "UN";
    }

    public String getTest() {
        return "TEST";
    }

    public String getVerifier() {
        return "VERIFIER";
    }

    public String getExtraction() {
        return "EXTRACTION";
    }

    public String getChamps() {
        return "CHAMPS";
    }

    public SubInternalTest getSubInternalTest() {
        return new SubInternalTest();
    }
}

class SubInternalTest {
    public String getUn() {
        return "1";
    }

    public String getTest() {
        return "2";
    }

    public String getVerifier() {
        return "3";
    }

    public String getExtraction() {
        return "4";
    }

    public String getChamps() {
        return "5";
    }
}
