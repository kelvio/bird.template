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
package com.hubspot.jinjava.lib.tag;

import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;

import com.hubspot.jinjava.interpret.InterpretException;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.hubspot.jinjava.tree.Node;
import com.hubspot.jinjava.tree.TagNode;
import com.hubspot.jinjava.util.ObjectTruthValue;

/**
 * {% if expr %}
 * {% else %}
 * {% endif %}
 * 
 * {% if expr %}
 * {% endif %}
 * 
 * @author anysome
 */
public class IfTag implements Tag {

  private static final String TAGNAME = "if";
  private static final String ENDTAGNAME = "endif";

  @Override
  public String interpret(TagNode tagNode, JinjavaInterpreter interpreter) {
    if(StringUtils.isBlank(tagNode.getHelpers())) {
      throw new InterpretException("Tag 'if' expects expression", tagNode.getLineNumber());
    }
    
    Iterator<Node> nodeIterator = tagNode.getChildren().iterator();
    TagNode nextIfElseTagNode = (TagNode) tagNode;
    
    while(nextIfElseTagNode != null && !evaluateIfElseTagNode(nextIfElseTagNode, interpreter)) {
      nextIfElseTagNode = findNextIfElseTagNode(nodeIterator);
    }
    
    StringBuilder sb = new StringBuilder();
    if(nextIfElseTagNode != null) {
      while(nodeIterator.hasNext()) {
        Node n = nodeIterator.next();
        if(n.getName().equals(ElseIfTag.ELSEIF) || n.getName().equals(ElseTag.ELSE)) {
          break;
        }
        sb.append(n.render(interpreter));
      }
    }
    
    return sb.toString();
  }

  private TagNode findNextIfElseTagNode(Iterator<Node> nodeIterator) {
    while(nodeIterator.hasNext()) {
      Node node = nodeIterator.next();
      if(TagNode.class.isAssignableFrom(node.getClass())) {
        TagNode tag = (TagNode) node;
        if(tag.getName().equals(ElseIfTag.ELSEIF) || tag.getName().equals(ElseTag.ELSE)) {
          return tag;
        }
      }
    }

    return null;
  }

  protected boolean evaluateIfElseTagNode(TagNode tagNode, JinjavaInterpreter interpreter) {
    if(tagNode.getName().equals(ElseTag.ELSE)) {
      return true;
    }

    return ObjectTruthValue.evaluate(interpreter.resolveELExpression(tagNode.getHelpers(), tagNode.getLineNumber()));
  }
  
  @Override
  public String getEndTagName() {
    return ENDTAGNAME;
  }

  @Override
  public String getName() {
    return TAGNAME;
  }

}
