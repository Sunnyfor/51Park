package cn.com.unispark.util.upload;

import java.io.File;
import java.util.Map;

import org.json.JSONObject;

public interface MultiPartJson {

	public void addFileUpload(String param, File file);

	public Map<String, File> getFileUploads();

	public void addStringUpload(String param, String content);

	public Map<String, String> getStringUploads();
}