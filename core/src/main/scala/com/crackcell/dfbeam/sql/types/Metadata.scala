/*
 * Copyright 2014-2017 Shanyishanmei, Inc. All Rights Reserved.
 */

package com.crackcell.dfbeam.sql.types

/**
  * Created by Menglong TAN on 3/19/17.
  */
sealed class Metadata(val map: Map[String, Any]) extends Serializable {

  /** Tests whether this Metadata contains a binding for a key. */
  def contains(key: String): Boolean = map.contains(key)

  /** Gets a Long. */
  def getLong(key: String): Long = get(key)

  /** Gets a Double. */
  def getDouble(key: String): Double = get(key)

  /** Gets a Boolean. */
  def getBoolean(key: String): Boolean = get(key)

  /** Gets a String. */
  def getString(key: String): String = get(key)

  /** Gets a Metadata. */
  def getMetadata(key: String): Metadata = get(key)

  /** Gets a Long array. */
  def getLongArray(key: String): Array[Long] = get(key)

  /** Gets a Double array. */
  def getDoubleArray(key: String): Array[Double] = get(key)

  /** Gets a Boolean array. */
  def getBooleanArray(key: String): Array[Boolean] = get(key)

  /** Gets a String array. */
  def getStringArray(key: String): Array[String] = get(key)

  /** Gets a Metadata array. */
  def getMetadataArray(key: String): Array[Metadata] = get(key)

  override def equals(obj: Any): Boolean = {
    obj match {
      case that: Metadata if map.size == that.map.size =>
        map.keysIterator.forall { key =>
          that.map.get(key) match {
            case Some(otherValue) =>
              val ourValue = map.get(key).get
              (ourValue, otherValue) match {
                case (v0: Array[Long], v1: Array[Long]) => java.util.Arrays.equals(v0, v1)
                case (v0: Array[Double], v1: Array[Double]) => java.util.Arrays.equals(v0, v1)
                case (v0: Array[Boolean], v1: Array[Boolean]) => java.util.Arrays.equals(v0, v1)
                case (v0: Array[AnyRef], v1: Array[AnyRef]) => java.util.Arrays.equals(v0, v1)
                case (v0, v1) => v0 == v1
              }
            case None => false
          }
        }
      case other =>
        false
    }
  }

  private def get[T](key: String): T = {
    map(key).asInstanceOf[T]
  }

}

object Metadata {

  private[this] val _empty = new Metadata(Map.empty)

  def empty: Metadata = _empty
}
