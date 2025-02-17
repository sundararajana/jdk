/*
 * Copyright (c) 2022, 2024, Oracle and/or its affiliates. All rights reserved.
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
 * @test
 * @summary Checks that -XX:+PrintC1Statistics works
 * @bug 8296969
 * @requires vm.debug == true & vm.compiler1.enabled
 * @library /test/lib
 * @run main/othervm -XX:+Verbose compiler.c1.TestPrintC1Statistics
 */

package compiler.c1;

import java.util.ArrayList;
import java.util.List;

import jdk.test.lib.process.OutputAnalyzer;
import jdk.test.lib.process.ProcessTools;

public class TestPrintC1Statistics {
    public static void main(String[] args) throws Exception {
        List<String> options = new ArrayList<String>();
        options.add("-XX:+PrintC1Statistics");
        options.add("--version");

        OutputAnalyzer oa = ProcessTools.executeTestJava(options);

        oa.shouldHaveExitValue(0).shouldContain("C1 Runtime statistics");
    }
 }
