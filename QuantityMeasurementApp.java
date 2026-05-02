public class QuantityMeasurementApp {

    public enum Category {
        LENGTH, WEIGHT
    }

    public enum Unit {
        // Length Units (Base: Inch)
        FEET(Category.LENGTH, 12.0), 
        INCH(Category.LENGTH, 1.0), 
        YARD(Category.LENGTH, 36.0), 
        CM(Category.LENGTH, 0.393701),
        
        // Weight Units (Base: Gram)
        KG(Category.WEIGHT, 1000.0), 
        GRAM(Category.WEIGHT, 1.0), 
        POUND(Category.WEIGHT, 453.592);

        public final Category category;
        public final double conversionFactor;

        Unit(Category category, double conversionFactor) {
            this.category = category;
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
            if (this.unit.category != that.unit.category) return false;
            return Math.abs(this.unit.toBaseUnit(this.value) - 
                            that.unit.toBaseUnit(that.value)) < 1e-6;
        }

        public Quantity convertTo(Unit targetUnit) {
            if (this.unit.category != targetUnit.category) {
                throw new IllegalArgumentException("Cannot convert between different categories: " 
                                                   + this.unit.category + " and " + targetUnit.category);
            }
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
            if (this.unit.category != other.unit.category || this.unit.category != targetUnit.category) {
                throw new IllegalArgumentException("All units must be in the same category");
            }
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
        System.out.println("Starting UC9: Weight Measurement Equality, Conversion, and Addition (Kilogram, Gram, Pound)");

        Quantity oneKg = new Quantity(1.0, Unit.KG);
        Quantity thousandGrams = new Quantity(1000.0, Unit.GRAM);
        System.out.println("1 kg == 1000 g: " + oneKg.equals(thousandGrams));

        Quantity onePound = new Quantity(1.0, Unit.POUND);
        System.out.println("1 pound in grams: " + onePound.convertTo(Unit.GRAM));

        Quantity sumWeight = oneKg.add(onePound, Unit.KG);
        System.out.println("1 kg + 1 pound in kg: " + sumWeight);

        // Category safety test
        try {
            oneKg.equals(new Quantity(1.0, Unit.FEET));
            System.out.println("Test Category Safety (1 kg vs 1 ft): false (Expected)");
        } catch (Exception e) {
            System.out.println("Caught unexpected error in equals: " + e.getMessage());
        }

        try {
            oneKg.add(new Quantity(1.0, Unit.FEET));
        } catch (IllegalArgumentException e) {
            System.out.println("Caught expected error adding kg and feet: " + e.getMessage());
        }

        System.out.println("UC9 Verification Complete.");
    }
}
