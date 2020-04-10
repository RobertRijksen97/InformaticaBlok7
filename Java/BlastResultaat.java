public class BlastResultaat extends Resultaat{
    String description;
    String eValue;
    String organism;

    /**
     * returns percent identity.
     * @return integer percIdentity.
     */
    public int getPercIdentity() {
        return percIdentity;
    }

    /**
     * sets percent identity.
     * @param percIdentity int percent identity.
     */
    public void setPercIdentity(int percIdentity) {
        this.percIdentity = percIdentity;
    }

    int percIdentity;

    /**
     * returns accesie code.
     * @return string accessiecode.
     */
    public String getAccesion() {
        return accesion;
    }

    /**
     * sets accesion code.
     * @param accesion string accesion code.
     */
    public void setAccesion(String accesion) {
        this.accesion = accesion;
    }

    String accesion;

    /**
     * returns description.
     * @return string containing description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * sets description
     * @param description sets the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * returns evalue.
     * @return String evalue.
     */
    public String geteValue() {
        return eValue;
    }

    /**
     * sets the evalue.
     * @param eValue string evalue.
     */
    public void seteValue(String eValue) {
        this.eValue = eValue;
    }

    /**
     * returns organism name.
     * @return string containing an organism name.
     */
    public String getOrganism() {
        return organism;
    }

    /**
     * sets orgranism name
     * @param organism string organism.
     */
    public void setOrganism(String organism) {
        this.organism = organism;
    }

}
