package com.agourlay.pomf.tools.rss;

import java.io.ByteArrayOutputStream;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class RSSFeedWriter {
	ByteArrayOutputStream byteArrayOutputStream;
	private Feed rssfeed;

	public RSSFeedWriter(Feed rssfeed,ByteArrayOutputStream byteArrayOutputStream) {
		this.rssfeed = rssfeed;
		this.byteArrayOutputStream = byteArrayOutputStream;
	}

	public ByteArrayOutputStream write() throws Exception {

		// Create a XMLOutputFactory
		XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();

		// Create XMLEventWriter
		XMLEventWriter eventWriter = outputFactory.createXMLEventWriter(byteArrayOutputStream);

		// Create a EventFactory

		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		XMLEvent end = eventFactory.createDTD("\n");

		// Create and write Start Tag

		StartDocument startDocument = eventFactory.createStartDocument();

		eventWriter.add(startDocument);

		// Create open tag
		eventWriter.add(end);

		StartElement rssStart = eventFactory.createStartElement("", "", "rss");
		eventWriter.add(rssStart);
		eventWriter.add(eventFactory.createAttribute("version", "2.0"));
		eventWriter.add(end);

		eventWriter.add(eventFactory.createStartElement("", "", "channel"));
		eventWriter.add(end);

		// Write the different nodes

		createNode(eventWriter, "title", rssfeed.getTitle());

		createNode(eventWriter, "link", rssfeed.getLink());

		createNode(eventWriter, "description", rssfeed.getDescription());

		createNode(eventWriter, "language", rssfeed.getLanguage());

		createNode(eventWriter, "copyright", rssfeed.getCopyright());

		createNode(eventWriter, "pubdate", rssfeed.getPubDate());

		for (FeedMessage entry : rssfeed.getMessages()) {
			eventWriter.add(eventFactory.createStartElement("", "", "item"));
			eventWriter.add(end);
			createNode(eventWriter, "title", entry.getTitle());
			createNode(eventWriter, "description", entry.getDescription());
			createNode(eventWriter, "link", entry.getLink());
			createNode(eventWriter, "author", entry.getAuthor());
			createNode(eventWriter, "guid", entry.getGuid());
			eventWriter.add(end);
			eventWriter.add(eventFactory.createEndElement("", "", "item"));
			eventWriter.add(end);

		}

		eventWriter.add(end);
		eventWriter.add(eventFactory.createEndElement("", "", "channel"));
		eventWriter.add(end);
		eventWriter.add(eventFactory.createEndElement("", "", "rss"));

		eventWriter.add(end);

		eventWriter.add(eventFactory.createEndDocument());

		eventWriter.close();
		return byteArrayOutputStream;
	}

	private void createNode(XMLEventWriter eventWriter, String name,

	String value) throws XMLStreamException {
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		XMLEvent end = eventFactory.createDTD("\n");
		XMLEvent tab = eventFactory.createDTD("\t");
		// Create Start node
		StartElement sElement = eventFactory.createStartElement("", "", name);
		eventWriter.add(tab);
		eventWriter.add(sElement);
		// Create Content
		Characters characters = eventFactory.createCharacters(value);
		eventWriter.add(characters);
		// Create End node
		EndElement eElement = eventFactory.createEndElement("", "", name);
		eventWriter.add(eElement);
		eventWriter.add(end);
	}
}
