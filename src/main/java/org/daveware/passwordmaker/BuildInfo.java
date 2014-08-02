/*
 * This file is part of Passwordmaker-je-lib.
 * Copyright (C) 2011 Dave Marotti
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
package org.daveware.passwordmaker;

import java.io.InputStream;
import java.util.Properties;

/**
 * Class to load and store the build information from build-info.properties.
 *
 * @author Dave Marotti
 */
@SuppressWarnings("UnusedDeclaration")
public class BuildInfo {
    String version = "Internal";
    String buildDate = "Internal";
    String buildTime = "Internal";

    public BuildInfo() {
        InputStream in = null;
        try {
            Properties prop = new Properties();
            in = getClass().getResourceAsStream("/build-info.properties");
            if (in == null) return; //fixes a throw from happening
            prop.load(in);
            version = prop.getProperty("Implementation-Version");
            buildDate = prop.getProperty("Built-On");
            buildTime = prop.getProperty("Built-At");
        } catch (Exception ignored) {
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception ignored) {
                }
            }
        }
    }

    public String getVersion() {
        return version;
    }

    public String getBuildDate() {
        return buildDate;
    }

    public String getBuildTime() {
        return buildTime;
    }
}
