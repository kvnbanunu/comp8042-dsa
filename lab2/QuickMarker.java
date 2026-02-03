import java.util.LinkedList;
import java.util.Queue;

public class QuickMarker {
  private Queue<Applicant> queue;
  private Applicant[] applicants;
  private int numberApplicants;
  private int skip;

  public static void main(String[] args) {
    // 1. a=12 n=3
    QuickMarker qm = new QuickMarker(12, 3);
    Applicant[] applicants = qm.getMarking();
    System.out.println("\nCase 1: a = 12, n = 3");
    for (Applicant a : applicants) {
      System.out.println(a);
    }

    // 2. a=180 n=37 get 50% Applicant
    qm = new QuickMarker(180, 37);
    applicants = qm.getMarking();
    System.out.println("\nCase 2: a = 180, n = 37");
    System.out.println("Applicant with 50%: " + qm.getApplicantWithMark(50.0F).applicantNumber);

    // 3. a=1100 n=259 get 100% Applicant
    qm = new QuickMarker(1100, 259);
    applicants = qm.getMarking();
    System.out.println("\nCase 3: a = 1100, n = 259");
    System.out.println("Applicant with 100%: " + qm.getApplicantWithMark(100.0F).applicantNumber);
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
    int marksGiven = 0;
    for (int i = 0; i < numberApplicants; i++) {
      if (i + 1 % skip == 0) {
        marksGiven++;
        float mark = (float) (marksGiven) / (float) (numberApplicants) * 100;
        applicants[i].setMark(mark);
      } else {
        queue.offer(applicants[i]);
      }
    }

    int seen = 0;
    while (numberApplicants - marksGiven > 0) {
      seen++;
      if (seen % skip == 0) {
        marksGiven++;
        Applicant curr = queue.poll();
        float mark = (float) (marksGiven) / (float) (numberApplicants) * 100;
        applicants[curr.applicantNumber].setMark(mark);
      } else {
        queue.offer(queue.poll());
      }
    }
  }

  public Applicant[] getMarking() {
    runMarking();
    return applicants;
  }

  public Applicant getApplicantWithMark(float mark) {
    for (Applicant a : applicants) {
      if (a.mark == mark) {
        return a;
      }
    }
    return null;
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
