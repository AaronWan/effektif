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
package com.effektif.workflow.test.examples;

import com.effektif.workflow.api.Configuration;
import com.effektif.workflow.api.WorkflowEngine;
import com.effektif.workflow.api.activities.ReceiveTask;
import com.effektif.workflow.api.condition.Equals;
import com.effektif.workflow.api.condition.Or;
import com.effektif.workflow.api.condition.Unspecified;
import com.effektif.workflow.api.model.TriggerInstance;
import com.effektif.workflow.api.model.VariableValues;
import com.effektif.workflow.api.model.WorkflowId;
import com.effektif.workflow.api.workflow.ExecutableWorkflow;
import com.effektif.workflow.api.workflow.Transition;
import com.effektif.workflow.api.workflowinstance.WorkflowInstance;
import com.effektif.workflow.impl.bpmn.BpmnMapper;
import com.effektif.workflow.impl.json.DefaultJsonStreamMapper;
import com.effektif.workflow.impl.json.JsonStreamMapper;
import com.effektif.workflow.impl.memory.MemoryConfiguration;
import com.google.common.collect.Lists;
import jodd.io.FileUtil;
import org.junit.Test;

import java.io.CharArrayReader;
import java.io.IOException;

/**
 * @author Tom Baeyens
 */
public class ApiExamplesTest {

  @Test
  public void testApiExample() {
    // Create the default (in-memory) workflow engine
    Configuration configuration = new MemoryConfiguration();
    configuration.start();
    WorkflowEngine workflowEngine = configuration.getWorkflowEngine();
    
    // Create a workflow
    ExecutableWorkflow workflow = new ExecutableWorkflow()
      .sourceWorkflowId("Release")
      .activity("Move open issues", new ReceiveTask()
        .transitionToNext())
      .activity("Check continuous integration", new ReceiveTask());
    
    // Deploy the workflow to the engine
    WorkflowId workflowId = workflowEngine
      .deployWorkflow(workflow)
      .checkNoErrorsAndNoWarnings()
      .getWorkflowId();

    // Start a new workflow instance
    WorkflowInstance workflowInstance = workflowEngine
      .start(new TriggerInstance()
        .workflowId(workflowId));

    System.err.println(configuration.get(JsonStreamMapper.class).write(workflow));
  }



  @Test
  public void testApiLoveExample() throws IOException {
    // Create the default (in-memory) workflow engine
    Configuration configuration = new MemoryConfiguration();
    configuration.start();
    WorkflowEngine workflowEngine = configuration.getWorkflowEngine();

    // Create a workflow
    ExecutableWorkflow workflow = new ExecutableWorkflow();
    String path="/Users/Aaron/Documents/facishare/code/gitfirstshare/opensourcestudy/activiti/src/main/java/com/opensource/test.bpmn";
    byte[] bytes=FileUtil.readBytes(path);
//     String str = getConfiguration().get(JsonService.class).objectToJsonString(new BpmnMapper(new DefaultJsonStreamMapper()).readFromString(new String(bytes)));
    WorkflowId id=workflow.getId();
    workflow= (ExecutableWorkflow) new BpmnMapper(new DefaultJsonStreamMapper()).readFromString(new String(bytes));
    workflow.setId(id);
    workflow.setSourceWorkflowId("love_flow");
//    workflow.setTransitions(Lists.newArrayList(new Transition().fromId("_7").toId("_10").condition(new Or()
//            .condition(new Unspecified())
//            .condition(new Equals()))));
    // Deploy the workflow to the engine
    WorkflowId workflowId = workflowEngine
            .deployWorkflow(workflow)
            .checkNoErrorsAndNoWarnings()
            .getWorkflowId();

    // Start a new workflow instance
    WorkflowInstance workflowInstance = workflowEngine
            .start(new TriggerInstance()
                    .workflowId(workflowId));


    System.err.println(configuration.get(JsonStreamMapper.class).write(workflow));
  }


}
