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

import java.io.Closeable;

/**
 * Because the Android version does not have javax.xml.stream.* we have to shim out the uses of it.
 *
 * Then in the Android version of the library it will use the XML libraries that it has available to it.
 *
 * Also the android system does not allow you to have classes in your jars that are part of the
 * javax.* package.  So we can't manually add them into our project.
 *
 * This way the same RDFDatabaseWriter code can work for both.
 */
public interface XmlStreamWriter extends Closeable {

    void writeStartDocument() throws XmlIOException;

    void writeStartDocument(String encoding, String version) throws XmlIOException;

    void writeStartElement(String name) throws XmlIOException;

    void writeAttribute(String localname, String value) throws XmlIOException;

    void writeEndElement() throws XmlIOException;

    void writeEndDocument() throws XmlIOException;

    void flush() throws XmlIOException;

    void addPrefix(String prefix, String namespace) throws XmlIOException;
}
