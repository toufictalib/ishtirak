/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aizong.ishtirak.common;

/**
 *
 * @author User
 */
public class Column {

    public enum Complexity
    {
        IGNORE,NORMAL,COMPLEX,MORE_COMPLEX
    }
    
    private boolean id;
    private String name;
    private Class type = Object.class;
    private boolean editable;
    private boolean hidden;
    private boolean required;
    private Complexity complexity = Complexity.NORMAL;    
    private String fieldName;

    /**
     * not editable and id field should be hide in state of creating new
     *
     * @return
     */
    public boolean ignoreField() {
        return id || !editable || hidden;
    }

    public Column() {
    }

    public Column(String name)
    {
        this.name= name;
    }
    public Column(boolean id, String name, Class clazz, boolean editable, boolean hidden) {
        this.id = id;
        this.name = name;
        this.type = clazz;
        this.editable = editable;
        this.hidden = hidden;
    }

    public boolean isId() {
        return id;
    }

    public void setId(boolean id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class getType() {
        return convertPrimitiveToWrapper(type);
    }
    
	public static Class<?> convertPrimitiveToWrapper(Class<?> clazz)
	{
		if (clazz == long.class) return Long.class;

		if (clazz == int.class) return Integer.class;

		if (clazz == boolean.class) return Boolean.class;

		return clazz;
	}
    

    public void setType(Class type) {
        this.type = type;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    
    @Override
    public String toString() {
        return "Column{" + "id=" + id + ", name=" + name + ", clazz=" + type + ", editable=" + editable + ", hidden=" + hidden + '}';
    }

    public boolean isComplex() {
        return complexity==Complexity.COMPLEX;
    }

    public Complexity getComplexity() {
        return complexity;
    }

    public void setComplexity(Complexity complexity) {
        this.complexity = complexity;
    }
    
    
    
}
