/* ownCloud Android Library is available under MIT license
 *   Copyright (C) 2014 ownCloud Inc.
 *   
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *   
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *   
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, 
 *   EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *   MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND 
 *   NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS 
 *   BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN 
 *   ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN 
 *   CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 *
 */
package com.owncloud.android.lib.test_project.test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.owncloud.android.lib.common.operations.RemoteOperationResult;
import com.owncloud.android.lib.common.operations.RemoteOperationResult.ResultCode;
import com.owncloud.android.lib.test_project.TestActivity;

import android.test.ActivityInstrumentationTestCase2;

/**
 * Class to test Create Folder Operation
 * @author masensio
 * @author David A. Velasco
 *
 */
public class CreateFolderTest extends ActivityInstrumentationTestCase2<TestActivity> {

	
	private static final String LOG_TAG = CreateFolderTest.class.getCanonicalName();

	private static final String FOLDER_PATH_BASE = "/testCreateFolder";

	private TestActivity mActivity;
	private String mCurrentDate;
	private List<String> mCreatedFolderPaths;
	
	public CreateFolderTest() {
	    super(TestActivity.class);
	   
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		mCurrentDate = sdf.format(new Date());
		mCreatedFolderPaths = new ArrayList<String>();
	}
	
	@Override
	  protected void setUp() throws Exception {
	    super.setUp();
	    setActivityInitialTouchMode(false);
	    mActivity = getActivity();
	    mCreatedFolderPaths.clear();
	}
	
	/**
	 * Test Create Folder
	 */
	public void testCreateFolder() {
		String remotePath = FOLDER_PATH_BASE + mCurrentDate;
		mCreatedFolderPaths.add(remotePath);
		RemoteOperationResult result =  mActivity.createFolder(remotePath, true);
		assertTrue(result.isSuccess() || result.getCode() == ResultCode.TIMEOUT);
		
		// Create Subfolder
		remotePath = FOLDER_PATH_BASE + mCurrentDate + FOLDER_PATH_BASE + mCurrentDate;
		mCreatedFolderPaths.add(remotePath);
		result =  mActivity.createFolder(remotePath, true);
		assertTrue(result.isSuccess() || result.getCode() == ResultCode.TIMEOUT);
	}
	
	
	/**
	 * Test to Create Folder with special characters: /  \  < >  :  "  |  ?  *
	 */
	public void testCreateFolderSpecialCharacters() {		
		
		String remotePath = FOLDER_PATH_BASE + "_\\" + mCurrentDate;
		RemoteOperationResult result =  mActivity.createFolder(remotePath, true);
		assertTrue(result.getCode() == ResultCode.INVALID_CHARACTER_IN_NAME);
		
		remotePath = FOLDER_PATH_BASE + "_<" + mCurrentDate;		
		result =  mActivity.createFolder(remotePath, true);
		assertTrue(result.getCode() == ResultCode.INVALID_CHARACTER_IN_NAME);
		
		remotePath = FOLDER_PATH_BASE + "_>" + mCurrentDate;		
		result =  mActivity.createFolder(remotePath, true);
		assertTrue(result.getCode() == ResultCode.INVALID_CHARACTER_IN_NAME);
		
		remotePath = FOLDER_PATH_BASE + "_:" + mCurrentDate;		
		result =  mActivity.createFolder(remotePath, true);
		assertTrue(result.getCode() == ResultCode.INVALID_CHARACTER_IN_NAME);
		
		remotePath = FOLDER_PATH_BASE + "_\"" + mCurrentDate;		
		result =  mActivity.createFolder(remotePath, true);
		assertTrue(result.getCode() == ResultCode.INVALID_CHARACTER_IN_NAME);
		
		remotePath = FOLDER_PATH_BASE + "_|" + mCurrentDate;		
		result =  mActivity.createFolder(remotePath, true);
		assertTrue(result.getCode() == ResultCode.INVALID_CHARACTER_IN_NAME);
		
		remotePath = FOLDER_PATH_BASE + "_?" + mCurrentDate;		
		result =  mActivity.createFolder(remotePath, true);
		assertTrue(result.getCode() == ResultCode.INVALID_CHARACTER_IN_NAME);
		
		remotePath = FOLDER_PATH_BASE + "_*" + mCurrentDate;		
		result =  mActivity.createFolder(remotePath, true);
		assertTrue(result.getCode() == ResultCode.INVALID_CHARACTER_IN_NAME);
	}


	@Override
	protected void tearDown() throws Exception {
		Iterator<String> it = mCreatedFolderPaths.iterator();
		RemoteOperationResult removeResult = null;
		while (it.hasNext()) {
			removeResult = mActivity.removeFile(it.next());
			if (!removeResult.isSuccess()) {
				Utils.logAndThrow(LOG_TAG, removeResult);
			}
		}
		super.tearDown();
	}
	
}
