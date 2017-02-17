package com.qybx.conf;

import java.io.IOException;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.FieldCache;
import org.apache.lucene.search.FieldComparator;
import org.apache.lucene.search.FieldComparatorSource;

/**
 * This class is used for ...
 * 
 * @author leepon1990
 * @version 1.0, 2016年11月30日 下午4:49:05
 */
public class MyFieldComparatorSource extends FieldComparatorSource {

	/**
	 * @Fields serialVersionUID : TODO
	 */

	private static final long serialVersionUID = 7408344251758802260L;

	@Override
	public FieldComparator<?> newComparator(String fieldname, int numHits, int sortPos, boolean reversed)
			throws IOException {
		System.out.println(fieldname);
		System.out.println(numHits);
		System.out.println(sortPos);
		System.out.println(reversed);
		return new StrValComparator(numHits, fieldname);
	}

	final class StrValComparator extends FieldComparator<String> {

		private String[] values;

		private String[] currentReaderValues;

		private final String field;

		private String bottom;

		public StrValComparator(int numHits, String field) {
			values = new String[numHits];
			this.field = field;
		}

		@Override
		public int compare(int slot1, int slot2) {
			final String val1 = values[slot1];
			final String val2 = values[slot2];
			if (val1 == null) {
				if (val2 == null) {
					return 0;
				}
				return -1;
			} else if (val2 == null) {
				return 1;
			}

			return val1.compareTo(val2);
		}

		@Override
		public void setBottom(int bottom) {
			this.bottom = values[bottom];
		}

		@Override
		public int compareBottom(int doc) throws IOException {

			final String val2 = currentReaderValues[doc];
			if (bottom == null) {
				if (val2 == null) {
					return 0;
				}
				return -1;
			} else if (val2 == null) {
				return 1;
			}
			return bottom.compareTo(val2);
		}

		@Override
		public void copy(int slot, int doc) throws IOException {
			values[slot] = currentReaderValues[doc];
		}

		@Override
		public void setNextReader(IndexReader reader, int docBase) throws IOException {
			currentReaderValues = FieldCache.DEFAULT.getStrings(reader, field);
		}

		@Override
		public String value(int slot) {
			return values[slot];
		}

	}

}
