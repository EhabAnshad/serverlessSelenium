package com.serverlessSelenium.helpers;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.serverlessSelenium.lambda.model.TestResult;

public class ResultsHandler {
//TODO: need complete refectoring
	private List<TestResult> results;

	public ResultsHandler() {
		results = new ArrayList<TestResult>();
	}

	public void addResult(TestResult result) {
		results.add(result);
	}

	public void aggregateAndReport() {

		new Thread(new Runnable() {
			public void run() {
				for (TestResult result : results) {
					// save attachments
					if (!result.getAttachments().isEmpty()) {
						writeAttachments(result.getAttachments());
					}
				}
			}
		}).start();
		
		new Thread(new Runnable() {
			public void run() {
				writeResults();
			}
		}).start();

		// Write reports

	}

	private void writeAttachments(Map<String, byte[]> attachments) {
		 File outputDirectory = setupOutputFolder();

		attachments.forEach((fileName, bytes) -> {
			try {
				FileUtils.writeByteArrayToFile(new File(outputDirectory, fileName), bytes);
			} catch (IOException e) {
				System.out.println(e);
			}
		});
	}

	private File setupOutputFolder() {
		String outputDirectoryPath =
                 Paths.get(Paths.get("").toAbsolutePath().toString(),"target","surefire-reports").toString();
		File outputDirectory = new File(outputDirectoryPath);
		outputDirectory.mkdirs();
		return outputDirectory;
	}

	private void writeResults() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		setupOutputFolder();
		
		String outputFile = 
				Paths.get(Paths.get("").toAbsolutePath().toString(), "target", "surefire-reports",
						java.time.LocalDateTime.now().toString().replace(":", "-") + ".xml").toString();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();

			if (results.size() > 1) {
				InputStream inputStream = new ByteArrayInputStream(results.get(0).getTestngResult().getBytes());

				Document baseDoc = db.parse(inputStream);
				NodeList baseList = baseDoc.getElementsByTagName("suite");
				Element baseElement = (Element) baseList.item(0);
				int total = 1;
				int passed = 0;
				int failed = 0;
				int skipped = 0;

				// total="1" passed="1" failed="0" skipped="0">
				for (int i = 1; i < results.size(); i++) {
					total++;

					InputStream inputStream2 = new ByteArrayInputStream(results.get(i).getTestngResult().getBytes());
					Document doc2 = (Document) db.parse(inputStream2);

					NodeList list = doc2.getElementsByTagName("test");
					Element element = (Element) list.item(0);

					Node firstDocImportedNode = baseDoc.importNode(element, true);
					baseElement.appendChild(firstDocImportedNode);

					// Copy Attributes
					NamedNodeMap namedNodeMap2 = doc2.getElementsByTagName("testng-results").item(0).getAttributes();
					for (int x = 0; x < namedNodeMap2.getLength(); x++) {
						int value = Integer.parseInt(namedNodeMap2.item(x).getChildNodes().item(0).getNodeValue());
						switch (namedNodeMap2.item(x).getNodeName()) {
						case "failed":
							failed += value;
							break;
						case "passed":
							passed += value;
							break;
						case "skipped":
							skipped += value;
							break;

						}

					}

				}

				// update totals
				NamedNodeMap namedNodeMap2 = baseDoc.getElementsByTagName("testng-results").item(0).getAttributes();
				for (int x = 0; x < namedNodeMap2.getLength(); x++) {
					int value = Integer.parseInt(namedNodeMap2.item(x).getChildNodes().item(0).getNodeValue());
					switch (namedNodeMap2.item(x).getNodeName()) {
					case "failed":
						System.out.println("failed: " + String.valueOf(failed + value));
						namedNodeMap2.item(x).getChildNodes().item(0).setNodeValue(String.valueOf(failed + value));
						break;
					case "passed":
						System.out.println("passed: " + String.valueOf(passed + value));
						namedNodeMap2.item(x).getChildNodes().item(0).setNodeValue(String.valueOf(passed + value));
						break;
					case "skipped":
						System.out.println("skipped: " + String.valueOf(skipped + value));
						namedNodeMap2.item(x).getChildNodes().item(0).setNodeValue(String.valueOf(skipped + value));
						break;
					case "total":
						System.out.println("total: " + String.valueOf(total + value));
						namedNodeMap2.item(x).getChildNodes().item(0).setNodeValue(String.valueOf(total + value));
						break;
					}

				}

				// Output Document
				TransformerFactory tf = TransformerFactory.newInstance();
				Transformer t = tf.newTransformer();
				DOMSource source = new DOMSource(baseDoc);
				StreamResult result = new StreamResult(new File(outputFile));// System.out
				t.transform(source, result);

			} else {
				BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
				writer.write(results.get(0).getTestngResult());
				System.out.println(results.get(0).getTestngResult());
				writer.close();
			}

		} catch (ParserConfigurationException | SAXException | IOException | TransformerException e1) {
			e1.printStackTrace();
		}

	}

}
