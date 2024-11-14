package ru.abarigena.NauJava.DTO;

public class RowRequestDto {
    private int row;
    private int seatCount;

    public RowRequestDto() {
    }

    public RowRequestDto(int row, int seatCount) {
        this.row = row;
        this.seatCount = seatCount;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(int seatCount) {
        this.seatCount = seatCount;
    }
}
