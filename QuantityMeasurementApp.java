public class QuantityMeasurementApp {

    public enum Unit {
        FEET(12.0), INCH(1.0), YARD(36.0), CM(0.393701);

        public final double conversionFactor;

        Unit(double conversionFactor) {
            this.conversionFactor = conversionFactor;
        }
    }

    public static class Quantity {
        private final double value;
        private final Unit unit;

        public Quantity(double value, Unit unit) {
            this.value = value;
            this.unit = unit;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || !(obj instanceof Quantity)) return false;
            Quantity that = (Quantity) obj;
            return Math.abs(this.value * this.unit.conversionFactor - 
                            that.value * that.unit.conversionFactor) < 1e-6;
        }

        @Override
        public String toString() {
            return value + " " + unit;
        }
    }

    public static void main(String[] args) {
        System.out.println("Starting UC4: Extended Unit Support");

        Quantity oneYard = new Quantity(1.0, Unit.YARD);
        Quantity threeFeet = new Quantity(3.0, Unit.FEET);
        System.out.println("Test 1.0 yd == 3.0 ft: " + oneYard.equals(threeFeet));

        Quantity oneCm = new Quantity(1.0, Unit.CM);
        Quantity pointThreeNineInches = new Quantity(0.393701, Unit.INCH);
        System.out.println("Test 1.0 cm == 0.393701 in: " + oneCm.equals(pointThreeNineInches));

        System.out.println("UC4 Verification Complete.");
    }
}
