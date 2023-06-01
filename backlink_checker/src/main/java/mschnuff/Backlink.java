package mschnuff;

public class Backlink {
    //string "Domain", int "Domainrank", string "Origin", string "Target", boolean "Nofollow", string "Anchor",boolean "Deleted"
    String domain; // the domain that contains the backlink
    int domainrank; // the ranking of the domain
    String origin; // the specific url that contains the backlink
    String target; // the url of the backlink
    boolean nofollow; // instruction for search engines (follow this link or not)
    String anchor; // the frontend text of the backlink (readable for the user)
    boolean deleted; // indicates whether or not the backlink exists
    public Backlink() {
        // no init atm
    }
    public void setDomainRank(String rankAsString) {
        try {
            int rankAsInt = Integer.parseInt(rankAsString);
            this.domainrank = rankAsInt;            
        }
        catch (NumberFormatException ex){
            ex.printStackTrace();
        }
    }
    public void setNoFollow(String noFollowAsString) {
        try {
            int nf = Integer.parseInt(noFollowAsString);
            boolean noFollowAsBool = false;
            if(nf != 0) {
                noFollowAsBool = true;
            }             
            this.nofollow = noFollowAsBool;                      
        }
        catch (NumberFormatException ex){
            ex.printStackTrace();
        }
    }
    public void setDeleted(String deletedAsString) {
        try {
            int del = Integer.parseInt(deletedAsString);
            boolean deletedAsBool = false;
            if(del != 0) {
               deletedAsBool = true;
            }             
            this.deleted = deletedAsBool;                      
        }
        catch (NumberFormatException ex){
            ex.printStackTrace();
        }
    }         
}
