/*
 * Copyright 2014-2017 Shanyishanmei, Inc. All Rights Reserved.
 */

package com.crackcell.dfbeam.sql.types

/**
  * Created by Menglong TAN on 3/19/17.
  */
case class StructType(fields: Array[StructField]) extends DataType with Seq[StructField] {
  /** No-arg constructor for kryo. */
  def this() = this(Array.empty[StructField])

  /** Returns all field names in an array. */
  def fieldNames: Array[String] = fields.map(_.name)

  private lazy val fieldNamesSet: Set[String] = fieldNames.toSet
  private lazy val nameToField: Map[String, StructField] = fields.map(f => f.name -> f).toMap
  private lazy val nameToIndex: Map[String, Int] = fieldNames.zipWithIndex.toMap

  override def equals(that: Any): Boolean = {
    that match {
      case StructType(otherFields) =>
        java.util.Arrays.equals(
          fields.asInstanceOf[Array[AnyRef]], otherFields.asInstanceOf[Array[AnyRef]])
      case _ => false
    }
  }

  /**
    * Creates a new [[StructType]] by adding a new field.
    * {{{
    * val struct = (new StructType)
    *   .add(StructField("a", IntegerType, true))
    *   .add(StructField("b", LongType, false))
    *   .add(StructField("c", StringType, true))
    *}}}
    */
  def add(field: StructField): StructType = {
    StructType(fields :+ field)
  }

  /**
    * Creates a new [[StructType]] by adding a new nullable field with no metadata.
    *
    * val struct = (new StructType)
    *   .add("a", IntegerType)
    *   .add("b", LongType)
    *   .add("c", StringType)
    */
  def add(name: String, dataType: DataType): StructType = {
    StructType(fields :+ new StructField(name, dataType, nullable = true, Metadata.empty))
  }

  /**
    * Creates a new [[StructType]] by adding a new field with no metadata.
    *
    * val struct = (new StructType)
    *   .add("a", IntegerType, true)
    *   .add("b", LongType, false)
    *   .add("c", StringType, true)
    */
  def add(name: String, dataType: DataType, nullable: Boolean): StructType = {
    StructType(fields :+ new StructField(name, dataType, nullable, Metadata.empty))
  }

  /**
    * Creates a new [[StructType]] by adding a new field and specifying metadata.
    * {{{
    * val struct = (new StructType)
    *   .add("a", IntegerType, true, Metadata.empty)
    *   .add("b", LongType, false, Metadata.empty)
    *   .add("c", StringType, true, Metadata.empty)
    * }}}
    */
  def add(name: String, dataType: DataType, nullable: Boolean, metadata: Metadata): StructType = {
    StructType(fields :+ new StructField(name, dataType, nullable, metadata))
  }

  /**
    * Returns the index of a given field.
    *
    * @throws IllegalArgumentException if a field with the given name does not exist
    */
  def fieldIndex(name: String): Int = {
    nameToIndex.getOrElse(name,
      throw new IllegalArgumentException(s"""Field "$name" does not exist."""))
  }

  private[sql] def getFieldIndex(name: String): Option[Int] = {
    nameToIndex.get(name)
  }

  override def apply(fieldIndex: Int): StructField = fields(fieldIndex)

  override def length: Int = fields.length

  override def iterator: Iterator[StructField] = fields.iterator

  /**
    * The default size of a value of the StructType is the total default sizes of all field types.
    */
  override def defaultSize: Int = fields.map(_.dataType.defaultSize).sum

}
