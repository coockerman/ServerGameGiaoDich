package ServerNew.Model;

public class Resource {
    private int quantity;
    private float priceBuy;
    private float priceSell;
    private int defaultQuantity; // Số lượng mặc định để so sánh
    private float priceAdjustmentRate; // Tốc độ thay đổi giá
    private final float basePriceBuy;  // Giá mua gốc
    private final float basePriceSell; // Giá bán gốc

    public Resource(int quantity, float priceBuy, float priceSell, int defaultQuantity, float priceAdjustmentRate) {
        this.quantity = quantity;
        this.priceBuy = priceBuy;
        this.priceSell = priceSell;
        this.defaultQuantity = defaultQuantity;
        this.priceAdjustmentRate = priceAdjustmentRate;
        this.basePriceBuy = priceBuy;  // Lưu giá gốc
        this.basePriceSell = priceSell; // Lưu giá gốc
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPriceBuy() {
        return priceBuy;
    }

    public void setPriceBuy(float priceBuy) {
        this.priceBuy = priceBuy;
    }

    public float getPriceSell() {
        return priceSell;
    }

    public void setPriceSell(float priceSell) {
        this.priceSell = priceSell;
    }

    public void increaseQuantity(int amount) {
        this.quantity += amount;
        updatePrices();
    }

    public boolean decreaseQuantity(int amount) {
        if (this.quantity >= amount) {
            this.quantity -= amount;
            updatePrices();
            return true;
        } else {
            return false;
        }
    }

    private void updatePrices() {
        // Nếu quantity là 0, giả sử supplyDemandRatio = 1 để tránh chia cho 0
        float supplyDemandRatio = (quantity == 0) ? 0.01f : (float) quantity / defaultQuantity;

        float power = 2.0f; // Điều chỉnh tốc độ thay đổi giá

        if (supplyDemandRatio > 1) {
            // Cung vượt cầu: giảm giá theo cấp số nhân
            priceBuy = basePriceBuy * (float) Math.pow(supplyDemandRatio, -power);
            priceSell = basePriceSell * (float) Math.pow(supplyDemandRatio, -power);
        } else {
            // Cầu vượt cung: tăng giá theo cấp số nhân
            priceBuy = basePriceBuy * (float) Math.pow(1 / supplyDemandRatio, power);
            priceSell = basePriceSell * (float) Math.pow(1 / supplyDemandRatio, power);
        }

        // Làm tròn giá thành số nguyên
        priceBuy = Math.round(priceBuy);
        priceSell = Math.round(priceSell);

        // Đảm bảo giá không âm
        priceBuy = Math.max(priceBuy, 1);  // Đảm bảo giá không dưới 1
        priceSell = Math.max(priceSell, 1);

        System.out.println(this.toString());
    }

    @Override
    public String toString() {
        return String.format(
                "Resource [Quantity: %d, PriceBuy: %.2f, PriceSell: %.2f, DefaultQuantity: %d, PriceAdjustmentRate: %.2f%%]",
                quantity, priceBuy, priceSell, defaultQuantity, priceAdjustmentRate * 100
        );
    }
}

