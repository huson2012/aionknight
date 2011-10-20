package ru.aionknight.commons.configuration;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to mark field that should be processed by
 * {@link ru.aionknight.commons.configuration.ConfigurableProcessor}<br>
 * <br>
 * 
 * This annotation is Documented, all definitions with it will appear in javadoc
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Property
{
	/**
	 * This string shows to {@link ru.aionknight.commons.configuration.ConfigurableProcessor} that init value of the
	 * object should not be overriden.
	 */
	public static final String	DEFAULT_VALUE	= "DO_NOT_OVERWRITE_INITIALIAZION_VALUE";

	/**
	 * Property name in configuration
	 * 
	 * @return name of the property that will be used
	 */
	public String key();

	/**
	 * PropertyTransformer to use.<br>
	 * List of automaticly transformed types:<br>
	 * <ul>
	 * <li>{@link Boolean} and boolean by {@link ru.aionknight.commons.configuration.transformers.BooleanTransformer}</li>
	 * <li>{@link Byte} and byte by {@link ru.aionknight.commons.configuration.transformers.ByteTransformer}</li>
	 * <li>{@link Character} and char by {@link ru.aionknight.commons.configuration.transformers.CharTransformer}</li>
	 * <li>{@link Short} and short by {@link ru.aionknight.commons.configuration.transformers.ShortTransformer}</li>
	 * <li>{@link Integer} and int by {@link ru.aionknight.commons.configuration.transformers.IntegerTransformer}</li>
	 * <li>{@link Float} and float by {@link ru.aionknight.commons.configuration.transformers.FloatTransformer}</li>
	 * <li>{@link Long} and long by {@link ru.aionknight.commons.configuration.transformers.LongTransformer}</li>
	 * <li>{@link Double} and double by {@link ru.aionknight.commons.configuration.transformers.DoubleTransformer}</li>
	 * <li>{@link String} by {@link ru.aionknight.commons.configuration.transformers.StringTransformer}</li>
	 * <li>{@link Enum} and enum by {@link ru.aionknight.commons.configuration.transformers.EnumTransformer}</li>
	 * <li>{@link java.io.File} by {@link ru.aionknight.commons.configuration.transformers.FileTransformer}</li>
	 * <li>{@link java.net.InetSocketAddress} by
	 * {@link ru.aionknight.commons.configuration.transformers.InetSocketAddressTransformer}</li>
	 * <li>{@link java.util.regex.Pattern} by {@link ru.aionknight.commons.configuration.transformers.PatternTransformer}
	 * </ul>
	 * <p/>
	 * If your value is one of this types - just leave this field empty
	 * 
	 * @return returns class that will be used to transform value
	 */
	@SuppressWarnings("rawtypes")
	public Class<? extends PropertyTransformer> propertyTransformer() default PropertyTransformer.class;

	/**
	 * Represents default value that will be parsed if key not found. If this key equals(default) {@link #DEFAULT_VALUE}
	 * init value of the object won't be overriden
	 * 
	 * @return default value of the property
	 */
	public String defaultValue() default DEFAULT_VALUE;
}
