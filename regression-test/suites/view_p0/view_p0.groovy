// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

suite("view_p0") {
    sql """DROP VIEW IF EXISTS test_view"""
    sql """ 
        create view test_view as select 1,to_base64(AES_ENCRYPT('doris','doris')); 
    """
    qt_sql "select * from test_view;"
    
    sql """DROP TABLE IF EXISTS test_view_table"""
    
    sql """ 
        create table test_view_table (id int) distributed by hash(id) properties('replication_num'='1');
    """
    
    sql """insert into test_view_table values(1);"""
    
    sql """DROP VIEW IF EXISTS test_varchar_view"""
    
    sql """ 
        create view test_varchar_view (id) as  SELECT GROUP_CONCAT(cast( id as varchar)) from test_view_table; 
    """
    
    qt_sql "select * from test_varchar_view;"
    qt_sql "select cast( id as varchar(*)) from test_view_table;"
}
