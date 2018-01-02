/* Soot - a J*va Optimization Framework
 * Copyright (C) 1999 Patrick Lam
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

/*
 * Modified by the Sable Research Group and others 1997-1999.
 * See the 'credits' file distributed with Soot for the complete list of
 * contributors.  (Soot is distributed at http://www.sable.mcgill.ca/soot)
 */

package soot.jimple.internal;

import java.util.ArrayList;
import java.util.List;

import soot.ArrayType;
import soot.RefType;
import soot.Type;
import soot.Unit;
import soot.UnitPrinter;
import soot.Value;
import soot.ValueBox;
import soot.baf.Baf;
import soot.grimp.PrecedenceTest;
import soot.jimple.CastExpr;
import soot.jimple.ConvertToBaf;
import soot.jimple.ExprSwitch;
import soot.jimple.Jimple;
import soot.jimple.JimpleToBafContext;
import soot.util.Switch;

@SuppressWarnings("serial")
public abstract class AbstractCastExpr implements CastExpr, ConvertToBaf {
  final ValueBox opBox;
  Type type;

  AbstractCastExpr(Value op, Type type) {
    this(Jimple.v().newImmediateBox(op), type);
  }

  @Override
  public abstract Object clone();

  protected AbstractCastExpr(ValueBox opBox, Type type) {
    this.opBox = opBox;
    this.type = type;
  }

  @Override
  public boolean equivTo(Object o) {
    if (o instanceof AbstractCastExpr) {
      AbstractCastExpr ace = (AbstractCastExpr) o;
      return opBox.getValue().equivTo(ace.opBox.getValue()) && type.equals(ace.type);
    }
    return false;
  }

  /** Returns a hash code for this object, consistent with structural equality. */
  @Override
  public int equivHashCode() {
    return opBox.getValue().equivHashCode() * 101 + type.hashCode() + 17;
  }

  @Override
  public String toString() {
    return "(" + type.toString() + ") " + opBox.getValue().toString();
  }

  @Override
  public void toString(UnitPrinter up) {
    up.literal("(");
    up.type(type);
    up.literal(") ");
    if (PrecedenceTest.needsBrackets(opBox, this)) {
      up.literal("(");
    }
    opBox.toString(up);
    if (PrecedenceTest.needsBrackets(opBox, this)) {
      up.literal(")");
    }
  }

  @Override
  public Value getOp() {
    return opBox.getValue();
  }

  @Override
  public void setOp(Value op) {
    opBox.setValue(op);
  }

  @Override
  public ValueBox getOpBox() {
    return opBox;
  }

  @Override
  public final List<ValueBox> getUseBoxes() {
    List<ValueBox> list = new ArrayList<>();

    list.addAll(opBox.getValue().getUseBoxes());
    list.add(opBox);

    return list;
  }

  @Override
  public Type getCastType() {
    return type;
  }

  @Override
  public void setCastType(Type castType) {
    this.type = castType;
  }

  @Override
  public Type getType() {
    return type;
  }

  @Override
  public void apply(Switch sw) {
    ((ExprSwitch) sw).caseCastExpr(this);
  }

  @Override
  public void convertToBaf(JimpleToBafContext context, List<Unit> out) {
    final Type toType = getCastType();
    final Type fromType = getOp().getType();

    ((ConvertToBaf) getOp()).convertToBaf(context, out);

    Unit u;
    if (toType instanceof ArrayType || toType instanceof RefType) {
      u = Baf.v().newInstanceCastInst(toType);
    } else {
      if (!fromType.equals(toType)) {
        u = Baf.v().newPrimitiveCastInst(fromType, toType);
      } else {
        u = Baf.v().newNopInst();
      }
    }

    out.add(u);

    u.addAllTagsOf(context.getCurrentUnit());
  }
}
