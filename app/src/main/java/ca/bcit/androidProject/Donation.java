package ca.bcit.androidProject;

public class Donation {
    private String userID;
    private String donation;

    public Donation() {
    }

    public Donation(String userID, String donation) {
        this.userID = userID;
        this.donation = donation;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDonation() {
        return donation;
    }

    public void setDonation(String donation) {
        this.donation = donation;
    }
}
