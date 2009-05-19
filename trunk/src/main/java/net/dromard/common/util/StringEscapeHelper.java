package net.dromard.common.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Utilities for String formatting, manipulation, and escaping. <br>
 * <br>
 */
public final class StringEscapeHelper {
    /** mask. */
    private static final int MASK = 0xffff;
    /** char. */
    private static final int CHAR = 32;
    /** radix. */
    private static final int RADIX = 10;

    /**
     * Empty private constructor for util class.
     */
    private StringEscapeHelper() {
        // Empty private constructor for util class.
    }

    /**
     * Replaces characters that may be confused by a HTML parser with their equivalent character entity references.
     * <p>
     * Any data that will appear as text on a web page should be be escaped. This is especially important for data that comes from untrusted sources
     * such as Internet users. A common mistake in CGI programming is to ask a user for data and then put that data on a web page. For example:
     *
     * <pre>
     * Server: What is your name?
     * User: &lt;b&gt;Joe&lt;b&gt;
     * Server: Hello &lt;b&gt;Joe&lt;/b&gt;, Welcome
     * </pre>
     *
     * If the name is put on the page without checking that it doesn't contain HTML code or without sanitizing that HTML code, the user could reformat
     * the page, insert scripts, and control the the content on your web server.
     * <p>
     * This method will replace HTML characters such as &gt; with their HTML entity reference (&amp;gt;) so that the html parser will be sure to
     * interpret them as plain text rather than HTML or script.
     * <p>
     * This method should be used for both data to be displayed in text in the html document, and data put in form elements. For example:<br>
     * <code>&lt;html&gt;&lt;body&gt;<i>This in not a &amp;lt;tag&amp;gt; in HTML</i>&lt;/body&gt;&lt;/html&gt;</code><br>
     * and<br>
     * <code>&lt;form&gt;&lt;input type="hidden" name="date" value="<i>This data could be &amp;quot;malicious&amp;quot;</i>
     * "&gt;&lt;/form&gt;</code><br>
     * In the second example, the form data would be properly be resubmitted to your cgi script in the URLEncoded format:<br>
     * <code><i>This data could be %22malicious%22</i></code>
     *
     * @param s String to be escaped
     * @return escaped String
     */
    public static String escapeHTML(final String s) {
        StringBuffer sb = new StringBuffer(s.length());
        for (int i = 0; i < s.length(); ++i) {
            char c = s.charAt(i);
            int cint = StringEscapeHelper.MASK & c;
            if (cint < StringEscapeHelper.CHAR) {
                switch (c) {
                case '\r':
                case '\n':
                case '\t':
                case '\f':
                    sb.append(c);
                    break;
                default: // Remove this character
                }
            } else {
                switch (c) {
                case '\'':
                    sb.append("&#39;");
                    break;
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                // be carefull with this one (non-breaking whitee space)
                // case ' ': sb.append("&nbsp;");break;
                default:
                    sb.append(c);
                    break;
                }
            }
        }
        return StringEscapeHelper.escapeHTMLSpecialCharacters(sb.toString());
    }

    public static String escapeHTMLSpecialCharacters(final String s) {
        StringBuffer sb = new StringBuffer(s.length());
        for (int i = 0; i < s.length(); ++i) {
            char c = s.charAt(i);
            int cint = StringEscapeHelper.MASK & c;
            if (cint < StringEscapeHelper.CHAR) {
                switch (c) {
                case '\r':
                case '\n':
                case '\t':
                case '\f':
                    sb.append(c);
                    break;
                default: // Remove this character
                }
            } else {
                switch (c) {
                case '&':
                    sb.append("&amp;");
                    break;
                case '’':
                    sb.append("&rsquo;");
                    break;
                case '"':
                    sb.append("&quot;");
                    break;
                case 'à':
                    sb.append("&agrave;");
                    break;
                case 'À':
                    sb.append("&Agrave;");
                    break;
                case 'â':
                    sb.append("&acirc;");
                    break;
                case 'Â':
                    sb.append("&Acirc;");
                    break;
                case 'ä':
                    sb.append("&auml;");
                    break;
                case 'Ä':
                    sb.append("&Auml;");
                    break;
                case 'å':
                    sb.append("&aring;");
                    break;
                case 'Å':
                    sb.append("&Aring;");
                    break;
                case 'æ':
                    sb.append("&aelig;");
                    break;
                case 'Æ':
                    sb.append("&AElig;");
                    break;
                case 'ç':
                    sb.append("&ccedil;");
                    break;
                case 'Ç':
                    sb.append("&Ccedil;");
                    break;
                case 'é':
                    sb.append("&eacute;");
                    break;
                case 'É':
                    sb.append("&Eacute;");
                    break;
                case 'è':
                    sb.append("&egrave;");
                    break;
                case 'È':
                    sb.append("&Egrave;");
                    break;
                case 'ê':
                    sb.append("&ecirc;");
                    break;
                case 'Ê':
                    sb.append("&Ecirc;");
                    break;
                case 'ë':
                    sb.append("&euml;");
                    break;
                case 'Ë':
                    sb.append("&Euml;");
                    break;
                case 'ï':
                    sb.append("&iuml;");
                    break;
                case 'Ï':
                    sb.append("&Iuml;");
                    break;
                case 'ô':
                    sb.append("&ocirc;");
                    break;
                case 'Ô':
                    sb.append("&Ocirc;");
                    break;
                case 'ö':
                    sb.append("&ouml;");
                    break;
                case 'Ö':
                    sb.append("&Ouml;");
                    break;
                case 'ø':
                    sb.append("&oslash;");
                    break;
                case 'Ø':
                    sb.append("&Oslash;");
                    break;
                case 'ß':
                    sb.append("&szlig;");
                    break;
                case 'ù':
                    sb.append("&ugrave;");
                    break;
                case 'Ù':
                    sb.append("&Ugrave;");
                    break;
                case 'û':
                    sb.append("&ucirc;");
                    break;
                case 'Û':
                    sb.append("&Ucirc;");
                    break;
                case 'ü':
                    sb.append("&uuml;");
                    break;
                case 'Ü':
                    sb.append("&Uuml;");
                    break;
                case '®':
                    sb.append("&reg;");
                    break;
                case '©':
                    sb.append("&copy;");
                    break;
                case '€':
                    sb.append("&euro;");
                    break;
                case '°':
                    sb.append("&deg;");
                    break;
                // be carefull with this one (non-breaking whitee space)
                // case ' ': sb.append("&nbsp;");break;
                default:
                    sb.append(c);
                    break;
                }
            }
        }
        return sb.toString();
    }

    /**
     * Replaces characters that may be confused by an SQL parser with their equivalent escape characters.
     * <p>
     * Any data that will be put in an SQL query should be be escaped. This is especially important for data that comes from untrusted sources such as
     * Internet users.
     * <p>
     * For example if you had the following SQL query:<br>
     * <code>"SELECT * FROM addresses WHERE name='" + name + "' AND private='N'"</code><br>
     * Without this function a user could give <code>" OR 1=1 OR ''='"</code> as their name causing the query to be:<br>
     * <code>"SELECT * FROM addresses WHERE name='' OR 1=1 OR ''='' AND private='N'"</code><br>
     * which will give all addresses, including private ones.<br>
     * Correct usage would be:<br>
     * <code>"SELECT * FROM addresses WHERE name='" + StringUtil.escapeSQL(name) + "' AND private='N'"</code><br>
     * <p>
     * Another way to avoid this problem is to use a PreparedStatement with appropriate placeholders.
     *
     * @param s String to be escaped
     * @return escaped String
     */
    public static String escapeSQL(final String s) {
        if (s == null) {
            return null;
        }
        int length = s.length();
        int newLength = length;
        // first check for characters that might
        // be dangerous and calculate a length
        // of the string that has escapes.
        for (int i = 0; i < length; ++i) {
            char c = s.charAt(i);
            switch (c) {
            case '\\':
            case '\"':
            case '\'':
            case '\0':
                newLength += 1;
                break;
            default:
                break;
            }
        }
        if (length == newLength) {
            // nothing to escape in the string
            return s;
        }
        StringBuffer sb = new StringBuffer(newLength);
        for (int i = 0; i < length; ++i) {
            char c = s.charAt(i);
            switch (c) {
            case '\\':
                sb.append("\\\\");
                break;
            case '\"':
                sb.append("\\\"");
                break;
            case '\'':
                sb.append("\\\'");
                break;
            case '\0':
                sb.append("\\0");
                break;
            default:
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * Replaces characters that are not allowed in a Java style string literal with their escape characters. Specifically quote ("), single quote ('),
     * new line (\n), carriage return (\r), and backslash (\), and tab (\t) are escaped.
     *
     * @param s String to be escaped
     * @return escaped String
     */
    public static String escapeJavaLiteral(final String s) {
        if (s == null) {
            return null;
        }
        int length = s.length();
        int newLength = length;
        // first check for characters that might
        // be dangerous and calculate a length
        // of the string that has escapes.
        for (int i = 0; i < length; ++i) {
            char c = s.charAt(i);
            switch (c) {
            case '\"':
            case '\'':
            case '\n':
            case '\r':
            case '\t':
            case '\\':
                newLength += 1;
                break;
            default:
                break;
            }
        }
        if (length == newLength) {
            // nothing to escape in the string
            return s;
        }
        StringBuffer sb = new StringBuffer(newLength);
        for (int i = 0; i < length; ++i) {
            char c = s.charAt(i);
            switch (c) {
            case '\"':
                sb.append("\\\"");
                break;
            case '\'':
                sb.append("\\\'");
                break;
            case '\n':
                sb.append("\\n");
                break;
            case '\r':
                sb.append("\\r");
                break;
            case '\t':
                sb.append("\\t");
                break;
            case '\\':
                sb.append("\\\\");
                break;
            default:
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * Replaces characters that are not allowed in a style string literal with their escape characters.
     *
     * @param string String to be escaped
     * @param escapeChars Escape characters
     * @return Escaped String
     */
    public static String escapeLiteral(final String string, final String escapeChars) {
        if (string == null) {
            return null;
        }
        int length = string.length();
        int newLength = length;
        // First check for characters that might
        // be dangerous and calculate a length
        // of the string that has escapes.
        for (int i = 0; i < length; ++i) {
            char c = string.charAt(i);
            if (escapeChars.indexOf(c) > -1) {
                newLength += 1;
            }
        }
        // nothing to escape in the string
        if (length == newLength) {
            return string;
        }

        StringBuffer sb = new StringBuffer(newLength);
        for (int i = 0; i < length; ++i) {
            char c = string.charAt(i);
            if (escapeChars.indexOf(c) > -1) {
                sb.append('\\');
            }
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * Replaces escape characters literal ones.
     *
     * @param string String to be transformed
     * @param escapeChars escape characters
     * @return escaped String
     */
    public static String unescapeLiteral(final String string, final String escapeChars) {
        if (string == null) {
            return null;
        }
        int length = string.length();
        int newLength = length;
        // First check for characters that might
        // be dangerous and calculate a length
        // of the string that has escapes.
        for (int i = 0; i < length; i++) {
            char c = string.charAt(i);
            if (escapeChars.indexOf(c) > -1 && i > 0 && string.charAt(i - 1) == '\\') {
                newLength -= 1;
            }
        }
        // nothing to escape in the string
        if (length == newLength) {
            return string;
        }

        StringBuffer sb = new StringBuffer(newLength);
        for (int i = 0; i < length; i++) {
            char c = string.charAt(i);
            if (escapeChars.indexOf(c) > -1 && i > 0 && string.charAt(i - 1) == '\\') {
                sb.deleteCharAt(sb.length() - 1);
            }
            sb.append(c);
        }
        return sb.toString();
    }

    /** Integer representation of lower case character NBSP. */
    public static final int MIN_NBSP = 160;
    /** Integer representation of lower case character IEXCL. */
    public static final int MIN_IEXCL = 161;
    /** Integer representation of lower case character CENT. */
    public static final int MIN_CENT = 162;
    /** Integer representation of lower case character POUND. */
    public static final int MIN_POUND = 163;
    /** Integer representation of lower case character CURREN. */
    public static final int MIN_CURREN = 164;
    /** Integer representation of lower case character YEN. */
    public static final int MIN_YEN = 165;
    /** Integer representation of lower case character BRVBAR. */
    public static final int MIN_BRVBAR = 166;
    /** Integer representation of lower case character SECT. */
    public static final int MIN_SECT = 167;
    /** Integer representation of lower case character UML. */
    public static final int MIN_UML = 168;
    /** Integer representation of lower case character COPY. */
    public static final int MIN_COPY = 169;
    /** Integer representation of lower case character ORDF. */
    public static final int MIN_ORDF = 170;
    /** Integer representation of lower case character LAQUO. */
    public static final int MIN_LAQUO = 171;
    /** Integer representation of lower case character NOT. */
    public static final int MIN_NOT = 172;
    /** Integer representation of lower case character SHY. */
    public static final int MIN_SHY = 173;
    /** Integer representation of lower case character REG. */
    public static final int MIN_REG = 174;
    /** Integer representation of lower case character MACR. */
    public static final int MIN_MACR = 175;
    /** Integer representation of lower case character DEG. */
    public static final int MIN_DEG = 176;
    /** Integer representation of lower case character PLUSMN. */
    public static final int MIN_PLUSMN = 177;
    /** Integer representation of lower case character SUP2. */
    public static final int MIN_SUP2 = 178;
    /** Integer representation of lower case character SUP3. */
    public static final int MIN_SUP3 = 179;
    /** Integer representation of lower case character ACUTE. */
    public static final int MIN_ACUTE = 180;
    /** Integer representation of lower case character MICRO. */
    public static final int MIN_MICRO = 181;
    /** Integer representation of lower case character PARA. */
    public static final int MIN_PARA = 182;
    /** Integer representation of lower case character MIDDOT. */
    public static final int MIN_MIDDOT = 183;
    /** Integer representation of lower case character CEDIL. */
    public static final int MIN_CEDIL = 184;
    /** Integer representation of lower case character SUP1. */
    public static final int MIN_SUP1 = 185;
    /** Integer representation of lower case character ORDM. */
    public static final int MIN_ORDM = 186;
    /** Integer representation of lower case character RAQUO. */
    public static final int MIN_RAQUO = 187;
    /** Integer representation of lower case character FRAC14. */
    public static final int MIN_FRAC14 = 188;
    /** Integer representation of lower case character FRAC12. */
    public static final int MIN_FRAC12 = 189;
    /** Integer representation of lower case character FRAC34. */
    public static final int MIN_FRAC34 = 190;
    /** Integer representation of lower case character IQUEST. */
    public static final int MIN_IQUEST = 191;
    /** Integer representation of lower case character TIMES. */
    public static final int MIN_TIMES = 215;
    /** Integer representation of lower case character SZLIG. */
    public static final int MIN_SZLIG = 223;
    /** Integer representation of lower case character AGRAVE. */
    public static final int MIN_AGRAVE = 224;
    /** Integer representation of lower case character AACUTE. */
    public static final int MIN_AACUTE = 225;
    /** Integer representation of lower case character ACIRC. */
    public static final int MIN_ACIRC = 226;
    /** Integer representation of lower case character ATILDE. */
    public static final int MIN_ATILDE = 227;
    /** Integer representation of lower case character AUML. */
    public static final int MIN_AUML = 228;
    /** Integer representation of lower case character ARING. */
    public static final int MIN_ARING = 229;
    /** Integer representation of lower case character AELIG. */
    public static final int MIN_AELIG = 230;
    /** Integer representation of lower case character CCEDIL. */
    public static final int MIN_CCEDIL = 231;
    /** Integer representation of lower case character EGRAVE. */
    public static final int MIN_EGRAVE = 232;
    /** Integer representation of lower case character EACUTE. */
    public static final int MIN_EACUTE = 233;
    /** Integer representation of lower case character ECIRC. */
    public static final int MIN_ECIRC = 234;
    /** Integer representation of lower case character EUML. */
    public static final int MIN_EUML = 235;
    /** Integer representation of lower case character IGRAVE. */
    public static final int MIN_IGRAVE = 236;
    /** Integer representation of lower case character IACUTE. */
    public static final int MIN_IACUTE = 237;
    /** Integer representation of lower case character ICIRC. */
    public static final int MIN_ICIRC = 238;
    /** Integer representation of lower case character IUML. */
    public static final int MIN_IUML = 239;
    /** Integer representation of lower case character ETH. */
    public static final int MIN_ETH = 240;
    /** Integer representation of lower case character NTILDE. */
    public static final int MIN_NTILDE = 241;
    /** Integer representation of lower case character OGRAVE. */
    public static final int MIN_OGRAVE = 242;
    /** Integer representation of lower case character OACUTE. */
    public static final int MIN_OACUTE = 243;
    /** Integer representation of lower case character OCIRC. */
    public static final int MIN_OCIRC = 244;
    /** Integer representation of lower case character OTILDE. */
    public static final int MIN_OTILDE = 245;
    /** Integer representation of lower case character OUML. */
    public static final int MIN_OUML = 246;
    /** Integer representation of lower case character DIVIDE. */
    public static final int MIN_DIVIDE = 247;
    /** Integer representation of lower case character OSLASH. */
    public static final int MIN_OSLASH = 248;
    /** Integer representation of lower case character UGRAVE. */
    public static final int MIN_UGRAVE = 249;
    /** Integer representation of lower case character UACUTE. */
    public static final int MIN_UACUTE = 250;
    /** Integer representation of lower case character UCIRC. */
    public static final int MIN_UCIRC = 251;
    /** Integer representation of lower case character UUML. */
    public static final int MIN_UUML = 252;
    /** Integer representation of lower case character YACUTE. */
    public static final int MIN_YACUTE = 253;
    /** Integer representation of lower case character THORN. */
    public static final int MIN_THORN = 254;
    /** Integer representation of lower case character YUML. */
    public static final int MIN_YUML = 255;
    /** Integer representation of lower case character FNOF. */
    public static final int MIN_FNOF = 402;
    /** Integer representation of lower case character ALPHA. */
    public static final int MIN_ALPHA = 945;
    /** Integer representation of lower case character BETA. */
    public static final int MIN_BETA = 946;
    /** Integer representation of lower case character GAMMA. */
    public static final int MIN_GAMMA = 947;
    /** Integer representation of lower case character DELTA. */
    public static final int MIN_DELTA = 948;
    /** Integer representation of lower case character EPSILON. */
    public static final int MIN_EPSILON = 949;
    /** Integer representation of lower case character ZETA. */
    public static final int MIN_ZETA = 950;
    /** Integer representation of lower case character ETA. */
    public static final int MIN_ETA = 951;
    /** Integer representation of lower case character THETA. */
    public static final int MIN_THETA = 952;
    /** Integer representation of lower case character IOTA. */
    public static final int MIN_IOTA = 953;
    /** Integer representation of lower case character KAPPA. */
    public static final int MIN_KAPPA = 954;
    /** Integer representation of lower case character LAMBDA. */
    public static final int MIN_LAMBDA = 955;
    /** Integer representation of lower case character MU. */
    public static final int MIN_MU = 956;
    /** Integer representation of lower case character NU. */
    public static final int MIN_NU = 957;
    /** Integer representation of lower case character XI. */
    public static final int MIN_XI = 958;
    /** Integer representation of lower case character OMICRON. */
    public static final int MIN_OMICRON = 959;
    /** Integer representation of lower case character PI. */
    public static final int MIN_PI = 960;
    /** Integer representation of lower case character RHO. */
    public static final int MIN_RHO = 961;
    /** Integer representation of lower case character SIGMAF. */
    public static final int MIN_SIGMAF = 962;
    /** Integer representation of lower case character SIGMA. */
    public static final int MIN_SIGMA = 963;
    /** Integer representation of lower case character TAU. */
    public static final int MIN_TAU = 964;
    /** Integer representation of lower case character UPSILON. */
    public static final int MIN_UPSILON = 965;
    /** Integer representation of lower case character PHI. */
    public static final int MIN_PHI = 966;
    /** Integer representation of lower case character CHI. */
    public static final int MIN_CHI = 967;
    /** Integer representation of lower case character PSI. */
    public static final int MIN_PSI = 968;
    /** Integer representation of lower case character OMEGA. */
    public static final int MIN_OMEGA = 969;
    /** Integer representation of lower case character THETASYM. */
    public static final int MIN_THETASYM = 977;
    /** Integer representation of lower case character UPSIH. */
    public static final int MIN_UPSIH = 978;
    /** Integer representation of lower case character PIV. */
    public static final int MIN_PIV = 982;
    /** Integer representation of lower case character BULL. */
    public static final int MIN_BULL = 8226;
    /** Integer representation of lower case character HELLIP. */
    public static final int MIN_HELLIP = 8230;
    /** Integer representation of lower case character PRIME. */
    public static final int MIN_PRIME = 8242;
    /** Integer representation of lower case character OLINE. */
    public static final int MIN_OLINE = 8254;
    /** Integer representation of lower case character FRASL. */
    public static final int MIN_FRASL = 8260;
    /** Integer representation of lower case character WEIERP. */
    public static final int MIN_WEIERP = 8472;
    /** Integer representation of lower case character IMAGE. */
    public static final int MIN_IMAGE = 8465;
    /** Integer representation of lower case character REAL. */
    public static final int MIN_REAL = 8476;
    /** Integer representation of lower case character TRADE. */
    public static final int MIN_TRADE = 8482;
    /** Integer representation of lower case character ALEFSYM. */
    public static final int MIN_ALEFSYM = 8501;
    /** Integer representation of lower case character LARR. */
    public static final int MIN_LARR = 8592;
    /** Integer representation of lower case character UARR. */
    public static final int MIN_UARR = 8593;
    /** Integer representation of lower case character RARR. */
    public static final int MIN_RARR = 8594;
    /** Integer representation of lower case character DARR. */
    public static final int MIN_DARR = 8595;
    /** Integer representation of lower case character HARR. */
    public static final int MIN_HARR = 8596;
    /** Integer representation of lower case character CRARR. */
    public static final int MIN_CRARR = 8629;
    /** Integer representation of lower case character FORALL. */
    public static final int MIN_FORALL = 8704;
    /** Integer representation of lower case character PART. */
    public static final int MIN_PART = 8706;
    /** Integer representation of lower case character EXIST. */
    public static final int MIN_EXIST = 8707;
    /** Integer representation of lower case character EMPTY. */
    public static final int MIN_EMPTY = 8709;
    /** Integer representation of lower case character NABLA. */
    public static final int MIN_NABLA = 8711;
    /** Integer representation of lower case character ISIN. */
    public static final int MIN_ISIN = 8712;
    /** Integer representation of lower case character NOTIN. */
    public static final int MIN_NOTIN = 8713;
    /** Integer representation of lower case character NI. */
    public static final int MIN_NI = 8715;
    /** Integer representation of lower case character PROD. */
    public static final int MIN_PROD = 8719;
    /** Integer representation of lower case character SUM. */
    public static final int MIN_SUM = 8721;
    /** Integer representation of lower case character MINUS. */
    public static final int MIN_MINUS = 8722;
    /** Integer representation of lower case character LOWAST. */
    public static final int MIN_LOWAST = 8727;
    /** Integer representation of lower case character RADIC. */
    public static final int MIN_RADIC = 8730;
    /** Integer representation of lower case character PROP. */
    public static final int MIN_PROP = 8733;
    /** Integer representation of lower case character INFIN. */
    public static final int MIN_INFIN = 8734;
    /** Integer representation of lower case character ANG. */
    public static final int MIN_ANG = 8736;
    /** Integer representation of lower case character AND. */
    public static final int MIN_AND = 8743;
    /** Integer representation of lower case character OR. */
    public static final int MIN_OR = 8744;
    /** Integer representation of lower case character CAP. */
    public static final int MIN_CAP = 8745;
    /** Integer representation of lower case character CUP. */
    public static final int MIN_CUP = 8746;
    /** Integer representation of lower case character INT. */
    public static final int MIN_INT = 8747;
    /** Integer representation of lower case character THERE4. */
    public static final int MIN_THERE4 = 8756;
    /** Integer representation of lower case character SIM. */
    public static final int MIN_SIM = 8764;
    /** Integer representation of lower case character CONG. */
    public static final int MIN_CONG = 8773;
    /** Integer representation of lower case character ASYMP. */
    public static final int MIN_ASYMP = 8776;
    /** Integer representation of lower case character NE. */
    public static final int MIN_NE = 8800;
    /** Integer representation of lower case character EQUIV. */
    public static final int MIN_EQUIV = 8801;
    /** Integer representation of lower case character LE. */
    public static final int MIN_LE = 8804;
    /** Integer representation of lower case character GE. */
    public static final int MIN_GE = 8805;
    /** Integer representation of lower case character SUB. */
    public static final int MIN_SUB = 8834;
    /** Integer representation of lower case character SUP. */
    public static final int MIN_SUP = 8835;
    /** Integer representation of lower case character NSUB. */
    public static final int MIN_NSUB = 8836;
    /** Integer representation of lower case character SUBE. */
    public static final int MIN_SUBE = 8838;
    /** Integer representation of lower case character SUPE. */
    public static final int MIN_SUPE = 8839;
    /** Integer representation of lower case character OPLUS. */
    public static final int MIN_OPLUS = 8853;
    /** Integer representation of lower case character OTIMES. */
    public static final int MIN_OTIMES = 8855;
    /** Integer representation of lower case character PERP. */
    public static final int MIN_PERP = 8869;
    /** Integer representation of lower case character SDOT. */
    public static final int MIN_SDOT = 8901;
    /** Integer representation of lower case character LCEIL. */
    public static final int MIN_LCEIL = 8968;
    /** Integer representation of lower case character RCEIL. */
    public static final int MIN_RCEIL = 8969;
    /** Integer representation of lower case character LFLOOR. */
    public static final int MIN_LFLOOR = 8970;
    /** Integer representation of lower case character RFLOOR. */
    public static final int MIN_RFLOOR = 8971;
    /** Integer representation of lower case character LANG. */
    public static final int MIN_LANG = 9001;
    /** Integer representation of lower case character RANG. */
    public static final int MIN_RANG = 9002;
    /** Integer representation of lower case character LOZ. */
    public static final int MIN_LOZ = 9674;
    /** Integer representation of lower case character SPADES. */
    public static final int MIN_SPADES = 9824;
    /** Integer representation of lower case character CLUBS. */
    public static final int MIN_CLUBS = 9827;
    /** Integer representation of lower case character HEARTS. */
    public static final int MIN_HEARTS = 9829;
    /** Integer representation of lower case character DIAMS. */
    public static final int MIN_DIAMS = 9830;
    /** Integer representation of lower case character QUOT. */
    public static final int MIN_QUOT = 34;
    /** Integer representation of lower case character AMP. */
    public static final int MIN_AMP = 38;
    /** Integer representation of lower case character LT. */
    public static final int MIN_LT = 60;
    /** Integer representation of lower case character GT. */
    public static final int MIN_GT = 62;
    /** Integer representation of lower case character OELIG. */
    public static final int MIN_OELIG = 339;
    /** Integer representation of lower case character SCARON. */
    public static final int MIN_SCARON = 353;
    /** Integer representation of lower case character CIRC. */
    public static final int MIN_CIRC = 710;
    /** Integer representation of lower case character TILDE. */
    public static final int MIN_TILDE = 732;
    /** Integer representation of lower case character ENSP. */
    public static final int MIN_ENSP = 8194;
    /** Integer representation of lower case character EMSP. */
    public static final int MIN_EMSP = 8195;
    /** Integer representation of lower case character THINSP. */
    public static final int MIN_THINSP = 8201;
    /** Integer representation of lower case character ZWNJ. */
    public static final int MIN_ZWNJ = 8204;
    /** Integer representation of lower case character ZWJ. */
    public static final int MIN_ZWJ = 8205;
    /** Integer representation of lower case character LRM. */
    public static final int MIN_LRM = 8206;
    /** Integer representation of lower case character RLM. */
    public static final int MIN_RLM = 8207;
    /** Integer representation of lower case character NDASH. */
    public static final int MIN_NDASH = 8211;
    /** Integer representation of lower case character MDASH. */
    public static final int MIN_MDASH = 8212;
    /** Integer representation of lower case character LSQUO. */
    public static final int MIN_LSQUO = 8216;
    /** Integer representation of lower case character RSQUO. */
    public static final int MIN_RSQUO = 8217;
    /** Integer representation of lower case character SBQUO. */
    public static final int MIN_SBQUO = 8218;
    /** Integer representation of lower case character LDQUO. */
    public static final int MIN_LDQUO = 8220;
    /** Integer representation of lower case character RDQUO. */
    public static final int MIN_RDQUO = 8221;
    /** Integer representation of lower case character BDQUO. */
    public static final int MIN_BDQUO = 8222;
    /** Integer representation of lower case character DAGGER. */
    public static final int MIN_DAGGER = 8224;
    /** Integer representation of lower case character PERMIL. */
    public static final int MIN_PERMIL = 8240;
    /** Integer representation of lower case character LSAQUO. */
    public static final int MIN_LSAQUO = 8249;
    /** Integer representation of lower case character RSAQUO. */
    public static final int MIN_RSAQUO = 8250;
    /** Integer representation of lower case character EURO. */
    public static final int MIN_EURO = 8364;

    /** Integer representation of upper case character AGRAVE. */
    public static final int MAJ_AGRAVE = 192;
    /** Integer representation of upper case character AACUTE. */
    public static final int MAJ_AACUTE = 193;
    /** Integer representation of upper case character ACIRC. */
    public static final int MAJ_ACIRC = 194;
    /** Integer representation of upper case character ATILDE. */
    public static final int MAJ_ATILDE = 195;
    /** Integer representation of upper case character AUML. */
    public static final int MAJ_AUML = 196;
    /** Integer representation of upper case character ARING. */
    public static final int MAJ_ARING = 197;
    /** Integer representation of upper case character AELIG. */
    public static final int MAJ_AELIG = 198;
    /** Integer representation of upper case character CCEDIL. */
    public static final int MAJ_CCEDIL = 199;
    /** Integer representation of upper case character EGRAVE. */
    public static final int MAJ_EGRAVE = 200;
    /** Integer representation of upper case character EACUTE. */
    public static final int MAJ_EACUTE = 201;
    /** Integer representation of upper case character ECIRC. */
    public static final int MAJ_ECIRC = 202;
    /** Integer representation of upper case character EUML. */
    public static final int MAJ_EUML = 203;
    /** Integer representation of upper case character IGRAVE. */
    public static final int MAJ_IGRAVE = 204;
    /** Integer representation of upper case character IACUTE. */
    public static final int MAJ_IACUTE = 205;
    /** Integer representation of upper case character ICIRC. */
    public static final int MAJ_ICIRC = 206;
    /** Integer representation of upper case character IUML. */
    public static final int MAJ_IUML = 207;
    /** Integer representation of upper case character ETH. */
    public static final int MAJ_ETH = 208;
    /** Integer representation of upper case character NTILDE. */
    public static final int MAJ_NTILDE = 209;
    /** Integer representation of upper case character OGRAVE. */
    public static final int MAJ_OGRAVE = 210;
    /** Integer representation of upper case character OACUTE. */
    public static final int MAJ_OACUTE = 211;
    /** Integer representation of upper case character OCIRC. */
    public static final int MAJ_OCIRC = 212;
    /** Integer representation of upper case character OTILDE. */
    public static final int MAJ_OTILDE = 213;
    /** Integer representation of upper case character OUML. */
    public static final int MAJ_OUML = 214;
    /** Integer representation of upper case character OSLASH. */
    public static final int MAJ_OSLASH = 216;
    /** Integer representation of upper case character UGRAVE. */
    public static final int MAJ_UGRAVE = 217;
    /** Integer representation of upper case character UACUTE. */
    public static final int MAJ_UACUTE = 218;
    /** Integer representation of upper case character UCIRC. */
    public static final int MAJ_UCIRC = 219;
    /** Integer representation of upper case character UUML. */
    public static final int MAJ_UUML = 220;
    /** Integer representation of upper case character YACUTE. */
    public static final int MAJ_YACUTE = 221;
    /** Integer representation of upper case character THORN. */
    public static final int MAJ_THORN = 222;
    /** Integer representation of upper case character ALPHA. */
    public static final int MAJ_ALPHA = 913;
    /** Integer representation of upper case character BETA. */
    public static final int MAJ_BETA = 914;
    /** Integer representation of upper case character GAMMA. */
    public static final int MAJ_GAMMA = 915;
    /** Integer representation of upper case character DELTA. */
    public static final int MAJ_DELTA = 916;
    /** Integer representation of upper case character EPSILON. */
    public static final int MAJ_EPSILON = 917;
    /** Integer representation of upper case character ZETA. */
    public static final int MAJ_ZETA = 918;
    /** Integer representation of upper case character ETA. */
    public static final int MAJ_ETA = 919;
    /** Integer representation of upper case character THETA. */
    public static final int MAJ_THETA = 920;
    /** Integer representation of upper case character IOTA. */
    public static final int MAJ_IOTA = 921;
    /** Integer representation of upper case character KAPPA. */
    public static final int MAJ_KAPPA = 922;
    /** Integer representation of upper case character LAMBDA. */
    public static final int MAJ_LAMBDA = 923;
    /** Integer representation of upper case character MU. */
    public static final int MAJ_MU = 924;
    /** Integer representation of upper case character NU. */
    public static final int MAJ_NU = 925;
    /** Integer representation of upper case character XI. */
    public static final int MAJ_XI = 926;
    /** Integer representation of upper case character OMICRON. */
    public static final int MAJ_OMICRON = 927;
    /** Integer representation of upper case character PI. */
    public static final int MAJ_PI = 928;
    /** Integer representation of upper case character RHO. */
    public static final int MAJ_RHO = 929;
    /** Integer representation of upper case character SIGMA. */
    public static final int MAJ_SIGMA = 931;
    /** Integer representation of upper case character TAU. */
    public static final int MAJ_TAU = 932;
    /** Integer representation of upper case character UPSILON. */
    public static final int MAJ_UPSILON = 933;
    /** Integer representation of upper case character PHI. */
    public static final int MAJ_PHI = 934;
    /** Integer representation of upper case character CHI. */
    public static final int MAJ_CHI = 935;
    /** Integer representation of upper case character PSI. */
    public static final int MAJ_PSI = 936;
    /** Integer representation of upper case character OMEGA. */
    public static final int MAJ_OMEGA = 937;
    /** Integer representation of upper case character PRIME. */
    public static final int MAJ_PRIME = 8243;
    /** Integer representation of upper case character LARR. */
    public static final int MAJ_LARR = 8656;
    /** Integer representation of upper case character UARR. */
    public static final int MAJ_UARR = 8657;
    /** Integer representation of upper case character RARR. */
    public static final int MAJ_RARR = 8658;
    /** Integer representation of upper case character DARR. */
    public static final int MAJ_DARR = 8659;
    /** Integer representation of upper case character HARR. */
    public static final int MAJ_HARR = 8660;
    /** Integer representation of upper case character OELIG. */
    public static final int MAJ_OELIG = 338;
    /** Integer representation of upper case character SCARON. */
    public static final int MAJ_SCARON = 352;
    /** Integer representation of upper case character YUML. */
    public static final int MAJ_YUML = 376;
    /** Integer representation of upper case character DAGGER. */
    public static final int MAJ_DAGGER = 8225;

    /** Html Entities map. */
    public static final Map HTML_ENTITIES = new HashMap();

    static {
        StringEscapeHelper.HTML_ENTITIES.put("aacute", new Integer(StringEscapeHelper.MIN_AACUTE));
        StringEscapeHelper.HTML_ENTITIES.put("acirc", new Integer(StringEscapeHelper.MIN_ACIRC));
        StringEscapeHelper.HTML_ENTITIES.put("acute", new Integer(StringEscapeHelper.MIN_ACUTE));
        StringEscapeHelper.HTML_ENTITIES.put("aelig", new Integer(StringEscapeHelper.MIN_AELIG));
        StringEscapeHelper.HTML_ENTITIES.put("agrave", new Integer(StringEscapeHelper.MIN_AGRAVE));
        StringEscapeHelper.HTML_ENTITIES.put("alefsym", new Integer(StringEscapeHelper.MIN_ALEFSYM));
        StringEscapeHelper.HTML_ENTITIES.put("alpha", new Integer(StringEscapeHelper.MIN_ALPHA));
        StringEscapeHelper.HTML_ENTITIES.put("amp", new Integer(StringEscapeHelper.MIN_AMP));
        StringEscapeHelper.HTML_ENTITIES.put("and", new Integer(StringEscapeHelper.MIN_AND));
        StringEscapeHelper.HTML_ENTITIES.put("ang", new Integer(StringEscapeHelper.MIN_ANG));
        StringEscapeHelper.HTML_ENTITIES.put("aring", new Integer(StringEscapeHelper.MIN_ARING));
        StringEscapeHelper.HTML_ENTITIES.put("asymp", new Integer(StringEscapeHelper.MIN_ASYMP));
        StringEscapeHelper.HTML_ENTITIES.put("atilde", new Integer(StringEscapeHelper.MIN_ATILDE));
        StringEscapeHelper.HTML_ENTITIES.put("auml", new Integer(StringEscapeHelper.MIN_AUML));
        StringEscapeHelper.HTML_ENTITIES.put("bdquo", new Integer(StringEscapeHelper.MIN_BDQUO));
        StringEscapeHelper.HTML_ENTITIES.put("beta", new Integer(StringEscapeHelper.MIN_BETA));
        StringEscapeHelper.HTML_ENTITIES.put("brvbar", new Integer(StringEscapeHelper.MIN_BRVBAR));
        StringEscapeHelper.HTML_ENTITIES.put("bull", new Integer(StringEscapeHelper.MIN_BULL));
        StringEscapeHelper.HTML_ENTITIES.put("cap", new Integer(StringEscapeHelper.MIN_CAP));
        StringEscapeHelper.HTML_ENTITIES.put("ccedil", new Integer(StringEscapeHelper.MIN_CCEDIL));
        StringEscapeHelper.HTML_ENTITIES.put("cedil", new Integer(StringEscapeHelper.MIN_CEDIL));
        StringEscapeHelper.HTML_ENTITIES.put("cent", new Integer(StringEscapeHelper.MIN_CENT));
        StringEscapeHelper.HTML_ENTITIES.put("chi", new Integer(StringEscapeHelper.MIN_CHI));
        StringEscapeHelper.HTML_ENTITIES.put("circ", new Integer(StringEscapeHelper.MIN_CIRC));
        StringEscapeHelper.HTML_ENTITIES.put("clubs", new Integer(StringEscapeHelper.MIN_CLUBS));
        StringEscapeHelper.HTML_ENTITIES.put("cong", new Integer(StringEscapeHelper.MIN_CONG));
        StringEscapeHelper.HTML_ENTITIES.put("copy", new Integer(StringEscapeHelper.MIN_COPY));
        StringEscapeHelper.HTML_ENTITIES.put("crarr", new Integer(StringEscapeHelper.MIN_CRARR));
        StringEscapeHelper.HTML_ENTITIES.put("cup", new Integer(StringEscapeHelper.MIN_CUP));
        StringEscapeHelper.HTML_ENTITIES.put("curren", new Integer(StringEscapeHelper.MIN_CURREN));
        StringEscapeHelper.HTML_ENTITIES.put("dagger", new Integer(StringEscapeHelper.MIN_DAGGER));
        StringEscapeHelper.HTML_ENTITIES.put("darr", new Integer(StringEscapeHelper.MIN_DARR));
        StringEscapeHelper.HTML_ENTITIES.put("deg", new Integer(StringEscapeHelper.MIN_DEG));
        StringEscapeHelper.HTML_ENTITIES.put("delta", new Integer(StringEscapeHelper.MIN_DELTA));
        StringEscapeHelper.HTML_ENTITIES.put("diams", new Integer(StringEscapeHelper.MIN_DIAMS));
        StringEscapeHelper.HTML_ENTITIES.put("divide", new Integer(StringEscapeHelper.MIN_DIVIDE));
        StringEscapeHelper.HTML_ENTITIES.put("eacute", new Integer(StringEscapeHelper.MIN_EACUTE));
        StringEscapeHelper.HTML_ENTITIES.put("ecirc", new Integer(StringEscapeHelper.MIN_ECIRC));
        StringEscapeHelper.HTML_ENTITIES.put("egrave", new Integer(StringEscapeHelper.MIN_EGRAVE));
        StringEscapeHelper.HTML_ENTITIES.put("empty", new Integer(StringEscapeHelper.MIN_EMPTY));
        StringEscapeHelper.HTML_ENTITIES.put("emsp", new Integer(StringEscapeHelper.MIN_EMSP));
        StringEscapeHelper.HTML_ENTITIES.put("ensp", new Integer(StringEscapeHelper.MIN_ENSP));
        StringEscapeHelper.HTML_ENTITIES.put("epsilon", new Integer(StringEscapeHelper.MIN_EPSILON));
        StringEscapeHelper.HTML_ENTITIES.put("equiv", new Integer(StringEscapeHelper.MIN_EQUIV));
        StringEscapeHelper.HTML_ENTITIES.put("eta", new Integer(StringEscapeHelper.MIN_ETA));
        StringEscapeHelper.HTML_ENTITIES.put("eth", new Integer(StringEscapeHelper.MIN_ETH));
        StringEscapeHelper.HTML_ENTITIES.put("euml", new Integer(StringEscapeHelper.MIN_EUML));
        StringEscapeHelper.HTML_ENTITIES.put("euro", new Integer(StringEscapeHelper.MIN_EURO));
        StringEscapeHelper.HTML_ENTITIES.put("exist", new Integer(StringEscapeHelper.MIN_EXIST));
        StringEscapeHelper.HTML_ENTITIES.put("fnof", new Integer(StringEscapeHelper.MIN_FNOF));
        StringEscapeHelper.HTML_ENTITIES.put("forall", new Integer(StringEscapeHelper.MIN_FORALL));
        StringEscapeHelper.HTML_ENTITIES.put("frac12", new Integer(StringEscapeHelper.MIN_FRAC12));
        StringEscapeHelper.HTML_ENTITIES.put("frac14", new Integer(StringEscapeHelper.MIN_FRAC14));
        StringEscapeHelper.HTML_ENTITIES.put("frac34", new Integer(StringEscapeHelper.MIN_FRAC34));
        StringEscapeHelper.HTML_ENTITIES.put("frasl", new Integer(StringEscapeHelper.MIN_FRASL));
        StringEscapeHelper.HTML_ENTITIES.put("gamma", new Integer(StringEscapeHelper.MIN_GAMMA));
        StringEscapeHelper.HTML_ENTITIES.put("ge", new Integer(StringEscapeHelper.MIN_GE));
        StringEscapeHelper.HTML_ENTITIES.put("gt", new Integer(StringEscapeHelper.MIN_GT));
        StringEscapeHelper.HTML_ENTITIES.put("harr", new Integer(StringEscapeHelper.MIN_HARR));
        StringEscapeHelper.HTML_ENTITIES.put("hearts", new Integer(StringEscapeHelper.MIN_HEARTS));
        StringEscapeHelper.HTML_ENTITIES.put("hellip", new Integer(StringEscapeHelper.MIN_HELLIP));
        StringEscapeHelper.HTML_ENTITIES.put("iacute", new Integer(StringEscapeHelper.MIN_IACUTE));
        StringEscapeHelper.HTML_ENTITIES.put("icirc", new Integer(StringEscapeHelper.MIN_ICIRC));
        StringEscapeHelper.HTML_ENTITIES.put("iexcl", new Integer(StringEscapeHelper.MIN_IEXCL));
        StringEscapeHelper.HTML_ENTITIES.put("igrave", new Integer(StringEscapeHelper.MIN_IGRAVE));
        StringEscapeHelper.HTML_ENTITIES.put("image", new Integer(StringEscapeHelper.MIN_IMAGE));
        StringEscapeHelper.HTML_ENTITIES.put("infin", new Integer(StringEscapeHelper.MIN_INFIN));
        StringEscapeHelper.HTML_ENTITIES.put("int", new Integer(StringEscapeHelper.MIN_INT));
        StringEscapeHelper.HTML_ENTITIES.put("iota", new Integer(StringEscapeHelper.MIN_IOTA));
        StringEscapeHelper.HTML_ENTITIES.put("iquest", new Integer(StringEscapeHelper.MIN_IQUEST));
        StringEscapeHelper.HTML_ENTITIES.put("isin", new Integer(StringEscapeHelper.MIN_ISIN));
        StringEscapeHelper.HTML_ENTITIES.put("iuml", new Integer(StringEscapeHelper.MIN_IUML));
        StringEscapeHelper.HTML_ENTITIES.put("kappa", new Integer(StringEscapeHelper.MIN_KAPPA));
        StringEscapeHelper.HTML_ENTITIES.put("lambda", new Integer(StringEscapeHelper.MIN_LAMBDA));
        StringEscapeHelper.HTML_ENTITIES.put("lang", new Integer(StringEscapeHelper.MIN_LANG));
        StringEscapeHelper.HTML_ENTITIES.put("laquo", new Integer(StringEscapeHelper.MIN_LAQUO));
        StringEscapeHelper.HTML_ENTITIES.put("larr", new Integer(StringEscapeHelper.MIN_LARR));
        StringEscapeHelper.HTML_ENTITIES.put("lceil", new Integer(StringEscapeHelper.MIN_LCEIL));
        StringEscapeHelper.HTML_ENTITIES.put("ldquo", new Integer(StringEscapeHelper.MIN_LDQUO));
        StringEscapeHelper.HTML_ENTITIES.put("le", new Integer(StringEscapeHelper.MIN_LE));
        StringEscapeHelper.HTML_ENTITIES.put("lfloor", new Integer(StringEscapeHelper.MIN_LFLOOR));
        StringEscapeHelper.HTML_ENTITIES.put("lowast", new Integer(StringEscapeHelper.MIN_LOWAST));
        StringEscapeHelper.HTML_ENTITIES.put("loz", new Integer(StringEscapeHelper.MIN_LOZ));
        StringEscapeHelper.HTML_ENTITIES.put("lrm", new Integer(StringEscapeHelper.MIN_LRM));
        StringEscapeHelper.HTML_ENTITIES.put("lsaquo", new Integer(StringEscapeHelper.MIN_LSAQUO));
        StringEscapeHelper.HTML_ENTITIES.put("lsquo", new Integer(StringEscapeHelper.MIN_LSQUO));
        StringEscapeHelper.HTML_ENTITIES.put("lt", new Integer(StringEscapeHelper.MIN_LT));
        StringEscapeHelper.HTML_ENTITIES.put("macr", new Integer(StringEscapeHelper.MIN_MACR));
        StringEscapeHelper.HTML_ENTITIES.put("mdash", new Integer(StringEscapeHelper.MIN_MDASH));
        StringEscapeHelper.HTML_ENTITIES.put("micro", new Integer(StringEscapeHelper.MIN_MICRO));
        StringEscapeHelper.HTML_ENTITIES.put("middot", new Integer(StringEscapeHelper.MIN_MIDDOT));
        StringEscapeHelper.HTML_ENTITIES.put("minus", new Integer(StringEscapeHelper.MIN_MINUS));
        StringEscapeHelper.HTML_ENTITIES.put("mu", new Integer(StringEscapeHelper.MIN_MU));
        StringEscapeHelper.HTML_ENTITIES.put("nabla", new Integer(StringEscapeHelper.MIN_NABLA));
        StringEscapeHelper.HTML_ENTITIES.put("nbsp", new Integer(StringEscapeHelper.MIN_NBSP));
        StringEscapeHelper.HTML_ENTITIES.put("ndash", new Integer(StringEscapeHelper.MIN_NDASH));
        StringEscapeHelper.HTML_ENTITIES.put("ne", new Integer(StringEscapeHelper.MIN_NE));
        StringEscapeHelper.HTML_ENTITIES.put("ni", new Integer(StringEscapeHelper.MIN_NI));
        StringEscapeHelper.HTML_ENTITIES.put("not", new Integer(StringEscapeHelper.MIN_NOT));
        StringEscapeHelper.HTML_ENTITIES.put("notin", new Integer(StringEscapeHelper.MIN_NOTIN));
        StringEscapeHelper.HTML_ENTITIES.put("nsub", new Integer(StringEscapeHelper.MIN_NSUB));
        StringEscapeHelper.HTML_ENTITIES.put("ntilde", new Integer(StringEscapeHelper.MIN_NTILDE));
        StringEscapeHelper.HTML_ENTITIES.put("nu", new Integer(StringEscapeHelper.MIN_NU));
        StringEscapeHelper.HTML_ENTITIES.put("oacute", new Integer(StringEscapeHelper.MIN_OACUTE));
        StringEscapeHelper.HTML_ENTITIES.put("ocirc", new Integer(StringEscapeHelper.MIN_OCIRC));
        StringEscapeHelper.HTML_ENTITIES.put("oelig", new Integer(StringEscapeHelper.MIN_OELIG));
        StringEscapeHelper.HTML_ENTITIES.put("ograve", new Integer(StringEscapeHelper.MIN_OGRAVE));
        StringEscapeHelper.HTML_ENTITIES.put("oline", new Integer(StringEscapeHelper.MIN_OLINE));
        StringEscapeHelper.HTML_ENTITIES.put("omega", new Integer(StringEscapeHelper.MIN_OMEGA));
        StringEscapeHelper.HTML_ENTITIES.put("omicron", new Integer(StringEscapeHelper.MIN_OMICRON));
        StringEscapeHelper.HTML_ENTITIES.put("oplus", new Integer(StringEscapeHelper.MIN_OPLUS));
        StringEscapeHelper.HTML_ENTITIES.put("or", new Integer(StringEscapeHelper.MIN_OR));
        StringEscapeHelper.HTML_ENTITIES.put("ordf", new Integer(StringEscapeHelper.MIN_ORDF));
        StringEscapeHelper.HTML_ENTITIES.put("ordm", new Integer(StringEscapeHelper.MIN_ORDM));
        StringEscapeHelper.HTML_ENTITIES.put("oslash", new Integer(StringEscapeHelper.MIN_OSLASH));
        StringEscapeHelper.HTML_ENTITIES.put("otilde", new Integer(StringEscapeHelper.MIN_OTILDE));
        StringEscapeHelper.HTML_ENTITIES.put("otimes", new Integer(StringEscapeHelper.MIN_OTIMES));
        StringEscapeHelper.HTML_ENTITIES.put("ouml", new Integer(StringEscapeHelper.MIN_OUML));
        StringEscapeHelper.HTML_ENTITIES.put("para", new Integer(StringEscapeHelper.MIN_PARA));
        StringEscapeHelper.HTML_ENTITIES.put("part", new Integer(StringEscapeHelper.MIN_PART));
        StringEscapeHelper.HTML_ENTITIES.put("permil", new Integer(StringEscapeHelper.MIN_PERMIL));
        StringEscapeHelper.HTML_ENTITIES.put("perp", new Integer(StringEscapeHelper.MIN_PERP));
        StringEscapeHelper.HTML_ENTITIES.put("phi", new Integer(StringEscapeHelper.MIN_PHI));
        StringEscapeHelper.HTML_ENTITIES.put("pi", new Integer(StringEscapeHelper.MIN_PI));
        StringEscapeHelper.HTML_ENTITIES.put("piv", new Integer(StringEscapeHelper.MIN_PIV));
        StringEscapeHelper.HTML_ENTITIES.put("plusmn", new Integer(StringEscapeHelper.MIN_PLUSMN));
        StringEscapeHelper.HTML_ENTITIES.put("pound", new Integer(StringEscapeHelper.MIN_POUND));
        StringEscapeHelper.HTML_ENTITIES.put("prime", new Integer(StringEscapeHelper.MIN_PRIME));
        StringEscapeHelper.HTML_ENTITIES.put("prod", new Integer(StringEscapeHelper.MIN_PROD));
        StringEscapeHelper.HTML_ENTITIES.put("prop", new Integer(StringEscapeHelper.MIN_PROP));
        StringEscapeHelper.HTML_ENTITIES.put("psi", new Integer(StringEscapeHelper.MIN_PSI));
        StringEscapeHelper.HTML_ENTITIES.put("quot", new Integer(StringEscapeHelper.MIN_QUOT));
        StringEscapeHelper.HTML_ENTITIES.put("radic", new Integer(StringEscapeHelper.MIN_RADIC));
        StringEscapeHelper.HTML_ENTITIES.put("rang", new Integer(StringEscapeHelper.MIN_RANG));
        StringEscapeHelper.HTML_ENTITIES.put("raquo", new Integer(StringEscapeHelper.MIN_RAQUO));
        StringEscapeHelper.HTML_ENTITIES.put("rarr", new Integer(StringEscapeHelper.MIN_RARR));
        StringEscapeHelper.HTML_ENTITIES.put("rceil", new Integer(StringEscapeHelper.MIN_RCEIL));
        StringEscapeHelper.HTML_ENTITIES.put("rdquo", new Integer(StringEscapeHelper.MIN_RDQUO));
        StringEscapeHelper.HTML_ENTITIES.put("real", new Integer(StringEscapeHelper.MIN_REAL));
        StringEscapeHelper.HTML_ENTITIES.put("reg", new Integer(StringEscapeHelper.MIN_REG));
        StringEscapeHelper.HTML_ENTITIES.put("rfloor", new Integer(StringEscapeHelper.MIN_RFLOOR));
        StringEscapeHelper.HTML_ENTITIES.put("rho", new Integer(StringEscapeHelper.MIN_RHO));
        StringEscapeHelper.HTML_ENTITIES.put("rlm", new Integer(StringEscapeHelper.MIN_RLM));
        StringEscapeHelper.HTML_ENTITIES.put("rsaquo", new Integer(StringEscapeHelper.MIN_RSAQUO));
        StringEscapeHelper.HTML_ENTITIES.put("rsquo", new Integer(StringEscapeHelper.MIN_RSQUO));
        StringEscapeHelper.HTML_ENTITIES.put("sbquo", new Integer(StringEscapeHelper.MIN_SBQUO));
        StringEscapeHelper.HTML_ENTITIES.put("scaron", new Integer(StringEscapeHelper.MIN_SCARON));
        StringEscapeHelper.HTML_ENTITIES.put("sdot", new Integer(StringEscapeHelper.MIN_SDOT));
        StringEscapeHelper.HTML_ENTITIES.put("sect", new Integer(StringEscapeHelper.MIN_SECT));
        StringEscapeHelper.HTML_ENTITIES.put("shy", new Integer(StringEscapeHelper.MIN_SHY));
        StringEscapeHelper.HTML_ENTITIES.put("sigma", new Integer(StringEscapeHelper.MIN_SIGMA));
        StringEscapeHelper.HTML_ENTITIES.put("sigmaf", new Integer(StringEscapeHelper.MIN_SIGMAF));
        StringEscapeHelper.HTML_ENTITIES.put("sim", new Integer(StringEscapeHelper.MIN_SIM));
        StringEscapeHelper.HTML_ENTITIES.put("spades", new Integer(StringEscapeHelper.MIN_SPADES));
        StringEscapeHelper.HTML_ENTITIES.put("sub", new Integer(StringEscapeHelper.MIN_SUB));
        StringEscapeHelper.HTML_ENTITIES.put("sube", new Integer(StringEscapeHelper.MIN_SUBE));
        StringEscapeHelper.HTML_ENTITIES.put("sum", new Integer(StringEscapeHelper.MIN_SUM));
        StringEscapeHelper.HTML_ENTITIES.put("sup", new Integer(StringEscapeHelper.MIN_SUP));
        StringEscapeHelper.HTML_ENTITIES.put("sup1", new Integer(StringEscapeHelper.MIN_SUP1));
        StringEscapeHelper.HTML_ENTITIES.put("sup2", new Integer(StringEscapeHelper.MIN_SUP2));
        StringEscapeHelper.HTML_ENTITIES.put("sup3", new Integer(StringEscapeHelper.MIN_SUP3));
        StringEscapeHelper.HTML_ENTITIES.put("supe", new Integer(StringEscapeHelper.MIN_SUPE));
        StringEscapeHelper.HTML_ENTITIES.put("szlig", new Integer(StringEscapeHelper.MIN_SZLIG));
        StringEscapeHelper.HTML_ENTITIES.put("tau", new Integer(StringEscapeHelper.MIN_TAU));
        StringEscapeHelper.HTML_ENTITIES.put("there4", new Integer(StringEscapeHelper.MIN_THERE4));
        StringEscapeHelper.HTML_ENTITIES.put("theta", new Integer(StringEscapeHelper.MIN_THETA));
        StringEscapeHelper.HTML_ENTITIES.put("thetasym", new Integer(StringEscapeHelper.MIN_THETASYM));
        StringEscapeHelper.HTML_ENTITIES.put("thinsp", new Integer(StringEscapeHelper.MIN_THINSP));
        StringEscapeHelper.HTML_ENTITIES.put("thorn", new Integer(StringEscapeHelper.MIN_THORN));
        StringEscapeHelper.HTML_ENTITIES.put("tilde", new Integer(StringEscapeHelper.MIN_TILDE));
        StringEscapeHelper.HTML_ENTITIES.put("times", new Integer(StringEscapeHelper.MIN_TIMES));
        StringEscapeHelper.HTML_ENTITIES.put("trade", new Integer(StringEscapeHelper.MIN_TRADE));
        StringEscapeHelper.HTML_ENTITIES.put("uacute", new Integer(StringEscapeHelper.MIN_UACUTE));
        StringEscapeHelper.HTML_ENTITIES.put("uarr", new Integer(StringEscapeHelper.MIN_UARR));
        StringEscapeHelper.HTML_ENTITIES.put("ucirc", new Integer(StringEscapeHelper.MIN_UCIRC));
        StringEscapeHelper.HTML_ENTITIES.put("ugrave", new Integer(StringEscapeHelper.MIN_UGRAVE));
        StringEscapeHelper.HTML_ENTITIES.put("uml", new Integer(StringEscapeHelper.MIN_UML));
        StringEscapeHelper.HTML_ENTITIES.put("upsih", new Integer(StringEscapeHelper.MIN_UPSIH));
        StringEscapeHelper.HTML_ENTITIES.put("upsilon", new Integer(StringEscapeHelper.MIN_UPSILON));
        StringEscapeHelper.HTML_ENTITIES.put("uuml", new Integer(StringEscapeHelper.MIN_UUML));
        StringEscapeHelper.HTML_ENTITIES.put("weierp", new Integer(StringEscapeHelper.MIN_WEIERP));
        StringEscapeHelper.HTML_ENTITIES.put("xi", new Integer(StringEscapeHelper.MIN_XI));
        StringEscapeHelper.HTML_ENTITIES.put("yacute", new Integer(StringEscapeHelper.MIN_YACUTE));
        StringEscapeHelper.HTML_ENTITIES.put("yen", new Integer(StringEscapeHelper.MIN_YEN));
        StringEscapeHelper.HTML_ENTITIES.put("yuml", new Integer(StringEscapeHelper.MIN_YUML));
        StringEscapeHelper.HTML_ENTITIES.put("zeta", new Integer(StringEscapeHelper.MIN_ZETA));
        StringEscapeHelper.HTML_ENTITIES.put("zwj", new Integer(StringEscapeHelper.MIN_ZWJ));
        StringEscapeHelper.HTML_ENTITIES.put("zwnj", new Integer(StringEscapeHelper.MIN_ZWNJ));

        StringEscapeHelper.HTML_ENTITIES.put("Aacute", new Integer(StringEscapeHelper.MAJ_AACUTE));
        StringEscapeHelper.HTML_ENTITIES.put("Acirc", new Integer(StringEscapeHelper.MAJ_ACIRC));
        StringEscapeHelper.HTML_ENTITIES.put("AElig", new Integer(StringEscapeHelper.MAJ_AELIG));
        StringEscapeHelper.HTML_ENTITIES.put("Agrave", new Integer(StringEscapeHelper.MAJ_AGRAVE));
        StringEscapeHelper.HTML_ENTITIES.put("Alpha", new Integer(StringEscapeHelper.MAJ_ALPHA));
        StringEscapeHelper.HTML_ENTITIES.put("Aring", new Integer(StringEscapeHelper.MAJ_ARING));
        StringEscapeHelper.HTML_ENTITIES.put("Atilde", new Integer(StringEscapeHelper.MAJ_ATILDE));
        StringEscapeHelper.HTML_ENTITIES.put("Auml", new Integer(StringEscapeHelper.MAJ_AUML));
        StringEscapeHelper.HTML_ENTITIES.put("Beta", new Integer(StringEscapeHelper.MAJ_BETA));
        StringEscapeHelper.HTML_ENTITIES.put("Ccedil", new Integer(StringEscapeHelper.MAJ_CCEDIL));
        StringEscapeHelper.HTML_ENTITIES.put("Chi", new Integer(StringEscapeHelper.MAJ_CHI));
        StringEscapeHelper.HTML_ENTITIES.put("Dagger", new Integer(StringEscapeHelper.MAJ_DAGGER));
        StringEscapeHelper.HTML_ENTITIES.put("dArr", new Integer(StringEscapeHelper.MAJ_DARR));
        StringEscapeHelper.HTML_ENTITIES.put("Delta", new Integer(StringEscapeHelper.MAJ_DELTA));
        StringEscapeHelper.HTML_ENTITIES.put("Eacute", new Integer(StringEscapeHelper.MAJ_EACUTE));
        StringEscapeHelper.HTML_ENTITIES.put("Ecirc", new Integer(StringEscapeHelper.MAJ_ECIRC));
        StringEscapeHelper.HTML_ENTITIES.put("Egrave", new Integer(StringEscapeHelper.MAJ_EGRAVE));
        StringEscapeHelper.HTML_ENTITIES.put("Epsilon", new Integer(StringEscapeHelper.MAJ_EPSILON));
        StringEscapeHelper.HTML_ENTITIES.put("Eta", new Integer(StringEscapeHelper.MAJ_ETA));
        StringEscapeHelper.HTML_ENTITIES.put("ETH", new Integer(StringEscapeHelper.MAJ_ETH));
        StringEscapeHelper.HTML_ENTITIES.put("Euml", new Integer(StringEscapeHelper.MAJ_EUML));
        StringEscapeHelper.HTML_ENTITIES.put("Gamma", new Integer(StringEscapeHelper.MAJ_GAMMA));
        StringEscapeHelper.HTML_ENTITIES.put("hArr", new Integer(StringEscapeHelper.MAJ_HARR));
        StringEscapeHelper.HTML_ENTITIES.put("Iacute", new Integer(StringEscapeHelper.MAJ_IACUTE));
        StringEscapeHelper.HTML_ENTITIES.put("Icirc", new Integer(StringEscapeHelper.MAJ_ICIRC));
        StringEscapeHelper.HTML_ENTITIES.put("Igrave", new Integer(StringEscapeHelper.MAJ_IGRAVE));
        StringEscapeHelper.HTML_ENTITIES.put("Iota", new Integer(StringEscapeHelper.MAJ_IOTA));
        StringEscapeHelper.HTML_ENTITIES.put("Iuml", new Integer(StringEscapeHelper.MAJ_IUML));
        StringEscapeHelper.HTML_ENTITIES.put("Kappa", new Integer(StringEscapeHelper.MAJ_KAPPA));
        StringEscapeHelper.HTML_ENTITIES.put("Lambda", new Integer(StringEscapeHelper.MAJ_LAMBDA));
        StringEscapeHelper.HTML_ENTITIES.put("lArr", new Integer(StringEscapeHelper.MAJ_LARR));
        StringEscapeHelper.HTML_ENTITIES.put("Mu", new Integer(StringEscapeHelper.MAJ_MU));
        StringEscapeHelper.HTML_ENTITIES.put("Ntilde", new Integer(StringEscapeHelper.MAJ_NTILDE));
        StringEscapeHelper.HTML_ENTITIES.put("Nu", new Integer(StringEscapeHelper.MAJ_NU));
        StringEscapeHelper.HTML_ENTITIES.put("Oacute", new Integer(StringEscapeHelper.MAJ_OACUTE));
        StringEscapeHelper.HTML_ENTITIES.put("Ocirc", new Integer(StringEscapeHelper.MAJ_OCIRC));
        StringEscapeHelper.HTML_ENTITIES.put("OElig", new Integer(StringEscapeHelper.MAJ_OELIG));
        StringEscapeHelper.HTML_ENTITIES.put("Ograve", new Integer(StringEscapeHelper.MAJ_OGRAVE));
        StringEscapeHelper.HTML_ENTITIES.put("Omega", new Integer(StringEscapeHelper.MAJ_OMEGA));
        StringEscapeHelper.HTML_ENTITIES.put("Omicron", new Integer(StringEscapeHelper.MAJ_OMICRON));
        StringEscapeHelper.HTML_ENTITIES.put("Oslash", new Integer(StringEscapeHelper.MAJ_OSLASH));
        StringEscapeHelper.HTML_ENTITIES.put("Otilde", new Integer(StringEscapeHelper.MAJ_OTILDE));
        StringEscapeHelper.HTML_ENTITIES.put("Ouml", new Integer(StringEscapeHelper.MAJ_OUML));
        StringEscapeHelper.HTML_ENTITIES.put("Phi", new Integer(StringEscapeHelper.MAJ_PHI));
        StringEscapeHelper.HTML_ENTITIES.put("Pi", new Integer(StringEscapeHelper.MAJ_PI));
        StringEscapeHelper.HTML_ENTITIES.put("Prime", new Integer(StringEscapeHelper.MAJ_PRIME));
        StringEscapeHelper.HTML_ENTITIES.put("Psi", new Integer(StringEscapeHelper.MAJ_PSI));
        StringEscapeHelper.HTML_ENTITIES.put("rArr", new Integer(StringEscapeHelper.MAJ_RARR));
        StringEscapeHelper.HTML_ENTITIES.put("Rho", new Integer(StringEscapeHelper.MAJ_RHO));
        StringEscapeHelper.HTML_ENTITIES.put("Scaron", new Integer(StringEscapeHelper.MAJ_SCARON));
        StringEscapeHelper.HTML_ENTITIES.put("Sigma", new Integer(StringEscapeHelper.MAJ_SIGMA));
        StringEscapeHelper.HTML_ENTITIES.put("Tau", new Integer(StringEscapeHelper.MAJ_TAU));
        StringEscapeHelper.HTML_ENTITIES.put("Theta", new Integer(StringEscapeHelper.MAJ_THETA));
        StringEscapeHelper.HTML_ENTITIES.put("THORN", new Integer(StringEscapeHelper.MAJ_THORN));
        StringEscapeHelper.HTML_ENTITIES.put("Uacute", new Integer(StringEscapeHelper.MAJ_UACUTE));
        StringEscapeHelper.HTML_ENTITIES.put("uArr", new Integer(StringEscapeHelper.MAJ_UARR));
        StringEscapeHelper.HTML_ENTITIES.put("Ucirc", new Integer(StringEscapeHelper.MAJ_UCIRC));
        StringEscapeHelper.HTML_ENTITIES.put("Ugrave", new Integer(StringEscapeHelper.MAJ_UGRAVE));
        StringEscapeHelper.HTML_ENTITIES.put("Upsilon", new Integer(StringEscapeHelper.MAJ_UPSILON));
        StringEscapeHelper.HTML_ENTITIES.put("Uuml", new Integer(StringEscapeHelper.MAJ_UUML));
        StringEscapeHelper.HTML_ENTITIES.put("Xi", new Integer(StringEscapeHelper.MAJ_XI));
        StringEscapeHelper.HTML_ENTITIES.put("Yacute", new Integer(StringEscapeHelper.MAJ_YACUTE));
        StringEscapeHelper.HTML_ENTITIES.put("Yuml", new Integer(StringEscapeHelper.MAJ_YUML));
        StringEscapeHelper.HTML_ENTITIES.put("Zeta", new Integer(StringEscapeHelper.MAJ_ZETA));
    }

    /**
     * Turn any HTML escape entities in the string into characters and return the resulting string.
     *
     * @param s String to be unescaped.
     * @return unescaped String.
     */
    public static String unescapeHTML(final String s) {
        if (s == null) {
            return null;
        }
        StringBuffer result = new StringBuffer(s.length());
        int ampInd = s.indexOf("&");
        int lastEnd = 0;
        while (ampInd >= 0) {
            int nextAmp = s.indexOf("&", ampInd + 1);
            int nextSemi = s.indexOf(";", ampInd + 1);
            if (nextSemi != -1 && (nextAmp == -1 || nextSemi < nextAmp)) {
                int value = -1;
                String escape = s.substring(ampInd + 1, nextSemi);
                try {
                    if (escape.startsWith("#")) {
                        value = Integer.parseInt(escape.substring(1), StringEscapeHelper.RADIX);
                    } else {
                        if (StringEscapeHelper.HTML_ENTITIES.containsKey(escape)) {
                            value = ((Integer) StringEscapeHelper.HTML_ENTITIES.get(escape)).intValue();
                        }
                    }
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                }
                result.append(s.substring(lastEnd, ampInd));
                lastEnd = nextSemi + 1;
                if (value >= 0 && value <= StringEscapeHelper.MASK) {
                    result.append((char) value);
                } else {
                    result.append("&").append(escape).append(";");
                }
            }
            ampInd = nextAmp;
        }
        result.append(s.substring(lastEnd));
        return result.toString();
    }

    /**
     * Turn any HTML escape entities in the string into characters and return the resulting string.
     *
     * @param s String to be unescaped.
     * @return unescaped String.
     */
    public static String unescapeXML(final String s) {
        return StringEscapeHelper.unescapeHTML(s);
    }

    /**
     * Escape Double quotes.
     * @param s The string to be escaped.
     * @return The escaped string.
     */
    public static String escapeXML(final String s) {
        if (s == null) {
            return null;
        }
        // Optimize by checking if parse is needed
        if (s.indexOf('>') == -1 && s.indexOf('<') == -1 && s.indexOf('&') == -1 && s.indexOf('\'') == -1 && s.indexOf('"') == -1) {
            return s;
        }
        int length = s.length();
        int newLength = length;
        // first check for characters that might
        // be dangerous and calculate a length
        // of the string that has escapes.
        for (int i = 0; i < length; ++i) {
            char c = s.charAt(i);
            switch (c) {
            case '<':
                newLength += "&lt".length();
                break;
            case '>':
                newLength += "&gt".length();
                break;
            case '&':
                newLength += "&amp".length();
                break;
            case '\'':
                newLength += "&apos".length();
                break;
            case '\"':
                newLength += "&quot".length();
                break;
            default:
                break;
            }
        }
        if (length == newLength) {
            // nothing to escape in the string
            return s;
        }
        StringBuffer sb = new StringBuffer(newLength);
        for (int i = 0; i < length; ++i) {
            char c = s.charAt(i);
            switch (c) {
            case '\'':
                sb.append("&apos;");
                break;
            case '\"':
                sb.append("&quot;");
                break;
            case '<':
                sb.append("&lt;");
                break;
            case '>':
                sb.append("&gt;");
                break;
            case '&':
                sb.append("&amp;");
                break;
            default:
                int cint = StringEscapeHelper.MASK & c;
                if (cint < StringEscapeHelper.CHAR) {
                    switch (c) {
                    case '\r':
                    case '\n':
                    case '\t':
                    case '\f':
                        sb.append(c);
                        break;
                    default:
                    }
                } else {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }
}
