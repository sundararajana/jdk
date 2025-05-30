/*
 * Copyright (c) 2018, 2025, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

/*
 * This class is intended to be run in the single source-file launcher
 * mode defined by JEP 330. It checks the operation of the getResource*
 * methods provided by the MemoryClassLoader used to run the compiled
 * classes.
 *
 * The class uses the ClassFile library to validate the contents of
 * the URLs and streams returned by the methods being tested.
 *
 * $ java /path/to/CLTest.java
 */
package p.q;

import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.classfile.ClassModel;
import java.lang.classfile.ClassFile;
import java.util.spi.ToolProvider;

public class CLTest {
    public static void main(String... args) throws Exception {
        try {
            var test = new CLTest();
            test.loadToolProviderByName(); // run first to create Tool.class
            test.getGetResources();
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    void loadToolProviderByName() {
        ServiceLoader.load(ToolProvider.class).stream()
                .map(ServiceLoader.Provider::get)
                .filter(toolProvider -> toolProvider.name().equals("Tool"))
                .findFirst()
                .orElseThrow();
    }

    void getGetResources() throws Exception {
        String[] names = {
            // scheme -> file:
                "Tool.java",
                "p/q/CLTest.java",
                "META-INF/services/java.util.spi.ToolProvider",
            // scheme -> sourcelauncher-memoryclassloaderNNN:
                "Tool.class",
                "p/q/CLTest.class",
                "p/q/CLTest$Inner.class",
                "p/q/CLTest2.class",
            // scheme -> jrt:
                "java/lang/Object.class",
            // no scheme applicable
                "UNKNOWN.class",
                "UNKNOWN"
        };

        for (String name : names) {
            testGetResource(name);
            testGetResources(name);
            testGetResourceAsStream(name);
        }

        if (errors > 0) {
            throw new Exception(errors + " errors found");
        }
    }

    void testGetResource(String name) {
        System.err.println("testGetResource: " + name);
        try {
            ClassLoader cl = getClass().getClassLoader();
            URL u = cl.getResource(name);
            if (u == null) {
                if (!name.contains("UNKNOWN")) {
                    error("resource not found: " + name);
                }
                return;
            }

            checkURL(u);
            checkClass(name, u);

        } catch (Throwable t) {
            t.printStackTrace(System.err);
            error("unexpected exception: " + t);
        }
    }

    void testGetResources(String name) {
        System.err.println("testGetResources: " + name);
        try {
            ClassLoader cl = getClass().getClassLoader();
            Enumeration<URL> e = cl.getResources(name);
            List<URL> list = new ArrayList<>();
            while (e.hasMoreElements()) {
                list.add(e.nextElement());
            }

            if (name.contains("META-INF")) {
                if (list.size() == 0) {
                    error("resource not found: " + name);
                }
                // one or more resources found, as expected
                return;
            }

            switch (list.size()) {
                case 0:
                    if (!name.contains("UNKNOWN")) {
                        error("resource not found: " + name);
                    }
                    break;

                case 1:
                    checkClass(name, list.get(0));
                    break;

                default:
                    error("unexpected resources found: " + list);
            }

        } catch (Throwable t) {
            t.printStackTrace(System.err);
            error("unexpected exception: " + t);
        }
    }

    void testGetResourceAsStream(String name) {
        System.err.println("testGetResourceAsStream: " + name);
        try {
            ClassLoader cl = getClass().getClassLoader();
            try (InputStream in = cl.getResourceAsStream(name)) {
                if (in == null) {
                    if (!name.contains("UNKNOWN")) {
                        error("resource not found: " + name);
                    }
                    return;
                }

                checkClass(name, in);
            }
        } catch (Throwable t) {
            t.printStackTrace(System.err);
            error("unexpected exception: " + t);
        }
    }

    void checkClass(String name, URL u) throws Exception {
        try (InputStream in = u.openConnection().getInputStream()) {
            checkClass(name, in);
        }
    }

    void checkClass(String name, InputStream in) throws Exception {
        if (!name.endsWith(".class")) {
            return; // ignore non-class resources
        }
        ClassModel cf = ClassFile.of().parse(in.readAllBytes());
        System.err.println("    class " + cf.thisClass().asInternalName());
        if (!name.equals(cf.thisClass().asInternalName() + ".class")) {
            error("unexpected class found: " + cf.thisClass().asInternalName());
        }
    }

    void checkURL(URL url) {
        try {
            // verify the URL is formatted strictly according to RFC2396
            url.toURI();
        } catch (URISyntaxException e) {
            error("bad URL: " + url + "; " + e);
        }
    }

    void error(String message) {
        System.err.println("Error: " + message);
        errors++;
    }

    int errors = 0;

    class Inner { }
}

class CLTest2 { }
