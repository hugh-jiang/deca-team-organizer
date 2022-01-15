package DecaTeamOrganizer;

/**
 * The Member class containing instance fields and methods for each Member that
 * will be added in the DECA team organizer.
 *
 * @author Hugh Jiang
 */
public class Member {

    private String email;
    private String eventID; // DECA event ID, i.e. BFS or FTDM
    private String firstName;
    private int grade;
    private String lastName;
    private String subteam; // DECA subteam name

    /**
     * Constructor to initialize this Member's instance fields
     *
     * @param firstName the String containing the first name of this Member
     * @param lastName the String containing the last name of this Member
     * @param email the String containing this Member's email
     * @param grade the integer containing this Member's grade level
     * @param subteam the String containing this Member's subteam
     * @param eventID the String with this Member's DECA event ID (i.e. BFS,
     * BTDM, etc)
     */
    public Member(String firstName, String lastName, String email, int grade, String subteam, String eventID) {
        this.firstName = firstName.trim();
        this.lastName = lastName.trim();
        this.email = email.trim();
        this.grade = grade;
        this.subteam = subteam.trim();
        this.eventID = eventID.trim();
    }

    /**
     * Constructor to initialize this Member's instance fields without
     * parameters for subteam and eventID, which will be set to default
     * temporary values
     *
     * @param firstName the String containing the first name of this Member
     * @param lastName the String containing the last name of this Member
     * @param email the String containing this Member's email
     * @param grade the integer containing this Member's grade level
     */
    public Member(String firstName, String lastName, String email, int grade) {
        this.firstName = firstName.trim();
        this.lastName = lastName.trim();
        this.email = email.trim();
        this.grade = grade;
        this.subteam = "";
        this.eventID = "";
    }

    /**
     * Constructor to initialize this Member's instance fields without
     * parameters for subteam, eventID, grade, email, which will be set to
     * default temporary values
     *
     * @param firstName the String containing the first name of this Member
     * @param lastName the String containing the last name of this Member
     */
    public Member(String firstName, String lastName) {
        this();
        this.firstName = firstName.trim();
        this.lastName = lastName.trim();
    }

    /**
     * Constructor to initialize this Member's instance fields to default
     * temporary values.
     */
    public Member() {
        this.firstName = "";
        this.lastName = "";
        this.email = "";
        this.grade = 0;
        this.subteam = "";
        this.eventID = "";
    }

    /**
     * Override the default toString() method
     *
     * @return the Member's full name in the format (firstName lastName)
     */
    public String toString() {
        return getFullName();
    }

    public boolean equals(Member m) {
        boolean equals = false;
        if (firstName.equals(m.getFirstName()) && lastName.equals(m.getLastname()) && email.equals(m.getEmail()) 
                && grade == m.getGrade() && subteam.equals(m.getSubteam()) && eventID.equals(m.getEventID())) {
            equals = true;
        }
        return equals;
    }
    // Accessor methods

    /**
     * Accessor method to return the member's full name, email, and event ID in
     * a String. Used for searching purposes in TeamList
     *
     * @return the String containing the Member's full name, email, and event ID
     */
    public String getSearchString() {
        return firstName + " " + lastName + " " + email + " " + eventID;
    }

    /**
     * Accessor method to return this Member's email address
     *
     * @return this Member's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Accessor method to return this Member's DECA event ID
     *
     * @return this Member's event ID
     */
    public String getEventID() {
        return eventID;
    }

    /**
     * Accessor method to return this Member's first name
     *
     * @return this Member's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Accessor method to return this Member's full name in form firstName
     * lastName
     *
     * @return this Member's full name (firstName lastName)
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * Accessor method to return this Member's full name in form lastName
     * firstName
     *
     * @return this Member's full name (lastName firstName)
     */
    public String getFullNameLastFirst() {
        return lastName + " " + firstName;
    }

    /**
     * Accessor method to return this Member's grade level
     *
     * @return this Member's grade level as a primitive integer
     */
    public int getGrade() {
        return grade;
    }

    /**
     * Accessor method to return this Member's last name
     *
     * @return this Member's last name
     */
    public String getLastname() {
        return lastName;
    }

    /**
     * Accessor method to return this Member's subteam
     *
     * @return this Member's subteam
     */
    public String getSubteam() {
        return subteam;
    }

    // Mutator methods
    /**
     * Mutator method to set this Member's email address
     *
     * @param email the String containing this Member's email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Mutator method to set this Member's DECA event ID
     *
     * @param eventID the String containing this Member's event ID
     */
    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    /**
     * Mutator method to set this Member's first name
     *
     * @param firstName the String containing this Member's first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Mutator method to set this Member's grade level
     *
     * @param grade the integer containing this Member's grade level
     */
    public void setGrade(int grade) {
        this.grade = grade;
    }

    /**
     * Mutator method to set this Member's last name
     *
     * @param lastName the String containing this Member's last name
     */
    public void setLastname(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Mutator method to set this Member's subteam
     *
     * @param subteam the String containing this Member's subteam
     */
    public void setSubteam(String subteam) {
        this.subteam = subteam;
    }
}
