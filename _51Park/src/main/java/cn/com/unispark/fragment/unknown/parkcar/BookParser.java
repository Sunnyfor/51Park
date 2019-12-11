package cn.com.unispark.fragment.unknown.parkcar;

import java.io.InputStream;
import java.util.ArrayList;

public interface BookParser {
	/**
	 * 解析输入流 得到Book对象集合
	 * 
	 * @param is
	 * @return
	 * @throws Exception
	 */
	public ArrayList<ParkCarEntity> parse(InputStream is) throws Exception;

	/**
	 * 序列化Book对象集合 得到XML形式的字符串
	 * 
	 * @param books
	 * @return
	 * @throws Exception
	 */
	public String serialize(ArrayList<ParkCarEntity> books) throws Exception;

	public ArrayList<Luxian> parse_luxian(InputStream is_luxian)
			throws Exception;

	public String serialize_luxian(ArrayList<Luxian> luxian) throws Exception;

	public ArrayList<Load_map> parse_map(InputStream is_map) throws Exception;

	public String serialize_map(ArrayList<Load_map> maps) throws Exception;

	public ArrayList<list_title> parse_title(InputStream is_title)
			throws Exception;

	public String serialize_title(ArrayList<list_title> list_name)
			throws Exception;
}