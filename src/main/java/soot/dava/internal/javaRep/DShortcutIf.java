package soot.dava.internal.javaRep;

import java.util.ArrayList;
import java.util.List;

import soot.Type;
import soot.UnitPrinter;
import soot.ValueBox;
import soot.jimple.Expr;
import soot.util.Switch;

public class DShortcutIf implements Expr {
  ValueBox testExprBox;
  ValueBox trueExprBox;
  ValueBox falseExprBox;
  Type exprType;

  public DShortcutIf(ValueBox test, ValueBox left, ValueBox right) {
    testExprBox = test;
    trueExprBox = left;
    falseExprBox = right;
  }

  @Override
  public Object clone() {
    // does not work
    return this;
  }

  @Override
  public List getUseBoxes() {
    List toReturn = new ArrayList();
    toReturn.addAll(testExprBox.getValue().getUseBoxes());
    toReturn.add(testExprBox);
    toReturn.addAll(trueExprBox.getValue().getUseBoxes());
    toReturn.add(trueExprBox);
    toReturn.addAll(falseExprBox.getValue().getUseBoxes());
    toReturn.add(falseExprBox);
    return toReturn;
  }

  @Override
  public Type getType() {
    return exprType;
  }

  @Override
  public String toString() {
    String toReturn = "";
    toReturn += testExprBox.getValue().toString();
    toReturn += " ? ";
    toReturn += trueExprBox.getValue().toString();
    toReturn += " : ";
    toReturn += falseExprBox.getValue().toString();
    return toReturn;
  }

  @Override
  public void toString(UnitPrinter up) {
    testExprBox.getValue().toString(up);
    up.literal(" ? ");
    trueExprBox.getValue().toString(up);
    up.literal(" : ");
    falseExprBox.getValue().toString(up);
  }

  @Override
  public void apply(Switch sw) {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean equivTo(Object o) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public int equivHashCode() {
    int toReturn = 0;
    toReturn += testExprBox.getValue().equivHashCode();
    toReturn += trueExprBox.getValue().equivHashCode();
    toReturn += falseExprBox.getValue().equivHashCode();
    return toReturn;
  }
}
