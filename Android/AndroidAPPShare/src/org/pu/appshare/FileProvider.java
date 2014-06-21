package org.pu.appshare;

import java.io.File;
import java.io.FileNotFoundException;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

/**
 * 
 * this class is provider for app Internal storage��
 * the authorities must be you package name plus ".fileprovider"
 * 
 * �������Android Ӧ���ڲ��洢�������ṩ�� ��Ҫ��AndroidManifest.xml��ע��,
 * authorities��������ĳ���������� ".fileprovider"
 * 
 * @author json pu
 * 
 */
public class FileProvider extends ContentProvider {

	public static String CONTENT_URI;

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		CONTENT_URI = getContext().getPackageName() + ".fileprovider";
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ParcelFileDescriptor openFile(Uri uri, String mode)
			throws FileNotFoundException {

		// this file is data/data/packName/files/
		// �򿪵���app filesĿ¼
		File file = new File(getContext().getFilesDir(),
				uri.getLastPathSegment());
		ParcelFileDescriptor parcel = null;
		try {
			//type is read only
			parcel = ParcelFileDescriptor.open(file,
					ParcelFileDescriptor.MODE_READ_ONLY);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return parcel;
	}

}
