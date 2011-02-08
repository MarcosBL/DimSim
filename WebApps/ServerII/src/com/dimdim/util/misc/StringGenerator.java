/*
 **************************************************************************
 *                                                                        *
 *               DDDDD   iii             DDDDD   iii                      *
 *               DD  DD      mm mm mmmm  DD  DD      mm mm mmmm           *
 *               DD   DD iii mmm  mm  mm DD   DD iii mmm  mm  mm          *
 *               DD   DD iii mmm  mm  mm DD   DD iii mmm  mm  mm          *
 *               DDDDDD  iii mmm  mm  mm DDDDDD  iii mmm  mm  mm          *
 *                                                                        *
 **************************************************************************
 **************************************************************************
 *                                                                        *
 * Part of the DimDim V 1.0 Codebase (http://www.dimdim.com)	          *
 *                                                                        *
 * Copyright (c) 2006 Communiva Inc. All Rights Reserved.                 *
 *                                                                        *
 *                                                                        *
 * This code is licensed under the DimDim License                         *
 * For details please visit http://www.dimdim.com/license                 *
 *                                                                        *
 **************************************************************************
 */

package com.dimdim.util.misc;

import	java.util.Locale;
import	java.util.Random;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This utility class provides a locale specific random and regular
 * strings. These are used in generating various keys and data for
 * testing.
 * 
 * The regular string is a simple base 36 representation of the given
 * argument number. The ramdom strings are generated as per pattern
 * required.
 */
public class StringGenerator
{
	protected	static	Random		randomNumberGenerator;
	protected	static	char[]		alphabets	=
	{'a','b','c','d','e','f','g','h','i','j','k','l','m','n',
		'o','p','q','r','s','t','u','v','w','x','y','z',
		'0','1','2','3','4','5','6','7','8','9'};
	
	protected	Locale	locale;
	
	public	StringGenerator()
	{
		locale = Locale.US;
	}
	public	StringGenerator(Locale locale)
	{
		this.locale = locale;
	}
	public	String		generateRandomString()
	{
		return	generateRandomString( 40, 10 );
	}
	
	//	Just to make life easier for user.
	
	public	String		generateRandomString( int maxLength )
	{
		return	generateRandomString( maxLength, maxLength/4 );
	}
	
	public	String		generateTimeBasedRandomString( int maxLength, int minLength )
	{
		String str = this.generateRandomString(maxLength, minLength);
		long t = System.currentTimeMillis();
		return	Long.toHexString(t)+str;
	}
	
	public	String		generateTimeBasedExtraRandomString( String prefix, int maxLength)
	{
		String str1 = this.generateRandomString(maxLength, maxLength);
		String str2 = this.generateRandomString(maxLength, 1);
		long t = System.currentTimeMillis();
		StringBuffer buf = new StringBuffer();
		buf.append(prefix);
		buf.append(Long.toHexString(t));
		buf.append(str1);
		buf.append(str2);
		return	buf.toString();
	}
	
	public	String		generateRandomString( int maxLength, int minLength )
	{
		if ( StringGenerator.randomNumberGenerator == null )
		{
			synchronized ( StringGenerator.class )
			{
				StringGenerator.randomNumberGenerator = new Random();
			}
		}
		double	d = randomNumberGenerator.nextDouble();
		int	sLen = minLength+(int)((maxLength-minLength)*d);
		StringBuffer	buf = new	StringBuffer();
		for ( int i=0; i<sLen; i++ )
		{
			int j = (int)(StringGenerator.randomNumberGenerator.nextDouble() * 35);
			buf.append( StringGenerator.alphabets[j] );
		}
		return	buf.toString();
	}
	
	//	Purpose of this string generator is to get exact string every time. This is so
	//	that you can refer to the data that you created. Random data can be used to load
	//	up the db, but then you dont know what user id to use to connect. So a combination
	//	of this random and regular strings will be used to stress test the system under
	//	large load.
	
	public	String	generateRegularString( int a )
	{
		int	index = 0;
		int	agg = a;
		int[] letters = new int[5];
		for ( int i=0;i<5; i++ )
		{
			index = agg%26;
			agg = agg/26;
			letters[4-i]=index;
		}
		return	generateRegularString( letters );
	}
	public	String	generateRegularString( int a, int b )
	{
		return	generateRegularString( a, b, 0, 0, 0 );
	}
	public	String	generateRegularString( int a, int b, int c )
	{
		return	generateRegularString( a, b, c, 0, 0 );
	}
	public	String	generateRegularString( int a, int b, int c, int d )
	{
		return	generateRegularString( a, b, c, d, 0 );
	}
	public	String	generateRegularString( int a, int b, int c, int d, int e )
	{
		int[] letters = new int[5];
		letters[0] = a;
		letters[1] = b;
		letters[2] = c;
		letters[3] = d;
		letters[4] = e;
		return	generateRegularString( letters );
	}
	public	String	generateRegularString( int letters[] )
	{
		StringBuffer buf = new StringBuffer();
		int	length = letters.length;
		for ( int i=0; i<length; i++ )
		{
			buf.append( StringGenerator.alphabets[letters[i]] );
		}
		return	buf.toString();
	}
	public	String	generateRegularStringFromIndex(int index)
	{
		return	generateRegularString(index);
	}
	public	String	generateRandomString_fixedLength(int length)
	{
		return generateRandomString(length,length);
	}
	public	String	generateRandomString_minMaxLength(int minLength, int MaxLength)
	{
		return	generateRandomString(minLength,MaxLength);
	}
}
