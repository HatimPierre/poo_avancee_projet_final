package imgedit.filters;

import javax.swing.*;

public interface Jobable {
    class Bounds{
        public int wLowB;
        public int wHighB;
        public int hLowB;
        public int hHighB;

        public Bounds(int wLowB, int wHighB, int hLowB, int hHighB){
            this.hHighB = hHighB;
            this.hLowB = hLowB;
            this.wHighB = wHighB;
            this.wLowB = wLowB;
      }
    }
    public void setJobSize(int jobSize);
    public void setPreviewFrame(JFrame preview);
}
