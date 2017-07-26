package ravotta.carrie.HW4;
import ravotta.carrie.HW4.UFOPositionReporter;

interface UFOService {
	void reset();
	void add(UFOPositionReporter reporter);
	void remove(UFOPositionReporter reporter);
}
