package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Field;

import ch.judos.generic.data.concurrent.SimpleList;
import ch.judos.generic.data.serialization.DeserializeException;
import ch.judos.generic.data.serialization.ReadableStorageImpl;
import ch.judos.generic.data.serialization.WrapperFactory;
import ch.judos.generic.data.serialization.types.RStorableObjectWrapperImpl;
import ch.judos.generic.reflection.Fields;

/**
 * @since 21.10.2014
 * @author Julian Schelker
 */
public class RStorableSimpleList extends RStorableObjectWrapperImpl {

	public static class Factory implements WrapperFactory<RStorableSimpleList> {

		private ReadableStorageImpl	impl;

		public Factory(ReadableStorageImpl readableStorageImpl) {
			this.impl = readableStorageImpl;
		}

		@Override
		public RStorableSimpleList createInstance() {
			return new RStorableSimpleList(this.impl);
		}

		@Override
		public RStorableSimpleList createInstance(Object wrappedObj) {
			return new RStorableSimpleList(wrappedObj, this.impl);
		}

	}

	public RStorableSimpleList(Object o, ReadableStorageImpl rStore) {
		super(o, rStore);
	}

	@SuppressWarnings("rawtypes")
	public RStorableSimpleList(ReadableStorageImpl rStore) {
		super(new SimpleList(), rStore);
	}

	@Override
	public Object getFieldData(Field f) throws IllegalArgumentException,
		IllegalAccessException {
		if (f.getName().equals("elementData"))
			return ((SimpleList<?>) this.wrapped).toArray();
		return super.getFieldData(f);
	}

	@Override
	public Object readFrom(BufferedReader r) throws IOException, DeserializeException,
		ClassNotFoundException {
		Object object = super.readFrom(r);
		try {
			Field sizeField = Fields.getAnyField(object.getClass(), "size");
			sizeField.setAccessible(true);
			Field dataField = Fields.getAnyField(object.getClass(), "elementData");
			dataField.setAccessible(true);
			Object[] dataArr = (Object[]) dataField.get(object);
			sizeField.set(object, dataArr.length);
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return object;
	}

	@Override
	public boolean serializeField(Field f) {
		if (f.getName().equals("elementData"))
			return true;
		if (f.getName().equals("size"))
			return false;
		return super.serializeField(f);
	}

}
