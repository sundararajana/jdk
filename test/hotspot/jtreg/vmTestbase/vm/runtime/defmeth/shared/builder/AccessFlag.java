/*
 * Copyright (c) 2013, 2025, Oracle and/or its affiliates. All rights reserved.
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

package vm.runtime.defmeth.shared.builder;

import static org.objectweb.asm.Opcodes.*;

/**
 * Access flags, related to element visibility (public, private, etc).
 *
 * Introduced to simplify management of access flags due to peculiarity how
 * package-private is represented ((ACC_PUBLIC | ACC_PROTECTED | ACC_PRIVATE) == 0).
 */
public enum AccessFlag {
    PUBLIC          (ACC_PUBLIC),
    PROTECTED       (ACC_PROTECTED),
    PRIVATE         (ACC_PRIVATE),
    PACKAGE_PRIVATE (0);

    public final int mask;

    AccessFlag(int mask) {
        this.mask = mask;
    }
}
