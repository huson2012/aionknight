/**   
 * Эмулятор игрового сервера Aion 2.7 от команды разработчиков 'Aion-Knight Dev. Team' является 
 * свободным программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного программного 
 * обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой более поздней 
 * версии.
 * 
 * Программа распространяется в надежде, что она будет полезной, но БЕЗ КАКИХ БЫ ТО НИ БЫЛО 
 * ГАРАНТИЙНЫХ ОБЯЗАТЕЛЬСТВ; даже без косвенных  гарантийных  обязательств, связанных с 
 * ПОТРЕБИТЕЛЬСКИМИ СВОЙСТВАМИ и ПРИГОДНОСТЬЮ ДЛЯ ОПРЕДЕЛЕННЫХ ЦЕЛЕЙ. Для подробностей смотрите 
 * Стандартную Общественную Лицензию GNU.
 * 
 * Вы должны были получить копию Стандартной Общественной Лицензии GNU вместе с этой программой. 
 * Если это не так, напишите в Фонд Свободного ПО (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * Веб-cайт разработчиков : http://aion-knight.ru
 * Поддержка клиента игры : Aion 2.7 - 'Арена Смерти' (Иннова) 
 * Версия серверной части : Aion-Knight 2.7 (Beta version)
 */

package gameserver.dataholders.loadingutils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.namespace.QName;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.*;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Comment;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.*;
import java.util.Collection;
import java.util.Properties;
import static org.apache.commons.io.filefilter.FileFilterUtils.*;

public class XmlMerger
{
	private static final Logger logger = Logger.getLogger(XmlMerger.class);
	private final File  baseDir;
	private final File  sourceFile;
	private final File  destFile;
	private final File  metaDataFile;
	private XMLInputFactory     inputFactory    = XMLInputFactory.newInstance();
	private XMLOutputFactory    outputFactory   = XMLOutputFactory.newInstance();
	private XMLEventFactory     eventFactory    = XMLEventFactory.newInstance();

	public XmlMerger(File source, File target)
	{
		this(source, target, source.getParentFile());
	}

	public XmlMerger(File source, File target, File baseDir)
	{
		this.baseDir = baseDir;

		this.sourceFile = source;
		this.destFile = target;
		
		this.metaDataFile = new File(target.getParent(), target.getName() + ".properties");
	}

	public void process() throws Exception
	{
		logger.debug("Processing " + sourceFile + " files into " + destFile);

		if (!sourceFile.exists())
			throw new FileNotFoundException("Source file " + sourceFile.getPath() + " not found.");

		boolean needUpdate = false;

		if (!destFile.exists())
		{
			logger.debug("Dest file not found - creating new file");
			needUpdate = true;
		}
		else if (!metaDataFile.exists())
		{
			logger.debug("Meta file not found - creating new file");
			needUpdate = true;
		}
		else
		{
			logger.debug("Dest file found - checking file modifications");
			needUpdate = checkFileModifications();
		}

		if (needUpdate)
		{
			logger.debug("Modifications found. Updating...");
			try
			{
				doUpdate();
			}
			catch (Exception e)
			{
				FileUtils.deleteQuietly(destFile);
				FileUtils.deleteQuietly(metaDataFile);
				throw e;
			}
		}
		else
		{
			logger.debug("Files are up-to-date");
		}
	}

	private boolean checkFileModifications() throws Exception
	{
		long destFileTime = destFile.lastModified();

		if (sourceFile.lastModified() > destFileTime)
		{
			logger.debug("Source file was modified ");
			return true;
		}

		Properties metadata = restoreFileModifications(metaDataFile);

		if (metadata == null)
			return true;

		SAXParserFactory parserFactory = SAXParserFactory.newInstance();
		SAXParser parser = parserFactory.newSAXParser();
		TimeCheckerHandler handler = new TimeCheckerHandler(baseDir, metadata);
		parser.parse(sourceFile, handler);

		return handler.isModified();
	}

	private void doUpdate() throws XMLStreamException, IOException
	{
		XMLEventReader reader = null;
		XMLEventWriter writer = null;

		Properties metadata = new Properties();
		
		try
		{
			writer = outputFactory.createXMLEventWriter(new BufferedWriter(new FileWriter(destFile, false)));
			reader = inputFactory.createXMLEventReader(new FileReader(sourceFile));

			while (reader.hasNext())
			{
				final XMLEvent xmlEvent = reader.nextEvent();

				if (xmlEvent.isStartElement() && isImportQName(xmlEvent.asStartElement().getName()))
				{
					processImportElement(xmlEvent.asStartElement(), writer, metadata);
					continue;
				}

				if (xmlEvent.isEndElement() && isImportQName(xmlEvent.asEndElement().getName()))
					continue;

				if (xmlEvent instanceof Comment)
					continue;

				if (xmlEvent.isCharacters())
					if (xmlEvent.asCharacters().isWhiteSpace() || xmlEvent.asCharacters().isIgnorableWhiteSpace())// skip whitespaces.
						continue;

				writer.add(xmlEvent);

				if (xmlEvent.isStartDocument()) {
					writer.add(eventFactory.createComment("\nThis file is machine-generated. DO NOT MODIFY IT!\n"));
				}
			}

			storeFileModifications(metadata, metaDataFile);
		}
		finally
		{
			if (writer != null)
				try { writer.close(); } catch (Exception ignored) {}
			if (reader != null)
				try { reader.close(); } catch (Exception ignored) {}
		}
	}

	private boolean isImportQName(QName name)
	{
		return "import".equals(name.getLocalPart());
	}

	private static final QName qNameFile = new QName("file");
	private static final QName qNameSkipRoot = new QName("skipRoot");
	private static final QName qNameRecursiveImport = new QName("recursiveImport");
	private void processImportElement(StartElement element, XMLEventWriter writer, Properties metadata) throws XMLStreamException, IOException
	{
		File file = new File(baseDir, getAttributeValue(element, qNameFile, null, "Attribute 'file' is missing or empty."));

		if (!file.exists())
			throw new FileNotFoundException("Missing file to import:" + file.getPath());

		boolean skipRoot = Boolean.valueOf(getAttributeValue(element, qNameSkipRoot, "false", null));
		boolean recImport = Boolean.valueOf(getAttributeValue(element, qNameRecursiveImport, "true", null));

		if (file.isFile())
		{
			importFile(file, skipRoot, writer, metadata);
		}
		else
		{
			logger.debug("Processing dir " + file);

			Collection<File> files = listFiles(file, recImport);

			for (File childFile : files)
			{
				importFile(childFile, skipRoot, writer, metadata);
			}
		}
	}

	private static Collection<File> listFiles(File root, boolean recursive)
	{
		IOFileFilter dirFilter = recursive ? makeSVNAware(HiddenFileFilter.VISIBLE) : null;

		return FileUtils.listFiles(root,
				and(
						and(
								notFileFilter(prefixFileFilter("new")), suffixFileFilter(".xml")),
						HiddenFileFilter.VISIBLE),
				dirFilter);
	}

	private String getAttributeValue(StartElement element, QName name, String def, String onErrorMessage)
			throws XMLStreamException
	{
		Attribute attribute = element.getAttributeByName(name);

		if (attribute == null)
		{
			if (def == null)
				throw new XMLStreamException(onErrorMessage, element.getLocation());

			return def;
		}

		return attribute.getValue();
	}

	private void importFile(File file, boolean skipRoot, XMLEventWriter writer, Properties metadata) throws XMLStreamException, IOException
	{
		logger.debug("Appending file " + file);
		metadata.setProperty(file.getPath(), makeHash(file));

		XMLEventReader reader = null;

		try
		{
			reader = inputFactory.createXMLEventReader(new FileReader(file));

			QName firstTagQName = null;

			while (reader.hasNext())
			{
				XMLEvent event = reader.nextEvent();

				if (event.isStartDocument() || event.isEndDocument())
					continue;

				if (event instanceof Comment)
					continue;

				if (event.isCharacters())
				{
					if (event.asCharacters().isWhiteSpace() || event.asCharacters().isIgnorableWhiteSpace())
						continue;
				}

				if (firstTagQName == null && event.isStartElement())
				{
					firstTagQName = event.asStartElement().getName();

					if (skipRoot)
					{
						continue;
					}
					else
					{
						StartElement old = event.asStartElement();

						event = eventFactory.createStartElement(old.getName(), old.getAttributes(), null);
					}
				}

				if (event.isEndElement() && skipRoot && event.asEndElement().getName().equals(firstTagQName))
					continue;

				writer.add(event);
			}
		}
		finally
		{
			if (reader != null)
				try { reader.close(); } catch (Exception ignored) {}
		}
	}

	private static class TimeCheckerHandler extends DefaultHandler
	{
		private File basedir;
		private Properties metadata;

		private boolean isModified = false;

		private Locator locator;

		private TimeCheckerHandler(File basedir, Properties metadata)
		{
			this.basedir = basedir;
			this.metadata = metadata;
		}

		@Override
		public void setDocumentLocator(Locator locator)
		{
			this.locator = locator;
		}

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
		{
			if (isModified || !"import".equals(qName))
				return;

			String value = attributes.getValue(qNameFile.getLocalPart());

			if (value == null)
				throw new SAXParseException("Attribute 'file' is missing ", locator);

			File file = new File(basedir, value);

			if (!file.exists())
				throw new SAXParseException("Imported file not found. file = " + file.getPath(), locator);

			if (file.isFile() && checkFile(file))
			{
				isModified = true;
				return;
			}

			if (file.isDirectory())
			{
				String rec = attributes.getValue(qNameRecursiveImport.getLocalPart());

				Collection<File> files = listFiles(file, rec == null ? true : Boolean.valueOf(rec));

				for (File childFile : files)
				{
					if (checkFile(childFile))
					{
						isModified = true;
						return;
					}
				}
			}
		}

		private boolean checkFile(File file)
		{
			String data = metadata.getProperty(file.getPath());

			if (data == null)
				return true;

			try
			{
				String hash = makeHash(file);

				if (!data.equals(hash))
					return true;
			}
			catch (IOException e)
			{
				logger.warn("File varification error. File: " + file.getPath()
						+ ", location="+locator.getLineNumber()+":"+locator.getColumnNumber(), e);
				return true;
			}

			return false;
		}

		public boolean isModified()
		{
			return isModified;
		}
	}

	private Properties restoreFileModifications(File file)
	{
		if (!file.exists() || !file.isFile())
			return null;

		FileReader reader = null;

		try
		{
			Properties props = new Properties();

			reader = new FileReader(file);

			props.load(reader);

			return props;
		}
		catch (IOException e)
		{
			logger.debug("File modfications restoring error. ", e);
			return null;
		}
		finally
		{
			IOUtils.closeQuietly(reader);
		}
	}

	private void storeFileModifications(Properties props, File file)
			throws IOException
	{
		FileWriter writer = null;
		try
		{
			writer = new FileWriter(file, false);
			props.store(writer, "This file is machine-generated. DO NOT EDIT!");
		}
		catch (IOException e)
		{
			logger.error("Failed to store file modification data.");
			throw e;
		}
		finally
		{
			IOUtils.closeQuietly(writer);
		}
	}

	private static String makeHash(File file)
			throws IOException
	{
		return String.valueOf(FileUtils.checksumCRC32(file));
	}
}