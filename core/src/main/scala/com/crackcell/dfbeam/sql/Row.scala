/*
 * Copyright 2014-2017 Shanyishanmei, Inc. All Rights Reserved.
 */

package com.crackcell.dfbeam.sql

import scala.collection.JavaConverters._

import com.crackcell.dfbeam.sql.types.StructType

/**
  * Created by Menglong TAN on 3/19/17.
  */
trait Row extends Serializable {
  /** Number of elements in the Row. */
  def size: Int = length

  /** Number of elements in the Row. */
  def length: Int

  /**
    * Schema for the row.
    */
  def schema: StructType = null

  def apply(i: Int): Any = get(i)

  def get(i: Int): Any

  /** Checks whether the value at position i is null. */
  def isNullAt(i: Int): Boolean = get(i) == null

  /**
    * Returns the value at position i as a primitive boolean.
    *
    * @throws ClassCastException when data type does not match.
    * @throws NullPointerException when value is null.
    */
  def getBoolean(i: Int): Boolean = getAnyValAs[Boolean](i)

  /**
    * Returns the value at position i as a primitive byte.
    *
    * @throws ClassCastException when data type does not match.
    * @throws NullPointerException when value is null.
    */
  def getByte(i: Int): Byte = getAnyValAs[Byte](i)

  /**
    * Returns the value at position i as a primitive short.
    *
    * @throws ClassCastException when data type does not match.
    * @throws NullPointerException when value is null.
    */
  def getShort(i: Int): Short = getAnyValAs[Short](i)

  /**
    * Returns the value at position i as a primitive int.
    *
    * @throws ClassCastException when data type does not match.
    * @throws NullPointerException when value is null.
    */
  def getInt(i: Int): Int = getAnyValAs[Int](i)

  /**
    * Returns the value at position i as a primitive long.
    *
    * @throws ClassCastException when data type does not match.
    * @throws NullPointerException when value is null.
    */
  def getLong(i: Int): Long = getAnyValAs[Long](i)

  /**
    * Returns the value at position i as a primitive float.
    * Throws an exception if the type mismatches or if the value is null.
    *
    * @throws ClassCastException when data type does not match.
    * @throws NullPointerException when value is null.
    */
  def getFloat(i: Int): Float = getAnyValAs[Float](i)

  /**
    * Returns the value at position i as a primitive double.
    *
    * @throws ClassCastException when data type does not match.
    * @throws NullPointerException when value is null.
    */
  def getDouble(i: Int): Double = getAnyValAs[Double](i)

  /**
    * Returns the value at position i as a String object.
    *
    * @throws ClassCastException when data type does not match.
    */
  def getString(i: Int): String = getAs[String](i)

  /**
    * Returns the value at position i of decimal type as java.math.BigDecimal.
    *
    * @throws ClassCastException when data type does not match.
    */
  def getDecimal(i: Int): java.math.BigDecimal = getAs[java.math.BigDecimal](i)

  /**
    * Returns the value at position i of date type as java.sql.Date.
    *
    * @throws ClassCastException when data type does not match.
    */
  def getDate(i: Int): java.sql.Date = getAs[java.sql.Date](i)

  /**
    * Returns the value at position i of date type as java.sql.Timestamp.
    *
    * @throws ClassCastException when data type does not match.
    */
  def getTimestamp(i: Int): java.sql.Timestamp = getAs[java.sql.Timestamp](i)

  /**
    * Returns the value at position i of array type as a Scala Seq.
    *
    * @throws ClassCastException when data type does not match.
    */
  def getSeq[T](i: Int): Seq[T] = getAs[Seq[T]](i)

  /**
    * Returns the value at position i of array type as [[java.util.List]].
    *
    * @throws ClassCastException when data type does not match.
    */
  def getList[T](i: Int): java.util.List[T] =
    getSeq[T](i).asJava

  /**
    * Returns the value at position i of map type as a Scala Map.
    *
    * @throws ClassCastException when data type does not match.
    */
  def getMap[K, V](i: Int): scala.collection.Map[K, V] = getAs[Map[K, V]](i)

  /**
    * Returns the value at position i of array type as a [[java.util.Map]].
    *
    * @throws ClassCastException when data type does not match.
    */
  def getJavaMap[K, V](i: Int): java.util.Map[K, V] =
    getMap[K, V](i).asJava

  /**
    * Returns the value at position i of struct type as a [[Row]] object.
    *
    * @throws ClassCastException when data type does not match.
    */
  def getStruct(i: Int): Row = getAs[Row](i)

  /**
    * Returns the value at position i.
    * For primitive types if value is null it returns 'zero value' specific for primitive
    * ie. 0 for Int - use isNullAt to ensure that value is not null
    *
    * @throws ClassCastException when data type does not match.
    */
  def getAs[T](i: Int): T = get(i).asInstanceOf[T]

  /**
    * Returns the value of a given fieldName.
    * For primitive types if value is null it returns 'zero value' specific for primitive
    * ie. 0 for Int - use isNullAt to ensure that value is not null
    *
    * @throws UnsupportedOperationException when schema is not defined.
    * @throws IllegalArgumentException when fieldName do not exist.
    * @throws ClassCastException when data type does not match.
    */
  def getAs[T](fieldName: String): T = getAs[T](fieldIndex(fieldName))

  /**
    * Returns the index of a given field name.
    *
    * @throws UnsupportedOperationException when schema is not defined.
    * @throws IllegalArgumentException when a field `name` does not exist.
    */
  def fieldIndex(name: String): Int = {
    throw new UnsupportedOperationException("fieldIndex on a Row without schema is undefined.")
  }

  /**
    * Returns a Map(name -> value) for the requested fieldNames
    * For primitive types if value is null it returns 'zero value' specific for primitive
    * ie. 0 for Int - use isNullAt to ensure that value is not null
    *
    * @throws UnsupportedOperationException when schema is not defined.
    * @throws IllegalArgumentException when fieldName do not exist.
    * @throws ClassCastException when data type does not match.
    */
  def getValuesMap[T](fieldNames: Seq[String]): Map[String, T] = {
    fieldNames.map { name =>
      name -> getAs[T](name)
    }.toMap
  }

  override def toString(): String = s"[${this.mkString(",")}]"

  /**
    * Make a copy of the current [[Row]] object.
    */
  def copy(): Row

  /** Returns true if there are any NULL values in this row. */
  def anyNull: Boolean = {
    val len = length
    var i = 0
    while (i < len) {
      if (isNullAt(i)) { return true }
      i += 1
    }
    false
  }

  override def equals(o: Any): Boolean = {
    if (!o.isInstanceOf[Row]) return false
    val other = o.asInstanceOf[Row]

    if (other eq null) return false

    if (length != other.length) {
      return false
    }

    var i = 0
    while (i < length) {
      if (isNullAt(i) != other.isNullAt(i)) {
        return false
      }
      if (!isNullAt(i)) {
        val o1 = get(i)
        val o2 = other.get(i)
        o1 match {
          case b1: Array[Byte] =>
            if (!o2.isInstanceOf[Array[Byte]] ||
              !java.util.Arrays.equals(b1, o2.asInstanceOf[Array[Byte]])) {
              return false
            }
          case f1: Float if java.lang.Float.isNaN(f1) =>
            if (!o2.isInstanceOf[Float] || ! java.lang.Float.isNaN(o2.asInstanceOf[Float])) {
              return false
            }
          case d1: Double if java.lang.Double.isNaN(d1) =>
            if (!o2.isInstanceOf[Double] || ! java.lang.Double.isNaN(o2.asInstanceOf[Double])) {
              return false
            }
          case d1: java.math.BigDecimal if o2.isInstanceOf[java.math.BigDecimal] =>
            if (d1.compareTo(o2.asInstanceOf[java.math.BigDecimal]) != 0) {
              return false
            }
          case _ => if (o1 != o2) {
            return false
          }
        }
      }
      i += 1
    }
    true
  }

  /**
    * Return a Scala Seq representing the row. Elements are placed in the same order in the Seq.
    */
  def toSeq: Seq[Any] = {
    val n = length
    val values = new Array[Any](n)
    var i = 0
    while (i < n) {
      values.update(i, get(i))
      i += 1
    }
    values.toSeq
  }

  /** Displays all elements of this sequence in a string (without a separator). */
  def mkString: String = toSeq.mkString

  /** Displays all elements of this sequence in a string using a separator string. */
  def mkString(sep: String): String = toSeq.mkString(sep)

  /**
    * Displays all elements of this traversable or iterator in a string using
    * start, end, and separator strings.
    */
  def mkString(start: String, sep: String, end: String): String = toSeq.mkString(start, sep, end)

  /**
    * Returns the value at position i.
    *
    * @throws UnsupportedOperationException when schema is not defined.
    * @throws ClassCastException when data type does not match.
    * @throws NullPointerException when value is null.
    */
  private def getAnyValAs[T <: AnyVal](i: Int): T =
    if (isNullAt(i)) throw new NullPointerException(s"Value at index $i is null")
    else getAs[T](i)
}
