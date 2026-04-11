package com.hayao0819.liconify;

import com.hayao0819.liconify.IExtractSubjectCallback;

interface IRootProviderProxy {
	String[] runCommand(String command);
	void extractSubject(in Bitmap input, String resultPath, IExtractSubjectCallback callback);
	void enableOverlay(in String packageName);
	void disableOverlay(in String packageName);
}