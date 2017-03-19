/*
 * Copyright 2014-2017 Shanyishanmei, Inc. All Rights Reserved.
 */

package com.crackcell.dfbeam.sql.types

/**
  * Created by Menglong TAN on 3/19/17.
  */
case class StructField(name: String, dataType: DataType,
                       nullable: Boolean = true, metadata: Metadata = Metadata.empty)
