package highframe.core.manager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtractSubXml {

	public void extract(String xmlFile, String directory) {
		try {
			FileReader fReader = new FileReader(xmlFile);
			BufferedReader readFile = new BufferedReader(fReader);

			boolean isSub = false;
			String nameSub = null;
			FileWriter fSub = null;
			String line = readFile.readLine();
			while (line != null) {

				if (line.contains("<subarchitecture")) {
					Pattern patternSubarchitecture = Pattern.compile("<subarchitecture idsubarchitecture=\"(.*?)\"");
					Matcher matcherUrlBase = patternSubarchitecture.matcher(line);
					if (matcherUrlBase.find()) {
						nameSub = matcherUrlBase.group(1).toString();
					}
					fSub = new FileWriter(directory +"\\"+nameSub + ".xml");
					fSub.write("<architecture xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"adl.xsd\">");
					isSub = true;
				}
				if (line.contains("</subarchitecture")) {
					isSub = false;
					fSub.append(line + "\n");
					fSub.append("</architecture>");
					fSub.close();
				}
				if (isSub) {
					fSub.write(line + "\n");
				}

				line = readFile.readLine();
			}
			readFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}