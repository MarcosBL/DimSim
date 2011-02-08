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

package com.dimdim.conference;

import java.io.*;

public class DimdimSetupCreator
{
	public static void main(String[] args)
	{
		String url = args[0];
		String sfxExePath = args[1];
		String installerExePath = args[2];
		String outputExePath = args[3];
		try
		{
			createSFX(url, sfxExePath, installerExePath, outputExePath);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	/**
	 * The exe paths are required to be fully qualified.
	 */
	public static synchronized void createSFX(String url, String sfxExe, String installerExe, String outputExe) throws Exception
	{

        System.out.println("Generating Self-Extracting Exe ["+outputExe+"]...");
        System.out.println("   URL : "+url);
        System.out.println("   Seed Exe : "+sfxExe);
        System.out.println("   Installer Exe : "+installerExe);
		byte[] sfxExeBuf = getFileContents(sfxExe);
		byte[] installerExeBuf = getFileContents(installerExe);

		FileOutputStream fos = new FileOutputStream(outputExe);
		fos.write(sfxExeBuf);
		writeLittleEndianInt(fos,url.length());
		fos.write(url.getBytes());
		writeLittleEndianInt(fos,installerExeBuf.length);
		fos.write(installerExeBuf);
		fos.flush();
		fos.close();

		System.out.println("Generated Self Extracting Installer : "+outputExe);

	}
	private static byte[] getFileContents(String fileName) throws Exception
	{

		File file = new File(fileName);
		int len = (int)file.length();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		FileInputStream fis = new FileInputStream(file);
		copy(fis,baos,len);
		fis.close();
		return baos.toByteArray();
	}
	/**
		   * Copies information from the input stream to the output stream using
		   * the specified buffer size
		   * @throws java.io.IOException
		   */
	  public static void copy(InputStream input,
		  OutputStream output,
		  int bufferSize)
		  throws IOException {
		byte[] buf = new byte[bufferSize];
		int bytesRead = input.read(buf);
		while (bytesRead != -1) {
		  output.write(buf, 0, bytesRead);
		  bytesRead = input.read(buf);
		}
		output.flush();
	  }
	private static void writeLittleEndianInt(FileOutputStream fos, int iVal) throws Exception
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		dos.writeInt(iVal);
		byte[] octets = bos.toByteArray();
		byte tmp = octets[0];
		octets[0] = octets[3];
		octets[3] = tmp;
		tmp = octets[2];
		octets[2] = octets[1];
		octets[1] = tmp;

		fos.write(octets);
		fos.flush();
	}
};