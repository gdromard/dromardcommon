/*
 * Created on 12 oct. 2005 By Gabriel DROMARD
 */
package net.dromard.common.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * Utility class for reflect purpose.
 *
 * <br>
 * <pre>
 *   +---------+
 *   | History |
 *   +---------+
 *
 * [03/10/2006] by Anthony Bucciero
 *    - add method that provide accessor method facilities.
 *
 * [02/10/2006] by Gabriel Dromard
 *    - Renamed from ClassUtil to ReflectUtil.
 *    - Add comments.
 *
 * [12/10/2005] by Gabriel Dromard
 *    - Created.
 * </pre>
 *
 * <br>
 * @author Gabriel Dromard
 * @version 1.0
 */
@SuppressWarnings("unchecked")
public final class ReflectHelper {
    /**
     * Empty private constructor for util class.
     */
    private ReflectHelper() {
        // Empty private constructor for util class.
    }

    /** Constant meaning accessor type setter. */
    public static final String SETTER = "set";

    /** Constant meaning accessor type getter. */
    public static final String GETTER = "get";

    /** Constant meaning accessor type getter for boolean. */
    public static final String BOOLEAN_GETTER = "is";

    /** Cache getters. Method object hashed by fieldName hashed by class object. */
    public static final Map GETTER_CACHE = new SoftHashtable();

    /** Cache methods. Method object hashed by fieldName hashed by class object. */
    public static final Map<Class<?>, Map> METHODS_CACHE = new SoftHashtable();

    /**
     * Instantiate a Class using its name.
     * @param clazz The class to be instantiated
     * @param arguments The arguments to be passed to constructor.
     * @param types The types of arguments.
     * @return The instance
     * @throws ClassNotFoundException Thrown if you try to instantiate a class that does not exist.
     * @throws NoSuchMethodException Thrown if you try to retrieve a method that does not exist.
     * @throws InvocationTargetException Can occur during invocation
     * @throws IllegalAccessException Thrown when an illegal access of the instance is done
     * @throws InstantiationException Any other case
     */
    public static Object newInstance(final Class<?> clazz, final Object[] arguments, final Class[] types) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        // Retrieving corresponding constructor
        Constructor<?> constructor = clazz.getConstructor(types);

        // Create an instance
        return constructor.newInstance(arguments);
    }

    /**
     * Instantiate a Class using its name.
     * @param clazz The class to be instantiated
     * @param arguments The parameters to be passed to constructor.
     * @return The instance
     * @throws ClassNotFoundException Thrown if you try to instantiate a class that does not exist.
     * @throws NoSuchMethodException Thrown if you try to retrieve a method that does not exist.
     * @throws InvocationTargetException Can occur during invocation
     * @throws IllegalAccessException Thrown when an illegal access of the instance is done
     * @throws InstantiationException Any other case
     */
    public static Object newInstance(final Class<?> clazz, final Object[] arguments) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        // Construct the types array
        Class<?>[] types = new Class[arguments.length];
        for (int i = 0; i < arguments.length; ++i) {
            types[i] = arguments[i].getClass();
        }
        // Instantiate class
        return ReflectHelper.newInstance(clazz, arguments, types);
    }

    /**
     * Instantiate a Class using its name.
     * @param className The class name
     * @return The instance
     * @throws ClassNotFoundException Thrown if you try to instantiate a class that does not exist.
     * @throws InstantiationException Any other case
     * @throws NoSuchMethodException Thrown if you try to retrieve a method that does not exist.
     * @throws InvocationTargetException Can occur during invocation
     * @throws IllegalAccessException Thrown when an illegal access of the instance is done
     */
    public static Object newInstance(final String className) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        // Retrieve class to be load
        Class<?> clazz = Class.forName(className);
        return ReflectHelper.newInstance(clazz, new Object[]{});
    }

    /**
     * Instantiate a Class using its name.
     * @param clazz The class to be instantiated
     * @return The instance
     * @throws ClassNotFoundException Thrown if you try to instantiate a class that does not exist.
     * @throws InstantiationException Any other case
     * @throws NoSuchMethodException Thrown if you try to retrieve a method that does not exist.
     * @throws InvocationTargetException Can occur during invocation
     * @throws IllegalAccessException Thrown when an illegal access of the instance is done
     */
    public static Object newInstance(final Class clazz) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        return ReflectHelper.newInstance(clazz, new Object[]{});
    }

    /**
     * Invoke a methods (<code>methodName</code>) from an <code>object</code> with <code>arguments</code> of <code>types</code>.
     * @param object     The object from which you want to invoke a methods.
     * @param methodName The method name to invoke.
     * @param arguments  The arguments to pass to the methods.
     * @param types      The types of arguments.
     * @return the result of the invoked method.
     * @throws NoSuchMethodException Thrown if you try to retrieve a method that does not exist.
     * @throws InvocationTargetException Can occur during invocation
     * @throws IllegalAccessException Thrown when an illegal access of the instance is done
     */
    public static Object invokeMethod(final Object object, final String methodName, final Object[] arguments, final Class[] types) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        try {
            Class beanClass = object.getClass();
            Map methods = ReflectHelper.getCachedMethods(beanClass);
            Method getter = (Method) methods.get(methodName);
            if (getter == null) {
                getter = ReflectHelper.getMethod(beanClass, methodName, types);
                methods.put(methodName, getter);
            }
            return getter.invoke(object, arguments);
        } catch (NoSuchMethodException ex) {
            NoSuchMethodException thrown = new NoSuchMethodException(ReflectHelper.formatInvokeException(ex, object, methodName, arguments));
            thrown.setStackTrace(ex.getStackTrace());
            throw thrown;
        } catch (InvocationTargetException ex) {
            throw new InvocationTargetException(ex.getTargetException(), ReflectHelper.formatInvokeException(ex.getTargetException(), object, methodName, arguments));
        } catch (IllegalAccessException ex) {
            IllegalAccessException thrown = new IllegalAccessException(ReflectHelper.formatInvokeException(ex, object, methodName, arguments));
            thrown.setStackTrace(ex.getStackTrace());
            throw thrown;
        }
    }

    /**
     * Invoke a methods (<code>methodName</code>) from an <code>object</code> with <code>arguments</code> of <code>types</code>.
     * @param object     The object from which you want to invoke a methods.
     * @param method     The method to invoke.
     * @param arguments  The arguments to pass to the methods.
     * @return the result of the invoked method.
     * @throws InvocationTargetException Can occure during invocation
     * @throws IllegalAccessException Thrown while trying to access illagaly the instance
     */
    public static Object invokeMethod(final Object object, final Method method, final Object[] arguments) throws IllegalAccessException, InvocationTargetException {
        return method.invoke(object, arguments);
    }

    /**
     * Invoke a methods (<code>methodName</code>) from an <code>object</code> with <code>arguments</code> of <code>types</code>.
     * @param clazz      The <code>clazz</code> on which to retrieve the method.
     * @param methodName The method name to invoke.
     * @param arguments  The arguments to pass to the methods.
     * @param types      The types of arguments.
     * @return the result of the invoked method.
     * @throws NoSuchMethodException Thrown if you try to retrieve a method that does not exist.
     * @throws InvocationTargetException Can occur during invocation
     * @throws IllegalAccessException Thrown when an illegal access of the instance is done
     */
    public static Object invokeStaticMethod(final Class clazz, final String methodName, final Object[] arguments, final Class[] types) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Map methods = ReflectHelper.getCachedMethods(clazz);
        Method getter = (Method) methods.get(methodName);
        if (getter == null) {
            getter = ReflectHelper.getMethod(clazz, methodName, types);
            methods.put(methodName, getter);
        }
        return getter.invoke(null, arguments);
    }

    /**
     * Invoke a methods (<code>methodName</code>) from an <code>object</code> with <code>arguments</code>.
     * @param object The object from which you want to invoke a methods.
     * @param methodName The method name to invoke.
     * @param arguments The arguments to pass to the methods.
     * @return the result of the invoked method.
     * @throws NoSuchMethodException Thrown if you try to retrieve a method that does not exist.
     * @throws InvocationTargetException Can occur during invocation
     * @throws IllegalAccessException Thrown when an illegal access of the instance is done
     */
    public static Object invokeMethod(final Object object, final String methodName, final Object[] arguments) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        // Construct the types array
        Class[] types = new Class[arguments.length];
        for (int i = 0; i < arguments.length; ++i) {
            types[i] = arguments[i].getClass();
        }
        return ReflectHelper.invokeMethod(object, methodName, arguments, types);
    }

    /**
     * This method invoke the setter according to the <code>fieldName</code> on the <code>toFill</code> object with as argument.
     * <code>valueToSet</code> of type <code>valueToSetType</code>
     * @param toFill the object to fill.
     * @param fieldName the field name.
     * @param valueToSet the value to set.
     * @param valueToSetType the type of the value to set.
     * @throws NoSuchMethodException Thrown if you try to retrieve a method that does not exist.
     * @throws InvocationTargetException Can occur during invocation
     * @throws IllegalAccessException Thrown when an illegal access of the instance is done
     */
    public static void invokeSetter(final Object toFill, final String fieldName, final Object valueToSet, final Class valueToSetType) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        //ReflectHelper.invokeMethod(toFill, ReflectHelper.computeAccessorMethodName(ReflectHelper.SETTER, fieldName), new Object[]{valueToSet}, new Class[]{valueToSetType});
        List<Method> objectSetters = ReflectHelper.getSettersRecursivly(toFill.getClass());
        String setterName = ReflectHelper.computeAccessorMethodName(ReflectHelper.SETTER, fieldName);
        for (Method setter : objectSetters) {
            if (setter.getName().equals(setterName) && setter.getParameterTypes().length == 1 && autoBoxingValidation(setter.getParameterTypes()[0], valueToSetType)) {
                ReflectHelper.invokeMethod(toFill, setterName, new Object[]{valueToSet}, setter.getParameterTypes());
                return;
            }
        }
        // Test if setter exists with parent class type
        for (Method setter : objectSetters) {
            if (setter.getName().equals(setterName) && setter.getParameterTypes().length == 1 && autoBoxingValidation(setter.getParameterTypes()[0], valueToSetType.getSuperclass())) {
                ReflectHelper.invokeMethod(toFill, setterName, new Object[]{valueToSet}, setter.getParameterTypes());
                return;
            }
        }
        throw new NoSuchMethodException(ReflectHelper.formatInvokeException(null, toFill, setterName, new Object[]{valueToSet}));
    }

    private static boolean autoBoxingValidation(final Class valid, final Class check) {
        if (valid.equals(check)) {
            return true;
        }
        if (check.equals(Boolean.class)) {
            return valid.equals(boolean.class);
        } else if (check.equals(Integer.class)) {
            return valid.equals(int.class);
        } else if (check.equals(Long.class)) {
            return valid.equals(long.class);
        } else if (check.equals(Double.class)) {
            return valid.equals(double.class);
        } else if (check.equals(Float.class)) {
            return valid.equals(float.class);
        } else if (check.equals(Character.class)) {
            return valid.equals(char.class);
        } else if (check.equals(Byte.class)) {
            return valid.equals(byte.class);
        }
        return false;
    }

    /**
     * This method invoke the getter according to the <code>fieldName</code> on the <code>toFill</code> object with as argument.
     * <code>valueToSet</code> of type <code>valueToSetType</code>
     * @param bean the JavaBean object.
     * @param fieldName the field name.
     * @return The value returned by the getter.
     * @throws NoSuchMethodException Thrown if you try to retrieve a method that does not exist.
     * @throws InvocationTargetException Can occur during invocation
     * @throws IllegalAccessException Thrown when an illegal access of the instance is done
     */
    public static Object invokeGetter(final Object bean, final String fieldName) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        List<Method> objectGetters = ReflectHelper.getGettersRecursivly(bean.getClass());
        String getterName = ReflectHelper.computeAccessorMethodName(ReflectHelper.GETTER, fieldName);
        String booleanGetterName = ReflectHelper.computeAccessorMethodName(ReflectHelper.BOOLEAN_GETTER, fieldName);
        for (Method getter : objectGetters) {
            if (getter.getName().equals(getterName) && getter.getParameterTypes().length == 0) {
                return ReflectHelper.invokeMethod(bean, getterName, new Object[]{});
            } else if (getter.getName().equals(booleanGetterName) && getter.getParameterTypes().length == 0) {
                return ReflectHelper.invokeMethod(bean, booleanGetterName, new Object[]{});
            }
        }
        throw new NoSuchMethodException(ReflectHelper.formatInvokeException(null, bean, fieldName, new Class[]{}));
    }

    /**
     * Retrieve the cached methods of a class.
     * @param clazz The class
     * @return the cached methods of a class.
     */
    private static Map getCachedGetters(final Class clazz) {
        Map getters = (Map) ReflectHelper.GETTER_CACHE.get(clazz);
        if (getters == null) {
            getters = new SoftHashtable();
            ReflectHelper.GETTER_CACHE.put(clazz, getters);
        }
        return getters;
    }

    /**
     * Retrieve the cached methods of a class.
     * @param clazz The class
     * @return the cached methods of a class.
     */
    private static Map getCachedMethods(final Class clazz) {
        Map methods = ReflectHelper.METHODS_CACHE.get(clazz);
        if (methods == null) {
            methods = new SoftHashtable();
            ReflectHelper.METHODS_CACHE.put(clazz, methods);
        }
        return methods;
    }

    /**
     * This method handle the return of getters using their corresponding fieldname, or the return of a method.
     * @param bean the JavaBean object.
     * @param reflect the field name.
     * @return The value returned by the getter or the method.
     * @throws NoSuchMethodException Thrown if you try to retrieve a method that does not exist.
     * @throws InvocationTargetException Can occur during invocation
     * @throws IllegalAccessException Thrown when an illegal access of the instance is done
     */
    public static Object invoke(final Object bean, final String reflect) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        String field = reflect;
        Object instance = bean;
        while (field.indexOf('[') > -1 || field.indexOf('.') > -1 || field.indexOf('(') > 0) {
            if (field.indexOf('[') > -1 && (field.indexOf('.') == -1 || field.indexOf('.') > field.indexOf('[')) && (field.indexOf('(') == -1 || field.indexOf('(') > field.indexOf('['))) {
                if (field.indexOf('[') > 0) {
                    // Invoke getter to retrieve the child element
                    instance = ReflectHelper.invokeGetter(instance, field.substring(0, field.indexOf('[')));
                    // If result is null no need to continue
                    if (instance == null) {
                        return null;
                    }
                }
                // Re route field name to retrieve the field of the child instance
                int index = Integer.parseInt(field.substring(field.indexOf('[') + 1, field.indexOf(']')));
                if (instance instanceof List) {
                    instance = ((List) instance).get(index);
                } else {
                    instance = Array.get(instance, index);
                }
                field = field.substring(field.indexOf(']') + 1);
            } else if (field.indexOf('.') > -1 && (field.indexOf('(') == -1 || field.indexOf('(') > field.indexOf('.'))) {
                // In the case that you want to retrieve in one time the field of a child object
                if (field.indexOf('.') > 0) {
                    // Invoke getter to retrieve the child element
                    instance = ReflectHelper.invokeGetter(instance, field.substring(0, field.indexOf('.')));
                    // If result is null no need to continue
                    if (instance == null) {
                        return null;
                    }
                }
                // Re route field name to retrieve the field of the child instance
                field = field.substring(field.indexOf('.') + 1);
            } else if (field.indexOf('(') > 0) {
                // Invoke getter to retrieve the child element
                instance = ReflectHelper.invokeMethod(instance, field.substring(0, field.indexOf('(')), new Object[]{});
                // If result is null no need to continue
                if (instance == null) {
                    return null;
                }
                // Re route field name to retrieve the field of the child instance
                field = field.substring(field.indexOf(')') + 1);
            }
        }
        if (field.length() == 0) {
            return instance;
        }
        if (field.indexOf('.') > -1 || field.indexOf('[') > -1) {
            return ReflectHelper.invoke(instance, field);
        }
        return ReflectHelper.invokeGetter(instance, field);
    }

    /**
     * Format a more detailed message when an exception occured during invocation.
     * @param ex        The exception
     * @param object    The object on which the given method is invoked
     * @param method    The invoked method
     * @param arguments The method arguments
     * @return The detailed message
     */
    private static String formatInvokeException(final Throwable ex, final Object object, final String method, final Object[] arguments) {
        String message;
        if (ex == null || ex.getMessage() == null) {
            message = "";
        } else {
            message = ex.getMessage() + " - ";
        }
        String args = "";
        for (int i = 0; i < arguments.length; ++i) {
            args += arguments[i].getClass().getName();
            // Concat each element
            args += "='" + arguments[i] + "'";
            // Add separator if necessary
            if ((i < arguments.length - 1)) {
                args += ", ";
            }
        }
        return message + "Error occured while tyring to invoke " + object.getClass().getName() + "." + method + "(" + args + ") method.";
    }

    /**
     * Get the field (<code>filedName</code>) value of an <code>object</code>.
     * @param object The <code>object</code> on which to retrieve the field value.
     * @param fieldName The <code>fieldName</code> to retrieve.
     * @return The value of the field.
     * @throws NoSuchFieldException Thrown if you try to retrieve a field that does not exist.
     * @throws InvocationTargetException Can occur during invocation
     * @throws IllegalAccessException Thrown when an illegal access of the instance is done
     */
    public static Object getFieldValue(final Object object, final String fieldName) throws NoSuchFieldException, IllegalAccessException, InvocationTargetException {
        try {
            return ReflectHelper.getField(object.getClass(), fieldName).get(object);
        } catch (NoSuchFieldException ex) {
            try {
                return ReflectHelper.invokeGetter(object, fieldName);
            } catch (NoSuchMethodException x) {
                throw ex;
            }
        }
    }

    /**
     * Set the field (<code>filedName</code>) value of an <code>object</code>.
     * @param object The <code>object</code> on which to set the field value.
     * @param fieldName The <code>fieldName</code> to set.
     * @param fieldValue The <code>fieldValue</code> to set.
     * @throws NoSuchFieldException Thrown if you try to retrieve a field that does not exist.
     * @throws IllegalAccessException Thrown when an illegal access of the instance is done
     */
    public static void setFieldValue(final Object object, final String fieldName, final Object fieldValue) throws NoSuchFieldException, IllegalAccessException {
        try {
            ReflectHelper.getField(object.getClass(), fieldName).set(object, fieldValue);
        } catch (NoSuchFieldException ex) {
            throw ex;
        }
    }

    /**
     * Get the field (<code>filedName</code>) of a class (<code>clazz</code>).
     * @param clazz The <code>clazz</code> on which to retrieve the field.
     * @param fieldName The <code>fieldName</code> to retrieve.
     * @return The Field object of the given class.
     * @throws NoSuchFieldException Thrown if you try to retrieve a field that does not exist.
     */
    public static Field getField(final Class<?> clazz, final String fieldName) throws NoSuchFieldException {
        try {
            Field f = clazz.getDeclaredField(fieldName);
            f.setAccessible(true);
            return f;
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            if (clazz == Object.class) {
                throw e;
            }
            // Get the one from parent super class recursivly
            return ReflectHelper.getField(clazz.getSuperclass(), fieldName);
        }
    }

    /**
     * Return all declared fields of a class (and all its super class).
     * @see {@link Class#getDeclaredFields()}
     * @param clazz The class on which to retrieve the fields.
     * @return All the fields.
     */
    public static List<Field> getDeclaredFields(final Class<?> clazz) {
        List<Field> fields = new ArrayList<Field>();
        Class<?> current = clazz;
        while (!current.getName().equals(Object.class.getName())) {
            fields.addAll(Arrays.asList(current.getDeclaredFields()));
            current = current.getSuperclass();
        }
        return fields;
    }

    /**
     * Return all getter of a class (but not the ones from parent).
     * @param clazz The class on which to retrieve the getters.
     * @return All the methods.
     */
    public static List<Method> getGetters(final Class<?> clazz) {
        List<Method> getters = new ArrayList<Method>();
        if (clazz != null) {
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                if (method.getDeclaringClass().equals(clazz)) {
                    if ((method.getName().startsWith(ReflectHelper.GETTER) || method.getName().startsWith(ReflectHelper.BOOLEAN_GETTER)) && method.getParameterTypes().length == 0) {
                        getters.add(method);
                    }
                }
            }
        }
        return getters;
    }

    /**
     * Return all getter of a class (even parent's ones).
     * @param clazz The class on which to retrieve the getters.
     * @return All the methods.
     */
    public static List<Method> getGettersRecursivly(final Class<?> clazz) {
        List<Method> getters = new ArrayList<Method>();
        if (clazz != null) {
            Class<?> current = clazz;
            while (!current.getName().equals(Object.class.getName())) {
                Method[] methods = current.getMethods();
                for (Method method : methods) {
                    if (method.getDeclaringClass().equals(current)) {
                        if ((method.getName().startsWith(ReflectHelper.GETTER) || method.getName().startsWith(ReflectHelper.BOOLEAN_GETTER)) && method.getParameterTypes().length == 0) {
                            getters.add(method);
                        }
                    }
                }
                current = current.getSuperclass();
            }
        }
        return getters;
    }

    /**
     * Return all getter of a class (but not the ones from parent).
     * @param clazz The class on which to retrieve the getters.
     * @return All the methods.
     */
    public static List<Method> getSetters(final Class<?> clazz) {
        List<Method> getters = new ArrayList<Method>();
        if (clazz != null) {
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                if (method.getDeclaringClass().equals(clazz)) {
                    if (method.getName().startsWith(ReflectHelper.SETTER) && method.getName().length() > ReflectHelper.SETTER.length() && method.getParameterTypes().length == 1) {
                        getters.add(method);
                    }
                }
            }
        }
        return getters;
    }

    /**
     * Return all getter of a class (even parent's ones).
     * @param clazz The class on which to retrieve the getters.
     * @return All the methods.
     */
    public static List<Method> getSettersRecursivly(final Class<?> clazz) {
        List<Method> getters = new ArrayList<Method>();
        if (clazz != null) {
            Class<?> current = clazz;
            while (!current.getName().equals(Object.class.getName())) {
                Method[] methods = current.getMethods();
                for (Method method : methods) {
                    if (method.getDeclaringClass().equals(current)) {
                        if (method.getName().startsWith(ReflectHelper.SETTER) && method.getName().length() > ReflectHelper.SETTER.length() && method.getParameterTypes().length == 1) {
                            getters.add(method);
                        }
                    }
                }
                current = current.getSuperclass();
            }
        }
        return getters;
    }

    /**
     * retrieve the method (<code>methodName</code>) of a class (<code>clazz</code>) using parameters <code>types</code>.
     * @param clazz The <code>clazz</code> on which to retrieve the method.
     * @param methodName The method name to retrieve.
     * @param types The types of the method's arguments.
     * @return A Method object.
     * @throws NoSuchMethodException Thrown if you try to retrieve a method that does not exist.
     */
    public static Method getMethod(final Class<?> clazz, final String methodName, final Class<?>[] types) throws NoSuchMethodException {
        try {
            Method m = clazz.getDeclaredMethod(methodName, types);
            m.setAccessible(true);
            return m;
        } catch (NoSuchMethodException x) {
            try {
                if (clazz == Object.class) {
                    throw x;
                }
                return ReflectHelper.getMethod(clazz.getSuperclass(), methodName, types);
            } catch (Exception ex) {
                throw x;
            }
        }
    }

    /**
     * This method compute the accessor method name according to the <code>accessorType</code> and <code>fieldName</code>.
     * @param accessorType the accessor type.
     * @param fieldName the field name.
     * @return the accessor method name.
     * @throw IllegalArgumentException If the arguments type is not valid.
     */
    private static String computeAccessorMethodName(final String accessorType, final String fieldName) {
        if (!ReflectHelper.GETTER.equals(accessorType) && !ReflectHelper.SETTER.equals(accessorType) && !ReflectHelper.BOOLEAN_GETTER.equals(accessorType)) {
            throw new IllegalArgumentException("Accessor Type : " + accessorType + " isn't correct");
        }
        if (StringHelper.isEmpty(fieldName)) {
            throw new IllegalArgumentException("fieldName migth not be null or empty");
        }
        return accessorType + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    /**
     * If you want to map a getter/setter with its field name.
     * Use this method to convert getter or setter into fieldname
     * @param method The method to be converted
     * @return The field name or method name if the method is neither a getter neither a setter
     */
    public static String getFieldName(final Method method) {
        String prefix = null;
        if (method.getName().startsWith(ReflectHelper.GETTER)) {
            prefix = ReflectHelper.GETTER;
        } else if (method.getName().startsWith(ReflectHelper.SETTER)) {
            prefix = ReflectHelper.SETTER;
        } else if (method.getName().startsWith(ReflectHelper.BOOLEAN_GETTER)) {
            prefix = ReflectHelper.BOOLEAN_GETTER;
        } else {
            return method.getName();
        }
        if (method.getName().length() == prefix.length()) {
            return method.getName();
        }

        return ("" + method.getName().charAt(prefix.length())).toLowerCase() + method.getName().substring(prefix.length() + 1);
    }

    public static boolean implement(final Class<?> clazz, final Class<?> father) {
        return Arrays.asList(clazz.getInterfaces()).contains(father);
    }

    /**
     * Retrieve the class list of a package.
     * @param packageName The package name
     * @return The list of classes.
     * @throws ClassNotFoundException No comment !!
     * @throws IOException
     */
    public static Set<Class<?>> getClasses(final String packageName) throws IOException, ClassNotFoundException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        return ReflectHelper.getClasses(loader, packageName);
    }

    private static Set<Class<?>> getClasses(final ClassLoader loader, final String packageName) throws IOException, ClassNotFoundException {
        Set<Class<?>> classes = new HashSet<Class<?>>();
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = loader.getResources(path);
        if (resources != null) {
            while (resources.hasMoreElements()) {
                String filePath = null;
                try {
                    filePath = resources.nextElement().toURI().getPath();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                if (filePath != null) {
                    if ((filePath.indexOf("!") > 0) & (filePath.indexOf(".jar") > 0)) {
                        String jarPath = filePath.substring(0, filePath.indexOf("!"));
                        classes.addAll(ReflectHelper.getFromJARFile(jarPath, path));
                    } else {
                        classes.addAll(ReflectHelper.getFromDirectory(new File(filePath), packageName));
                    }
                }
            }
        }
        return classes;
    }

    private static Set<Class<?>> getFromDirectory(final File directory, final String packageName) throws ClassNotFoundException {
        Set<Class<?>> classes = new HashSet<Class<?>>();
        if (directory.exists()) {
            for (String file : directory.list()) {
                if (file.endsWith(".class")) {
                    String name = packageName + '.' + ReflectHelper.stripFilenameExtension(file);
                    Class<?> clazz = Class.forName(name);
                    classes.add(clazz);
                }
            }
        }
        return classes;
    }

    private static String stripFilenameExtension(final String file) {
        return file.substring(0, file.lastIndexOf('.'));
    }

    private static Set<Class<?>> getFromJARFile(final String jarUrl, final String packageName) throws FileNotFoundException, IOException, ClassNotFoundException {
        Set<Class<?>> classes = new HashSet<Class<?>>();
        JarInputStream jarFile = new JarInputStream(new URL(jarUrl).openStream());
        JarEntry jarEntry;
        do {
            jarEntry = jarFile.getNextJarEntry();
            if (jarEntry != null) {
                String className = jarEntry.getName();
                if (className.endsWith(".class")) {
                    className = ReflectHelper.stripFilenameExtension(className);
                    if (className.startsWith(packageName)) {
                        classes.add(Class.forName(className.replace('/', '.')));
                    }
                }
            }
        } while (jarEntry != null);
        return classes;
    }
}
