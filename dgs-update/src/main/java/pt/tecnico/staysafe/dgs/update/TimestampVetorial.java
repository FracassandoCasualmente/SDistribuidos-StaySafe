package pt.tecnico.staysafe.dgs.update;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class TimestampVetorial {
	private Integer[] _array;
	private final Integer DEFAULT_NUM_REPLICAS = 3;

	// builds the default timestamp
	public TimestampVetorial() {
		_array = new Integer[DEFAULT_NUM_REPLICAS];
		for(int i=0;i<DEFAULT_NUM_REPLICAS;i++) _array[i] = 0;
	}
	// receives the number of replicas and creates zero-ed timestamp
	public TimestampVetorial(Integer numReps) {
		_array = new Integer[numReps];
		for(int i=0;i<numReps;i++) _array[i] = 0;
	}

	public TimestampVetorial(Integer[] array) {
		_array = array;
		for(int i=0;i<array.length;i++) _array[i] = array[i];
	}

	// returns
	// true - if this happens before the other or are equal
	// false - if this happens after the other, or if they are concurrent
	// (deprecated)null - if they are concurrent
	// throws exception if size is different
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
			return false; // it used to be null in deprecated version!
		}
		// if none is bigger and none is smaller, they are equal
		if (!oneIsBigger && !oneIsSmaller) {
			return true;
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
		return (_array.length < repId) ? null : _array[repId - 1];
	}

	// sets the repId timestamp position to value
	// returns true if success, false otherwise
	public Boolean setPos(Integer repId, Integer value) {
		if ( _array.length < repId ) {
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

	// increments every position [i] of my timestamp
	// to the max of ours both timestamps
	public void update(TimestampVetorial otherTV) {
		for (int i = 0; i < _array.length; i++) {
			if ( _array[i] < otherTV._array[i] ) {
				_array[i] = otherTV._array[i];
			}
		}
	}
	
	// returns the timestamp as a list
	public ArrayList<Integer> getTvAsList()
	{
		return new ArrayList<Integer>(Arrays.asList(_array));
	}
}