package pt.tecnico.staysafe.dgs.update;

import java.io.IOException;

public class TimestampVetorial {
	private Integer[] _array;

	// receives the number of replicas and creates zero-ed timestamp
	public TimestampVetorial(Integer numReps) {
		_array = new Integer[numReps];
	}

	public TimestampVetorial(Integer[] array) {
		_array = array;
	}

	// returns
	// true - if this happens before the other
	// false - if this happens after the other
	// null - if they are concurrent
	// throws exception if size is different or timestamps are equal
	public Boolean happensBefore(TimestampVetorial otherTV) throws IOException {
		// its true if every pos is >= than the other and
		// atleast one is bigger
		if (this._array.length != otherTV._array.length) {
			throw new IOException("Different length in vectorial timestamps!");
		}
		Boolean oneIsBigger = false;
		Boolean oneIsSmaller = false;
		Integer[] myArr = this._array;
		Integer[] hisArr = otherTV._array;

		for (int i = 0; i < _array.length; i++) {
			// if they are equal
			if (myArr[i] == hisArr[i]) {
				continue;
			}
			// if one is bigger than his
			if (myArr[i] > hisArr[i]) {
				oneIsBigger = true;
				continue;
			}
			// one is smaller than his
			oneIsSmaller = true;
			
		}

		/* ---calculate result--- */

		// if one is smaller and one is bigger, then
		// they are concurrent
		if ( oneIsBigger && oneIsSmaller ) { 
			return null; 
		}
		// if none is bigger and none is smaller, they are equal
		if (!oneIsBigger && !oneIsSmaller) {
			throw new IOException("The timestamps are equal");
		}
		// if mine happens after the other
		if (oneIsBigger && !oneIsSmaller) return false;

		// if mine happens before the other
		return true;
	}

	public Boolean equals(TimestampVetorial otherTV) throws IOException {
		if (this._array.length != otherTV._array.length) {
			throw new IOException("Different length in vectorial timestamps!");
		}

		Integer[] myArr = this._array;
		Integer[] hisArr = otherTV._array;

		for (int i = 0; i < _array.length; i++) {
			// if they are equal
			if (myArr[i] != hisArr[i]) {
				return false;
			}
		}
		return true;
	}

	// receives replica id and returns it's timestamp position
	// null if the rep id is too long
	public Integer getPos(Integer repId) {
		return (_array.length > repId) ? null : _array[repId - 1];
	}

	// sets the repId timestamp position to value
	// returns true if success, false otherwise
	public Boolean setPos(Integer repId, Integer value) {
		if ( _array.length > repId ) {
			return false;
		}
		_array[repId - 1] = value;
		return true;
	}

	// returns something like "< n1, n2, n3 >"
	public String toString() {
		String res = "< ";
		for (Integer i : _array) {
			res += i + ", ";
		}
		res = res.substring(0, res.length() - 2); // remove last ", "
		res = res + " >";
		return res;
	}
}