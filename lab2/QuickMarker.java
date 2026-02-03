import java.util.LinkedList;
import java.util.Queue;

public class QuickMarker {
  private Queue<Applicant> queue;
  private Applicant[] applicants;
  private int numberApplicants;
  private int skip;

  public static void main(String[] args) {
    QuickMarker qm = new QuickMarker(10, 3);
    Applicant[] applicants = qm.getMarking();
    for (Applicant a : applicants) {
      System.out.println(a);
    }
  }

  public QuickMarker(int numberApplicants, int skip) {
    this.numberApplicants = numberApplicants;
    this.applicants = new Applicant[numberApplicants];
    this.skip = skip;
    this.queue = new LinkedList<>();
    initApplicants();
  }

  private void initApplicants() {
    for (int i = 0; i < numberApplicants; i++) {
      applicants[i] = new Applicant(i, 0);
    }
  }

  private void runMarking() {
    // We can use our 'circular queue' concept to implement this algorithm

    // We want to iteratively count the applicants through 'skip' steps

    // We do this by removing an applicant from the front of the queue and
    // putting them to the back

    // Once we've hit the 'skip' steps, we remove the applicant from the queue
    // and assign them their mark

    // We repeat this until there are no more applicants left in the queue

    // To visualize this, think of yourself stay fixed in one position and
    // rotating the queue around you 'skip' times. Then remove whoever is in
    // front of you and repeat.
  }

  public Applicant[] getMarking() {
    runMarking();
    return applicants;
  }
}

class Applicant {
  int applicantNumber;
  float mark;

  public Applicant(int applicantNumber, int mark) {
    this.applicantNumber = applicantNumber;
    this.mark = mark;
  }

  public String toString() {
    return "Applicant " + applicantNumber + " got a mark of " + mark;
  }

  private void validateMark(float mark) throws IllegalArgumentException {
    if (mark < 0 || mark > 100) {
      throw new IllegalArgumentException("Mark must be between 0 and 100");
    }
  }

  public void setMark(float mark) {
    validateMark(mark);
    this.mark = mark;
  }
}
