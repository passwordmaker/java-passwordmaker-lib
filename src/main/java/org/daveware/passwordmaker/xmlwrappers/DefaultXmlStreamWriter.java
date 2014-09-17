/*
 * This file is part of Passwordmaker-je-lib.
 * Copyright (C) 2014 James Stapleton
 *
 * Passwordmaker-je-lib is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Passwordmaker-je-lib is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Passwordmaker-je-lib.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.daveware.passwordmaker.xmlwrappers;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * This is the Default implementation, that is just wrapping out the javax.xml.stream version
 * This is for the normal java libraries to use the RDDatabaseWriter.
 */
public class DefaultXmlStreamWriter implements XmlStreamWriter {
    XMLOutputFactory outputFactory;
    XMLStreamWriter streamWriter;
    public DefaultXmlStreamWriter(Writer writer) throws XmlIOException {
        outputFactory =  XMLOutputFactory.newFactory();
        try {
            streamWriter = outputFactory.createXMLStreamWriter(writer);
        } catch (XMLStreamException e) {
            throw new XmlIOException(e);
        }
    }

    @Override
    public void writeStartDocument() throws XmlIOException {
        try {
            streamWriter.writeStartDocument();
        } catch (XMLStreamException e) {
            throw new XmlIOException(e);
        }
    }

    @Override
    public void writeStartDocument(String encoding, String version) throws XmlIOException {
        try {
            streamWriter.writeStartDocument(encoding, version);
        } catch (XMLStreamException e) {
            throw new XmlIOException(e);
        }
    }


    @Override
    public void addPrefix(String prefix, String namespace) throws XmlIOException {
        writeAttribute("xmlns:" + prefix, namespace);
    }

    @Override
    public void writeStartElement(String name) throws XmlIOException {
        try {
            streamWriter.writeStartElement(name);
        } catch (XMLStreamException e) {
            throw new XmlIOException(e);
        }
    }

    @Override
    public void writeAttribute(String localname, String value) throws XmlIOException {
        try {
            streamWriter.writeAttribute(localname, value);
        } catch (XMLStreamException e) {
            throw new XmlIOException(e);
        }
    }

    @Override
    public void writeEndElement() throws XmlIOException {
        try {
            streamWriter.writeEndElement();
        } catch (XMLStreamException e) {
            throw new XmlIOException(e);
        }
    }

    @Override
    public void writeEndDocument() throws XmlIOException {
        try {
            streamWriter.writeEndDocument();
        } catch (XMLStreamException e) {
            throw new XmlIOException(e);
        }
    }

    @Override
    public void flush() throws XmlIOException {
        try {
            streamWriter.flush();
        } catch (XMLStreamException e) {
            throw new XmlIOException(e);
        }
    }

    @Override
    public void close() throws IOException {
        try {
            streamWriter.close();
        } catch (XMLStreamException e) {
            throw new XmlIOException(e);
        }
    }
}
