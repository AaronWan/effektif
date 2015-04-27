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
package com.effektif.mongo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.joda.time.LocalDateTime;

import com.effektif.workflow.impl.data.types.DateTypeImpl;
import com.effektif.workflow.impl.mapper.JsonMapper;
import com.mongodb.BasicDBObject;


/** adds some transformations so jackson deserialization can be used on a mongodb object.
 *  
 * @author Tom Baeyens
 */
@Deprecated
public class MongoMapper<T> {

  Class<T> type;
  JsonMapper jsonMapper;
  boolean convertId;
  List<String> userIdFields = new ArrayList<>();
  List<String> timeFields = new ArrayList<>();

  public MongoMapper(Class<T> type, JsonMapper jsonMapper) {
    this.type = type;
    this.jsonMapper = jsonMapper;
  }

  public MongoMapper convertId() {
    this.convertId = true;
    return this;
  }

  public MongoMapper convertUserId(String fieldName) {
    userIdFields.add(fieldName);
    return this;
  }

  public MongoMapper convertTime(String fieldName) {
    timeFields.add(fieldName);
    return this;
  }
  
  public T read(BasicDBObject dbObject) {
    if (dbObject==null) {
      return null;
    }
    
    if (convertId) {
      Object id = dbObject.remove("_id");
      if (id!=null) {
        dbObject.put("id", id.toString());
      }
    }
    
    for (String userIdField: userIdFields) {
      String value = (String) dbObject.get(userIdField);
      if (value!=null) {
        dbObject.put(userIdField, value.toString());
      }
    }

    for (String timeField: timeFields) {
      Date value = (Date) dbObject.get(timeField);
      if (value!=null) {
        dbObject.put(timeField, DateTypeImpl.printer.print(new LocalDateTime(value)));
      }
    }

    // return jsonMapper.jsonMapToObject(dbObject, type);
    throw new RuntimeException("TODO i didn't expect this was still used");
  }

  public BasicDBObject write(Object object) {
    if (object==null) {
      return null;
    }
    
    Map<String,Object> jsonMap = null; // jsonMapper.objectToJsonMap(object);
    if (true) throw new RuntimeException("TODO i didn't expect this was still used");
    
    BasicDBObject dbObject = new BasicDBObject(jsonMap);

    if (convertId) {
      String id = (String) jsonMap.remove("id");
      if (id!=null) {
        dbObject.put("_id", new ObjectId(id));
      }
    }
    
    for (String userIdField: userIdFields) {
      String value = (String) dbObject.get(userIdField);
      if (value!=null) {
        dbObject.put(userIdField, new ObjectId(value));
      }
    }

    for (String timeField: timeFields) {
      String value = (String) dbObject.get(timeField);
      if (value!=null) {
        dbObject.put(timeField, DateTypeImpl.formatter.parseLocalDateTime(value));
      }
    }

    return dbObject;
  }
}
