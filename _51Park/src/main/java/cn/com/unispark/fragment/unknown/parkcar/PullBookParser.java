package cn.com.unispark.fragment.unknown.parkcar;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import android.util.Log;
import android.util.Xml;

public class PullBookParser implements BookParser {

	@Override
	public ArrayList<ParkCarEntity> parse(InputStream is) throws Exception {
		ArrayList<ParkCarEntity> points = null;
		ParkCarEntity p = null;

		// XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		// XmlPullParser parser = factory.newPullParser();

		XmlPullParser parser = Xml.newPullParser(); // 由android.util.Xml创建一个XmlPullParser实例
		parser.setInput(is, "UTF-8"); // 设置输入流 并指明编码方式

		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:

				points = new ArrayList<ParkCarEntity>();
				break;
			case XmlPullParser.START_TAG:

				if (parser.getName().equals("point")) {
					p = new ParkCarEntity();
				} else if (parser.getName().equals("title")) {
					eventType = parser.next();
					p.setTitle(parser.getText());
					Log.e("slx", "parser.getText()--->" + parser.getText());
				}

				else if (parser.getName().equals("id")) {
					eventType = parser.next();
					p.setId(parser.getText());
				} else if (parser.getName().equals("pCount")) {
					eventType = parser.next();
					p.setpCount(Integer.parseInt(parser.getText()));
				} else if (parser.getName().equals("tCount")) {
					eventType = parser.next();
					p.settCount(Integer.parseInt(parser.getText()));
				} else if (parser.getName().equals("tType")) {
					eventType = parser.next();
					p.settType(parser.getText());
				} else if (parser.getName().equals("imgUrl")) {
					eventType = parser.next();
					p.setImgUrl(parser.getText());
				} else if (parser.getName().equals("dPrice")) {
					eventType = parser.next();
					p.setdPrice(Double.parseDouble(parser.getText()));
				} else if (parser.getName().equals("dPriceDay")) {
					eventType = parser.next();
					p.setdPriceDay(parser.getText());
				} else if (parser.getName().equals("dPriceNight")) {
					eventType = parser.next();
					p.setdPriceNight(parser.getText());
				} else if (parser.getName().equals("dOpenTime")) {
					eventType = parser.next();
					p.setdOpenTime(parser.getText());
				} else if (parser.getName().equals("dCloseTime")) {
					eventType = parser.next();
					p.setdCloseTime(parser.getText());
				} else if (parser.getName().equals("state")) {
					eventType = parser.next();
					p.setState(parser.getText());
				} else if (parser.getName().equals("state_flag")) {
					eventType = parser.next();
					p.setState_falg(Integer.parseInt(parser.getText()));
				} else if (parser.getName().equals("address")) {
					Log.e("slx", "address99999--->" + parser.getName());
					eventType = parser.next();

					p.setAddress(parser.getText());
					Log.e("slx",
							"parser.getText()--->Address" + parser.getText());
				} else if (parser.getName().equals("lat")) {
					eventType = parser.next();
					p.setLat(Double.parseDouble(parser.getText()));
				} else if (parser.getName().equals("lon")) {
					eventType = parser.next();
					p.setLon(Double.parseDouble(parser.getText()));
				} else if (parser.getName().equals("my_lat")) {
					eventType = parser.next();
					p.setMy_lat(Double.parseDouble(parser.getText()));
				} else if (parser.getName().equals("my_lon")) {
					eventType = parser.next();
					p.setMy_lon(Double.parseDouble(parser.getText()));
				}

				break;
			case XmlPullParser.END_TAG:

				if (parser.getName().equals("point")) {
					points.add(p);
					p = null;
				}
				break;
			}
			eventType = parser.next();
		}
		for (int i = 0; i < points.size(); i++) {
			Log.e("slx", "pointssssssssssss--->" + points.get(i).getAddress());
		}
		return points;
	}

	@Override
	public String serialize(ArrayList<ParkCarEntity> points) throws Exception {
		// XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		// XmlSerializer serializer = factory.newSerializer();
		Log.i("XML", "01");
		XmlSerializer serializer = Xml.newSerializer(); // 由android.util.Xml创建一个XmlSerializer实例
		StringWriter writer = new StringWriter();
		serializer.setOutput(writer); // 设置输出方向为writer
		serializer.startDocument("UTF-8", true);
		serializer.startTag("", "points");
		Log.i("XML", "02");
		for (ParkCarEntity p : points) {
			serializer.startTag("", "point");

			Log.i("XML", "03");
			serializer.startTag("", "title");
			serializer.text(p.getTitle());
			serializer.endTag("", "title");

			serializer.startTag("", "id");
			serializer.text(p.getId() + "");
			serializer.endTag("", "id");

			serializer.startTag("", "address");
			serializer.text(p.getAddress() + "");
			serializer.endTag("", "address");

			serializer.startTag("", "my_lat");
			serializer.text(p.getMy_lat() + "");
			serializer.endTag("", "my_lat");

			serializer.startTag("", "my_lon");
			serializer.text(p.getMy_lon() + "");
			serializer.endTag("", "my_lon");

			serializer.startTag("", "lat");
			serializer.text(p.getLat() + "");
			serializer.endTag("", "lat");

			serializer.startTag("", "lon");
			serializer.text(p.getLon() + "");
			serializer.endTag("", "lon");

			serializer.startTag("", "pCount");
			serializer.text(p.getpCount() + "");
			serializer.endTag("", "pCount");

			serializer.startTag("", "tCount");
			serializer.text(p.gettCount() + "");
			serializer.endTag("", "tCount");

			serializer.startTag("", "tType");
			serializer.text(p.gettType() + "");
			serializer.endTag("", "tType");

			serializer.startTag("", "imgUrl");
			serializer.text(p.getImgUrl() + "");
			serializer.endTag("", "imgUrl");

			serializer.startTag("", "dPrice");
			serializer.text(p.getdPrice() + "");
			serializer.endTag("", "dPrice");

			serializer.startTag("", "dPriceDay");
			serializer.text(p.getdPriceDay() + "");
			serializer.endTag("", "dPriceDay");

			serializer.startTag("", "dPriceNight");
			serializer.text(p.getdPriceNight() + "");
			serializer.endTag("", "dPriceNight");

			serializer.startTag("", "dOpenTime");
			serializer.text(p.getdOpenTime() + "");
			serializer.endTag("", "dOpenTime");

			serializer.startTag("", "dCloseTime");
			serializer.text(p.getdCloseTime() + "");
			serializer.endTag("", "dCloseTime");

			serializer.startTag("", "state");
			serializer.text(p.getState() + "");
			serializer.endTag("", "state");

			serializer.startTag("", "state_flag");
			serializer.text(p.getState_falg() + "");
			serializer.endTag("", "state_flag");

			serializer.endTag("", "point");
		}
		serializer.endTag("", "points");
		serializer.endDocument();

		return writer.toString();
	}




	@Override
	public ArrayList<Luxian> parse_luxian(InputStream is_luxian)
			throws Exception {
		ArrayList<Luxian> luxians = null;
		Luxian luxian = null;

		// XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		// XmlPullParser parser = factory.newPullParser();

		XmlPullParser parser_luxian = Xml.newPullParser(); // 由android.util.Xml创建一个XmlPullParser实例
		parser_luxian.setInput(is_luxian, "UTF-8"); // 设置输入流 并指明编码方式

		int eventType = parser_luxian.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:

				luxians = new ArrayList<Luxian>();
				break;
			case XmlPullParser.START_TAG:

				if (parser_luxian.getName().equals("luxian")) {
					luxian = new Luxian();
				} else if (parser_luxian.getName().equals("sta_point")) {
					eventType = parser_luxian.next();
					luxian.setSta_point(parser_luxian.getText());
				} else if (parser_luxian.getName().equals("sta_lat")) {
					eventType = parser_luxian.next();
					luxian.setSta_lat(Double.parseDouble(parser_luxian
							.getText()));
				} else if (parser_luxian.getName().equals("sta_lon")) {
					eventType = parser_luxian.next();
					luxian.setSta_lon(Double.parseDouble(parser_luxian
							.getText()));
				} else if (parser_luxian.getName().equals("end_point")) {
					eventType = parser_luxian.next();
					luxian.setEnd_point(parser_luxian.getText());
				} else if (parser_luxian.getName().equals("end_lat")) {
					eventType = parser_luxian.next();
					luxian.setEnd_lat(Double.parseDouble(parser_luxian
							.getText()));
				} else if (parser_luxian.getName().equals("end_lon")) {
					eventType = parser_luxian.next();
					luxian.setEnd_lon(Double.parseDouble(parser_luxian
							.getText()));
				}
				break;
			case XmlPullParser.END_TAG:

				if (parser_luxian.getName().equals("luxian")) {
					luxians.add(luxian);
					luxian = null;
				}
				break;
			}
			eventType = parser_luxian.next();
		}
		return luxians;
	}

	@Override
	public String serialize_luxian(ArrayList<Luxian> luxians) throws Exception {
		// XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		// XmlSerializer serializer = factory.newSerializer();
		Log.i("XML", "01");
		XmlSerializer serializer_luxian = Xml.newSerializer(); // 由android.util.Xml创建一个XmlSerializer实例
		StringWriter writer = new StringWriter();
		serializer_luxian.setOutput(writer); // 设置输出方向为writer
		serializer_luxian.startDocument("UTF-8", true);
		serializer_luxian.startTag("", "luxians");
		Log.i("XML", "02");
		for (Luxian p : luxians) {
			serializer_luxian.startTag("", "luxian");

			Log.i("XML", "03");
			serializer_luxian.startTag("", "sta_point");
			serializer_luxian.text(p.getSta_point());
			serializer_luxian.endTag("", "sta_point");

			serializer_luxian.startTag("", "sta_lat");
			serializer_luxian.text(p.getSta_lat() + "");
			serializer_luxian.endTag("", "sta_lat");

			serializer_luxian.startTag("", "sta_lon");
			serializer_luxian.text(p.getSta_lon() + "");
			serializer_luxian.endTag("", "sta_lon");

			serializer_luxian.startTag("", "end_point");
			serializer_luxian.text(p.getEnd_point() + "");
			serializer_luxian.endTag("", "end_point");

			serializer_luxian.startTag("", "end_lat");
			serializer_luxian.text(p.getEnd_lat() + "");
			serializer_luxian.endTag("", "end_lat");

			serializer_luxian.startTag("", "end_lon");
			serializer_luxian.text(p.getEnd_lon() + "");
			serializer_luxian.endTag("", "end_lon");

			serializer_luxian.endTag("", "luxian");
		}
		serializer_luxian.endTag("", "luxians");
		serializer_luxian.endDocument();

		return writer.toString();
	}

	@Override
	public ArrayList<Load_map> parse_map(InputStream is_map) throws Exception {
		ArrayList<Load_map> maps = null;
		Load_map map = null;

		// XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		// XmlPullParser parser = factory.newPullParser();

		XmlPullParser parser_map = Xml.newPullParser(); // 由android.util.Xml创建一个XmlPullParser实例
		parser_map.setInput(is_map, "UTF-8"); // 设置输入流 并指明编码方式

		int eventType = parser_map.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:

				maps = new ArrayList<Load_map>();
				break;
			case XmlPullParser.START_TAG:

				if (parser_map.getName().equals("city_map")) {
					map = new Load_map();
				} else if (parser_map.getName().equals("city_name")) {
					eventType = parser_map.next();
					map.setCity_name(parser_map.getText());
				} else if (parser_map.getName().equals("jindu")) {
					eventType = parser_map.next();
					map.setJindu(parser_map.getText());
				}
				break;
			case XmlPullParser.END_TAG:

				if (parser_map.getName().equals("city_map")) {
					maps.add(map);
					map = null;
				}
				break;
			}
			eventType = parser_map.next();
		}
		return maps;
	}

	@Override
	public String serialize_map(ArrayList<Load_map> maps) throws Exception {
		// XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		// XmlSerializer serializer = factory.newSerializer();
		Log.i("XML", "01");
		XmlSerializer serializer_map = Xml.newSerializer(); // 由android.util.Xml创建一个XmlSerializer实例
		StringWriter writer = new StringWriter();
		serializer_map.setOutput(writer); // 设置输出方向为writer
		serializer_map.startDocument("UTF-8", true);
		serializer_map.startTag("", "city_maps");
		Log.i("XML", "02");
		for (Load_map p : maps) {
			serializer_map.startTag("", "city_map");

			Log.i("XML", "03");
			serializer_map.startTag("", "city_name");
			serializer_map.text(p.getCity_name());
			serializer_map.endTag("", "city_name");

			serializer_map.startTag("", "jindu");
			serializer_map.text(p.getJindu() + "");
			serializer_map.endTag("", "jindu");

			serializer_map.endTag("", "city_map");
		}
		serializer_map.endTag("", "city_maps");
		serializer_map.endDocument();

		return writer.toString();
	}

	@Override
	public ArrayList<list_title> parse_title(InputStream is_title)
			throws Exception {
		ArrayList<list_title> list_names = null;
		list_title list_name = null;

		// XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		// XmlPullParser parser = factory.newPullParser();

		XmlPullParser parser_title = Xml.newPullParser(); // 由android.util.Xml创建一个XmlPullParser实例
		parser_title.setInput(is_title, "UTF-8"); // 设置输入流 并指明编码方式

		int eventType = parser_title.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:

				list_names = new ArrayList<list_title>();
				break;
			case XmlPullParser.START_TAG:

				if (parser_title.getName().equals("Auto_name")) {
					list_name = new list_title();
				} else if (parser_title.getName().equals("a_name")) {
					eventType = parser_title.next();
					list_name.setList_name(parser_title.getText());
				}
				break;
			case XmlPullParser.END_TAG:

				if (parser_title.getName().equals("Auto_name")) {
					list_names.add(list_name);
					list_name = null;
				}
				break;
			}
			eventType = parser_title.next();
		}
		return list_names;
	}

	@Override
	public String serialize_title(ArrayList<list_title> list_name)
			throws Exception {
		// XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		// XmlSerializer serializer = factory.newSerializer();
		Log.i("XML", "01");
		XmlSerializer serializer_luxian = Xml.newSerializer(); // 由android.util.Xml创建一个XmlSerializer实例
		StringWriter writer = new StringWriter();
		serializer_luxian.setOutput(writer); // 设置输出方向为writer
		serializer_luxian.startDocument("UTF-8", true);
		serializer_luxian.startTag("", "Auto_names");
		Log.i("XML", "02");
		for (list_title p : list_name) {
			serializer_luxian.startTag("", "Auto_name");

			Log.i("XML", "03");
			serializer_luxian.startTag("", "a_name");
			serializer_luxian.text(p.getList_name());
			serializer_luxian.endTag("", "a_name");

			serializer_luxian.endTag("", "Auto_name");
		}
		serializer_luxian.endTag("", "Auto_names");
		serializer_luxian.endDocument();

		return writer.toString();
	}

}