/**
 *
 * Copyright (c) 2014, the Railo Company Ltd. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * 
 **/
package org.lucee.extension.esapi.functions;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Encoder;
import org.owasp.esapi.codecs.Codec;
import org.owasp.esapi.codecs.DB2Codec;
import org.owasp.esapi.codecs.MySQLCodec;
import org.owasp.esapi.codecs.MySQLCodec.Mode;
import org.owasp.esapi.codecs.OracleCodec;
import org.owasp.esapi.errors.EncodingException;

import lucee.loader.engine.CFMLEngineFactory;
import lucee.loader.util.Util;
import lucee.runtime.PageContext;
import lucee.runtime.exp.PageException;

public class ESAPIEncode extends FunctionSupport {

	private static final long serialVersionUID = -6432679747287827759L;

	// this constants are also defined in Lucee core, do not change
	public static final short ENC_BASE64 = 1;
	public static final short ENC_CSS = 2;
	public static final short ENC_DN = 3;
	public static final short ENC_HTML = 4;
	public static final short ENC_HTML_ATTR = 5;
	public static final short ENC_JAVA_SCRIPT = 6;
	public static final short ENC_LDAP = 7;
	public static final short ENC_OS = 8;
	public static final short ENC_SQL = 9;
	public static final short ENC_URL = 10;
	public static final short ENC_VB_SCRIPT = 11;
	public static final short ENC_XML = 12;
	public static final short ENC_XML_ATTR = 13;
	public static final short ENC_XPATH = 14;
	public static final short ENC_NONE = 15;

	public static String encode(String item, short encFor, boolean canonicalize) throws PageException {
		return encode(item, encFor, canonicalize, null);
	}

	public static String encode(String item, short encFor, boolean canonicalize, Codec codec) throws PageException {
		if (eng.getStringUtil().isEmpty(item)) return item;

		try {
			Encoder encoder = ESAPI.encoder();
			if (canonicalize) item = encoder.canonicalize(item, false);

			switch (encFor) {
			case ENC_CSS:
				return encoder.encodeForCSS(item);
			case ENC_DN:
				return encoder.encodeForDN(item);
			case ENC_HTML:
				return encoder.encodeForHTML(item);
			case ENC_HTML_ATTR:
				return encoder.encodeForHTMLAttribute(item);
			case ENC_JAVA_SCRIPT:
				return encoder.encodeForJavaScript(item);
			case ENC_LDAP:
				return encoder.encodeForLDAP(item);
			case ENC_NONE:
				return item;
			case ENC_URL:
				return encoder.encodeForURL(item);
			case ENC_VB_SCRIPT:
				return encoder.encodeForVBScript(item);
			case ENC_XML:
				return encoder.encodeForXML(item);
			case ENC_XML_ATTR:
				return encoder.encodeForXMLAttribute(item);
			case ENC_XPATH:
				return encoder.encodeForXPath(item);
			case ENC_SQL:
				return encoder.encodeForSQL(codec, item);
			}
			throw exp.createApplicationException("invalid target encoding defintion");
		}
		catch (EncodingException ee) {
			throw cast.toPageException(ee);
		}
	}

	public static Codec toCodec(String sqlDialect) throws PageException, RuntimeException {
		if (!Util.isEmpty(sqlDialect, true)) {
			sqlDialect = sqlDialect.trim().toLowerCase();
			if ("mysql_ansi".equals(sqlDialect)) return new MySQLCodec(Mode.ANSI);
			if ("mysql".equals(sqlDialect)) return new MySQLCodec(Mode.STANDARD);
			if ("oracle".equals(sqlDialect)) return new OracleCodec();
			if ("db2".equals(sqlDialect)) return new DB2Codec();
		}
		else throw CFMLEngineFactory.getInstance().getExceptionUtil()
				.createApplicationException("You need to define a SQL dialect, this dialects are supported [db2, mysql, mysql_ansi, oracle]");

		throw CFMLEngineFactory.getInstance().getExceptionUtil()
				.createApplicationException("SQL dialect [" + sqlDialect + "] is not supported, supported dialects are [db2, mysql, mysql_ansi, oracle]");

	}

	public static String call(PageContext pc, String strEncodeFor, String value) throws PageException {
		return call(pc, strEncodeFor, value, false, null);
	}

	public static String call(PageContext pc, String strEncodeFor, String value, boolean canonicalize) throws PageException {
		return call(pc, strEncodeFor, value, canonicalize, null);
	}

	public static String call(PageContext pc, String strEncodeFor, String value, boolean canonicalize, String dialect) throws PageException {
		short type = toEncodeType(pc, strEncodeFor);
		return encode(value, type, canonicalize, type == ENC_SQL ? toCodec(dialect) : null);
	}

	public static short toEncodeType(String strEncodeFor, short defaultValue) {
		strEncodeFor = eng.getStringUtil().emptyIfNull(strEncodeFor).trim().toLowerCase();

		if ("css".equals(strEncodeFor)) return ENC_CSS;
		else if ("dn".equals(strEncodeFor)) return ENC_DN;
		else if ("html".equals(strEncodeFor)) return ENC_HTML;
		else if ("html_attr".equals(strEncodeFor)) return ENC_HTML_ATTR;
		else if ("htmlattr".equals(strEncodeFor)) return ENC_HTML_ATTR;
		else if ("html-attr".equals(strEncodeFor)) return ENC_HTML_ATTR;
		else if ("html attr".equals(strEncodeFor)) return ENC_HTML_ATTR;
		else if ("htmlattribute".equals(strEncodeFor)) return ENC_HTML_ATTR;
		else if ("html_attributes".equals(strEncodeFor)) return ENC_HTML_ATTR;
		else if ("htmlattributes".equals(strEncodeFor)) return ENC_HTML_ATTR;
		else if ("html-attributes".equals(strEncodeFor)) return ENC_HTML_ATTR;
		else if ("html attributes".equals(strEncodeFor)) return ENC_HTML_ATTR;
		else if ("js".equals(strEncodeFor)) return ENC_JAVA_SCRIPT;
		else if ("javascript".equals(strEncodeFor)) return ENC_JAVA_SCRIPT;
		else if ("java_script".equals(strEncodeFor)) return ENC_JAVA_SCRIPT;
		else if ("java script".equals(strEncodeFor)) return ENC_JAVA_SCRIPT;
		else if ("java-script".equals(strEncodeFor)) return ENC_JAVA_SCRIPT;
		else if ("ldap".equals(strEncodeFor)) return ENC_LDAP;
		else if ("".equals(strEncodeFor) || "none".equals(strEncodeFor)) return ENC_NONE;
		// else if("".equals(strEncodeFor)) encFor=ENC_OS;
		else if ("sql".equals(strEncodeFor)) return ENC_SQL;
		else if ("url".equals(strEncodeFor)) return ENC_URL;
		else if ("vbs".equals(strEncodeFor)) return ENC_VB_SCRIPT;
		else if ("vbscript".equals(strEncodeFor)) return ENC_VB_SCRIPT;
		else if ("vb-script".equals(strEncodeFor)) return ENC_VB_SCRIPT;
		else if ("vb_script".equals(strEncodeFor)) return ENC_VB_SCRIPT;
		else if ("vb script".equals(strEncodeFor)) return ENC_VB_SCRIPT;
		else if ("xml".equals(strEncodeFor)) return ENC_XML;
		else if ("xmlattr".equals(strEncodeFor)) return ENC_XML_ATTR;
		else if ("xml attr".equals(strEncodeFor)) return ENC_XML_ATTR;
		else if ("xml-attr".equals(strEncodeFor)) return ENC_XML_ATTR;
		else if ("xml_attr".equals(strEncodeFor)) return ENC_XML_ATTR;
		else if ("xmlattribute".equals(strEncodeFor)) return ENC_XML_ATTR;
		else if ("xmlattributes".equals(strEncodeFor)) return ENC_XML_ATTR;
		else if ("xml attributes".equals(strEncodeFor)) return ENC_XML_ATTR;
		else if ("xml-attributes".equals(strEncodeFor)) return ENC_XML_ATTR;
		else if ("xml_attributes".equals(strEncodeFor)) return ENC_XML_ATTR;
		else if ("xpath".equals(strEncodeFor)) return ENC_XPATH;
		else return defaultValue;
	}

	public static short toEncodeType(PageContext pc, String strEncodeFor) throws PageException {
		short df = (short) -1;
		short encFor = toEncodeType(strEncodeFor, df);
		if (encFor != df) return encFor;

		String msg = "value [" + strEncodeFor + "] is invalid, valid values are " + "[css,dn,html,html_attr,javascript,ldap,sql,vbscript,xml,xml_attr,xpath]";
		throw exp.createApplicationException(msg);

	}

	public static String canonicalize(String input, boolean restrictMultiple, boolean restrictMixed, boolean throwOnError) throws PageException {
		if (eng.getStringUtil().isEmpty(input)) return input;

		try {
			Encoder encoder = ESAPI.encoder();
			String item = encoder.canonicalize(input, restrictMultiple, restrictMixed);
			return item;
		}
		catch (Exception e) {
			if (throwOnError == false) return "";
			throw cast.toPageException(e);
		}

	}

	@Override
	public Object invoke(PageContext pc, Object[] args) throws PageException {
		if (args.length == 2) return call(pc, cast.toString(args[0]), cast.toString(args[1]));
		if (args.length == 3) return call(pc, cast.toString(args[0]), cast.toString(args[1]), cast.toBooleanValue(args[2]));
		if (args.length == 4) return call(pc, cast.toString(args[0]), cast.toString(args[1]), cast.toBooleanValue(args[2]), cast.toString(args[3]));
		throw exp.createFunctionException(pc, "ESAPIEncode", 2, 4, args.length);
	}

}
