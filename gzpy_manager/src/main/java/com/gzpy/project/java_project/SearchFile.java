package com.gzpy.project.java_project;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 查找某一类文件
 * */
public class SearchFile {

	private static final Logger log = LoggerFactory.getLogger(SearchFile.class);
	//保存文件信息
	private static final ArrayBlockingQueue<FileDemo> queen = new ArrayBlockingQueue<FileDemo>(1000,true);
	//处理完移动到这里
	private static final List<FileDemo> queenHis = new ArrayList<FileDemo>();
	//判断生产者是否执行完
	private static final AtomicBoolean isfileCompleted = new AtomicBoolean(false);
	//消费者进程
	private static FileForpersist thread;
	
	static {
		//消费者保存到excel
		thread = new FileForpersistExcel();
	}
	/**
	 * @param path 路径
	 * @param key 查找的文件名中关键字
	 * @return 当前查找到的文件
	 * @throws Exception 
	 */
	public static File[] searchFile(String path,String key) throws Exception {
		List<File> list = new ArrayList<File>();
		File file = new File(path);
		if(!file.exists()) {
			throw new Exception("文件不存在"+file.getPath());
		}
		if(file.isFile()&&file.getName().indexOf(key)>-1) {
			list.add(file);
			return list.toArray(new File[0]);
		}
		File childs[] = file.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				if(pathname.isDirectory()) {
					return true;
				}
				if(key==null||"".equals(key)||pathname.getName().indexOf(key)>-1) {
					return true;
				}
				return false;
			}
		});
		for(File f:childs) {
			log.info("查找文件:{}", f.getPath());
			if(f.isFile()) {
				list.add(f);
			}else {
				Collections.addAll(list, searchFile(f.getPath(),key));
			}
		}
		return list.toArray(new File[0]);
	}
	
	/** 查询并导出文件
	 * @param path 在那个文件夹下查找的路径
	 * @param content 需要查找文件所包含的内容 
	 * @param suffix 文件夹下哪些类型文件需要扫描
	 * @return void
	 * @throws Exception 
	 * */
	public static void saveandSearch(String path,String content,String...suffix) throws Exception {
		//设置成未执行完
		isfileCompleted.compareAndSet(true, false);
		
		new Thread(thread).start();
		
		File file[] = searchFileByContent(path, content, suffix);
		
		if(file.length==0) {
			log.info("没有符合条件的文件");
		}

		isfileCompleted.compareAndSet(false, true);
	}

	/**
	 * @param path 在那个文件夹下查找的路径
	 * @param content 需要查找文件所包含的内容 
	 * @param suffix 文件夹下哪些类型文件需要扫描
	 * @return 文件数组
	 * @throws Exception 
	 * */
	public static File[] searchFileByContent(String path,String content,String...suffix) throws Exception {
		File file = new File(path);
		List<File> list = new ArrayList<File>();
		if(!file.exists()) {
			throw new Exception("文件不存在"+file.getPath());
		}
		if(content==null||content.equals("")) {
			throw new Exception("content不能为空或空字符串");
		}
		File files[] = file.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				if(!pathname.isFile()) {
					return true;
				}
				for(String suff:suffix) {
					if("all".equals(suff)||pathname.getName().endsWith(suff)) {
						return checkFile(pathname, content);
					}
				}
				return false;
			}
		});

		for(File f: files) {
			if(f.isFile()) {
				list.add(f);
			}else {
				Collections.addAll(list, searchFileByContent(f.getPath(), content, suffix));
			}
		}
		return list.toArray(new File[0]);
	}
	
	/**
	 * *检测文件是否符合,将符合的文件存入队列中，并返回boolean
	 */
	private static boolean checkFile(File file,String content) {
		//读取文件内容
		String sb = readFile(file);
		//\n换行 newline并不移动左右 \r回到该行最左边 \n\r另起一行
		//第二个参数例如为xmls,那么就是以字符[x,m,l,s]分割
		StringTokenizer token = new StringTokenizer(sb,"\r\n");

		Pattern p = Pattern.compile(content);
		//行数
		int lineNum = 0;
	
		Map<String,String> map = new HashMap<String,String>();
		//判断当前文件内容是否符合
		boolean isExist = false;
        
		//一行一行读取
		while(token.hasMoreTokens()) {
			++lineNum;
			//一行一行的匹配
			String line = token.nextToken();
			Matcher match = p.matcher(line);
			while(match.find()){
				String ret = match.group(); 
				map.put(lineNum+"|"+ret,line.trim());
				//当前文件满足
				isExist = true;
			}
		}
		//满足条件的文件信息存入队列,
		//返回true
		if(isExist) {
			FileDemo demo = new FileDemo();
			demo.setNum_Content(map);
			demo.setKey(content);
			demo.setFile(file);
			log.debug("---加入队列---:{}",demo);
			queen.add(demo);
			return true;
		}
		
		return false;
	}

	/**
	 * 读取文件内容 
	 * */
	private static String readFile(File file) {
		ByteBuffer readBuffer = ByteBuffer.allocate(1024);
		FileChannel channel = null;
		StringBuffer sb = new StringBuffer();
		try {
			//读取的方式打开为read
			channel =  FileChannel.open(Paths.get(file.toURI()),StandardOpenOption.READ);
			int len = -1; 
			while((len = channel.read(readBuffer))!=-1) {
				sb.append(new String(readBuffer.array(),0,len,"utf-8"));
				//要清理，不然读取为0
				readBuffer.clear();
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(channel!=null) {
					channel.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public static FileDemo[] getQueen() {
		return queen.toArray(new FileDemo[0]);
	}

	//Excel存储
	private static class FileForpersistExcel extends  FileForpersist{
		
		private HSSFWorkbook workbook;

		private HSSFSheet sheet;

		//写入的文件
		private  File file;
		
		public FileForpersistExcel() {
			this(new File("p://default.xls"));
		}
		
		public FileForpersistExcel(File file) {
			this.file = file;
			init();
		}
		
		private void init()  {
			try {
				if(!file.exists()) {
					log.debug("文件不存在,新创建文件"+file.getPath());
					file.createNewFile();
				}
				
				workbook = new HSSFWorkbook();
				//创建新工作簿
				sheet = workbook.createSheet();
				//表单的列
				HSSFRow row = sheet.createRow(0);
				//标头
				String str [] = {"文件路径","行数","检索内容","行内容"};

				createCell(row,str,getHSSStyle(true,13));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
        /**
         **   返回样式
         * */
		private HSSFCellStyle getHSSStyle(boolean blod,int Fontsize) {
			//字体样式
			HSSFFont font = workbook.createFont();
			
			font.setBold(blod);
			
			font.setFontHeightInPoints((short) Fontsize);
			
			font.setFontName("新宋体");
			
			HSSFCellStyle style = workbook.createCellStyle();
			
			style.setFont(font);
			
			return style;
		}

		/**
		 * @param row 行对象
		 * @param str 每个表格内容数组,长度代表列数
		 * @param style 格式
		 * @throws 
		 * */
		private void createCell(HSSFRow row,String[] str,HSSFCellStyle style)  {
			for(int i=0;i<str.length;i++) {
				HSSFCell cell = row.createCell(i);
				//设置字体并赋值
				cell.setCellStyle(style);
				cell.setCellValue(str[i]);
			}
		}


		@Override
		public void persistFile(FileDemo demo) {
			try {
				int RowNum = sheet.getLastRowNum();
				HSSFRow row = sheet.createRow(RowNum+1);
				Map<String,String> map = demo.getNum_Content();
				for(Entry<String,String> e:map.entrySet()) {
					String key = e.getKey();
					//行内容
					String lineContent = e.getValue();
					String lineNum_Content[] = key.split("\\|");
					String lineNum = lineNum_Content[0];
					String findKey = lineNum_Content[1];
					String[] s= {demo.getFile().getPath(),lineNum,findKey,lineContent};
					createCell(row, s, null);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		@Override
		public void close() {
			try {
				workbook.write(file);
				workbook.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public String getTemp() {
			return file.getPath();
		}
	}

	//消费者将队列的信息持久化
	private static abstract class FileForpersist implements Runnable{
		//解析文件保存
		public abstract void persistFile(FileDemo demo);
		//提交和释放资源等操作
		public abstract void close();
		
		public abstract String getTemp();
		@Override
		public void run() {
			log.debug("消费者线程启动！");
			FileDemo demo = null; 
			try {
				while((demo = queen.poll(1,TimeUnit.SECONDS))!=null||!isfileCompleted.get()) {
					if(demo==null) {
						continue;
					}
					log.debug("消费者开始处理:"+demo);
					persistFile(demo);
					close();
				}
				log.debug("消费者处理完");
				log.info("保存至文件:{}",getTemp());
			} catch (InterruptedException e) {
				log.error("消费者进程被中断",e);
				e.printStackTrace();
			}catch(Exception e) {
				log.error("消费者出现异常",e);
			}
		}

	} 
	
	
	public static void main(String[] args) throws Exception {
		//				String path = "C:\\Program Files (x86)\\MySQL";
		//				String key = "undo";
		//			    File file[] = searchFile(path, key);
		//			    for(File f:file) {
		//			    	System.out.println(f.getPath());
		//			    }
		/*
		 * checkFile(new File("P:\\HNBOSS\\customer-web\\pom老.xml"), "enabled");
		 * FileDemo[] files = getQueen(); for(FileDemo demo:files) {
		 * System.out.println(demo); }
		 */
		
//		  String path = "P:\\HNBOSS"; String keyContent =
//		  "((25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))";
//		  saveandSearch(path, keyContent,"all");
		 
		String p = "C:/Users/mdj/Desktop/新建文件夹/和助手和助手需求文档";
		saveandSearch(p,"二维码","all");
	}
}
