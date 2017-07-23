package com.javadude.maps;
import com.javadude.maps.UFOPositionReporter;

interface UFOService {
	void reset();
	void add(UFOPositionReporter reporter);
	void remove(UFOPositionReporter reporter);
}
