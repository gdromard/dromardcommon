/**
 * 	File : StringEscapeHelperTest.java 21 juin 07
 */

package net.dromard.common.junit;

import java.util.HashMap;
import java.util.Iterator;

import net.dromard.common.util.StringEscapeHelper;

import junit.framework.TestCase;

/**
 * .
 * <br>
 * @author Gabriel Dromard
 */
public class StringEscapeHelperTest extends TestCase {
    private static final HashMap HTML_ENTITIES = new HashMap();
    static {
        HTML_ENTITIES.put("nbsp", new Integer(160));
        HTML_ENTITIES.put("iexcl", new Integer(161));
        HTML_ENTITIES.put("cent", new Integer(162));
        HTML_ENTITIES.put("pound", new Integer(163));
        HTML_ENTITIES.put("curren", new Integer(164));
        HTML_ENTITIES.put("yen", new Integer(165));
        HTML_ENTITIES.put("brvbar", new Integer(166));
        HTML_ENTITIES.put("sect", new Integer(167));
        HTML_ENTITIES.put("uml", new Integer(168));
        HTML_ENTITIES.put("copy", new Integer(169));
        HTML_ENTITIES.put("ordf", new Integer(170));
        HTML_ENTITIES.put("laquo", new Integer(171));
        HTML_ENTITIES.put("not", new Integer(172));
        HTML_ENTITIES.put("shy", new Integer(173));
        HTML_ENTITIES.put("reg", new Integer(174));
        HTML_ENTITIES.put("macr", new Integer(175));
        HTML_ENTITIES.put("deg", new Integer(176));
        HTML_ENTITIES.put("plusmn", new Integer(177));
        HTML_ENTITIES.put("sup2", new Integer(178));
        HTML_ENTITIES.put("sup3", new Integer(179));
        HTML_ENTITIES.put("acute", new Integer(180));
        HTML_ENTITIES.put("micro", new Integer(181));
        HTML_ENTITIES.put("para", new Integer(182));
        HTML_ENTITIES.put("middot", new Integer(183));
        HTML_ENTITIES.put("cedil", new Integer(184));
        HTML_ENTITIES.put("sup1", new Integer(185));
        HTML_ENTITIES.put("ordm", new Integer(186));
        HTML_ENTITIES.put("raquo", new Integer(187));
        HTML_ENTITIES.put("frac14", new Integer(188));
        HTML_ENTITIES.put("frac12", new Integer(189));
        HTML_ENTITIES.put("frac34", new Integer(190));
        HTML_ENTITIES.put("iquest", new Integer(191));
        HTML_ENTITIES.put("Agrave", new Integer(192));
        HTML_ENTITIES.put("Aacute", new Integer(193));
        HTML_ENTITIES.put("Acirc", new Integer(194));
        HTML_ENTITIES.put("Atilde", new Integer(195));
        HTML_ENTITIES.put("Auml", new Integer(196));
        HTML_ENTITIES.put("Aring", new Integer(197));
        HTML_ENTITIES.put("AElig", new Integer(198));
        HTML_ENTITIES.put("Ccedil", new Integer(199));
        HTML_ENTITIES.put("Egrave", new Integer(200));
        HTML_ENTITIES.put("Eacute", new Integer(201));
        HTML_ENTITIES.put("Ecirc", new Integer(202));
        HTML_ENTITIES.put("Euml", new Integer(203));
        HTML_ENTITIES.put("Igrave", new Integer(204));
        HTML_ENTITIES.put("Iacute", new Integer(205));
        HTML_ENTITIES.put("Icirc", new Integer(206));
        HTML_ENTITIES.put("Iuml", new Integer(207));
        HTML_ENTITIES.put("ETH", new Integer(208));
        HTML_ENTITIES.put("Ntilde", new Integer(209));
        HTML_ENTITIES.put("Ograve", new Integer(210));
        HTML_ENTITIES.put("Oacute", new Integer(211));
        HTML_ENTITIES.put("Ocirc", new Integer(212));
        HTML_ENTITIES.put("Otilde", new Integer(213));
        HTML_ENTITIES.put("Ouml", new Integer(214));
        HTML_ENTITIES.put("times", new Integer(215));
        HTML_ENTITIES.put("Oslash", new Integer(216));
        HTML_ENTITIES.put("Ugrave", new Integer(217));
        HTML_ENTITIES.put("Uacute", new Integer(218));
        HTML_ENTITIES.put("Ucirc", new Integer(219));
        HTML_ENTITIES.put("Uuml", new Integer(220));
        HTML_ENTITIES.put("Yacute", new Integer(221));
        HTML_ENTITIES.put("THORN", new Integer(222));
        HTML_ENTITIES.put("szlig", new Integer(223));
        HTML_ENTITIES.put("agrave", new Integer(224));
        HTML_ENTITIES.put("aacute", new Integer(225));
        HTML_ENTITIES.put("acirc", new Integer(226));
        HTML_ENTITIES.put("atilde", new Integer(227));
        HTML_ENTITIES.put("auml", new Integer(228));
        HTML_ENTITIES.put("aring", new Integer(229));
        HTML_ENTITIES.put("aelig", new Integer(230));
        HTML_ENTITIES.put("ccedil", new Integer(231));
        HTML_ENTITIES.put("egrave", new Integer(232));
        HTML_ENTITIES.put("eacute", new Integer(233));
        HTML_ENTITIES.put("ecirc", new Integer(234));
        HTML_ENTITIES.put("euml", new Integer(235));
        HTML_ENTITIES.put("igrave", new Integer(236));
        HTML_ENTITIES.put("iacute", new Integer(237));
        HTML_ENTITIES.put("icirc", new Integer(238));
        HTML_ENTITIES.put("iuml", new Integer(239));
        HTML_ENTITIES.put("eth", new Integer(240));
        HTML_ENTITIES.put("ntilde", new Integer(241));
        HTML_ENTITIES.put("ograve", new Integer(242));
        HTML_ENTITIES.put("oacute", new Integer(243));
        HTML_ENTITIES.put("ocirc", new Integer(244));
        HTML_ENTITIES.put("otilde", new Integer(245));
        HTML_ENTITIES.put("ouml", new Integer(246));
        HTML_ENTITIES.put("divide", new Integer(247));
        HTML_ENTITIES.put("oslash", new Integer(248));
        HTML_ENTITIES.put("ugrave", new Integer(249));
        HTML_ENTITIES.put("uacute", new Integer(250));
        HTML_ENTITIES.put("ucirc", new Integer(251));
        HTML_ENTITIES.put("uuml", new Integer(252));
        HTML_ENTITIES.put("yacute", new Integer(253));
        HTML_ENTITIES.put("thorn", new Integer(254));
        HTML_ENTITIES.put("yuml", new Integer(255));
        HTML_ENTITIES.put("fnof", new Integer(402));
        HTML_ENTITIES.put("Alpha", new Integer(913));
        HTML_ENTITIES.put("Beta", new Integer(914));
        HTML_ENTITIES.put("Gamma", new Integer(915));
        HTML_ENTITIES.put("Delta", new Integer(916));
        HTML_ENTITIES.put("Epsilon", new Integer(917));
        HTML_ENTITIES.put("Zeta", new Integer(918));
        HTML_ENTITIES.put("Eta", new Integer(919));
        HTML_ENTITIES.put("Theta", new Integer(920));
        HTML_ENTITIES.put("Iota", new Integer(921));
        HTML_ENTITIES.put("Kappa", new Integer(922));
        HTML_ENTITIES.put("Lambda", new Integer(923));
        HTML_ENTITIES.put("Mu", new Integer(924));
        HTML_ENTITIES.put("Nu", new Integer(925));
        HTML_ENTITIES.put("Xi", new Integer(926));
        HTML_ENTITIES.put("Omicron", new Integer(927));
        HTML_ENTITIES.put("Pi", new Integer(928));
        HTML_ENTITIES.put("Rho", new Integer(929));
        HTML_ENTITIES.put("Sigma", new Integer(931));
        HTML_ENTITIES.put("Tau", new Integer(932));
        HTML_ENTITIES.put("Upsilon", new Integer(933));
        HTML_ENTITIES.put("Phi", new Integer(934));
        HTML_ENTITIES.put("Chi", new Integer(935));
        HTML_ENTITIES.put("Psi", new Integer(936));
        HTML_ENTITIES.put("Omega", new Integer(937));
        HTML_ENTITIES.put("alpha", new Integer(945));
        HTML_ENTITIES.put("beta", new Integer(946));
        HTML_ENTITIES.put("gamma", new Integer(947));
        HTML_ENTITIES.put("delta", new Integer(948));
        HTML_ENTITIES.put("epsilon", new Integer(949));
        HTML_ENTITIES.put("zeta", new Integer(950));
        HTML_ENTITIES.put("eta", new Integer(951));
        HTML_ENTITIES.put("theta", new Integer(952));
        HTML_ENTITIES.put("iota", new Integer(953));
        HTML_ENTITIES.put("kappa", new Integer(954));
        HTML_ENTITIES.put("lambda", new Integer(955));
        HTML_ENTITIES.put("mu", new Integer(956));
        HTML_ENTITIES.put("nu", new Integer(957));
        HTML_ENTITIES.put("xi", new Integer(958));
        HTML_ENTITIES.put("omicron", new Integer(959));
        HTML_ENTITIES.put("pi", new Integer(960));
        HTML_ENTITIES.put("rho", new Integer(961));
        HTML_ENTITIES.put("sigmaf", new Integer(962));
        HTML_ENTITIES.put("sigma", new Integer(963));
        HTML_ENTITIES.put("tau", new Integer(964));
        HTML_ENTITIES.put("upsilon", new Integer(965));
        HTML_ENTITIES.put("phi", new Integer(966));
        HTML_ENTITIES.put("chi", new Integer(967));
        HTML_ENTITIES.put("psi", new Integer(968));
        HTML_ENTITIES.put("omega", new Integer(969));
        HTML_ENTITIES.put("thetasym", new Integer(977));
        HTML_ENTITIES.put("upsih", new Integer(978));
        HTML_ENTITIES.put("piv", new Integer(982));
        HTML_ENTITIES.put("bull", new Integer(8226));
        HTML_ENTITIES.put("hellip", new Integer(8230));
        HTML_ENTITIES.put("prime", new Integer(8242));
        HTML_ENTITIES.put("Prime", new Integer(8243));
        HTML_ENTITIES.put("oline", new Integer(8254));
        HTML_ENTITIES.put("frasl", new Integer(8260));
        HTML_ENTITIES.put("weierp", new Integer(8472));
        HTML_ENTITIES.put("image", new Integer(8465));
        HTML_ENTITIES.put("real", new Integer(8476));
        HTML_ENTITIES.put("trade", new Integer(8482));
        HTML_ENTITIES.put("alefsym", new Integer(8501));
        HTML_ENTITIES.put("larr", new Integer(8592));
        HTML_ENTITIES.put("uarr", new Integer(8593));
        HTML_ENTITIES.put("rarr", new Integer(8594));
        HTML_ENTITIES.put("darr", new Integer(8595));
        HTML_ENTITIES.put("harr", new Integer(8596));
        HTML_ENTITIES.put("crarr", new Integer(8629));
        HTML_ENTITIES.put("lArr", new Integer(8656));
        HTML_ENTITIES.put("uArr", new Integer(8657));
        HTML_ENTITIES.put("rArr", new Integer(8658));
        HTML_ENTITIES.put("dArr", new Integer(8659));
        HTML_ENTITIES.put("hArr", new Integer(8660));
        HTML_ENTITIES.put("forall", new Integer(8704));
        HTML_ENTITIES.put("part", new Integer(8706));
        HTML_ENTITIES.put("exist", new Integer(8707));
        HTML_ENTITIES.put("empty", new Integer(8709));
        HTML_ENTITIES.put("nabla", new Integer(8711));
        HTML_ENTITIES.put("isin", new Integer(8712));
        HTML_ENTITIES.put("notin", new Integer(8713));
        HTML_ENTITIES.put("ni", new Integer(8715));
        HTML_ENTITIES.put("prod", new Integer(8719));
        HTML_ENTITIES.put("sum", new Integer(8721));
        HTML_ENTITIES.put("minus", new Integer(8722));
        HTML_ENTITIES.put("lowast", new Integer(8727));
        HTML_ENTITIES.put("radic", new Integer(8730));
        HTML_ENTITIES.put("prop", new Integer(8733));
        HTML_ENTITIES.put("infin", new Integer(8734));
        HTML_ENTITIES.put("ang", new Integer(8736));
        HTML_ENTITIES.put("and", new Integer(8743));
        HTML_ENTITIES.put("or", new Integer(8744));
        HTML_ENTITIES.put("cap", new Integer(8745));
        HTML_ENTITIES.put("cup", new Integer(8746));
        HTML_ENTITIES.put("int", new Integer(8747));
        HTML_ENTITIES.put("there4", new Integer(8756));
        HTML_ENTITIES.put("sim", new Integer(8764));
        HTML_ENTITIES.put("cong", new Integer(8773));
        HTML_ENTITIES.put("asymp", new Integer(8776));
        HTML_ENTITIES.put("ne", new Integer(8800));
        HTML_ENTITIES.put("equiv", new Integer(8801));
        HTML_ENTITIES.put("le", new Integer(8804));
        HTML_ENTITIES.put("ge", new Integer(8805));
        HTML_ENTITIES.put("sub", new Integer(8834));
        HTML_ENTITIES.put("sup", new Integer(8835));
        HTML_ENTITIES.put("nsub", new Integer(8836));
        HTML_ENTITIES.put("sube", new Integer(8838));
        HTML_ENTITIES.put("supe", new Integer(8839));
        HTML_ENTITIES.put("oplus", new Integer(8853));
        HTML_ENTITIES.put("otimes", new Integer(8855));
        HTML_ENTITIES.put("perp", new Integer(8869));
        HTML_ENTITIES.put("sdot", new Integer(8901));
        HTML_ENTITIES.put("lceil", new Integer(8968));
        HTML_ENTITIES.put("rceil", new Integer(8969));
        HTML_ENTITIES.put("lfloor", new Integer(8970));
        HTML_ENTITIES.put("rfloor", new Integer(8971));
        HTML_ENTITIES.put("lang", new Integer(9001));
        HTML_ENTITIES.put("rang", new Integer(9002));
        HTML_ENTITIES.put("loz", new Integer(9674));
        HTML_ENTITIES.put("spades", new Integer(9824));
        HTML_ENTITIES.put("clubs", new Integer(9827));
        HTML_ENTITIES.put("hearts", new Integer(9829));
        HTML_ENTITIES.put("diams", new Integer(9830));
        HTML_ENTITIES.put("quot", new Integer(34));
        HTML_ENTITIES.put("amp", new Integer(38));
        HTML_ENTITIES.put("lt", new Integer(60));
        HTML_ENTITIES.put("gt", new Integer(62));
        HTML_ENTITIES.put("OElig", new Integer(338));
        HTML_ENTITIES.put("oelig", new Integer(339));
        HTML_ENTITIES.put("Scaron", new Integer(352));
        HTML_ENTITIES.put("scaron", new Integer(353));
        HTML_ENTITIES.put("Yuml", new Integer(376));
        HTML_ENTITIES.put("circ", new Integer(710));
        HTML_ENTITIES.put("tilde", new Integer(732));
        HTML_ENTITIES.put("ensp", new Integer(8194));
        HTML_ENTITIES.put("emsp", new Integer(8195));
        HTML_ENTITIES.put("thinsp", new Integer(8201));
        HTML_ENTITIES.put("zwnj", new Integer(8204));
        HTML_ENTITIES.put("zwj", new Integer(8205));
        HTML_ENTITIES.put("lrm", new Integer(8206));
        HTML_ENTITIES.put("rlm", new Integer(8207));
        HTML_ENTITIES.put("ndash", new Integer(8211));
        HTML_ENTITIES.put("mdash", new Integer(8212));
        HTML_ENTITIES.put("lsquo", new Integer(8216));
        HTML_ENTITIES.put("rsquo", new Integer(8217));
        HTML_ENTITIES.put("sbquo", new Integer(8218));
        HTML_ENTITIES.put("ldquo", new Integer(8220));
        HTML_ENTITIES.put("rdquo", new Integer(8221));
        HTML_ENTITIES.put("bdquo", new Integer(8222));
        HTML_ENTITIES.put("dagger", new Integer(8224));
        HTML_ENTITIES.put("Dagger", new Integer(8225));
        HTML_ENTITIES.put("permil", new Integer(8240));
        HTML_ENTITIES.put("lsaquo", new Integer(8249));
        HTML_ENTITIES.put("rsaquo", new Integer(8250));
        HTML_ENTITIES.put("euro", new Integer(8364));
    }

    /**
     * See tests attributes for more details.
     * @testId             PLT_MIS_0005
     * @testName           GString tests
     * @testDescription    Test in GString class, the content of HTML ENTITIES map.
     * @testType           MIS
     */
    public void testHtmlEntitiesMap() {
        assertEquals(StringEscapeHelper.HTML_ENTITIES.size(), HTML_ENTITIES.size());
        for (Iterator it = HTML_ENTITIES.keySet().iterator(); it.hasNext();) {
            Object key = it.next();
            Object value = HTML_ENTITIES.get(key);
            assertTrue(StringEscapeHelper.HTML_ENTITIES.containsKey(key));
            assertTrue(StringEscapeHelper.HTML_ENTITIES.containsValue(value));
            assertEquals(value, StringEscapeHelper.HTML_ENTITIES.get(key));
        }
    }

    public void testEscapeXMLDoubleQuotes() {
        assertEquals("&quot;&apos;&lt;&gt;&amp;", StringEscapeHelper.escapeXML("\"'<>&"));
	}
}


