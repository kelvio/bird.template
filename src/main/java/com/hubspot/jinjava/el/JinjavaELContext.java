package com.hubspot.jinjava.el;

import java.lang.reflect.Method;

import javax.el.ELResolver;
import javax.el.FunctionMapper;

import de.odysseus.el.util.SimpleContext;

public class JinjavaELContext extends SimpleContext {

  private MacroFunctionMapper functionMapper;
  
  public JinjavaELContext() {
    super();
  }

  public JinjavaELContext(ELResolver resolver) {
    super(resolver);
  }

  @Override
  public FunctionMapper getFunctionMapper() {
    if(functionMapper == null) {
      functionMapper = new MacroFunctionMapper();
    }
    return functionMapper;
  }
  
  @Override
  public void setFunction(String prefix, String localName, Method method) {
    if(functionMapper == null) {
      functionMapper = new MacroFunctionMapper();
    }
    functionMapper.setFunction(prefix, localName, method);
  }
  
}
