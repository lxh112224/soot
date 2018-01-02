/* Soot - a J*va Optimization Framework
 * Copyright (C) 2003 Feng Qian
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 */

/**
 * Simulates the native method side effects in class java.lang.Object.
 *
 * @author Feng Qian
 */
package soot.jimple.toolkits.pointer.nativemethods;

import soot.SootMethod;
import soot.jimple.toolkits.pointer.representations.Environment;
import soot.jimple.toolkits.pointer.representations.ReferenceVariable;
import soot.jimple.toolkits.pointer.util.NativeHelper;

public class JavaLangObjectNative extends NativeMethodClass {
  public JavaLangObjectNative(NativeHelper helper) {
    super(helper);
  }

  /**
   * Implements the abstract method simulateMethod. It distributes the request to the corresponding
   * methods by signatures.
   */
  @Override
  public void simulateMethod(
      SootMethod method,
      ReferenceVariable thisVar,
      ReferenceVariable returnVar,
      ReferenceVariable params[]) {
    String subSignature = method.getSubSignature();

    /* Driver */

    if (subSignature.equals("java.lang.Class getClass()")) {
      java_lang_Object_getClass(method, thisVar, returnVar, params);
      return;

    } else if (subSignature.equals("java.lang.Object clone()")) {
      java_lang_Object_clone(method, thisVar, returnVar, params);
      return;

    } else {
      defaultMethod(method, thisVar, returnVar, params);
      return;
    }
  }

  /** ********************* java.lang.Object ******************** */
  /**
   * The return variable is assigned an abstract object representing all classes (UnknowClassObject)
   * from environment.
   *
   * <p>public final native java.lang.Class getClass();
   */
  public void java_lang_Object_getClass(
      SootMethod method,
      ReferenceVariable thisVar,
      ReferenceVariable returnVar,
      ReferenceVariable params[]) {
    helper.assignObjectTo(returnVar, Environment.v().getClassObject());
  }

  /**
   * Creates and returns a copy of this object. The precise meaning of "copy" may depend on the
   * class of the object. The general intent is that, for any object x, the expression:
   *
   * <p>x.clone() != x
   *
   * <p>will be true, and that the expression:
   *
   * <p>x.clone().getClass() == x.getClass()
   *
   * <p>will be true, but these are not absolute requirements. While it is typically the case that:
   *
   * <p>x.clone().equals(x)
   *
   * <p>will be true, this is not an absolute requirement. Copying an object will typically entail
   * creating a new instance of its class, but it also may require copying of internal data
   * structures as well. No constructors are called.
   *
   * <p>NOTE: it may raise an exception, the decision of cloning made by analysis by implementing
   * the ReferneceVariable.cloneObject() method.
   *
   * <p>protected native java.lang.Object clone() throws java.lang.CloneNotSupported
   */
  public void java_lang_Object_clone(
      SootMethod method,
      ReferenceVariable thisVar,
      ReferenceVariable returnVar,
      ReferenceVariable params[]) {
    if (thisVar == null) {
      throw new RuntimeException("Need a 'this' variable to perform a clone()");
    }

    ReferenceVariable newVar = helper.cloneObject(thisVar);
    helper.assign(returnVar, newVar);
  }

  /**
   * Following methods have NO side effect
   *
   * <p>private static native void registerNatives(); public native int hashCode(); public final
   * native void notify(); public final native void notifyAll(); public final native void wait(long)
   * throws java.lang.InterruptedException;
   */
}
