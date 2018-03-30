package com.aizong.ishtirak.common.misc.component;

import java.awt.Color;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import com.jidesoft.converter.CacheMap;
import com.jidesoft.converter.RegistrationListener;
import com.jidesoft.grid.AbstractJideCellEditor;
import com.jidesoft.grid.BooleanCheckBoxCellRenderer;
import com.jidesoft.grid.ColorCellRenderer;
import com.jidesoft.grid.ContextSensitiveCellRenderer;
import com.jidesoft.grid.EditorContext;
import com.jidesoft.grid.FontNameCellEditor;
import com.jidesoft.grid.IconRenderer;
import com.jidesoft.grid.MultilineTableCellRenderer;
import com.jidesoft.grid.NumberCellRenderer;
import com.jidesoft.grid.PasswordCellRenderer;

public class CellRendererManager
{
  private static CacheMap<Object, EditorContext> a = new CacheMap(EditorContext.DEFAULT_CONTEXT);
  private static TableCellRenderer b = new DefaultTableCellRenderer();
  private static boolean c = false;
  private static boolean d = false;
  private static boolean e = true;
  
  public static void registerRenderer(Class<?> paramClass, TableCellRenderer paramTableCellRenderer)
  {
    registerRenderer(paramClass, paramTableCellRenderer, EditorContext.DEFAULT_CONTEXT);
  }
  
  public static void registerRenderer(Class<?> paramClass, TableCellRenderer paramTableCellRenderer, EditorContext paramEditorContext)
  {
    int i = AbstractJideCellEditor.d;
    if (paramClass == null) {
      throw new IllegalArgumentException("Parameter clazz cannot be null");
    }
    if ((i != 0) || (paramEditorContext == null)) {
      paramEditorContext = EditorContext.DEFAULT_CONTEXT;
    }
    if ((i != 0) || ((isAutoInit()) && (!d))) {
      initDefaultRenderer();
    }
    a.register(paramClass, paramTableCellRenderer, paramEditorContext);
  }
  
  public static void unregisterRenderer(Class<?> paramClass, EditorContext paramEditorContext)
  {
    int i = AbstractJideCellEditor.d;
    if ((i != 0) || (paramEditorContext == null)) {
      paramEditorContext = EditorContext.DEFAULT_CONTEXT;
    }
    if ((i != 0) || ((isAutoInit()) && (!d))) {
      initDefaultRenderer();
    }
    a.unregister(paramClass, paramEditorContext);
  }
  
  public static void unregisterRenderer(Class<?> paramClass)
  {
    unregisterRenderer(paramClass, EditorContext.DEFAULT_CONTEXT);
  }
  
  public static void unregisterAllRenderers()
  {
    a.clear();
  }
  
  public static TableCellRenderer getRenderer(Class<?> paramClass, EditorContext paramEditorContext)
  {
    int i = AbstractJideCellEditor.d;
    if (isAutoInit()) {
      initDefaultRenderer();
    }
    if (i == 0) {
      if (paramEditorContext == null) {
        paramEditorContext = EditorContext.DEFAULT_CONTEXT;
      }
    }
    Object localObject = a.getRegisteredObject(paramClass, paramEditorContext);
    if ((i != 0) || (localObject != null))
    {
      if (i != 0) {
       return null;
      }
      if ((localObject instanceof TableCellRenderer)) {
        return (TableCellRenderer)localObject;
      }
    }
    if (paramEditorContext.equals(EditorContext.DEFAULT_CONTEXT)) {
      label65:
      return null;
    }
    return getRenderer(paramClass, EditorContext.DEFAULT_CONTEXT);
  }
  
  public static TableCellRenderer getRenderer(Class<?> paramClass)
  {
    return getRenderer(paramClass, EditorContext.DEFAULT_CONTEXT);
  }
  
  public static void updateUI()
  {
    a(b);
    List localList = a.getValues();
    Iterator localIterator = localList.iterator();
    do
    {
      if (!localIterator.hasNext()) {
        break;
      }
      Object localObject = localIterator.next();
      a(localObject);
    } while (AbstractJideCellEditor.d == 0);
  }
  
  private static void a(Object paramObject)
  {
    int i = AbstractJideCellEditor.d;
    if (paramObject == null) {
      return;
    }
    Component localComponent = null;
    if ((i != 0) || ((paramObject instanceof Component))) {
      localComponent = (Component)paramObject;
    }
    if ((i != 0) || ((paramObject instanceof DefaultCellEditor))) {
      localComponent = ((DefaultCellEditor)paramObject).getComponent();
    }
    if (((i != 0) || (localComponent != null)) && ((i != 0) || ((localComponent instanceof JComponent)))) {
      ((JComponent)localComponent).updateUI();
    }
  }
  
  public static boolean isAutoInit()
  {
    return e;
  }
  
  public static void setAutoInit(boolean paramBoolean)
  {
    e = paramBoolean;
  }
  
  public static void addRegistrationListener(RegistrationListener paramRegistrationListener)
  {
    a.addRegistrationListener(paramRegistrationListener);
  }
  
  public static void removeRegistrationListener(RegistrationListener paramRegistrationListener)
  {
    a.removeRegistrationListener(paramRegistrationListener);
  }
  
  public static RegistrationListener[] getRegistrationListeners()
  {
    return new RegistrationListener[] {};
  }
  
  public static EditorContext[] getEditorContexts(Class<?> paramClass)
  {
    return (EditorContext[])a.getKeys(paramClass, new EditorContext[0]);
  }
  
  public static void initDefaultRenderer()
  {
    if (AbstractJideCellEditor.d == 0) {
      if (c) {
        return;
      }
    }
    d = true;
    try
    {
      UIManager.addPropertyChangeListener(new PropertyChangeListener()
      {
        public void propertyChange(PropertyChangeEvent paramAnonymousPropertyChangeEvent)
        {
          if ("lookAndFeel".equals(paramAnonymousPropertyChangeEvent.getPropertyName())) {
            CellRendererManager.updateUI();
          }
        }
      });
      ContextSensitiveCellRenderer localContextSensitiveCellRenderer = new ContextSensitiveCellRenderer();
      registerRenderer(Object.class, localContextSensitiveCellRenderer);
      registerRenderer(String.class, localContextSensitiveCellRenderer);
      registerRenderer(String.class, localContextSensitiveCellRenderer, FontNameCellEditor.CONTEXT);
      registerRenderer(String.class, new MultilineTableCellRenderer(), MultilineTableCellRenderer.CONTEXT);
      NumberCellRenderer localNumberCellRenderer = new NumberCellRenderer();
      registerRenderer(Number.class, localNumberCellRenderer);
      registerRenderer(Integer.TYPE, localNumberCellRenderer);
      registerRenderer(Double.TYPE, localNumberCellRenderer);
      registerRenderer(Float.TYPE, localNumberCellRenderer);
      registerRenderer(Short.TYPE, localNumberCellRenderer);
      registerRenderer(Long.TYPE, localNumberCellRenderer);
      registerRenderer(Color.class, new ColorCellRenderer());
      BooleanCheckBoxCellRenderer localBooleanCheckBoxCellRenderer = new BooleanCheckBoxCellRenderer();
      registerRenderer(Boolean.class, localBooleanCheckBoxCellRenderer, BooleanCheckBoxCellRenderer.CONTEXT);
      registerRenderer(Boolean.TYPE, localBooleanCheckBoxCellRenderer, BooleanCheckBoxCellRenderer.CONTEXT);
      IconRenderer localIconRenderer = new IconRenderer();
      registerRenderer(Icon.class, localIconRenderer);
      registerRenderer(char[].class, new PasswordCellRenderer(), PasswordCellRenderer.CONTEXT);
    }
    finally
    {
      d = true;
      c = true;
    }
  }
  
  public static void resetInit()
  {
    c = false;
  }
}
