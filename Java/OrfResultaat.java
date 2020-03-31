public class OrfResultaat extends Resultaat {
    int start;
    int stop;

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getStop() {
        return stop;
    }

    public void setStop(int stop) {
        this.stop = stop;
    }
    OrfResultaat(int start, int stop){
        this.start=start;
        this.stop=stop;
    }
}
