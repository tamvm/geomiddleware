package geomobility.core.utils;

import geomobility.core.GlobalCore;
import geomobility.core.GlobalCore.MyOutStream;
import geomobility.core.exception.GeoException;
import geomobility.localization.I18N;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import net.opengis.xls.v_1_1_0.AbstractBodyType;
import net.opengis.xls.v_1_1_0.AbstractRequestParametersType;
import net.opengis.xls.v_1_1_0.ObjectFactory;
import net.opengis.xls.v_1_1_0.RequestHeaderType;
import net.opengis.xls.v_1_1_0.RequestType;
import net.opengis.xls.v_1_1_0.SLIAType;
import net.opengis.xls.v_1_1_0.XLSType;

/**
 * Some utility functions
 * 
 * @author <a href="mailto:vo.mita.ov@gmail.com">VoMinhTam</a>
 */
public class StaticFunc {
	public static final String CONFIG_FILE_PATH = "config.properties";

	public static String getContent(String fileName) {
		File file = new File(fileName);
		StringBuffer contents = new StringBuffer();
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(file));
			String text = null;

			// repeat until all lines is read
			while ((text = reader.readLine()) != null) {
				// Unicode
				text = new String(text.getBytes(), "UTF-8");
				text = text.replaceAll("" + (char)65533 + "\\" + (char)63, "Đ");
				contents.append(text).append("\n");//
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return contents.toString();
	}

	public static void writeFile(String fileName, String content,
			boolean isAppend) {
		try {
			// Create file
			FileWriter fstream = new FileWriter(fileName, isAppend);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(content);
			// Close the output stream
			out.close();
		} catch (Exception e) {// Catch exception if any
			e.printStackTrace();
		}
	}

	/**
	 * Load a Properties File
	 * 
	 * @param propsFile
	 * @return Properties
	 * @throws IOException
	 */
	private static Properties loadProperties(File propsFile) throws IOException {
		Properties props = new Properties();
		FileInputStream fis = new FileInputStream(propsFile);
		props.load(fis);
		fis.close();
		return props;
	}

	private static Properties prop = null;
	static {
		try {
			File file = new File(CONFIG_FILE_PATH);
			if (!file.exists())
				file.createNewFile();
			prop = StaticFunc.loadProperties(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getProperty(String key) {
		return String.valueOf(prop.get(key));
	}

	public static boolean isNOE(String s) {
		return (s == null || s.equals(""));
	}
	
	/**
	 * @deprecated
	 * @param <T>
	 * @param xml
	 * @return
	 * @throws GeoException
	 */
	public static <T> T parse(String xml) throws GeoException {
		JAXBElement<XLSType> element;
		try {
			element = (JAXBElement<XLSType>) GlobalCore.unmarshaller
					.unmarshal(new StringReader(xml));
			XLSType item = element.getValue();
			List<JAXBElement<? extends AbstractBodyType>> listRT = item
					.getBody();
			int sizeBody = listRT.size();

			JAXBElement<RequestType> reqType = (JAXBElement<RequestType>) listRT
					.get(0);

			Class<RequestType> declaredType = reqType.getDeclaredType();
			QName name = reqType.getName();
			boolean nil = reqType.isNil();
			Class scope = reqType.getScope();
			RequestType value = reqType.getValue();
			BigInteger maxinumResponses = value.getMaximumResponses();
			String methodName = value.getMethodName();
			String requestID = value.getRequestID();
			JAXBElement<? extends AbstractRequestParametersType> requestParameters = value
					.getRequestParameters();
			String version = value.getVersion();

			Class<? extends AbstractRequestParametersType> declaredTypeOfValueRequestParameters = requestParameters
					.getDeclaredType();

			T t = (T) requestParameters.getValue();

			return t;
		} catch (JAXBException e) {
			e.printStackTrace();
			throw new GeoException(I18N.m.getString("cannot_parse_request"));
		}
	}
	
	public static RequestHeaderType parseRequestHeader(XLSType item) throws GeoException {
		JAXBElement<RequestHeaderType> requestHeaderType = (JAXBElement<RequestHeaderType>) item.getHeader();
		RequestHeaderType requestHeader = requestHeaderType.getValue();
		
		
		
		return requestHeader;
	}

	public static <T> List<T> parse(XLSType item) throws GeoException {
		List<JAXBElement<? extends AbstractBodyType>> listRT = item.getBody();
		int sizeBody = listRT.size();

		List<T> result = new ArrayList<T>();

		for (int i = 0; i < listRT.size(); i++) {
			JAXBElement<RequestType> reqType = (JAXBElement<RequestType>) listRT
					.get(i);
			// Class<RequestType> declaredType = reqType.getDeclaredType();
			// QName name = reqType.getName();
			// boolean nil = reqType.isNil();
			// Class scope = reqType.getScope();
			//			
			RequestType value = reqType.getValue();
			// BigInteger maxinumResponses = value.getMaximumResponses();
			// String methodName = value.getMethodName();
			// String requestID = value.getRequestID();
			JAXBElement<? extends AbstractRequestParametersType> requestParameters = value
					.getRequestParameters();
			String version = value.getVersion();

			Class<? extends AbstractRequestParametersType> declaredTypeOfValueRequestParameters = requestParameters
					.getDeclaredType();

			T t = (T) requestParameters.getValue();

			result.add(t);
		}
		return result;
	}

	public static XLSType getXLSType(String xml) throws GeoException {
		JAXBElement<XLSType> element;
		try {
			element = (JAXBElement<XLSType>) GlobalCore.unmarshaller
					.unmarshal(new StringReader(xml));
			XLSType item = element.getValue();

			return item;
		} catch (JAXBException e) {
			e.printStackTrace();
			throw new GeoException(I18N.m.getString("cannot_parse_request"));
		}
	}

	public static OutputStream buildXLSResult(XLSType xlsType, OutputStream output)
			throws GeoException {
		GlobalCore.MyOutStream myOutStream = (MyOutStream) output;
		myOutStream.clear();

		JAXBElement<XLSType> jaxbElement = GlobalCore.objectFactoryXLS
				.createXLS(xlsType);
		try {
			GlobalCore.marshaller.marshal(jaxbElement, myOutStream);
		} catch (JAXBException e) {
			e.printStackTrace();
			throw new GeoException(I18N.m.getString("cannot_build_result"));
		}
		return myOutStream;

	}
	
	public static String toOLSString(SLIAType sliaType) {
		JAXBContext jcOLS = null;
		{
			try {
				jcOLS = JAXBContext.newInstance(GlobalCore.OLSContextPath);
			} catch (JAXBException e) {
				e.printStackTrace();
			}
		}

		ObjectFactory objectFactory = new ObjectFactory();
		Marshaller m = null;
		{
			try {
				m = jcOLS.createMarshaller();
			} catch (JAXBException e) {
				e.printStackTrace();
			}
		}

		try {
			JAXBElement<SLIAType> jaxbElement = objectFactory.createSLIA(sliaType);
			javax.xml.bind.Marshaller marshaller = jcOLS.createMarshaller();

			OutputStream output = new OutputStream() {
				private StringBuilder string = new StringBuilder();

				@Override
				public void write(int b) throws IOException {
					this.string.append((char) b);
				}

				public String toString() {
					return this.string.toString();
				}
			};

			marshaller.marshal(jaxbElement, output);
			return output.toString();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Double parseDouble(String s){
		try {
			return Double.parseDouble(s);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String removeSign(String str) {
	   String vietSign = "aáàảãạăắằẳẵặâấầẩẫậAÁÀẢÃẠĂẮẰẲẴẶÂẤẦẨẪẬeéèẻẽẹêếềểễệEÉÈẺẼẸÊẾỀỂỄỆiíìỉĩịIÍÌỈĨỊoóòỏõọôốồổỗộơớờởỡợOÓÒỎÕỌÔỐỒỔỖỘƠỚỜỞỠỢuúùủũụưứừửữựUÚÙỦŨỤƯỨỪỬỮỰyýỳỷỹỵYÝỲỶỸỴdđDĐ";
	   String noSign = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaeeeeeeeeeeeeeeeeeeeeeeeeiiiiiiiiiiiioooooooooooooooooooooooooooooooooooouuuuuuuuuuuuuuuuuuuuuuuuyyyyyyyyyyyydddd";
	   for (int i = 0; i < str.length(); i++) {
	       for (int j = 0; j < noSign.length(); j++) {
	           str = str.replaceAll(String.valueOf(vietSign.charAt(j)), String.valueOf(noSign.charAt(j)));
	       }
	   }
	   return str.toLowerCase();
	}
	
	public static void main(String[] args) {
		String tmp = "Đông Hương";
		System.out.println(removeSign(tmp));
	}
	
	public static String join(List<? extends CharSequence> s, String delimiter) {
		int capacity = 0;
		int delimLength = delimiter.length();
		Iterator<? extends CharSequence> iter = s.iterator();
		if (iter.hasNext()) {
			capacity += iter.next().length() + delimLength;
		}

		StringBuilder buffer = new StringBuilder(capacity);
		iter = s.iterator();
		if (iter.hasNext()) {
			buffer.append(iter.next());
			while (iter.hasNext()) {
				buffer.append(delimiter);
				buffer.append(iter.next());
			}
		}
		return buffer.toString();
	}
	
}
