package com.kin.ecosystem.backup.qr;


import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import java.io.IOException;

public interface QRFileUriHandler {

	@NonNull
	Bitmap loadFile(@NonNull Uri uri) throws IOException;

	@NonNull
	Uri saveFile(@NonNull Bitmap image) throws IOException;
}