public class QuantityMeasurementApp {

    public enum Unit {
        FEET(12.0), INCH(1.0), YARD(36.0), CM(0.393701);

        public final double conversionFactor;

        Unit(double conversionFactor) {
            this.conversionFactor = conversionFactor;
        }

        public double toBaseUnit(double value) {
            return value * conversionFactor;
        }

        public double fromBaseUnit(double valueInBase) {
            return valueInBase / conversionFactor;
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
            return Math.abs(this.unit.toBaseUnit(this.value) - 
                            that.unit.toBaseUnit(that.value)) < 1e-6;
        }

        public Quantity convertTo(Unit targetUnit) {
            double valueInBase = this.unit.toBaseUnit(this.value);
            double convertedValue = targetUnit.fromBaseUnit(valueInBase);
            return new Quantity(convertedValue, targetUnit);
        }

        public Quantity add(Quantity other) {
            return add(other, this.unit);
        }

        public Quantity add(Quantity other, Unit targetUnit) {
            if (other == null) throw new IllegalArgumentException("Operand cannot be null");
            if (targetUnit == null) throw new IllegalArgumentException("Target unit cannot be null");
            double sumInBase = this.unit.toBaseUnit(this.value) + other.unit.toBaseUnit(other.value);
            double sumInTargetUnit = targetUnit.fromBaseUnit(sumInBase);
            return new Quantity(sumInTargetUnit, targetUnit);
        }

        @Override
        public String toString() {
            return value + " " + unit;
        }
    }

    public static void main(String[] args) {
        System.out.println("Starting UC8: Refactoring Unit Enum to Standalone with Conversion Responsibility");

        Quantity oneFoot = new Quantity(1.0, Unit.FEET);
        Quantity twelveInches = new Quantity(12.0, Unit.INCH);

        System.out.println("1 foot == 12 inches: " + oneFoot.equals(twelveInches));
        System.out.println("1 foot converted to inches: " + oneFoot.convertTo(Unit.INCH));
        System.out.println("1 foot + 12 inches in yards: " + oneFoot.add(twelveInches, Unit.YARD));

        System.out.println("UC8 Verification Complete.");
    }
}
