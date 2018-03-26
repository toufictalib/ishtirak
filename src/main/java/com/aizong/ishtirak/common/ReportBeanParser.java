/*
* To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aizong.ishtirak.common;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author User
 */
public class ReportBeanParser implements BeanParsing{

    private List<Column> columns;

    private List<List<Object>> rows;
    
    private List data;
    private Class clazz;

    public ReportBeanParser() {
        
    }

    public static <T> void fillValue(T instance, List<Column> columns, List<Object> objects) throws IllegalArgumentException, IllegalAccessException {

        /**
         * map contains field name as key and object as value
         */
        Map<String/*field name*/, Object> map = new HashMap<String, Object>();
        for (int i = 0; i < columns.size(); i++) {
            map.put(columns.get(i).getFieldName(), objects.get(i));
        }

        for (Field field : instance.getClass().getDeclaredFields()) {
            Object o = map.get(field.getName());
            if (o != null) {
                field.setAccessible(true);
                field.set(instance, o);
            }
        }

    }

    public void parseColumn() {
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            TableColumnAnnotation annotation = field.getAnnotation(TableColumnAnnotation.class);
            if (annotation != null) {

                Column column = new Column();
                column.setType(field.getType());
                column.setName(annotation.name());
                column.setFieldName(field.getName());

                columns.add(column);

                field.setAccessible(true);


            }
        }

    }

    
    public static List<Object> parse(Object o) {
        Field[] fields = o.getClass().getDeclaredFields();

        List<Object> row = new ArrayList<Object>();

        for (Field field : fields) {
            TableColumnAnnotation annotation = field.getAnnotation(TableColumnAnnotation.class);
            if (annotation != null) {
                try {

                    field.setAccessible(true);
                    Object get = field.get(o);
                    row.add(get);

                } catch (IllegalArgumentException ex) {
                    ex.printStackTrace();
                    row.add(null);
                } catch (IllegalAccessException ex) {
                    ex.printStackTrace();
                    row.add(null);
                }

            }
        }

        return row;

    }
   

    public static void main(String[] args) {
//        TableBeanParser bean = new TableBeanParser(CheckupResponse.list());
//
//        System.out.println(bean.columns.toString());
//        System.out.println(bean.rows.toString());
    }

    @Override
    public List<Column> getColumns() {
        return columns;
    }

    @Override
    public List<List<Object>> getRows() {
        return rows;
    }

    @Override
    public void prepare(ModelHolder holder) {
        this.data = holder.getData();
        this.clazz = holder.getClazz();
        
    }

    @Override
    public void init() {
       this.columns = new ArrayList<Column>();
        this.rows = new ArrayList<List<Object>>();

        parseColumn();
        for (Object o : data) {
           rows.add(parse(o));
        }
    }

}
