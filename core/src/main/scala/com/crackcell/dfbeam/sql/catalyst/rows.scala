/*
 * Copyright 2014-2017 Shanyishanmei, Inc. All Rights Reserved.
 */

package com.crackcell.dfbeam.sql.catalyst

import com.crackcell.dfbeam.sql.Row

/**
  * Created by Menglong TAN on 3/20/17.
  */
/**
  * A row implementation that uses an array of objects as the underlying storage.  Note that, while
  * the array is not copied, and thus could technically be mutated after creation, this is not
  * allowed.
  */
class GenericRow(val values: Array[Any]) extends Row {
  /** No-arg constructor for serialization. */
  protected def this() = this(null)

  def this(size: Int) = this(new Array[Any](size))

  override def length: Int = values.length

  override def get(i: Int): Any = values(i)

  override def toSeq: Seq[Any] = values.clone()

  override def copy(): GenericRow = this
}
