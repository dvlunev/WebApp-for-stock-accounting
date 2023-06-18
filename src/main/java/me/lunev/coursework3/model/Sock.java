package me.lunev.coursework3.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;

/**
 * The class describes a Sock
 */
@Getter
@ToString
@NoArgsConstructor
public class Sock {

    private String color;
    private float size;
    private int cottonPart;
    private int quantity;
    private boolean stockAvailability;

    public Sock(String color, float size, int cottonPart, int quantity, boolean stockAvailability) {
        Color colorType = Color.findByTextColor(color);
        if (colorType == null) {
            throw new IllegalArgumentException("Параметры запроса отсутствуют или имеют некорректный формат");
        }
        this.color = color;

        Size sizeType = Size.findByNumberSize(size);
        if (sizeType == null) {
            throw new IllegalArgumentException("Параметры запроса отсутствуют или имеют некорректный формат");
        }
        this.size = size;

        if (cottonPart < 0 || cottonPart > 100) {
            throw new IllegalArgumentException("Параметры запроса отсутствуют или имеют некорректный формат");
        }
        this.cottonPart = cottonPart;

        if (quantity <= 0) {
            throw new IllegalArgumentException("Параметры запроса отсутствуют или имеют некорректный формат");
        }
        this.quantity = quantity;

        if (!stockAvailability) {
            throw new IllegalArgumentException("Параметры запроса отсутствуют или имеют некорректный формат");
        }
        this.stockAvailability = true;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setStockAvailability(boolean stockAvailability) {
        this.stockAvailability = stockAvailability;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sock sock = (Sock) o;
        return Float.compare(sock.size, size) == 0 && cottonPart == sock.cottonPart && Objects.equals(color, sock.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, size, cottonPart);
    }
}
