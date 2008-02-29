/*
 * Created on 12 oct. 2005 By Gabriel DROMARD
 */
package net.dromard.common.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
 *    - Renamed from net.dromard.common.util.system.ClassUtil to net.dromard.common.util.ReflectUtil.
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

    /**
     * Instantiate a Class using its name.
     * @param className The class name.
     * @param arguments The arguments to be passed to constructor.
     * @param types The types of arguments.
     * @return The instance
     * @throws ClassNotFoundException Thrown if you try to instanciate a class that does not exist.
     * @throws NoSuchMethodException Thrown if you try to retreive a method that does not exist.
     * @throws InvocationTargetException Can occure during invocation
     * @throws IllegalAccessException Thrown while trying to access illagaly the instance
     * @throws InstantiationException Any other case
     */
    public static Object newInstance(final String className, final Object[] arguments, final Class[] types) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        // Retrieve class to be load
        Class clazz = Class.forName(className);

        // Récupération du constructeur correspondant à la liste des parametres donnée
        Constructor constructor = clazz.getConstructor(types);

        // Création d'une instance avec le constructeur récupérés
        return constructor.newInstance(arguments);
    }

    /**
     * Instantiate a Class using its name.
     * @param className The class name.
     * @param arguments The parameters to be passed to constructor.
     * @return The instance
     * @throws ClassNotFoundException Thrown if you try to instanciate a class that does not exist.
     * @throws NoSuchMethodException Thrown if you try to retreive a method that does not exist.
     * @throws InvocationTargetException Can occure during invocation
     * @throws IllegalAccessException Thrown while trying to access illagaly the instance
     * @throws InstantiationException Any other case
     */
    public static Object newInstance(final String className, final Object[] arguments) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        // Construct the types array
        Class[] types = new Class[arguments.length];
        for (int i = 0; i <= arguments.length; ++i) {
            types[i] = arguments[i].getClass();
        }
        // Instantiate class
        return newInstance(className, arguments, types);
    }

    /**
     * Instantiate a Class using its name.
     * @param className The class name
     * @return The instance
     * @throws ClassNotFoundException Thrown if you try to instanciate a class that does not exist.
     * @throws InstantiationException Any other case
     * @throws NoSuchMethodException Thrown if you try to retreive a method that does not exist.
     * @throws InvocationTargetException Can occure during invocation
     * @throws IllegalAccessException Thrown while trying to access illagaly the instance
     */
    public static Object newInstance(final String className) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        return newInstance(className, new Object[] {});
    }

    /**
     * Invoke a methods (<code>methodName</code>) from an <code>object</code> with <code>arguments</code> of <code>types</code>.
     * @param object     The object from which you want to invoke a methods.
     * @param methodName The method name to invoke.
     * @param arguments  The arguments to pass to the methods.
     * @param types      The types of arguments.
     * @return the result of the invoked method.
     * @throws NoSuchMethodException Thrown if you try to retreive a method that does not exist.
     * @throws InvocationTargetException Can occure during invocation
     * @throws IllegalAccessException Thrown while trying to access illagaly the instance
     */
    public static Object invokeMethod(final Object object, final String methodName, final Object[] arguments, final Class[] types) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        try {
            // If simple getter is not found ... try boolean one
            return getMethod(object.getClass(), methodName, types).invoke(object, arguments);
        } catch (NoSuchMethodException ex) {
            NoSuchMethodException thrown = new NoSuchMethodException(formatInvokeGetterException(ex, object, methodName, arguments));
            thrown.setStackTrace(ex.getStackTrace());
            throw thrown;
        } catch (InvocationTargetException ex) {
            throw new InvocationTargetException(ex.getTargetException(), formatInvokeGetterException(ex.getTargetException(), object, methodName, arguments));
        } catch (IllegalAccessException ex) {
            IllegalAccessException thrown = new IllegalAccessException(formatInvokeGetterException(ex, object, methodName, arguments));
            thrown.setStackTrace(ex.getStackTrace());
            throw thrown;
        }
    }

    /**
     * Invoke a methods (<code>methodName</code>) from an <code>object</code> with <code>arguments</code> of <code>types</code>.
     * @param clazz      The <code>clazz</code> on which to retreive the method.
     * @param methodName The method name to invoke.
     * @param arguments  The arguments to pass to the methods.
     * @param types      The types of arguments.
     * @return the result of the invoked method.
     * @throws NoSuchMethodException Thrown if you try to retreive a method that does not exist.
     * @throws InvocationTargetException Can occure during invocation
     * @throws IllegalAccessException Thrown while trying to access illagaly the instance
     */
    public static Object invokeStaticMethod(final Class clazz, final String methodName, final Object[] arguments, final Class[] types) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        return getMethod(clazz, methodName, types).invoke(null, arguments);
    }

    /**
     * Invoke a methods (<code>methodName</code>) from an <code>object</code> with <code>arguments</code>.
     * @param object The object from which you want to invoke a methods.
     * @param methodName The method name to invoke.
     * @param arguments The arguments to pass to the methods.
     * @return the result of the invoked method.
     * @throws NoSuchMethodException Thrown if you try to retreive a method that does not exist.
     * @throws InvocationTargetException Can occure during invocation
     * @throws IllegalAccessException Thrown while trying to access illagaly the instance
     */
    public static Object invokeMethod(final Object object, final String methodName, final Object[] arguments) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        // Construct the types array
        Class[] types = new Class[arguments.length];
        for (int i = 0; i < arguments.length; ++i) {
            types[i] = arguments[i].getClass();
        }
        return invokeMethod(object, methodName, arguments, types);
    }

    /**
     * This method invoke the setter according to the <code>fieldName</code> on the <code>toFill</code> object with as argument.
     * <code>valueToSet</code> of type <code>valueToSetType</code>
     * @param toFill the object to fill.
     * @param fieldName the field name.
     * @param valueToSet the value to set.
     * @param valueToSetType the type of the value to set.
     * @throws NoSuchMethodException Thrown if you try to retreive a method that does not exist.
     * @throws InvocationTargetException Can occure during invocation
     * @throws IllegalAccessException Thrown while trying to access illagaly the instance
     */
    public static void invokeSetter(final Object toFill, final String fieldName, final Object valueToSet, final Class valueToSetType) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        invokeMethod(toFill, computeAccessorMethodName(SETTER, fieldName), new Object[] {valueToSet}, new Class[] {valueToSetType});
    }

    /**
     * This method invoke the getter according to the <code>fieldName</code> on the <code>toFill</code> object with as argument.
     * <code>valueToSet</code> of type <code>valueToSetType</code>
     * @param bean the JavaBean object.
     * @param fieldName the field name.
     * @return The value returned by the getter.
     * @throws NoSuchMethodException Thrown if you try to retreive a method that does not exist.
     * @throws InvocationTargetException Can occure during invocation
     * @throws IllegalAccessException Thrown while trying to access illagaly the instance
     */
    public static Object invokeGetter(final Object bean, final String fieldName) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        String field = fieldName;
        Object instance = bean;
        // In the case that you want to retrieve in one time the field of a child object
        while (field.indexOf('.') > 0) {
            // Invoke getter to retreive the child element
            instance = invokeGetter(instance, field.substring(0, field.indexOf('.')));
            // If result is null no need to continue
            if (instance == null) {
                return null;
            }
            // Re rout field name to retreive the field of the child instance
            field = field.substring(field.indexOf('.') + 1);
        }
        try {
            return invokeMethod(instance, computeAccessorMethodName(GETTER, field), new Object[] {});
        } catch (NoSuchMethodException noSuchMethod) {
            String accessor = computeAccessorMethodName(BOOLEAN_GETTER, field);
            try {
                // If simple getter is not found ... try boolean one
                return invokeMethod(instance, accessor, new Object[] {});
            } catch (NoSuchMethodException ex) {
                // If it's still not found or return an error ... than throw the original error.
                throw noSuchMethod;
            }
        }
    }

    /**
     * Format a more detailed message when an exception occured during invocation.
     * @param ex        The exception
     * @param object    The object on which the given method is invoked
     * @param method    The invoked method
     * @param arguments The method arguments
     * @return The detailed message
     */
    private static String formatInvokeGetterException(final Throwable ex, final Object object, final String method, final Object[] arguments) {
        String message = ex.getMessage();
        if (message == null) {
            message = "";
        } else {
            message += " - ";
        }
        String args = "";
        for (int i = 0; i < arguments.length; ++i) {
            args += arguments[i].getClass().getName();
            // Concat each element
            args += arguments[i];
            // Add separator if necessary
            if ((i < arguments.length - 1)) {
                args += ", ";
            }
        }
        return message + "Error occured while tyring to invoke " + object.getClass().getName() + "." + method + "(" + args + ") method.";
    }

    /**
     * Get the field (<code>filedName</code>) value of an <code>object</code>.
     * @param object The <code>object</code> on which to retreive the field value.
     * @param fieldName The <code>fieldName</code> to retreive.
     * @return The value of the field.
     * @throws NoSuchFieldException Thrown if you try to retreive a field that does not exist.
     * @throws InvocationTargetException Can occure during invocation
     * @throws IllegalAccessException Thrown while trying to access illagaly the instance
     */
    public static Object getFieldValue(final Object object, final String fieldName) throws NoSuchFieldException, IllegalAccessException, InvocationTargetException {
        try {
            return getField(object.getClass(), fieldName).get(object);
        } catch (NoSuchFieldException ex) {
            try {
                return invokeGetter(object, fieldName);
            } catch (NoSuchMethodException x) {
                throw ex;
            }
        }
    }

    /**
     * Get the field (<code>filedName</code>) of a class (<code>clazz</code>).
     * @param clazz The <code>clazz</code> on which to retreive the field.
     * @param fieldName The <code>fieldName</code> to retreive.
     * @return The Field object of the given class.
     * @throws NoSuchFieldException Thrown if you try to retreive a field that does not exist.
     */
    public static Field getField(final Class clazz, final String fieldName) throws NoSuchFieldException {
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
            return getField(clazz.getSuperclass(), fieldName);
        }
    }

    /**
     * Retreive the method (<code>methodName</code>) of a class (<code>clazz</code>) using parameters <code>types</code>.
     * @param clazz The <code>clazz</code> on which to retreive the method.
     * @param methodName The method name to retreive.
     * @param types The types of the method's arguments.
     * @return A Method objec.
     * @throws NoSuchMethodException Thrown if you try to retreive a method that does not exist.
     */
    public static Method getMethod(final Class clazz, final String methodName, final Class[] types) throws NoSuchMethodException {
        try {
            Method m = clazz.getDeclaredMethod(methodName, types);
            m.setAccessible(true);
            return m;
        } catch (NoSuchMethodException x) {
            try {
                if (clazz == Object.class) {
                    throw x;
                }
                return getMethod(clazz.getSuperclass(), methodName, types);
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
        if (!accessorType.equals(SETTER) && !accessorType.equals(GETTER) && !accessorType.equals(BOOLEAN_GETTER)) {
            throw new IllegalArgumentException("Accessor Type : " + accessorType + " isn't correct");
        }
        if (null == fieldName || "".equals(fieldName)) {
            throw new IllegalArgumentException("fieldName migth not be null or empty");
        }
        return accessorType + fieldName.replaceFirst("" + fieldName.charAt(0), ("" + fieldName.charAt(0)).toUpperCase());
    }
}
