/* Copyright (c) 2014, Effektif GmbH.
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
 * limitations under the License. */
package com.effektif.workflow.api.mapper;

import java.util.List;

import com.effektif.workflow.api.condition.Condition;
import com.effektif.workflow.api.model.RelativeTime;
import org.joda.time.LocalDateTime;

import com.effektif.workflow.api.model.Id;
import com.effektif.workflow.api.workflow.Binding;


/** an abstraction that allows {@link BpmnReadable}s to read 
 * their internal data from any BPMN reader.
 * 
 * The goal is to make it easy to implement BPMN (de)serialization 
 * by offering an API that is similar to the JSON abstractions.
 * 
 * The methods in this BPMN reader apply to the current element.
 * 
 * @author Tom Baeyens
 */
public interface BpmnReader {

  /** extracts and removes elements in the BPMN namespace from the current element.
   * Typical use:
   * <pre>
   * for (XmlElement nestedElement: r.readElementsBpmn("nested")) {
   *   r.startElement(nestedElement);
   *   ... read stuff from the nested elements with the r.readXxxx methods ...
   *   r.endElement(nestedElement);
   * }
   * </pre> 
   */
  List<XmlElement> readElementsBpmn(String localPart);

  /** extracts and removes elements in the Effektif namespace from the current element.
   * Typical use:
   * <pre>
   * for (XmlElement nestedElement: r.readElementsBpmn("nested")) {
   *   r.startElement(nestedElement);
   *   ... read stuff from the nested elements with the r.readXxxx methods ...
   *   r.endElement(nestedElement);
   * }
   * </pre> 
   */
  List<XmlElement> readElementsEffektif(String localPart);

  /** Extracts and removes elements whose name corresponds to the given model class name. */
  List<XmlElement> readElementsEffektif(Class modelClass);

  /** set the current element on which the other readXxxx methods apply.
   * Always ensure there is a matching endElement() called afterwards.*/
  void startElement(XmlElement xmlElement);
  /** restores the previous element as the current element on which the other readXxxx methods apply.
   * This should be matching an earlier startElement(XmlElement).*/
  void endElement();
  
  /** set the BPMN extensionsElements as the current element on which the other readXxxx methods apply.
   * Always ensure there is a matching endExtensionElements() called afterwards.*/
  void startExtensionElements();
  /** restores the previous element as the current element on which the other readXxxx methods apply.
   * This should be matching an earlier startExtensionElements().*/
  void endExtensionElements();
  
  /** reads all scope information like nested activities (flowNodes), transitions (sequenceFlows)
   * variables and timers.*/
  void readScope();

  /** Reads a binding from the element whose name corresponds to the given model class. */
  <T> Binding<T> readBinding(Class modelClass, Class<T> type);

  /** Reads a binding like
   * e.g. <e:assignee value="42"/> or <e:assignee expression="v1.fullName"/>. */
  <T> Binding<T> readBinding(String elementName, Class<T> type);

  /** Returns a list of bindings like
   * e.g. <e:assignee value="42"/> or <e:assignee expression="v1.fullName"/>. */
  <T> List<Binding<T>> readBindings(String elementName, Class<T> type);
  
  /** Reads the given documentation string as a BPMN <code>documentation</code> element. */
  String readDocumentation();

  /** Reads a boolean string field as an attribute on the current xml element in the Effektif namespace */
  Boolean readBooleanAttributeEffektif(String localPart);

  /** Reads a string field as an attribute on the current xml element in the BPMN namespace */
  String readStringAttributeBpmn(String localPart);

  /** Reads a string field as an attribute on the current xml element in the Effektif namespace */
  String readStringAttributeEffektif(String localPart);

  /** Reads an id as an attribute on the current xml element in the BPMN namespace */
  <T extends Id> T readIdAttributeBpmn(String localPart, Class<T> idType);

  /** Reads an id as an attribute on the current xml element in the Effektif namespace */
  <T extends Id> T readIdAttributeEffektif(String localPart, Class<T> idType);

  /** Reads a date field as an attribute on the current xml element in the Effektif namespace */
  LocalDateTime readDateAttributeEffektif(String localPart);

  /** Reads a {@link RelativeTime} from an element in the Effektif namespace. */
  RelativeTime readRelativeTimeEffektif(String localPart);

  /** Reads a string from the ‘value’ attribute in the current XML element in the Effektif namespace. */
  String readStringValue(String localPart);

  /** Reads a string as content text in the current xml element */
  String readTextEffektif(String localPart);

  /** TODO */
  XmlElement getUnparsedXml();

  /** Reads the first condition from the available condition elements. */
  Condition readCondition();

  /** Reads a list of condition instances from their various XML elements. */
  List<Condition> readConditions();
}
