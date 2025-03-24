
package burp.pocs;

import java.util.List;
import burp.IParameter;
import burp.IRequestInfo;
import burp.util.Request;
import burp.util.Util;

/**
 * HTML CSRF POCs
 * 
 * @author Ricardo Ferreira
 */
public class HtmlPocGenerator implements PocGenerator {

	@Override
	public byte[] generate(Request request) {
		String enctype = "";
		if (request.getContentType() == IRequestInfo.CONTENT_TYPE_MULTIPART) {
			enctype = "enctype=\"multipart/form-data\"";
		}
		String lineSep = System.lineSeparator();
		String pocString = String.format("\t<form method=\"%s\" action=\"%s\" %s>%s", request.getMethod(),
				request.getUrl().toString(), enctype, lineSep);
		pocString += createInputs(request.getParameters(), lineSep);
		pocString += "\t</form>" + lineSep;
		pocString += "\t<script>document.forms[0].submit();</script>" + lineSep;
		return createHTMLPage(pocString, lineSep).getBytes();
	}

	private String createInputs(List<IParameter> parameters, String lineSep) {
		List<String> inputs = parameters.stream().map(e -> createInput(e)).toList();
		String pocString = String.join(lineSep, inputs) + lineSep;
		return pocString;
	}

	private String createInput(IParameter parameter) {
		String name = Util.encodeHTML(parameter.getName());
		String value = Util.encodeHTML(parameter.getValue());
		return String.format("\t\t<input type=\"text\" name=\"%s\" value=\"%s\">", name, value);
	}

	private String createHTMLPage(String body, String lineSeparator) {
		return body;
	}
	
}
