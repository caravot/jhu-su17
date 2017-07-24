package com.javadude.maps;
import com.javadude.maps.UFOPosition;
import java.util.List;

interface UFOPositionReporter {
	void report(in List<UFOPosition> ufoPosition);
}