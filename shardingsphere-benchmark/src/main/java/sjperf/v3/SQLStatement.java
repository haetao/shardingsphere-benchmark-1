package sjperf.v3;

public enum SQLStatement {
    //DataBase Routing SQL
    DELETE_SQL_DB_ROUTING("delete from t_order where user_id=5")
    ,INSERT_SQL_DB_ROUTING("insert into t_order(user_id) values(7)")
    ,SELECT_SQL_DB_ROUTING("select * from t_order where user_id=5")
    ,UPDATE_SQL_DB_ROUTING("update t_order set name='jdbc',sex='sun' where user_id=5")

    //Full Routing SQL
    ,DELETE_SQL_FULL_ROUTING("delete from t_order")
    ,INSERT_SQL_FULL_ROUTING("insert into t_order(name,sex) values('beijing','tongzhou')")
    ,SELECT_SQL_FULL_ROUTING("select * from t_order")
    ,UPDATE_SQL_FULL_ROUTING("update t_order set name='hunter',sex='male'")

    //Single Routing SQL
    ,DELETE_SQL_SINGLE_ROUTING("delete from t_order where user_id=7 and order_id=11")
    ,INSERT_SQL_SINGLE_ROUTING("insert into t_order(user_id,order_id,name,sex) values(9,13,'Tom','male')")
    ,SELECT_SQL_SINGLE_ROUTING("select * from t_order where user_id=3 and order_id=5")
    ,UPDATE_SQL_SINGLE_ROUTING("update t_order set name='Crazy Man',sex='male' where user_id=9 and order_id=13")

    //Table Routing SQL
    ,DELETE_SQL_TABLE_ROUTING("delete from t_order where order_id=9")
    ,INSERT_SQL_TABLE_ROUTING("insert into t_order(order_id,name,sex) values(7,'Alex','male')")
    ,SELECT_SQL_TABLE_ROUTING("select * from t_order where order_id=5")
    ,UPDATE_SQL_TABLE_ROUTING("update t_order set name='Lily',sex='famale' where order_id=11")

    //Master Slave SQL
    ,DELETE_SQL_MASTER_SLAVE("delete from t_order where user_id = 7")
    ,INSERT_SQL_MASTER_SLAVE("insert into t_order(order_id,name,sex) values(7,'Alex','male')")
    ,SELECT_SQL_MASTER_SLAVE("select * from t_order")
    ,UPDATE_SQL_MASTER_SLAVE("update t_order set name='Curry',sex='male' where order_id=6");


    private String value;
    SQLStatement(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
