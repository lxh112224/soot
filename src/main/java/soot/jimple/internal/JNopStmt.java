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

import java.util.List;

import soot.Unit;
import soot.UnitPrinter;
import soot.baf.Baf;
import soot.jimple.Jimple;
import soot.jimple.JimpleToBafContext;
import soot.jimple.NopStmt;
import soot.jimple.StmtSwitch;
import soot.util.Switch;

public class JNopStmt extends AbstractStmt implements NopStmt {
  public JNopStmt() {}

  @Override
  public Object clone() {
    return new JNopStmt();
  }

  @Override
  public String toString() {
    return Jimple.NOP;
  }

  @Override
  public void toString(UnitPrinter up) {
    up.literal(Jimple.NOP);
  }

  @Override
  public void apply(Switch sw) {
    ((StmtSwitch) sw).caseNopStmt(this);
  }

  @Override
  public void convertToBaf(JimpleToBafContext context, List<Unit> out) {
    Unit u = Baf.v().newNopInst();
    u.addAllTagsOf(this);
    out.add(u);
  }

  @Override
  public boolean fallsThrough() {
    return true;
  }

  @Override
  public boolean branches() {
    return false;
  }
}
