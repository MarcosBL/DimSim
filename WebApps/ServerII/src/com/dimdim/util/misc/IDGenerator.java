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
 *								                                          *
 * Copyright (c) 2006 Communiva Inc. All Rights Reserved.	              *
 *								                                          *
 *								                                          *
 * This code is licensed under the DimDim License                         *
 * For details please visit http://www.dimdim.com/license		          *
 *									                                      *
 **************************************************************************
 */
/*
 **************************************************************************
 *	File Name  : IDGenerator.java
 *  Created On : Apr 10, 2006
 *  Created By : Saurav Mohapatra
 **************************************************************************
*/
 
package com.dimdim.util.misc;

/**
 * A Helper class to generate ids for messages
 * Now this one uses a user specified prefix and a generate middle string (hashcode of the id generator)
 * and a monotonically increasing index
 * 
 * This is good for generating message ids, xmpp IQ ids etc.
 * 
 * Not as powerful as the UUID class but much much faster
 * 
 * @author Saurav Mohapatra
 * @email  Saurav.Mohapatra@communiva.com
 */
public class IDGenerator
{
	protected	StringGenerator		stringGenerator;
	
	public static final char SEPARATOR ='_';
	private String idPrefix = "default";
	private String idMid = "";
	private int currentIndex = 0;

	/**
	 * create an id generator with the prefix and set 
	 * the mid to hashcode of the instance
	 */
	public IDGenerator(String idPrefix)
	{
		super();
		this.idPrefix = idPrefix;
		this.idMid = Integer.toString(hashCode());
		this.stringGenerator = new StringGenerator();
	}
	/**
	 * generate the next id
	 * id = prefix_midvalue(hashcode)_currentIndex
	 * @author Saurav Mohapatra
	 * @email  Saurav.Mohapatra@communiva.com
	 * @return
	 *
	 */
	public String generate()
	{
		/*
		StringBuffer buf = new StringBuffer(idPrefix);
		buf.append(SEPARATOR);
		buf.append(idMid);
		buf.append(SEPARATOR);
		buf.append(currentIndex++);
		return buf.toString();
		*/
		return	this.stringGenerator.generateTimeBasedExtraRandomString(this.idPrefix,8);
	}
	/**
	 * test main for id generator
	 * 
	 * @author Saurav Mohapatra
	 * @email  Saurav.Mohapatra@communiva.com
	 * @param args
	 *
	 */
	public static void main(String[] args)
	{
		//check for no clash with same prefix in two different id generators
		IDGenerator idgen = new IDGenerator("mohaps");
		IDGenerator idgen2 = new IDGenerator("mohaps");
		for(int i = 0; i < Integer.MAX_VALUE; i++)
		{
			System.out.println("idgen1>> "+i+" <<"+idgen.generate());
			System.out.println("idgen2>> "+i+" <<"+idgen2.generate());
			System.out.println();
		}
		for(int i = 0; i < Integer.MAX_VALUE; i++)
		{
			System.out.println("idgen1>> "+i+" <<"+idgen.generate());
			System.out.println("idgen2>> "+i+" <<"+idgen2.generate());
			System.out.println();
		}
	}

}
