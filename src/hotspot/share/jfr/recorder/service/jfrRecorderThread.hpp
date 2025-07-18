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
 *
 */

#ifndef SHARE_JFR_RECORDER_SERVICE_JFRRECORDERTHREAD_HPP
#define SHARE_JFR_RECORDER_SERVICE_JFRRECORDERTHREAD_HPP

#include "memory/allStatic.hpp"
#include "runtime/javaThread.hpp"
#include "utilities/debug.hpp"

class JfrCheckpointManager;
class JfrPostBox;
class Thread;

class JfrRecorderThreadEntry : AllStatic {
 private:
  static JfrPostBox* _post_box;

 public:
  static JfrPostBox& post_box();
  static bool start(JfrCheckpointManager* cp_manager, JfrPostBox* post_box, TRAPS);
};

class JfrRecorderThread : public JavaThread {
 public:
  JfrRecorderThread(ThreadFunction entry_point) : JavaThread(entry_point) {}
  virtual ~JfrRecorderThread() {}

  virtual bool is_JfrRecorder_thread() const { return true; }
};

#endif // SHARE_JFR_RECORDER_SERVICE_JFRRECORDERTHREAD_HPP
