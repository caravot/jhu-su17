// UFOPositionAIDL.aidl
package com.javadude.maps;
import com.javadude.maps.UFOPositionReporter;

parcelable UFOPosition;

interface UFOPositionAIDL {
	void reset();
	void add(UFOPositionReporter reporter);
	void remove(UFOPositionReporter reporter);
}
