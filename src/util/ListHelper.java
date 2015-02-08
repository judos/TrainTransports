package util;

import java.util.List;
import java.util.function.BiFunction;

/**
 * @since 08.02.2015
 * @author Julian Schelker
 */
public class ListHelper {

	public enum Key {
		CONTINUE, BREAK;
	}

	public static <E> void iterate(List<E> list, BiFunction<E, Integer, Key> step) {
		for (int i = 0; i < list.size(); i++)
			if (step.apply(list.get(i), i) == Key.BREAK)
				return;
	}

}
