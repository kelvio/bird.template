/**********************************************************************
Copyright (c) 2014 HubSpot Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 **********************************************************************/
package com.hubspot.jinjava.tree;

import java.util.LinkedList;

import org.apache.commons.lang3.StringUtils;

import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.hubspot.jinjava.tree.parse.ExpressionToken;
import com.hubspot.jinjava.util.Logging;
import com.hubspot.jinjava.util.ObjectValue;

public class ExpressionNode extends Node {
  private static final long serialVersionUID = 341642231109911346L;

  private final ExpressionToken master;

  public ExpressionNode(ExpressionToken token) {
    super(token, token.getLineNumber());
    master = token;
  }

  @Override
  public String render(JinjavaInterpreter interpreter) {
    Object var = interpreter.resolveELExpression(master.getExpr(), getLineNumber());
    
    String result = ObjectValue.printable(var);
    if(!StringUtils.equals(result, master.getImage()) && StringUtils.contains(result, "{{")) {
      try {
        return interpreter.renderString(result);
      }
      catch(Exception e) {
        Logging.ENGINE_LOG.warn("Error rendering variable node result", e);
      }
    }

    return result;
  }

  @Override
  public String toString() {
    return master.toString();
  }

  @Override
  public String getName() {
    return getClass().getSimpleName();
  }

  @Override
  public Node clone() {
    Node clone = new ExpressionNode(master);
    clone.setChildren(new LinkedList<>(this.getChildren()));
    return clone;
  }
}
