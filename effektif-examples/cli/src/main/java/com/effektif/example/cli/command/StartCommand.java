/*
 * Copyright 2014 Effektif GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.effektif.example.cli.command;

import java.io.PrintWriter;

import com.effektif.workflow.api.Configuration;
import com.effektif.workflow.api.model.TriggerInstance;

/**
 * Starts the workflow with the given source ID.
 */
public class StartCommand implements CommandImpl {

  @Override
  public void execute(CommandLine command, Configuration configuration, PrintWriter out) {
    final String sourceWorkflowId = command.getArgument();
    final TriggerInstance trigger = new TriggerInstance().sourceWorkflowId(sourceWorkflowId);
    configuration.getWorkflowEngine().start(trigger);
    out.println("Started workflow " + sourceWorkflowId);
  }
}
