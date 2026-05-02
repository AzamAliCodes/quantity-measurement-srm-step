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
            if (other == null) throw new IllegalArgumentException("Operand cannot be null");
            double sumInInches = (this.value * this.unit.conversionFactor) + (other.value * other.unit.conversionFactor);
            double sumInThisUnit = sumInInches / this.unit.conversionFactor;
            return new Quantity(sumInThisUnit, this.unit);
        }

        @Override
        public String toString() {
            return value + " " + unit;
        }
    }

    public static void main(String[] args) {
        System.out.println("Starting UC6: Addition of Two Length Units");

        Quantity oneFoot = new Quantity(1.0, Unit.FEET);
        Quantity twelveInches = new Quantity(12.0, Unit.INCH);

        System.out.println("1 foot + 12 inches = " + oneFoot.add(twelveInches));
        System.out.println("Test sum == 2.0 ft: " + oneFoot.add(twelveInches).equals(new Quantity(2.0, Unit.FEET)));

        System.out.println("UC6 Verification Complete.");
    }
}
