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

import lucee.runtime.PageContext;
import lucee.runtime.ext.function.Function;
import java.net.URLDecoder;


public class Canonicalize implements Function {

	private static final long serialVersionUID = -4248746351014698481L;

	public static String call(PageContext pc,String input, boolean restrictMultiple, boolean restrictMixed) throws
		Exception{
	
		return ESAPIEncode.canonicalize(input, restrictMultiple, restrictMixed, false);
	}

	public static String call(PageContext pc,String input, boolean restrictMultiple, boolean restrictMixed, boolean throwonError) throws Exception{
		
		String str = input;
		String decodeUrl = " ";
        String strFind = "%";
        int count = 0, fromIndex = 0;
        
        while ((fromIndex = str.indexOf(strFind, fromIndex)) != -1 ){
            count++;
            fromIndex++;
        }

		decodeUrl = URLDecoder.decode(input, "UTF-8");

		if(decodeUrl == input) return ESAPIEncode.canonicalize(input, restrictMultiple, restrictMixed, throwonError);
			
		else {
			if(throwonError == false && (restrictMultiple == true || restrictMixed == true)) {
				if(count > 0) return " ";
				else return ESAPIEncode.canonicalize(input, restrictMultiple, restrictMixed, throwonError);
			}
			else {
				return ESAPIEncode.canonicalize(input, restrictMultiple, restrictMixed, throwonError);
			}
		}
	}
}
