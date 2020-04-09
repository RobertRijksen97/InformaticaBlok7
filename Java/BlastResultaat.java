package ProjectBlok7;

public class BlastResultaat extends Resultaat{
    String description;
    String eValue;
    String organism;

    public int getPercIdentity() {
        return percIdentity;
    }

    public void setPercIdentity(int percIdentity) {
        this.percIdentity = percIdentity;
    }

    int percIdentity;

    public String getAccesion() {
        return accesion;
    }

    public void setAccesion(String accesion) {
        this.accesion = accesion;
    }

    String accesion;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String geteValue() {
        return eValue;
    }

    public void seteValue(String eValue) {
        this.eValue = eValue;
    }

    public String getOrganism() {
        return organism;
    }

    public void setOrganism(String organism) {
        this.organism = organism;
    }

}
