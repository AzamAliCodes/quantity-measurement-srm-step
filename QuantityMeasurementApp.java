public class QuantityMeasurementApp {

    public enum Unit {
        FEET(12.0), INCH(1.0);

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
            return Double.compare(this.value * this.unit.conversionFactor, 
                                  that.value * that.unit.conversionFactor) == 0;
        }

        @Override
        public String toString() {
            return value + " " + unit;
        }
    }

    public static void main(String[] args) {
        System.out.println("Starting UC3: Generic Quantity Class for DRY Principle");

        Quantity oneFoot = new Quantity(1.0, Unit.FEET);
        Quantity twelveInches = new Quantity(12.0, Unit.INCH);

        System.out.println("Test 1.0 ft == 12.0 in: " + oneFoot.equals(twelveInches));
        System.out.println("Test 1.0 ft != 1.0 in: " + !oneFoot.equals(new Quantity(1.0, Unit.INCH)));

        System.out.println("UC3 Verification Complete.");
    }
}
