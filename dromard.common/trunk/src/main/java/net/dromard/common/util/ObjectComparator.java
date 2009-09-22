/**
 * 	File : ObjectComparator.java 19 oct. 07
 */

package net.dromard.common.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Tool class that compare two object.
 * <br>
 * @author Gabriel Dromard
 */
public final class ObjectComparator {
    /** For debugging purpose. */
    private static HashMap<Integer, String> debug = new HashMap<Integer, String>();
    /** Preserve last hierarchy position. */
    private static int debugHierarchy = -1;

    /**
     * Private empty constructor for util class.
     */
    private ObjectComparator() {
    }

    /**
     * Compare two given object, parse all fields recursivly and test if there are (all) equals.
     * @param source The source object
     * @param object The object to compare.
     * @return True if the two object are equals.
     * @throws Exception Has reflection is used for testing equality betwwen two objects ...
     */
    public static boolean equals(final Object source, final Object object) throws Exception {
        return ObjectComparator.equals(source, object, true);
    }

    /**
     * Compare two given object, parse all fields recursivly and test if there are (all) equals.
     * @param source The source object
     * @param object The object to compare.
     * @param strict If set to false the list element will be compared without taking care of ordering
     * @return True if the two object are equals.
     * @throws Exception Has reflection is used for testing equality betwwen two objects ...
     */
    public static synchronized boolean equals(final Object source, final Object object, final boolean strict) throws Exception {
        if (source == null && object == null || source == object) {
            return true;
        }
        // If not null and If their types are equals
        if (source != null && object != null) {
            Class clazz = null;
            if (source instanceof Collection && object instanceof Collection) {
                if (ObjectComparator.collectionsEqual((Collection) source, (Collection) object, strict)) {
                    return true;
                }
                ObjectComparator.addDebugMessage(source.getClass() + " [NOT EQUAL] '" + source + "' != '" + object + "'");
                return ObjectComparator.printMessagesAndReturnFalse();
            }
            if (source instanceof Map && object instanceof Map) {
                if (ObjectComparator.mapsEqual((Map) source, (Map) object, strict)) {
                    return true;
                }
                ObjectComparator.addDebugMessage(source.getClass() + " [NOT EQUAL] '" + source + "' != '" + object + "'");
                return ObjectComparator.printMessagesAndReturnFalse();
            }
            if (source.getClass().equals(object.getClass())) {
                clazz = source.getClass();
            } else {
                if (ObjectComparator.commonInterfaces(source.getClass(), object.getClass()).size() > 0) {
                    ObjectComparator.addDebugMessage("[NOT EQUAL] '" + source.getClass() + "' and '" + object.getClass() + "' have a common interface but we are not able to test equality !");
                } else {
                    ObjectComparator.addDebugMessage("[NOT EQUAL] '" + source.getClass() + "' and '" + object.getClass() + "' !");
                }
                return ObjectComparator.printMessagesAndReturnFalse();
            }
            if (clazz != null) {
                if (clazz.isPrimitive() || clazz.equals(String.class) || clazz.equals(Object.class)) {
                    if (!source.equals(object)) {
                        ObjectComparator.addDebugMessage(clazz + " [NOT EQUAL] '" + source + "' != '" + object + "'");
                        return ObjectComparator.printMessagesAndReturnFalse();
                    }
                    return true;
                }
                List fields = ReflectHelper.getDeclaredFields(clazz);
                for (int i = 0; i < fields.size(); i++) {
                    Field field = (Field) fields.get(i);
                    Object srcValue = ReflectHelper.getFieldValue(source, field.getName());
                    Object objValue = ReflectHelper.getFieldValue(object, field.getName());
                    if (srcValue == null || objValue == null) {
                        if (srcValue == null && objValue == null) {
                            return true;
                        }
                        ObjectComparator.addDebugMessage("[NOT EQUAL] '" + srcValue + "' != '" + objValue + "'");
                        return ObjectComparator.printMessagesAndReturnFalse();
                    }
                    if (field.getType().isPrimitive() || field.getType().equals(String.class) || field.getType().equals(Object.class)) {
                        if (!srcValue.equals(objValue)) {
                            ObjectComparator.addDebugMessage(field.getName() + " [NOT EQUAL] '" + srcValue + "' != '" + objValue + "'");
                            return ObjectComparator.printMessagesAndReturnFalse();
                        }
                    } else if (field.getType().isArray()) {
                        if (!ObjectComparator.arrayEqual(srcValue, objValue)) {
                            ObjectComparator.addDebugMessage(field.getName() + " [NOT EQUAL] '" + srcValue + "' != '" + objValue + "'");
                            return ObjectComparator.printMessagesAndReturnFalse();
                        }
                    } else {
                        if (!ObjectComparator.equals(srcValue, objValue, strict)) {
                            ObjectComparator.addDebugMessage(field.getName() + " [NOT EQUAL] '" + srcValue + "' != '" + objValue + "'");
                            return ObjectComparator.printMessagesAndReturnFalse();
                        }
                    }
                }
                // If we passed all equals on each fields ...
                return true;
            }
        }
        return false;
    }

    /**
     * Test equality of two map.
     * @param map1 The first one
     * @param map2 The second one
     * @return True if the map are equal
     * @throws Exception Has reflection is used for testing equality betwwen two objects ...
     */
    public static boolean mapsEqual(final Map map1, final Map map2) throws Exception {
        return ObjectComparator.mapsEqual(map1, map2, true);
    }

    /**
     * Test equality of two map.
     * @param map1 The first one
     * @param map2 The second one
     * @param strict If set to false the list element will be compared without taking care of ordering
     * @return True if the map are equal
     * @throws Exception Has reflection is used for testing equality betwwen two objects ...
     */
    public static boolean mapsEqual(final Map map1, final Map map2, final boolean strict) throws Exception {
        if (map1.size() != map2.size()) {
            ObjectComparator.addDebugMessage("[NOT EQUAL] Map does not contain same number of elements", map1, map2);
            return false;
        }
        for (Iterator iter1 = map1.keySet().iterator(); iter1.hasNext();) {
            Object key = iter1.next();
            Object obj = map1.get(key);
            if (!ObjectComparator.mapContains(map2, key, obj, strict)) {
                ObjectComparator.addDebugMessage("[NOT CONTAINS] Map does not contain ", map2, obj);
                return false;
            }
        }
        return true;
    }

    /**
     * Test if a map contains a given object (stored in a given key).
     * @param map    The map
     * @param key    The key of the object in the map
     * @param object The object it self
     * @return True if the given object is in collection
     * @throws Exception Has reflection is used for testing equality betwwen two objects ...
     */
    public static boolean mapContains(final Map map, final Object key, final Object object) throws Exception {
        return ObjectComparator.mapContains(map, key, object, true);
    }

    /**
     * Test if a map contains a given object (stored in a given key).
     * @param map    The map
     * @param key    The key of the object in the map
     * @param object The object it self
     * @param strict If set to false the list element will be compared without taking care of ordering
     * @return True if the given object is in collection
     * @throws Exception Has reflection is used for testing equality betwwen two objects ...
     */
    public static boolean mapContains(final Map map, final Object key, final Object object, final boolean strict) throws Exception {
        if (map == null) {
            return false;
        }
        Object obj = map.get(key);
        if (obj != null) {
            return ObjectComparator.equals(object, obj, strict);
        }
        return false;
    }

    /**
     * Test equality of two collections.
     * @param collection1 The first one
     * @param collection2 The second one
     * @return True if the collection are equals
     * @throws Exception Has reflection is used for testing equality betwwen two objects ...
     */
    public static boolean collectionsEqual(final Collection collection1, final Collection collection2) throws Exception {
        return ObjectComparator.collectionsEqual(collection1, collection2, true);
    }

    /**
     * Test equality of two collections.
     * @param collection1 The first one
     * @param collection2 The second one
     * @param strict If set to false the list element will be compared without taking care of ordering
     * @return True if the collection are equals
     * @throws Exception Has reflection is used for testing equality betwwen two objects ...
     */
    public static boolean collectionsEqual(final Collection collection1, final Collection collection2, final boolean strict) throws Exception {
        if (collection1.size() != collection2.size()) {
            ObjectComparator.addDebugMessage("[NOT EQUAL] Collection does not contain same number of elements", collection1, collection2);
            return false;
        }
        if (strict && collection1 instanceof List && collection2 instanceof List) {
            List list1 = (List) collection1;
            List list2 = (List) collection2;
            for (int i = 0; i < list1.size(); ++i) {
                if (!ObjectComparator.equals(list1.get(i), list2.get(i), strict)) {
                    ObjectComparator.addDebugMessage("[NOT EQUALS] Element at index " + i + " are not equals", list1.get(i), list2.get(i));
                    return false;
                }
            }
            return true;
        }
        for (Iterator iter1 = collection1.iterator(); iter1.hasNext();) {
            Object obj = iter1.next();
            if (!ObjectComparator.collectionContains(collection2, obj, strict)) {
                ObjectComparator.addDebugMessage("[NOT CONTAINS] Collection does not contain ", collection2, obj);
                return false;
            }
        }
        return true;
    }

    /**
     * Test if a collection contains a given object.
     * @param collection The collection
     * @param object The object
     * @return True if the given object is in collection
     * @throws Exception Has reflection is used for testing equality betwwen two objects ...
     */
    public static boolean collectionContains(final Collection collection, final Object object) throws Exception {
        return ObjectComparator.collectionContains(collection, object, true);
    }

    /**
     * Test if a collection contains a given object.
     * @param collection The collection
     * @param object The object
     * @param strict If set to false the list element will be compared without taking care of ordering
     * @return True if the given object is in collection
     * @throws Exception Has reflection is used for testing equality betwwen two objects ...
     */
    public static boolean collectionContains(final Collection collection, final Object object, final boolean strict) throws Exception {
        if (collection == null) {
            return false;
        }
        for (Iterator iter = collection.iterator(); iter.hasNext();) {
            Object obj2 = iter.next();
            if (ObjectComparator.equals(object, obj2, strict)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Compare two given object array, parse all fields recursivly and test if there are (all) equals.
     * @param srcArray The source array
     * @param objArray The object array to compare.
     * @return True if the two object are equals.
     * @throws Exception Has reflection is used for testing equality betwwen two objects ...
     */
    public static boolean arrayEqual(final Object srcArray, final Object objArray) throws Exception {
        return ObjectComparator.arrayEqual(srcArray, objArray, true);
    }

    /**
     * Compare two given object array, parse all fields recursivly and test if there are (all) equals.
     * @param srcArray The source array
     * @param objArray The object array to compare.
     * @param strict If set to false the list element will be compared without taking care of ordering
     * @return True if the two object are equals.
     * @throws Exception Has reflection is used for testing equality betwwen two objects ...
     */
    public static boolean arrayEqual(final Object srcArray, final Object objArray, final boolean strict) throws Exception {
        ObjectComparator.addDebugMessage("arrayEquals", srcArray, objArray);
        if (Array.getLength(srcArray) != Array.getLength(objArray)) {
            ObjectComparator.addDebugMessage(srcArray.getClass() + " [NOT EQUAL] '" + srcArray + "' != '" + objArray + "'");
            return false;
        }
        for (int i = 0; i < Array.getLength(srcArray); ++i) {
            Object src = Array.get(srcArray, i);
            Object obj = Array.get(objArray, i);
            if (!(srcArray instanceof Object[]) || (srcArray instanceof String[])) {
                if (!src.equals(obj)) {
                    ObjectComparator.addDebugMessage(srcArray.getClass() + "[" + i + "] [NOT EQUAL] '" + src + "' != '" + obj + "'");
                    return false;
                }
            } else if (!ObjectComparator.equals(src, obj, strict)) {
                ObjectComparator.addDebugMessage(srcArray.getClass() + "[" + i + "] [NOT EQUAL] '" + src + "' != '" + obj + "'");
                return false;
            }
        }
        return true;
    }

    /**
     * Retreive common interfaces of two different classes.
     * @param class1 The first one
     * @param class2 The second one
     * @return The common interface of the two classes
     */
    private static List commonInterfaces(final Class class1, final Class class2) {
        List interfaces1 = new ArrayList();
        interfaces1.addAll(Arrays.asList(class1.getInterfaces()));
        List interfaces2 = new ArrayList();
        interfaces2.addAll(Arrays.asList(class2.getInterfaces()));
        interfaces1.retainAll(interfaces2);
        return interfaces1;
    }

    /**
     * Print a ko message for debugging purpose.
     * @param message The message to be print.
     * @param src The source object
     * @param obj The object
     */
    private static void addDebugMessage(final String message, final Object src, final Object obj) {
        int list = ObjectComparator.countCollectionOrMapHierarchy();
        //debug = (message.indexOf("NOT CONTAINS") > -1 && list < 1) || (message.indexOf("NOT") > -1 && list < 1);

        Throwable th = new Exception();
        int offset = 1;
        StackTraceElement elmt = th.getStackTrace()[offset];
        if (elmt.getMethodName().indexOf("addDebugMessage") > -1) {
            elmt = th.getStackTrace()[++offset];
        }
        int hierachy = 0;
        while (elmt.getClassName().indexOf(ObjectComparator.class.getName()) > -1) {
            elmt = th.getStackTrace()[++hierachy + offset];
        }
        hierachy -= list;
        elmt = th.getStackTrace()[offset + hierachy];
        // Remove last one
        --hierachy;
        String pad = StringHelper.pad("", hierachy, "  ");
        String suffix = "";
        if (src != null && obj != null) {
            suffix = " <" + src.getClass().getName() + " -- " + obj.getClass().getName() + "> ['" + src + "' -- '" + obj + "']";
        }
        String msg = "[DEBUG] <" + hierachy + ">" + pad + message + suffix + " # " + elmt.getClassName() + "." + elmt.getMethodName() + "(" + elmt.getFileName() + ":" + elmt.getLineNumber() + ")";
        if (message.indexOf("NOT") > -1) {
            if (ObjectComparator.debugHierarchy == -1 || hierachy < ObjectComparator.debugHierarchy) {
                ObjectComparator.debug.put(hierachy, msg);
                ObjectComparator.debugHierarchy = hierachy;
            }
        }
        if (hierachy == 1) {
            if (message.indexOf("NOT") == -1) {
                ObjectComparator.debug.clear();
            }
            ObjectComparator.printMessages();
        }
    }

    /**
     * Util method that print debug informations and return false.
     * @return false
     */
    private static boolean printMessagesAndReturnFalse() {
        ObjectComparator.printMessages();
        return false;
    }

    /**
     * Util method that print debug informations.
     */
    private static void printMessages() {
        if (ObjectComparator.debug.get(1) != null) {
            for (int i = 1; i <= ObjectComparator.debug.size(); ++i) {
                System.out.println(ObjectComparator.debug.get(i));
            }
            ObjectComparator.debug.clear();
        }
    }

    /**
     * Say if the process is iterating in a collection or a map.
     * @return True if the process is iterating
     */
    private static int countCollectionOrMapHierarchy() {
        int count = 0;
        Throwable th = new Exception();
        String methodName;
        for (int i = 0; i < th.getStackTrace().length; ++i) {
            methodName = th.getStackTrace()[i].getMethodName();
            if (methodName.indexOf("sEqual") > -1) {
                ++count;
            }
        }
        return count;
    }

    /**
     * Print a ko message for debugging purpose.
     * @param message The message to be print.
     */
    private static void addDebugMessage(final String message) {
        ObjectComparator.addDebugMessage(message, null, null);
    }
}
