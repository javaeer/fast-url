package cn.net.yunlou.fasturl;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author wangyb
 */
public interface IEnum<T> extends Serializable {

    /**
     * 键值类型 Integer、Long、String
     *
     * @return T 值
     */
    T getValue();

    /**
     * 抽象方法
     *
     * @return 描述
     */
    String getLabel();


    /**
     * 获取枚举
     *
     * @param value
     * @param clazz
     * @param <E>
     * @return
     */
    static <E extends Enum<E> & IEnum<?>> E getEnumByValue(Object value, Class<E> clazz) {
        Objects.requireNonNull(value, "Value cannot be null");
        return EnumSet.allOf(clazz).stream()
                .filter(e -> Objects.equals(e.getValue(), value))
                .findFirst()
                .orElse(null);
    }

    /**
     * 获取枚举描述
     *
     * @param value
     * @param clazz
     * @param <E>
     * @return
     */
    static <E extends Enum<E> & IEnum<?>> String getLabelByValue(Object value, Class<E> clazz) {
        Objects.requireNonNull(value, "Value cannot be null");
        return Optional.ofNullable(getEnumByValue(value, clazz))
                .map(IEnum::getLabel)
                .orElse(null);
    }

    /**
     * 根据描述 获取枚举值
     *
     * @param label
     * @param clazz
     * @param <E>
     * @return
     */
    static <E extends Enum<E> & IEnum<?>> Object getValueByLabel(String label, Class<E> clazz) {
        Objects.requireNonNull(label, "Label cannot be null");
        return EnumSet.allOf(clazz).stream()
                .filter(e -> Objects.equals(e.getLabel(), label))
                .findFirst()
                .map(IEnum::getValue)
                .orElse(null);
    }

    /**
     * 验证枚举值
     *
     * @param value
     * @param clazz
     * @param <E>
     * @return
     */
    static <E extends Enum<E> & IEnum<?>> boolean validateValue(Object value, Class<E> clazz) {
        try {
            return value != null && getEnumByValue(value, clazz) != null;
        } catch (NullPointerException e) {
            return false;
        }
    }

    /**
     * 根据值定器 转换为map
     *
     * @param clazz
     * @param valueExtractor
     * @param <E>
     * @return
     */
    static <E extends Enum<E> & IEnum<?>> Map<E, Object> toMap(Class<E> clazz,
                                                               Function<E, Object> valueExtractor) {
        EnumMap<E, Object> enumMap = new EnumMap<>(clazz);
        EnumSet.allOf(clazz).forEach(e -> enumMap.put(e, valueExtractor.apply(e)));
        return enumMap;
    }

    /**
     * 获取所有值的Map
     *
     * @param clazz
     * @param <E>
     * @return
     */
    static <E extends Enum<E> & IEnum<?>> Map<E, Object> getAllValues(Class<E> clazz) {
        return toMap(clazz, IEnum::getValue);
    }

    /**
     * 获取所有标签的Map
     *
     * @param clazz
     * @param <E>
     * @return
     */
    static <E extends Enum<E> & IEnum<?>> Map<E, Object> getAllLabels(Class<E> clazz) {
        return toMap(clazz, IEnum::getLabel);
    }

    /**
     * 获取所有值的列表
     *
     * @param clazz
     * @param <E>
     * @return
     */
    static <E extends Enum<E> & IEnum<?>> List<Object> getValueList(Class<E> clazz) {
        return EnumSet.allOf(clazz).stream()
                .map(IEnum::getValue)
                .collect(Collectors.toList());
    }

    /**
     * 获取所有标签的列表
     *
     * @param clazz
     * @param <E>
     * @return
     */
    static <E extends Enum<E> & IEnum<?>> List<String> getLabelList(Class<E> clazz) {
        return EnumSet.allOf(clazz).stream()
                .map(IEnum::getLabel)
                .collect(Collectors.toList());
    }

}
