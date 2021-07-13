
import java.util.Comparator;

public class Gerate {

    private int bauteil = -1;
    private String name = "";
    private int slot_id = 1;
    private int benutzer_id = 15;
    private String auftrag = "";
    private String ergebnis = "Kein Ergebnis";
    private String vortest_ergebnis = "Kein Ergebnis";
    private String haupttest_ergebnis = "Kein Ergebnis";

    public Gerate(int bauteil, String name, String auftrag, int slot_id, int benutzer_id,
            String vortest_ergebnis, String haupttest_ergebnis) {
        this.bauteil = bauteil;
        this.name = name;
        this.slot_id = slot_id;
        this.benutzer_id = benutzer_id;
        this.auftrag = auftrag;
        this.vortest_ergebnis = vortest_ergebnis;
        this.haupttest_ergebnis = haupttest_ergebnis;
    }
    public Gerate(){
        
    }
    @Override
    public String toString() {
        return slot_id + " " + bauteil + " " + auftrag + " " + vortest_ergebnis + " " + haupttest_ergebnis;
    }

    public String getVortest_ergebnis() {
        return vortest_ergebnis;
    }

    public void setVortest_ergebnis(String vortest_ergebnis) {
        this.vortest_ergebnis = vortest_ergebnis;
    }

    public String getHaupttest_ergebnis() {
        return haupttest_ergebnis;
    }

    public void setHaupttest_ergebnis(String nachtest_ergebnis) {
        this.haupttest_ergebnis = nachtest_ergebnis;
    }

    public int[] getSlot_bauteil_id() {
        int[] array = new int[]{slot_id, bauteil};
        return array;
    }

    public String getErgebnis() {
        return ergebnis;
    }

    public void setErgebnis(String ergebnis) {
        this.ergebnis = ergebnis;
    }

    public int getBauteil() {
        return bauteil;
    }

    public void setBauteil(int bauteil) {
        this.bauteil = bauteil;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSlot_id() {
        return slot_id;
    }

    public void setSlot_id(int slot_id) {
        this.slot_id = slot_id;
    }

    public int getBenutzer_id() {
        return benutzer_id;
    }

    public void setBenutzer_id(int benutzer_id) {
        this.benutzer_id = benutzer_id;
    }

    public String getAuftrag() {
        return auftrag;
    }

    public void setAuftrag(String auftrag) {
        this.auftrag = auftrag;
    }

}
//ürünlerin tabloda slot numarasına göre sıralı tutulabilmesi için gerekli comparator

class sortGerate implements Comparator<Gerate> {

    @Override
    public int compare(Gerate o1, Gerate o2) {
        if (o1.getSlot_id() < o2.getSlot_id()) {
            return -1;

        } else if (o1.getSlot_id() > o2.getSlot_id()) {
            return 15;
        }
        return 0;

    }
}
