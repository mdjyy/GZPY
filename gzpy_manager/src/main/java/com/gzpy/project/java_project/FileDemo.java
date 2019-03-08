package com.gzpy.project.java_project;

import java.io.File;
import java.util.Map;

/**
 * 封装文件信息
 * */
public class FileDemo {
	//文件
    private File file;
    //行对应内容映射 key:行数+"|"+匹配的关键字 例如 "1|demo:<project demo>"
    private Map<String,String> Num_Content; 
    //正则表达式
    private String key;
    
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public Map<String, String> getNum_Content() {
		return Num_Content;
	}
	public void setNum_Content(Map<String, String> num_Content) {
		Num_Content = num_Content;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	@Override
	public String toString() {
		return "FileDemo [file=" + file + ", Num_Content=" + Num_Content + ", key=" + key + "]";
	}
	
}
