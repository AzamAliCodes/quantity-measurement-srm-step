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
            if (unit == null) throw new IllegalArgumentException("Unit cannot be null");
            if (!Double.isFinite(value)) throw new IllegalArgumentException("Value must be finite");
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

        public Quantity convertTo(Unit targetUnit) {
            double convertedValue = (this.value * this.unit.conversionFactor) / targetUnit.conversionFactor;
            return new Quantity(convertedValue, targetUnit);
        }

        public Quantity add(Quantity other) {
            return add(other, this.unit);
        }

        public Quantity add(Quantity other, Unit targetUnit) {
            if (other == null) throw new IllegalArgumentException("Operand cannot be null");
            if (targetUnit == null) throw new IllegalArgumentException("Target unit cannot be null");
            double sumInInches = (this.value * this.unit.conversionFactor) + (other.value * other.unit.conversionFactor);
            double sumInTargetUnit = sumInInches / targetUnit.conversionFactor;
            return new Quantity(sumInTargetUnit, targetUnit);
        }

        @Override
        public String toString() {
            return value + " " + unit;
        }
    }

    public static void main(String[] args) {
        System.out.println("Starting UC7: Addition with Target Unit Specification");

        Quantity oneFoot = new Quantity(1.0, Unit.FEET);
        Quantity twelveInches = new Quantity(12.0, Unit.INCH);

        // Standard add (uses first operand's unit)
        System.out.println("1 foot + 12 inches = " + oneFoot.add(twelveInches));

        // Add with target unit
        Quantity sumInInches = oneFoot.add(twelveInches, Unit.INCH);
        System.out.println("1 foot + 12 inches in inches = " + sumInInches);
        System.out.println("Test sum == 24.0 in: " + sumInInches.equals(new Quantity(24.0, Unit.INCH)));

        Quantity twoInches = new Quantity(2.0, Unit.INCH);
        Quantity fiveCm = new Quantity(5.0, Unit.CM);
        System.out.println("2 inches + 5 cm in inches = " + twoInches.add(fiveCm, Unit.INCH));

        System.out.println("UC7 Verification Complete.");
    }
}
