package ru.abarigena.NauJava.DTO;

import java.util.List;

public class HallRequestDto {
    private String name;
    private boolean active;
    private List<RowRequestDto> rows;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<RowRequestDto> getRows() {
        return rows;
    }

    public void setRows(List<RowRequestDto> rows) {
        this.rows = rows;
    }
}

