package org.pu.appshare;

/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File;
import java.util.ArrayList;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

/**
 * This class encapsulates a content item. Referencing the content's type, and
 * the differing way to reference the content (asset URI or resource id).
 */
public class ContentItem {
	
	// 图片 image
	public static final int CONTENT_TYPE_IMAGE = 0;
	// 文本  text
	public static final int CONTENT_TYPE_TEXT = 1;
	// 图文  image and text
	public static final int CONTENT_TYPE_TELETEXT = 2;
	// 分享文件  file
	public static final int CONTENT_TYPE_FILE = 3;

	public final int contentType;
	public final String[] contents;

	/**
	 * 
	 * @param contentType
	 *            分享的类型 
	 *            
	 *            share type， such as CONTENT_TYPE_TEXT
	 * @param contents
	 *            一个String数组，根据类型设置着，如果类型是图文，contens[0]就是文，余下的都是图片地址
	 *            
	 *            string array,seting content by content type
	 *            
	 */
	public ContentItem(int contentType, String... contents) {
		this.contentType = contentType;
		this.contents = contents;
	}

	/**
	 * 
	 * 返回一个分享类型intent
	 * 
	 * Returns an {@link android.content.Intent} which can be used to share this
	 * item's content with other applications.
	 * 
	 * @return Intent
	 */
	public Intent getShareIntent() {
		if (contents.length == 1)
			return getSingleIntent();
		else if (contents.length == 2 && contentType == CONTENT_TYPE_TELETEXT)
			return getSingleIntent();
		else if (contents.length > 1)
			return getMultipleIntent();
		else
			return new Intent(Intent.ACTION_SEND);

	}


	private Intent getSingleIntent() {
		Intent intent = new Intent(Intent.ACTION_SEND);
		switch (contentType) {
		case CONTENT_TYPE_IMAGE:
			intent.setType("image/*");
			// Bundle the asset content uri as the EXTRA_STREAM uri
			intent.putExtra(Intent.EXTRA_STREAM, getContentUri(contents[0]));
			break;

		case CONTENT_TYPE_TEXT:
			intent.setType("text/plain");
			// Get the string resource and bundle it as an intent extra
			intent.putExtra(Intent.EXTRA_TEXT, contents[0]);
			break;

		case CONTENT_TYPE_TELETEXT:
			intent.setType("image/*");
			intent.putExtra(Intent.EXTRA_TEXT, contents[0]);
			if (contents.length == 2)
				intent.putExtra(Intent.EXTRA_STREAM, getContentUri(contents[1]));
			break;
		case CONTENT_TYPE_FILE:
			intent.setType("*/*");
			intent.putExtra(Intent.EXTRA_STREAM, getContentUri(contents[0]));
			break;
		}

		return intent;
	}

	private Intent getMultipleIntent() {
		Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
		ArrayList<Uri> uris = new ArrayList<Uri>();
		if (contentType == CONTENT_TYPE_TELETEXT) {
			intent.putExtra(Intent.EXTRA_TEXT, contents[0]);
			for (int i = 1; i < contents.length; i++)
				uris.add(getContentUri(contents[i]));
		} else {
			for (String content : contents)
				uris.add(getContentUri(content));
		}
		intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);

		switch (contentType) {
		case CONTENT_TYPE_IMAGE:
			intent.setType("image/*");
			break;

		case CONTENT_TYPE_TELETEXT:
			intent.setType("image/*");
			break;

		case CONTENT_TYPE_FILE:
			intent.setType("*/*");
			break;
		}

		return intent;
	}
	
	private Uri getContentUri(String content) {
		if (!TextUtils.isEmpty(content)) {
			return Uri.fromFile(new File(content));
		} else {
			throw new NullPointerException();
		}
	}

	/**
	 * 
	 * 从assets和file内容提供者创建 intent
	 * 
	 * create intent from assets provider and file provider
	 * 
	 * @author Json PU
	 * 
	 */
	static class IntentFactory {

		/**
		 * 获取分享到指定APP的intent 
		 * 
		 * get intent by packName
		 * 
		 * @param packName
		 *            package name
		 * @param text
		 *            content text
		 * @return {@link android.content.Intent}
		 */
		public static Intent getTextIntentTo(String packName, String text) {
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.putExtra(Intent.EXTRA_TEXT, text);
			intent.setPackage(packName);
			intent.setType("text/plain");
			return intent;
		}

		/**
		 * 分享assets目录的图片
		 * 
		 * @param names
		 *            the image's file name
		 * @return {@link android.content.Intent}
		 */
		public static Intent decAssetImageIntent(String... names) {
			Intent intent = getIntentByArray(names);
			if (names.length == 1) {
				intent.putExtra(Intent.EXTRA_STREAM, getAssetUri(names[0]));
			} else {
				ArrayList<Uri> uris = new ArrayList<Uri>();
				for (String name : names)
					uris.add(getAssetUri(name));
				intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
			}
			intent.setType("image/*");
			return intent;

		}

		/**
		 * @param text
		 *            content text
		 * @param names
		 *            the image's file name
		 * @return {@link android.content.Intent}
		 */
		public static Intent decAssetTeletextIntent(String text,
				String... names) {
			Intent intent = decAssetImageIntent(names);
			intent.putExtra(Intent.EXTRA_TEXT, text);
			return intent;
		}

		/**
		 * @param names
		 *            the file's name
		 * @return {@link android.content.Intent}
		 */
		public static Intent decAssetFileIntent(String... names) {
			Intent intent = getIntentByArray(names);
			if (names.length == 1) {
				intent.putExtra(Intent.EXTRA_STREAM, getAssetUri(names[0]));
			} else {
				ArrayList<Uri> uris = new ArrayList<Uri>();
				for (String name : names)
					uris.add(getAssetUri(name));
				intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
			}
			intent.setType("*/*");
			return intent;

		}

		/**
		 * @param names
		 *            the image's file name
		 * @return {@link android.content.Intent}
		 */
		public static Intent decFileImageIntent(String... names) {
			Intent intent = getIntentByArray(names);
			if (names.length == 1) {
				intent.putExtra(Intent.EXTRA_STREAM, getFileUri(names[0]));
			} else {
				ArrayList<Uri> uris = new ArrayList<Uri>();
				for (String name : names)
					uris.add(getFileUri(name));
				intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
			}
			intent.setType("image/*");
			return intent;
		}

		/**
		 * 
		 * @param text
		 *            content text
		 * @param names
		 *            the image's file name
		 * @return {@link android.content.Intent}
		 */
		public static Intent decFileTeletext(String text, String names) {
			Intent intent = decFileImageIntent(names);
			intent.putExtra(Intent.EXTRA_TEXT, text);
			return intent;
		}

		/**
		 * @param names
		 *            the file's name
		 * @return
		 */
		public static Intent decFileIntent(String... names) {
			Intent intent = getIntentByArray(names);
			if (names.length == 1) {
				intent.putExtra(Intent.EXTRA_STREAM, getFileUri(names[0]));
			} else {
				ArrayList<Uri> uris = new ArrayList<Uri>();
				for (String name : names)
					uris.add(getFileUri(name));
				intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
			}
			intent.setType("*/*");
			return intent;
		}

		private static Intent getIntentByArray(Object[] objs) {
			if (objs.length == 1)
				return new Intent(Intent.ACTION_SEND);
			else if (objs.length > 1)
				return new Intent(Intent.ACTION_SEND_MULTIPLE);
			else
				throw new NullPointerException();
		}

		private static Uri getAssetUri(String content) {
			if (!TextUtils.isEmpty(content))
				return Uri.parse("content://" + AssetProvider.CONTENT_URI + "/"
						+ content);
			else
				throw new NullPointerException();
		}

		private static Uri getFileUri(String fileName) {
			if (!TextUtils.isEmpty(fileName))
				return Uri.parse("content://" + FileProvider.CONTENT_URI + "/"
						+ fileName);
			else
				throw new NullPointerException();
		}

	}

}